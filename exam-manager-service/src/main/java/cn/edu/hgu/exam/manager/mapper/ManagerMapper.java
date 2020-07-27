package cn.edu.hgu.exam.manager.mapper;

import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.bean.ExamUserPerformance;
import cn.edu.hgu.exam.bean.UniversityDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ManagerMapper extends Mapper<UniversityDTO> {
    /**
     * 查询上下限合适的学校
     * @param upperLimit
     * @param lowerLimit
     * @param artOrScience
     * @return
     */
    List<UniversityDTO> selectFitCollage(@Param("upperLimit") Integer upperLimit, @Param("lowerLimit") Integer lowerLimit,@Param("artOrScience") String artOrScience);


    /**
     * 普通用户合适的学校
     * @param upperLimit
     * @param lowerLimit
     * @return
     */
    List<UniversityDTO> selectHeshiCollage(@Param("upperLimit") Integer upperLimit, @Param("lowerLimit") Integer lowerLimit);

    /**
     * 查询求稳的学校
     * @param sumScore
     */
    List<UniversityDTO> selectStabilityCollage(@Param("sumScore") Integer sumScore,@Param("artOrScience") String artOrScience);

    /**
     * 普通用户
     * @param sumScore
     * @return
     */
    List<UniversityDTO> selectQiuwenCollage(@Param("sumScore") Integer sumScore);

    /**
     * 查询可以冲刺的学校
     * @param sumScore
     * @param artOrScience
     * @return
     */
    List<UniversityDTO> selectSprintCollage(@Param("sumScore") Integer sumScore,@Param("artOrScience") String artOrScience);

    /**
     * 普通用户
     * @param sumScore
     * @return
     */
    List<UniversityDTO> selectChongciCollage(@Param("sumScore") Integer sumScore);

    /**
     * 插入学生成绩信息
     * @param eup
     */
    int insertStudentPerformance(@Param("eup") ExamUserPerformance eup);

    /**
     * 根据专业编码查询学校信息
     * @param exUniCode
     */
    UniversityDTO selectUniByCode(@Param("exUniCode") String exUniCode);

}
