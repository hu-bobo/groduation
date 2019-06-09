package graprojection.controller;

import com.alibaba.fastjson.JSONObject;
import graprojection.entity.FpointDTO;
import graprojection.entity.common.PageDataResult;
import graprojection.service.FpointInfoService;
import graprojection.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fpoint")
@Slf4j
public class FpointInfoController {

    @Autowired
    private FpointInfoService fpointInfoService;
//    @ApiOperation("获取样本点数据")
//    @ApiResponses({
    @PostMapping(value = "/getList")
    public PageDataResult getList(@RequestBody JSONObject json) {
        PageDataResult pdr = new PageDataResult();
        try {
            // 获取样本点列表
            pdr = fpointInfoService.getList(json);
            log.info("样本点列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("样本点列表查询异常！", e);
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
            result = fpointInfoService.getId(json);
            log.info("根据用户ID获取用户数据！gid：{0}", json);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("根据用户ID获取用户数据！", e);
        }
        return result;
    }

    /**
     * 功能描述: 更新样本点信息
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
    @PostMapping(value = "/update")
    public Map<String, Object> update(@RequestBody FpointDTO fpoint) {
        log.info("设置样本点[更新]:" + fpoint);
        Map<String, Object> data = new HashMap();
//        if (StringUtil.isEmpty(fpoint.getUserId())) {
//            data = fpointInfoService.add(fpoint);
//        } else {
//            data = fpointInfoService.update(fpoint);
//        }
        data = fpointInfoService.update(fpoint);
        return data;
    }


    @PostMapping(value = "/batchUpdateStatus")
    public Map<String, Object> batchUpdateStatus(@RequestBody List<FpointDTO> fpointList) {
        log.info("用户[状态]addOrUpdateUser！size:" + fpointList.size());
        Map<String, Object> data = fpointInfoService.batchUpdateStatus(fpointList);
        return data;
    }

    //查询图表数据
    @PostMapping(value = "/getdata")
    public Map<String, Integer> getdata(@RequestBody JSONObject json) {
        Map<String, Integer> result = new HashMap<>();

        //字典处理
        //Map mapDi = new HashMap();

        try {
            // 获取所取字段和个数
            result = fpointInfoService.getData(json);
            //result.put("pttype", "TIN_INSPECTION_PSPTTYPE");//设施种类
            //result.put("errtype", "TIN_INSPECTION_DANGERTYPE");//隐患类型
            log.info("字段和个数！=result:" + result);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("字段和个数！", e);
        }
        return result;
    }

}
