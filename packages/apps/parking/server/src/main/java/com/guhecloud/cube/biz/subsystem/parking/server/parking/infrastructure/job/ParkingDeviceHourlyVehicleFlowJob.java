package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.job;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.constants.ParkingPropCodes;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingHourlyVehicleFlowPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoDevicePO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.device.ParkDeviceAppService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingHourlyVehicleFlowRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoDeviceRepository;
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
public class ParkingDeviceHourlyVehicleFlowJob {

    private final ParkingInfoDeviceRepository parkingInfoDeviceRepository;

    private final ParkDeviceAppService parkDeviceAppService;

    private final ParkingHourlyVehicleFlowRepository parkingHourlyVehicleFlowRepository;

    @JobAnnotation(name = "parkingDeviceHourlyVehicleFlow",
                   cron = "0 0 0/1 * * ?",
                   desc = "停车场-设备小时车流采集")
    public void collect(String param) {
        var executionTime = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        try {
            SessionContextHolder.requestTenant(null);
            var parkingDevices = parkingInfoDeviceRepository.findAllPO();
            if (CollectionUtils.isEmpty(parkingDevices)) {
                log.info("停车场设备小时车流采集结束，未查询到停车场关联设备，executionTime={}", executionTime);
                return;
            }
            var tenantDeviceMap = parkingDevices.stream()
                    .filter(device -> Objects.nonNull(device.getTenant()) && Objects.nonNull(device.getDeviceId()))
                    .collect(Collectors.groupingBy(ParkingInfoDevicePO::getTenant,
                                                   Collectors.toMap(ParkingInfoDevicePO::getDeviceId,
                                                                    ParkingInfoDevicePO::getDeviceId,
                                                                    (oldValue, newValue) -> oldValue)));
            var defaultRecordTimes = buildRecordTimes(executionTime);
            tenantDeviceMap.forEach((tenant, deviceMap) -> collectTenantDeviceVehicleFlow(tenant,
                                                                                           deviceMap,
                                                                                           executionTime,
                                                                                           defaultRecordTimes));
        } catch (Exception e) {
            log.error("停车场设备小时车流采集失败，executionTime={}", executionTime, e);
        } finally {
            SessionContextHolder.requestTenant(null);
        }
    }

    private void collectTenantDeviceVehicleFlow(Integer tenant,
                                                Map<Long, Long> deviceMap,
                                                LocalDateTime executionTime,
                                                List<LocalDateTime> defaultRecordTimes) {
        if (Objects.isNull(tenant) || deviceMap.isEmpty()) {
            return;
        }
        try {
            SessionContextHolder.requestTenant(tenant);
            var recordTimes = parkingHourlyVehicleFlowRepository.hasRecords()
                              ? List.of(executionTime.minusHours(1))
                              : defaultRecordTimes;
            for (var recordTime : recordTimes) {
                var period = new Period(recordTime, recordTime.plusHours(1));
                var deviceVehicleCountMap = parkDeviceAppService.getDevicesSinglePropHistorySumValue(deviceMap.keySet(),
                                                                                                      ParkingPropCodes.CURRENT_CAR_NUM,
                                                                                                      period);
                var records = deviceMap.keySet().stream()
                        .map(deviceId -> buildRecord(tenant,
                                                     deviceId,
                                                     recordTime,
                                                     deviceVehicleCountMap.getOrDefault(deviceId, 0)))
                        .toList();
                parkingHourlyVehicleFlowRepository.saveRecords(records);
            }
            log.info("停车场设备小时车流采集完成，tenant={}，deviceCount={}，executionTime={}，recordTimeCount={}",
                     tenant, deviceMap.size(), executionTime, recordTimes.size());
        } catch (Exception e) {
            log.error("停车场设备小时车流采集失败，tenant={}，executionTime={}", tenant, executionTime, e);
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

    private ParkingHourlyVehicleFlowPO buildRecord(Integer tenant,
                                                    Long deviceId,
                                                    LocalDateTime recordTime,
                                                    Integer vehicleCount) {
        var record = ParkingHourlyVehicleFlowPO.builder()
                .id(IdWorker.getId())
                .tenant(tenant)
                .deviceId(deviceId)
                .recordTime(recordTime)
                .vehicleCount(vehicleCount)
                .build();
        EntityAuditor.populateAudit(record);
        return record;
    }
}
