package cn.edu.hgu.exam.bean;

import java.io.Serializable;

/**
 * 学校信息查询DTO参数类
 */
public class UniversityParamsDTO implements Serializable {
    private String uniProvince;
    private String uniCity;
    private String uniType;
    private String uniGrade;
    private String uniNature;
    private String uniLevel;
    private String uniName;

    public String getUniProvince() {
        return uniProvince;
    }

    public void setUniProvince(String uniProvince) {
        this.uniProvince = uniProvince;
    }

    public String getUniCity() {
        return uniCity;
    }

    public void setUniCity(String uniCity) {
        this.uniCity = uniCity;
    }

    public String getUniType() {
        return uniType;
    }

    public void setUniType(String uniType) {
        this.uniType = uniType;
    }

    public String getUniGrade() {
        return uniGrade;
    }

    public void setUniGrade(String uniGrade) {
        this.uniGrade = uniGrade;
    }

    public String getUniNature() {
        return uniNature;
    }

    public void setUniNature(String uniNature) {
        this.uniNature = uniNature;
    }

    public String getUniLevel() {
        return uniLevel;
    }

    public void setUniLevel(String uniLevel) {
        this.uniLevel = uniLevel;
    }

    public String getUniName() {
        return uniName;
    }

    public void setUniName(String uniName) {
        this.uniName = uniName;
    }

    @Override
    public String toString() {
        return "UniversityParamsDTO{" +
                "uniProvince='" + uniProvince + '\'' +
                ", uniCity='" + uniCity + '\'' +
                ", uniType='" + uniType + '\'' +
                ", uniGrade='" + uniGrade + '\'' +
                ", uniNature='" + uniNature + '\'' +
                ", uniLevel='" + uniLevel + '\'' +
                ", uniName='" + uniName + '\'' +
                '}';
    }
}
