package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.job;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.traffic.detection.server.common.constants.TrafficDetectionPropCodes;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionAreaDevicePO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionHourlyPersonFlowPO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device.TrafficDetectionAreaDeviceAppService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionAreaDeviceRepository;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionHourlyPersonFlowRepository;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrafficDetectionDeviceHourlyPersonFlowJob {

    private final TrafficDetectionAreaDeviceRepository trafficDetectionAreaDeviceRepository;

    private final TrafficDetectionAreaDeviceAppService trafficDetectionAreaDeviceAppService;

    private final TrafficDetectionHourlyPersonFlowRepository trafficDetectionHourlyPersonFlowRepository;

    @JobAnnotation(name = "trafficDetectionDeviceHourlyPersonFlow",
                   cron = "0 0 0/1 * * ?",
                   desc = "人流检测-设备小时人流采集")
    public void collect(String param) {
        var executionTime = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        try {
            SessionContextHolder.requestTenant(null);
            var areaDevices = trafficDetectionAreaDeviceRepository.findAllPO();
            if (CollectionUtils.isEmpty(areaDevices)) {
                log.info("人流检测设备小时人流采集结束，未查询到区域设备，executionTime={}", executionTime);
                return;
            }
            var tenantDeviceMap = areaDevices.stream()
                    .filter(device -> Objects.nonNull(device.getTenant()) && Objects.nonNull(device.getDeviceId()))
                    .collect(Collectors.groupingBy(TrafficDetectionAreaDevicePO::getTenant,
                                                   Collectors.toMap(TrafficDetectionAreaDevicePO::getDeviceId,
                                                                    TrafficDetectionAreaDevicePO::getDeviceId,
                                                                    (oldValue, newValue) -> oldValue)));
            var defaultRecordTimes = buildRecordTimes(executionTime);
            tenantDeviceMap.forEach((tenant, deviceMap) -> collectTenantDevicePersonFlow(tenant, deviceMap, executionTime, defaultRecordTimes));
        } catch (Exception e) {
            log.error("人流检测设备小时人流采集失败，executionTime={}", executionTime, e);
        } finally {
            SessionContextHolder.requestTenant(null);
        }
    }

    private void collectTenantDevicePersonFlow(Integer tenant, Map<Long, Long> deviceMap, LocalDateTime executionTime, List<LocalDateTime> defaultRecordTimes) {
        if (Objects.isNull(tenant) || deviceMap.isEmpty()) {
            return;
        }
        try {
            SessionContextHolder.requestTenant(tenant);
            var recordTimes = trafficDetectionHourlyPersonFlowRepository.hasRecords() ? List.of(executionTime.minusHours(1)) : defaultRecordTimes;
            for (var currentRecordTime : recordTimes) {
                var period = new Period(currentRecordTime, currentRecordTime.plusHours(1));
                var devicePersonCountMap = trafficDetectionAreaDeviceAppService.getDevicesSinglePropHistorySumValue(deviceMap.keySet(),
                                                                                                                     TrafficDetectionPropCodes.CURRENT_PEOPLE_NUM,
                                                                                                                     period);
                var records = deviceMap.keySet().stream()
                        .map(deviceId -> buildRecord(tenant, deviceId, currentRecordTime, devicePersonCountMap.getOrDefault(deviceId, 0)))
                        .toList();
                trafficDetectionHourlyPersonFlowRepository.saveRecords(records);
            }
            log.info("人流检测设备小时人流采集完成，tenant={}，deviceCount={}，executionTime={}，recordTimeCount={}",
                     tenant, deviceMap.size(), executionTime, recordTimes.size());
        } catch (Exception e) {
            log.error("人流检测设备小时人流采集失败，tenant={}，executionTime={}", tenant, executionTime, e);
        }
    }

    private List<LocalDateTime> buildRecordTimes(LocalDateTime executionTime) {
        var recordTimes = new ArrayList<LocalDateTime>();
        var cursor = LocalDateTime.of(executionTime.getYear(), Month.JANUARY, 1, 0, 0);
        while (cursor.isBefore(executionTime)) {
            recordTimes.add(cursor);
            cursor = cursor.plusHours(1);
        }
        return recordTimes;
    }

    private TrafficDetectionHourlyPersonFlowPO buildRecord(Integer tenant,
                                                            Long deviceId,
                                                            LocalDateTime recordTime,
                                                            Integer personCount
                                                           ) {
        var record = TrafficDetectionHourlyPersonFlowPO.builder()
                .id(IdWorker.getId())
                .tenant(tenant)
                .deviceId(deviceId)
                .recordTime(recordTime)
                .personCount(personCount)
                .build();
        EntityAuditor.populateAudit(record);
        return record;
    }
}
