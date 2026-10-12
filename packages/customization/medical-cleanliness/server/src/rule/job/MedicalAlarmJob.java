package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.job;

import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.base.alarm.client.param.AlarmDeviceStatusTotalQueryParam;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.entity.MedicalAlarmStatistics;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.service.MedicalAlarmStatisticsService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-01  11:34
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalAlarmJob {
    private final MedicalAlarmStatisticsService medicalAlarmStatisticsService;
    private final AlarmRecordClient alarmRecordClient;
    private final TenantClient tenantClient;

    /**
     * 读取设备报警数量
     */
    @JobAnnotation(name = "medicalAlarm", cron = "0 0 0 * * ?", desc = "洁净手术室-读取设备报警数量")
    public void MedicalAlarm(String param) {
        SessionContextHolder.clearSessionContext();
        var queryParam = new AlarmDeviceStatusTotalQueryParam();
        queryParam.setApplicationCode("OPERATING_ROOM");
        queryParam.setPeriod(ObjectUtils.isNotEmpty(param) ? param : DateUtils.beginOfDay(LocalDateTime.now()) + "," +
                                                                     DateUtils.endOfDay(LocalDateTime.now()));
        var tenant = tenantClient.listTenants("");
        tenant.forEach(ex -> {
            SessionContextHolder.requestTenant(ex.getCode());
            var alarmList = alarmRecordClient.exportDeviceAlarmStatusTotalList(queryParam);
            List<MedicalAlarmStatistics> params = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(alarmList) && !alarmList.isEmpty()) {
                alarmList.forEach(alarmEx -> {
                    var alarmStatistics = ConvertUtils.convert(MedicalAlarmStatistics.class, alarmEx);
                    alarmStatistics.setTenantName(ex.getName());
                    alarmStatistics.setDeviceProdCategoryCode(alarmEx.getAlarmTargetTypeId());
                    alarmStatistics.setDeviceProdCategoryName(alarmEx.getAlarmTargetTypeName());
                    alarmStatistics.setAcquisitionDate(LocalDateTime.now());
                    params.add(alarmStatistics);
                });
                var bool = medicalAlarmStatisticsService.addMedicalAlarm(params);
                log.info("同步报警信息结果：{}", bool);
            }
            SessionContextHolder.clearSessionContext();
        });
    }
}
