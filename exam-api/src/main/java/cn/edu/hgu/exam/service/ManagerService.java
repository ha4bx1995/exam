package cn.edu.hgu.exam.service;

import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.bean.ScoreDTO;

import java.util.Map;

public interface ManagerService {
    //测试接口
    public String test();

    /**
     * 为学生用户个性化定制服务
     */
    Boolean specialConfForStudentUser(String userId);

    /**
     * 从缓存中获取用户信息
     * @param userId
     * @return
     */
    public ExamUser getUserInfoFromCache(String userId);

    /**
     * 根据用户输入的分数，推荐合适的学校
     * @param scoreDTO
     * @return
     */
    Map<String,Object> getRecommendFitCollage(ScoreDTO scoreDTO, String userId,Boolean isFirstLogin);

    /**
     * 查询可冲刺的学校
     * @param sumScore
     * @return
     */
    Map<String,Object> normalRecommendChongci(String sumScore);

    /**
     * 查询可留作求稳的学校
     * @param sumScore
     * @return
     */
    Map<String,Object> normalRecommendQiuwen(String sumScore);

    /**
     * 查询较为合适的学校
     * @param sumScore
     * @return
     */
    Map<String,Object> normalRecommendHeshi(String sumScore);
}
