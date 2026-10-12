package com.guhecloud.cube.srv.gen.cabbage.server.third.client;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.cabbage.server.third.utils.DbcHttpUtils;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcGoodsCategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品品类client
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcGoodsCategoryClient {
    private final DbcHttpUtils dbcHttpUtils;

    /**
     * 获取商品品类列表
     *
     * @param marketId 市场id
     *
     * @return 商品品类列表
     */
    public List<DbcGoodsCategoryDTO> dbcGoodsCategoryList(Integer marketId) {
        // 商品品类请求地址
        String goodsCategoryUrl = "openApi/base/getMarketProductTypeList";
        // 入参
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("marketId", String.valueOf(marketId));
        // 请求
        Object obj = dbcHttpUtils.sendDBCPost(goodsCategoryUrl, dataMap);
        // 处理返回
        List<DbcGoodsCategoryDTO> resultList = new ArrayList<>();
        if (obj == null) {
            return resultList;
        }
        JSONObject dataObj = (JSONObject) obj;
        Object listObj = dataObj.get("list");
        if (listObj == null) {
            log.warn("三方接口返回的品类集合为空!" + dataObj.toJSONString());
            return resultList;
        }
        JSONArray jsonArray = (JSONArray) listObj;
        resultList = jsonArray.toJavaList(DbcGoodsCategoryDTO.class);
        return resultList;
    }

}
