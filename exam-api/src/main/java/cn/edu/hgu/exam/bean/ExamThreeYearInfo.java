package cn.edu.hgu.exam.bean;

import org.omg.CORBA.INTERNAL;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 各学校近三年信息表 包括近三年分数
 */
public class ExamThreeYearInfo implements Serializable {
    @Id
    private String exUniRecentId;
    private String exUniCode;
    private String exUniName;
    /**近三年分数*/
    private Integer exUniRecentFirst;
    private Integer exUniRecentSecond;
    private Integer exUniRecentThird;
    /**近三年平均分*/
    private Integer exUniRecentAvg;

    private String exUniApply;

    private String exUniEnrollment;

    private String exUniAdmission;

    public String getExUniRecentId() {
        return exUniRecentId;
    }

    public void setExUniRecentId(String exUniRecentId) {
        this.exUniRecentId = exUniRecentId;
    }

    public String getExUniCode() {
        return exUniCode;
    }

    public void setExUniCode(String exUniCode) {
        this.exUniCode = exUniCode;
    }

    public String getExUniName() {
        return exUniName;
    }

    public void setExUniName(String exUniName) {
        this.exUniName = exUniName;
    }

    public Integer getExUniRecentFirst() {
        return exUniRecentFirst;
    }

    public void setExUniRecentFirst(Integer exUniRecentFirst) {
        this.exUniRecentFirst = exUniRecentFirst;
    }

    public Integer getExUniRecentSecond() {
        return exUniRecentSecond;
    }

    public void setExUniRecentSecond(Integer exUniRecentSecond) {
        this.exUniRecentSecond = exUniRecentSecond;
    }

    public Integer getExUniRecentThird() {
        return exUniRecentThird;
    }

    public void setExUniRecentThird(Integer exUniRecentThird) {
        this.exUniRecentThird = exUniRecentThird;
    }

    public Integer getExUniRecentAvg() {
        return exUniRecentAvg;
    }

    public void setExUniRecentAvg(Integer exUniRecentAvg) {
        this.exUniRecentAvg = exUniRecentAvg;
    }

    public String getExUniApply() {
        return exUniApply;
    }

    public void setExUniApply(String exUniApply) {
        this.exUniApply = exUniApply;
    }

    public String getExUniEnrollment() {
        return exUniEnrollment;
    }

    public void setExUniEnrollment(String exUniEnrollment) {
        this.exUniEnrollment = exUniEnrollment;
    }

    public String getExUniAdmission() {
        return exUniAdmission;
    }

    public void setExUniAdmission(String exUniAdmission) {
        this.exUniAdmission = exUniAdmission;
    }

    @Override
    public String toString() {
        return "ExamThreeYearInfo{" +
                "exUniRecentId='" + exUniRecentId + '\'' +
                ", exUniCode='" + exUniCode + '\'' +
                ", exUniName='" + exUniName + '\'' +
                ", exUniRecentFirst=" + exUniRecentFirst +
                ", exUniRecentSecond=" + exUniRecentSecond +
                ", exUniRecentThird=" + exUniRecentThird +
                ", exUniRecentAvg=" + exUniRecentAvg +
                ", exUniApply='" + exUniApply + '\'' +
                ", exUniEnrollment='" + exUniEnrollment + '\'' +
                ", exUniAdmission='" + exUniAdmission + '\'' +
                '}';
    }
}
