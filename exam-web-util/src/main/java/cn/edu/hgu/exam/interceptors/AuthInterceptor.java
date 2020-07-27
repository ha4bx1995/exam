package cn.edu.hgu.exam.interceptors;

import cn.edu.hgu.exam.annotations.LoginRequired;
import cn.edu.hgu.exam.util.CookieUtil;
import cn.edu.hgu.util.ContextUtil;
import cn.edu.hgu.util.HttpclientUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录拦截器，用户接收前端所有必要模块的request请求
 * 根据携带token状态确定拦截状态
 * token合法之后，才进行放行
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter{

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
        logger.info(this.getClass().getName() + "AuthInterceptor:::::::::::::::::启动");
        response.setHeader("Access-Control-Allow-Origin","*");
        //拦截代码
        //判断被拦截请求的访问的方法的注解是否是需要拦截的
        HandlerMethod hm = (HandlerMethod) handler;
        LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);

        //1,如果没有拦截注解，代表不需要进行拦截，直接放行
        if(methodAnnotation == null){
            return true;
        }

        String token = "";
        String oldToken = CookieUtil.getCookieValue(request,"oldToken",true);
        if(StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }
       String newToken = request.getParameter("token");

        System.out.println("new token : "+newToken);
        if(StringUtils.isNotBlank(newToken)){
            token = newToken;
        }

        //logger.info(this.getClass().getName() + "token:::::::::::::::::"+token);

        /*
        2,需要拦截，但是拦截校验失败也可以继续访问方法(用户没有登录或者登陆过期)@loginRequired(loginSuccess=false)
        3，需要拦截，并且但是拦截校验一定要校验通过，用户登录成功才可以访问的方法@loginRequired(loginSuccess=true)
         */
        boolean loginSuccess = methodAnnotation.loginSuccess();  //获取接口方法上的注解值
        //调用认证中心进行验证
        String success = "fail";
        Map<String,String> successMap = new HashMap<>();
        if(StringUtils.isNotBlank(token)){
            String ip = request.getHeader("x-forwarded-for");   //获取真实主机的ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();    //从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            if("0:0:0:0:0:0:0:1".equals(ip)){
                ip = "127.0.0.1";
            }
            String successJson = HttpclientUtil.doGet("http://127.0.0.1:8080/verify?token="+token+"&currentIp="+ip);
            successMap = JSON.parseObject(successJson,Map.class);
            success = successMap.get("status");
        }


        if(loginSuccess){
            //必须登录成功才能使用，
            if(!success.equals("success")){
                //重定向到登录页面登录
                final StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect("http://127.0.0.1:8080/login?Return="+requestURL);
                logger.info(this.getClass().getName() + "验证失败:::::::::::::::::拦截器返回false");
                return false;
            }
                //验证通过，覆盖cookie中的token
                //需要将token携带的用户信息写入
                request.setAttribute("userId",successMap.get("userId"));
                request.setAttribute("nickname",successMap.get("nickname"));
                logger.info(this.getClass().getName() + "验证通过:::::::::::::::::拦截器返回true");

                return true;

        }else {
            //没有登录也能使用，但是必须验证
            if(success.equals("success")){
                //需要将token携带的用户信息写入
                request.setAttribute("userId",successMap.get("userId"));
                request.setAttribute("nickname",successMap.get("nickname"));

                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }
            }
        }


        return true;
    }
}
