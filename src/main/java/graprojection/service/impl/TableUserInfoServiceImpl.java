package graprojection.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import graprojection.entity.TableUserDTO;
import graprojection.entity.common.PageDataResult;
import graprojection.jpa.model.TableUserInfo;
import graprojection.repository.TableUserInfoRepository;
import graprojection.service.TableUserInfoService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TableUserInfoServiceImpl implements TableUserInfoService {

    @Autowired
    private TableUserInfoRepository tableUserInfoRepository;

    @Override
    public Map<String,Object> check(JSONObject json){
        Map<String,Object> data = new HashMap();
        String pass = null;
        String username = json.getString("username");
        String password = json.getString("password");
        try{
         TableUserInfo old = tableUserInfoRepository.findByUsername(username);
         //String pass = old.getPassword();
         if (old == null){
             data.put("code",0);
             data.put("msg","用户名不存在，请先注册");
             log.info("用户名不存在");
             return data;
         }else if (!password.equals(pass = old.getPassword())){
             data.put("code",1);
             data.put("msg","密码错误，请重新输入");
             log.info("密码错误");
             return data;
         }else{
             data.put("code",2);
             data.put("msg","成功");
             return data;
         }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("登陆异常！", e);
            return data;
        }
    }

    @Override
    public Map<String,Object> register(JSONObject json){
        Map<String,Object> data = new HashMap();
        String pass = null;
        String username = json.getString("username");
        String password = json.getString("password");
        try{
            TableUserInfo old = tableUserInfoRepository.findByUsername(username);
            //String pass = old.getPassword();
            if (old != null){
                data.put("code",0);
                data.put("msg","用户名已存在");
                log.info("用户名已存在");
                return data;
            }else{
                TableUserInfo user = new TableUserInfo();
                user.setUsername(username);
                user.setPassword(password);
                user.setStatus("1");
                TableUserInfo save = tableUserInfoRepository.save(user);
                if (save != null) {
                    data.put("code", 2);
                    data.put("msg", "成功");
                    return data;
                }
            }
            return data;
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("异常！", e);
            return data;
        }
    }


    @Override
    public PageDataResult getList(JSONObject json) {
        PageDataResult pageDataResult = new PageDataResult();
        Integer pageSize = json.getInteger("limit");
        Integer pageNum = json.getInteger("page");
        String userid = json.getString("userid");
        String name = json.getString("name");
        String status = json.getString("sysEnable");
        Sort sort = new Sort(Sort.Direction.ASC,"userid"); //创建时间降序排序
        Pageable pageable = new PageRequest(pageNum - 1, pageSize, sort);

        Page<TableUserInfo> userPages;

        Specification querySpecifi = new Specification<TableUserInfo>(){
            @Override
            public Predicate toPredicate(Root<TableUserInfo> root, CriteriaQuery<?> criteriaQ, CriteriaBuilder criteriaB) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtil.isNotEmpty(userid)){
                    predicates.add(criteriaB.like(root.get("userid"), StringUtil.formatLike(userid)));
                }
                if(StringUtil.isNotEmpty(name)){
                    predicates.add(criteriaB.like(root.get("name"), StringUtil.formatLike(name)));
                }
                if(StringUtil.isNotEmpty(status)){
                    predicates.add(criteriaB.equal(root.get("status"), status));
                }
                return criteriaB.and(predicates.toArray(new Predicate[predicates.size()]));
            }

        };
        userPages = tableUserInfoRepository.findAll(querySpecifi, pageable);

        List<TableUserDTO> userInfoDtos = new ArrayList<>();

        try {
            PageHelper.startPage(pageNum, pageSize);
            if (userPages.getContent().size()!= 0) {
                List<TableUserInfo> userInfoList = userPages.getContent();
                userInfoDtos = CopyBeanUtil.convertList(userInfoList, TableUserDTO.class);
                PageInfo<TableUserDTO> pageInfo = new PageInfo<>(userInfoDtos);
                userInfoDtos.forEach(item->{
                    if(item.getRemark() == null) {
                        item.setRemark("");
                    }
                });
                pageDataResult.setList(userInfoDtos);
                pageDataResult.setTotals(Integer.valueOf(userPages.getTotalElements() + ""));
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
            String userid = json.getString("userid");
            TableUserInfo old = tableUserInfoRepository.findByUserid(userid);
            if(old == null){
                data.put("code",0);
                data.put("msg","用户名不存在！");
                log.error("用户[更新]，结果=用户名不存在！");
                return data;
            }
            TableUserInfo userDao = new TableUserInfo();
            BeanUtils.copyProperties(old, userDao);
            data.put("code",1);
            data.put("data", userDao);
            log.info("用户[新增]，结果=新增成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户[新增]异常！", e);
            return data;
        }
        return data;
    }

    @Override
    public Map<String,Object> add(TableUserDTO user) {
        Map<String,Object> data = new HashMap();
        try {
            TableUserInfo old = tableUserInfoRepository.findByUsername(user.getUsername());
            if(old != null){
                data.put("code",0);
                data.put("msg","用户名已存在！");
                log.error("用户[新增]，结果=用户名已存在！");
                return data;
            }
            TableUserInfo oldid = tableUserInfoRepository.findByUserid(user.getUserid());
            if(oldid != null){
                data.put("code",0);
                data.put("msg","用户编号已存在！");
                log.error("用户[新增]，结果=用户编号已存在！");
                return data;
            }
            String phone = user.getPhone();
            if(phone.length() != 11){
                data.put("code",0);
                data.put("msg","手机号位数不对！");
                log.error("用户[新增或更新]，结果=手机号位数不对！");
                return data;
            }
            user.setStatus("1");
            TableUserInfo userDao = new TableUserInfo();
            BeanUtils.copyProperties(user, userDao);

            TableUserInfo save = tableUserInfoRepository.save(userDao);
            if(save == null){
                data.put("code",0);
                data.put("msg","新增失败！");
                log.error("用户[新增]，结果=新增失败！");
                return data;
            }
            data.put("code",1);
            data.put("msg","新增成功！");
            log.info("用户[新增]，结果=新增成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户[新增]异常！", e);
            return data;
        }
        return data;
    }


    @Override
    public Map <String, Object> update(TableUserDTO user) {
        Map<String,Object> data = new HashMap();
        String id = user.getUserid();
        TableUserInfo old = tableUserInfoRepository.findByUserid(id);
        if(old == null){
            data.put("code",0);
            data.put("msg","用户名不存在！");
            log.error("用户[更新]，结果=用户名已存在！");
            return data;
        }
        TableUserInfo userDao = new TableUserInfo();
        CopyBeanUtil.copyProperties(old, userDao);
        CopyBeanUtil.copyProperties(user, userDao);
        TableUserInfo result = tableUserInfoRepository.save(userDao);
        if(result == null){
            data.put("code",0);
            data.put("msg","更新失败！");
            log.error("用户[更新]，结果=更新失败！");
            return data;
        }
        data.put("code",1);
        data.put("msg","更新成功！");
        log.info("用户[更新]，结果=更新成功！");
        return data;
    }

    @Override
    public Map<String, Object> batchUpdateStatus(List<TableUserDTO> userList) {
        Map<String,Object> data = new HashMap();
        try {
            for(int i = 0; i < userList.size(); i++) {
                TableUserDTO user = userList.get(i);
                this.update(user);
            }
            data.put("code",1);
            data.put("msg", "状态更新成功！");
            log.info("用户[状态更新]，结果=状态更新！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户[新增]异常！", e);
            return data;
        }
        return data;
    }

}
