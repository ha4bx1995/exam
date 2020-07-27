package cn.edu.hgu.exam.service;

import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.bean.UmsMember;
import cn.edu.hgu.exam.bean.UmsMemberReceiveAddress;

import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * 查询所有用户接口
     * @return
     */
    List<ExamUser> getAllUser();


    /**
     * 用户主动登录接口
     * @param examUser
     * @return
     */
    ExamUser login(ExamUser examUser);

    /**
     * 向redis中写入token
     * @param token
     * @param userId
     */
    void addUserToken(String token, String userId);

    /**
     * 注册接口
     * @param examUser
     * @return
     */
    boolean dealRegister(ExamUser examUser);

    /**
     * 将用户信息添加进缓存
     * @param examUserLogin
     */
    void addUserInfoToCache(ExamUser examUserLogin);

    /**
     * 用户登录成功之后判断用户是否是第一次登录
     * @param userId
     * @return
     */
    Boolean isFirstLogin(String userId);

    /**
     * 查询用户的基本信息
     * @param userId
     * @return
     */
    ExamUser getOneUserByUserId(String userId);

    /**
     * 修改用户信息
     * @param examUser
     * @return
     */
    Boolean modifyUserMsg(ExamUser examUser);
}
