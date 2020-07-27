package cn.edu.hgu.exam.examuser.service.impl;


import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.bean.UmsMember;
import cn.edu.hgu.exam.bean.UmsMemberReceiveAddress;
import cn.edu.hgu.exam.examuser.mapper.UmsMemberReceiveAddressMapper;
import cn.edu.hgu.exam.examuser.mapper.UserMapper;
import cn.edu.hgu.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<ExamUser> getAllUser() {
        List<ExamUser> umsMemverList = userMapper.selectAll(); //  userMapper.selectAllUser();
        return umsMemverList;
    }

    @Override
    public ExamUser login(ExamUser examUser) {
        return null;
    }

    @Override
    public void addUserToken(String token, String userId) {

    }

    @Override
    public boolean dealRegister(ExamUser examUser) {
        return false;
    }

    @Override
    public void addUserInfoToCache(ExamUser examUserLogin) {

    }

    @Override
    public Boolean isFirstLogin(String userId) {
        return null;
    }

}
