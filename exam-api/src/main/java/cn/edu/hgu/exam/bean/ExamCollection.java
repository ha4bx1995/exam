package cn.edu.hgu.exam.bean;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 用户收藏信息表
 */
public class ExamCollection implements Serializable {
    @Id
    private String exColId;
    private String userId;
    private String username;
    private String exUniCode;
    private String exUniName;
    private String exColTime;

    public String getExColId() {
        return exColId;
    }

    public void setExColId(String exColId) {
        this.exColId = exColId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getExColTime() {
        return exColTime;
    }

    public void setExColTime(String exColTime) {
        this.exColTime = exColTime;
    }

    @Override
    public String toString() {
        return "ExamCollection{" +
                "exColId='" + exColId + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", exUniCode='" + exUniCode + '\'' +
                ", exUniName='" + exUniName + '\'' +
                ", exColTime='" + exColTime + '\'' +
                '}';
    }
}
