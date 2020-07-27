package cn.edu.hgu.exam.service;

import cn.edu.hgu.exam.bean.ExamMajor;
import cn.edu.hgu.exam.bean.UniversityDTO;

import java.util.List;

public interface MajorService {
    /**
     * 根据专业的类别编码 加载专业信息
     * @param attrCode
     * @return
     */
    List<ExamMajor> getMajorMsgByType(String attrCode);

    /**
     * 根据专业编码，查询专业学校
     * @param majorCode
     * @return
     */
    List<UniversityDTO> getUniversityByMajorCode(String majorCode,Integer pageNum,Integer pageSize);

    /**
     * 根据学校编码查询属于该学校的专业信息
     * @param exUniCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<ExamMajor> getMajorByUniCode(String exUniCode, Integer pageNum, Integer pageSize);

    /**
     * 查询总记录数
     * @return
     */
    Integer getDataCount(String majorCode);

    /**
     * c查询专业总记录数
     * @param exUniCode
     * @return
     */
    Integer getDataCountMajor(String exUniCode);
}
