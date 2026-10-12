package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.LiangcangLocationCardConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.TenantDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseCablePoint;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureAggType;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureTrendScope;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureReportParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureTrendStatisticsParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseCablePointService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendStatisticsService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureReportPointVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureTrendConfigVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureTrendStatisticsVO;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.PropertyTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiHistorySampleParam;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 仓库粮温趋势统计。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseTemperatureTrendStatisticsServiceImpl implements WarehouseTemperatureTrendStatisticsService {

    private static final int ACTUAL_DAYS = 5;
    private static final int DEFAULT_FORECAST_DAYS = 7;
    private static final Set<Integer> SUPPORTED_FORECAST_DAYS = Set.of(7, 15);
    private static final BigDecimal ZERO_TEMPERATURE = BigDecimal.ZERO;
    private static final String STATUS_NORMAL = "NORMAL";
    private static final String STATUS_HIGH = "HIGH";
    private static final String STATUS_LOW = "LOW";

    private final WarehouseTemperatureTrendConfigService configService;
    private final WarehouseCablePointService cablePointService;
    private final WarehouseDeviceBindingService warehouseDeviceBindingService;
    private final LiangcangLocationCardConfigProvider configProvider;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final IotDevicePropClient iotDevicePropClient;
    private final DataDictionaryClient dataDictionaryClient;

    @Override
    public Long specialLayoutWarehouseId() {
        TemperatureReportLayoutConfig config = loadTemperatureReportLayoutConfig();
        return config == null ? null : config.getWarehouseId();
    }

    @Override
    public WarehouseTemperatureTrendStatisticsVO statistics(WarehouseTemperatureTrendStatisticsParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Long warehouseId = param.getWarehouseId();
        Assert.isParameterError(warehouseId == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        WarehouseTemperatureTrendScope scope = param.getScope() == null ? WarehouseTemperatureTrendScope.ALL : param.getScope();
        Integer forecastDays = param.getForecastDays() == null ? DEFAULT_FORECAST_DAYS : param.getForecastDays();
        Assert.isParameterError(!(SUPPORTED_FORECAST_DAYS.contains(forecastDays)), MsgBinding.TEMP_TREND_FORECAST_DAYS_UNSUPPORTED);
        LocalDateTime endTime = param.getEndTime() == null ? LocalDateTime.now() : param.getEndTime();

        WarehouseTemperatureTrendConfigVO config = configService.detail(warehouseId);
        String temperaturePropCode = resolveTemperaturePropCode();
        Map<WarehouseTemperatureTrendScope, WarehouseTemperatureTrendConfigVO.Layer> layerByScope = buildLayerByScope(config);
        Map<WarehouseTemperatureTrendScope, List<Long>> cableIdsByScope = buildCableIdsByScope(layerByScope);
        Map<Long, List<Long>> deviceIdsByCableId = loadDeviceIdsByCableId(warehouseId, cableIdsByScope.values().stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        Map<WarehouseTemperatureTrendScope, List<Long>> deviceIdsByScope = buildDeviceIdsByScope(cableIdsByScope, deviceIdsByCableId);
        Map<Long, BigDecimal> realtimeTemperatures = loadRealtimeTemperatures(deviceIdsByScope.values().stream()
                                                                                      .flatMap(List::stream)
                                                                                      .filter(Objects::nonNull)
                                                                                      .distinct()
                                                                                      .toList(), temperaturePropCode);

        var out = new WarehouseTemperatureTrendStatisticsVO();
        out.setUpdateTime(endTime);
        out.setCards(buildCards(config, layerByScope, deviceIdsByScope, realtimeTemperatures));
        out.setChart(buildChart(config, layerByScope, scope, deviceIdsByScope.getOrDefault(scope, List.of()),
                                temperaturePropCode, endTime, forecastDays));
        return out;
    }

    @Override
    public List<List<WarehouseTemperatureReportPointVO>> temperatureReport(WarehouseTemperatureReportParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        TemperatureReportLayoutConfig layoutConfig = loadTemperatureReportLayoutConfig();
        if (matchesTemperatureReportLayoutWarehouse(param.getWarehouseId(), layoutConfig)) {
            return specialTemperatureReport(param.getWarehouseId(), layoutConfig);
        }
        String temperaturePropCode = resolveTemperaturePropCode();
        LiangcangLocationCardConfigDTO cfg = configProvider.resolve();
        List<DeviceBaseDetailsDTO> temperatureDevices = loadBoundTemperatureDevices(param.getWarehouseId(), cfg);
        if (ListUtils.isEmpty(temperatureDevices)) {
            return List.of();
        }

        List<Long> deviceIds = temperatureDevices.stream()
                .map(DeviceBaseDetailsDTO::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, BigDecimal> temperatureByDeviceId = loadRealtimeTemperatures(deviceIds, temperaturePropCode);

        Map<String, WarehouseTemperatureReportPointVO> pointByCableAndPoint = new HashMap<>();
        for (DeviceBaseDetailsDTO device : temperatureDevices.stream()
                .filter(device -> device != null && device.getId() != null)
                .sorted(Comparator.comparing(DeviceBaseDetailsDTO::getId))
                .toList()) {
            CablePointName cablePointName = parseCablePointName(device.getDeviceName());
            if (cablePointName == null) {
                continue;
            }
            String key = cablePointName.cableNo() + "\u0000" + cablePointName.pointNo();
            if (pointByCableAndPoint.containsKey(key)) {
                continue;
            }
            BigDecimal temperature = temperatureByDeviceId.get(device.getId());
            var point = new WarehouseTemperatureReportPointVO();
            point.setCableNo(cablePointName.cableNo());
            point.setPointNo(cablePointName.pointNo());
            point.setDeviceId(device.getId());
            point.setDeviceName(device.getDeviceName());
            point.setTemperature(temperature);
            pointByCableAndPoint.put(key, point);
        }
        return groupReportPoints(pointByCableAndPoint.values().stream().toList());
    }

    private List<List<WarehouseTemperatureReportPointVO>> specialTemperatureReport(
            Long warehouseId,
            TemperatureReportLayoutConfig layoutConfig
                                                                                   ) {
        String temperaturePropCode = resolveTemperaturePropCode();
        LiangcangLocationCardConfigDTO cfg = configProvider.resolve();
        List<DeviceBaseDetailsDTO> temperatureDevices = loadBoundTemperatureDevices(warehouseId, cfg);
        Map<Long, BigDecimal> temperatureByDeviceId = loadRealtimeTemperatures(temperatureDevices.stream()
                                                                                         .map(DeviceBaseDetailsDTO::getId)
                                                                                         .filter(Objects::nonNull)
                                                                                         .distinct()
                                                                                         .toList(), temperaturePropCode);

        Map<String, WarehouseTemperatureReportPointVO> pointByDeviceName = new HashMap<>();
        for (DeviceBaseDetailsDTO device : temperatureDevices.stream()
                .filter(device -> device != null && device.getId() != null)
                .sorted(Comparator.comparing(DeviceBaseDetailsDTO::getId))
                .toList()) {
            String matchName = normalizeDeviceNameForLayout(device.getDeviceName());
            if (StringUtils.isBlank(matchName) || pointByDeviceName.containsKey(matchName)) {
                continue;
            }
            BigDecimal temperature = temperatureByDeviceId.get(device.getId());
            var point = new WarehouseTemperatureReportPointVO();
            point.setDeviceId(device.getId());
            point.setDeviceName(device.getDeviceName());
            point.setTemperature(temperature);
            pointByDeviceName.put(matchName, point);
        }

        List<List<List<WarehouseTemperatureReportPointVO>>> out = new ArrayList<>();
        List<List<List<String>>> layout = layoutConfig.getLayout();
        for (int layerIndex = 0; layerIndex < layout.size(); layerIndex++) {
            List<List<String>> layer = layout.get(layerIndex) == null ? List.of() : layout.get(layerIndex);
            List<List<WarehouseTemperatureReportPointVO>> outLayer = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < layer.size(); rowIndex++) {
                List<String> row = layer.get(rowIndex);
                List<WarehouseTemperatureReportPointVO> outRow = new ArrayList<>();
                List<String> actualRow = row == null ? List.of() : row;
                for (int columnIndex = 0; columnIndex < actualRow.size(); columnIndex++) {
                    String configuredDeviceName = actualRow.get(columnIndex);
                    String matchName = normalizeConfiguredName(configuredDeviceName);
                    WarehouseTemperatureReportPointVO point = StringUtils.isBlank(matchName) ? null : pointByDeviceName.get(matchName);
                    outRow.add(point == null ? emptyReportPoint() : copyReportPoint(point));
                }
                outLayer.add(outRow);
            }
            out.add(outLayer);
        }
        return flattenLayoutReport(out);
    }

    private static List<List<WarehouseTemperatureReportPointVO>> flattenLayoutReport(
            List<List<List<WarehouseTemperatureReportPointVO>>> layoutReportList
                                                                                  ) {
        if (ListUtils.isEmpty(layoutReportList)) {
            return List.of();
        }
        List<List<WarehouseTemperatureReportPointVO>> out = new ArrayList<>();
        for (List<List<WarehouseTemperatureReportPointVO>> layer : layoutReportList) {
            if (layer == null) {
                continue;
            }
            for (List<WarehouseTemperatureReportPointVO> row : layer) {
                if (row == null) {
                    continue;
                }
                out.add(row);
            }
        }
        return out;
    }

    private String resolveTemperaturePropCode() {
        LiangcangLocationCardConfigDTO cfg = configProvider.resolve();
        String propCode = cfg == null ? null : cfg.getTemperatureRealtimePropCode();
        Assert.isParameterError(!(StringUtils.isNotBlank(propCode)), MsgBinding.TEMP_TREND_TEMPERATURE_PROP_CODE_REQUIRED);
        return propCode.trim();
    }

    private List<DeviceBaseDetailsDTO> loadBoundTemperatureDevices(Long warehouseId, LiangcangLocationCardConfigDTO cfg) {
        if (cfg == null || ListUtils.isEmpty(cfg.getTemperatureProductCodes())) {
            return List.of();
        }
        List<WarehouseDeviceBinding> bindings = warehouseDeviceBindingService.list(
                Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                        .select(WarehouseDeviceBinding::getDeviceId)
                        .eq(WarehouseDeviceBinding::getWarehouseId, warehouseId));
        if (ListUtils.isEmpty(bindings)) {
            return List.of();
        }
        Set<Long> deviceIds = bindings.stream()
                .map(WarehouseDeviceBinding::getDeviceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (SetUtils.isEmpty(deviceIds)) {
            return List.of();
        }
        var postParam = new DevicePostByIdSetParam();
        postParam.setDeviceIdSet(deviceIds);
        List<DeviceBaseDetailsDTO> details = deviceCenterStdClient.postDetailsListByDeviceId(postParam);
        if (ListUtils.isEmpty(details)) {
            return List.of();
        }
        return details.stream()
                .filter(device -> deviceMatches(device, cfg.getTemperatureProductCodes()))
                .toList();
    }

    private Map<WarehouseTemperatureTrendScope, WarehouseTemperatureTrendConfigVO.Layer> buildLayerByScope(
            WarehouseTemperatureTrendConfigVO config
                                                                                                          ) {
        if (config == null || ListUtils.isEmpty(config.getLayers())) {
            return Map.of();
        }
        Map<WarehouseTemperatureTrendScope, WarehouseTemperatureTrendConfigVO.Layer> out = new LinkedHashMap<>();
        for (WarehouseTemperatureTrendConfigVO.Layer layer : config.getLayers()) {
            if (layer == null || StringUtils.isBlank(layer.getLayerCode())) {
                continue;
            }
            WarehouseTemperatureTrendScope scope = WarehouseTemperatureTrendScope.fromCode(layer.getLayerCode());
            if (scope != null && scope != WarehouseTemperatureTrendScope.ALL) {
                out.put(scope, layer);
            }
        }
        return out;
    }

    private Map<WarehouseTemperatureTrendScope, List<Long>> buildCableIdsByScope(
            Map<WarehouseTemperatureTrendScope, WarehouseTemperatureTrendConfigVO.Layer> layerByScope
                                                                                ) {
        Map<WarehouseTemperatureTrendScope, List<Long>> out = new LinkedHashMap<>();
        List<Long> allCableIds = new ArrayList<>();
        for (WarehouseTemperatureTrendScope scope : List.of(WarehouseTemperatureTrendScope.UPPER,
                                                            WarehouseTemperatureTrendScope.MIDDLE,
                                                            WarehouseTemperatureTrendScope.LOWER)) {
            WarehouseTemperatureTrendConfigVO.Layer layer = layerByScope.get(scope);
            List<Long> cableIds = layer == null || layer.getSelectedCableIds() == null ? List.of()
                                                                                       : layer.getSelectedCableIds().stream()
                                                                                         .filter(Objects::nonNull).distinct().toList();
            out.put(scope, cableIds);
            allCableIds.addAll(cableIds);
        }
        out.put(WarehouseTemperatureTrendScope.ALL, allCableIds.stream().distinct().toList());
        return out;
    }

    private Map<Long, List<Long>> loadDeviceIdsByCableId(Long warehouseId, Set<Long> cableIds) {
        if (SetUtils.isEmpty(cableIds)) {
            return Map.of();
        }
        List<WarehouseCablePoint> points = cablePointService.list(Wrappers.<WarehouseCablePoint>lambdaQuery()
                                                                          .select(WarehouseCablePoint::getCableId, WarehouseCablePoint::getDeviceId)
                                                                          .eq(WarehouseCablePoint::getWarehouseId, warehouseId)
                                                                          .in(WarehouseCablePoint::getCableId, cableIds)
                                                                          .isNotNull(WarehouseCablePoint::getDeviceId));
        if (ListUtils.isEmpty(points)) {
            return Map.of();
        }
        return points.stream()
                .filter(point -> point != null && point.getCableId() != null && point.getDeviceId() != null)
                .collect(Collectors.groupingBy(WarehouseCablePoint::getCableId,
                                               HashMap::new,
                                               Collectors.mapping(WarehouseCablePoint::getDeviceId,
                                                                  Collectors.collectingAndThen(Collectors.toList(),
                                                                                               list -> list.stream()
                                                                                                       .distinct()
                                                                                                       .toList()))));
    }

    private Map<WarehouseTemperatureTrendScope, List<Long>> buildDeviceIdsByScope(
            Map<WarehouseTemperatureTrendScope, List<Long>> cableIdsByScope,
            Map<Long, List<Long>> deviceIdsByCableId
                                                                                 ) {
        Map<WarehouseTemperatureTrendScope, List<Long>> out = new LinkedHashMap<>();
        for (Map.Entry<WarehouseTemperatureTrendScope, List<Long>> entry : cableIdsByScope.entrySet()) {
            List<Long> deviceIds = entry.getValue().stream()
                    .map(deviceIdsByCableId::get)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();
            out.put(entry.getKey(), deviceIds);
        }
        return out;
    }

    private Map<Long, BigDecimal> loadRealtimeTemperatures(List<Long> deviceIds, String propCode) {
        if (ListUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        String spec = buildDevIdAndPropCodes(deviceIds, propCode);
        if (StringUtils.isBlank(spec)) {
            return Map.of();
        }
        var param = new MultiDevicePropParam();
        param.setDevIdAndPropCodes(spec);
        Map<String, List<KeyValueWithTsDTO>> byDevice = iotDevicePropClient.multiProp(param);
        if (MapUtils.isEmpty(byDevice)) {
            return Map.of();
        }
        Map<Long, BigDecimal> out = new HashMap<>();
        for (Long deviceId : deviceIds) {
            List<KeyValueWithTsDTO> rows = byDevice.get(String.valueOf(deviceId));
            BigDecimal value = findPropValue(rows, propCode);
            if (value != null) {
                out.put(deviceId, value);
            }
        }
        return out;
    }

    private List<WarehouseTemperatureTrendStatisticsVO.Card> buildCards(
            WarehouseTemperatureTrendConfigVO config,
            Map<WarehouseTemperatureTrendScope, WarehouseTemperatureTrendConfigVO.Layer> layerByScope,
            Map<WarehouseTemperatureTrendScope, List<Long>> deviceIdsByScope,
            Map<Long, BigDecimal> realtimeTemperatures
                                                                       ) {
        List<WarehouseTemperatureTrendStatisticsVO.Card> cards = new ArrayList<>();
        cards.add(buildCard(WarehouseTemperatureTrendScope.ALL, "全仓温度", config.getWarehouseAggType(),
                            config.getHighAlarmThreshold(), config.getLowAlarmThreshold(),
                            deviceIdsByScope.getOrDefault(WarehouseTemperatureTrendScope.ALL, List.of()), realtimeTemperatures));
        cards.add(buildLayerCard(WarehouseTemperatureTrendScope.UPPER, "上层温度", layerByScope, deviceIdsByScope, realtimeTemperatures));
        cards.add(buildLayerCard(WarehouseTemperatureTrendScope.MIDDLE, "中层温度", layerByScope, deviceIdsByScope, realtimeTemperatures));
        cards.add(buildLayerCard(WarehouseTemperatureTrendScope.LOWER, "下层温度", layerByScope, deviceIdsByScope, realtimeTemperatures));
        return cards;
    }

    private WarehouseTemperatureTrendStatisticsVO.Card buildLayerCard(
            WarehouseTemperatureTrendScope scope,
            String name,
            Map<WarehouseTemperatureTrendScope, WarehouseTemperatureTrendConfigVO.Layer> layerByScope,
            Map<WarehouseTemperatureTrendScope, List<Long>> deviceIdsByScope,
            Map<Long, BigDecimal> realtimeTemperatures
                                                                     ) {
        WarehouseTemperatureTrendConfigVO.Layer layer = layerByScope.get(scope);
        WarehouseTemperatureAggType aggType = layer == null ? WarehouseTemperatureAggType.MAX : layer.getLayerAggType();
        BigDecimal highAlarmThreshold = layer == null ? null : layer.getHighAlarmThreshold();
        BigDecimal lowAlarmThreshold = layer == null ? null : layer.getLowAlarmThreshold();
        return buildCard(scope, name, aggType, highAlarmThreshold, lowAlarmThreshold,
                         deviceIdsByScope.getOrDefault(scope, List.of()), realtimeTemperatures);
    }

    private WarehouseTemperatureTrendStatisticsVO.Card buildCard(
            WarehouseTemperatureTrendScope scope,
            String name,
            WarehouseTemperatureAggType aggType,
            BigDecimal highAlarmThreshold,
            BigDecimal lowAlarmThreshold,
            List<Long> deviceIds,
            Map<Long, BigDecimal> realtimeTemperatures
                                                                ) {
        var card = new WarehouseTemperatureTrendStatisticsVO.Card();
        card.setScope(scope);
        card.setName(name);
        card.setHighAlarmThreshold(highAlarmThreshold);
        card.setLowAlarmThreshold(lowAlarmThreshold);
        BigDecimal temperature = aggregateTemperatures(deviceIds.stream()
                                                               .map(realtimeTemperatures::get)
                                                               .filter(Objects::nonNull)
                                                               .toList(), aggType);
        card.setTemperature(temperature);
        card.setStatus(resolveStatus(temperature, highAlarmThreshold, lowAlarmThreshold));
        return card;
    }

    private WarehouseTemperatureTrendStatisticsVO.Chart buildChart(
            WarehouseTemperatureTrendConfigVO config,
            Map<WarehouseTemperatureTrendScope, WarehouseTemperatureTrendConfigVO.Layer> layerByScope,
            WarehouseTemperatureTrendScope scope,
            List<Long> deviceIds,
            String temperaturePropCode,
            LocalDateTime endTime,
            Integer forecastDays
                                                                  ) {
        var chart = new WarehouseTemperatureTrendStatisticsVO.Chart();
        chart.setScope(scope);
        chart.setWarningLine(resolveWarningLine(config, layerByScope, scope));
        List<WarehouseTemperatureTrendStatisticsVO.Point> actualSeries = loadActualSeries(deviceIds, temperaturePropCode, endTime);
        chart.setActualSeries(actualSeries);
        chart.setForecastSeries(buildForecastSeries(endTime, forecastDays, actualSeries));
        return chart;
    }

    private BigDecimal resolveWarningLine(WarehouseTemperatureTrendConfigVO config,
                                          Map<WarehouseTemperatureTrendScope, WarehouseTemperatureTrendConfigVO.Layer> layerByScope,
                                          WarehouseTemperatureTrendScope scope
                                         ) {
        if (scope == WarehouseTemperatureTrendScope.ALL) {
            return config.getHighAlarmThreshold();
        }
        WarehouseTemperatureTrendConfigVO.Layer layer = layerByScope.get(scope);
        return layer == null ? null : layer.getHighAlarmThreshold();
    }

    private List<WarehouseTemperatureTrendStatisticsVO.Point> loadActualSeries(
            List<Long> deviceIds,
            String temperaturePropCode,
            LocalDateTime endTime
                                                                              ) {
        if (ListUtils.isEmpty(deviceIds)) {
            return List.of();
        }
        LocalDate startDate = endTime.toLocalDate().minusDays(ACTUAL_DAYS - 1L);
        LocalDateTime startTime = startDate.atStartOfDay();
        var query = new MultiHistorySampleParam();
        query.setDevIdAndPropCodes(buildDevIdAndPropCodes(deviceIds, temperaturePropCode));
        query.setStartTs(DateUtils.toTimestamp(startTime));
        query.setEndTs(DateUtils.toTimestamp(endTime));
        query.setAggType("raw");
        query.setStep(1);
        query.setMaxRecords(Math.max(deviceIds.size() * ACTUAL_DAYS * 288, 1000));
        Map<String, DeviceTSDTO> history = iotDevicePropClient.multiHistorySampleProp(query);
        if (MapUtils.isEmpty(history)) {
            return List.of();
        }
        Map<LocalDate, MaxTemperaturePoint> maxPointByDate = new HashMap<>();
        for (DeviceTSDTO deviceTs : history.values()) {
            collectDailyMax(deviceTs, temperaturePropCode, maxPointByDate);
        }
        List<WarehouseTemperatureTrendStatisticsVO.Point> out = new ArrayList<>();
        for (int i = 0; i < ACTUAL_DAYS; i++) {
            LocalDate date = startDate.plusDays(i);
            MaxTemperaturePoint maxPoint = maxPointByDate.get(date);
            if (maxPoint != null) {
                out.add(toPoint(date, maxPoint.time(), maxPoint.temperature()));
            }
        }
        return out;
    }

    private void collectDailyMax(DeviceTSDTO deviceTs, String temperaturePropCode,
                                 Map<LocalDate, MaxTemperaturePoint> maxPointByDate
                                ) {
        if (deviceTs == null || ListUtils.isEmpty(deviceTs.getPropertyTsBoList())) {
            return;
        }
        for (PropertyTsDTO propTs : deviceTs.getPropertyTsBoList()) {
            if (propTs == null || !temperaturePropCode.equals(trim(propTs.getPropertyCode()))
                || ListUtils.isEmpty(propTs.getValueTsBoList())) {
                continue;
            }
            for (KeyValueWithTsDTO valueTs : propTs.getValueTsBoList()) {
                BigDecimal temperature = valueTs == null ? null : toBigDecimal(valueTs.getValue());
                LocalDateTime time = valueTs == null || valueTs.getTs() == null ? null : toLocalDateTime(valueTs.getTs());
                if (temperature == null || time == null) {
                    continue;
                }
                LocalDate date = time.toLocalDate();
                MaxTemperaturePoint old = maxPointByDate.get(date);
                if (old == null || temperature.compareTo(old.temperature()) > 0) {
                    maxPointByDate.put(date, new MaxTemperaturePoint(time, temperature));
                }
            }
        }
    }

    private List<WarehouseTemperatureTrendStatisticsVO.Point> buildForecastSeries(
            LocalDateTime endTime,
            Integer forecastDays,
            List<WarehouseTemperatureTrendStatisticsVO.Point> actualSeries
                                                                                ) {
        List<WarehouseTemperatureTrendStatisticsVO.Point> out = new ArrayList<>();
        if (ListUtils.isNotEmpty(actualSeries)) {
            WarehouseTemperatureTrendStatisticsVO.Point lastActual = actualSeries.getLast();
            out.add(toPoint(lastActual.getDate(), lastActual.getTime(), lastActual.getTemperature()));
        }
        LocalDate startDate = endTime.toLocalDate().plusDays(1);
        for (int i = 0; i < forecastDays; i++) {
            LocalDate date = startDate.plusDays(i);
            out.add(toPoint(date, date.atTime(LocalTime.MIN), ZERO_TEMPERATURE));
        }
        return out;
    }

    private WarehouseTemperatureTrendStatisticsVO.Point toPoint(LocalDate date, LocalDateTime time, BigDecimal temperature) {
        var point = new WarehouseTemperatureTrendStatisticsVO.Point();
        point.setDate(date);
        point.setTime(time);
        point.setTemperature(temperature);
        return point;
    }

    private static BigDecimal aggregateTemperatures(List<BigDecimal> values, WarehouseTemperatureAggType aggType) {
        if (ListUtils.isEmpty(values)) {
            return null;
        }
        WarehouseTemperatureAggType actualAggType = aggType == null ? WarehouseTemperatureAggType.MAX : aggType;
        Comparator<BigDecimal> comparator = Comparator.naturalOrder();
        if (actualAggType == WarehouseTemperatureAggType.MIN) {
            return values.stream().min(comparator).orElse(null);
        }
        return values.stream().max(comparator).orElse(null);
    }

    private static String resolveStatus(BigDecimal temperature, BigDecimal highAlarmThreshold, BigDecimal lowAlarmThreshold) {
        if (temperature == null) {
            return STATUS_NORMAL;
        }
        if (highAlarmThreshold != null && temperature.compareTo(highAlarmThreshold) > 0) {
            return STATUS_HIGH;
        }
        if (lowAlarmThreshold != null && temperature.compareTo(lowAlarmThreshold) < 0) {
            return STATUS_LOW;
        }
        return STATUS_NORMAL;
    }

    private static CablePointName parseCablePointName(String deviceName) {
        if (StringUtils.isBlank(deviceName)) {
            return null;
        }
        String[] parts = deviceName.trim().split("-");
        if (parts.length < 3) {
            return null;
        }
        String cableNo = parts[parts.length - 2].trim();
        String pointNumber = extractLeftmostNumber(parts[parts.length - 1]);
        if (StringUtils.isBlank(cableNo) || StringUtils.isBlank(pointNumber)) {
            return null;
        }
        return new CablePointName(cableNo, "S" + pointNumber);
    }

    private TemperatureReportLayoutConfig loadTemperatureReportLayoutConfig() {
        Requester previousRequester = SessionContextHolder.getRequester(true);
        try {
            var query = new DataDictionaryaramByItemAndCodeParam();
            query.setItemCode(TenantDictionary.ITEM_THIRD_CONFIG);
            query.setCode(TenantDictionary.CODE_TEMPERATURE_REPORT_LAYOUT);
            var dict = dataDictionaryClient.getDataDictionaryByItemAndCode(query);
            if (dict == null || StringUtils.isBlank(dict.getVariable())) {
                return null;
            }
            TemperatureReportLayoutConfig config = JsonUtils.deserialize(dict.getVariable(), TemperatureReportLayoutConfig.class);
            if (config == null || config.getWarehouseId() == null || ListUtils.isEmpty(config.getLayout())) {
                log.warn("粮温报表特殊排版租户字典配置不完整 itemCode={} code={}",
                         TenantDictionary.ITEM_THIRD_CONFIG, TenantDictionary.CODE_TEMPERATURE_REPORT_LAYOUT);
                return null;
            }
            return config;
        } catch (RuntimeException e) {
            log.warn("粮温报表特殊排版租户字典 variable 解析失败 itemCode={} code={}",
                     TenantDictionary.ITEM_THIRD_CONFIG, TenantDictionary.CODE_TEMPERATURE_REPORT_LAYOUT, e);
            return null;
        } finally {
            restoreRequester(previousRequester);
        }
    }

    private static boolean matchesTemperatureReportLayoutWarehouse(Long warehouseId, TemperatureReportLayoutConfig layoutConfig) {
        return warehouseId != null && layoutConfig != null && warehouseId.equals(layoutConfig.getWarehouseId());
    }

    private static WarehouseTemperatureReportPointVO copyReportPoint(WarehouseTemperatureReportPointVO source) {
        var point = new WarehouseTemperatureReportPointVO();
        point.setDeviceId(source.getDeviceId());
        point.setDeviceName(source.getDeviceName());
        point.setTemperature(source.getTemperature());
        return point;
    }

    private static WarehouseTemperatureReportPointVO emptyReportPoint() {
        return new WarehouseTemperatureReportPointVO();
    }

    private static String normalizeDeviceNameForLayout(String text) {
        String normalized = normalizeConfiguredName(text);
        if (StringUtils.isBlank(normalized)) {
            return null;
        }
        int index = normalized.lastIndexOf('-');
        if (index < 0 || index == normalized.length() - 1) {
            return normalized;
        }
        return normalizeConfiguredName(normalized.substring(index + 1));
    }

    private static String normalizeConfiguredName(String text) {
        return StringUtils.isBlank(text) ? null : text.trim();
    }

    private static List<List<WarehouseTemperatureReportPointVO>> groupReportPoints(List<WarehouseTemperatureReportPointVO> points) {
        if (ListUtils.isEmpty(points)) {
            return List.of();
        }
        Map<String, List<WarehouseTemperatureReportPointVO>> byCable = points.stream()
                .collect(Collectors.groupingBy(WarehouseTemperatureReportPointVO::getCableNo,
                                               LinkedHashMap::new,
                                               Collectors.toList()));
        return byCable.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().stream()
                        .sorted(Comparator.comparing(WarehouseTemperatureReportPointVO::getPointNo,
                                                     WarehouseTemperatureTrendStatisticsServiceImpl::comparePointNo))
                        .toList())
                .toList();
    }

    private static int comparePointNo(String left, String right) {
        BigDecimal leftNumber = toBigDecimal(extractLeftmostNumber(left));
        BigDecimal rightNumber = toBigDecimal(extractLeftmostNumber(right));
        if (leftNumber != null && rightNumber != null) {
            int result = leftNumber.compareTo(rightNumber);
            if (result != 0) {
                return result;
            }
        }
        if (left == null && right == null) {
            return 0;
        }
        if (left == null) {
            return -1;
        }
        if (right == null) {
            return 1;
        }
        return left.compareTo(right);
    }

    private static String extractLeftmostNumber(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        StringBuilder out = new StringBuilder();
        boolean started = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isDigit(c)) {
                out.append(c);
                started = true;
            } else if (started) {
                break;
            }
        }
        return out.isEmpty() ? null : out.toString();
    }

    private static void restoreRequester(Requester requester) {
        if (requester == null) {
            SessionContextHolder.clearSessionContext();
        } else {
            SessionContextHolder.setRequester(requester);
        }
    }

    private static boolean deviceMatches(DeviceBaseDetailsDTO detail, List<String> productCodes) {
        if (detail == null || ListUtils.isEmpty(productCodes)) {
            return false;
        }
        for (String code : productCodes) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            if (detail.getDeviceIotProdId() != null && code.trim().equals(String.valueOf(detail.getDeviceIotProdId()))) {
                return true;
            }
        }
        return false;
    }

    private static BigDecimal findPropValue(List<KeyValueWithTsDTO> rows, String propCode) {
        if (ListUtils.isEmpty(rows)) {
            return null;
        }
        for (KeyValueWithTsDTO row : rows) {
            if (row != null && propCode.equals(trim(row.getKey())) && row.getValue() != null) {
                BigDecimal value = toBigDecimal(row.getValue());
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    private static String buildDevIdAndPropCodes(List<Long> deviceIds, String propCode) {
        return deviceIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(deviceId -> deviceId + "." + propCode)
                .collect(Collectors.joining(","));
    }

    private static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal bd) {
            return bd;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        String text = String.valueOf(value).trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static LocalDateTime toLocalDateTime(Long ts) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault());
    }

    private static String trim(String text) {
        return text == null ? null : text.trim();
    }

    private record MaxTemperaturePoint(LocalDateTime time, BigDecimal temperature) {
    }

    private record CablePointName(String cableNo, String pointNo) {
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TemperatureReportLayoutConfig {
        private Long warehouseId;
        private List<List<List<String>>> layout = new ArrayList<>();
    }
}
