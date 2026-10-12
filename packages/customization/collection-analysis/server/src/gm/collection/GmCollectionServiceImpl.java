package com.guhecloud.cube.biz.collection.analysis.gm.collection;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DataDictionaryEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryDayDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryWeekDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.TransportDayDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.TransportWeekDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-16  11:47
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
public class GmCollectionServiceImpl implements GmCollectionService {

    @Override
    public ParkSettleCategoryDayDTO getParkSettleCategoryDay(List<DataDictionaryDTO> param, String deliveryDate) {
        for (DataDictionaryDTO ex : param) {
            if (DataDictionaryEnum.PARK_SETTLE_CATEGORY_DAY.name().equals(ex.getCode())) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("bgnTime", deliveryDate);
                String result = HttpUtil.post(ex.getVariable(), JSON.toJSONString(paramMap));
                log.info("result:{}", result);
                Map<String, Object> map = JsonUtils.deserialize(result, Map.class);
                return JsonUtils.deserialize(map.get("data"), ParkSettleCategoryDayDTO.class);
            }
        }
        return null;
    }

    @Override
    public List<ParkSettleCategoryWeekDTO> parkSettleCategoryWeek(List<DataDictionaryDTO> param, String bgnTime, String endTime) {
        for (DataDictionaryDTO ex : param) {
            if (DataDictionaryEnum.PARK_SETTLE_CATEGORY_WEEK.name().equals(ex.getCode())) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("bgnTime", bgnTime);
                paramMap.put("endTime", endTime);
                String result = HttpUtil.post(ex.getVariable(), JSON.toJSONString(paramMap));
                log.info("result:{}", result);
                Map<String, Object> map = JsonUtils.deserialize(result, Map.class);
                return JsonUtils.deserialize(map.get("data"), new TypeReference<List<ParkSettleCategoryWeekDTO>>() {
                });
            }
        }
        return null;
    }

    @Override
    public TransportDayDTO transportDay(List<DataDictionaryDTO> param, String bgnTime, String endTime) {
        for (DataDictionaryDTO ex : param) {
            if (DataDictionaryEnum.PARK_STAFF_WORK_DAY.name().equals(ex.getCode())) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("bgnTime", bgnTime);
                paramMap.put("endTime", endTime);
                String result = HttpUtil.post(ex.getVariable(), JSON.toJSONString(paramMap));
                log.info("result:{}", result);
                Map<String, Object> map = JsonUtils.deserialize(result, Map.class);
                return JsonUtils.deserialize(map.get("data"), TransportDayDTO.class);
            }
        }
        return null;
    }

    @Override
    public List<TransportWeekDTO> transportWeek(List<DataDictionaryDTO> param, String bgnTime, String endTime) {
        for (DataDictionaryDTO ex : param) {
            if (DataDictionaryEnum.PARK_STAFF_WORK_WEEK.name().equals(ex.getCode())) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("bgnDate", bgnTime);
                paramMap.put("endDate", endTime);
                String result = HttpUtil.post(ex.getVariable(), JSON.toJSONString(paramMap));
                log.info("result:{}", result);
                Map<String, Object> map = JsonUtils.deserialize(result, Map.class);
                return JsonUtils.deserialize(map.get("data"), new TypeReference<List<TransportWeekDTO>>() {
                });
            }
        }
        return null;
    }
}
