package com.guhecloud.cube.biz.collection.analysis.gm.repository.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DataDictionaryEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DictionaryItemEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.VehicleNatureEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.VehicleDayStatisticsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.VehicleDayStatisticsPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleDayStatisticsRepository extends ServiceImpl<VehicleDayStatisticsMapper, VehicleDayStatisticsPO> {

    private final DataDictionaryAdapter dataDictionaryAdapter;

    /**
     * 近一日车流分布图
     *
     * @param queryDate 日期（yyyy-MM-dd）
     * @param tenant    租户
     * @param nowTime   当前时间
     * @param operator  操作人
     *
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean VehicleDayPull(String queryDate, Integer tenant, LocalDateTime nowTime, String operator, String userId,
                                  ActorTypeEnum userType
                                 ) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("queryDate", queryDate);
        Map<String, Object> map = this.getHttpMap(DictionaryItemEnum.GM_VISUALIZATION_DATA_URL.name(), DataDictionaryEnum.PARK_TRAFFIC_STATISTIC_INFO.name(), paramMap);
        if (map == null) {
            return false;
        }
        Object dataObj = map.get("data");
        if (dataObj == null) {
            return false;
        }
        List list = JsonUtils.deserialize(dataObj, new TypeReference<List<LinkedHashMap>>() {});
        if (list == null) {
            return false;
        }
        List<VehicleDayStatisticsPO> inserList = new ArrayList<>();
        Set<String> deleteSet = new HashSet<>();
        var timeDay = DateUtils.parseDate(queryDate);
        Map<String, List<VehicleDayStatisticsPO>> typeMap = new HashMap<>();
        for (Object val : list) {
            LinkedHashMap dayMap = (LinkedHashMap) val;
            String bussType = dayMap.get("bussType") == null ? null : String.valueOf(dayMap.get("bussType"));
            if ("2".equals(bussType)) {
                continue;
            }
            String inComingType = dayMap.get("inComingType") == null ? "" : String.valueOf(dayMap.get("inComingType"));
            String dateType = dayMap.get("dateType") == null ? "" : String.valueOf(dayMap.get("dateType"));
            String vehicleNature = StringUtils.isNotBlank(inComingType) ? VehicleNatureEnum.getCode(Integer.parseInt(inComingType)) : "";
            List<VehicleDayStatisticsPO> typeList = JsonUtils.deserialize(dayMap.get("list"), new TypeReference<List<VehicleDayStatisticsPO>>() {});
            typeList.forEach(po -> {
                po.setInComingType(inComingType);
                po.setTenant(tenant);
                EntityAuditor.populateCreateAudit(po, operator, userId, userType, nowTime);
                po.setTimeDay(timeDay);
                po.setTimeDayFormat(queryDate);
                po.setVehicleNature(vehicleNature);
                po.setDateType(dateType);
                String hour = po.getDateHour();
                if (StringUtils.isNotBlank(hour)) {
                    String format = String.format("%02d", Integer.parseInt(hour));
                    var hourTime = DateUtils.parse(po.getTimeDayFormat() + " " + format + ":00:00");
                    po.setTimeHour(hourTime);
                    po.setTimeHourFormat(po.getTimeDayFormat() + " " + format);
                    deleteSet.add(po.getTimeHourFormat());
                }
            });
            List<VehicleDayStatisticsPO> tempList = typeMap.get(inComingType);
            if (tempList != null) {
                typeList.addAll(tempList);
            }
            typeMap.put(inComingType, typeList);
        }
        typeMap.forEach((k, v) -> inserList.addAll(v));
        if (!CollectionUtils.isEmpty(inserList)) {
            this.saveBatch(inserList);
        }
        if (!CollectionUtils.isEmpty(deleteSet)) {
            this.remove(Wrappers.<VehicleDayStatisticsPO>lambdaQuery().in(VehicleDayStatisticsPO::getTimeHourFormat, deleteSet)
                                .lt(VehicleDayStatisticsPO::getCreateTime, DateUtils.offset(nowTime, ChronoUnit.SECONDS, -1)));
        }
        return true;
    }

    private Map<String, Object> getHttpMap(String itemCode, String code, Map<String, Object> paramMap) {
        DataDictionaryaramByItemAndCodeParam param = new DataDictionaryaramByItemAndCodeParam();
        param.setItemCode(itemCode);
        param.setCode(code);
        DataDictionaryDTO ex = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);
        if (ex == null) {
            return null;
        }
        String result = HttpUtil.post(ex.getVariable(), JSON.toJSONString(paramMap));
        log.info("（code）result:{}", result);
        return JsonUtils.deserialize(result, Map.class);
    }
}
