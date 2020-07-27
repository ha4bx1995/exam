package cn.edu.hgu.exam.service;

import cn.edu.hgu.exam.bean.ExamAttrSpec;
import cn.edu.hgu.exam.bean.ExamAttrValue;

import java.util.List;
import java.util.Map;

public interface StaticDataService {
    /**
     * 加载所有静态数据接口
     * @return
     */
    //Map<String,Object> getStaticData();

    /**
     *将所有静态数据分类，返回前端
     * @return
     */
    Map<String,Object> getStaticDataDis();

    /**
     * 根据省份attrCode 查询对应的城市信息
     * @param attrCode
     * @return
     */
    List<ExamAttrValue> getCityByAttrCode(String attrCode);

    /**
     * 加载专业类别静态字段
     * @return
     */
    List<ExamAttrSpec> getStaticMajorType();

    /**
     * 对外提供spec表的转换能力
     * @param attrCode
     * @return
     */
    public String transferStaticFieldSpec(String attrCode);

    /**
     * 对外提供value表的转换能力
     * @param attrValueId
     * @return
     */
    public String transferStaticFieldValue(String attrValueId);
}
