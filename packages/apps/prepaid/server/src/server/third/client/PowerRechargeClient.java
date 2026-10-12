package com.guhecloud.cube.srv.prepaid.server.third.client;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.srv.prepaid.common.vo.PowerRechargeDTO;
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
 * 电表充值记录
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PowerRechargeClient {
    private final GhHttpUtils ghHttpUtils;

    /**
     * 获取电表充值记录列表
     *
     * @param meterId 表id(非模糊匹配
     *
     * @return 电表充值记录列表
     */
    public List<PowerRechargeDTO> getPowerRechargeList(String meterId, LocalDateTime startDate, LocalDateTime endDate) {
        String powerRechargeListUrl = "/api/Yun/Getsalepowerhistory";

        List<PowerRechargeDTO> resultList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        if (StringUtils.isNotBlank(meterId)) {
            dataMap.put("str", meterId);
        }
        if (startDate != null && endDate != null) {
            dataMap.put("startTime", DateUtils.format(startDate, "yyyy-MM-dd"));
            dataMap.put("endTime", DateUtils.format(endDate, "yyyy-MM-dd"));
        }
        long startHttp = System.currentTimeMillis();
        Object obj = ghHttpUtils.doubleRequestAgain(powerRechargeListUrl, dataMap);
        log.warn("电表充值记录第三方http请求耗时(毫秒)：{}", System.currentTimeMillis() - startHttp);
        if (obj == null) {
            return resultList;
        }
        JSONArray jsonArray = (JSONArray) obj;
        resultList = jsonArray.toJavaList(PowerRechargeDTO.class);
        return resultList;
    }
}
