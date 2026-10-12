package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.LiangcangLocationCardConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseMeasureHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseThreeDimensionalPeriodParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.LiangcangWarehouseThreeDimensionalStatisticsService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseMeasureHistoryService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseThreeDimensionalEnvironmentHistoryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseThreeDimensionalMeasureHistoryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseThreeDimensionalOverviewVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseOverviewSummaryVO;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.PropertyTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiHistorySampleParam;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/** 粮仓三维统计。 */
@Service
@RequiredArgsConstructor
public class LiangcangWarehouseThreeDimensionalStatisticsServiceImpl implements LiangcangWarehouseThreeDimensionalStatisticsService {

    private static final int AVERAGE_SCALE = 4;
    private static final int DEFAULT_STEP_MINUTES = 1;
    private static final String DEFAULT_AGG_TYPE = "raw";
    private static final String DEFAULT_STEP_AGG_TYPE = "avg";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final WarehouseDeviceBindingService warehouseDeviceBindingService;
    private final WarehouseMeasureHistoryService warehouseMeasureHistoryService;
    private final WarehousePageApiClient warehousePageApiClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final IotDevicePropClient iotDevicePropClient;
    private final LiangcangLocationCardConfigProvider locationCardConfigProvider;

    @Override
    public List<WarehouseThreeDimensionalOverviewVO> overview() {
        List<WarehouseSummary> summaries = resolveBoundWarehouseSummaries(null);
        if (ListUtils.isEmpty(summaries)) {
            return List.of();
        }
        Set<Long> warehouseIds = summaries.stream().map(WarehouseSummary::warehouseId).collect(Collectors.toSet());
        Map<Long, WarehouseMeasureHistory> measureByWarehouseId =
                warehouseMeasureHistoryService.latestByWarehouseIds(warehouseIds);
        Map<Long, BigDecimal> avgTemperatureByWarehouseId = loadCurrentAverageTemperatures(warehouseIds);

        List<WarehouseThreeDimensionalOverviewVO> out = new ArrayList<>();
        for (WarehouseSummary summary : summaries) {
            WarehouseMeasureHistory measure = measureByWarehouseId.get(summary.warehouseId());
            var vo = new WarehouseThreeDimensionalOverviewVO();
            vo.setWarehouseId(summary.warehouseId());
            vo.setWarehouseName(summary.name());
            vo.setCategoryName(summary.categoryName());
            vo.setQualityGrade(summary.qualityGrade());
            vo.setInventoryWeight(measure == null ? null : measure.getWeight());
            vo.setInventoryVolume(measure == null ? null : measure.getMaterialVolume());
            vo.setAvgTemperature(avgTemperatureByWarehouseId.get(summary.warehouseId()));
            vo.setMeasureUpdateTime(measure == null ? null : measure.getRemoteUpdateTime());
            out.add(vo);
        }
        return out;
    }

    @Override
    public WarehouseOverviewSummaryVO overviewSummary() {
        List<WarehouseSummary> summaries = resolveBoundWarehouseSummaries(null);
        var vo = new WarehouseOverviewSummaryVO();
        if (ListUtils.isEmpty(summaries)) {
            vo.setTotalInventoryWeight(BigDecimal.ZERO);
            vo.setAverageCapacityUsageRate(BigDecimal.ZERO);
            vo.setDeviceOnlineRate(BigDecimal.ZERO);
            return vo;
        }

        Set<Long> warehouseIds = summaries.stream().map(WarehouseSummary::warehouseId).collect(Collectors.toSet());
        Map<Long, WarehouseMeasureHistory> measureByWarehouseId =
                warehouseMeasureHistoryService.latestByWarehouseIds(warehouseIds);
        vo.setTotalInventoryWeight(sumWeight(measureByWarehouseId.values()));
        vo.setAverageCapacityUsageRate(defaultZero(average(measureByWarehouseId.values().stream()
                .map(WarehouseMeasureHistory::getUsageRate)
                .toList())));
        vo.setDeviceOnlineRate(loadBoundDeviceOnlineRate(warehouseIds));
        return vo;
    }

    @Override
    public List<WarehouseThreeDimensionalMeasureHistoryVO> measureHistory(WarehouseThreeDimensionalPeriodParam param) {
        PeriodRange range = validatePeriod(param);
        List<WarehouseSummary> summaries = resolveBoundWarehouseSummaries(null);
        if (ListUtils.isEmpty(summaries)) {
            return List.of();
        }
        Set<Long> warehouseIds = summaries.stream().map(WarehouseSummary::warehouseId).collect(Collectors.toSet());
        Map<Long, String> nameByWarehouseId = summaries.stream()
                .collect(Collectors.toMap(WarehouseSummary::warehouseId, WarehouseSummary::name, (a, b) -> a));
        List<WarehouseMeasureHistory> rows = warehouseMeasureHistoryService.list(
                Wrappers.<WarehouseMeasureHistory>lambdaQuery()
                        .in(WarehouseMeasureHistory::getWarehouseId, warehouseIds)
                        .ge(WarehouseMeasureHistory::getAcquisitionDate, range.begin())
                        .le(WarehouseMeasureHistory::getAcquisitionDate, range.end())
                        .orderByAsc(WarehouseMeasureHistory::getAcquisitionDate));
        if (ListUtils.isEmpty(rows)) {
            return List.of();
        }
        List<WarehouseThreeDimensionalMeasureHistoryVO> out = new ArrayList<>();
        for (WarehouseMeasureHistory row : rows) {
            var vo = new WarehouseThreeDimensionalMeasureHistoryVO();
            vo.setWarehouseId(row.getWarehouseId());
            vo.setWarehouseName(nameByWarehouseId.get(row.getWarehouseId()));
            vo.setAcquisitionDate(row.getAcquisitionDate());
            vo.setMaterialVolume(row.getMaterialVolume());
            vo.setRemainingVolume(row.getRemainingVolume());
            vo.setTotalVolume(row.getTotalVolume());
            vo.setWeight(row.getWeight());
            vo.setMaterialHeight(row.getMaterialHeight());
            vo.setRemainingHeight(row.getRemainingHeight());
            vo.setTotalHeight(row.getTotalHeight());
            vo.setUsageRate(row.getUsageRate());
            vo.setRemoteUpdateTime(row.getRemoteUpdateTime());
            out.add(vo);
        }
        return out;
    }

    @Override
    public List<WarehouseThreeDimensionalEnvironmentHistoryVO> environmentHistory(WarehouseThreeDimensionalPeriodParam param) {
        PeriodRange range = validatePeriod(param);
        int stepMinutes = resolveStepMinutes(param);
        String aggType = resolveAggType(param);

        List<WarehouseSummary> summaries = resolveBoundWarehouseSummaries(null);
        if (ListUtils.isEmpty(summaries)) {
            return List.of();
        }
        Set<Long> warehouseIds = summaries.stream().map(WarehouseSummary::warehouseId).collect(Collectors.toSet());
        EnvironmentDeviceGroups groups = resolveEnvironmentDeviceGroups(warehouseIds);
        LiangcangLocationCardConfigDTO cfg = locationCardConfigProvider.resolve();
        Map<Long, Map<DeviceTimeKey, EnvironmentAccumulator>> accByWarehouse = new HashMap<>();

        Map<String, EnvironmentRole> temperatureRoles = new LinkedHashMap<>();
        addRole(temperatureRoles, cfg.getTemperatureRealtimePropCode(), EnvironmentRole.TEMPERATURE);
        addRole(temperatureRoles, cfg.getHumidityRealtimePropCode(), EnvironmentRole.HUMIDITY);
        collectHistory(accByWarehouse, groups.temperatureDevicesByWarehouse(), temperatureRoles, range, stepMinutes, aggType);

        Map<String, EnvironmentRole> gasRoles = new LinkedHashMap<>();
        addRole(gasRoles, cfg.getGasCoPpmPropCode(), EnvironmentRole.CO);
        addRole(gasRoles, cfg.getGasCo2PpmPropCode(), EnvironmentRole.CO2);
        addRole(gasRoles, cfg.getGasNoPpmPropCode(), EnvironmentRole.NO);
        addRole(gasRoles, cfg.getGasO2PercentVolPropCode(), EnvironmentRole.O2);
        addRole(gasRoles, cfg.getGasPh3PpmPropCode(), EnvironmentRole.PH3);
        addRole(gasRoles, cfg.getGasSo2f2PpmPropCode(), EnvironmentRole.SO2F2);
        collectHistory(accByWarehouse, groups.gasDevicesByWarehouse(), gasRoles, range, stepMinutes, aggType);

        List<WarehouseThreeDimensionalEnvironmentHistoryVO> out = new ArrayList<>();
        for (WarehouseSummary summary : summaries) {
            Map<DeviceTimeKey, EnvironmentAccumulator> byTime =
                    accByWarehouse.getOrDefault(summary.warehouseId(), Map.of());
            List<WarehouseThreeDimensionalEnvironmentHistoryVO.Item> items = byTime.entrySet().stream()
                    .sorted(Comparator.comparing((Map.Entry<DeviceTimeKey, EnvironmentAccumulator> entry) -> entry.getKey().time())
                                      .thenComparing(entry -> entry.getKey().deviceId()))
                    .map(entry -> toEnvironmentItem(entry.getKey(), entry.getValue()))
                    .toList();
            var vo = new WarehouseThreeDimensionalEnvironmentHistoryVO();
            vo.setWarehouseId(summary.warehouseId());
            vo.setWarehouseName(summary.name());
            vo.setItems(items);
            out.add(vo);
        }
        return out;
    }

    private int resolveStepMinutes(WarehouseThreeDimensionalPeriodParam param) {
        int stepMinutes = param.getStepMinutes() == null ? DEFAULT_STEP_MINUTES : param.getStepMinutes();
        Assert.isParameterError(stepMinutes <= 0, MsgBinding.HISTORY_STEP_MINUTES_INVALID);
        return stepMinutes;
    }

    private String resolveAggType(WarehouseThreeDimensionalPeriodParam param) {
        if (StringUtils.isNotBlank(param.getAggType())) {
            return param.getAggType().trim().toLowerCase();
        }
        return param.getStepMinutes() == null ? DEFAULT_AGG_TYPE : DEFAULT_STEP_AGG_TYPE;
    }

    private PeriodRange validatePeriod(WarehouseThreeDimensionalPeriodParam param) {
        Assert.isParameterError(param == null, MsgBinding.STAT_PERIOD_REQUIRED);
        Period period = param.getPeriod();
        Assert.isParameterError(period == null, MsgBinding.STAT_PERIOD_REQUIRED);
        Assert.isParameterError(period.getBegin() == null, MsgBinding.STAT_PERIOD_REQUIRED);
        Assert.isParameterError(period.getEnd() == null, MsgBinding.STAT_PERIOD_REQUIRED);
        Assert.isParameterError(period.getBegin().isAfter(period.getEnd()), MsgBinding.PERIOD_BEGIN_AFTER_END);
        return new PeriodRange(period.getBegin(), period.getEnd());
    }

    private List<WarehouseSummary> resolveBoundWarehouseSummaries(Long warehouseId) {
        List<RadarWarehouseBinding> bindings = radarWarehouseBindingService.lambdaQuery()
                .select(RadarWarehouseBinding::getWarehouseId)
                .orderByDesc(RadarWarehouseBinding::getWarehouseId)
                .list();
        if (ListUtils.isEmpty(bindings)) {
            return List.of();
        }
        List<Long> orderedIds = new ArrayList<>();
        Set<Long> seen = new HashSet<>();
        for (RadarWarehouseBinding binding : bindings) {
            Long id = binding == null ? null : binding.getWarehouseId();
            if (id == null || (warehouseId != null && !Objects.equals(warehouseId, id))) {
                continue;
            }
            if (seen.add(id)) {
                orderedIds.add(id);
            }
        }
        if (ListUtils.isEmpty(orderedIds)) {
            return List.of();
        }
        Map<Long, LiangcangWarehousePageItemVO> remoteById =
                warehousePageApiClient.fetchPageItemsByIds(new HashSet<>(orderedIds));
        List<WarehouseSummary> out = new ArrayList<>();
        for (Long id : orderedIds) {
            LiangcangWarehousePageItemVO item = remoteById.get(id);
            out.add(new WarehouseSummary(id,
                                         item == null ? null : item.getName(),
                                         item == null ? null : item.getCategoryName(),
                                         item == null ? null : item.getQualityGrade()));
        }
        return out;
    }

    private Map<Long, BigDecimal> loadCurrentAverageTemperatures(Set<Long> warehouseIds) {
        if (SetUtils.isEmpty(warehouseIds)) {
            return Map.of();
        }
        LiangcangLocationCardConfigDTO cfg = locationCardConfigProvider.resolve();
        Map<Long, BigDecimal> out = loadCurrentWarehouseLevelTemperatures(warehouseIds, cfg);
        Set<Long> missingWarehouseIds = warehouseIds.stream()
                .filter(id -> out.get(id) == null)
                .collect(Collectors.toSet());
        if (SetUtils.isEmpty(missingWarehouseIds)) {
            return out;
        }
        Map<Long, List<Long>> tempDevices = resolveEnvironmentDeviceGroups(missingWarehouseIds).temperatureDevicesByWarehouse();
        out.putAll(loadCurrentSensorAverageTemperatures(tempDevices, cfg.getTemperatureRealtimePropCode()));
        return out;
    }

    private BigDecimal loadBoundDeviceOnlineRate(Set<Long> warehouseIds) {
        if (SetUtils.isEmpty(warehouseIds)) {
            return BigDecimal.ZERO;
        }
        List<WarehouseDeviceBinding> bindings = warehouseDeviceBindingService.list(
                Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                        .select(WarehouseDeviceBinding::getDeviceId)
                        .in(WarehouseDeviceBinding::getWarehouseId, warehouseIds));
        if (ListUtils.isEmpty(bindings)) {
            return BigDecimal.ZERO;
        }
        Set<Long> deviceIds = bindings.stream()
                .map(WarehouseDeviceBinding::getDeviceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (SetUtils.isEmpty(deviceIds)) {
            return BigDecimal.ZERO;
        }
        Map<Long, DeviceBaseDetailsDTO> detailById = loadDeviceDetailsById(deviceIds);
        long onlineCount = detailById.values().stream()
                .filter(Objects::nonNull)
                .map(DeviceBaseDetailsDTO::getDeviceState)
                .filter(Boolean.TRUE::equals)
                .count();
        return BigDecimal.valueOf(onlineCount)
                .divide(BigDecimal.valueOf(deviceIds.size()), AVERAGE_SCALE, RoundingMode.HALF_UP);
    }

    private Map<Long, BigDecimal> loadCurrentWarehouseLevelTemperatures(Set<Long> warehouseIds, LiangcangLocationCardConfigDTO cfg) {
        if (StringUtils.isBlank(cfg.getWarehouseAvgTemperaturePropCode())) {
            return new HashMap<>();
        }
        Map<Long, BigDecimal> out = new HashMap<>();
        String propCode = cfg.getWarehouseAvgTemperaturePropCode().trim();
        for (Long warehouseId : warehouseIds) {
            DeviceBaseDetailsDTO device = deviceCenterStdClient.getDetailsByThirdPartyId(String.valueOf(warehouseId));
            if (device == null || device.getId() == null) {
                continue;
            }
            var param = new DevicePropParam();
            param.setDeviceId(device.getId());
            param.setPropertyCodes(propCode);
            List<KeyValueWithTsDTO> values = iotDevicePropClient.deviceProp(param);
            BigDecimal value = findPropValue(values, propCode);
            if (value != null) {
                out.put(warehouseId, value);
            }
        }
        return out;
    }

    private Map<Long, BigDecimal> loadCurrentSensorAverageTemperatures(Map<Long, List<Long>> devicesByWarehouse, String propCode) {
        if (MapUtils.isEmpty(devicesByWarehouse) || StringUtils.isBlank(propCode)) {
            return Map.of();
        }
        String code = propCode.trim();
        String spec = buildSpec(devicesByWarehouse.values().stream().flatMap(List::stream).toList(), List.of(code));
        if (StringUtils.isBlank(spec)) {
            return Map.of();
        }
        Map<String, List<KeyValueWithTsDTO>> byDevice = iotDevicePropClient.multiProp(newMultiDeviceParam(spec));
        if (MapUtils.isEmpty(byDevice)) {
            return Map.of();
        }
        Map<Long, BigDecimal> out = new HashMap<>();
        for (Map.Entry<Long, List<Long>> entry : devicesByWarehouse.entrySet()) {
            var values = new ArrayList<BigDecimal>();
            for (Long deviceId : entry.getValue()) {
                BigDecimal value = findPropValue(byDevice.get(String.valueOf(deviceId)), code);
                if (value != null) {
                    values.add(value);
                }
            }
            BigDecimal avg = average(values);
            if (avg != null) {
                out.put(entry.getKey(), avg);
            }
        }
        return out;
    }

    private EnvironmentDeviceGroups resolveEnvironmentDeviceGroups(Set<Long> warehouseIds) {
        if (SetUtils.isEmpty(warehouseIds)) {
            return new EnvironmentDeviceGroups(Map.of(), Map.of());
        }
        List<WarehouseDeviceBinding> bindings = warehouseDeviceBindingService.list(
                Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                        .select(WarehouseDeviceBinding::getWarehouseId, WarehouseDeviceBinding::getDeviceId)
                        .in(WarehouseDeviceBinding::getWarehouseId, warehouseIds));
        if (ListUtils.isEmpty(bindings)) {
            return new EnvironmentDeviceGroups(Map.of(), Map.of());
        }
        Set<Long> deviceIds = bindings.stream()
                .map(WarehouseDeviceBinding::getDeviceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DeviceBaseDetailsDTO> detailById = loadDeviceDetailsById(deviceIds);
        LiangcangLocationCardConfigDTO cfg = locationCardConfigProvider.resolve();
        Map<Long, List<Long>> temperatureDevices = new HashMap<>();
        Map<Long, List<Long>> gasDevices = new HashMap<>();
        for (WarehouseDeviceBinding binding : bindings) {
            if (binding == null || binding.getWarehouseId() == null || binding.getDeviceId() == null) {
                continue;
            }
            DeviceBaseDetailsDTO detail = detailById.get(binding.getDeviceId());
            if (deviceMatches(detail, cfg.getTemperatureProductCodes())) {
                temperatureDevices.computeIfAbsent(binding.getWarehouseId(), key -> new ArrayList<>()).add(binding.getDeviceId());
            } else if (deviceMatches(detail, cfg.getGasProductCodes())) {
                gasDevices.computeIfAbsent(binding.getWarehouseId(), key -> new ArrayList<>()).add(binding.getDeviceId());
            }
        }
        return new EnvironmentDeviceGroups(temperatureDevices, gasDevices);
    }

    private Map<Long, DeviceBaseDetailsDTO> loadDeviceDetailsById(Set<Long> deviceIds) {
        if (SetUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        var param = new DevicePostByIdSetParam();
        param.setDeviceIdSet(deviceIds);
        List<DeviceBaseDetailsDTO> rows = deviceCenterStdClient.postDetailsListByDeviceId(param);
        return ListUtils.isEmpty(rows) ? Map.of() : rows.stream()
                .filter(row -> row != null && row.getId() != null)
                .collect(Collectors.toMap(DeviceBaseDetailsDTO::getId, row -> row, (a, b) -> b));
    }

    private void collectHistory(Map<Long, Map<DeviceTimeKey, EnvironmentAccumulator>> accByWarehouse,
                                Map<Long, List<Long>> devicesByWarehouse,
                                Map<String, EnvironmentRole> roleByPropCode,
                                PeriodRange range,
                                int stepMinutes,
                                String aggType) {
        if (MapUtils.isEmpty(devicesByWarehouse) || MapUtils.isEmpty(roleByPropCode)) {
            return;
        }
        Map<Long, Long> warehouseByDevice = new HashMap<>();
        List<Long> deviceIds = new ArrayList<>();
        for (Map.Entry<Long, List<Long>> entry : devicesByWarehouse.entrySet()) {
            for (Long deviceId : entry.getValue()) {
                if (deviceId != null) {
                    warehouseByDevice.put(deviceId, entry.getKey());
                    deviceIds.add(deviceId);
                }
            }
        }
        String spec = buildSpec(deviceIds, new ArrayList<>(roleByPropCode.keySet()));
        if (StringUtils.isBlank(spec)) {
            return;
        }
        var query = new MultiHistorySampleParam();
        query.setDevIdAndPropCodes(spec);
        query.setStartTs(DateUtils.toTimestamp(range.begin()));
        query.setEndTs(DateUtils.toTimestamp(range.end()));
        query.setAggType(aggType);
        query.setStep(stepMinutes);
        query.setMaxRecords(Math.max(deviceIds.size() * roleByPropCode.size() * 200, 1000));
        Map<String, DeviceTSDTO> history = iotDevicePropClient.multiHistorySampleProp(query);
        if (MapUtils.isEmpty(history)) {
            return;
        }
        for (Map.Entry<String, DeviceTSDTO> entry : history.entrySet()) {
            Long deviceId = toLong(entry.getKey());
            Long warehouseId = deviceId == null ? null : warehouseByDevice.get(deviceId);
            if (warehouseId == null || entry.getValue() == null || ListUtils.isEmpty(entry.getValue().getPropertyTsBoList())) {
                continue;
            }
            for (PropertyTsDTO property : entry.getValue().getPropertyTsBoList()) {
                EnvironmentRole role = property == null ? null : roleByPropCode.get(trim(property.getPropertyCode()));
                if (role == null || ListUtils.isEmpty(property.getValueTsBoList())) {
                    continue;
                }
                for (KeyValueWithTsDTO value : property.getValueTsBoList()) {
                    BigDecimal bd = value == null ? null : toBigDecimal(value.getValue());
                    LocalDateTime time = value == null || value.getTs() == null ? null : toLocalDateTime(value.getTs());
                    if (bd == null || time == null) {
                        continue;
                    }
                    accByWarehouse
                            .computeIfAbsent(warehouseId, key -> new HashMap<>())
                            .computeIfAbsent(new DeviceTimeKey(deviceId, time), key -> new EnvironmentAccumulator())
                            .add(role, bd);
                }
            }
        }
    }

    private WarehouseThreeDimensionalEnvironmentHistoryVO.Item toEnvironmentItem(DeviceTimeKey key, EnvironmentAccumulator acc) {
        var item = new WarehouseThreeDimensionalEnvironmentHistoryVO.Item();
        item.setDeviceId(key.deviceId());
        item.setTime(TIME_FORMATTER.format(key.time()));
        item.setTemperature(acc.value(EnvironmentRole.TEMPERATURE));
        item.setHumidity(acc.value(EnvironmentRole.HUMIDITY));
        item.setCoPpm(acc.value(EnvironmentRole.CO));
        item.setCo2Ppm(acc.value(EnvironmentRole.CO2));
        item.setNoPpm(acc.value(EnvironmentRole.NO));
        item.setO2PercentVol(acc.value(EnvironmentRole.O2));
        item.setPhosphinePpm(acc.value(EnvironmentRole.PH3));
        item.setSulfurylFluoridePpm(acc.value(EnvironmentRole.SO2F2));
        return item;
    }

    private static void addRole(Map<String, EnvironmentRole> out, String propCode, EnvironmentRole role) {
        if (StringUtils.isNotBlank(propCode)) {
            out.put(propCode.trim(), role);
        }
    }

    private static MultiDevicePropParam newMultiDeviceParam(String spec) {
        var param = new MultiDevicePropParam();
        param.setDevIdAndPropCodes(spec);
        return param;
    }

    private static String buildSpec(List<Long> deviceIds, List<String> propCodes) {
        if (ListUtils.isEmpty(deviceIds) || ListUtils.isEmpty(propCodes)) {
            return "";
        }
        return deviceIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .flatMap(deviceId -> propCodes.stream()
                        .filter(StringUtils::isNotBlank)
                        .map(propCode -> deviceId + "." + propCode.trim()))
                .collect(Collectors.joining(","));
    }

    private static BigDecimal findPropValue(List<KeyValueWithTsDTO> values, String propCode) {
        if (ListUtils.isEmpty(values) || StringUtils.isBlank(propCode)) {
            return null;
        }
        String code = propCode.trim();
        for (KeyValueWithTsDTO value : values) {
            if (value != null && value.getKey() != null && code.equals(value.getKey().trim())) {
                return toBigDecimal(value.getValue());
            }
        }
        return null;
    }

    private static boolean deviceMatches(DeviceBaseDetailsDTO detail, List<String> codes) {
        if (detail == null || ListUtils.isEmpty(codes)) {
            return false;
        }
        for (String code : codes) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            if (detail.getDeviceIotProdId() != null && code.trim().equals(String.valueOf(detail.getDeviceIotProdId()))) {
                return true;
            }
        }
        return false;
    }

    private static BigDecimal average(List<BigDecimal> values) {
        if (ListUtils.isEmpty(values)) {
            return null;
        }
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (BigDecimal value : values) {
            if (value != null) {
                sum = sum.add(value);
                count++;
            }
        }
        return count == 0 ? null : sum.divide(BigDecimal.valueOf(count), AVERAGE_SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal sumWeight(Iterable<WarehouseMeasureHistory> rows) {
        BigDecimal sum = BigDecimal.ZERO;
        if (rows == null) {
            return sum;
        }
        for (WarehouseMeasureHistory row : rows) {
            if (row != null && row.getWeight() != null) {
                sum = sum.add(row.getWeight());
            }
        }
        return sum;
    }

    private static BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private static BigDecimal toBigDecimal(Object value) {
        switch (value) {
            case null -> {
                return null;
            }
            case BigDecimal bigDecimal -> {
                return bigDecimal;
            }
            case Number number -> {
                return BigDecimal.valueOf(number.doubleValue());
            }
            default -> {
            }
        }
        String s = value.toString().trim();
        if (s.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Long toLong(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    private static LocalDateTime toLocalDateTime(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    private record PeriodRange(LocalDateTime begin, LocalDateTime end) {
    }

    private record WarehouseSummary(Long warehouseId, String name, String categoryName, String qualityGrade) {
    }

    private record DeviceTimeKey(Long deviceId, LocalDateTime time) {
    }

    private record EnvironmentDeviceGroups(Map<Long, List<Long>> temperatureDevicesByWarehouse,
                                           Map<Long, List<Long>> gasDevicesByWarehouse) {
    }

    private enum EnvironmentRole {
        TEMPERATURE, HUMIDITY, CO, CO2, NO, O2, PH3, SO2F2
    }

    private static class EnvironmentAccumulator {
        private final Map<EnvironmentRole, BigDecimal> values = new HashMap<>();

        void add(EnvironmentRole role, BigDecimal value) {
            values.put(role, value);
        }

        BigDecimal value(EnvironmentRole role) {
            return values.get(role);
        }
    }
}
