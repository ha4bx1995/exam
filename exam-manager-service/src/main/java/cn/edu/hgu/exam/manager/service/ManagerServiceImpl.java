package cn.edu.hgu.exam.manager.service;

import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.bean.ExamUserPerformance;
import cn.edu.hgu.exam.bean.ScoreDTO;
import cn.edu.hgu.exam.bean.UniversityDTO;
import cn.edu.hgu.exam.manager.mapper.ManagerMapper;
import cn.edu.hgu.exam.service.ManagerService;
import cn.edu.hgu.exam.service.StaticDataService;
import cn.edu.hgu.util.RedisUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ManagerServiceImpl<T> implements ManagerService {


    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    StaticDataService staticDataService;


    /**
     * 学生用户个性化定制服务
     */
    @Override
    public Boolean specialConfForStudentUser(String userId) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String userInfo = jedis.get("userInfo" + userId + "info");
            if(StringUtils.isNotBlank(userInfo)){
                ExamUser examUserInfo = JSON.parseObject(userInfo, ExamUser.class);
                Boolean isStudent = examUserInfo.getIsStudent().equals("1") ? true : false;

                return isStudent;
            }

        }finally {
            jedis.close();
        }
        return false;
    }

    /**
     * 从缓存中获取用户信息
     */
    @Override
    public ExamUser getUserInfoFromCache(String userId){
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String userInfo = jedis.get("userInfo" + userId + "info");
            if(StringUtils.isNotBlank(userInfo)){
                ExamUser examUser = JSON.parseObject(userInfo, ExamUser.class);
                return  examUser;
            }
        }finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 根据用户输入的分数 推荐合适的学校
     * @param scoreDTO
     * @param userId
     * @param isFirstLogin
     * @return
     */
    @Override
    public Map<String, Object> getRecommendFitCollage(ScoreDTO scoreDTO,String userId,Boolean isFirstLogin) {

        Map<String,Object> result = new HashMap<>();
        if(scoreDTO == null){
            result.put("code","1111");
            result.put("msg","参数为空！！");
            result.put("data",null);
        }
        //准备参数
        Integer chinese = scoreDTO.getChinese();
        Integer math = scoreDTO.getMath();
        Integer foreign = scoreDTO.getForign();
        Integer multiple = scoreDTO.getMultiple();
        String artOrScience = scoreDTO.getArtAndScience();

        Integer sumScore = chinese + math + foreign + multiple;
        //上下合适的学校
        //计算上限和下限
        Integer upperLimit = sumScore + 10;
        Integer lowerLimit = sumScore - 10;
        List<UniversityDTO> universityDTOSFit = managerMapper.selectFitCollage(upperLimit, lowerLimit, artOrScience);

        universityDTOSFit.forEach(item -> {
            item.setStatus("合适");
            item.setExUniProvince(staticDataService.transferStaticFieldSpec(item.getExUniProvince()));
            item.setExUniCity(staticDataService.transferStaticFieldValue(item.getExUniCity()));
            item.setExUniType(staticDataService.transferStaticFieldValue(item.getExUniType()));
            item.setExUniNature(staticDataService.transferStaticFieldValue(item.getExUniNature()));
            item.setExUniLevel(staticDataService.transferStaticFieldValue(item.getExUniLevel()));
            item.setExUniGrade(staticDataService.transferStaticFieldValue(item.getExUniGrade()));
        });
        //对英文成绩比较差的，将外语类学校过滤掉
        if(scoreDTO.getForign() < 60){
            universityDTOSFit = universityDTOSFit.stream().filter(item -> (!item.getExUniType().equals("语言类"))).collect(Collectors.toList());
        }
        //数学低于60分  或者是文科学生，理工类学校 过滤掉
        if(scoreDTO.getMath() < 60 || scoreDTO.getArtAndScience().equals("0")){
            universityDTOSFit = universityDTOSFit.stream().filter(item -> (!item.getExUniType().equals("理工类"))).collect(Collectors.toList());
        }
        //如果是理科学生，将艺术类过滤掉
        if(scoreDTO.getArtAndScience().equals("1")){
            universityDTOSFit = universityDTOSFit.stream().filter(item -> (!item.getExUniType().equals("艺术类"))).collect(Collectors.toList());
        }

        universityDTOSFit = subListNew(universityDTOSFit);


        //求稳的学校 用当前的总分数，与学校的最高分比较，如果总分数大于最高分+10 那可以认为是求稳
        List<UniversityDTO> universityDTOSStability = managerMapper.selectStabilityCollage(sumScore, artOrScience);

        universityDTOSStability.forEach(item -> {
            item.setStatus("求稳");
            item.setExUniProvince(staticDataService.transferStaticFieldSpec(item.getExUniProvince()));
            item.setExUniCity(staticDataService.transferStaticFieldValue(item.getExUniCity()));
            item.setExUniType(staticDataService.transferStaticFieldValue(item.getExUniType()));
            item.setExUniNature(staticDataService.transferStaticFieldValue(item.getExUniNature()));
            item.setExUniLevel(staticDataService.transferStaticFieldValue(item.getExUniLevel()));
            item.setExUniGrade(staticDataService.transferStaticFieldValue(item.getExUniGrade()));
        });
        //对英文成绩比较差的，将外语类学校过滤掉
        if(scoreDTO.getForign() < 60){
            universityDTOSStability = universityDTOSStability.stream().filter(item -> (!item.getExUniType().equals("语言类"))).collect(Collectors.toList());
        }
        //数学低于60分  或者是文科学生，理工类学校 过滤掉
        if(scoreDTO.getMath() < 60 || scoreDTO.getArtAndScience().equals("0")){
            universityDTOSStability = universityDTOSStability.stream().filter(item -> (!item.getExUniType().equals("理工类"))).collect(Collectors.toList());
        }
        //如果是理科学生，将艺术类过滤掉
        if(scoreDTO.getArtAndScience().equals("1")){
            universityDTOSStability = universityDTOSStability.stream().filter(item -> (!item.getExUniType().equals("艺术类"))).collect(Collectors.toList());
        }

        universityDTOSStability = subListNew(universityDTOSStability);

        //冲刺的学校 用当前的总分数 与学校的最低分比较，如果总分数大于最低分 认为是冲刺
        List<UniversityDTO> universityDTOSSprint = managerMapper.selectSprintCollage(sumScore, artOrScience);

        universityDTOSSprint.forEach(item -> {
            item.setStatus("冲刺");
            item.setExUniProvince(staticDataService.transferStaticFieldSpec(item.getExUniProvince()));
            item.setExUniCity(staticDataService.transferStaticFieldValue(item.getExUniCity()));
            item.setExUniType(staticDataService.transferStaticFieldValue(item.getExUniType()));
            item.setExUniNature(staticDataService.transferStaticFieldValue(item.getExUniNature()));
            item.setExUniLevel(staticDataService.transferStaticFieldValue(item.getExUniLevel()));
            item.setExUniGrade(staticDataService.transferStaticFieldValue(item.getExUniGrade()));
        });
        //对英文成绩比较差的，将外语类学校过滤掉
        if(scoreDTO.getForign() < 60){
            universityDTOSSprint = universityDTOSSprint.stream().filter(item -> (!item.getExUniType().equals("语言类"))).collect(Collectors.toList());
        }
        //数学低于60分  或者是文科学生，理工类学校 过滤掉
        if(scoreDTO.getMath() < 60 || scoreDTO.getArtAndScience().equals("0")){
            universityDTOSSprint = universityDTOSSprint.stream().filter(item -> (!item.getExUniType().equals("理工类"))).collect(Collectors.toList());
        }
        //如果是理科学生，将艺术类过滤掉
        if(scoreDTO.getArtAndScience().equals("1")){
            universityDTOSSprint = universityDTOSSprint.stream().filter(item -> (!item.getExUniType().equals("艺术类"))).collect(Collectors.toList());
        }

        universityDTOSSprint = subListNew(universityDTOSSprint);

        universityDTOSFit.addAll(universityDTOSStability);
        universityDTOSFit.addAll(universityDTOSSprint);


        //只有学生用户首次登录才插入数据，只插一次，
        if(isFirstLogin){
            //从缓存中拿到用户信息
            Jedis jedis = null;
            ExamUser examUser = null;
            try{
                jedis = redisUtil.getJedis();
                if(jedis != null){
                    String userInfo = jedis.get("userInfo" + userId + "info");
                    if(StringUtils.isNotBlank(userInfo)){
                        examUser = JSON.parseObject(userInfo, ExamUser.class);
                    }
                }
            }finally {
                jedis.close();
            }

            //返回数据之前，需要将用户在首页输入的学生成绩插入学生成绩表
            ExamUserPerformance eup = new ExamUserPerformance();
            eup.setUserId(examUser.getUserId());
            eup.setUsename(examUser.getUsername());
            eup.setExChinese(chinese);
            eup.setExMath(math);
            eup.setExForeign(foreign);
            eup.setExMultiple(multiple);
            eup.setExArtOrSci(artOrScience);
            eup.setExTotalScore(sumScore);
            boolean success = managerMapper.insertStudentPerformance(eup) > 0;

            if(!success){
                result.put("data",null);
                result.put("msg","数据处理异常");
                result.put("count",0);
                result.put("code","1111");
            }
        }


        result.put("data",universityDTOSFit);
        result.put("msg","查询成功");
        result.put("count",universityDTOSFit.size());
        result.put("code","0000");
        return result;
    }

    /**
     * 查询可以冲刺的学校
     * @param sumScore
     * @return
     */
    @Override
    public Map<String, Object> normalRecommendChongci(String sumScore) {
        Map<String,Object> result = new HashMap<>();
        if(StringUtils.isBlank(sumScore)){
            result.put("code","1111");
            result.put("msg","冲刺：参数为空！");
            result.put("data",null);
            result.put("count",0);
            return result;
        }
        List<UniversityDTO> universityDTOListChongci = managerMapper.selectChongciCollage(Integer.parseInt(sumScore));

        universityDTOListChongci = transfer(subListNew(universityDTOListChongci));

        if(universityDTOListChongci != null){
            result.put("code","0000");
            result.put("msg","冲刺列表：查询成功");
            result.put("data",universityDTOListChongci);
            result.put("count",universityDTOListChongci.size());
        }
        return result;
    }

    /**
     * 查询留作求稳的学校
     * @param sumScore
     * @return
     */
    @Override
    public Map<String, Object> normalRecommendQiuwen(String sumScore) {
        Map<String,Object> result = new HashMap<>();
        if(StringUtils.isBlank(sumScore)){
            result.put("code","1111");
            result.put("msg","求稳：参数为空！");
            result.put("data",null);
            result.put("count",0);
            return result;
        }
        List<UniversityDTO> universityDTOList = managerMapper.selectQiuwenCollage(Integer.parseInt(sumScore));
        //截取前五个元素
        universityDTOList = transfer(subListNew(universityDTOList));


        if(universityDTOList != null){
            result.put("code","0000");
            result.put("msg","求稳列表：查询成功");
            result.put("data",universityDTOList);
            result.put("count",universityDTOList.size());
        }

        return result;
    }

    /**
     * 查询比较合适的学校
     * @param sumScore
     * @return
     */
    @Override
    public Map<String, Object> normalRecommendHeshi(String sumScore) {
        Map<String,Object> result = new HashMap<>();
        if(StringUtils.isBlank(sumScore)){
            result.put("code","1111");
            result.put("msg","合适：参数为空！");
            result.put("data",null);
            result.put("count",0);
            return result;
        }
        Integer upperLimit = Integer.parseInt(sumScore) + 10;
        Integer lowerLimit = Integer.parseInt(sumScore) - 10;
        List<UniversityDTO> universityDTOList = managerMapper.selectHeshiCollage(upperLimit, lowerLimit);

        universityDTOList = transfer(subListNew(universityDTOList));

        if(universityDTOList != null){
            result.put("code","0000");
            result.put("msg","合适列表：查询成功");
            result.put("data",universityDTOList);
            result.put("count",universityDTOList.size());
        }

        return result;
    }

    /**
     * 获取前五个推荐学校即可
     * @param list
     * @return
     */
    private List subListNew(List list){
        if(list == null || list.size() <= 0){
            return new ArrayList(0);
        }
        if(list.size() >= 5){
            list = list.subList(0,5);
        }else if (list.size() > 0 && list.size() < 5) {
            list = list.subList(0,list.size() - 1);
        }else {
            list.clear();
        }
        return list;
    }

    /**
     * 转换编码
     * @param node
     * @return
     */
    private List<UniversityDTO> transfer(List<UniversityDTO> node){
        node.stream().forEach(item -> {
            item.setExUniProvince(staticDataService.transferStaticFieldSpec(item.getExUniProvince()));
            item.setExUniCity(staticDataService.transferStaticFieldValue(item.getExUniCity()));
            item.setExUniType(staticDataService.transferStaticFieldValue(item.getExUniType()));
            item.setExUniLevel(staticDataService.transferStaticFieldValue(item.getExUniLevel()));
            item.setExUniGrade(staticDataService.transferStaticFieldValue(item.getExUniGrade()));
            item.setExUniNature(staticDataService.transferStaticFieldValue(item.getExUniNature()));
        });
        return node;
    }


    /*************************测试方法**************************/
    @Override
    public String test() {
        return "test";
    }
}
