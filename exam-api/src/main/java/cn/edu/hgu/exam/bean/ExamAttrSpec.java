package cn.edu.hgu.exam.bean;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * 静态资源属性配置表
 */
public class ExamAttrSpec implements Serializable {
    @Id
    private String attrSpecId;
    private String attrCode;
    private String attrSpecName;
    private String attrSpecDesc;
    private String attrSpecCreatetime;

    public String getAttrSpecId() {
        return attrSpecId;
    }

    public void setAttrSpecId(String attrSpecId) {
        this.attrSpecId = attrSpecId;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public String getAttrSpecName() {
        return attrSpecName;
    }

    public void setAttrSpecName(String attrSpecName) {
        this.attrSpecName = attrSpecName;
    }

    public String getAttrSpecDesc() {
        return attrSpecDesc;
    }

    public void setAttrSpecDesc(String attrSpecDesc) {
        this.attrSpecDesc = attrSpecDesc;
    }

    public String getAttrSpecCreatetime() {
        return attrSpecCreatetime;
    }

    public void setAttrSpecCreatetime(String attrSpecCreatetime) {
        this.attrSpecCreatetime = attrSpecCreatetime;
    }

    @Override
    public String toString() {
        return "ExamAttrSpec{" +
                "attrSpecId='" + attrSpecId + '\'' +
                ", attrCode='" + attrCode + '\'' +
                ", attrSpecName='" + attrSpecName + '\'' +
                ", attrSpecDesc='" + attrSpecDesc + '\'' +
                ", attrSpecCreatetime='" + attrSpecCreatetime + '\'' +
                '}';
    }

    /*
        重写equals & hasCode方法，
         */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamAttrSpec that = (ExamAttrSpec) o;
        return attrCode.equals(that.attrCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(attrSpecId, attrCode, attrSpecName, attrSpecDesc, attrSpecCreatetime);
    }
}
