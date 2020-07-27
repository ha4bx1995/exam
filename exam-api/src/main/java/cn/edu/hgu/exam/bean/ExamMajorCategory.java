package cn.edu.hgu.exam.bean;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 专业信息类别表
 */
public class ExamMajorCategory implements Serializable {
    @Id
    private String exMajorCateId;
    private String exMajorCateCode;
    private String exMajorCateName;
    private String exMajorNannvRate;
    private String exMajorDegree;

    public String getExMajorCateId() {
        return exMajorCateId;
    }

    public void setExMajorCateId(String exMajorCateId) {
        this.exMajorCateId = exMajorCateId;
    }

    public String getExMajorCateCode() {
        return exMajorCateCode;
    }

    public void setExMajorCateCode(String exMajorCateCode) {
        this.exMajorCateCode = exMajorCateCode;
    }

    public String getExMajorCateName() {
        return exMajorCateName;
    }

    public void setExMajorCateName(String exMajorCateName) {
        this.exMajorCateName = exMajorCateName;
    }

    public String getExMajorNannvRate() {
        return exMajorNannvRate;
    }

    public void setExMajorNannvRate(String exMajorNannvRate) {
        this.exMajorNannvRate = exMajorNannvRate;
    }

    public String getExMajorDegree() {
        return exMajorDegree;
    }

    public void setExMajorDegree(String exMajorDegree) {
        this.exMajorDegree = exMajorDegree;
    }

    @Override
    public String toString() {
        return "ExamMajorCategory{" +
                "exMajorCateId='" + exMajorCateId + '\'' +
                ", exMajorCateCode='" + exMajorCateCode + '\'' +
                ", exMajorCateName='" + exMajorCateName + '\'' +
                ", exMajorNannvRate='" + exMajorNannvRate + '\'' +
                ", exMajorDegree='" + exMajorDegree + '\'' +
                '}';
    }
}
