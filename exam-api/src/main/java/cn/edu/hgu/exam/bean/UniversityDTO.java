package cn.edu.hgu.exam.bean;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 各类高校信息表
 */
public class UniversityDTO implements Serializable {
    @Id
    private String exUniId;
    private String exUniCode;
    private String exUniProvince;
    private String exUniCity;
    private String exUniType;
    private String exUniAddress;
    private String exUniLevel;
    private String exUniName;
    private String exUniBuild;
    private String exUniWebsite;
    private String exUniDesc;
    private String exUniTel;
    private String exUniNature;
    private Integer exUniAvg;
    private Integer exUniMin;
    private Integer exUniMax;
    private String getExUniCityLevel;
    private String exUniGraRate;
    private Integer exUniRanking;
    private Integer exUniTotal;
    private String exUniGrade;
    private String exUniArtSci;
    private String status;

    /**
     * 收藏夹返回收藏时间使用
     */
    private String colTime;

    public String getColTime() {
        return colTime;
    }

    public void setColTime(String colTime) {
        this.colTime = colTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExUniId() {
        return exUniId;
    }

    public void setExUniId(String exUniId) {
        this.exUniId = exUniId;
    }

    public String getExUniCode() {
        return exUniCode;
    }

    public void setExUniCode(String exUniCode) {
        this.exUniCode = exUniCode;
    }

    public String getExUniProvince() {
        return exUniProvince;
    }

    public void setExUniProvince(String exUniProvince) {
        this.exUniProvince = exUniProvince;
    }

    public String getExUniCity() {
        return exUniCity;
    }

    public void setExUniCity(String exUniCity) {
        this.exUniCity = exUniCity;
    }

    public String getExUniType() {
        return exUniType;
    }

    public void setExUniType(String exUniType) {
        this.exUniType = exUniType;
    }

    public String getExUniAddress() {
        return exUniAddress;
    }

    public void setExUniAddress(String exUniAddress) {
        this.exUniAddress = exUniAddress;
    }

    public String getExUniLevel() {
        return exUniLevel;
    }

    public void setExUniLevel(String exUniLevel) {
        this.exUniLevel = exUniLevel;
    }

    public String getExUniName() {
        return exUniName;
    }

    public void setExUniName(String exUniName) {
        this.exUniName = exUniName;
    }

    public String getExUniBuild() {
        return exUniBuild;
    }

    public void setExUniBuild(String exUniBuild) {
        this.exUniBuild = exUniBuild;
    }

    public String getExUniWebsite() {
        return exUniWebsite;
    }

    public void setExUniWebsite(String exUniWebsite) {
        this.exUniWebsite = exUniWebsite;
    }

    public String getExUniDesc() {
        return exUniDesc;
    }

    public void setExUniDesc(String exUniDesc) {
        this.exUniDesc = exUniDesc;
    }

    public String getExUniTel() {
        return exUniTel;
    }

    public void setExUniTel(String exUniTel) {
        this.exUniTel = exUniTel;
    }

    public String getExUniNature() {
        return exUniNature;
    }

    public void setExUniNature(String exUniNature) {
        this.exUniNature = exUniNature;
    }

    public Integer getExUniAvg() {
        return exUniAvg;
    }

    public void setExUniAvg(Integer exUniAvg) {
        this.exUniAvg = exUniAvg;
    }

    public Integer getExUniMin() {
        return exUniMin;
    }

    public void setExUniMin(Integer exUniMin) {
        this.exUniMin = exUniMin;
    }

    public Integer getExUniMax() {
        return exUniMax;
    }

    public void setExUniMax(Integer exUniMax) {
        this.exUniMax = exUniMax;
    }

    public String getGetExUniCityLevel() {
        return getExUniCityLevel;
    }

    public void setGetExUniCityLevel(String getExUniCityLevel) {
        this.getExUniCityLevel = getExUniCityLevel;
    }

    public String getExUniGraRate() {
        return exUniGraRate;
    }

    public void setExUniGraRate(String exUniGraRate) {
        this.exUniGraRate = exUniGraRate;
    }

    public Integer getExUniRanking() {
        return exUniRanking;
    }

    public void setExUniRanking(Integer exUniRanking) {
        this.exUniRanking = exUniRanking;
    }

    public Integer getExUniTotal() {
        return exUniTotal;
    }

    public void setExUniTotal(Integer exUniTotal) {
        this.exUniTotal = exUniTotal;
    }

    public String getExUniGrade() {
        return exUniGrade;
    }

    public void setExUniGrade(String exUniGrade) {
        this.exUniGrade = exUniGrade;
    }

    public String getExUniArtSci() {
        return exUniArtSci;
    }

    public void setExUniArtSci(String exUniArtSci) {
        this.exUniArtSci = exUniArtSci;
    }

    @Override
    public String toString() {
        return "UniversityDTO{" +
                "exUniId='" + exUniId + '\'' +
                ", exUniCode='" + exUniCode + '\'' +
                ", exUniProvince='" + exUniProvince + '\'' +
                ", exUniCity='" + exUniCity + '\'' +
                ", exUniType='" + exUniType + '\'' +
                ", exUniAddress='" + exUniAddress + '\'' +
                ", exUniLevel='" + exUniLevel + '\'' +
                ", exUniName='" + exUniName + '\'' +
                ", exUniBuild='" + exUniBuild + '\'' +
                ", exUniWebsite='" + exUniWebsite + '\'' +
                ", exUniDesc='" + exUniDesc + '\'' +
                ", exUniTel='" + exUniTel + '\'' +
                ", exUniNature='" + exUniNature + '\'' +
                ", exUniAvg=" + exUniAvg +
                ", exUniMin=" + exUniMin +
                ", exUniMax=" + exUniMax +
                ", getExUniCityLevel='" + getExUniCityLevel + '\'' +
                ", exUniGraRate='" + exUniGraRate + '\'' +
                ", exUniRanking=" + exUniRanking +
                ", exUniTotal=" + exUniTotal +
                ", exUniGrade='" + exUniGrade + '\'' +
                ", exUniArtSci='" + exUniArtSci + '\'' +
                '}';
    }
}
