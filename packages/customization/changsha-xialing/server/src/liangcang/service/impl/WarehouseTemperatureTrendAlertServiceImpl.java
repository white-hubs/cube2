package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.message2.client.SatMsgSiteSendClient;
import com.guhecloud.cube.base.message2.client.enumeration.SiteMsgTypeEnum;
import com.guhecloud.cube.base.message2.client.param.SatMsgSiteEventDataParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgSiteSendParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgSiteTargetModifyParam;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.LiangcangSystemDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseMeasureSyncConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.RadarWarehouseBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertRecord;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureDailyAvgMax;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureSample;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureAlertClearReason;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureAlertStatus;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertGlobalConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertSampleParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertTrendParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertWarehouseConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureReportParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarWarehouseBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureAlertConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureAlertGlobalConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureAlertRecordService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureDailyAvgMaxService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureSampleService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendAlertService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendStatisticsService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertConfigVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertGlobalConfigVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertNoticeVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertPopupVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertRecordExportVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertRecordVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertSampleDayVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertSampleExportVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertTrendVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertWarehouseConfigVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureReportPointVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 粮仓温度趋势预警总服务。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseTemperatureTrendAlertServiceImpl implements WarehouseTemperatureTrendAlertService {

    private static final int SCALE = 2;
    private static final int MAX_LOOKBACK_DAYS = 30;
    private static final String TEMPERATURE_ALERT_POPUP_EVENT = "LIANGCANG_TEMPERATURE_ALERT_POPUP";
    private static final String DEFAULT_POPUP_TERMINAL = "bimops-manage-web";

    private final WarehouseTemperatureAlertGlobalConfigService globalConfigService;
    private final WarehouseTemperatureAlertConfigService warehouseConfigService;
    private final WarehouseTemperatureSampleService sampleService;
    private final WarehouseTemperatureDailyAvgMaxService dailyAvgMaxService;
    private final WarehouseTemperatureAlertRecordService recordService;
    private final RadarWarehouseBindingService radarWarehouseBindingService;
    private final WarehousePageApiClient warehousePageApiClient;
    private final WarehouseTemperatureTrendStatisticsService warehouseTemperatureTrendStatisticsService;
    private final SysDictionaryClient sysDictionaryClient;
    private final UserClient userClient;
    private final SatMsgSiteSendClient satMsgSiteSendClient;

    @Override
    public Boolean saveGlobalConfig(WarehouseTemperatureAlertGlobalConfigSaveParam param) {
        return globalConfigService.saveConfig(param);
    }

    @Override
    public WarehouseTemperatureAlertGlobalConfigVO globalConfigDetail() {
        return globalConfigService.detail();
    }

    @Override
    public WarehouseTemperatureAlertWarehouseConfigVO warehouseConfigDetail(Long warehouseId) {
        return warehouseConfigService.detail(warehouseId);
    }

    @Override
    public Boolean saveWarehouseConfig(WarehouseTemperatureAlertWarehouseConfigSaveParam param) {
        return warehouseConfigService.saveConfig(param);
    }

    @Override
    public Boolean resetWarehouseConfig(Long warehouseId) {
        return warehouseConfigService.resetToGlobalDefault(warehouseId);
    }

    @Override
    public Pagination<WarehouseTemperatureAlertRecordVO> recordPage(WarehouseTemperatureAlertRecordPageParam param) {
        long pageNumber = param == null || param.getPageNumber() == null || param.getPageNumber() < 1 ? 1 : param.getPageNumber();
        long pageSize = param == null || param.getPageSize() == null || param.getPageSize() < 1 ? 10 : param.getPageSize();
        Page<WarehouseTemperatureAlertRecord> page = pageRecords(pageNumber, pageSize, param);
        if (ListUtils.isEmpty(page.getRecords())) {
            return new Pagination<>(page.getTotal(), page.getPages(), List.of());
        }
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(this::toRecordVO).toList());
    }

    @Override
    public List<WarehouseTemperatureAlertRecordVO> recordExport(WarehouseTemperatureAlertRecordPageParam param) {
        Page<WarehouseTemperatureAlertRecord> page = pageRecords(1, Integer.MAX_VALUE, param);
        if (ListUtils.isEmpty(page.getRecords())) {
            return List.of();
        }
        return page.getRecords().stream().map(this::toRecordVO).toList();
    }

    @Override
    public WarehouseTemperatureAlertTrendVO trend(WarehouseTemperatureAlertTrendParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Long warehouseId = param.getWarehouseId();
        WarehouseTemperatureAlertGlobalConfigVO effectiveConfig = warehouseConfigService.resolveEffectiveConfig(warehouseId);
        LocalDate endDate = param.getEndDate() == null ? LocalDate.now() : param.getEndDate();
        LocalDate startDate = param.getStartDate() == null ? endDate.minusDays(MAX_LOOKBACK_DAYS - 1L) : param.getStartDate();
        if (startDate.isAfter(endDate)) {
            startDate = endDate;
        }
        Map<Long, LiangcangWarehousePageItemVO> warehouseMap = warehousePageApiClient.fetchPageItemsByIds(Set.of(warehouseId));
        LiangcangWarehousePageItemVO warehouse = warehouseMap.get(warehouseId);
        List<WarehouseTemperatureDailyAvgMax> dailyRows = dailyAvgMaxService.listByWarehouseIdsAndDateRange(
                Set.of(warehouseId), startDate, endDate);
        Map<LocalDate, WarehouseTemperatureDailyAvgMax> dailyByDate = dailyRows.stream()
                .filter(row -> row != null && row.getStatDate() != null)
                .collect(Collectors.toMap(WarehouseTemperatureDailyAvgMax::getStatDate, row -> row, (a, b) -> b, LinkedHashMap::new));
        WarehouseTemperatureAlertNoticeVO notice = activeNotice(warehouseId);
        List<WarehouseTemperatureAlertTrendVO.Point> series = new ArrayList<>();
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            WarehouseTemperatureDailyAvgMax daily = dailyByDate.get(date);
            WarehouseTemperatureAlertTrendVO.Point point = new WarehouseTemperatureAlertTrendVO.Point();
            point.setDate(date);
            point.setTime(daily == null || daily.getCalculatedAt() == null ? date.atStartOfDay() : daily.getCalculatedAt());
            point.setTemperature(daily == null ? null : daily.getAvgMaxTemperature());
            point.setTriggered(notice != null && Boolean.TRUE.equals(notice.getAlerting())
                               && notice.getTriggeredAt() != null
                               && notice.getTriggeredAt().toLocalDate().equals(date));
            series.add(point);
            date = date.plusDays(1L);
        }

        WarehouseTemperatureAlertTrendVO vo = new WarehouseTemperatureAlertTrendVO();
        vo.setWarehouseId(warehouseId);
        vo.setWarehouseName(warehouse == null ? null : warehouse.getName());
        vo.setGrainType(warehouse == null ? null : warehouse.getCategoryName());
        vo.setWarningThreshold(effectiveConfig.getWarningThreshold());
        vo.setApproachMargin(effectiveConfig.getApproachMargin());
        vo.setWarningLine(resolveWarningLine(effectiveConfig));
        vo.setSeries(series);
        vo.setAlertNotice(notice);
        return vo;
    }

    @Override
    public List<WarehouseTemperatureAlertSampleDayVO> samples(WarehouseTemperatureAlertSampleParam param) {
        return buildSampleDays(param);
    }

    @Override
    public List<WarehouseTemperatureAlertSampleDayVO> sampleExport(WarehouseTemperatureAlertSampleParam param) {
        return buildSampleDays(param);
    }

    @Override
    public WarehouseTemperatureAlertNoticeVO activeNotice(Long warehouseId) {
        if (warehouseId == null) {
            return inactiveNotice();
        }
        List<WarehouseTemperatureAlertRecord> records = recordService.listActiveByWarehouseIds(List.of(warehouseId));
        if (ListUtils.isEmpty(records)) {
            return inactiveNotice();
        }
        return toNoticeVO(records.get(0));
    }

    @Override
    public void calculateAndDetect() {
        // 同一任务同时承担采样、日均落库和预警判断，避免采样与状态更新错位。
        Set<Long> warehouseIds = loadBoundWarehouseIds();
        if (SetUtils.isEmpty(warehouseIds)) {
            return;
        }
        Map<Long, TemperatureSampleSnapshot> temperatureByWarehouse = loadTemperatureSamplesFromReportCenter(warehouseIds);
        if (temperatureByWarehouse.isEmpty()) {
            return;
        }
        LocalDateTime sampleTime = floorToHalfHour(LocalDateTime.now());
        List<WarehouseTemperatureSample> sampleRows = new ArrayList<>();
        for (Map.Entry<Long, TemperatureSampleSnapshot> entry : temperatureByWarehouse.entrySet()) {
            TemperatureSampleSnapshot snapshot = entry.getValue();
            WarehouseTemperatureSample row = new WarehouseTemperatureSample();
            row.setId(IdWorker.getId());
            row.setWarehouseId(entry.getKey());
            row.setSampleTime(sampleTime);
            row.setMaxTemperature(scale(snapshot.maxTemperature()));
            row.setSampleCount(snapshot.sampleCount());
            sampleRows.add(row);
        }
        sampleService.saveOrUpdateByWarehouseAndSampleTime(sampleRows);
        if (ListUtils.isEmpty(sampleRows)) {
            return;
        }
        Set<Long> sampledWarehouseIds = sampleRows.stream()
                .map(WarehouseTemperatureSample::getWarehouseId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        LocalDate today = sampleTime.toLocalDate();
        updateDailyAverages(sampledWarehouseIds, today);
        detectAlerts(sampledWarehouseIds, today);
    }

    @Override
    public void markPreviousDayComplete() {
        LocalDate yesterday = LocalDate.now().minusDays(1L);
        dailyAvgMaxService.markComplete(yesterday, LocalDateTime.now());
    }

    private Page<WarehouseTemperatureAlertRecord> pageRecords(long pageNumber, long pageSize,
                                                              WarehouseTemperatureAlertRecordPageParam param
                                                             ) {
        var query = Wrappers.<WarehouseTemperatureAlertRecord>lambdaQuery()
                .orderByDesc(WarehouseTemperatureAlertRecord::getTriggeredAt);
        if (param != null) {
            if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
                query.between(WarehouseTemperatureAlertRecord::getTriggeredAt, param.getPeriod().getBegin(), param.getPeriod().getEnd());
            }
            if (ListUtils.isNotEmpty(param.getWarehouseIds())) {
                query.in(WarehouseTemperatureAlertRecord::getWarehouseId, param.getWarehouseIds());
            }
            if (ListUtils.isNotEmpty(param.getStatuses())) {
                query.in(WarehouseTemperatureAlertRecord::getStatus, param.getStatuses());
            }
        }
        return recordService.page(new Page<>(pageNumber, pageSize), query);
    }

    private List<WarehouseTemperatureAlertSampleDayVO> buildSampleDays(WarehouseTemperatureAlertSampleParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Long warehouseId = param.getWarehouseId();
        LocalDate endDate = param.getEndDate() == null ? LocalDate.now() : param.getEndDate();
        LocalDate startDate = param.getStartDate() == null ? endDate.minusDays(MAX_LOOKBACK_DAYS - 1L) : param.getStartDate();
        if (startDate.isAfter(endDate)) {
            startDate = endDate;
        }
        List<WarehouseTemperatureSample> sampleRows = sampleService.listByWarehouseIdsAndDateRange(List.of(warehouseId), startDate, endDate);
        List<WarehouseTemperatureDailyAvgMax> dailyRows = dailyAvgMaxService.listByWarehouseIdsAndDateRange(List.of(warehouseId), startDate, endDate);
        Map<LocalDate, List<WarehouseTemperatureSample>> sampleByDate = sampleRows.stream()
                .filter(row -> row != null && row.getSampleTime() != null)
                .collect(Collectors.groupingBy(row -> row.getSampleTime().toLocalDate(), LinkedHashMap::new, Collectors.toList()));
        Map<LocalDate, WarehouseTemperatureDailyAvgMax> dailyByDate = dailyRows.stream()
                .filter(row -> row != null && row.getStatDate() != null)
                .collect(Collectors.toMap(WarehouseTemperatureDailyAvgMax::getStatDate, row -> row, (a, b) -> b, LinkedHashMap::new));
        Map<Long, LiangcangWarehousePageItemVO> warehouseMap = warehousePageApiClient.fetchPageItemsByIds(Set.of(warehouseId));
        LiangcangWarehousePageItemVO warehouse = warehouseMap.get(warehouseId);
        List<WarehouseTemperatureAlertSampleDayVO> out = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            WarehouseTemperatureAlertSampleDayVO dayVO = new WarehouseTemperatureAlertSampleDayVO();
            dayVO.setDate(current);
            WarehouseTemperatureDailyAvgMax daily = dailyByDate.get(current);
            dayVO.setComplete(daily != null && Boolean.TRUE.equals(daily.getComplete()));
            dayVO.setSampleCount(daily == null ? 0 : daily.getSampleCount());
            dayVO.setAvgMaxTemperature(daily == null ? null : daily.getAvgMaxTemperature());
            List<WarehouseTemperatureAlertSampleDayVO.Item> items = new ArrayList<>();
            for (WarehouseTemperatureSample sample : sampleByDate.getOrDefault(current, List.of())) {
                WarehouseTemperatureAlertSampleDayVO.Item item = new WarehouseTemperatureAlertSampleDayVO.Item();
                item.setWarehouseName(warehouse == null ? null : warehouse.getName());
                item.setSampleTime(sample.getSampleTime());
                item.setMaxTemperature(sample.getMaxTemperature());
                items.add(item);
            }
            dayVO.setItems(items);
            out.add(dayVO);
            current = current.plusDays(1L);
        }
        return out;
    }

    private void updateDailyAverages(Set<Long> warehouseIds, LocalDate statDate) {
        if (SetUtils.isEmpty(warehouseIds) || statDate == null) {
            return;
        }
        List<WarehouseTemperatureSample> samples = sampleService.listByWarehouseIdsAndDateRange(warehouseIds, statDate, statDate);
        if (ListUtils.isEmpty(samples)) {
            return;
        }
        Map<Long, List<WarehouseTemperatureSample>> sampleByWarehouse = samples.stream()
                .filter(row -> row != null && row.getWarehouseId() != null)
                .collect(Collectors.groupingBy(WarehouseTemperatureSample::getWarehouseId, LinkedHashMap::new, Collectors.toList()));
        List<WarehouseTemperatureDailyAvgMax> rows = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Map.Entry<Long, List<WarehouseTemperatureSample>> entry : sampleByWarehouse.entrySet()) {
            BigDecimal avg = average(entry.getValue().stream().map(WarehouseTemperatureSample::getMaxTemperature).toList());
            if (avg == null) {
                continue;
            }
            WarehouseTemperatureDailyAvgMax row = new WarehouseTemperatureDailyAvgMax();
            row.setId(IdWorker.getId());
            row.setWarehouseId(entry.getKey());
            row.setStatDate(statDate);
            row.setAvgMaxTemperature(scale(avg));
            row.setSampleCount(entry.getValue().size());
            row.setComplete(Boolean.FALSE);
            row.setCalculatedAt(now);
            rows.add(row);
        }
        dailyAvgMaxService.saveOrUpdateByWarehouseAndStatDate(rows);
    }

    private void detectAlerts(Set<Long> warehouseIds, LocalDate today) {
        if (SetUtils.isEmpty(warehouseIds) || today == null) {
            return;
        }
        // 单仓覆盖优先，全局默认兜底。
        WarehouseTemperatureAlertGlobalConfigVO globalConfig = globalConfigService.resolveEffectiveConfig();
        List<WarehouseTemperatureAlertConfigVO> configRows = warehouseConfigService.list(Wrappers
                                                                                                 .<com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertConfig>lambdaQuery()
                                                                                                 .in(com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertConfig::getWarehouseId, warehouseIds))
                .stream().map(this::toConfigVO).toList();
        Map<Long, WarehouseTemperatureAlertConfigVO> configByWarehouse = configRows.stream()
                .filter(row -> row != null && row.getWarehouseId() != null)
                .collect(Collectors.toMap(WarehouseTemperatureAlertConfigVO::getWarehouseId, row -> row, (a, b) -> b, LinkedHashMap::new));
        Map<Long, List<WarehouseTemperatureDailyAvgMax>> dailyByWarehouse = dailyAvgMaxService.mapByWarehouseIdsAndDateRange(
                warehouseIds, today.minusDays(MAX_LOOKBACK_DAYS - 1L), today);
        List<WarehouseTemperatureAlertRecord> activeRecords = recordService.listActiveByWarehouseIds(warehouseIds);
        Map<Long, WarehouseTemperatureAlertRecord> activeByWarehouse = activeRecords.stream()
                .filter(row -> row != null && row.getWarehouseId() != null)
                .collect(Collectors.toMap(WarehouseTemperatureAlertRecord::getWarehouseId, row -> row, (a, b) -> a, LinkedHashMap::new));
        Map<Long, LiangcangWarehousePageItemVO> warehouseMap = warehousePageApiClient.fetchPageItemsByIds(warehouseIds);
        List<WarehouseTemperatureAlertRecord> inserts = new ArrayList<>();
        List<WarehouseTemperatureAlertRecord> updates = new ArrayList<>();

        for (Long warehouseId : warehouseIds) {
            WarehouseTemperatureAlertConfigVO config = configByWarehouse.getOrDefault(warehouseId, null);
            WarehouseTemperatureAlertGlobalConfigVO effective = config == null ? globalConfig : mergeConfig(globalConfig, config);
            List<WarehouseTemperatureDailyAvgMax> rows = dailyByWarehouse.getOrDefault(warehouseId, List.of()).stream()
                    .sorted(Comparator.comparing(WarehouseTemperatureDailyAvgMax::getStatDate))
                    .toList();
            TrendResult result = detectTrend(rows, effective);
            WarehouseTemperatureAlertRecord active = activeByWarehouse.get(warehouseId);
            if (active != null) {
                // 已有 ACTIVE 记录时，触发不重复创建，只处理消除。
                if (Boolean.TRUE.equals(result.triggered())) {
                    continue;
                }
                active.setStatus(WarehouseTemperatureAlertStatus.CLEARED.name());
                active.setClearedAt(LocalDateTime.now());
                active.setClearedReason(resolveClearReason(result).name());
                updates.add(active);
                activeByWarehouse.remove(warehouseId);
                continue;
            }
            // 没有 ACTIVE 记录时，只有满足触发条件才创建新的预警事件。
            if (!Boolean.TRUE.equals(result.triggered())) {
                continue;
            }
            WarehouseTemperatureAlertRecord record = new WarehouseTemperatureAlertRecord();
            record.setId(IdWorker.getId());
            record.setWarehouseId(warehouseId);
            LiangcangWarehousePageItemVO warehouse = warehouseMap.get(warehouseId);
            record.setWarehouseName(warehouse == null ? null : warehouse.getName());
            record.setGrainType(warehouse == null ? null : warehouse.getCategoryName());
            record.setTriggeredAt(LocalDateTime.now());
            record.setCurrentAvgTemperature(result.currentAvgTemperature());
            record.setWarningThreshold(effective.getWarningThreshold());
            record.setApproachMargin(effective.getApproachMargin());
            record.setDistanceToThreshold(result.distanceToThreshold());
            record.setDailyIncrease(result.dailyIncrease());
            record.setConsecutiveDays(effective.getConsecutiveDays());
            record.setStatus(WarehouseTemperatureAlertStatus.ACTIVE.name());
            record.setPopupEnabled(resolvePopupEnabled(config));
            record.setPopupPushed(Boolean.FALSE);
            inserts.add(record);
        }
        if (ListUtils.isNotEmpty(inserts)) {
            recordService.saveBatch(inserts);
            List<WarehouseTemperatureAlertRecord> popupPushedUpdates = pushPopupMessages(inserts, warehouseMap);
            if (ListUtils.isNotEmpty(popupPushedUpdates)) {
                updates.addAll(popupPushedUpdates);
            }
        }
        if (ListUtils.isNotEmpty(updates)) {
            recordService.updateBatchById(updates);
        }
    }

    private List<WarehouseTemperatureAlertRecord> pushPopupMessages(List<WarehouseTemperatureAlertRecord> records,
                                                                    Map<Long, LiangcangWarehousePageItemVO> warehouseMap
                                                                   ) {
        if (ListUtils.isEmpty(records)) {
            return List.of();
        }
        List<WarehouseTemperatureAlertRecord> pushedRecords = new ArrayList<>();
        for (WarehouseTemperatureAlertRecord record : records) {
            if (record == null || !Boolean.TRUE.equals(record.getPopupEnabled())) {
                continue;
            }
            if (sendPopupMessage(record, warehouseMap.get(record.getWarehouseId()))) {
                record.setPopupPushed(Boolean.TRUE);
                pushedRecords.add(record);
            } else {
                log.warn("粮仓温度趋势预警弹窗消息发送失败, alertRecordId={}, warehouseId={}",
                         record.getId(), record.getWarehouseId());
            }
        }
        return pushedRecords;
    }

    private Boolean sendPopupMessage(WarehouseTemperatureAlertRecord record, LiangcangWarehousePageItemVO warehouse) {
        WarehouseTemperatureAlertPopupVO popup = toPopupVO(record, warehouse);
        List<SatMsgSiteTargetModifyParam> targets = buildPopupTargets(loadTemperatureAlertPopupRoleIds());
        if (ListUtils.isEmpty(targets)) {
            log.warn("粮仓温度趋势预警弹窗跳过，未解析到角色接收人 alertRecordId={} warehouseId={}",
                     record.getId(), record.getWarehouseId());
            return Boolean.FALSE;
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("msgType", TEMPERATURE_ALERT_POPUP_EVENT);
        data.put("msgContent", popup.getMessage());
        data.put("businessPrincipal", JsonUtils.serialize(popup));
        return satMsgSiteSendClient.sendSite(SatMsgSiteSendParam.builder()
                                                                .msgType(SiteMsgTypeEnum.NOTICE_MESSAGE.name())
                                                                .eventDataParam(new SatMsgSiteEventDataParam(
                                                                        TEMPERATURE_ALERT_POPUP_EVENT,
                                                                        data))
                                                                .targetModifyParams(targets)
                                                                .build());
    }

    private Set<Long> loadTemperatureAlertPopupRoleIds() {
        var dict = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(LiangcangSystemDictionary.ITEM_THIRD_CONFIG,
                                                                         LiangcangSystemDictionary.CODE_WAREHOUSE_TEMPERATURE_ALERT_SYNC);
        if (dict == null || StringUtils.isBlank(dict.getVariable())) {
            log.warn("粮仓温度趋势预警弹窗未配置系统字典 {}:{}",
                     LiangcangSystemDictionary.ITEM_THIRD_CONFIG,
                     LiangcangSystemDictionary.CODE_WAREHOUSE_TEMPERATURE_ALERT_SYNC);
            return Set.of();
        }
        WarehouseMeasureSyncConfigDTO config;
        try {
            config = JsonUtils.deserialize(dict.getVariable(), WarehouseMeasureSyncConfigDTO.class);
        } catch (RuntimeException e) {
            log.warn("粮仓温度趋势预警弹窗系统字典 variable 解析失败: {}", dict.getVariable(), e);
            return Set.of();
        }
        if (config == null) {
            return Set.of();
        }
        Set<Long> roleIds = parseRoleIds(config.getRoleIds());
        roleIds.addAll(parseRoleIds(config.getRoleId()));
        return roleIds;
    }

    private Set<Long> parseRoleIds(Object rawRoleIds) {
        if (rawRoleIds == null) {
            return new java.util.LinkedHashSet<>();
        }
        if (rawRoleIds instanceof Iterable<?> iterable) {
            Set<Long> out = new java.util.LinkedHashSet<>();
            for (Object item : iterable) {
                parseRoleId(item).ifPresent(out::add);
            }
            return out;
        }
        String text = String.valueOf(rawRoleIds);
        if (StringUtils.isBlank(text)) {
            return new java.util.LinkedHashSet<>();
        }
        Set<Long> out = new java.util.LinkedHashSet<>();
        for (String item : text.split(",")) {
            parseRoleId(item).ifPresent(out::add);
        }
        return out;
    }

    private java.util.Optional<Long> parseRoleId(Object rawRoleId) {
        if (rawRoleId == null) {
            return java.util.Optional.empty();
        }
        if (rawRoleId instanceof Number number) {
            return java.util.Optional.of(number.longValue());
        }
        String text = String.valueOf(rawRoleId).trim();
        if (StringUtils.isBlank(text)) {
            return java.util.Optional.empty();
        }
        try {
            return java.util.Optional.of(Long.parseLong(text));
        } catch (NumberFormatException e) {
            log.warn("粮仓温度趋势预警弹窗角色 id 配置非法: {}", rawRoleId);
            return java.util.Optional.empty();
        }
    }

    private List<SatMsgSiteTargetModifyParam> buildPopupTargets(Set<Long> roleIds) {
        if (SetUtils.isEmpty(roleIds)) {
            return List.of();
        }
        List<UserDTO> users = userClient.listUsersByRoles(roleIds);
        if (ListUtils.isEmpty(users)) {
            log.warn("粮仓温度趋势预警弹窗角色下未查询到用户 roleIds={}", roleIds);
            return List.of();
        }
        Map<Long, SatMsgSiteTargetModifyParam> targetByUserId = new LinkedHashMap<>();
        for (UserDTO user : users) {
            if (user == null || user.getId() == null || targetByUserId.containsKey(user.getId())) {
                continue;
            }
            SatMsgSiteTargetModifyParam target = new SatMsgSiteTargetModifyParam();
            target.setUserId(user.getId());
            target.setUserName(user.getName());
            target.setTerminal(DEFAULT_POPUP_TERMINAL);
            targetByUserId.put(user.getId(), target);
        }
        return new ArrayList<>(targetByUserId.values());
    }

    private TrendResult detectTrend(List<WarehouseTemperatureDailyAvgMax> rows, WarehouseTemperatureAlertGlobalConfigVO config) {
        if (ListUtils.isEmpty(rows) || config == null || config.getConsecutiveDays() == null || config.getConsecutiveDays() < 2) {
            return TrendResult.no();
        }
        int n = config.getConsecutiveDays();
        Map<LocalDate, WarehouseTemperatureDailyAvgMax> byDate = rows.stream()
                .filter(row -> row != null && row.getStatDate() != null && row.getAvgMaxTemperature() != null)
                .collect(Collectors.toMap(WarehouseTemperatureDailyAvgMax::getStatDate, row -> row, (a, b) -> b, LinkedHashMap::new));
        LocalDate currentDate = rows.get(rows.size() - 1).getStatDate();
        List<BigDecimal> temperatures = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            LocalDate date = currentDate.minusDays(i);
            WarehouseTemperatureDailyAvgMax row = byDate.get(date);
            if (row == null || row.getAvgMaxTemperature() == null) {
                return TrendResult.no();
            }
            temperatures.add(row.getAvgMaxTemperature());
        }
        // 连续上升、日均增幅、逼近或超过阈值三条件同时满足才算触发。
        boolean strictlyIncreasing = true;
        for (int i = 1; i < temperatures.size(); i++) {
            if (temperatures.get(i).compareTo(temperatures.get(i - 1)) <= 0) {
                strictlyIncreasing = false;
                break;
            }
        }
        BigDecimal first = temperatures.get(0);
        BigDecimal current = temperatures.get(temperatures.size() - 1);
        BigDecimal dailyIncrease = temperatures.size() > 1
                                   ? current.subtract(first)
                                     .divide(BigDecimal.valueOf(temperatures.size() - 1L), SCALE, RoundingMode.HALF_UP)
                                   : BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal distance = config.getWarningThreshold() == null ? null : config.getWarningThreshold().subtract(current);
        boolean withinOrOverThresholdRange = distance != null &&
                                             distance.compareTo(config.getApproachMargin() == null
                                                                ? BigDecimal.ZERO
                                                                : config.getApproachMargin()) <= 0;
        boolean triggered = strictlyIncreasing && dailyIncrease.compareTo(
                config.getMinDailyIncrease() == null
                ? BigDecimal.ZERO
                : config.getMinDailyIncrease()) >= 0 && withinOrOverThresholdRange;
        return new TrendResult(triggered, current, dailyIncrease, distance);
    }

    private WarehouseTemperatureAlertNoticeVO toNoticeVO(WarehouseTemperatureAlertRecord record) {
        if (record == null) {
            return inactiveNotice();
        }
        WarehouseTemperatureAlertNoticeVO vo = new WarehouseTemperatureAlertNoticeVO();
        vo.setAlerting(WarehouseTemperatureAlertStatus.ACTIVE.name().equals(record.getStatus()));
        vo.setMessage(buildNoticeMessage(record.getDistanceToThreshold()));
        vo.setDistanceToThreshold(record.getDistanceToThreshold());
        vo.setCurrentAvgTemperature(record.getCurrentAvgTemperature());
        vo.setWarningThreshold(record.getWarningThreshold());
        vo.setAlertRecordId(record.getId());
        vo.setTriggeredAt(record.getTriggeredAt());
        return vo;
    }

    private WarehouseTemperatureAlertNoticeVO inactiveNotice() {
        WarehouseTemperatureAlertNoticeVO vo = new WarehouseTemperatureAlertNoticeVO();
        vo.setAlerting(Boolean.FALSE);
        return vo;
    }

    private WarehouseTemperatureAlertPopupVO toPopupVO(WarehouseTemperatureAlertRecord record, LiangcangWarehousePageItemVO warehouse) {
        WarehouseTemperatureAlertPopupVO vo = new WarehouseTemperatureAlertPopupVO();
        vo.setAlertRecordId(record.getId());
        vo.setWarehouseId(record.getWarehouseId());
        vo.setWarehouseName(record.getWarehouseName() == null && warehouse != null ? warehouse.getName() : record.getWarehouseName());
        vo.setGrainType(record.getGrainType() == null && warehouse != null ? warehouse.getCategoryName() : record.getGrainType());
        vo.setCurrentAvgTemperature(record.getCurrentAvgTemperature());
        vo.setWarningThreshold(record.getWarningThreshold());
        vo.setApproachMargin(record.getApproachMargin());
        vo.setDistanceToThreshold(record.getDistanceToThreshold());
        vo.setTriggeredAt(record.getTriggeredAt());
        vo.setMessage(buildNoticeMessage(record.getDistanceToThreshold()));
        return vo;
    }

    private String buildNoticeMessage(BigDecimal distanceToThreshold) {
        if (distanceToThreshold == null) {
            return "温度持续上升，请关注";
        }
        return "温度持续上升，距阈值 " + distanceToThreshold.setScale(1, RoundingMode.HALF_UP).toPlainString() + "℃，请及时处置";
    }

    private List<Long> resolveWarehouseIdsFromBindings() {
        List<RadarWarehouseBinding> rows = radarWarehouseBindingService.list(Wrappers.<RadarWarehouseBinding>lambdaQuery()
                                                                                     .select(RadarWarehouseBinding::getWarehouseId));
        if (ListUtils.isEmpty(rows)) {
            return List.of();
        }
        return rows.stream()
                .map(RadarWarehouseBinding::getWarehouseId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private Set<Long> loadBoundWarehouseIds() {
        return SetUtils.listToSet(resolveWarehouseIdsFromBindings(), item -> item);
    }

    private Map<Long, TemperatureSampleSnapshot> loadTemperatureSamplesFromReportCenter(Set<Long> warehouseIds) {
        if (SetUtils.isEmpty(warehouseIds)) {
            return Map.of();
        }
        Map<Long, TemperatureSampleSnapshot> out = new LinkedHashMap<>();
        for (Long warehouseId : warehouseIds) {
            TemperatureSampleSnapshot snapshot = loadTemperatureSampleFromReportCenter(warehouseId);
            if (snapshot != null) {
                out.put(warehouseId, snapshot);
            }
        }
        return out;
    }

    private TemperatureSampleSnapshot loadTemperatureSampleFromReportCenter(Long warehouseId) {
        if (warehouseId == null) {
            return null;
        }
        var param = new WarehouseTemperatureReportParam();
        param.setWarehouseId(warehouseId);
        List<List<WarehouseTemperatureReportPointVO>> reportList = warehouseTemperatureTrendStatisticsService.temperatureReport(param);
        if (ListUtils.isEmpty(reportList)) {
            return null;
        }
        BigDecimal max = null;
        int sampleCount = 0;
        for (List<WarehouseTemperatureReportPointVO> row : reportList) {
            if (ListUtils.isEmpty(row)) {
                continue;
            }
            for (WarehouseTemperatureReportPointVO point : row) {
                BigDecimal temperature = point == null ? null : point.getTemperature();
                if (temperature == null) {
                    continue;
                }
                sampleCount++;
                if (max == null || temperature.compareTo(max) > 0) {
                    max = temperature;
                }
            }
        }
        return max == null ? null : new TemperatureSampleSnapshot(max, sampleCount);
    }

    private void exportRecords(HttpServletResponse response, List<WarehouseTemperatureAlertRecordVO> rows,
                               String fileName
                              ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
        List<WarehouseTemperatureAlertRecordExportVO> exports = rows.stream().map(this::toRecordExportVO).toList();
        EasyExcel.write(response.getOutputStream(), WarehouseTemperatureAlertRecordExportVO.class)
                .sheet("预警记录")
                .doWrite(exports);
    }

    private void exportSamples(HttpServletResponse response, List<WarehouseTemperatureAlertSampleDayVO> rows,
                               String fileName
                              ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
        List<WarehouseTemperatureAlertSampleExportVO> exports = new ArrayList<>();
        for (WarehouseTemperatureAlertSampleDayVO day : rows) {
            if (day == null) {
                continue;
            }
            for (WarehouseTemperatureAlertSampleDayVO.Item item : day.getItems()) {
                WarehouseTemperatureAlertSampleExportVO export = new WarehouseTemperatureAlertSampleExportVO();
                export.setStatDate(day.getDate());
                export.setWarehouseName(item == null ? null : item.getWarehouseName());
                export.setSampleTime(item == null ? null : item.getSampleTime());
                export.setMaxTemperature(item == null ? null : item.getMaxTemperature());
                export.setSampleCount(day.getSampleCount());
                export.setComplete(day.getComplete());
                export.setAvgMaxTemperature(day.getAvgMaxTemperature());
                exports.add(export);
            }
            if (ListUtils.isEmpty(day.getItems())) {
                WarehouseTemperatureAlertSampleExportVO export = new WarehouseTemperatureAlertSampleExportVO();
                export.setStatDate(day.getDate());
                export.setSampleCount(day.getSampleCount());
                export.setComplete(day.getComplete());
                export.setAvgMaxTemperature(day.getAvgMaxTemperature());
                exports.add(export);
            }
        }
        EasyExcel.write(response.getOutputStream(), WarehouseTemperatureAlertSampleExportVO.class)
                .sheet("采样明细")
                .doWrite(exports);
    }

    private WarehouseTemperatureAlertRecordVO toRecordVO(WarehouseTemperatureAlertRecord record) {
        WarehouseTemperatureAlertRecordVO vo = new WarehouseTemperatureAlertRecordVO();
        vo.setId(record.getId());
        vo.setWarehouseId(record.getWarehouseId());
        vo.setWarehouseName(record.getWarehouseName());
        vo.setGrainType(record.getGrainType());
        vo.setTriggeredAt(record.getTriggeredAt());
        vo.setCurrentAvgTemperature(record.getCurrentAvgTemperature());
        vo.setWarningThreshold(record.getWarningThreshold());
        vo.setApproachMargin(record.getApproachMargin());
        vo.setDistanceToThreshold(record.getDistanceToThreshold());
        vo.setDailyIncrease(record.getDailyIncrease());
        vo.setConsecutiveDays(record.getConsecutiveDays());
        vo.setStatus(record.getStatus());
        vo.setClearedAt(record.getClearedAt());
        vo.setClearedReason(record.getClearedReason());
        vo.setPopupEnabled(record.getPopupEnabled());
        vo.setPopupPushed(record.getPopupPushed());
        return vo;
    }

    private WarehouseTemperatureAlertRecordExportVO toRecordExportVO(WarehouseTemperatureAlertRecordVO vo) {
        WarehouseTemperatureAlertRecordExportVO export = new WarehouseTemperatureAlertRecordExportVO();
        export.setWarehouseName(vo.getWarehouseName());
        export.setGrainType(vo.getGrainType());
        export.setTriggeredAt(vo.getTriggeredAt());
        export.setCurrentAvgTemperature(vo.getCurrentAvgTemperature());
        export.setWarningThreshold(vo.getWarningThreshold());
        export.setApproachMargin(vo.getApproachMargin());
        export.setDistanceToThreshold(vo.getDistanceToThreshold());
        export.setDailyIncrease(vo.getDailyIncrease());
        export.setConsecutiveDays(vo.getConsecutiveDays());
        export.setStatus(vo.getStatus());
        export.setClearedAt(vo.getClearedAt());
        export.setClearedReason(vo.getClearedReason());
        export.setPopupEnabled(vo.getPopupEnabled());
        export.setPopupPushed(vo.getPopupPushed());
        return export;
    }

    private WarehouseTemperatureAlertConfigVO toConfigVO(
            com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertConfig entity
                                                        ) {
        if (entity == null) {
            return null;
        }
        WarehouseTemperatureAlertConfigVO vo = new WarehouseTemperatureAlertConfigVO();
        vo.setId(entity.getId());
        vo.setWarehouseId(entity.getWarehouseId());
        vo.setUseGlobalDefault(entity.getUseGlobalDefault());
        vo.setConsecutiveDays(entity.getConsecutiveDays());
        vo.setMinDailyIncrease(entity.getMinDailyIncrease());
        vo.setWarningThreshold(entity.getWarningThreshold());
        vo.setApproachMargin(entity.getApproachMargin());
        vo.setPopupEnabled(entity.getPopupEnabled());
        return vo;
    }

    private WarehouseTemperatureAlertGlobalConfigVO mergeConfig(WarehouseTemperatureAlertGlobalConfigVO global,
                                                                WarehouseTemperatureAlertConfigVO config
                                                               ) {
        if (config == null || Boolean.TRUE.equals(config.getUseGlobalDefault())) {
            return global;
        }
        WarehouseTemperatureAlertGlobalConfigVO vo = new WarehouseTemperatureAlertGlobalConfigVO();
        vo.setId(global.getId());
        vo.setConsecutiveDays(config.getConsecutiveDays() == null ? global.getConsecutiveDays() : config.getConsecutiveDays());
        vo.setMinDailyIncrease(config.getMinDailyIncrease() == null ? global.getMinDailyIncrease() : config.getMinDailyIncrease());
        vo.setWarningThreshold(config.getWarningThreshold() == null ? global.getWarningThreshold() : config.getWarningThreshold());
        vo.setApproachMargin(config.getApproachMargin() == null ? global.getApproachMargin() : config.getApproachMargin());
        return vo;
    }

    private boolean resolvePopupEnabled(WarehouseTemperatureAlertConfigVO config) {
        return config == null || config.getPopupEnabled() == null || config.getPopupEnabled();
    }

    private BigDecimal resolveWarningLine(WarehouseTemperatureAlertGlobalConfigVO config) {
        if (config == null || config.getWarningThreshold() == null || config.getApproachMargin() == null) {
            return null;
        }
        return config.getWarningThreshold().subtract(config.getApproachMargin());
    }

    private WarehouseTemperatureAlertClearReason resolveClearReason(TrendResult result) {
        if (result == null || result.currentAvgTemperature() == null) {
            return WarehouseTemperatureAlertClearReason.LEFT_DANGER_ZONE;
        }
        return result.distanceToThreshold() != null && result.distanceToThreshold().compareTo(BigDecimal.ZERO) > 0
               ? WarehouseTemperatureAlertClearReason.LEFT_DANGER_ZONE
               : WarehouseTemperatureAlertClearReason.TREND_REVERSED;
    }

    private static LocalDateTime floorToHalfHour(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        int minute = time.getMinute();
        int flooredMinute = minute < 30 ? 0 : 30;
        return time.withMinute(flooredMinute).withSecond(0).withNano(0);
    }

    private static BigDecimal scale(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal average(List<BigDecimal> values) {
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
        return sum.divide(BigDecimal.valueOf(count), SCALE, RoundingMode.HALF_UP);
    }

    private record TrendResult(boolean triggered,
                               BigDecimal currentAvgTemperature,
                               BigDecimal dailyIncrease,
                               BigDecimal distanceToThreshold) {
        private static TrendResult no() {
            return new TrendResult(false, null, null, null);
        }
    }

    private record TemperatureSampleSnapshot(BigDecimal maxTemperature, Integer sampleCount) {
    }

    // 供 Controller 直接复用的导出方法
    @Override
    public void exportRecord(HttpServletResponse response, WarehouseTemperatureAlertRecordPageParam param) throws IOException {
        exportRecords(response, recordExport(param), "粮仓温度趋势预警记录");
    }

    @Override
    public void exportSample(HttpServletResponse response, WarehouseTemperatureAlertSampleParam param) throws IOException {
        exportSamples(response, samples(param), "粮仓温度趋势预警采样明细");
    }
}
