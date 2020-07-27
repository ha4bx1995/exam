package cn.edu.hgu.exam.user.controller;

import cn.edu.hgu.exam.bean.ExamCollection;
import cn.edu.hgu.exam.bean.ExamUniversity;
import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.bean.UniversityDTO;
import cn.edu.hgu.exam.service.CollectService;
import cn.edu.hgu.exam.service.ManagerService;
import cn.edu.hgu.exam.service.UserService;
import cn.edu.hgu.util.ContextUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CollectController {
    @Reference
    UserService userService;

    @Reference
    CollectService collectService;

    @Reference
    ManagerService managerService;

    /**
     * 用户点击收藏按钮，对学校进行收藏
     * @param exUniCode
     * @param exUniName
     * @param request
     * @return
     */
    @RequestMapping("collectCollage")
    @ResponseBody
    public Map<String,Object> collectCollage(String exUniCode, String exUniName, HttpServletRequest request){
       Map<String,Object> result  = new HashMap<>();
        //先使用工具类获取用户id
        String userId = ContextUtil.getUser(request);
        if(StringUtils.isBlank(userId)){
            result.put("resultCode","1111");
            result.put("resultMsg","登录信息过期，请重新登录");
            result.put("data",null);
            return result;
        }
        //先从缓存中获取用户信息
        ExamUser userInfoFromCache = managerService.getUserInfoFromCache(userId);
        if(userInfoFromCache == null){
            result.put("resultCode","1111");
            result.put("resultMsg","登录信息过期，请重新登录");
            result.put("data",null);
            return result;
        }

        ExamCollection examCollection = new ExamCollection();
        examCollection.setExUniCode(exUniCode);
        examCollection.setExUniName(exUniName);
        examCollection.setUserId(userInfoFromCache.getUserId());
        examCollection.setUsername(userInfoFromCache.getUsername());
        java.sql.Date time= new java.sql.Date(new Date().getTime());
        examCollection.setExColTime(time.toString());
        //开始操作数据库插入数据
        result = collectService.collectCollage(examCollection);
        //直接返回result即可
        return result;
    }

    /**
     * 查询当前登录用户下的收藏信息
     * @param request
     * @return
     */
    @RequestMapping("getCollectMsgByUserId")
    @ResponseBody
    public Map<String,Object> getCollectMsgByUserId(HttpServletRequest request,@RequestParam(defaultValue = "1",value = "curr")Integer pageNum,
                                                                                @RequestParam(defaultValue = "10",value = "limit") Integer pageSize){
        Map<String,Object> result = new HashMap<>();
        //先获取用户id
        String userId = ContextUtil.getUser(request);
        //调用收藏服务，查询该用户下的收藏信息，返回值是一个学校信息列表
        List<UniversityDTO> universityDTOList = collectService.getCollectMsgByUserId(userId,pageNum,pageSize);

        if(universityDTOList != null && universityDTOList.size() > 0){
            result.put("code","0");
            result.put("msg","查询成功");
            result.put("data",universityDTOList);
            result.put("count",universityDTOList.size());
        }else {
            result.put("code","0");
            result.put("msg","查询异常");
            result.put("data",null);
            result.put("count",universityDTOList.size());
        }
        return result;
    }

    /**
     * 删除当前用户下的收藏信息
     * @param exUniCode
     * @param request
     * @return
     */
    @RequestMapping("cancelCollectCollage")
    @ResponseBody
    public Map<String,Object> cancelCollectCollage(String exUniCode,HttpServletRequest request){
        Map<String,Object> result = null;
        if(StringUtils.isBlank(exUniCode)){
            result = new HashMap<>();
            result.put("resultCode","1111");
            result.put("resultMsg","参数为空！");
        }
        //先获取用户id
        String userId = ContextUtil.getUser(request);
        //直接调用服务，删除该收藏信息
        result =  collectService.cancelCollectCollage(exUniCode,userId);

        if(result != null){
            return result;
        }

        return null;
    }

}














