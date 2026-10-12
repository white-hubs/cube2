package com.guhecloud.cube.customization.xialing.server.liangcang.job;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehouseStatisticsApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.LiangcangSystemDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseMeasureInfoRemoteDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseMeasureSyncConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseMeasureHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseMeasureHistoryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 定时同步粮仓测量信息。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseMeasureHistorySyncJob {

    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final WarehouseStatisticsApiClient warehouseStatisticsApiClient;
    private final WarehouseMeasureHistoryService warehouseMeasureHistoryService;
    private final SysDictionaryClient sysDictionaryClient;

    @JobAnnotation(name = "warehouseMeasureHistorySync", cron = "0 0/30 * * * ?", desc = "定时同步粮仓测量信息")
    public void syncWarehouseMeasureHistory(String param) {
        WarehouseMeasureHistorySyncJobParam jobParam = loadJobParamFromDictionary();
        if (jobParam == null) {
            return;
        }
        try {
            SessionContextHolder.requestTenant(jobParam.tenant());
            doSyncWarehouseMeasureHistory(jobParam);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }

    private WarehouseMeasureHistorySyncJobParam loadJobParamFromDictionary() {
        SessionContextHolder.clearSessionContext();
        var dict = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(LiangcangSystemDictionary.ITEM_THIRD_CONFIG,
                                                                         LiangcangSystemDictionary.CODE_WAREHOUSE_MEASURE_SYNC);
        if (dict == null || StringUtils.isBlank(dict.getVariable())) {
            log.warn("warehouseMeasureHistorySync 未配置系统字典 {}:{}，跳过",
                     LiangcangSystemDictionary.ITEM_THIRD_CONFIG, LiangcangSystemDictionary.CODE_WAREHOUSE_MEASURE_SYNC);
            return null;
        }
        WarehouseMeasureSyncConfigDTO config;
        try {
            config = JsonUtils.deserialize(dict.getVariable(), WarehouseMeasureSyncConfigDTO.class);
        } catch (RuntimeException e) {
            log.warn("warehouseMeasureHistorySync 系统字典 variable 解析失败: {}", dict.getVariable(), e);
            return null;
        }
        if (config == null || config.getTenant() == null) {
            log.warn("warehouseMeasureHistorySync 系统字典未配置 tenant，跳过");
            return null;
        }
        return new WarehouseMeasureHistorySyncJobParam(config.getTenant());
    }

    private void doSyncWarehouseMeasureHistory(WarehouseMeasureHistorySyncJobParam param) {
        List<RadarWarehouseBinding> bindings = radarWarehouseBindingService.list(
                Wrappers.<RadarWarehouseBinding>lambdaQuery()
                        .select(RadarWarehouseBinding::getWarehouseId, RadarWarehouseBinding::getTenant));
        if (ListUtils.isEmpty(bindings)) {
            log.debug("无粮仓绑定，跳过测量信息同步");
            return;
        }
        Map<Long, Integer> tenantByWarehouseId = new LinkedHashMap<>();
        for (RadarWarehouseBinding binding : bindings) {
            if (binding != null && binding.getWarehouseId() != null) {
                tenantByWarehouseId.putIfAbsent(binding.getWarehouseId(), binding.getTenant());
            }
        }
        if (MapUtils.isEmpty(tenantByWarehouseId)) {
            return;
        }

        LocalDateTime acquisitionDate = LocalDateTime.now().withNano(0);
        List<WarehouseMeasureHistory> batch = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : tenantByWarehouseId.entrySet()) {
            Long warehouseId = entry.getKey();
            WarehouseMeasureInfoRemoteDTO measureInfo = warehouseStatisticsApiClient.fetchMeasureInfo(warehouseId)
                    .orElse(null);
            if (measureInfo == null) {
                continue;
            }
            WarehouseMeasureHistory row = toHistoryRow(warehouseId, acquisitionDate, measureInfo);
            row.setTenant(entry.getValue());
            batch.add(row);
        }
        warehouseMeasureHistoryService.saveIgnoreDuplicates(batch);
        log.info("测量信息同步完成 tenant={}，候选 {} 条", param.tenant(), batch.size());
    }

    private WarehouseMeasureHistory toHistoryRow(Long warehouseId, LocalDateTime acquisitionDate,
                                                 WarehouseMeasureInfoRemoteDTO measureInfo) {
        var row = new WarehouseMeasureHistory();
        row.setId(IdWorker.getId());
        row.setWarehouseId(warehouseId);
        row.setAcquisitionDate(acquisitionDate);
        row.setMaterialVolume(measureInfo.materialVolumeValue());
        row.setRemainingVolume(measureInfo.remainingVolumeValue());
        row.setTotalVolume(measureInfo.capacityVolumeValue());
        row.setWeight(measureInfo.weightTonValue());
        row.setMaterialHeight(measureInfo.materialHeightValue());
        row.setRemainingHeight(measureInfo.remainingHeightValue());
        row.setTotalHeight(measureInfo.capacityHeightValue());
        row.setUsageRate(measureInfo.usageRateValue());
        row.setRemoteUpdateTime(measureInfo.getUpdateTime());
        return row;
    }

    private record WarehouseMeasureHistorySyncJobParam(Integer tenant) {
    }
}
