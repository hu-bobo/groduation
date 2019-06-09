package graprojection.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import graprojection.entity.TableUserDTO;
import graprojection.entity.common.PageDataResult;

import java.util.List;
import java.util.Map;

public interface TableUserInfoService {
    Map<String,Object> check(JSONObject json);
    Map<String,Object> register(JSONObject json);

    PageDataResult getList(JSONObject json);

    Map<String,Object> getId(JSONObject json);

    Map<String, Object> add(TableUserDTO user);

    Map<String,Object> update(TableUserDTO user);

    Map<String, Object> batchUpdateStatus(List<TableUserDTO> userList);

}
