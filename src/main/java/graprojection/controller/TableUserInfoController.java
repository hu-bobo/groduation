package graprojection.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.github.pagehelper.util.StringUtil;
import graprojection.entity.TableUserDTO;
import graprojection.entity.common.PageDataResult;
import graprojection.service.TableUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class TableUserInfoController {

    @Autowired
    private TableUserInfoService tableUserInfoService;

    @PostMapping(value = "/register")
    public Map<String,Object> register(@RequestBody JSONObject json){
        Map<String, Object> data = new HashMap();

        data = tableUserInfoService.register(json);

        return data;
    }

    @PostMapping(value = "/check")
    public Map<String,Object> check(@RequestBody JSONObject json){
        Map<String, Object> data = new HashMap();

        data = tableUserInfoService.check(json);

        return data;
    }


    /**
     * 功能描述: 分页查询用户列表
     */
//    @ApiOperation("获取用户数据")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 400, message = "请求参数没填好"),
//            @ApiResponse(code = 401, message = "未授权用户"),
//            @ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
//            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "json", dataType = "object", required = true, value = "json", defaultValue = ""),
//    })
    @PostMapping(value = "/getList")
    public PageDataResult getList(@RequestBody JSONObject json) {
        PageDataResult pdr = new PageDataResult();
        try {
            // 获取用户列表
            pdr = tableUserInfoService.getList(json);
            log.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户列表查询异常！", e);
        }
        return pdr;
    }


    /**
     * 功能描述: 根据userID查询用户
     */
//    @ApiOperation("根据userID查询用户")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 400, message = "请求参数没填好"),
//            @ApiResponse(code = 401, message = "未授权用户"),
//            @ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
//            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "json", dataType = "object", required = true, value = "json", defaultValue = ""),
//    })
    @PostMapping(value = "/getId")
    public Map<String, Object> getId(@RequestBody JSONObject json) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = tableUserInfoService.getId(json);
            log.info("根据用户ID获取用户数据！userId：{0}", json);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据用户ID获取用户数据！", e);
        }
        return result;
    }


    /**
     * 功能描述: 新增和更新系统用户
     */
//    @ApiOperation("新增和更新系统用户")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 400, message = "请求参数没填好"),
//            @ApiResponse(code = 401, message = "未授权用户"),
//            @ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
//            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "user", dataType = "UserInfo", required = true, value = "用户信息", defaultValue = ""),
//    })
    @PostMapping(value = "/addOrUpdate")
    public Map<String, Object> addOrUpdateUser(@RequestBody TableUserDTO user) {
        log.info("设置用户[新增或更新]addUser！user:" + user);
        Map<String, Object> data = new HashMap();
        if (StringUtil.isEmpty(user.getId())) {
            data = tableUserInfoService.add(user);
        } else {
            data = tableUserInfoService.update(user);
        }
        return data;
    }

    @PostMapping(value = "/batchUpdateStatus")
    public Map<String, Object> batchUpdateStatus(@RequestBody List<TableUserDTO> userList) {
        log.info("用户[状态]addOrUpdateUser！size:" + userList.size());
        Map<String, Object> data = tableUserInfoService.batchUpdateStatus(userList);
        return data;
    }


}
