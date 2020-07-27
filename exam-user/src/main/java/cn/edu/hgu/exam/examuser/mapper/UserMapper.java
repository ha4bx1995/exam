package cn.edu.hgu.exam.examuser.mapper;

import cn.edu.hgu.exam.bean.ExamUser;
import cn.edu.hgu.exam.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<ExamUser> {
    /**
     * 查询所有用户sql
     * @return
     */
    List<ExamUser> selectAllUser();
}
