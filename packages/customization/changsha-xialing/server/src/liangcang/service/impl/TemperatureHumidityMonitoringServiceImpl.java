package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmDeviceStatusStatisticsPageDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmDeviceStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmRecordPageVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.LiangcangLocationCardConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertRecord;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureReportParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.TemperatureHumidityMonitoringService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureAlertRecordService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendStatisticsService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringAlarmVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringAlertVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringDashboardVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringSummaryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringWarehouseVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringWarningVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureReportPointVO;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.cube.srv.gen.securitymanagement.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.DeviceMonitoringVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/** 温湿度监测看板聚合服务。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureHumidityMonitoringServiceImpl implements TemperatureHumidityMonitoringService {

    private static final String APPLICATION_CODE = "TEMP_HUM_MANAGEME";
    private static final int RECENT_LIMIT = 5;
    private static final int ALARM_QUERY_PAGE_SIZE = 1000;
    private static final int NUMBER_SCALE = 5;

    private final SecurityDeviceClient securityDeviceClient;
    private final WarehouseDeviceBindingService warehouseDeviceBindingService;
    private final WarehousePageApiClient warehousePageApiClient;
    private final LiangcangLocationCardConfigProvider locationCardConfigProvider;
    private final WarehouseTemperatureTrendStatisticsService temperatureStatisticsService;
    private final WarehouseTemperatureAlertRecordService temperatureAlertRecordService;
    private final IotDevicePropClient iotDevicePropClient;
    private final AlarmRecordClient alarmRecordClient;

    @Override
    public TemperatureHumidityMonitoringDashboardVO dashboard() {
        List<DeviceMonitoringVo> applicationDevices = loadApplicationDevices();
        Map<Long, DeviceMonitoringVo> applicationDeviceById = indexApplicationDevices(applicationDevices);
        Map<Long, List<DeviceMonitoringVo>> devicesByWarehouse = loadBoundDevicesByWarehouse(applicationDeviceById);
        Set<Long> warehouseIds = devicesByWarehouse.keySet();

        Map<Long, LiangcangWarehousePageItemVO> warehouseById = warehousePageApiClient.fetchPageItemsByIds(warehouseIds);
        LiangcangLocationCardConfigDTO config = locationCardConfigProvider.resolve();
        Map<Long, TemperatureValues> temperatureByWarehouse = loadTemperatures(
                warehouseIds, applicationDevices, config);
        Map<Long, BigDecimal> humidityByWarehouse = loadHumidity(devicesByWarehouse, config);

        Map<Long, Long> warehouseByAlarmTarget = buildAlarmTargetMapping(
                devicesByWarehouse, applicationDevices, warehouseIds);
        Pagination<AlarmRecordPageVO> alarmPage = loadAlarms(warehouseByAlarmTarget.keySet());
        Set<Long> alarmWarehouseIds = loadActiveAlarmWarehouseIds(warehouseByAlarmTarget);
        List<TemperatureHumidityMonitoringAlarmVO> recentAlarms = toRecentAlarms(
                alarmPage.getRecords(), warehouseByAlarmTarget, warehouseById);

        WarningData warningData = loadWarnings(warehouseIds);
        List<TemperatureHumidityMonitoringWarehouseVO> warehouses = devicesByWarehouse.entrySet().stream()
                .map(entry -> toWarehouse(entry.getKey(), entry.getValue(), warehouseById,
                                          temperatureByWarehouse, humidityByWarehouse,
                                          alarmWarehouseIds, warningData.activeWarehouseIds()))
                .toList();

        TemperatureHumidityMonitoringDashboardVO result = new TemperatureHumidityMonitoringDashboardVO();
        result.setSummary(toSummary(applicationDevices, alarmPage, alarmWarehouseIds, warningData));
        result.setWarehouses(warehouses);
        result.setRecentAlarms(recentAlarms);
        result.setRecentTemperatureWarnings(warningData.recentWarnings());
        log.info("温湿度监测看板聚合完成，应用设备数={}，展示仓房数={}，告警数={}，温度预警数={}",
                 applicationDevices.size(), warehouses.size(), alarmPage.getTotal(), warningData.total());
        return result;
    }

    @Override
    public TemperatureHumidityMonitoringSummaryVO summary() {
        List<DeviceMonitoringVo> applicationDevices = loadApplicationDevices();
        Map<Long, DeviceMonitoringVo> applicationDeviceById = indexApplicationDevices(applicationDevices);
        Map<Long, List<DeviceMonitoringVo>> devicesByWarehouse = loadBoundDevicesByWarehouse(applicationDeviceById);
        Set<Long> warehouseIds = devicesByWarehouse.keySet();

        Map<Long, Long> warehouseByAlarmTarget = buildAlarmTargetMapping(
                devicesByWarehouse, applicationDevices, warehouseIds);
        Pagination<AlarmRecordPageVO> alarmPage = loadAlarms(warehouseByAlarmTarget.keySet());
        Set<Long> alarmWarehouseIds = loadActiveAlarmWarehouseIds(warehouseByAlarmTarget);
        WarningData warningData = loadWarnings(warehouseIds);

        return toSummary(applicationDevices, alarmPage, alarmWarehouseIds, warningData);
    }

    @Override
    public List<TemperatureHumidityMonitoringWarehouseVO> warehouses() {
        List<DeviceMonitoringVo> applicationDevices = loadApplicationDevices();
        Map<Long, DeviceMonitoringVo> applicationDeviceById = indexApplicationDevices(applicationDevices);
        Map<Long, List<DeviceMonitoringVo>> devicesByWarehouse = loadBoundDevicesByWarehouse(applicationDeviceById);
        Set<Long> warehouseIds = devicesByWarehouse.keySet();

        if (warehouseIds.isEmpty()) {
            return List.of();
        }

        Map<Long, LiangcangWarehousePageItemVO> warehouseById = warehousePageApiClient.fetchPageItemsByIds(warehouseIds);
        LiangcangLocationCardConfigDTO config = locationCardConfigProvider.resolve();
        Map<Long, TemperatureValues> temperatureByWarehouse = loadTemperatures(
                warehouseIds, applicationDevices, config);
        Map<Long, BigDecimal> humidityByWarehouse = loadHumidity(devicesByWarehouse, config);

        Map<Long, Long> warehouseByAlarmTarget = buildAlarmTargetMapping(
                devicesByWarehouse, applicationDevices, warehouseIds);
        Set<Long> alarmWarehouseIds = loadActiveAlarmWarehouseIds(warehouseByAlarmTarget);
        WarningData warningData = loadWarnings(warehouseIds);

        return devicesByWarehouse.entrySet().stream()
                .map(entry -> toWarehouse(entry.getKey(), entry.getValue(), warehouseById,
                                          temperatureByWarehouse, humidityByWarehouse,
                                          alarmWarehouseIds, warningData.activeWarehouseIds()))
                .toList();
    }

    @Override
    public TemperatureHumidityMonitoringAlertVO recentAlarmsAndWarnings() {
        List<DeviceMonitoringVo> applicationDevices = loadApplicationDevices();
        Map<Long, DeviceMonitoringVo> applicationDeviceById = indexApplicationDevices(applicationDevices);
        Map<Long, List<DeviceMonitoringVo>> devicesByWarehouse = loadBoundDevicesByWarehouse(applicationDeviceById);
        Set<Long> warehouseIds = devicesByWarehouse.keySet();
        Map<Long, LiangcangWarehousePageItemVO> warehouseById = warehousePageApiClient.fetchPageItemsByIds(warehouseIds);

        Map<Long, Long> warehouseByAlarmTarget = buildAlarmTargetMapping(
                devicesByWarehouse, applicationDevices, warehouseIds);
        Pagination<AlarmRecordPageVO> alarmPage = loadAlarms(warehouseByAlarmTarget.keySet());
        List<TemperatureHumidityMonitoringAlarmVO> recentAlarms = toRecentAlarms(
                alarmPage.getRecords(), warehouseByAlarmTarget, warehouseById);
        WarningData warningData = loadWarnings(warehouseIds);

        TemperatureHumidityMonitoringAlertVO result = new TemperatureHumidityMonitoringAlertVO();
        result.setRecentAlarms(recentAlarms);
        result.setRecentTemperatureWarnings(warningData.recentWarnings());
        return result;
    }

    private List<DeviceMonitoringVo> loadApplicationDevices() {
        InsideSecurityDeviceParam param = new InsideSecurityDeviceParam();
        param.setApplicationCode(APPLICATION_CODE);
        List<DeviceMonitoringVo> devices = securityDeviceClient.getDeviceMonitoringList(param);
        return devices == null ? List.of() : devices.stream().filter(Objects::nonNull).toList();
    }

    private Map<Long, List<DeviceMonitoringVo>> loadBoundDevicesByWarehouse(
            Map<Long, DeviceMonitoringVo> applicationDeviceById) {
        if (applicationDeviceById.isEmpty()) {
            return Map.of();
        }
        List<WarehouseDeviceBinding> bindings = warehouseDeviceBindingService.list(
                Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                        .select(WarehouseDeviceBinding::getWarehouseId, WarehouseDeviceBinding::getDeviceId)
                        .in(WarehouseDeviceBinding::getDeviceId, applicationDeviceById.keySet())
                        .orderByAsc(WarehouseDeviceBinding::getWarehouseId));
        Map<Long, Map<Long, DeviceMonitoringVo>> indexed = new LinkedHashMap<>();
        for (WarehouseDeviceBinding binding : bindings) {
            if (binding == null || binding.getWarehouseId() == null || binding.getDeviceId() == null) {
                continue;
            }
            DeviceMonitoringVo device = applicationDeviceById.get(binding.getDeviceId());
            if (device != null) {
                indexed.computeIfAbsent(binding.getWarehouseId(), ignored -> new LinkedHashMap<>())
                        .putIfAbsent(binding.getDeviceId(), device);
            }
        }
        return indexed.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> new ArrayList<>(entry.getValue().values()),
                (first, ignored) -> first,
                LinkedHashMap::new));
    }

    private Map<Long, TemperatureValues> loadTemperatures(Set<Long> warehouseIds,
                                                           List<DeviceMonitoringVo> applicationDevices,
                                                           LiangcangLocationCardConfigDTO config) {
        if (warehouseIds.isEmpty()) {
            return Map.of();
        }
        Long specialWarehouseId = temperatureStatisticsService.specialLayoutWarehouseId();
        Map<Long, TemperatureValues> result = new HashMap<>();
        if (specialWarehouseId != null && warehouseIds.contains(specialWarehouseId)) {
            WarehouseTemperatureReportParam param = new WarehouseTemperatureReportParam();
            param.setWarehouseId(specialWarehouseId);
            result.put(specialWarehouseId, aggregateReport(
                    temperatureStatisticsService.temperatureReport(param)));
        }

        Map<Long, Long> mainDeviceByWarehouse = applicationDevices.stream()
                .filter(device -> device.getDeviceId() != null)
                .filter(device -> warehouseIds.contains(parseLong(device.getThirdPartyId())))
                .collect(Collectors.toMap(device -> parseLong(device.getThirdPartyId()),
                                          DeviceMonitoringVo::getDeviceId,
                                          (first, ignored) -> first,
                                          LinkedHashMap::new));
        if (specialWarehouseId != null) {
            mainDeviceByWarehouse.remove(specialWarehouseId);
        }
        Map<Long, Map<String, BigDecimal>> propsByDevice = loadCurrentProps(
                mainDeviceByWarehouse.values(), temperaturePropCodes(config));
        for (Map.Entry<Long, Long> entry : mainDeviceByWarehouse.entrySet()) {
            Map<String, BigDecimal> values = propsByDevice.get(entry.getValue());
            if (values == null) {
                continue;
            }
            result.put(entry.getKey(), new TemperatureValues(
                    scale(values.get(trim(config.getWarehouseMaxTemperaturePropCode()))),
                    scale(values.get(trim(config.getWarehouseMinTemperaturePropCode()))),
                    scale(values.get(trim(config.getWarehouseAvgTemperaturePropCode())))));
        }
        return result;
    }

    private Map<Long, BigDecimal> loadHumidity(Map<Long, List<DeviceMonitoringVo>> devicesByWarehouse,
                                               LiangcangLocationCardConfigDTO config) {
        String propCode = trim(config.getHumidityRealtimePropCode());
        Set<String> humidityProductCodes = config.getHumidityProductCodes() == null
                                           ? Set.of()
                                           : config.getHumidityProductCodes().stream()
                                                   .map(TemperatureHumidityMonitoringServiceImpl::trim)
                                                   .filter(Objects::nonNull)
                                                   .collect(Collectors.toSet());
        if (devicesByWarehouse.isEmpty() || propCode == null || humidityProductCodes.isEmpty()) {
            return Map.of();
        }
        List<Long> deviceIds = devicesByWarehouse.values().stream().flatMap(List::stream)
                .filter(device -> matchesProductCode(device, humidityProductCodes))
                .map(DeviceMonitoringVo::getDeviceId).filter(Objects::nonNull).distinct().toList();
        Map<Long, Map<String, BigDecimal>> propsByDevice = loadCurrentProps(deviceIds, Set.of(propCode));
        Map<Long, BigDecimal> result = new HashMap<>();
        for (Map.Entry<Long, List<DeviceMonitoringVo>> entry : devicesByWarehouse.entrySet()) {
            List<BigDecimal> values = entry.getValue().stream()
                    .filter(device -> matchesProductCode(device, humidityProductCodes))
                    .map(DeviceMonitoringVo::getDeviceId)
                    .map(propsByDevice::get)
                    .filter(Objects::nonNull)
                    .map(props -> props.get(propCode))
                    .filter(Objects::nonNull)
                    .toList();
            BigDecimal average = average(values);
            if (average != null) {
                result.put(entry.getKey(), scale(average));
            }
        }
        return result;
    }

    private static boolean matchesProductCode(DeviceMonitoringVo device, Set<String> productCodes) {
        return device != null && device.getDeviceProdId() != null
               && productCodes.contains(String.valueOf(device.getDeviceProdId()));
    }

    private Map<Long, Map<String, BigDecimal>> loadCurrentProps(Iterable<Long> deviceIds,
                                                                Set<String> propCodes) {
        Set<Long> ids = new LinkedHashSet<>();
        deviceIds.forEach(id -> {
            if (id != null) {
                ids.add(id);
            }
        });
        Set<String> codes = propCodes.stream().map(TemperatureHumidityMonitoringServiceImpl::trim)
                .filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
        if (ids.isEmpty() || codes.isEmpty()) {
            return Map.of();
        }
        String spec = ids.stream().flatMap(id -> codes.stream().map(code -> id + "." + code))
                .collect(Collectors.joining(","));
        MultiDevicePropParam param = new MultiDevicePropParam();
        param.setDevIdAndPropCodes(spec);
        Map<String, List<KeyValueWithTsDTO>> response = iotDevicePropClient.multiProp(param);
        if (response == null || response.isEmpty()) {
            return Map.of();
        }
        Map<Long, Map<String, BigDecimal>> result = new HashMap<>();
        for (Long deviceId : ids) {
            List<KeyValueWithTsDTO> rows = response.get(String.valueOf(deviceId));
            if (rows == null) {
                continue;
            }
            Map<String, BigDecimal> values = new HashMap<>();
            for (KeyValueWithTsDTO row : rows) {
                String code = row == null ? null : trim(row.getKey());
                BigDecimal value = row == null ? null : toBigDecimal(row.getValue());
                if (code != null && value != null) {
                    values.put(code, value);
                }
            }
            if (!values.isEmpty()) {
                result.put(deviceId, values);
            }
        }
        return result;
    }

    private Pagination<AlarmRecordPageVO> loadAlarms(Set<Long> alarmTargetKeys) {
        if (alarmTargetKeys.isEmpty()) {
            return Pagination.empty();
        }
        AlarmRecordQueryParam param = new AlarmRecordQueryParam();
        param.setPageNumber(1);
        param.setPageSize(ALARM_QUERY_PAGE_SIZE);
        param.setApplicationCode(APPLICATION_CODE);
        param.setDeviceIds(new ArrayList<>(alarmTargetKeys));
        Pagination<AlarmRecordPageVO> page = alarmRecordClient.page(param);
        return page == null ? Pagination.empty() : page;
    }

    private Set<Long> loadActiveAlarmWarehouseIds(Map<Long, Long> warehouseByAlarmTarget) {
        if (warehouseByAlarmTarget.isEmpty()) {
            return Set.of();
        }
        Set<Long> alarmTargetKeys = warehouseByAlarmTarget.keySet();
        AlarmDeviceStatusTotalQueryParam param = new AlarmDeviceStatusTotalQueryParam();
        param.setIds(new ArrayList<>(alarmTargetKeys));
        param.setApplicationCode(APPLICATION_CODE);
        List<AlarmDeviceStatusStatisticsPageDTO> deviceStatuses =
                alarmRecordClient.getDeviceAlarmStatusTotalList(param);
        if (deviceStatuses == null) {
            return Set.of();
        }
        return deviceStatuses.stream()
                .filter(TemperatureHumidityMonitoringServiceImpl::hasActiveAlarm)
                .map(AlarmDeviceStatusStatisticsPageDTO::getDeviceId)
                .map(warehouseByAlarmTarget::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private static boolean hasActiveAlarm(AlarmDeviceStatusStatisticsPageDTO status) {
        return status != null && (positive(status.getUnConfirmedNum())
                                  || positive(status.getProgressConfirmedNum())
                                  || positive(status.getSuspendNum()));
    }

    private static boolean positive(Long value) {
        return value != null && value > 0;
    }

    private WarningData loadWarnings(Set<Long> warehouseIds) {
        if (warehouseIds.isEmpty()) {
            return new WarningData(0L, Set.of(), List.of());
        }
        Page<WarehouseTemperatureAlertRecord> recentPage =
                temperatureAlertRecordService.pageByWarehouseIds(warehouseIds, 1, RECENT_LIMIT);
        Set<Long> activeWarehouseIds = temperatureAlertRecordService.listActiveByWarehouseIds(warehouseIds).stream()
                .map(WarehouseTemperatureAlertRecord::getWarehouseId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<TemperatureHumidityMonitoringWarningVO> warnings = recentPage.getRecords().stream()
                .map(TemperatureHumidityMonitoringServiceImpl::toWarning)
                .toList();
        return new WarningData(recentPage.getTotal(), activeWarehouseIds, warnings);
    }

    private static Map<Long, DeviceMonitoringVo> indexApplicationDevices(List<DeviceMonitoringVo> devices) {
        return devices.stream().filter(device -> device.getDeviceId() != null)
                .collect(Collectors.toMap(DeviceMonitoringVo::getDeviceId,
                                          device -> device,
                                          (first, ignored) -> first,
                                          LinkedHashMap::new));
    }

    private static Map<Long, Long> buildAlarmTargetMapping(
            Map<Long, List<DeviceMonitoringVo>> devicesByWarehouse,
            List<DeviceMonitoringVo> applicationDevices,
            Set<Long> warehouseIds) {
        Map<Long, Long> result = new HashMap<>();
        devicesByWarehouse.forEach((warehouseId, devices) -> devices.forEach(device -> {
            if (device.getDeviceId() != null) {
                result.putIfAbsent(device.getDeviceId(), warehouseId);
            }
            Long thirdPartyId = parseLong(device.getThirdPartyId());
            if (thirdPartyId != null) {
                result.putIfAbsent(thirdPartyId, warehouseId);
            }
        }));
        for (DeviceMonitoringVo device : applicationDevices) {
            Long warehouseId = parseLong(device.getThirdPartyId());
            if (warehouseId == null || !warehouseIds.contains(warehouseId)) {
                continue;
            }
            result.putIfAbsent(warehouseId, warehouseId);
            if (device.getDeviceId() != null) {
                result.putIfAbsent(device.getDeviceId(), warehouseId);
            }
        }
        return result;
    }

    private static List<TemperatureHumidityMonitoringAlarmVO> toRecentAlarms(
            List<AlarmRecordPageVO> alarms,
            Map<Long, Long> warehouseByAlarmTarget,
            Map<Long, LiangcangWarehousePageItemVO> warehouseById) {
        if (alarms == null || alarms.isEmpty()) {
            return List.of();
        }
        return alarms.stream().filter(Objects::nonNull)
                .filter(alarm -> warehouseByAlarmTarget.containsKey(alarm.getAlarmTargetKey()))
                .limit(RECENT_LIMIT)
                .map(alarm -> toAlarm(alarm, warehouseByAlarmTarget, warehouseById))
                .toList();
    }

    private static TemperatureHumidityMonitoringWarehouseVO toWarehouse(
            Long warehouseId,
            List<DeviceMonitoringVo> devices,
            Map<Long, LiangcangWarehousePageItemVO> warehouseById,
            Map<Long, TemperatureValues> temperatureByWarehouse,
            Map<Long, BigDecimal> humidityByWarehouse,
            Set<Long> alarmWarehouseIds,
            Set<Long> warningWarehouseIds) {
        int onlineCount = (int) devices.stream()
                .filter(device -> Boolean.TRUE.equals(device.getDeviceStatus()))
                .count();
        LiangcangWarehousePageItemVO warehouse = warehouseById.get(warehouseId);
        TemperatureValues temperature = temperatureByWarehouse.get(warehouseId);
        TemperatureHumidityMonitoringWarehouseVO result = new TemperatureHumidityMonitoringWarehouseVO();
        result.setWarehouseId(warehouseId);
        result.setName(warehouse == null ? null : warehouse.getName());
        result.setOnlineDeviceCount(onlineCount);
        result.setOfflineDeviceCount(devices.size() - onlineCount);
        result.setOnline(onlineCount > 0);
        if (temperature != null) {
            result.setMaxTemperature(temperature.max());
            result.setMinTemperature(temperature.min());
            result.setAvgTemperature(temperature.avg());
        }
        result.setHumidity(humidityByWarehouse.get(warehouseId));
        result.setAlarm(alarmWarehouseIds.contains(warehouseId));
        result.setWarning(warningWarehouseIds.contains(warehouseId));
        return result;
    }

    private static TemperatureHumidityMonitoringSummaryVO toSummary(
            List<DeviceMonitoringVo> devices,
            Pagination<AlarmRecordPageVO> alarmPage,
            Set<Long> alarmWarehouseIds,
            WarningData warningData) {
        int onlineCount = (int) devices.stream()
                .filter(device -> Boolean.TRUE.equals(device.getDeviceStatus()))
                .count();
        TemperatureHumidityMonitoringSummaryVO result = new TemperatureHumidityMonitoringSummaryVO();
        result.setTotalDeviceCount(devices.size());
        result.setOnlineDeviceCount(onlineCount);
        result.setOfflineDeviceCount(devices.size() - onlineCount);
        result.setWarningWarehouseCount(warningData.activeWarehouseIds().size());
        result.setAlarmWarehouseCount(alarmWarehouseIds.size());
        result.setAlarmCount(alarmPage.getTotal());
        result.setWarningCount(warningData.total());
        return result;
    }

    private static TemperatureHumidityMonitoringAlarmVO toAlarm(
            AlarmRecordPageVO alarm,
            Map<Long, Long> warehouseByAlarmTarget,
            Map<Long, LiangcangWarehousePageItemVO> warehouseById) {
        Long warehouseId = warehouseByAlarmTarget.get(alarm.getAlarmTargetKey());
        LiangcangWarehousePageItemVO warehouse = warehouseById.get(warehouseId);
        TemperatureHumidityMonitoringAlarmVO result = new TemperatureHumidityMonitoringAlarmVO();
        result.setId(alarm.getId());
        result.setContent(alarmContent(alarm));
        result.setLevel(alarm.getAlarmLevelName());
        result.setAlarmTargetKey(alarm.getAlarmTargetKey());
        result.setWarehouseId(warehouseId);
        result.setWarehouseName(warehouse == null ? alarm.getAlarmTargetName() : warehouse.getName());
        result.setAlarmArea(alarm.getAreaName() == null ? alarm.getArea() : alarm.getAreaName());
        result.setAlarmTime(alarm.getAlarmTime());
        return result;
    }

    private static TemperatureHumidityMonitoringWarningVO toWarning(WarehouseTemperatureAlertRecord record) {
        TemperatureHumidityMonitoringWarningVO result = new TemperatureHumidityMonitoringWarningVO();
        result.setId(record.getId());
        result.setWarehouseId(record.getWarehouseId());
        result.setWarehouseName(record.getWarehouseName());
        result.setTriggeredAt(record.getTriggeredAt());
        result.setCurrentAvgTemperature(record.getCurrentAvgTemperature());
        result.setWarningThreshold(record.getWarningThreshold());
        result.setDistanceToThreshold(record.getDistanceToThreshold());
        result.setDailyIncrease(record.getDailyIncrease());
        result.setConsecutiveDays(record.getConsecutiveDays());
        result.setStatus(record.getStatus());
        return result;
    }

    private static TemperatureValues aggregateReport(List<List<WarehouseTemperatureReportPointVO>> report) {
        List<BigDecimal> values = report == null ? List.of() : report.stream()
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(WarehouseTemperatureReportPointVO::getTemperature)
                .filter(Objects::nonNull)
                .toList();
        if (values.isEmpty()) {
            return new TemperatureValues(null, null, null);
        }
        return new TemperatureValues(scale(values.stream().max(BigDecimal::compareTo).orElse(null)),
                                     scale(values.stream().min(BigDecimal::compareTo).orElse(null)),
                                     scale(average(values)));
    }

    private static Set<String> temperaturePropCodes(LiangcangLocationCardConfigDTO config) {
        Set<String> result = new LinkedHashSet<>();
        result.add(trim(config.getWarehouseMaxTemperaturePropCode()));
        result.add(trim(config.getWarehouseMinTemperaturePropCode()));
        result.add(trim(config.getWarehouseAvgTemperaturePropCode()));
        result.remove(null);
        return result;
    }

    private static BigDecimal average(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        BigDecimal sum = values.stream().filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long count = values.stream().filter(Objects::nonNull).count();
        return count == 0 ? null : sum.divide(BigDecimal.valueOf(count), NUMBER_SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal scale(BigDecimal value) {
        return value == null ? null : value.setScale(NUMBER_SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return value instanceof BigDecimal decimal ? decimal : new BigDecimal(String.valueOf(value).trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static Long parseLong(String value) {
        try {
            return trim(value) == null ? null : Long.valueOf(value.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static String trim(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static String alarmContent(AlarmRecordPageVO alarm) {
        if (trim(alarm.getAlarmEventName()) != null) {
            return alarm.getAlarmEventName();
        }
        if (trim(alarm.getAlarmEvent()) != null) {
            return alarm.getAlarmEvent();
        }
        return alarm.getAlarmTargetName();
    }

    private record TemperatureValues(BigDecimal max, BigDecimal min, BigDecimal avg) {
    }

    private record WarningData(long total,
                               Set<Long> activeWarehouseIds,
                               List<TemperatureHumidityMonitoringWarningVO> recentWarnings) {
    }
}
