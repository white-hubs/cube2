package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoWithDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.LiangcangLocationCardConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.Business;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseGoodsHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseMeasureHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.ExternalWarehouseGoodsHistoryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.ExternalWarehousePeriodParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.LiangcangWarehouseExternalService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseGoodsHistoryService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseMeasureHistoryService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseDailyTemperatureAverageGroupVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseDailyStorageValueVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseGoodsFlowTrendVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseGoodsHistoryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseMeasureInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseDeviceInfoVO;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.PropertyTsDTO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 粮仓外部对接查询实现。
 */
@Service
@RequiredArgsConstructor
public class LiangcangWarehouseExternalServiceImpl implements LiangcangWarehouseExternalService {

    private static final String WAREHOUSE_NOT_BOUND = "粮仓未绑定";
    private static final int AVERAGE_SCALE = 4;

    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final WarehouseDeviceBindingService warehouseDeviceBindingService;
    private final WarehouseGoodsHistoryService warehouseGoodsHistoryService;
    private final WarehousePageApiClient warehousePageApiClient;
    private final WarehouseMeasureHistoryService warehouseMeasureHistoryService;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final IotDevicePropClient iotDevicePropClient;
    private final LiangcangLocationCardConfigProvider locationCardConfigProvider;

    @Override
    public List<ExternalWarehouseDailyTemperatureAverageGroupVO> dailyTemperatureAverage(ExternalWarehousePeriodParam param) {
        PeriodRange range = validatePeriod(param);
        List<LocalDate> dates = listDates(range);
        List<WarehouseSummary> warehouses = resolveRequestedBoundWarehouseSummaries(param.getWarehouseId());
        if (ListUtils.isEmpty(dates) || ListUtils.isEmpty(warehouses)) {
            return List.of();
        }

        Map<Long, WarehouseSummary> summaryById = warehouses.stream()
                .collect(Collectors.toMap(WarehouseSummary::warehouseId, item -> item, (a, b) -> a, LinkedHashMap::new));
        Map<Long, List<Long>> warehouseAvgDevicesByWarehouse = resolveWarehouseAverageTemperatureDeviceIds(summaryById.keySet());
        Map<Long, List<Long>> tempDevicesByWarehouse = resolveTemperatureDeviceIds(summaryById.keySet());
        LiangcangLocationCardConfigDTO cfg = locationCardConfigProvider.resolve();
        String warehouseAvgTempPropCode = cfg.getWarehouseAvgTemperaturePropCode();
        String tempPropCode = cfg.getTemperatureRealtimePropCode();

        Map<Long, List<ExternalWarehouseDailyTemperatureAverageGroupVO.DailyTemperatureAverageItem>> itemsByWarehouse =
                new LinkedHashMap<>();
        for (WarehouseSummary warehouse : warehouses) {
            itemsByWarehouse.put(warehouse.warehouseId(), new ArrayList<>());
        }
        for (LocalDate date : dates) {
            Map<Long, BigDecimal> avgByWarehouse = StringUtils.isBlank(warehouseAvgTempPropCode) ? new HashMap<>()
                    : new HashMap<>(queryDailyTemperatureAverage(date, range, warehouseAvgDevicesByWarehouse,
                                                                 warehouseAvgTempPropCode.trim()));
            Set<Long> missingWarehouseIds = warehouses.stream()
                    .map(WarehouseSummary::warehouseId)
                    .filter(warehouseId -> avgByWarehouse.get(warehouseId) == null)
                    .collect(Collectors.toSet());
            if (StringUtils.isNotBlank(tempPropCode) && SetUtils.isNotEmpty(missingWarehouseIds)) {
                Map<Long, List<Long>> fallbackDevices = filterDeviceMap(tempDevicesByWarehouse, missingWarehouseIds);
                Map<Long, BigDecimal> fallbackAvgByWarehouse =
                        queryDailyTemperatureAverage(date, range, fallbackDevices, tempPropCode.trim());
                for (Long warehouseId : missingWarehouseIds) {
                    BigDecimal value = fallbackAvgByWarehouse.get(warehouseId);
                    if (value != null) {
                        avgByWarehouse.put(warehouseId, value);
                    }
                }
            }
            for (WarehouseSummary warehouse : warehouses) {
                var item = new ExternalWarehouseDailyTemperatureAverageGroupVO.DailyTemperatureAverageItem();
                item.setDate(date.toString());
                item.setAvgTemperature(avgByWarehouse.get(warehouse.warehouseId()));
                itemsByWarehouse.get(warehouse.warehouseId()).add(item);
            }
        }
        List<ExternalWarehouseDailyTemperatureAverageGroupVO> out = new ArrayList<>();
        for (WarehouseSummary warehouse : warehouses) {
            var group = new ExternalWarehouseDailyTemperatureAverageGroupVO();
            group.setWarehouseId(warehouse.warehouseId());
            group.setWarehouseName(warehouse.name());
            group.setItems(itemsByWarehouse.getOrDefault(warehouse.warehouseId(), List.of()));
            out.add(group);
        }
        return out;
    }

    @Override
    public List<ExternalWarehouseDailyStorageValueVO> dailyStorageValue(ExternalWarehousePeriodParam param) {
        PeriodRange range = validatePeriod(param);
        List<LocalDate> dates = listDates(range);
        List<WarehouseSummary> warehouses = resolveBoundWarehouseSummaries();
        if (ListUtils.isEmpty(dates) || ListUtils.isEmpty(warehouses)) {
            return List.of();
        }

        Set<Long> warehouseIds = warehouses.stream().map(WarehouseSummary::warehouseId).collect(Collectors.toSet());
        Map<DailyWarehouseKey, StorageValue> values = resolveDailyLatestStorageValues(
                queryGoodsHistory(warehouseIds, range.begin(), range.end()));
        List<ExternalWarehouseDailyStorageValueVO> out = new ArrayList<>();
        for (LocalDate date : dates) {
            for (WarehouseSummary warehouse : warehouses) {
                StorageValue value = values.get(new DailyWarehouseKey(date, warehouse.warehouseId()));
                var vo = new ExternalWarehouseDailyStorageValueVO();
                vo.setDate(date.toString());
                vo.setWarehouseId(warehouse.warehouseId());
                vo.setWarehouseName(warehouse.name());
                if (value != null) {
                    vo.setVolumeValue(value.volumeValue());
                    vo.setWeightValue(value.weightValue());
                }
                out.add(vo);
            }
        }
        return out;
    }

    @Override
    public ExternalWarehouseDetailVO warehouseDetail(Long warehouseId) {
        WarehouseSummary summary = resolveBoundWarehouseSummary(warehouseId);
        WarehouseMeasureHistory measureInfo = warehouseMeasureHistoryService.latestByWarehouseId(warehouseId)
                .orElse(null);

        var vo = new ExternalWarehouseDetailVO();
        vo.setWarehouseId(summary.warehouseId());
        vo.setGrainType(summary.categoryName());
        vo.setUsageRate(measureInfo == null ? null : measureInfo.getUsageRate());
        vo.setQualityGrade(summary.qualityGrade());
        if (measureInfo != null) {
            vo.setCurrentHeight(measureInfo.getMaterialHeight());
            vo.setTotalVolume(measureInfo.getTotalVolume());
            vo.setRealtimeStorageVolume(measureInfo.getMaterialVolume());
            vo.setTotalWeight(measureInfo.getWeight());
            vo.setRemainingAvailableVolume(measureInfo.getRemainingVolume());
            vo.setRemainingHeight(measureInfo.getRemainingHeight());
        }
        return vo;
    }

    @Override
    public List<ExternalWarehouseGoodsFlowTrendVO> goodsFlowTrend(Long warehouseId, ExternalWarehousePeriodParam param) {
        resolveBoundWarehouseSummary(warehouseId);
        PeriodRange range = validatePeriod(param);
        List<LocalDate> dates = listDates(range);
        if (ListUtils.isEmpty(dates)) {
            return List.of();
        }
        Map<LocalDate, FlowAccumulator> byDate = aggregateFlowTrend(
                queryGoodsHistory(Set.of(warehouseId), range.begin(), range.end()));
        List<ExternalWarehouseGoodsFlowTrendVO> out = new ArrayList<>();
        for (LocalDate date : dates) {
            FlowAccumulator acc = byDate.getOrDefault(date, new FlowAccumulator());
            var vo = new ExternalWarehouseGoodsFlowTrendVO();
            vo.setDate(date.toString());
            vo.setInboundVolume(acc.inboundVolume());
            vo.setOutboundVolume(acc.outboundVolume());
            vo.setInboundWeight(acc.inboundWeight());
            vo.setOutboundWeight(acc.outboundWeight());
            vo.setNetVolumeChange(acc.netVolumeChange());
            vo.setNetWeightChange(acc.netWeightChange());
            out.add(vo);
        }
        return out;
    }

    @Override
    public List<ExternalWarehouseGoodsHistoryVO> goodsHistory(ExternalWarehouseGoodsHistoryParam param) {
        Long warehouseId = param == null ? null : param.getWarehouseId();
        Period period = param == null ? null : param.getPeriod();
        if (period != null && period.getBegin() != null && period.getEnd() != null) {
            Assert.isParameterError(period.getBegin().isAfter(period.getEnd()), MsgBinding.PERIOD_BEGIN_AFTER_END);
        }
        var query = Wrappers.<WarehouseGoodsHistory>lambdaQuery();
        if (warehouseId != null) {
            query.eq(WarehouseGoodsHistory::getWarehouseId, warehouseId);
        }
        if (period != null && period.getBegin() != null) {
            query.ge(WarehouseGoodsHistory::getAcquisitionDate, period.getBegin());
        }
        if (period != null && period.getEnd() != null) {
            query.le(WarehouseGoodsHistory::getAcquisitionDate, period.getEnd());
        }
        query.orderByAsc(WarehouseGoodsHistory::getAcquisitionDate);
        return toGoodsHistoryVOList(warehouseGoodsHistoryService.list(query));
    }

    @Override
    public ExternalWarehouseMeasureInfoVO measureInfo(Long warehouseId) {
        resolveBoundWarehouseSummary(warehouseId);
        WarehouseMeasureHistory measureInfo = warehouseMeasureHistoryService.latestByWarehouseId(warehouseId)
                .orElse(null);
        if (measureInfo == null) {
            return null;
        }
        var vo = new ExternalWarehouseMeasureInfoVO();
        vo.setWarehouseId(warehouseId);
        vo.setMaterialVolume(measureInfo.getMaterialVolume());
        vo.setRemainingVolume(measureInfo.getRemainingVolume());
        vo.setTotalVolume(measureInfo.getTotalVolume());
        vo.setWeight(measureInfo.getWeight());
        vo.setMaterialHeight(measureInfo.getMaterialHeight());
        vo.setRemainingHeight(measureInfo.getRemainingHeight());
        vo.setTotalHeight(measureInfo.getTotalHeight());
        vo.setUsageRate(measureInfo.getUsageRate());
        vo.setUpdateTime(measureInfo.getRemoteUpdateTime());
        return vo;
    }

    @Override
    public List<WarehouseDeviceInfoVO> radarInfo(Long warehouseId) {
        WarehouseSummary summary = resolveBoundWarehouseSummary(warehouseId);
        if (ListUtils.isEmpty(summary.deviceList())) {
            return List.of();
        }
        return summary.deviceList().stream()
                .filter(Objects::nonNull)
                .filter(item -> Business.DEVICE_TYPE_RADAR.equals(trim(item.getDeviceType())))
                .toList();
    }

    private static List<ExternalWarehouseGoodsHistoryVO> toGoodsHistoryVOList(List<WarehouseGoodsHistory> rows) {
        if (ListUtils.isEmpty(rows)) {
            return List.of();
        }
        List<ExternalWarehouseGoodsHistoryVO> out = new ArrayList<>();
        for (WarehouseGoodsHistory row : rows) {
            if (row == null) {
                continue;
            }
            var vo = new ExternalWarehouseGoodsHistoryVO();
            vo.setWarehouseId(row.getWarehouseId());
            vo.setRadarDeviceId(row.getRadarDeviceId());
            vo.setAcquisitionDate(row.getAcquisitionDate());
            vo.setWeightValue(row.getWeightValue());
            vo.setVolumeValue(row.getVolumeValue());
            vo.setWeightDifference(row.getWeightDifference());
            vo.setVolumeDifference(row.getVolumeDifference());
            out.add(vo);
        }
        return out;
    }

    private PeriodRange validatePeriod(ExternalWarehousePeriodParam param) {
        Assert.isParameterError(param == null, MsgBinding.STAT_PERIOD_REQUIRED);
        Period period = param.getPeriod();
        Assert.isParameterError(period == null, MsgBinding.STAT_PERIOD_REQUIRED);
        Assert.isParameterError(period.getBegin() == null, MsgBinding.STAT_PERIOD_REQUIRED);
        Assert.isParameterError(period.getEnd() == null, MsgBinding.STAT_PERIOD_REQUIRED);
        Assert.isParameterError(!(!period.getBegin().isAfter(period.getEnd())), MsgBinding.PERIOD_BEGIN_AFTER_END);
        return new PeriodRange(period.getBegin(), period.getEnd());
    }

    private List<LocalDate> listDates(PeriodRange range) {
        LocalDate start = range.begin().toLocalDate();
        LocalDate end = range.end().toLocalDate();
        List<LocalDate> out = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            out.add(d);
        }
        return out;
    }

    private List<WarehouseSummary> resolveBoundWarehouseSummaries() {
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
            if (binding != null && binding.getWarehouseId() != null && seen.add(binding.getWarehouseId())) {
                orderedIds.add(binding.getWarehouseId());
            }
        }
        if (ListUtils.isEmpty(orderedIds)) {
            return List.of();
        }
        Map<Long, LiangcangWarehousePageItemVO> remoteById =
                warehousePageApiClient.fetchPageItemsByIds(new HashSet<>(orderedIds));
        List<WarehouseSummary> out = new ArrayList<>();
        for (Long warehouseId : orderedIds) {
            LiangcangWarehousePageItemVO item = remoteById.get(warehouseId);
            out.add(toSummary(warehouseId, item));
        }
        return out;
    }

    private List<WarehouseSummary> resolveRequestedBoundWarehouseSummaries(Long warehouseId) {
        if (warehouseId == null) {
            return resolveBoundWarehouseSummaries();
        }
        return List.of(resolveBoundWarehouseSummary(warehouseId));
    }

    private WarehouseSummary resolveBoundWarehouseSummary(Long warehouseId) {
        Assert.isParameterError(warehouseId == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        boolean bound = radarWarehouseBindingService.lambdaQuery()
                .eq(RadarWarehouseBinding::getWarehouseId, warehouseId)
                .exists();
        Assert.isParameterError(!(bound), WAREHOUSE_NOT_BOUND);
        var item = warehousePageApiClient.fetchPageItemById(warehouseId);
        Assert.isParameterError(!(item.isPresent()), MsgBinding.WAREHOUSE_INFO_NOT_FOUND);
        return toSummary(warehouseId, item.get());
    }

    private WarehouseSummary toSummary(Long warehouseId, LiangcangWarehousePageItemVO item) {
        if (item == null) {
            return new WarehouseSummary(warehouseId, null, null, null, null, null, null, List.of());
        }
        return new WarehouseSummary(warehouseId, item.getName(), item.getCategoryName(), item.getQualityGrade(),
                                    item.getWarehouseHeight(), item.getTotalVolume(), item.getUsageRate(),
                                    item.getDeviceList() == null ? List.of() : item.getDeviceList());
    }

    private Map<Long, List<Long>> resolveTemperatureDeviceIds(Set<Long> warehouseIds) {
        if (SetUtils.isEmpty(warehouseIds)) {
            return Map.of();
        }
        List<WarehouseDeviceBinding> bindings = warehouseDeviceBindingService.list(
                Wrappers.<WarehouseDeviceBinding>lambdaQuery()
                        .select(WarehouseDeviceBinding::getWarehouseId, WarehouseDeviceBinding::getDeviceId)
                        .in(WarehouseDeviceBinding::getWarehouseId, warehouseIds));
        if (ListUtils.isEmpty(bindings)) {
            return Map.of();
        }
        Set<Long> deviceIds = bindings.stream()
                .map(WarehouseDeviceBinding::getDeviceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (SetUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        LiangcangLocationCardConfigDTO cfg = locationCardConfigProvider.resolve();
        Map<Long, DeviceProductInfoDTO> productByDevice = loadProductByDeviceId(deviceIds);
        Map<Long, List<Long>> out = new HashMap<>();
        for (WarehouseDeviceBinding binding : bindings) {
            if (binding == null || binding.getWarehouseId() == null || binding.getDeviceId() == null) {
                continue;
            }
            if (!productMatches(productByDevice.get(binding.getDeviceId()), cfg.getTemperatureProductCodes())) {
                continue;
            }
            out.computeIfAbsent(binding.getWarehouseId(), key -> new ArrayList<>()).add(binding.getDeviceId());
        }
        return out;
    }

    private Map<Long, List<Long>> resolveWarehouseAverageTemperatureDeviceIds(Set<Long> warehouseIds) {
        if (SetUtils.isEmpty(warehouseIds)) {
            return Map.of();
        }
        Map<Long, List<Long>> out = new HashMap<>();
        for (Long warehouseId : warehouseIds) {
            if (warehouseId == null) {
                continue;
            }
            DeviceBaseDetailsDTO device = deviceCenterStdClient.getDetailsByThirdPartyId(String.valueOf(warehouseId));
            if (device != null && device.getId() != null) {
                out.put(warehouseId, List.of(device.getId()));
            }
        }
        return out;
    }

    private static Map<Long, List<Long>> filterDeviceMap(Map<Long, List<Long>> source, Set<Long> warehouseIds) {
        if (MapUtils.isEmpty(source) || SetUtils.isEmpty(warehouseIds)) {
            return Map.of();
        }
        Map<Long, List<Long>> out = new HashMap<>();
        for (Long warehouseId : warehouseIds) {
            List<Long> devices = source.get(warehouseId);
            if (ListUtils.isNotEmpty(devices)) {
                out.put(warehouseId, devices);
            }
        }
        return out;
    }

    private Map<Long, DeviceProductInfoDTO> loadProductByDeviceId(Set<Long> deviceIds) {
        var param = new DevicePostByIdSetParam();
        param.setDeviceIdSet(deviceIds);
        List<DeviceProductInfoWithDeviceDTO> rows = deviceCenterStdClient.postProductInfoByDeviceId(param);
        if (ListUtils.isEmpty(rows)) {
            return Map.of();
        }
        return rows.stream()
                .filter(row -> row != null && row.getDeviceId() != null)
                .collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId,
                                          DeviceProductInfoWithDeviceDTO::getProductInfo, (a, b) -> b));
    }

    private Map<Long, BigDecimal> queryDailyTemperatureAverage(LocalDate date, PeriodRange range,
                                                               Map<Long, List<Long>> devicesByWarehouse,
                                                               String tempPropCode
                                                              ) {
        if (MapUtils.isEmpty(devicesByWarehouse)) {
            return Map.of();
        }
        Map<Long, Long> deviceToWarehouse = new HashMap<>();
        List<Long> deviceIds = new ArrayList<>();
        for (Map.Entry<Long, List<Long>> entry : devicesByWarehouse.entrySet()) {
            if (ListUtils.isEmpty(entry.getValue())) {
                continue;
            }
            for (Long deviceId : entry.getValue()) {
                if (deviceId != null) {
                    deviceToWarehouse.put(deviceId, entry.getKey());
                    deviceIds.add(deviceId);
                }
            }
        }
        if (ListUtils.isEmpty(deviceIds)) {
            return Map.of();
        }
        LocalDateTime start = date.equals(range.begin().toLocalDate()) ? range.begin() : date.atStartOfDay();
        LocalDateTime end = date.equals(range.end().toLocalDate()) ? range.end() : date.atTime(LocalTime.MAX);

        var query = new MultiHistorySampleParam();
        query.setDevIdAndPropCodes(buildDevIdAndPropCodes(deviceIds, tempPropCode));
        query.setStartTs(DateUtils.toTimestamp(start));
        query.setEndTs(DateUtils.toTimestamp(end));
        query.setAggType("avg");
        query.setStep(resolveStepMinutes(start, end));
        query.setMaxRecords(Math.max(deviceIds.size(), 1));

        Map<String, DeviceTSDTO> history = iotDevicePropClient.multiHistorySampleProp(query);
        if (MapUtils.isEmpty(history)) {
            return Map.of();
        }
        Map<Long, List<BigDecimal>> valuesByWarehouse = new HashMap<>();
        for (Map.Entry<String, DeviceTSDTO> entry : history.entrySet()) {
            Long deviceId = toLong(entry.getKey());
            Long warehouseId = deviceId == null ? null : deviceToWarehouse.get(deviceId);
            if (warehouseId == null) {
                continue;
            }
            List<BigDecimal> values = extractPropertyValues(entry.getValue(), tempPropCode);
            if (ListUtils.isEmpty(values)) {
                continue;
            }
            valuesByWarehouse.computeIfAbsent(warehouseId, key -> new ArrayList<>()).addAll(values);
        }
        Map<Long, BigDecimal> out = new HashMap<>();
        for (Map.Entry<Long, List<BigDecimal>> entry : valuesByWarehouse.entrySet()) {
            out.put(entry.getKey(), average(entry.getValue(), AVERAGE_SCALE));
        }
        return out;
    }

    private List<WarehouseGoodsHistory> queryGoodsHistory(Set<Long> warehouseIds, LocalDateTime begin, LocalDateTime end) {
        if (SetUtils.isEmpty(warehouseIds)) {
            return List.of();
        }
        return warehouseGoodsHistoryService.list(Wrappers.<WarehouseGoodsHistory>lambdaQuery()
                                                         .in(WarehouseGoodsHistory::getWarehouseId, warehouseIds)
                                                         .ge(WarehouseGoodsHistory::getAcquisitionDate, begin)
                                                         .le(WarehouseGoodsHistory::getAcquisitionDate, end)
                                                         .orderByAsc(WarehouseGoodsHistory::getAcquisitionDate));
    }

    private Map<DailyWarehouseKey, StorageValue> resolveDailyLatestStorageValues(List<WarehouseGoodsHistory> rows) {
        Map<DailyWarehouseKey, WarehouseGoodsHistory> latestByDayWarehouse = new HashMap<>();
        if (ListUtils.isEmpty(rows)) {
            return Map.of();
        }
        for (WarehouseGoodsHistory row : rows) {
            if (row == null || row.getWarehouseId() == null || row.getAcquisitionDate() == null) {
                continue;
            }
            DailyWarehouseKey key = new DailyWarehouseKey(row.getAcquisitionDate().toLocalDate(), row.getWarehouseId());
            WarehouseGoodsHistory old = latestByDayWarehouse.get(key);
            if (old == null || old.getAcquisitionDate() == null || old.getAcquisitionDate().isBefore(row.getAcquisitionDate())) {
                latestByDayWarehouse.put(key, row);
            }
        }
        Map<DailyWarehouseKey, StorageValue> out = new HashMap<>();
        for (Map.Entry<DailyWarehouseKey, WarehouseGoodsHistory> entry : latestByDayWarehouse.entrySet()) {
            WarehouseGoodsHistory row = entry.getValue();
            out.put(entry.getKey(), new StorageValue(row.getVolumeValue(), row.getWeightValue()));
        }
        return out;
    }

    private Map<LocalDate, FlowAccumulator> aggregateFlowTrend(List<WarehouseGoodsHistory> rows) {
        Map<LocalDate, FlowAccumulator> out = new HashMap<>();
        if (ListUtils.isEmpty(rows)) {
            return out;
        }
        for (WarehouseGoodsHistory row : rows) {
            if (row == null || row.getAcquisitionDate() == null) {
                continue;
            }
            out.computeIfAbsent(row.getAcquisitionDate().toLocalDate(), key -> new FlowAccumulator())
                    .add(row.getVolumeDifference(), row.getWeightDifference());
        }
        return out;
    }

    private static boolean productMatches(DeviceProductInfoDTO product, List<String> codes) {
        if (product == null || ListUtils.isEmpty(codes)) {
            return false;
        }
        for (String code : codes) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            String want = code.trim();
            if (product.getId() != null && want.equals(String.valueOf(product.getId()))) {
                return true;
            }
            if (StringUtils.isNotBlank(product.getModelName()) && want.equals(product.getModelName().trim())) {
                return true;
            }
            if (product.getProductCategory() != null) {
                var category = product.getProductCategory();
                if (StringUtils.isNotBlank(category.getCode()) && want.equals(category.getCode().trim())) {
                    return true;
                }
                if (StringUtils.isNotBlank(category.getPathCode()) && want.equals(category.getPathCode().trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<BigDecimal> extractPropertyValues(DeviceTSDTO deviceTs, String propCode) {
        if (deviceTs == null || ListUtils.isEmpty(deviceTs.getPropertyTsBoList())) {
            return List.of();
        }
        List<BigDecimal> out = new ArrayList<>();
        for (PropertyTsDTO propTs : deviceTs.getPropertyTsBoList()) {
            if (propTs == null || !propCode.equals(trim(propTs.getPropertyCode()))
                || ListUtils.isEmpty(propTs.getValueTsBoList())) {
                continue;
            }
            for (KeyValueWithTsDTO value : propTs.getValueTsBoList()) {
                BigDecimal bd = value == null ? null : toBigDecimal(value.getValue());
                if (bd != null) {
                    out.add(bd);
                }
            }
        }
        return out;
    }

    private static String buildDevIdAndPropCodes(List<Long> deviceIds, String propCode) {
        return deviceIds.stream()
                .filter(Objects::nonNull)
                .map(deviceId -> deviceId + "." + propCode)
                .collect(Collectors.joining(","));
    }

    private static int resolveStepMinutes(LocalDateTime start, LocalDateTime end) {
        long minutes = ChronoUnit.MINUTES.between(start, end);
        if (minutes < 1) {
            return 1;
        }
        return minutes > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) minutes;
    }

    private static BigDecimal average(List<BigDecimal> values, int scale) {
        if (ListUtils.isEmpty(values)) {
            return null;
        }
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (BigDecimal value : values) {
            if (value == null) {
                continue;
            }
            sum = sum.add(value);
            count++;
        }
        if (count == 0) {
            return null;
        }
        return sum.divide(BigDecimal.valueOf(count), scale, RoundingMode.HALF_UP);
    }

    private static BigDecimal toBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
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

    private record PeriodRange(LocalDateTime begin, LocalDateTime end) {
    }

    private record DailyWarehouseKey(LocalDate date, Long warehouseId) {
    }

    private record WarehouseSummary(Long warehouseId, String name, String categoryName, String qualityGrade,
                                    Double warehouseHeight, Double totalVolume, Double usageRate,
                                    List<WarehouseDeviceInfoVO> deviceList) {
    }

    private record StorageValue(BigDecimal volumeValue, BigDecimal weightValue) {
    }

    private static class FlowAccumulator {
        private BigDecimal inboundVolume = BigDecimal.ZERO;
        private BigDecimal outboundVolume = BigDecimal.ZERO;
        private BigDecimal inboundWeight = BigDecimal.ZERO;
        private BigDecimal outboundWeight = BigDecimal.ZERO;
        private BigDecimal netVolumeChange = BigDecimal.ZERO;
        private BigDecimal netWeightChange = BigDecimal.ZERO;

        void add(BigDecimal volume, BigDecimal weight) {
            netVolumeChange = addNullable(netVolumeChange, volume);
            netWeightChange = addNullable(netWeightChange, weight);
            if (volume != null) {
                if (volume.signum() >= 0) {
                    inboundVolume = inboundVolume.add(volume);
                } else {
                    outboundVolume = outboundVolume.add(volume.abs());
                }
            }
            if (weight != null) {
                if (weight.signum() >= 0) {
                    inboundWeight = inboundWeight.add(weight);
                } else {
                    outboundWeight = outboundWeight.add(weight.abs());
                }
            }
        }

        BigDecimal inboundVolume() {
            return inboundVolume;
        }

        BigDecimal outboundVolume() {
            return outboundVolume;
        }

        BigDecimal inboundWeight() {
            return inboundWeight;
        }

        BigDecimal outboundWeight() {
            return outboundWeight;
        }

        BigDecimal netVolumeChange() {
            return netVolumeChange;
        }

        BigDecimal netWeightChange() {
            return netWeightChange;
        }

        private static BigDecimal addNullable(BigDecimal left, BigDecimal right) {
            return right == null ? left : left.add(right);
        }
    }
}
