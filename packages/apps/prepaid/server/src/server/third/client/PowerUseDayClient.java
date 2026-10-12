package com.guhecloud.cube.srv.prepaid.server.third.client;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.srv.prepaid.common.vo.PowerUseDayDTO;
import com.guhecloud.cube.srv.prepaid.server.third.utils.GhHttpUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电表日用能
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PowerUseDayClient {
    private final GhHttpUtils ghHttpUtils;

    /**
     * 获取电表日用能列表
     *
     * @param meterId 表id(非模糊匹配
     *
     * @return 电表日用能列表
     */
    public List<PowerUseDayDTO> getPowerUseDayList(String meterId, LocalDateTime startDate, LocalDateTime endDate) {
        String powerUseDayListUrl = "/api/Yun/GetEnergyData2";

        List<PowerUseDayDTO> resultList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        if (StringUtils.isNotBlank(meterId)) {
            dataMap.put("MeterId", meterId);
        }
        if (startDate != null && endDate != null) {
            dataMap.put("StartDate", DateUtils.formatDate(startDate));
            dataMap.put("endDate", DateUtils.formatDate(endDate));
        }
        long startHttp = System.currentTimeMillis();
        Object obj = ghHttpUtils.doubleRequestAgain(powerUseDayListUrl, dataMap);
        log.warn("电表第三方http请求耗时(毫秒)：{}", System.currentTimeMillis() - startHttp);
        if (obj == null) {
            return resultList;
        }
        JSONArray jsonArray = (JSONArray) obj;
        resultList = jsonArray.toJavaList(PowerUseDayDTO.class);
        return resultList;
    }
}
