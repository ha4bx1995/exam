package cn.edu.hgu.exam.bean;

import java.io.Serializable;

/**
 * 用户分数信息DTO类
 */
public class ScoreDTO implements Serializable {

    private Integer chinese;
    private Integer math;
    private Integer forign;
    private Integer multiple;
    private String artAndScience;

    public Integer getChinese() {
        return chinese;
    }

    public void setChinese(Integer chinese) {
        this.chinese = chinese;
    }

    public Integer getMath() {
        return math;
    }

    public void setMath(Integer math) {
        this.math = math;
    }

    public Integer getForign() {
        return forign;
    }

    public void setForign(Integer forign) {
        this.forign = forign;
    }

    public Integer getMultiple() {
        return multiple;
    }

    public void setMultiple(Integer multiple) {
        this.multiple = multiple;
    }

    public String getArtAndScience() {
        return artAndScience;
    }

    public void setArtAndScience(String artAndScience) {
        this.artAndScience = artAndScience;
    }

    @Override
    public String toString() {
        return "ScoreDTO{" +
                "chinese=" + chinese +
                ", math=" + math +
                ", forign=" + forign +
                ", multiple=" + multiple +
                ", artAndScience='" + artAndScience + '\'' +
                '}';
    }
}
