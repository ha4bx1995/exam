package cn.edu.hgu.exam.bean;

import java.io.Serializable;

/**
 * 学校<->专业信息关联表
 */
public class ExamUniMajorRel implements Serializable {

    private String exUniMajorId;
    private String exUniCode;
    private String exMajorCode;
    private Double exMajorFee;
    /**招生人数**/
    private Integer exMajorEnrollment;
    /**专业总人数**/
    private Integer exMajorTotal;
    private String exMajorSetTime;
    private String exMajorLevel;

    public String getExUniMajorId() {
        return exUniMajorId;
    }

    public void setExUniMajorId(String exUniMajorId) {
        this.exUniMajorId = exUniMajorId;
    }

    public String getExUniCode() {
        return exUniCode;
    }

    public void setExUniCode(String exUniCode) {
        this.exUniCode = exUniCode;
    }

    public String getExMajorCode() {
        return exMajorCode;
    }

    public void setExMajorCode(String exMajorCode) {
        this.exMajorCode = exMajorCode;
    }

    public Double getExMajorFee() {
        return exMajorFee;
    }

    public void setExMajorFee(Double exMajorFee) {
        this.exMajorFee = exMajorFee;
    }

    public Integer getExMajorEnrollment() {
        return exMajorEnrollment;
    }

    public void setExMajorEnrollment(Integer exMajorEnrollment) {
        this.exMajorEnrollment = exMajorEnrollment;
    }

    public Integer getExMajorTotal() {
        return exMajorTotal;
    }

    public void setExMajorTotal(Integer exMajorTotal) {
        this.exMajorTotal = exMajorTotal;
    }

    public String getExMajorSetTime() {
        return exMajorSetTime;
    }

    public void setExMajorSetTime(String exMajorSetTime) {
        this.exMajorSetTime = exMajorSetTime;
    }

    public String getExMajorLevel() {
        return exMajorLevel;
    }

    public void setExMajorLevel(String exMajorLevel) {
        this.exMajorLevel = exMajorLevel;
    }

    @Override
    public String toString() {
        return "ExamUniMajorRel{" +
                "exUniMajorId='" + exUniMajorId + '\'' +
                ", exUniCode='" + exUniCode + '\'' +
                ", exMajorCode='" + exMajorCode + '\'' +
                ", exMajorFee=" + exMajorFee +
                ", exMajorEnrollment=" + exMajorEnrollment +
                ", exMajorTotal=" + exMajorTotal +
                ", exMajorSetTime='" + exMajorSetTime + '\'' +
                ", exMajorLevel='" + exMajorLevel + '\'' +
                '}';
    }
}
