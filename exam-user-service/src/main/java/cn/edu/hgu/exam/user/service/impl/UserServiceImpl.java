package cn.edu.hgu.exam.user.service.impl;


import cn.edu.hgu.exam.bean.ExamUniversity;
import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.service.UserService;
import cn.edu.hgu.exam.user.mapper.UmsMemberReceiveAddressMapper;
import cn.edu.hgu.exam.user.mapper.UserMapper;
import cn.edu.hgu.util.RedisUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<ExamUser> getAllUser() {
        List<ExamUser> umsMemverList = userMapper.selectAll(); //  userMapper.selectAllUser();
        return umsMemverList;
    }

    /**
     * 前台页面主动登录功能
     * @param examUser
     * @return
     */
    @Override
    public ExamUser login(ExamUser examUser) {
        Jedis jedis = null;
        try{
            //先从缓存里边拿
            jedis = redisUtil.getJedis();
            if(jedis != null){
                String userInfo = jedis.get("user:" + examUser.getPassword() + ":password");
                if(StringUtils.isNotBlank(userInfo)){
                    //密码正确
                    ExamUser examUserFromCache = JSON.parseObject(userInfo, ExamUser.class);
                    return examUserFromCache;
                }
            }
            //连接redis失败，或者缓存中没有用户信息，开启数据库查询
            ExamUser examUserFromDB = loginFromDB(examUser);
            if(examUserFromDB != null){
                jedis.setex("user:" + examUser.getPassword() + ":password",60*60*24,JSON.toJSONString(examUserFromDB));
            }
            return examUserFromDB;

        }finally {
            jedis.close();
        }
    }

    @Override
    public void addUserToken(String token, String userId) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            jedis.setex("user:"+userId+":token",60*60*2,token);
        }finally {
            jedis.close();
        }
    }

    /**
     * 将用户信息添加进缓存，方便使用
     * @param examUserLogin
     */
    @Override
    public void addUserInfoToCache(ExamUser examUserLogin) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String userInfoJson = JSON.toJSONString(examUserLogin);
            jedis.setex("userInfo"+examUserLogin.getUserId()+"info",60*60*2,userInfoJson);

        }finally {
            jedis.close();
        }
    }

    /**
     * 用户是否是第一次登录
     * @param userId
     * @return
     */
    @Override
    public Boolean isFirstLogin(String userId) {
        ExamUser examUser = new ExamUser();
        examUser.setUserId(userId);
        ExamUser e = userMapper.selectOne(examUser);
        if(e != null){
            //第一次登录 标志位为0 ，返回之前，需要将标志位置为1
            if(e.getFirstLogin().equals("0")){
                examUser.setFirstLogin("1");
                boolean setSuccess = userMapper.updateByPrimaryKeySelective(examUser) > 0;
                if(!setSuccess){
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 查询用户的信息，从缓存拿
     * @param userId
     * @return
     */
    @Override
    public ExamUser getOneUserByUserId(String userId) {
        if(StringUtils.isBlank(userId)){
            return  null;
        }

        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if(jedis != null){
                String userInfo = jedis.get("userInfo" + userId + "info");
                if(StringUtils.isNotBlank(userInfo)){
                    ExamUser examUserFromCache = JSON.parseObject(userInfo, ExamUser.class);
                    return examUserFromCache;
                }
            }else {         //如果缓存中没有，就从数据库中查询
                ExamUser examUser = new ExamUser();
                examUser.setUserId(userId);
                ExamUser e = userMapper.selectOne(examUser);
                if(e != null){
                    return e;
                }
                return new ExamUser();
            }
        }finally {
            jedis.close();
        }
        return new ExamUser();
    }

    /**
     * 用户修改信息
     * @param examUser
     * @return
     */
    @Override
    public Boolean modifyUserMsg(ExamUser examUser) {
        if(examUser == null){
            return false;
        }

        int i = userMapper.updateByPrimaryKeySelective(examUser);

        return i > 0;
    }

    /**
     * 实现注册功能
     * @param examUser
     * @return
     */
    @Override
    public boolean dealRegister(ExamUser examUser) {
        if(examUser == null){
            return false;
        }
        ExamUser e = new ExamUser();
        e.setUsername(examUser.getUsername());
        ExamUser user = userMapper.selectOne(e);
        if(user != null){
            return false;
        }
        examUser.setEffDate(new SimpleDateFormat("yyy-MM-dd").format(new Date()));
        examUser.setStatus("1");

        boolean isSuccess = userMapper.insert(examUser) > 0;

        return isSuccess;
    }



    private ExamUser loginFromDB(ExamUser examUser) {
        List<ExamUser> userList = userMapper.select(examUser);
        System.out.print(userList.toString());
        if(userList != null && userList.size() > 0){
            return userList.get(0);
        }
        return null;
    }
}












