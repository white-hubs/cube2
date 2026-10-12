package com.guhecloud.cube.customization.xialing.server.liangcang.job;

import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.LiangcangSystemDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseMeasureSyncConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendAlertService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** 粮仓温度趋势预警每日完整性标记。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseTemperatureDailyCompleteJob {

    private final WarehouseTemperatureTrendAlertService temperatureAlertService;
    private final SysDictionaryClient sysDictionaryClient;

    @JobAnnotation(name = "warehouseTemperatureDailyComplete", cron = "0 5 0 * * ?",
            desc = "粮仓温度趋势预警日完整性标记")
    public void markComplete(String param) {
        JobTenantParam jobParam = loadJobParamFromDictionary();
        if (jobParam == null) {
            return;
        }
        try {
            SessionContextHolder.requestTenant(jobParam.tenant());
            temperatureAlertService.markPreviousDayComplete();
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }

    private JobTenantParam loadJobParamFromDictionary() {
        SessionContextHolder.clearSessionContext();
        var dict = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(LiangcangSystemDictionary.ITEM_THIRD_CONFIG,
                                                                         LiangcangSystemDictionary.CODE_WAREHOUSE_TEMPERATURE_ALERT_SYNC);
        if (dict == null || StringUtils.isBlank(dict.getVariable())) {
            log.warn("warehouseTemperatureDailyComplete 未配置系统字典 {}:{}，跳过",
                     LiangcangSystemDictionary.ITEM_THIRD_CONFIG,
                     LiangcangSystemDictionary.CODE_WAREHOUSE_TEMPERATURE_ALERT_SYNC);
            return null;
        }
        WarehouseMeasureSyncConfigDTO config;
        try {
            config = JsonUtils.deserialize(dict.getVariable(), WarehouseMeasureSyncConfigDTO.class);
        } catch (RuntimeException e) {
            log.warn("warehouseTemperatureDailyComplete 系统字典 variable 解析失败: {}", dict.getVariable(), e);
            return null;
        }
        if (config == null || config.getTenant() == null) {
            log.warn("warehouseTemperatureDailyComplete 系统字典未配置 tenant，跳过");
            return null;
        }
        return new JobTenantParam(config.getTenant());
    }

    private record JobTenantParam(Integer tenant) {
    }
}
