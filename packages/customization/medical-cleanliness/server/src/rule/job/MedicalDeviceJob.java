package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.job;

import com.guhecloud.cube.base.device.center.std.statistics.DeviceBaseStatisticsClient;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.entity.MedicalDeviceStatistics;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.service.MedicalDeviceStatisticsService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-01  11:33
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalDeviceJob {
    private final MedicalDeviceStatisticsService medicalDeviceStatisticsService;
    private final DeviceBaseStatisticsClient deviceBaseStatisticsClient;
    private final TenantClient tenantClient;

    /**
     * 读取物联设备在线、离线数据
     */
    @JobAnnotation(name = "medicalDevice", cron = "0 0 0 * * ?", desc = "洁净手术室-读取设备在线、离线数量")
    public void MedicalDevice(String param) {
        SessionContextHolder.clearSessionContext();
        var period = ObjectUtils.isNotEmpty(param) ? param : DateUtils.beginOfDay(LocalDateTime.now()) + "," +
                                                             DateUtils.endOfDay(LocalDateTime.now());
        var tenant = tenantClient.listTenants("");
        tenant.forEach(ex -> {
            SessionContextHolder.requestTenant(ex.getCode());
            try {
                var deviceStatisticsList = deviceBaseStatisticsClient.getDevicesNumber(period);
                var medicalDeviceStatisticsList = ConvertUtils.toList(MedicalDeviceStatistics.class, deviceStatisticsList);
                medicalDeviceStatisticsList.forEach(device -> {
                    device.setTenantName(ex.getName());
                    device.setAcquisitionDate(LocalDateTime.now());
                });
                medicalDeviceStatisticsService.addMedicalDevice(medicalDeviceStatisticsList);

            } catch (Exception e) {
                log.error("", e);
            }
            SessionContextHolder.clearSessionContext();
        });
    }
}
