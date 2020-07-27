package cn.edu.hgu.exam.service;

import cn.edu.hgu.exam.bean.ExamCollection;
import cn.edu.hgu.exam.bean.UniversityDTO;

import java.util.List;
import java.util.Map;

/**
 * 用户收藏服务
 */
public interface CollectService {

    /**
     * 点击收藏按钮
     * @param examCollection
     * @return
     */
    public Map<String,Object> collectCollage(ExamCollection examCollection);

    /**
     * 查询当前登录用户下的收藏信息
     * @param userId
     * @return
     */
    List<UniversityDTO> getCollectMsgByUserId(String userId,Integer pageNum,Integer pageSize);

    /**
     * 取消用户收藏
     * @param exUniCode
     * @param userId
     * @return
     */
    Map<String,Object> cancelCollectCollage(String exUniCode, String userId);
}
