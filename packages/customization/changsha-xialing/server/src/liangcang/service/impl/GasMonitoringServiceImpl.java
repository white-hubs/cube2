package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmDeviceStatusStatisticsPageDTO;
import com.guhecloud.cube.base.alarm.client.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.client.param.AlarmDeviceStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmRecordPageVO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.LiangcangLocationCardConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringAlarmVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringDashboardVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringSummaryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringWarehouseVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.cube.srv.gen.securitymanagement.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.DeviceMonitoringVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.SetUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/** 气体监测看板聚合服务。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GasMonitoringServiceImpl implements com.guhecloud.cube.customization.xialing.server.liangcang.service.GasMonitoringService {

    private static final String GAS_MONITORING_APPLICATION_CODE = "GAS_MONITORING";
    private static final int RECENT_ALARM_LIMIT = 5;
    private static final int ALARM_QUERY_PAGE_SIZE = 1000;
    private static final int NUMBER_SCALE = 5;
    private static final BigDecimal PPM_TO_PERCENT_VOL_DIVISOR = BigDecimal.valueOf(10000);

    private final WarehouseDeviceBindingService warehouseDeviceBindingService;
    private final WarehousePageApiClient warehousePageApiClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final IotDevicePropClient iotDevicePropClient;
    private final LiangcangLocationCardConfigProvider locationCardConfigProvider;
    private final SecurityDeviceClient securityDeviceClient;
    private final AlarmRecordClient alarmRecordClient;

    @Override
    public GasMonitoringDashboardVO dashboard() {
        List<DeviceMonitoringVo> applicationDevices = loadApplicationDevices();
        List<WarehouseDeviceBinding> bindingRows = warehouseDeviceBindingService.lambdaQuery()
                .orderByAsc(WarehouseDeviceBinding::getWarehouseId)
                .list();
        LiangcangLocationCardConfigDTO config = locationCardConfigProvider.resolve();
        Map<Long, List<DeviceBaseDetailsDTO>> gasDevicesByWarehouse = resolveGasDevices(bindingRows, config);

        if (gasDevicesByWarehouse.isEmpty()) {
            return dashboard(toSummary(applicationDevices, Pagination.empty(), 0),
                             Collections.emptyList(), Collections.emptyList());
        }

        Set<Long> warehouseIds = gasDevicesByWarehouse.keySet();
        Map<Long, LiangcangWarehousePageItemVO> warehouseById = warehousePageApiClient.fetchPageItemsByIds(warehouseIds);
        Map<Long, Long> warehouseByAlarmTargetKey = buildAlarmWarehouseMapping(gasDevicesByWarehouse);
        AlarmData alarmData = loadAlarmData(warehouseByAlarmTargetKey);
        Pagination<AlarmRecordPageVO> alarmPage = alarmData.alarmPage();
        Map<Long, AlarmRecordPageVO> alarmByWarehouse = indexWarehouseAlarms(alarmPage.getRecords(), warehouseByAlarmTargetKey);
        Map<Long, BigDecimal> gasValueByDevice = loadGasValues(gasDevicesByWarehouse, config.getGasRealtimePropCode());

        List<GasMonitoringWarehouseVO> warehouses = gasDevicesByWarehouse.entrySet().stream()
                .map(entry -> toWarehouse(entry.getKey(), entry.getValue(), warehouseById, gasValueByDevice,
                                           alarmByWarehouse.get(entry.getKey()),
                                           alarmData.activeWarehouseIds().contains(entry.getKey())))
                .toList();
        List<GasMonitoringAlarmVO> recentAlarms = toRecentAlarms(alarmPage.getRecords(), warehouseByAlarmTargetKey,
                                                                 warehouseById);

        GasMonitoringDashboardVO dashboard = dashboard(toSummary(
                                                               applicationDevices, alarmPage,
                                                               alarmData.activeWarehouseIds().size()),
                                                       warehouses, recentAlarms);
        log.info("气体监测看板聚合完成，仓房数={}，设备数={}，告警数={}",
                 warehouses.size(), applicationDevices.size(), alarmPage.getTotal());
        return dashboard;
    }

    @Override
    public GasMonitoringSummaryVO summary() {
        List<DeviceMonitoringVo> applicationDevices = loadApplicationDevices();
        List<WarehouseDeviceBinding> bindingRows = warehouseDeviceBindingService.lambdaQuery()
                .list();
        LiangcangLocationCardConfigDTO config = locationCardConfigProvider.resolve();
        Map<Long, List<DeviceBaseDetailsDTO>> gasDevicesByWarehouse = resolveGasDevices(bindingRows, config);

        if (gasDevicesByWarehouse.isEmpty()) {
            return toSummary(applicationDevices, Pagination.empty(), 0);
        }

        Map<Long, Long> warehouseByAlarmTargetKey = buildAlarmWarehouseMapping(gasDevicesByWarehouse);
        AlarmData alarmData = loadAlarmData(warehouseByAlarmTargetKey);
        return toSummary(applicationDevices, alarmData.alarmPage(), alarmData.activeWarehouseIds().size());
    }

    @Override
    public List<GasMonitoringWarehouseVO> warehouses() {
        List<WarehouseDeviceBinding> bindingRows = warehouseDeviceBindingService.lambdaQuery()
                .list();
        LiangcangLocationCardConfigDTO config = locationCardConfigProvider.resolve();
        Map<Long, List<DeviceBaseDetailsDTO>> gasDevicesByWarehouse = resolveGasDevices(bindingRows, config);

        if (gasDevicesByWarehouse.isEmpty()) {
            return List.of();
        }

        Set<Long> warehouseIds = gasDevicesByWarehouse.keySet();
        Map<Long, LiangcangWarehousePageItemVO> warehouseById = warehousePageApiClient.fetchPageItemsByIds(warehouseIds);
        Map<Long, BigDecimal> gasValueByDevice = loadGasValues(gasDevicesByWarehouse, config.getGasRealtimePropCode());
        Map<Long, Long> warehouseByAlarmTargetKey = buildAlarmWarehouseMapping(gasDevicesByWarehouse);
        AlarmData alarmData = loadAlarmData(warehouseByAlarmTargetKey);
        Map<Long, AlarmRecordPageVO> alarmByWarehouse = indexWarehouseAlarms(
                alarmData.alarmPage().getRecords(), warehouseByAlarmTargetKey);

        return gasDevicesByWarehouse.entrySet().stream()
                .map(entry -> toWarehouse(entry.getKey(), entry.getValue(), warehouseById,
                        gasValueByDevice, alarmByWarehouse.get(entry.getKey()),
                        alarmData.activeWarehouseIds().contains(entry.getKey())))
                .toList();
    }

    @Override
    public List<GasMonitoringAlarmVO> recentAlarms() {
        List<WarehouseDeviceBinding> bindingRows = warehouseDeviceBindingService.lambdaQuery()
                .list();
        LiangcangLocationCardConfigDTO config = locationCardConfigProvider.resolve();
        Map<Long, List<DeviceBaseDetailsDTO>> gasDevicesByWarehouse = resolveGasDevices(bindingRows, config);

        if (gasDevicesByWarehouse.isEmpty()) {
            return List.of();
        }

        Set<Long> warehouseIds = gasDevicesByWarehouse.keySet();
        Map<Long, LiangcangWarehousePageItemVO> warehouseById = warehousePageApiClient.fetchPageItemsByIds(warehouseIds);
        Map<Long, Long> warehouseByAlarmTargetKey = buildAlarmWarehouseMapping(gasDevicesByWarehouse);
        Pagination<AlarmRecordPageVO> alarmPage = loadAlarms(warehouseByAlarmTargetKey.keySet());
        return toRecentAlarms(alarmPage.getRecords(), warehouseByAlarmTargetKey, warehouseById);
    }

    private List<DeviceMonitoringVo> loadApplicationDevices() {
        InsideSecurityDeviceParam param = new InsideSecurityDeviceParam();
        param.setApplicationCode(GAS_MONITORING_APPLICATION_CODE);
        List<DeviceMonitoringVo> devices = securityDeviceClient.getDeviceMonitoringList(param);
        return devices == null ? List.of() : devices;
    }

    private Map<Long, List<DeviceBaseDetailsDTO>> resolveGasDevices(List<WarehouseDeviceBinding> rows,
                                                                     LiangcangLocationCardConfigDTO config) {
        if (ListUtils.isEmpty(rows) || config == null || ListUtils.isEmpty(config.getGasProductCodes())) {
            return Map.of();
        }
        Set<Long> deviceIds = rows.stream().map(WarehouseDeviceBinding::getDeviceId)
                .filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, DeviceBaseDetailsDTO> details = loadDeviceDetails(deviceIds);
        Map<Long, List<DeviceBaseDetailsDTO>> result = new LinkedHashMap<>();
        for (WarehouseDeviceBinding row : rows) {
            if (row == null || row.getWarehouseId() == null || row.getDeviceId() == null) {
                continue;
            }
            DeviceBaseDetailsDTO detail = details.get(row.getDeviceId());
            if (deviceMatches(detail, config.getGasProductCodes())) {
                result.computeIfAbsent(row.getWarehouseId(), ignored -> new ArrayList<>()).add(detail);
            }
        }
        return result;
    }

    private Map<Long, DeviceBaseDetailsDTO> loadDeviceDetails(Set<Long> deviceIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        DevicePostByIdSetParam param = new DevicePostByIdSetParam();
        param.setDeviceIdSet(deviceIds);
        List<DeviceBaseDetailsDTO> rows = deviceCenterStdClient.postDetailsListByDeviceId(param);
        return ListUtils.isEmpty(rows) ? Map.of() : rows.stream()
                .filter(row -> row != null && row.getId() != null)
                .collect(Collectors.toMap(DeviceBaseDetailsDTO::getId, row -> row, (first, ignored) -> first));
    }

    private Map<Long, BigDecimal> loadGasValues(Map<Long, List<DeviceBaseDetailsDTO>> devicesByWarehouse,
                                                String propCode) {
        if (MapUtils.isEmpty(devicesByWarehouse) || propCode == null || propCode.isBlank()) {
            return Map.of();
        }
        List<Long> deviceIds = devicesByWarehouse.values().stream().flatMap(List::stream)
                .map(DeviceBaseDetailsDTO::getId).filter(Objects::nonNull).distinct().toList();
        if (deviceIds.isEmpty()) {
            return Map.of();
        }
        MultiDevicePropParam param = new MultiDevicePropParam();
        param.setDevIdAndPropCodes(deviceIds.stream().map(id -> id + "." + propCode.trim())
                .collect(Collectors.joining(",")));
        Map<String, List<KeyValueWithTsDTO>> valuesByDevice = iotDevicePropClient.multiProp(param);
        if (MapUtils.isEmpty(valuesByDevice)) {
            return Map.of();
        }
        Map<Long, BigDecimal> result = new HashMap<>();
        for (Long deviceId : deviceIds) {
            BigDecimal value = findValue(valuesByDevice.get(String.valueOf(deviceId)), propCode.trim());
            if (value != null) {
                result.put(deviceId, value);
            }
        }
        return result;
    }

    private GasMonitoringWarehouseVO toWarehouse(Long warehouseId, List<DeviceBaseDetailsDTO> devices,
                                                 Map<Long, LiangcangWarehousePageItemVO> warehouseById,
                                                 Map<Long, BigDecimal> gasValueByDevice,
                                                 AlarmRecordPageVO alarm,
                                                 boolean activeAlarm) {
        GasMonitoringWarehouseVO result = new GasMonitoringWarehouseVO();
        LiangcangWarehousePageItemVO warehouse = warehouseById.get(warehouseId);
        result.setWarehouseId(warehouseId);
        result.setWarehouseName(warehouse == null ? null : warehouse.getName());
        result.setCo2Ppm(averageByRole(devices, gasValueByDevice, GasRole.CO2));
        result.setO2Percent(averageByRole(devices, gasValueByDevice, GasRole.O2));
        result.setPh3Ppm(averageByRole(devices, gasValueByDevice, GasRole.PH3));
        result.setOnline(devices.stream().anyMatch(device -> Boolean.TRUE.equals(device.getDeviceState())));
        result.setAlarm(activeAlarm);
        result.setAlarmMessage(activeAlarm && alarm != null ? alarmContent(alarm) : null);
        return result;
    }

    private AlarmData loadAlarmData(Map<Long, Long> warehouseByAlarmTargetKey) {
        if (warehouseByAlarmTargetKey.isEmpty()) {
            return new AlarmData(Pagination.empty(), Set.of());
        }
        Set<Long> alarmTargetKeys = warehouseByAlarmTargetKey.keySet();
        Pagination<AlarmRecordPageVO> alarmPage = loadAlarms(alarmTargetKeys);

        AlarmDeviceStatusTotalQueryParam statusParam = new AlarmDeviceStatusTotalQueryParam();
        statusParam.setIds(new ArrayList<>(alarmTargetKeys));
        statusParam.setApplicationCode(GAS_MONITORING_APPLICATION_CODE);
        List<AlarmDeviceStatusStatisticsPageDTO> deviceStatuses =
                alarmRecordClient.getDeviceAlarmStatusTotalList(statusParam);
        Set<Long> activeWarehouseIds = deviceStatuses == null
                                       ? Set.of()
                                       : deviceStatuses.stream()
                                               .filter(GasMonitoringServiceImpl::hasActiveAlarm)
                                               .map(AlarmDeviceStatusStatisticsPageDTO::getDeviceId)
                                               .map(warehouseByAlarmTargetKey::get)
                                               .filter(Objects::nonNull)
                                               .collect(Collectors.toSet());
        return new AlarmData(alarmPage, activeWarehouseIds);
    }

    private Pagination<AlarmRecordPageVO> loadAlarms(Set<Long> alarmTargetKeys) {
        if (alarmTargetKeys.isEmpty()) {
            return Pagination.empty();
        }
        AlarmRecordQueryParam param = new AlarmRecordQueryParam();
        param.setPageNumber(1);
        param.setPageSize(ALARM_QUERY_PAGE_SIZE);
        param.setApplicationCode(GAS_MONITORING_APPLICATION_CODE);
        param.setDeviceIds(new ArrayList<>(alarmTargetKeys));
        Pagination<AlarmRecordPageVO> page = alarmRecordClient.page(param);
        return page == null ? Pagination.empty() : page;
    }

    private static boolean hasActiveAlarm(AlarmDeviceStatusStatisticsPageDTO status) {
        return status != null && (positive(status.getUnConfirmedNum())
                                  || positive(status.getProgressConfirmedNum())
                                  || positive(status.getSuspendNum()));
    }

    private static boolean positive(Long value) {
        return value != null && value > 0;
    }

    private static Map<Long, Long> buildAlarmWarehouseMapping(Map<Long, List<DeviceBaseDetailsDTO>> devicesByWarehouse) {
        Map<Long, Long> result = new HashMap<>();
        devicesByWarehouse.forEach((warehouseId, devices) -> devices.forEach(device -> {
            if (device.getId() != null) {
                result.put(device.getId(), warehouseId);
            }
            Long thirdPartyId = parseLong(device.getThirdPartyId());
            if (thirdPartyId != null) {
                result.put(thirdPartyId, warehouseId);
            }
        }));
        return result;
    }

    private static Map<Long, AlarmRecordPageVO> indexWarehouseAlarms(List<AlarmRecordPageVO> alarms,
                                                                      Map<Long, Long> warehouseByTarget) {
        Map<Long, AlarmRecordPageVO> result = new HashMap<>();
        if (alarms != null) {
            alarms.stream()
                    .filter(Objects::nonNull)
                    .filter(alarm -> AlarmStatusEnum.ALARM_HANDLER != alarm.getAlarmStatus())
                    .forEach(alarm -> {
                        Long warehouseId = warehouseByTarget.get(alarm.getAlarmTargetKey());
                        if (warehouseId != null) {
                            result.putIfAbsent(warehouseId, alarm);
                        }
                    });
        }
        return result;
    }

    private static List<GasMonitoringAlarmVO> toRecentAlarms(List<AlarmRecordPageVO> alarms,
                                                              Map<Long, Long> warehouseByTarget,
                                                              Map<Long, LiangcangWarehousePageItemVO> warehouseById) {
        if (alarms == null) {
            return List.of();
        }
        return alarms.stream().filter(Objects::nonNull).limit(RECENT_ALARM_LIMIT)
                .map(alarm -> {
                    GasMonitoringAlarmVO result = new GasMonitoringAlarmVO();
                    LiangcangWarehousePageItemVO warehouse = warehouseById.get(warehouseByTarget.get(alarm.getAlarmTargetKey()));
                    result.setId(alarm.getId());
                    result.setContent(alarmContent(alarm));
                    result.setLevel(alarm.getAlarmLevelName());
                    result.setAlarmTargetKey(alarm.getAlarmTargetKey());
                    result.setWarehouseName(warehouse == null ? alarm.getAlarmTargetName() : warehouse.getName());
                    result.setAlarmArea(alarm.getAreaName() == null ? alarm.getArea() : alarm.getAreaName());
                    result.setAlarmTime(alarm.getAlarmTime());
                    return result;
                }).toList();
    }

    private static GasMonitoringSummaryVO toSummary(List<DeviceMonitoringVo> devices,
                                                     Pagination<AlarmRecordPageVO> alarmPage,
                                                     int alarmWarehouseCount) {
        int online = (int) devices.stream().filter(device -> device != null && Boolean.TRUE.equals(device.getDeviceStatus())).count();
        GasMonitoringSummaryVO result = new GasMonitoringSummaryVO();
        result.setTotalDeviceCount(devices.size());
        result.setOnlineDeviceCount(online);
        result.setOfflineDeviceCount(devices.size() - online);
        result.setAlarmWarehouseCount(alarmWarehouseCount);
        result.setAlarmCount(alarmPage.getTotal());
        return result;
    }

    private static GasMonitoringDashboardVO dashboard(GasMonitoringSummaryVO summary,
                                                      List<GasMonitoringWarehouseVO> warehouses,
                                                      List<GasMonitoringAlarmVO> alarms) {
        GasMonitoringDashboardVO result = new GasMonitoringDashboardVO();
        result.setSummary(summary);
        result.setWarehouses(warehouses);
        result.setRecentAlarms(alarms);
        return result;
    }

    private static BigDecimal averageByRole(List<DeviceBaseDetailsDTO> devices,
                                            Map<Long, BigDecimal> values,
                                            GasRole role) {
        List<BigDecimal> roleValues = devices.stream().filter(device -> resolveRole(device) == role)
                .map(device -> values.get(device.getId())).filter(Objects::nonNull).toList();
        if (roleValues.isEmpty()) {
            return null;
        }
        return roleValues.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(roleValues.size()), NUMBER_SCALE, RoundingMode.HALF_UP)
                .divide(role == GasRole.CO2 || role == GasRole.PH3 ? PPM_TO_PERCENT_VOL_DIVISOR : BigDecimal.ONE,
                       NUMBER_SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal findValue(List<KeyValueWithTsDTO> values, String propCode) {
        if (values == null) {
            return null;
        }
        for (KeyValueWithTsDTO value : values) {
            if (value != null && propCode.equals(value.getKey())) {
                try {
                    return new BigDecimal(String.valueOf(value.getValue()));
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    private static boolean deviceMatches(DeviceBaseDetailsDTO detail, List<String> productCodes) {
        if (detail == null || detail.getDeviceIotProdId() == null || productCodes == null) {
            return false;
        }
        return productCodes.stream().filter(Objects::nonNull)
                .anyMatch(code -> code.trim().equals(String.valueOf(detail.getDeviceIotProdId())));
    }

    private static GasRole resolveRole(DeviceBaseDetailsDTO detail) {
        if (detail == null || detail.getDeviceName() == null) {
            return null;
        }
        String name = detail.getDeviceName();
        if (name.contains("二氧化碳") || name.toLowerCase().contains("co2")) {
            return GasRole.CO2;
        }
        if (name.contains("磷化氢") || name.toLowerCase().contains("ph3")) {
            return GasRole.PH3;
        }
        if (name.contains("氧气") || name.toLowerCase().contains("o2")) {
            return GasRole.O2;
        }
        return null;
    }

    private static Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static String alarmContent(AlarmRecordPageVO alarm) {
        if (alarm.getAlarmEventName() != null && !alarm.getAlarmEventName().isBlank()) {
            return alarm.getAlarmEventName();
        }
        if (alarm.getAlarmEvent() != null && !alarm.getAlarmEvent().isBlank()) {
            return alarm.getAlarmEvent();
        }
        return alarm.getAlarmTargetName();
    }

    private enum GasRole {
        CO2, O2, PH3
    }

    private record AlarmData(Pagination<AlarmRecordPageVO> alarmPage, Set<Long> activeWarehouseIds) {
    }
}
