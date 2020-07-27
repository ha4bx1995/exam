package cn.edu.hgu.exam.service;

import cn.edu.hgu.exam.bean.ExamUserPerformance;

import java.util.Map;

/**
 * 学生成绩信息service接口
 */
public interface PerformanceService {
    /**
     * 学生用户非首次登录，进行学生成绩查询，以便于调用推荐接口
     * @param userId
     * @return
     */
    Map<String, Object> getUserPerformance(String userId);

    /**
     * 获取用户成绩信息
     * @param userId
     * @return
     */
    ExamUserPerformance getUserScoreMsg(String userId);
}
