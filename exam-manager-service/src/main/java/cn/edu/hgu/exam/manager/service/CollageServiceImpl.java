package cn.edu.hgu.exam.manager.service;

import cn.edu.hgu.exam.bean.*;
import cn.edu.hgu.exam.manager.mapper.CollageMapper;
import cn.edu.hgu.exam.manager.mapper.CollectMapper;
import cn.edu.hgu.exam.manager.mapper.ThreeYearInfoMapper;
import cn.edu.hgu.exam.service.CollageService;
import cn.edu.hgu.exam.service.StaticDataService;
import cn.edu.hgu.util.RedisUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.*;

@Service
@Transactional
public class CollageServiceImpl implements CollageService {

    @Autowired
    CollageMapper collageMapper;

    @Autowired
    StaticDataService staticDataService;

    @Autowired
    CollectMapper collectMapper;

    @Autowired
    ThreeYearInfoMapper threeYearInfoMapper;

    @Autowired
    RedisUtil redisUtil;


    /**
     * 条件查询学校信息
     * @param universityParamsDTO
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<ExamUniversity> searchCollageByExample(ExamUniversity universityParamsDTO,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ExamUniversity> universityDTOS = collageMapper.select(universityParamsDTO);

        //遍历返回结果，将字段值转化为对应的属性名称
        universityDTOS.stream().forEach(item -> {
            item.setExUniProvince(staticDataService.transferStaticFieldSpec(item.getExUniProvince()));
            item.setExUniCity(staticDataService.transferStaticFieldValue(item.getExUniCity()));
            item.setExUniType(staticDataService.transferStaticFieldValue(item.getExUniType()));
            item.setExUniNature(staticDataService.transferStaticFieldValue(item.getExUniNature()));
            item.setExUniLevel(staticDataService.transferStaticFieldValue(item.getExUniLevel()));
            item.setExUniGrade(staticDataService.transferStaticFieldValue(item.getExUniGrade()));
        });

        PageInfo<ExamUniversity> examUniversityPageInfo = new PageInfo<>(universityDTOS);
        //return universityDTOS;
        return examUniversityPageInfo.getList();
    }

    @Override
    public ExamUniversity getOneCollage(String exUniCode) {
        ExamUniversity examUniversity = new ExamUniversity();
        examUniversity.setExUniCode(exUniCode);
        ExamUniversity university = collageMapper.selectOne(examUniversity);
        return university;
    }

    @Override
    public Integer getCountByExample(ExamUniversity universityDTO) {
        if(universityDTO == null){
            return -1;
        }
        Integer count = collageMapper.selectCount(universityDTO);
        return count;
    }

    /**
     * 查询学校近三年学校信息，做可视化处理
     * @param exUniCode
     * @return
     */
    @Override
    public Map<String, Object> getUniThreeYearMsg(String exUniCode) {

        //定义一个存放结果的大map
        Map<String,Object> result = new HashMap<>();
        if(StringUtils.isBlank(exUniCode)){
            result.put("code","1111");
            result.put("msg","查询失败：参数为空!");
            result.put("data",null);
        }

        ExamThreeYearInfo examThreeYearInfo = new ExamThreeYearInfo();
        examThreeYearInfo.setExUniCode(exUniCode);

        ExamThreeYearInfo threeInfo = threeYearInfoMapper.selectOne(examThreeYearInfo);
        //先处理近三年分数信息
        //定义一个小map ，近三年分数信息
        Map<String,Object> scoreMap = new HashMap<>();
        scoreMap.put("firstYearScore",threeInfo.getExUniRecentFirst());
        scoreMap.put("secondYearScore",threeInfo.getExUniRecentSecond());
        scoreMap.put("thirdYearScore",threeInfo.getExUniRecentThird());

        //接着处理申请人数
        Map<String,Object> applyMap = new HashMap<>();
        String exUniApply = threeInfo.getExUniApply();
        String[] applyArray = exUniApply.split(",");
        applyMap.put("firstYearApply",Integer.valueOf(applyArray[0]));
        applyMap.put("secondYearApply",Integer.valueOf(applyArray[1]));
        applyMap.put("thirdYearApply",Integer.valueOf(applyArray[2]));

        //处理报考人数
        Map<String,Object> enrollmentMap = new HashMap<>();
        String exUniEnrollment = threeInfo.getExUniEnrollment();
        String[] enrollmentArray = exUniEnrollment.split(",");
        enrollmentMap.put("firstYearEnrollment",Integer.valueOf(enrollmentArray[0]));
        enrollmentMap.put("secondYearEnrollment",Integer.valueOf(enrollmentArray[1]));
        enrollmentMap.put("thirdYearEnrollment",Integer.valueOf(enrollmentArray[2]));

        //处理录取人数
        Map<String,Object> admissionMap = new HashMap<>();
        String exUniAdmission = threeInfo.getExUniAdmission();
        String[] admissionArray = exUniAdmission.split(",");
        admissionMap.put("firstYearAdmission",Integer.valueOf(admissionArray[0]));
        admissionMap.put("secondYearAdmission",Integer.valueOf(admissionArray[1]));
        admissionMap.put("thirdYearAdmission",Integer.valueOf(admissionArray[2]));

        //这里需要查一下学校信息
        ExamUniversity examUniversity = new ExamUniversity();
        examUniversity.setExUniCode(exUniCode);
        ExamUniversity university = collageMapper.selectOne(examUniversity);

        //处理最终结果
        result.put("scoreMap",scoreMap);
        result.put("applyMap",applyMap);
        result.put("enrollmentMap",enrollmentMap);
        result.put("admissionMap",admissionMap);
        result.put("uniMsg",university);

        return result;
    }

    /**
     * 根据城市编码，查询学校列表
     * @return
     */
    @Override
    public Map<String, Object> getCollageByCityCode(String exUniCity) {
        Map<String,Object> result = new HashMap<>();
        if(StringUtils.isBlank(exUniCity)){
            result.put("code","1111");
            result.put("msg","查询失败，参数异常");
            result.put("data",null);
        }

        ExamUniversity e = new ExamUniversity();
        e.setExUniCity(exUniCity);
        List<ExamUniversity> universities = collageMapper.select(e);

        if(universities != null){
            result.put("code","0000");
            result.put("msg","查询成功");
            result.put("data",universities);
        }else {
            result.put("code","1111");
            result.put("msg","查询失败，系统异常");
            result.put("data",universities);
        }

        return result;
    }

    /**
     * 在缓冲中设置分数排名列表，用于模拟位次
     * @return
     */
    @Override
    public Boolean setScoreMsgList() {
        Jedis jedis = null;
        List<Integer> scoreList = new ArrayList<>();
        try {
            jedis = redisUtil.getJedis();
            if(jedis != null){
                Integer min = 200;
                Integer max = 700;
                for(int i = 0; i < 5000;i++){
                    Integer score = new Random().nextInt(max - min) + min;
                    scoreList.add(score);
                }
                jedis.setex("SCORELIST",60*60*24, JSON.toJSONString(scoreList));  //第一次登录时就存储进缓存
            }
        }finally {
            jedis.close();
        }
        return true;
    }

    /**
     * 根据用户的总成绩计算排名 返回排名即可
     * @param sumScore
     * @return
     */
    @Override
    public Integer getScorePosition(Integer sumScore) {
        //调用缓存
        Jedis jedis = null;
        Integer result = 0;
        try {
            jedis = redisUtil.getJedis();
            if(jedis != null){
                String scorelist = jedis.get("SCORELIST");
                List<Integer> parseArray = JSONObject.parseArray(scorelist, Integer.class);
                parseArray.add(sumScore);
                Collections.sort(parseArray, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o2 - o1;
                    }
                });
                result = parseArray.indexOf(sumScore);
            }
        }finally {
            jedis.close();
        }
        return result + 1;
    }
}
