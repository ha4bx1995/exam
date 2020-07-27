package cn.edu.hgu.exam.user.controller;


import cn.edu.hgu.exam.bean.*;
import cn.edu.hgu.exam.service.CollageService;
import cn.edu.hgu.exam.service.ManagerService;
import cn.edu.hgu.exam.service.PerformanceService;
import cn.edu.hgu.exam.service.UserService;
import cn.edu.hgu.util.ContextUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CollageController {
    @Reference
    UserService userService;

    @Reference
    CollageService collageService;

    @Reference
    ManagerService managerService;

    @Reference
    PerformanceService performanceService;

    @RequestMapping(value = "searchCollage",method = RequestMethod.GET)
    public String searchCollage(){
        return "order/list";
    }

    /**
     * 分页查询学校信息controller
     * @param universityDTO
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("searchCollageByExample")
    @ResponseBody
    public Map<String,Object> searchCollageByExample(ExamUniversity universityDTO,
                                                     @RequestParam(defaultValue = "1",value = "curr")Integer pageNum,
                                                     @RequestParam(defaultValue = "10",value = "limit") Integer pageSize){
        Map<String,Object> result = new HashMap<>();


        if(StringUtils.isBlank(universityDTO.getExUniCity())){
            universityDTO.setExUniCity(null);
        }
        if(StringUtils.isBlank(universityDTO.getExUniProvince())){
            universityDTO.setExUniProvince(null);
        }
        if(StringUtils.isBlank(universityDTO.getExUniGrade())){
            universityDTO.setExUniGrade(null);
        }
        if(StringUtils.isBlank(universityDTO.getExUniLevel())){
            universityDTO.setExUniLevel(null);
        }
        if(StringUtils.isBlank(universityDTO.getExUniType())){
            universityDTO.setExUniType(null);
        }
        if(StringUtils.isBlank(universityDTO.getExUniNature())){
            universityDTO.setExUniNature(null);
        }
        if(StringUtils.isBlank(universityDTO.getExUniName())){
            universityDTO.setExUniName(null);
        }

       //PageHelper.startPage(pageNum,pageSize);

        List<ExamUniversity> collageList = collageService.searchCollageByExample(universityDTO,pageNum,pageSize);

        Integer count = collageService.getCountByExample(universityDTO);


       //PageInfo<ExamUniversity> examUniversityPageInfo = new PageInfo<>(collageList);

        //返回数据时需要根据layui的数据格式，转换一下 取pageInfo中的数据信息
        if(collageList != null){
            result.put("data",collageList);
            result.put("msg","查询成功");
            result.put("count",count);
            result.put("code",0);
        }
        return result;
    }

    @RequestMapping("getUserName")
    @ResponseBody
    public ExamUniversity getUniNameByCode(String exUniCode){
        ExamUniversity examUniversity = collageService.getOneCollage(exUniCode);
        return examUniversity;
    }

    /**
     * 处理学校近三年信息，可视化功能
     * @param exUniCode
     * @return
     */
    @RequestMapping("getUniThreeYearMsg")
    @ResponseBody
    public Map<String,Object> getUniThreeYearMsg(String exUniCode){
        Map<String,Object> result = null;
        if(StringUtils.isBlank(exUniCode)){
            result = new HashMap<>();
            result.put("code","1111");
            result.put("msg","查询失败，参数异常");
            result.put("data",result);
        }
        result = collageService.getUniThreeYearMsg(exUniCode);
        if(result != null){
            result.put("code","0000");
            result.put("msg","查询成功");
            return  result;
        }
        return new HashMap<>();
    }

    @RequestMapping("getCollageByCityCode")
    @ResponseBody
    public Map<String,Object> getCollageByCityCode(String exUniCity){
        Map<String,Object> result = null;
        if(StringUtils.isBlank(exUniCity)){
            result = new HashMap<>();
            result.put("code","1111");
            result.put("msg","查询失败，参数异常");
            result.put("data",null);
        }

        result = collageService.getCollageByCityCode(exUniCity);
        if(result != null){
            return result;
        }
        return new HashMap<>();
    }

    @RequestMapping("getScorePosition")
    @ResponseBody
    public Map<String,Object> getScorePosition(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        //先获取用户id
        String userId = ContextUtil.getUser(request);
        ExamUserPerformance e = performanceService.getUserScoreMsg(userId);
        //计算总成绩
        Integer sumScore = e.getExChinese() + e.getExMath() + e.getExForeign() + e.getExMultiple();
        //根据总成绩调用服务 确认该用户的排名
        Integer position = collageService.getScorePosition(sumScore);

        if(position != null &&position != 0){
            result.put("code","0000");
            result.put("msg","查询成功");
            result.put("data",position);
        }
        return result;
    }

}
