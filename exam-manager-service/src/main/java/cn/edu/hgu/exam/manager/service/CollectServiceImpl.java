package cn.edu.hgu.exam.manager.service;

import cn.edu.hgu.exam.bean.ExamCollection;
import cn.edu.hgu.exam.bean.UniversityDTO;
import cn.edu.hgu.exam.manager.mapper.CollectMapper;
import cn.edu.hgu.exam.manager.mapper.ManagerMapper;
import cn.edu.hgu.exam.service.CollectService;
import cn.edu.hgu.exam.service.StaticDataService;
import cn.edu.hgu.util.RedisUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户收藏服务实现
 */
@Service
@Transactional
public class CollectServiceImpl implements CollectService {

    @Autowired
    CollectMapper collectMapper;
    
    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    StaticDataService staticDataService;



    /**
     * 用户点击收藏功能
     * @param examCollection
     * @return
     */
    @Override
    public Map<String,Object> collectCollage(ExamCollection examCollection) {
        Map<String,Object> result = new HashMap<>();
        if(examCollection == null){
           result.put("resultCode","1111");
           result.put("resultMsg","参数为空,建议重新登录尝试");
           result.put("data",null);
        }
        Jedis jedis = null;
        boolean isSuccess = false;
        List<ExamCollection> examCollections = null;
        try {
            jedis = redisUtil.getJedis();
            String collectJson = jedis.get("USER_COLLECT" + examCollection.getUserId() + "INFO");
            if(StringUtils.isNotBlank(collectJson)){
                examCollections = JSONObject.parseArray(collectJson, ExamCollection.class);
            }else {
                ExamCollection collection = new ExamCollection();
                collection.setUserId(examCollection.getUserId());
                examCollections = collectMapper.select(collection);
            }
            //遍历整个收藏的集合，确认是否有重复收藏
            for(ExamCollection item : examCollections){
                if(examCollection.getExUniCode().equals(item.getExUniCode())){
                    result.put("resultCode","1111");
                    result.put("resultMsg","当前学校已经收藏了哦~~~");
                    result.put("data",null);
                    return result;
                }
            }
            isSuccess = collectMapper.insert(examCollection) > 0;
            ExamCollection e = new ExamCollection();
            e.setUserId(examCollection.getUserId());
            List<ExamCollection> collectionslist = collectMapper.select(e);
            jedis.setex("USER_COLLECT" + examCollection.getUserId() + "INFO",24*60*60, JSON.toJSONString(collectionslist));

        }finally {
            jedis.close();
        }
        if(isSuccess){
            result.put("resultCode","0000");
            result.put("resultMsg","收藏成功!");
            result.put("data",null);
        }
        return result;
    }

    /**
     * 查询当前登录用户下的收藏信息 实现
     * @param userId
     * @return
     */
    @Override
    public List<UniversityDTO> getCollectMsgByUserId(String userId,Integer pageNum,Integer pageSize) {
        if(userId == null){
            return null;
        }
        List<UniversityDTO> result = new ArrayList<>();
        List<ExamCollection> examCollections = null;
        //先从缓存中查询，如果缓存中没有，查询数据库,并且将数据放入缓存
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String collectJson = jedis.get("USER_COLLECT" + userId + "INFO");
            if(StringUtils.isNotBlank(collectJson)){
                examCollections = JSONObject.parseArray(collectJson, ExamCollection.class);
            }else {
                ExamCollection examCollection = new ExamCollection();           //如果缓存没有，就查询数据库
                examCollection.setUserId(userId);

                //从数据库查询用户的收藏信息
                examCollections = collectMapper.select(examCollection);
            }
            //遍历收藏列表，取学校编码，查询学校信息，封装，返回
            examCollections.stream().forEach(item -> {
                UniversityDTO universityDTO = managerMapper.selectUniByCode(item.getExUniCode());
                universityDTO.setColTime(item.getExColTime());
                universityDTO.setExUniProvince(staticDataService.transferStaticFieldSpec(universityDTO.getExUniProvince()));
                universityDTO.setExUniCity(staticDataService.transferStaticFieldValue(universityDTO.getExUniCity()));
                universityDTO.setExUniType(staticDataService.transferStaticFieldValue(universityDTO.getExUniType()));
                universityDTO.setExUniLevel(staticDataService.transferStaticFieldValue(universityDTO.getExUniLevel()));
                universityDTO.setExUniGrade(staticDataService.transferStaticFieldValue(universityDTO.getExUniGrade()));
                universityDTO.setExUniNature(staticDataService.transferStaticFieldValue(universityDTO.getExUniNature()));
                result.add(universityDTO);
            });
        }finally {
            jedis.close();
        }
        return result;
    }

    /**
     * 删除当前用户的收藏信息
     * @param exUniCode
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> cancelCollectCollage(String exUniCode, String userId) {
        Map<String,Object> result = new HashMap<>();
        if(StringUtils.isBlank(exUniCode)){
            result.put("resultCode","1111");
            result.put("resultMsg","参数为空！");
            result.put("data",null);
        }
        //调用删除服务
        ExamCollection examCollection = new ExamCollection();
        examCollection.setUserId(userId);
        examCollection.setExUniCode(exUniCode);
        int i = collectMapper.delete(examCollection);
        //数据库删除成功后，需要删除缓存
        Jedis jedis = null;

        try {
            jedis = redisUtil.getJedis();
            String collectJson = jedis.get("USER_COLLECT" + userId + "INFO");
            List<ExamCollection> examCollections = JSONObject.parseArray(collectJson, ExamCollection.class);
            List<ExamCollection> examCollectionsNew = examCollections.stream().filter(item -> (!item.getExUniCode().equals(exUniCode))).collect(Collectors.toList());
            //更新
            jedis.setex("USER_COLLECT" + userId + "INFO", 24 * 60 * 60, JSON.toJSONString(examCollectionsNew));
        }finally {
            jedis.close();
        }


        if(i > 0){
            result.put("resultCode","0000");
            result.put("resultMsg","删除成功！");
            result.put("data",null);
        }else {
            result.put("resultCode","1111");
            result.put("resultMsg","删除失败！");
            result.put("data",null);
        }

        return result;
    }
}
