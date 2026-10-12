package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmDeviceStatusStatisticsPageDTO;
import com.guhecloud.cube.base.alarm.client.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.client.param.AlarmDeviceStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmRecordPageVO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.cube.srv.gen.securitymanagement.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.DeviceMonitoringVo;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseMeasureHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarMonitoringService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseMeasureHistoryService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringAlarmVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringDashboardVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringSummaryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringWarehouseVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** 雷达监测看板聚合服务。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RadarMonitoringServiceImpl implements RadarMonitoringService {

    private static final int RECENT_ALARM_LIMIT = 5;
    private static final int ALARM_QUERY_PAGE_SIZE = 1000;
    private static final String RADAR_MONITORING_APPLICATION_CODE = "RESERVE_MONITORING";

    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final WarehousePageApiClient warehousePageApiClient;
    private final WarehouseMeasureHistoryService warehouseMeasureHistoryService;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final SecurityDeviceClient securityDeviceClient;
    private final AlarmRecordClient alarmRecordClient;

    @Override
    public RadarMonitoringDashboardVO dashboard() {
        List<RadarWarehouseBinding> bindingRows = radarWarehouseBindingService.lambdaQuery()
                .orderByAsc(RadarWarehouseBinding::getWarehouseId)
                .list();
        List<RadarWarehouseBinding> bindings = distinctBindings(bindingRows);
        List<DeviceMonitoringVo> applicationDevices = loadApplicationDevices();
        if (bindings.isEmpty()) {
            RadarMonitoringDashboardVO dashboard = emptyDashboard();
            dashboard.setSummary(toSummary(applicationDevices, Pagination.empty(), Set.of()));
            return dashboard;
        }

        Map<Long, Long> warehouseIdByRadarId = bindings.stream()
                .collect(Collectors.toMap(RadarWarehouseBinding::getRadarDeviceId,
                                          RadarWarehouseBinding::getWarehouseId,
                                          (first, ignored) -> first,
                                          LinkedHashMap::new));
        Set<Long> warehouseIds = new HashSet<>(warehouseIdByRadarId.values());
        Set<Long> radarDeviceIds = warehouseIdByRadarId.keySet();

        Map<Long, LiangcangWarehousePageItemVO> warehouseById = warehousePageApiClient.fetchPageItemsByIds(warehouseIds);
        Map<Long, WarehouseMeasureHistory> measureByWarehouseId =
                warehouseMeasureHistoryService.latestByWarehouseIds(warehouseIds);
        Map<Long, DeviceBaseDetailsDTO> deviceByRadarThirdPartyId = loadDeviceDetailsByRadarThirdPartyId(radarDeviceIds);
        Map<Long, Long> warehouseIdByAlarmTargetKey = buildAlarmTargetWarehouseMapping(
                warehouseIdByRadarId, applicationDevices);
        AlarmData alarmData = loadAlarmData(warehouseIdByAlarmTargetKey);
        Pagination<AlarmRecordPageVO> alarmPage = alarmData.alarmPage();
        Map<Long, AlarmRecordPageVO> alarmByWarehouseId = indexAlarms(alarmPage.getRecords(), warehouseIdByAlarmTargetKey);

        List<RadarMonitoringWarehouseVO> warehouses = bindings.stream()
                .map(binding -> toWarehouseVO(binding, warehouseById, measureByWarehouseId,
                                              deviceByRadarThirdPartyId,
                                              alarmByWarehouseId, alarmData.activeWarehouseIds()))
                .toList();
        List<RadarMonitoringAlarmVO> recentAlarms = toRecentAlarms(alarmPage.getRecords(),
                                                                    warehouseIdByAlarmTargetKey, warehouseById);

        RadarMonitoringDashboardVO dashboard = new RadarMonitoringDashboardVO();
        dashboard.setSummary(toSummary(applicationDevices, alarmPage, alarmData.activeWarehouseIds()));
        dashboard.setWarehouses(warehouses);
        dashboard.setRecentAlarms(recentAlarms);
        log.info("雷达监测看板聚合完成，绑定数={}，设备详情数={}，测量数={}，告警数={}",
                 bindings.size(), deviceByRadarThirdPartyId.size(), measureByWarehouseId.size(), alarmPage.getTotal());
        return dashboard;
    }

    @Override
    public RadarMonitoringSummaryVO summary() {
        List<DeviceMonitoringVo> applicationDevices = loadApplicationDevices();
        List<RadarWarehouseBinding> bindings = distinctBindings(radarWarehouseBindingService.lambdaQuery()
                .orderByAsc(RadarWarehouseBinding::getWarehouseId)
                .list());

        if (bindings.isEmpty()) {
            return toSummary(applicationDevices, Pagination.empty(), Set.of());
        }

        Map<Long, Long> warehouseIdByRadarId = bindings.stream()
                .collect(Collectors.toMap(RadarWarehouseBinding::getRadarDeviceId,
                                          RadarWarehouseBinding::getWarehouseId,
                                          (first, ignored) -> first,
                                          LinkedHashMap::new));
        Map<Long, Long> warehouseIdByAlarmTargetKey = buildAlarmTargetWarehouseMapping(
                warehouseIdByRadarId, applicationDevices);
        AlarmData alarmData = loadAlarmData(warehouseIdByAlarmTargetKey);

        return toSummary(applicationDevices, alarmData.alarmPage(), alarmData.activeWarehouseIds());
    }

    @Override
    public List<RadarMonitoringWarehouseVO> warehouses() {
        List<RadarWarehouseBinding> bindings = distinctBindings(radarWarehouseBindingService.lambdaQuery()
                .orderByAsc(RadarWarehouseBinding::getWarehouseId)
                .list());
        if (bindings.isEmpty()) {
            return List.of();
        }

        Map<Long, Long> warehouseIdByRadarId = bindings.stream()
                .collect(Collectors.toMap(RadarWarehouseBinding::getRadarDeviceId,
                                          RadarWarehouseBinding::getWarehouseId,
                                          (first, ignored) -> first,
                                          LinkedHashMap::new));
        Set<Long> warehouseIds = new HashSet<>(warehouseIdByRadarId.values());
        Set<Long> radarDeviceIds = warehouseIdByRadarId.keySet();
        List<DeviceMonitoringVo> applicationDevices = loadApplicationDevices();

        Map<Long, LiangcangWarehousePageItemVO> warehouseById = warehousePageApiClient.fetchPageItemsByIds(warehouseIds);
        Map<Long, WarehouseMeasureHistory> measureByWarehouseId =
                warehouseMeasureHistoryService.latestByWarehouseIds(warehouseIds);
        Map<Long, DeviceBaseDetailsDTO> deviceByRadarThirdPartyId = loadDeviceDetailsByRadarThirdPartyId(radarDeviceIds);
        Map<Long, Long> warehouseIdByAlarmTargetKey = buildAlarmTargetWarehouseMapping(
                warehouseIdByRadarId, applicationDevices);
        AlarmData alarmData = loadAlarmData(warehouseIdByAlarmTargetKey);
        Map<Long, AlarmRecordPageVO> alarmByWarehouseId = indexAlarms(
                alarmData.alarmPage().getRecords(), warehouseIdByAlarmTargetKey);

        return bindings.stream()
                .map(binding -> toWarehouseVO(binding, warehouseById, measureByWarehouseId,
                                              deviceByRadarThirdPartyId,
                                              alarmByWarehouseId, alarmData.activeWarehouseIds()))
                .toList();
    }

    @Override
    public List<RadarMonitoringAlarmVO> recentAlarms() {
        List<RadarWarehouseBinding> bindings = distinctBindings(radarWarehouseBindingService.lambdaQuery()
                .orderByAsc(RadarWarehouseBinding::getWarehouseId)
                .list());
        if (bindings.isEmpty()) {
            return List.of();
        }

        Map<Long, Long> warehouseIdByRadarId = bindings.stream()
                .collect(Collectors.toMap(RadarWarehouseBinding::getRadarDeviceId,
                                          RadarWarehouseBinding::getWarehouseId,
                                          (first, ignored) -> first,
                                          LinkedHashMap::new));
        Set<Long> warehouseIds = new HashSet<>(warehouseIdByRadarId.values());
        List<DeviceMonitoringVo> applicationDevices = loadApplicationDevices();
        Map<Long, Long> warehouseIdByAlarmTargetKey = buildAlarmTargetWarehouseMapping(
                warehouseIdByRadarId, applicationDevices);
        Map<Long, LiangcangWarehousePageItemVO> warehouseById = warehousePageApiClient.fetchPageItemsByIds(warehouseIds);
        Pagination<AlarmRecordPageVO> alarmPage = loadAlarms(warehouseIdByAlarmTargetKey.keySet());

        return toRecentAlarms(alarmPage.getRecords(), warehouseIdByAlarmTargetKey, warehouseById);
    }

    private List<DeviceMonitoringVo> loadApplicationDevices() {
        InsideSecurityDeviceParam param = new InsideSecurityDeviceParam();
        param.setApplicationCode(RADAR_MONITORING_APPLICATION_CODE);
        List<DeviceMonitoringVo> devices = securityDeviceClient.getDeviceMonitoringList(param);
        return devices == null ? List.of() : devices;
    }

    private static List<RadarWarehouseBinding> distinctBindings(List<RadarWarehouseBinding> rows) {
        if (rows == null || rows.isEmpty()) {
            return List.of();
        }
        Set<Long> warehouseIds = new HashSet<>();
        Set<Long> radarDeviceIds = new HashSet<>();
        List<RadarWarehouseBinding> result = new ArrayList<>();
        for (RadarWarehouseBinding row : rows) {
            if (row == null || row.getWarehouseId() == null || row.getRadarDeviceId() == null
                || !warehouseIds.add(row.getWarehouseId()) || !radarDeviceIds.add(row.getRadarDeviceId())) {
                continue;
            }
            result.add(row);
        }
        return result;
    }

    private Map<Long, DeviceBaseDetailsDTO> loadDeviceDetailsByRadarThirdPartyId(Set<Long> radarThirdPartyIds) {
        if (radarThirdPartyIds.isEmpty()) {
            return Map.of();
        }
        Map<Long, DeviceBaseDetailsDTO> result = new LinkedHashMap<>();
        for (Long radarThirdPartyId : radarThirdPartyIds) {
            if (radarThirdPartyId == null) {
                continue;
            }
            DeviceBaseDetailsDTO detail = deviceCenterStdClient
                    .getDetailsByThirdPartyId(String.valueOf(radarThirdPartyId));
            if (detail != null) {
                result.put(radarThirdPartyId, detail);
            }
        }
        return result;
    }

    private static Map<Long, Long> buildAlarmTargetWarehouseMapping(Map<Long, Long> warehouseIdByRadarId,
                                                                    List<DeviceMonitoringVo> applicationDevices) {
        Map<Long, Long> result = new HashMap<>();
        for (DeviceMonitoringVo device : applicationDevices) {
            if (device == null) {
                continue;
            }
            Long radarDeviceId = parseLong(device.getThirdPartyId());
            Long warehouseId = warehouseIdByRadarId.get(radarDeviceId);
            if (warehouseId == null) {
                continue;
            }
            if (device.getDeviceId() != null) {
                result.putIfAbsent(device.getDeviceId(), warehouseId);
            }
            if (radarDeviceId != null) {
                result.putIfAbsent(radarDeviceId, warehouseId);
            }
        }
        return result;
    }

    private AlarmData loadAlarmData(Map<Long, Long> warehouseIdByAlarmTargetKey) {
        if (warehouseIdByAlarmTargetKey.isEmpty()) {
            return new AlarmData(Pagination.empty(), Set.of());
        }
        Set<Long> alarmTargetKeys = warehouseIdByAlarmTargetKey.keySet();
        Pagination<AlarmRecordPageVO> alarmPage = loadAlarms(alarmTargetKeys);

        AlarmDeviceStatusTotalQueryParam statusParam = new AlarmDeviceStatusTotalQueryParam();
        statusParam.setIds(new ArrayList<>(alarmTargetKeys));
        statusParam.setApplicationCode(RADAR_MONITORING_APPLICATION_CODE);
        List<AlarmDeviceStatusStatisticsPageDTO> deviceStatuses =
                alarmRecordClient.getDeviceAlarmStatusTotalList(statusParam);
        Set<Long> activeWarehouseIds = deviceStatuses == null
                                       ? Set.of()
                                       : deviceStatuses.stream()
                                               .filter(RadarMonitoringServiceImpl::hasActiveAlarm)
                                               .map(AlarmDeviceStatusStatisticsPageDTO::getDeviceId)
                                               .map(warehouseIdByAlarmTargetKey::get)
                                               .filter(java.util.Objects::nonNull)
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
        param.setApplicationCode(RADAR_MONITORING_APPLICATION_CODE);
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

    private static Map<Long, AlarmRecordPageVO> indexAlarms(List<AlarmRecordPageVO> alarms,
                                                             Map<Long, Long> warehouseIdByAlarmTargetKey) {
        if (alarms == null || alarms.isEmpty()) {
            return Map.of();
        }
        Map<Long, AlarmRecordPageVO> result = new HashMap<>();
        for (AlarmRecordPageVO alarm : alarms) {
            if (alarm == null || alarm.getAlarmTargetKey() == null
                || AlarmStatusEnum.ALARM_HANDLER == alarm.getAlarmStatus()
                || !warehouseIdByAlarmTargetKey.containsKey(alarm.getAlarmTargetKey())) {
                continue;
            }
            Long warehouseId = warehouseIdByAlarmTargetKey.get(alarm.getAlarmTargetKey());
            result.putIfAbsent(warehouseId, alarm);
        }
        return result;
    }

    private static RadarMonitoringWarehouseVO toWarehouseVO(RadarWarehouseBinding binding,
                                                            Map<Long, LiangcangWarehousePageItemVO> warehouseById,
                                                            Map<Long, WarehouseMeasureHistory> measureByWarehouseId,
                                                            Map<Long, DeviceBaseDetailsDTO> deviceByRadarThirdPartyId,
                                                            Map<Long, AlarmRecordPageVO> alarmByWarehouseId,
                                                            Set<Long> activeWarehouseIds) {
        Long warehouseId = binding.getWarehouseId();
        Long radarDeviceId = binding.getRadarDeviceId();
        LiangcangWarehousePageItemVO warehouse = warehouseById.get(warehouseId);
        WarehouseMeasureHistory measure = measureByWarehouseId.get(warehouseId);
        DeviceBaseDetailsDTO device = deviceByRadarThirdPartyId.get(radarDeviceId);
        AlarmRecordPageVO alarm = alarmByWarehouseId.get(warehouseId);

        RadarMonitoringWarehouseVO result = new RadarMonitoringWarehouseVO();
        result.setWarehouseId(warehouseId);
        result.setWarehouseName(warehouse == null ? null : warehouse.getName());
        result.setRadarDeviceId(radarDeviceId);
        result.setTotalVolume(warehouse == null ? null : warehouse.getTotalVolume());
        if (measure != null) {
            result.setCurrentWeight(measure.getWeight());
            result.setCurrentVolume(measure.getMaterialVolume());
            result.setLatestReportTime(measure.getAcquisitionDate());
        }
        if (result.getLatestReportTime() == null && device != null) {
            result.setLatestReportTime(device.getCommunicationTime());
        }
        result.setOnline(device != null && Boolean.TRUE.equals(device.getDeviceState()));
        boolean activeAlarm = activeWarehouseIds.contains(warehouseId);
        result.setAlarm(activeAlarm);
        result.setAlarmMessage(activeAlarm && alarm != null ? alarmContent(alarm) : null);
        return result;
    }

    private static List<RadarMonitoringAlarmVO> toRecentAlarms(List<AlarmRecordPageVO> alarms,
                                                               Map<Long, Long> warehouseIdByAlarmTargetKey,
                                                               Map<Long, LiangcangWarehousePageItemVO> warehouseById) {
        if (alarms == null || alarms.isEmpty()) {
            return List.of();
        }
        return alarms.stream()
                .filter(java.util.Objects::nonNull)
                .limit(RECENT_ALARM_LIMIT)
                .map(alarm -> toAlarmVO(alarm, warehouseIdByAlarmTargetKey, warehouseById))
                .toList();
    }

    private static RadarMonitoringAlarmVO toAlarmVO(AlarmRecordPageVO alarm,
                                                    Map<Long, Long> warehouseIdByAlarmTargetKey,
                                                    Map<Long, LiangcangWarehousePageItemVO> warehouseById) {
        Long warehouseId = warehouseIdByAlarmTargetKey.get(alarm.getAlarmTargetKey());
        LiangcangWarehousePageItemVO warehouse = warehouseById.get(warehouseId);
        RadarMonitoringAlarmVO result = new RadarMonitoringAlarmVO();
        result.setId(alarm.getId());
        result.setContent(alarmContent(alarm));
        result.setLevel(alarm.getAlarmLevelName());
        result.setAlarmTargetKey(alarm.getAlarmTargetKey());
        result.setWarehouseName(warehouse == null ? alarm.getAlarmTargetName() : warehouse.getName());
        result.setAlarmArea(alarm.getAreaName() == null ? alarm.getArea() : alarm.getAreaName());
        result.setAlarmTime(alarm.getAlarmTime());
        return result;
    }

    private static RadarMonitoringSummaryVO toSummary(List<DeviceMonitoringVo> applicationDevices,
                                                      Pagination<AlarmRecordPageVO> alarmPage,
                                                      Set<Long> activeWarehouseIds) {
        RadarMonitoringSummaryVO summary = new RadarMonitoringSummaryVO();
        int onlineCount = (int) applicationDevices.stream()
                .filter(device -> device != null && Boolean.TRUE.equals(device.getDeviceStatus()))
                .count();
        summary.setTotalDeviceCount(applicationDevices.size());
        summary.setOnlineDeviceCount(onlineCount);
        summary.setOfflineDeviceCount(applicationDevices.size() - onlineCount);
        summary.setAlarmWarehouseCount(activeWarehouseIds.size());
        summary.setAlarmCount(alarmPage.getTotal());
        return summary;
    }

    private static RadarMonitoringDashboardVO emptyDashboard() {
        RadarMonitoringSummaryVO summary = new RadarMonitoringSummaryVO();
        summary.setTotalDeviceCount(0);
        summary.setOnlineDeviceCount(0);
        summary.setOfflineDeviceCount(0);
        summary.setAlarmWarehouseCount(0);
        summary.setAlarmCount(0L);

        RadarMonitoringDashboardVO dashboard = new RadarMonitoringDashboardVO();
        dashboard.setSummary(summary);
        dashboard.setWarehouses(Collections.emptyList());
        dashboard.setRecentAlarms(Collections.emptyList());
        return dashboard;
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

    private record AlarmData(Pagination<AlarmRecordPageVO> alarmPage, Set<Long> activeWarehouseIds) {
    }
}
