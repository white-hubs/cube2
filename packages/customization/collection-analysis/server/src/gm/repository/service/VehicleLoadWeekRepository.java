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
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.VehicleLoadWeekStatisticsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.VehicleLoadWeekStatisticsPO;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleLoadWeekRepository extends ServiceImpl<VehicleLoadWeekStatisticsMapper, VehicleLoadWeekStatisticsPO> {

    private final DataDictionaryAdapter dataDictionaryAdapter;

    /**
     * 近一周装货趋势
     *
     * @param bgnTime  日期（yyyy-MM-dd）
     * @param endTime  日期（yyyy-MM-dd）
     * @param tenant   租户
     * @param nowTime  当前时间
     * @param operator 操作人
     *
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean VehicleLoadWeekPull(String bgnTime, String endTime, Integer tenant, LocalDateTime nowTime, String operator) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bgnTime", bgnTime);
        paramMap.put("endTime", endTime);
        Map<String, Object> map = this.getHttpMap(DictionaryItemEnum.GM_VISUALIZATION_DATA_URL.name(), DataDictionaryEnum.PARK_LOADING_WEEK.name(), paramMap);
        if (map == null) {
            return false;
        }
        Object dataObj = map.get("data");
        if (dataObj == null) {
            return false;
        }
        Set<String> deleteSet = new HashSet<>();
        List<VehicleLoadWeekStatisticsPO> insertList = JsonUtils.deserialize(dataObj, new TypeReference<List<VehicleLoadWeekStatisticsPO>>() {});
        if (CollectionUtils.isEmpty(insertList)) {
            return false;
        }
        insertList.forEach(po -> {
            String days = po.getDays();
            if (StringUtils.isNotBlank(days)) {
                po.setTenant(tenant);
                po.setTimeDayFormat(days);
                po.setTimeDay(DateUtils.parseDate(days));
                EntityAuditor.populateCreateAudit(po, operator, StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, nowTime);
            }
            deleteSet.add(po.getDays());
        });
        if (!CollectionUtils.isEmpty(insertList)) {
            this.saveBatch(insertList);
        }
        if (!CollectionUtils.isEmpty(deleteSet)) {
            this.remove(Wrappers.<VehicleLoadWeekStatisticsPO>lambdaQuery().in(VehicleLoadWeekStatisticsPO::getDays, deleteSet)
                                .lt(VehicleLoadWeekStatisticsPO::getCreateTime, DateUtils.offset(nowTime, ChronoUnit.SECONDS, -1)));
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
