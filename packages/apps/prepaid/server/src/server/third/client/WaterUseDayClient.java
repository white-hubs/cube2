package com.guhecloud.cube.srv.prepaid.server.third.client;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.cube.srv.prepaid.common.vo.WaterUseDayDTO;
import com.guhecloud.cube.srv.prepaid.server.third.utils.GhHttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 水表日用能
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WaterUseDayClient {
    private final GhHttpUtils ghHttpUtils;

    /**
     * 水表日用能列表
     *
     * @param meterId 表id(非模糊匹配
     *
     * @return 水表日用能列表
     */
    public List<WaterUseDayDTO> getWaterUseDayList(String meterId, String startDate, String endDate) {
        String waterUseDayListUrl = "/api/Yun/GetEnergyData2";

        List<WaterUseDayDTO> resultList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        if (StringUtils.isNotBlank(meterId)) {
            dataMap.put("meterId", meterId);
        }
        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            dataMap.put("startTime", startDate);
            dataMap.put("endTime", endDate);
        }
        long startHttp = System.currentTimeMillis();
        Object obj = ghHttpUtils.doubleRequestAgain(waterUseDayListUrl, dataMap);
        log.warn("水表日用能第三方http请求耗时(毫秒)：{}", System.currentTimeMillis() - startHttp);
        if (obj == null) {
            return resultList;
        }
        JSONArray jsonArray = (JSONArray) obj;
        resultList = jsonArray.toJavaList(WaterUseDayDTO.class);
        return resultList;
    }
}
