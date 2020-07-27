package cn.edu.hgu.exam.bean;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 静态资源配置属性->值表
 */
public class ExamAttrValue implements Serializable {
    @Id
    private String attrValueId;
    private String attrCode;
    private String attrValueName;
    private String attrValueDesc;
    private String attrValueCreatetime;

    public String getAttrValueId() {
        return attrValueId;
    }

    public void setAttrValueId(String attrValueId) {
        this.attrValueId = attrValueId;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public String getAttrValueName() {
        return attrValueName;
    }

    public void setAttrValueName(String attrValueName) {
        this.attrValueName = attrValueName;
    }

    public String getAttrValueDesc() {
        return attrValueDesc;
    }

    public void setAttrValueDesc(String attrValueDesc) {
        this.attrValueDesc = attrValueDesc;
    }

    public String getAttrValueCreatetime() {
        return attrValueCreatetime;
    }

    public void setAttrValueCreatetime(String attrValueCreatetime) {
        this.attrValueCreatetime = attrValueCreatetime;
    }

    @Override
    public String toString() {
        return "ExamAttrValue{" +
                "attrValueId='" + attrValueId + '\'' +
                ", attrCode='" + attrCode + '\'' +
                ", attrValueName='" + attrValueName + '\'' +
                ", attrValueDesc='" + attrValueDesc + '\'' +
                ", attrValueCreatetime='" + attrValueCreatetime + '\'' +
                '}';
    }
}
