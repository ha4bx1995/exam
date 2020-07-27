package cn.edu.hgu.exam.manager.service;

import cn.edu.hgu.exam.bean.ExamMajor;
import cn.edu.hgu.exam.bean.ExamMajorCategory;
import cn.edu.hgu.exam.bean.ExamUniMajorRel;
import cn.edu.hgu.exam.bean.UniversityDTO;
import cn.edu.hgu.exam.manager.mapper.MajorCategoryMapper;
import cn.edu.hgu.exam.manager.mapper.MajorMapper;
import cn.edu.hgu.exam.manager.mapper.MajorUniRelMapper;
import cn.edu.hgu.exam.manager.mapper.ManagerMapper;
import cn.edu.hgu.exam.service.MajorService;
import cn.edu.hgu.exam.service.StaticDataService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MajorServiceImpl implements MajorService {

    @Autowired
    MajorMapper majorMapper;

    @Autowired
    MajorUniRelMapper majorUniRelMapper;

    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    StaticDataService staticDataService;

    @Autowired
    MajorCategoryMapper majorCategoryMapper;
    /**
     * 根据专业的类别信息加载专业列表
     * @param attrCode
     * @return
     */
    @Override
    public List<ExamMajor> getMajorMsgByType(String attrCode) {
        ExamMajor examMajor = new ExamMajor();
        examMajor.setExMajorCate(attrCode);
        List<ExamMajor> examMajors = majorMapper.select(examMajor);
        return examMajors;
    }

    /**
     * 根据专业编码查询专业学校信息
     * @param majorCode
     * @return
     */
    @Override
    public List<UniversityDTO> getUniversityByMajorCode(String majorCode,Integer pageNum,Integer pageSize) {

        List<UniversityDTO> result = new ArrayList<>();

        ExamUniMajorRel examUniMajorRel = new ExamUniMajorRel();
        examUniMajorRel.setExMajorCode(majorCode);

        PageHelper.startPage(pageNum,pageSize);
        List<ExamUniMajorRel> examUniMajorRels = majorUniRelMapper.select(examUniMajorRel);
        PageInfo<ExamUniMajorRel> relPageInfo = new PageInfo<>(examUniMajorRels);

        //查询出来所有的关联信息，
        relPageInfo.getList().forEach(item -> {
            UniversityDTO universityDTO = managerMapper.selectUniByCode(item.getExUniCode());
            //开始转换数据
            universityDTO.setExUniProvince(staticDataService.transferStaticFieldSpec(universityDTO.getExUniProvince()));
            universityDTO.setExUniCity(staticDataService.transferStaticFieldValue(universityDTO.getExUniCity()));
            universityDTO.setExUniType(staticDataService.transferStaticFieldValue(universityDTO.getExUniType()));
            universityDTO.setExUniNature(staticDataService.transferStaticFieldValue(universityDTO.getExUniNature()));
            universityDTO.setExUniLevel(staticDataService.transferStaticFieldValue(universityDTO.getExUniLevel()));
            universityDTO.setExUniGrade(staticDataService.transferStaticFieldValue(universityDTO.getExUniGrade()));
            //转换完成之后将对象添加进列表
            result.add(universityDTO);
        });
        return result;
    }

    /**
     * 根据学校编码查询属于该学校的专业信息
     * @param exUniCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<ExamMajor> getMajorByUniCode(String exUniCode, Integer pageNum, Integer pageSize) {

        List<ExamMajor> result = new ArrayList<>();

        ExamUniMajorRel examUniMajorRel = new ExamUniMajorRel();
        examUniMajorRel.setExUniCode(exUniCode);
        //进行分页查询
        PageHelper.startPage(pageNum,pageSize);
        List<ExamUniMajorRel> examUniMajorRels = majorUniRelMapper.select(examUniMajorRel);
        PageInfo<ExamUniMajorRel> relPageInfo = new PageInfo<>(examUniMajorRels);

        //返回的是一个包含该学校所有的关联专业信息的列表
        ExamMajor examMajor = new ExamMajor();
        ExamMajorCategory examMajorCategory = new ExamMajorCategory();
        if(relPageInfo != null){
            relPageInfo.getList().forEach(item -> {
                examMajor.setExMajorCode(item.getExMajorCode());
                ExamMajor major = majorMapper.selectOne(examMajor);
                //进行静态字段转换 专业类别没设置缓存 所以需要去库里查
                major.setExMajorCate(staticDataService.transferStaticFieldSpec(major.getExMajorCate()));
                String isHot = major.getExMajorHot().equals("1") ? "火热" : "一般";
                major.setExMajorHot(isHot);
                result.add(major);
            });
        }
        return result;
    }

    @Override
    public Integer getDataCount(String majorCode) {
        ExamUniMajorRel examUniMajorRel = new ExamUniMajorRel();
        examUniMajorRel.setExMajorCode(majorCode);
        int i = majorUniRelMapper.selectCount(examUniMajorRel);
        return i > 0 ? i : 0;
    }

    @Override
    public Integer getDataCountMajor(String exUniCode) {
        ExamUniMajorRel examUniMajorRel = new ExamUniMajorRel();
        examUniMajorRel.setExUniCode(exUniCode);
        Integer i = majorUniRelMapper.selectCount(examUniMajorRel);

        return i > 0 ? i : 0;
    }
}
