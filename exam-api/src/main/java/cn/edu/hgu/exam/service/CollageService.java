package cn.edu.hgu.exam.service;

import cn.edu.hgu.exam.bean.ExamCollection;
import cn.edu.hgu.exam.bean.ExamUniversity;
import cn.edu.hgu.exam.bean.UniversityDTO;
import cn.edu.hgu.exam.bean.UniversityParamsDTO;

import java.util.List;
import java.util.Map;

/**
 * 学校服务接口
 * @author lzt
 */
public interface CollageService {

    /**
     * 条件查询学校基本信息接口
     * @param universityParamsDTO
     * @return
     */
    List<ExamUniversity> searchCollageByExample(ExamUniversity universityParamsDTO,Integer pageNum,Integer pageSize);

    /**
     * 根据学校编码查询学校信息
     * @param exUniCode
     * @return
     */
    ExamUniversity getOneCollage(String exUniCode);

    /**
     * 查询数据总数
     * @param universityDTO
     * @return
     */
    Integer getCountByExample(ExamUniversity universityDTO);

    /**
     * 查询学校近三年信息，做可视化处理
     * @param exUniCode
     * @return
     */
    Map<String,Object> getUniThreeYearMsg(String exUniCode);

    /**
     * 根据城市编码 查询学校信息
     * @return
     */
    Map<String,Object> getCollageByCityCode(String exUniCode);

    /**
     * 在缓冲设置分数信息列表，用于考试位次模拟计算
     * @return
     */
    Boolean setScoreMsgList();

    /**
     * 根据用户的成绩计算总排名
     * @param sumScore
     * @return
     */
    Integer getScorePosition(Integer sumScore);
}
