package cn.edu.hgu.util;

import cn.edu.hgu.exam.bean.ExamUser;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义从token中获取用户信息的工具类
 *@author liuzt
 * @return
 */
public class ContextUtil {

    @Autowired
    RedisUtil redisUtil;

    public static String getUser(HttpServletRequest request){
        ContextUtil contextUtil = new ContextUtil();
        return contextUtil.getUserInfo(request);
    }

    public  String getUserInfo(HttpServletRequest request){
        //先从request请求获取token
        String requestURL = request.getRequestURL().toString();
       // Map<String, String> urlParamsMap = urlSplit(requestURL);
        //String token = urlParamsMap.get("token");
        String token = request.getParameter("token");
        String ip = request.getRemoteAddr();
        if("0:0:0:0:0:0:0:1".equals(ip)){
            ip = "127.0.0.1";
        }
        System.out.println("contextToken："+token);

        Map<String, Object> decode = JwtUtil.decode(token, "20200419examForHgu", ip);
        String userId = (String)decode.get("userId");

        return userId;
    }

    /**
     * 获取request请求的工具
     * @return
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;

    }

    /**
     * 获取session的工具
     * @return
     */
    public static HttpSession getSession() {
        HttpSession session = getRequest().getSession();
        return session;
    }

    public static Map<String, String> urlSplit(String URL){
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit=null;
        String strUrlParam=TruncateUrlPage(URL);
        if(strUrlParam==null){
            return mapRequest;
        }
        arrSplit=strUrlParam.split("[&]");
        for(String strSplit:arrSplit){
            String[] arrSplitEqual=null;
            arrSplitEqual= strSplit.split("[=]");
            //解析出键值
            if(arrSplitEqual.length>1){
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            }else{
                if(arrSplitEqual[0]!=""){
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    private static String TruncateUrlPage(String strURL){
        String strAllParam=null;
        String[] arrSplit=null;
        strURL=strURL.trim().toLowerCase();
        arrSplit=strURL.split("[?]");
        if(strURL.length()>1){
            if(arrSplit.length>1){
                for (int i=1;i<arrSplit.length;i++){
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }
}
