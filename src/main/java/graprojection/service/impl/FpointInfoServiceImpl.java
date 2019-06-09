package graprojection.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import graprojection.entity.FpoingBl;
import graprojection.entity.FpointDTO;
import graprojection.entity.common.PageDataResult;
import graprojection.jpa.model.FpointInfo;
import graprojection.jpa.model.TableUserInfo;
import graprojection.repository.FpointInfoRepository;
import graprojection.repository.TableUserInfoRepository;
import graprojection.service.FpointInfoService;
import graprojection.utils.CopyBeanUtil;
import graprojection.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class FpointInfoServiceImpl implements FpointInfoService {
    @Autowired
    private FpointInfoRepository fpointInfoRepository;
    @Autowired
    private TableUserInfoRepository tableUserInfoRepository;


    @Override
    public PageDataResult getList(JSONObject json) {
        PageDataResult pageDataResult = new PageDataResult();
        Integer pageSize = json.getInteger("limit");
        Integer pageNum = json.getInteger("page");
        String optuser = json.getString("optuser");
        //String gid = Long.toString(gidd);
        String name = json.getString("name");
        //String status = json.getString("sysEnable");
        Sort sort = new Sort(Sort.Direction.ASC,"gid"); //创建时间降序排序
        Pageable pageable = new PageRequest(pageNum - 1, pageSize, sort);

        Page<FpointInfo> fpointPages;



        Specification querySpecifi = new Specification<FpointInfo>(){
            @Override
            public Predicate toPredicate(Root<FpointInfo> root, CriteriaQuery<?> criteriaQ, CriteriaBuilder criteriaB) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtil.isNotEmpty(optuser)){
                    predicates.add(criteriaB.like(root.get("optuser"), StringUtil.formatLike(optuser)));
                }
                if(StringUtil.isNotEmpty(name)){
                    predicates.add(criteriaB.like(root.get("name"), StringUtil.formatLike(name)));
                }
//                if(StringUtil.isNotEmpty(status)){
//                    predicates.add(criteriaB.equal(root.get("status"), status));
//                }
                return criteriaB.and(predicates.toArray(new Predicate[predicates.size()]));
            }

        };
        fpointPages = fpointInfoRepository.findAll(querySpecifi, pageable);

        List<FpointDTO> fpointDTOS = new ArrayList<>();

        try {
            PageHelper.startPage(pageNum, pageSize);
            if (fpointPages.getContent().size()!= 0) {
                List<FpointInfo> fpointInfoList = fpointPages.getContent();
                fpointDTOS = CopyBeanUtil.convertList(fpointInfoList, FpointDTO.class);
                PageInfo<FpointDTO> pageInfo = new PageInfo<>(fpointDTOS);
                fpointDTOS.forEach(item->{
                    if(item.getRemark() == null) {
                        item.setRemark("");
                    }
                });
                pageDataResult.setList(fpointDTOS);
                pageDataResult.setTotals(Integer.valueOf(fpointPages.getTotalElements() + ""));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();

        }
        return pageDataResult;
    }


    @Override
    public Map<String, Object> getId(JSONObject json) {
        Map<String,Object> data = new HashMap();
        try {
            Long gid = json.getLong("gid");
            FpointInfo old = fpointInfoRepository.findByGid(gid);
            if(old == null){
                data.put("code",0);
                data.put("msg","点编号不存在！");
                log.error("用户[更新]，结果=点编号不存在！");
                return data;
            }
            FpointInfo fpointInfo = new FpointInfo();
            BeanUtils.copyProperties(old, fpointInfo);
            data.put("code",1);
            data.put("data", fpointInfo);
            log.info("样本点[新增]，结果=新增成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("样本点[新增]异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public Map <String, Object> update(FpointDTO fpoint) {
        Map<String,Object> data = new HashMap();
        Long gid = fpoint.getGid();
        FpointInfo old = fpointInfoRepository.findByGid(gid);
//        if(old == null){
//            data.put("code",0);
//            data.put("msg","用户名不存在！");
//            log.error("用户[更新]，结果=用户名已存在！");
//            return data;
//        }
        FpointInfo fpointDao = new FpointInfo();
        CopyBeanUtil.copyProperties(old, fpointDao);
        CopyBeanUtil.copyProperties(fpoint, fpointDao);
        FpointInfo result = fpointInfoRepository.save(fpointDao);
        if(result == null){
            data.put("code",0);
            data.put("msg","更新失败！");
            log.error("样本点[更新]，结果=更新失败！");
            return data;
        }
        data.put("code",1);
        data.put("msg","更新成功！");
        log.info("样本点[更新]，结果=更新成功！");
        return data;
    }


    @Override
    public Map<String, Object> batchUpdateStatus(List<FpointDTO> fpointList) {
        Map<String,Object> data = new HashMap();
        try {
            for(int i = 0; i < fpointList.size(); i++) {
                FpointDTO fpoint = fpointList.get(i);
                this.update(fpoint);
            }
            data.put("code",1);
            data.put("msg", "状态更新成功！");
            log.info("样本点[状态更新]，结果=状态更新！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("样本点[新增]异常！", e);
            return data;
        }
        return data;
    }


    /**
     *  获取字段和个数生成统计图（er个统计图共用）
     */
    //@Override
    public Map<String, Integer> getData(JSONObject json) {
        Date starttime = json.getDate("starttime");
        Date endtime = json.getDate("endtime");
        String type = json.getString("type");

        if (starttime == null && endtime == null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Calendar c = Calendar.getInstance();

            //过去七天
            c.setTime(new Date());

            endtime = c.getTime();//new Date();
            c.add(Calendar.MONTH, -1);
            starttime = c.getTime();//new Date(System.currentTimeMillis()+30000*60*60*24);
        }
        List<FpointInfo> eventsInfoList = new ArrayList<>();
        List<Object[]> data = new ArrayList<>();
        switch (type) {
            case "category":
                data = fpointInfoRepository.findData(starttime, endtime);
                break;
            case "user":
                data = fpointInfoRepository.finduserData(starttime, endtime);
                break;
            default:
                break;
        }
        Map<String, Integer> result = new HashMap<>();

        for(int i=0;i < data.size();i++) {
            if (type.equals("category")) {

                Object[] n = data.get(i);
                if ( n[0] == null) {
                    break;
                }

                String teamName = (String) n[0];

                Integer teamMemberSum = Integer.valueOf(n[1].toString());

                result.put(teamName, teamMemberSum);
            }else{
                Object[] n = data.get(i);
                if ( n[0] == null) {
                    break;
                }

                TableUserInfo old = tableUserInfoRepository.findByUsername((String) n[0]);
                String teamName = old.getName();


                Integer teamMemberSum = Integer.valueOf(n[1].toString());

                result.put(teamName, teamMemberSum);
            }
        }

        log.info("统计result的值"+result);
        return result;
    }


}
