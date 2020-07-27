package cn.edu.hgu.exam.manager.service;

import cn.edu.hgu.exam.bean.ExamAttrSpec;
import cn.edu.hgu.exam.bean.ExamAttrValue;
import cn.edu.hgu.exam.manager.mapper.StaticDataMapper;
import cn.edu.hgu.exam.manager.mapper.StaticDataValueMapper;
import cn.edu.hgu.exam.service.StaticDataService;
import cn.edu.hgu.util.RedisUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class StaticDataServiceImpl implements StaticDataService {

    @Autowired
    StaticDataMapper staticDataMapper;

    @Autowired
    StaticDataValueMapper staticDataValueMapper;

    @Autowired
    RedisUtil redisUtil;
    /**
     * 加载静态数据服务
     * @return 静态数据json字符串
     */
    private String getStaticData() {
        Map<String,Object> staticDataFinal = new HashMap<>();
        Map<String,Object> result = new HashMap<>();
        //先查询缓存
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String staticJson = jedis.get("staticData");
            if(StringUtils.isNotBlank(staticJson)){
                return staticJson;
            }else {
                //查询数据库，先加载所有静态资源属性
                List<ExamAttrSpec> examAttrSpecs = staticDataMapper.selectAll();
                Map<String,Object> examAttrValue = new HashMap<>();
                //从list中遍历到每个属性，使用attrCode查询attrValue获取属性值
                ExamAttrValue e = new ExamAttrValue();
                examAttrSpecs.stream().forEach(item -> {
                            e.setAttrCode(item.getAttrCode());
                            List<ExamAttrValue> attrValues = staticDataValueMapper.select(e);
                            examAttrValue.put(item.getAttrCode(),attrValues);
                        }
                );

                staticDataFinal.put("staticDataSpec",examAttrSpecs);
                staticDataFinal.put("staticDataVal",examAttrValue);
                //查询完毕后，将数据写入缓存
                String staticDataJson = JSON.toJSONString(staticDataFinal);
                jedis.setex("staticData",60*60*24,staticDataJson);
            }
        }finally {
            jedis.close();
        }
        return JSON.toJSONString(staticDataFinal);
    }


    @Override
    public Map<String, Object> getStaticDataDis() {
       // Map<String, Object> staticDataAll = getStaticData();
        String staticDataAll = getStaticData();   //从缓存中拿到json串
        List<ExamAttrSpec> examAttrSpecs = JSON.parseArray(JSON.parseObject(staticDataAll).getString("staticDataSpec"),ExamAttrSpec.class);

        //用户存储最终返回结果
        Map<String, Object> staticDataFinal = new HashMap<>();

        //用户区分不同的遍历结果
        List<ExamAttrSpec> listProvince = new ArrayList<>();
        List<ExamAttrValue> listUniType = new ArrayList<>();
        List<ExamAttrValue> listUniLevel = new ArrayList<>();
        List<ExamAttrValue> listUniNature = new ArrayList<>();
        List<ExamAttrValue> listUniGrade = new ArrayList<>();
        ExamAttrValue e = new ExamAttrValue();


        //这里直接遍历spec  list集合，将省份集合返回
        examAttrSpecs.forEach(item -> {
            Integer attrCode = Integer.valueOf(item.getAttrCode());
            if(attrCode >= 10000 && attrCode <= 20000){
                listProvince.add(item);
            }
            //如果是20000-30000区间 大学学校类型 根据id去查询value表
            if(attrCode >=20001 && attrCode <= 30000){
                e.setAttrCode(item.getAttrCode());
                listUniType.addAll(staticDataValueMapper.select(e));
            }
            //如果是介于30000-40000之间，办学层次 根据id去查询value表
            if(attrCode >= 30001 && attrCode <= 40000){
                e.setAttrCode(item.getAttrCode());
                listUniLevel.addAll(staticDataValueMapper.select(e));
            }

            //如果是介于40000-50000之间 办学类型 根据id去查询value表
            if(attrCode >= 40001 && attrCode <= 50000){
                e.setAttrCode(item.getAttrCode());
                listUniNature.addAll(staticDataValueMapper.select(e));
            }

            //如果是介于50000-60000之间，学校档次 根据id去查询value表
            if(attrCode >= 50001 && attrCode <= 60000){
                e.setAttrCode(item.getAttrCode());
                listUniGrade.addAll(staticDataValueMapper.select(e));
            }
        });

        //将所有数据使用map返回
        staticDataFinal.put("listProvince",listProvince);
        staticDataFinal.put("listUniType",listUniType);
        staticDataFinal.put("listUniLevel",listUniLevel);
        staticDataFinal.put("listUniNature",listUniNature);
        staticDataFinal.put("listUniGrade",listUniGrade);

        //城市的需要根据所选省份动态加载，这里不需要返回


        return staticDataFinal;
    }

    @Override
    public List<ExamAttrValue> getCityByAttrCode(String attrCode) {
        ExamAttrValue e = new ExamAttrValue();
        e.setAttrCode(attrCode);
        List<ExamAttrValue> cityStaticData = staticDataValueMapper.select(e);
        return cityStaticData;
    }

    /**
     * 加载专业类别静态字段
     * @return
     */
    @Override
    public List<ExamAttrSpec> getStaticMajorType() {
        System.out.println("访问服务数据接口了");
        String staticDataAll = getStaticData();   //直接从缓存中拿到json串
        //接着从所有的静态数据中拿到属性信息
        List<ExamAttrSpec> examAttrSpecs = JSON.parseArray(JSON.parseObject(staticDataAll).getString("staticDataSpec"),ExamAttrSpec.class);

        List<ExamAttrSpec> examMajorTypeList = new ArrayList<>();

        //遍历属性信息，从中拿到专业类别信息 返回
        examAttrSpecs.forEach(item -> {
            Integer attrCode = Integer.valueOf(item.getAttrCode());
            //如果是介于60000-70000之间，专业类别信息 直接返回
            if(attrCode >= 60001 && attrCode <= 70000){
                examMajorTypeList.add(item);
            }
        });

        return examMajorTypeList;
    }

    /**
     * 提供spec表的静态字段转换功能,判断依据是表中的attrCode字段
     * @param attrCode
     * @return
     */
    public String transferStaticFieldSpec(String attrCode){
        //先从缓存中获取spec静态资源数据
        String staticDataAll = getStaticData();
        //将spec静态数据转换为list集合
        List<ExamAttrSpec> examAttrSpecs = JSON.parseArray(JSON.parseObject(staticDataAll).getString("staticDataSpec"),ExamAttrSpec.class);
        String attrSpecName = "";
        try{
            attrSpecName = examAttrSpecs.stream().filter(item -> (item.getAttrCode().equals(attrCode))).collect(Collectors.toList()).get(0).getAttrSpecName();
        }catch (Exception e){
            System.out.println("数据库数据字段设置有问题：匹配不到编码信息->"+attrCode);
        }

        if(StringUtils.isBlank(attrSpecName)){
            attrSpecName = attrCode;
        }
        return attrSpecName;
    }

    /**
     * 提供value表的静态字段转换功能，判断依据是表中的id值
     */

    public String transferStaticFieldValue(String attrValueId){
        //先从缓存中拿value静态资源
        String staticDataAll = getStaticData();

        JSONObject jsonObject = JSONObject.parseObject(staticDataAll);
        Map<String, Object> map = (Map<String,Object>)jsonObject;

        List<ExamAttrSpec> staticDataSpec = (List)map.get("staticDataSpec");
        Map<String,Object> staticDataValue = (Map<String,Object>)map.get("staticDataVal");

        //这里需要遍历整个map集合，从中查找该字段的信息
        for(Map.Entry<String,Object> entry : staticDataValue.entrySet()){
            //获取每个元素的值，是一个list集合
            List<Map<String,Object>> staticValList = (List)entry.getValue();
           // Map<String, Object> stringObjectMap = staticValList.stream().filter(item -> (JSONObject.parseObject(jsonObject.toJSONString(item), ExamAttrValue.class).getAttrValueId().equals(attrValueId))).collect(Collectors.toList()).get(0);
            //遍历list集合，将list中的map转为对象 做判断
            for(Map<String,Object> item : staticValList){
                ExamAttrValue examAttrValue = JSONObject.parseObject(jsonObject.toJSONString(item), ExamAttrValue.class);
                if(examAttrValue.getAttrValueId().equals(attrValueId)){
                    return examAttrValue.getAttrValueName();
                }
            }
        }

        //这里暂定为一般的遍历方式，
//        String attrValueName = examAttrValues.stream().filter(item -> (item.getAttrValueId().equals(attrValueId))).collect(Collectors.toList()).get(0).getAttrValueName();
//
//        if(StringUtils.isBlank(attrValueName)){
//            attrValueName = attrValueId;
//        }
        return attrValueId;
    }


}
