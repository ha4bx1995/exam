package cn.edu.hgu.exam.bean;

import java.io.Serializable;

/**
 * 大学专业信息表
 */
public class ExamMajor implements Serializable {
    private String exMajorId;
    private String exMajorCode;
    private String exMajorName;
    private String exMajorCate;
    private String exMajorHot;
    private String exMajorDesc;
    private String exMajorRate;

    public String getExMajorId() {
        return exMajorId;
    }

    public void setExMajorId(String exMajorId) {
        this.exMajorId = exMajorId;
    }

    public String getExMajorCode() {
        return exMajorCode;
    }

    public void setExMajorCode(String exMajorCode) {
        this.exMajorCode = exMajorCode;
    }

    public String getExMajorName() {
        return exMajorName;
    }

    public void setExMajorName(String exMajorName) {
        this.exMajorName = exMajorName;
    }

    public String getExMajorCate() {
        return exMajorCate;
    }

    public void setExMajorCate(String exMajorCate) {
        this.exMajorCate = exMajorCate;
    }

    public String getExMajorHot() {
        return exMajorHot;
    }

    public void setExMajorHot(String exMajorHot) {
        this.exMajorHot = exMajorHot;
    }

    public String getExMajorDesc() {
        return exMajorDesc;
    }

    public void setExMajorDesc(String exMajorDesc) {
        this.exMajorDesc = exMajorDesc;
    }

    public String getExMajorRate() {
        return exMajorRate;
    }

    public void setExMajorRate(String exMajorRate) {
        this.exMajorRate = exMajorRate;
    }

    @Override
    public String toString() {
        return "ExamMajor{" +
                "exMajorId='" + exMajorId + '\'' +
                ", exMajorCode='" + exMajorCode + '\'' +
                ", exMajorName='" + exMajorName + '\'' +
                ", exMajorCate='" + exMajorCate + '\'' +
                ", exMajorHot='" + exMajorHot + '\'' +
                ", exMajorDesc='" + exMajorDesc + '\'' +
                ", exMajorRate='" + exMajorRate + '\'' +
                '}';
    }
}
