package cn.edu.hgu.exam.user.controller;

import cn.edu.hgu.exam.annotations.LoginRequired;
import cn.edu.hgu.exam.bean.ExamAttrSpec;
import cn.edu.hgu.exam.bean.ExamAttrValue;
import cn.edu.hgu.exam.service.StaticDataService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;

@Controller
public class StaticDateController {

    @Reference
    StaticDataService staticDataService;

    /**
     * 加载页面初始化时必须加载的静态数据
     * @return
     */
    @RequestMapping("getStaticData")
    @ResponseBody
    public Map<String,Object> getStaticData(){
        //调用service层，返回静态数据
        Map<String, Object> staticData = staticDataService.getStaticDataDis();
        //ModelAndView m = new ModelAndView(new RedirectView("http://localhost:8080/searchCollage"));
        //m.addObject("map",staticData);
//        if(staticData != null){
//            return staticData;
//        }
        return staticData;
    }

    /**
     * 根据省attrCode加载对应的城市下拉菜单信息
     */
    @RequestMapping("getCityByAttrCode")
    @ResponseBody
    public List<ExamAttrValue> getCityByAttrCode(String attrCode){
        List<ExamAttrValue> cityStaticData = staticDataService.getCityByAttrCode(attrCode);
        return cityStaticData;
    }


    /**
     * 根据专业查学校 专业类别静态字段加载
     */

    @RequestMapping("getStaticMajorType")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public List<ExamAttrSpec> getStaticMajorType(){
        System.out.println("访问到接口了 。。。。。。。。。。。。。");
        List<ExamAttrSpec> majorList = staticDataService.getStaticMajorType();
        return majorList;
    }
}
