package com.guhecloud.cube.srv.gen.cabbage.server.third.client;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.cabbage.server.third.utils.DbcHttpUtils;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcVehicleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆client
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcVehicleClient {

    private final DbcHttpUtils dbcHttpUtils;

    /**
     * 获取大白菜车辆集合
     *
     * @param marketId 市场id
     *
     * @return 车辆集合
     */
    public List<DbcVehicleDTO> dbcVehicleList(Integer marketId) {
        // 车辆请求地址
        String vehicleUrl = "openApi/base/getVehicleList";
        // 入参
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("marketId", String.valueOf(marketId));
        // 请求
        Object obj = dbcHttpUtils.sendDBCPost(vehicleUrl, dataMap);
        // 处理返回
        List<DbcVehicleDTO> resultList = new ArrayList<>();
        if (obj == null) {
            return resultList;
        }
        JSONObject dataObj = (JSONObject) obj;
        Object listObj = dataObj.get("list");
        if (listObj == null) {
            return resultList;
        }
        JSONArray jsonArray = (JSONArray) listObj;
        resultList = jsonArray.toJavaList(DbcVehicleDTO.class);
        return resultList;
    }
}
