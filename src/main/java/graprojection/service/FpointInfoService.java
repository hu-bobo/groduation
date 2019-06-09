package graprojection.service;

import com.alibaba.fastjson.JSONObject;
import graprojection.entity.FpointDTO;
import graprojection.entity.common.PageDataResult;
import graprojection.jpa.model.FpointInfo;

import java.util.List;
import java.util.Map;

public interface FpointInfoService {
    PageDataResult getList(JSONObject json);

    Map<String,Object> getId(JSONObject json);

    Map<String,Object> update(FpointDTO fpoint);

    Map<String, Object> batchUpdateStatus(List<FpointDTO> fpointList);

    Map<String, Integer> getData(JSONObject json);
}
