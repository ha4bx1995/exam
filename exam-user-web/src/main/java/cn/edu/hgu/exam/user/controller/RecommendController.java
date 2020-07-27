package cn.edu.hgu.exam.user.controller;


import cn.edu.hgu.exam.annotations.LoginRequired;
import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.bean.ExamUserPerformance;
import cn.edu.hgu.exam.bean.ScoreDTO;
import cn.edu.hgu.exam.service.ManagerService;
import cn.edu.hgu.exam.service.PerformanceService;
import cn.edu.hgu.exam.service.UserService;
import cn.edu.hgu.util.ContextUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RecommendController {

    @Reference
    ManagerService managerService;

    @Reference
    UserService userService;

    @Reference
    PerformanceService performanceService;

    /**
     * 首页推荐学校controller
     * @return
     */
    @RequestMapping("/recommendCollage")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public Map<String,Object> recommendCollage(ScoreDTO scoreDTO, HttpServletResponse response, HttpServletRequest request){
        response.setHeader("Access-Control-Allow-Origin","*");
        String userId = ContextUtil.getUser(request);
        Map<String, Object> recommend = managerService.getRecommendFitCollage(scoreDTO,userId,true);
        if(recommend != null){
            return recommend;
        }
        System.out.println(scoreDTO.toString());
        return null;
    }

    @RequestMapping("recommendCollageNotFirst")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public Map<String,Object> recommendCollageNotFirst(HttpServletRequest request){
        //使用工具类获取userId
        String userId = ContextUtil.getUser(request);
        //调用成绩表service 查询成绩信息
        Map<String, Object> userPerformance = performanceService.getUserPerformance(userId);
        //直接返回即可
        return userPerformance;
    }

    /**
     * 可以冲刺的学校
     * @param sumScore
     * @return
     */
    @RequestMapping("normalRecommendChongci")
    @ResponseBody
    public Map<String,Object> normalRecommendChongci(String sumScore){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(sumScore)){
            map.put("code","1");
            map.put("msg","冲刺：参数为空！");
            map.put("data",null);
            map.put("count",0);
            return map;
        }
        Map<String,Object> result = managerService.normalRecommendChongci(sumScore);
        if(result != null){
            return result;
        }
        return null;
    }

    /**
     * 留作求稳的学校
     * @param sumScore
     * @return
     */
    @RequestMapping("normalRecommendQiuwen")
    @ResponseBody
    public Map<String,Object> normalRecommendQiuwen(String sumScore){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(sumScore)){
            map.put("code","1");
            map.put("msg","求稳：参数为空！");
            map.put("data",null);
            map.put("count",0);
            return map;
        }
        Map<String,Object> result = managerService.normalRecommendQiuwen(sumScore);
        if(result != null){
            return result;
        }
        return null;
    }

    /**
     * 当前分数段合适的学校
     * @param sumScore
     * @return
     */
    @RequestMapping("normalRecommendHeshi")
    @ResponseBody
    public Map<String,Object> normalRecommendHeshi(String sumScore){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(sumScore)){
            map.put("code","1");
            map.put("msg","合适：参数为空！");
            map.put("data",null);
            map.put("count",0);
            return map;
        }
        Map<String,Object> result = managerService.normalRecommendHeshi(sumScore);
        if(result != null){
            return result;
        }
        return null;
    }

}
