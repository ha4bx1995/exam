package cn.edu.hgu.exam.user.controller;


import ch.qos.logback.classic.pattern.SyslogStartConverter;
import cn.edu.hgu.exam.annotations.LoginRequired;
import cn.edu.hgu.exam.bean.ExamUniMajorRel;
import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.bean.UmsMember;
import cn.edu.hgu.exam.bean.UmsMemberReceiveAddress;
import cn.edu.hgu.exam.service.CollageService;
import cn.edu.hgu.exam.service.ManagerService;
import cn.edu.hgu.exam.service.UserService;
import cn.edu.hgu.exam.util.CookieUtil;
import cn.edu.hgu.exam.util.JwtUtil;
import cn.edu.hgu.util.ContextUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import javafx.beans.binding.ObjectExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.ContentHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Reference
    UserService userService;

    @Reference
    ManagerService managerService;

    @Reference
    CollageService collageService;


    /**
     * 注册功能controller
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    @LoginRequired(loginSuccess = false)
    public Map<String,Object> register(ExamUser examUser){
        Map<String,Object> result = new HashMap<>();
        Logger logger = LoggerFactory.getLogger(UserController.class);
        logger.info(this.getClass().getName() + "register:::::::::::::::::入参{" + examUser.toString()+"}");

        //调用service层注册接口
        boolean isSuccess = userService.dealRegister(examUser);

        if(isSuccess){
            result.put("msgCode","0000");
            result.put("msg","恭喜您注册成功！");
            result.put("isSuccess",isSuccess);
        }else {
            result.put("msgCode","1111");
            result.put("msg","注册失败，请重新尝试！");
            result.put("isSuccess",isSuccess);
        }
        return result;
    }

    /**
     * 登录功能controller
     * @param examUser
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/loginUser")
    @ResponseBody
    public String loginUser(ExamUser examUser, HttpServletResponse response, HttpServletRequest request){
        Logger logger = LoggerFactory.getLogger(UserController.class);
        logger.info(this.getClass().getName() + "loginUser:::::::::::::::::入参{" + examUser.toString()+"}");
        if(examUser == null){
            return null;
        }
        String token = "";
        response.setHeader("Access-Control-Allow-Origin","*");
        ExamUser examUserLogin = userService.login(examUser);
        if(examUserLogin != null){
            //按照老师的需求，结合自己系统当前的状况。登录成功之后，给缓存中初始化一个分数信息排名
            Boolean setSuccess = collageService.setScoreMsgList();
            if(!setSuccess){
                return null;
            }
            //登录成功
            //用Jwt制作token
            String userId = examUserLogin.getUserId();
            String nickrname = examUserLogin.getNickname();
            Map<String,Object> userMap = new HashMap<>();
            userMap.put("userId",userId);
            userMap.put("nickname",nickrname);

            String ip = request.getHeader("x-forwarded-for");   //获取真实主机的ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();    //从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            //客户端和服务端都在本机情况下，请求的默认地址是用ipv6方式表示,改成IPV4的
            if(ip.equals("0:0:0:0:0:0:0:1")){
                ip = "127.0.0.1";
            }
            //制作token
            token = JwtUtil.encode("20200419examForHgu",userMap,ip);

            if(StringUtils.isNotBlank(token)){
                CookieUtil.setCookie(request,response,"token",token,60*60*2,true);
            }
            //将token存入redis一份
            userService.addUserToken(token,userId);
            //增加用户信息进缓存的环节
            userService.addUserInfoToCache(examUserLogin);
        }else {
            //登录失败
            token = "fail";
        }
        logger.info(this.getClass().getName() + "loginUser:::::::::::::::::出参{" + token+"}");
        return token;
    }

    /**
     * 访问登录页面时，需要返回原页面的url，以便登录成功进行重定向
     * @param ReturnUrl
     * @param map
     * @return
     */
    @RequestMapping("login")
    public String login(String ReturnUrl, ModelMap map){
        map.put("ReturnUrl",ReturnUrl);
        return "login";
    }

    /**
     * 映射一个测试页面
     * @return
     */
    @RequestMapping("exam")
    public String exam(){
        return "exam";
    }

    //index页面不需要设置必须登录，但是必须添加该注解，继续拦截，但是不需要token也可以访问，
    //否则会在拦截器判空时直接返回，导致后边无法向cookie中写入token
    //跳转index页面时，需要根据缓存中的用户信息判断用户是否是学生用户，如果是学生用户，需要对页面进行个性化定制->调用推荐学校接口
    @RequestMapping("index")
    @LoginRequired(loginSuccess = true)
    public ModelAndView index(String token,HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        //String ip = request.getHeader("x-forwarded-for");
        String ip = request.getRemoteAddr();
        Boolean isStudent = false;
        Boolean isFirstLogin = false;
        //先对token进行解码，获取用户id
        String userId = null;
        Map<String, Object> decode = JwtUtil.decode(token, "20200419examForHgu", ip);
        if(decode != null){
            userId = (String)decode.get("userId");
            //处理index页面之前，调用服务判断当前用户是否是学生用户
            isStudent = managerService.specialConfForStudentUser(userId);
            //登录成功之后，要判断一下用户是否是第一次登录
            isFirstLogin = userService.isFirstLogin(userId);
        }

        //如果是学生账户，需要调用接口返回个性化的首页

        //只有是学生用户，并且是第一次登录，才会提醒完善信息
        mav.addObject("isStudent",isStudent);
        mav.addObject("isFirstLogin",isFirstLogin);
        mav.addObject("token",token);


        return mav;
    }

    /**
     * token信息认证中心
     * @param token
     * @param currentIp
     * @return
     */
    @RequestMapping("/verify")
    @ResponseBody
    public String verifyUser(String token,String currentIp){
        //通过jwt校验token真伪
        Logger logger = LoggerFactory.getLogger(UserController.class);
        logger.info(this.getClass().getName() + "verify:::::::::::::::::入参{" + token+"::::"+currentIp+"}");;
        Map<String,String> map = new HashMap<>();

        //对拦截器发送过来的token进行验证，如果解码成功，表示token是真的
        Map<String, Object> decode = JwtUtil.decode(token, "20200419examForHgu", currentIp);

        if(decode != null){
            map.put("status","success");
            map.put("userId",(String)decode.get("userId"));
            map.put("nickname",(String)decode.get("nickname"));
        }else {
            map.put("status","fail");
        }
        logger.info(this.getClass().getName() + "verify:::::::::::::::::出参{" + map.toString()+"}");;
        return JSON.toJSONString(map);
    }

    @RequestMapping("getUserMsg")
    @ResponseBody
    public Map<String,Object> getUserMsg(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        //获取用户id
        String userId = ContextUtil.getUser(request);
        ExamUser examUser = userService.getOneUserByUserId(userId);
        if(examUser != null){
            result.put("code","0000");
            result.put("msg","查询成功");
            result.put("data",examUser);
        }else {
            result.put("code","1111");
            result.put("msg","查询失败");
            result.put("data",examUser);
        }
        return result;
    }

    /**
     * 用户修改信息controller
     * @param examUser
     * @return
     */
    @RequestMapping("modifyUserMsg")
    @ResponseBody
    public Map<String,Object> modifyUserMsg(ExamUser examUser,HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        String userId = ContextUtil.getUser(request);
        if(StringUtils.isBlank(userId)){
            result.put("code","1111");
            result.put("msg","登录状态异常，请重新登录");
            result.put("data",null);
        }
        if(examUser == null){
            result.put("code","1111");
            result.put("msg","参数异常，请联系管理员");
            result.put("data",null);
        }
        examUser.setUserId(userId);

        Boolean isSuccess = userService.modifyUserMsg(examUser);

        if(isSuccess){
            result.put("code","0000");
            result.put("msg","修改成功！");
            result.put("data",null);
        }else {
            result.put("code","1111");
            result.put("msg","修改失败，建议重新尝试！");
            result.put("data",null);
        }

        return result;
    }


    /****************以下方法是测试方法，不参与项目代码*****************/

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<ExamUser> getAllUser(){

        List<ExamUser> umsMembers = userService.getAllUser();
        return umsMembers;
    }

    //测试manager模块
    @RequestMapping("test")
    @ResponseBody
    public String test(){
        String test = managerService.test();
        return test;
    }

}
