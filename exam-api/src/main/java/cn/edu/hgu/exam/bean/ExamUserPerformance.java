package cn.edu.hgu.exam.bean;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 学生用户成绩表
 */
public class ExamUserPerformance implements Serializable {
    @Id
    private String exPerformanceId;
    private String userId;
    private String usename;
    private Integer exChinese;
    private Integer exMath;
    private Integer exForeign;
    private Integer exMultiple;
    private String exArtOrSci;
    private Integer exTotalScore;

    public String getExPerformanceId() {
        return exPerformanceId;
    }

    public void setExPerformanceId(String exPerformanceId) {
        this.exPerformanceId = exPerformanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public Integer getExChinese() {
        return exChinese;
    }

    public void setExChinese(Integer exChinese) {
        this.exChinese = exChinese;
    }

    public Integer getExMath() {
        return exMath;
    }

    public void setExMath(Integer exMath) {
        this.exMath = exMath;
    }

    public Integer getExForeign() {
        return exForeign;
    }

    public void setExForeign(Integer exForeign) {
        this.exForeign = exForeign;
    }

    public Integer getExMultiple() {
        return exMultiple;
    }

    public void setExMultiple(Integer exMultiple) {
        this.exMultiple = exMultiple;
    }

    public String getExArtOrSci() {
        return exArtOrSci;
    }

    public void setExArtOrSci(String exArtOrSci) {
        this.exArtOrSci = exArtOrSci;
    }

    public Integer getExTotalScore() {
        return exTotalScore;
    }

    public void setExTotalScore(Integer exTotalScore) {
        this.exTotalScore = exTotalScore;
    }

    @Override
    public String toString() {
        return "examUserPerformance{" +
                "exPerformanceId='" + exPerformanceId + '\'' +
                ", userId='" + userId + '\'' +
                ", usename='" + usename + '\'' +
                ", exChinese=" + exChinese +
                ", exMath=" + exMath +
                ", exForeign=" + exForeign +
                ", exMultiple=" + exMultiple +
                ", exArtOrSci='" + exArtOrSci + '\'' +
                ", exTotalScore=" + exTotalScore +
                '}';
    }
}
