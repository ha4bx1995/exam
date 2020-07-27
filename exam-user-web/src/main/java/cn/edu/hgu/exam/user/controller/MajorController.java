package cn.edu.hgu.exam.user.controller;

import cn.edu.hgu.exam.annotations.LoginRequired;
import cn.edu.hgu.exam.bean.ExamMajor;
import cn.edu.hgu.exam.bean.UniversityDTO;
import cn.edu.hgu.exam.service.MajorService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专业信息Controller
 */
@Controller
public class MajorController {

    @Reference
    MajorService majorService;

    /**
     * 根据专业类别编码 查询专业列表信息
     * @param attrCode
     * @return
     */
    @RequestMapping("getMajorMsgByType")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public List<ExamMajor> getMajorMsgByType(String attrCode){
        List<ExamMajor> majorList = majorService.getMajorMsgByType(attrCode);
        return majorList;
    }

    /**
     * 根据专业编码，分页查询学校信息
     * @param majorCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("getUniversityByMajorCode")
    @ResponseBody
    public Map<String,Object> getUniversityByMajorCode(String majorCode, @RequestParam(defaultValue = "1",value = "curr")Integer pageNum,
                                                        @RequestParam(defaultValue = "10",value = "limit") Integer pageSize){
        Map<String,Object> result = new HashMap<>();

        List<UniversityDTO> universityDTOList = majorService.getUniversityByMajorCode(majorCode,pageNum,pageSize);

        //查一下总记录数
        Integer count = majorService.getDataCount(majorCode);

        if(universityDTOList != null){
            result.put("data",universityDTOList);
            result.put("msg","查询成功");
            result.put("count",count);
            result.put("code",0);
        }
        return result;
    }

    /**
     * 根据学校编码查询专业信息
     * @param exUniCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("getMajorByUniCode")
    @ResponseBody
    public Map<String,Object> getMajorByUniCode(String exUniCode,@RequestParam(defaultValue = "1",value = "curr")Integer pageNum,
                                                @RequestParam(defaultValue = "10",value = "limit") Integer pageSize){

        Map<String,Object> result = new HashMap<>();

        //这里直接调用服务,返回值属于该学校的专业信息
        List<ExamMajor> examMajorList = majorService.getMajorByUniCode(exUniCode,pageNum,pageSize);

        //查一下总条数
        Integer count = majorService.getDataCountMajor(exUniCode);

        if(examMajorList != null){
            result.put("data",examMajorList);
            result.put("msg","查询成功");
            result.put("count",count);
            result.put("code",0);
        }
        return result;
    }

}
