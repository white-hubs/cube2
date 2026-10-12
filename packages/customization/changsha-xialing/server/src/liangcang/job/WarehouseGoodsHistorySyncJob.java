package com.guhecloud.cube.customization.xialing.server.liangcang.job;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehouseStatisticsApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.LiangcangSystemDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseGoodsHistorySyncConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseGoodsInfoRemoteItem;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseGoodsHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseGoodsHistoryService;
import com.guhecloud.cube.customization.xialing.server.liangcang.util.WarehouseAcquisitionTimeParser;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时拉智慧仓库货量历史；从系统字典读取租户和查询时间范围，Session 绑定。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseGoodsHistorySyncJob {
    private static final long DEFAULT_QUERY_MINUTES = 30L;

    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final WarehouseStatisticsApiClient warehouseStatisticsApiClient;
    private final WarehouseGoodsHistoryService warehouseGoodsHistoryService;
    private final SysDictionaryClient sysDictionaryClient;
    
    @JobAnnotation(name = "warehouseGoodsHistorySync", cron = "0/30 * * * * ?", desc = "定时同步粮仓信息")
    public void syncWarehouseGoodsHistory(String param) {
        var jobParam = loadJobParamFromDictionary();
        if (jobParam == null) {
            return;
        }
        try {
            SessionContextHolder.requestTenant(jobParam.tenant());
            doSyncWarehouseGoodsHistory(jobParam);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }

    private WarehouseGoodsHistorySyncJobParam loadJobParamFromDictionary() {
        SessionContextHolder.clearSessionContext();
        var dict = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(LiangcangSystemDictionary.ITEM_THIRD_CONFIG,
                                                                         LiangcangSystemDictionary.CODE_WAREHOUSE_GOODS_HISTORY_SYNC);
        if (dict == null || StringUtils.isBlank(dict.getVariable())) {
            log.warn("warehouseGoodsHistorySync 未配置系统字典 {}:{}，跳过",
                     LiangcangSystemDictionary.ITEM_THIRD_CONFIG, LiangcangSystemDictionary.CODE_WAREHOUSE_GOODS_HISTORY_SYNC);
            return null;
        }
        WarehouseGoodsHistorySyncConfigDTO config;
        try {
            config = JsonUtils.deserialize(dict.getVariable(), WarehouseGoodsHistorySyncConfigDTO.class);
        } catch (RuntimeException e) {
            log.warn("warehouseGoodsHistorySync 系统字典 variable 解析失败: {}", dict.getVariable(), e);
            return null;
        }
        if (config == null || config.getTenant() == null) {
            log.warn("warehouseGoodsHistorySync 系统字典未配置 tenant，跳过");
            return null;
        }
        long queryMinutes = DEFAULT_QUERY_MINUTES;
        if (config.getQueryMinutes() != null) {
            queryMinutes = config.getQueryMinutes();
        }
        if (queryMinutes <= 0) {
            log.warn("warehouseGoodsHistorySync 查询分钟数需大于0: {}，使用默认 {} 分钟", queryMinutes, DEFAULT_QUERY_MINUTES);
            queryMinutes = DEFAULT_QUERY_MINUTES;
        }
        return new WarehouseGoodsHistorySyncJobParam(config.getTenant(), queryMinutes);
    }

    private void doSyncWarehouseGoodsHistory(WarehouseGoodsHistorySyncJobParam param) {
        var now = Instant.now();
        long endSec = now.getEpochSecond();
        long startSec = now.minus(param.queryMinutes(), ChronoUnit.MINUTES).getEpochSecond();

        List<RadarWarehouseBinding> bindings = radarWarehouseBindingService.list(
                Wrappers.<RadarWarehouseBinding>lambdaQuery()
                        .select(RadarWarehouseBinding::getWarehouseId, RadarWarehouseBinding::getRadarDeviceId, RadarWarehouseBinding::getTenant));
        if (ListUtils.isEmpty(bindings)) {
            log.debug("无粮仓-雷达绑定，跳过货量历史同步");
            return;
        }

        Map<Long, WarehouseSyncContext> byWarehouse = new LinkedHashMap<>();
        for (RadarWarehouseBinding b : bindings) {
            Long warehouseId = b.getWarehouseId();
            Long radarDeviceId = b.getRadarDeviceId();
            if (warehouseId == null || radarDeviceId == null) {
                continue;
            }
            byWarehouse.putIfAbsent(warehouseId, new WarehouseSyncContext(warehouseId, radarDeviceId, b.getTenant()));
        }
        if (MapUtils.isEmpty(byWarehouse)) {
            return;
        }

        List<WarehouseGoodsHistory> batch = new ArrayList<>();
        for (WarehouseSyncContext ctx : byWarehouse.values()) {
            long warehouseId = ctx.warehouseId();
            long radarDeviceId = ctx.radarDeviceId();
            List<WarehouseGoodsInfoRemoteItem> items =
                    warehouseStatisticsApiClient.fetchWarehouseGoodsInfo(warehouseId, startSec, endSec);
            for (WarehouseGoodsInfoRemoteItem item : items) {
                var acquisition = WarehouseAcquisitionTimeParser.parse(item.getAcquisitionDate());
                if (acquisition == null) {
                    log.debug("跳过无采集时间的记录 warehouseId={}", warehouseId);
                    continue;
                }
                var normalized = acquisition.withNano(0);
                var row = new WarehouseGoodsHistory();
                row.setId(IdWorker.getId());
                row.setRadarDeviceId(radarDeviceId);
                row.setWarehouseId(warehouseId);
                row.setAcquisitionDate(normalized);
                row.setWeightValue(item.getWeightValue());
                row.setVolumeValue(item.getVolumeValue());
                row.setWeightDifference(item.getWeightDifference());
                row.setVolumeDifference(item.getVolumeDifference());
                row.setTenant(ctx.tenant());
                batch.add(row);
            }
        }

        warehouseGoodsHistoryService.saveIgnoreDuplicates(batch);
        log.info("货量历史同步完成 tenant={}，查询近 {} 分钟，候选 {} 条（去重后落库由服务层处理）", param.tenant(), param.queryMinutes(), batch.size());
    }

    private record WarehouseSyncContext(long warehouseId, long radarDeviceId, Integer tenant) {
    }

    private record WarehouseGoodsHistorySyncJobParam(Integer tenant, long queryMinutes) {
    }
}
