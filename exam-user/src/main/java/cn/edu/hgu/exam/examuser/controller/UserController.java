package cn.edu.hgu.exam.examuser.controller;



import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.bean.UmsMember;

import cn.edu.hgu.exam.bean.UmsMemberReceiveAddress;
import cn.edu.hgu.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping("getAllUser")
    @ResponseBody
    public List<ExamUser> getAllUser(){

        List<ExamUser> umsMembers = userService.getAllUser();
        return umsMembers;
    }

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello user";
    }
}
