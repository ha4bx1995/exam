package cn.edu.hgu.exam.manager.service;

import cn.edu.hgu.exam.bean.ExamUserPerformance;
import cn.edu.hgu.exam.bean.ScoreDTO;
import cn.edu.hgu.exam.manager.mapper.PerformanceMapper;
import cn.edu.hgu.exam.service.ManagerService;
import cn.edu.hgu.exam.service.PerformanceService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PerformanceServiceImpl implements PerformanceService {

    @Autowired
    PerformanceMapper performanceMapper;

    @Autowired
    ManagerService managerService;

    /**
     * 学生用户非首次登录获取学生成绩信息
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getUserPerformance(String userId) {

        ExamUserPerformance examUserPerformance = new ExamUserPerformance();
        examUserPerformance.setUserId(userId);
        //查询该用户成绩信息
        ExamUserPerformance userPerformance = performanceMapper.selectOne(examUserPerformance);

        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setChinese(userPerformance.getExChinese());
        scoreDTO.setMath(userPerformance.getExMath());
        scoreDTO.setForign(userPerformance.getExForeign());
        scoreDTO.setMultiple(userPerformance.getExMultiple());
        scoreDTO.setArtAndScience(userPerformance.getExArtOrSci());

        Map<String, Object> recommendFitCollage = null;
        //直接调用推荐学校服务接口，返回推荐学校信息
        if(userPerformance != null){
            recommendFitCollage = managerService.getRecommendFitCollage(scoreDTO, userId, false);
        }
        return recommendFitCollage;
    }

    /**
     * 根据用户id查询该用户的成绩信息
     * @param userId
     * @return
     */
    @Override
    public ExamUserPerformance getUserScoreMsg(String userId) {
        ExamUserPerformance e = new ExamUserPerformance();
        e.setUserId(userId);
        List<ExamUserPerformance> list = performanceMapper.select(e);
        if(!list.isEmpty()){
            return  list.get(0);
        }
        return null;
    }
}
