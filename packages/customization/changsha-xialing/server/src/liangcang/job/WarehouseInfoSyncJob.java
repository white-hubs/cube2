package com.guhecloud.cube.customization.xialing.server.liangcang.job;

import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.LiangcangSystemDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseMeasureSyncConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseInfoCacheService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** 定时同步三方粮仓基础信息。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseInfoSyncJob {

    private final WarehousePageApiClient warehousePageApiClient;
    private final WarehouseInfoCacheService warehouseInfoCacheService;
    private final SysDictionaryClient sysDictionaryClient;

    @JobAnnotation(name = "warehouseInfoSync", cron = "0 0/1 * * * ?", desc = "定时同步三方粮仓基础信息")
    public void syncWarehouseInfo(String param) {
        WarehouseInfoSyncJobParam jobParam = loadJobParamFromDictionary();
        if (jobParam == null) {
            return;
        }
        try {
            SessionContextHolder.requestTenant(jobParam.tenant());
            var rows = warehousePageApiClient.fetchRemoteAllWarehouseRowsByHierarchy();
            if (ListUtils.isEmpty(rows)) {
                log.debug("三方粮仓基础信息为空，跳过同步 tenant={}", jobParam.tenant());
                return;
            }
            warehouseInfoCacheService.upsertPageItems(rows, jobParam.tenant());
            log.info("三方粮仓基础信息同步完成 tenant={}，同步 {} 条", jobParam.tenant(), rows.size());
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }

    private WarehouseInfoSyncJobParam loadJobParamFromDictionary() {
        SessionContextHolder.clearSessionContext();
        var dict = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(LiangcangSystemDictionary.ITEM_THIRD_CONFIG,
                                                                         LiangcangSystemDictionary.CODE_WAREHOUSE_INFO_SYNC);
        if (dict == null || StringUtils.isBlank(dict.getVariable())) {
            log.warn("warehouseInfoSync 未配置系统字典 {}:{}，跳过",
                     LiangcangSystemDictionary.ITEM_THIRD_CONFIG, LiangcangSystemDictionary.CODE_WAREHOUSE_INFO_SYNC);
            return null;
        }
        WarehouseMeasureSyncConfigDTO config;
        try {
            config = JsonUtils.deserialize(dict.getVariable(), WarehouseMeasureSyncConfigDTO.class);
        } catch (RuntimeException e) {
            log.warn("warehouseInfoSync 系统字典 variable 解析失败: {}", dict.getVariable(), e);
            return null;
        }
        if (config == null || config.getTenant() == null) {
            log.warn("warehouseInfoSync 系统字典未配置 tenant，跳过");
            return null;
        }
        return new WarehouseInfoSyncJobParam(config.getTenant());
    }

    private record WarehouseInfoSyncJobParam(Integer tenant) {
    }
}
