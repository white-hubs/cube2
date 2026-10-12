package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.emergency.client.EmergencyRuleClient;
import com.guhecloud.cube.biz.emergency.client.vo.EmergencyRuleConditionCollectionVO;
import com.guhecloud.cube.biz.emergency.client.vo.EmergencyRuleConditionExpCollectionVO;
import com.guhecloud.cube.biz.emergency.client.vo.EmergencyRuleConditionGroupVO;
import com.guhecloud.cube.biz.emergency.client.vo.EmergencyRuleDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.config.WarehouseStorageArchiveAlarmConfigProvider;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseStorageArchiveAlarmConfigDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseMeasureHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseStorageArchiveTrendTimeType;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseStorageArchiveScanRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseStorageArchiveTrendParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseMeasureHistoryService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseStorageArchiveService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStorageArchiveBasicInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStorageArchiveScanRecordExportVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStorageArchiveScanRecordVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStorageArchiveTrendVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 储量档案。
 */
@Service
@RequiredArgsConstructor
public class WarehouseStorageArchiveServiceImpl implements WarehouseStorageArchiveService {

    private static final String STATUS_NORMAL = "NORMAL";
    private static final String STATUS_ALARM = "ALARM";
    private static final int EXPORT_PAGE_SIZE = 1000;
    private static final int MAX_CUSTOM_RANGE_DAYS = 30;
    private static final DateTimeFormatter HALF_HOUR_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final WarehouseMeasureHistoryService warehouseMeasureHistoryService;
    private final WarehousePageApiClient warehousePageApiClient;
    private final EmergencyRuleClient emergencyRuleClient;
    private final WarehouseStorageArchiveAlarmConfigProvider alarmConfigProvider;

    @Override
    public WarehouseStorageArchiveBasicInfoVO basicInfo(Long warehouseId) {
        if (warehouseId == null) {
            return null;
        }
        LiangcangWarehousePageItemVO warehouse = warehousePageApiClient.fetchPageItemById(warehouseId).orElse(null);
        if (warehouse == null) {
            return null;
        }
        WarehouseMeasureHistory latest = warehouseMeasureHistoryService.latestByWarehouseId(warehouseId).orElse(null);
        WarningConfig warningConfig = loadWarningConfig(warehouseId);
        if (warningConfig == null) {
            return null;
        }

        var vo = new WarehouseStorageArchiveBasicInfoVO();
        vo.setWarehouseId(warehouseId);
        vo.setWarehouseName(warehouse == null ? null : warehouse.getName());
        vo.setGrainType(warehouse == null ? null : warehouse.getCategoryName());
        BigDecimal currentVolume = latest == null ? toBigDecimal(warehouse.getMaterialVolume()) : latest.getMaterialVolume();
        vo.setCurrentVolume(currentVolume);
        vo.setVolumeWarningValue(warningConfig.volumeThreshold());
        vo.setVolumeDifferenceWarningValue(warningConfig.volumeDifferenceThreshold());
        vo.setLatestScanTime(latest == null ? null : latest.getAcquisitionDate());
        return vo;
    }

    @Override
    public WarehouseStorageArchiveTrendVO trend(WarehouseStorageArchiveTrendParam param) {
        Long warehouseId = param == null ? null : param.getWarehouseId();
        if (warehouseId == null) {
            return null;
        }
        WarningConfig warningConfig = loadWarningConfig(warehouseId);
        if (warningConfig == null) {
            return null;
        }
        TrendRange range = resolveTrendRange(param);
        BigDecimal warningValue = warningConfig.volumeThreshold();
        String warningOp = warningConfig.volumeOp();

        List<WarehouseMeasureHistory> rows = warehouseMeasureHistoryService.list(
                Wrappers.<WarehouseMeasureHistory>lambdaQuery()
                        .eq(WarehouseMeasureHistory::getWarehouseId, warehouseId)
                        .ge(WarehouseMeasureHistory::getAcquisitionDate, range.queryBegin())
                        .le(WarehouseMeasureHistory::getAcquisitionDate, range.queryEnd())
                        .orderByAsc(WarehouseMeasureHistory::getAcquisitionDate));
        Map<LocalDateTime, BigDecimal> averageByBucket = averageByBucket(rows, range.granularity());
        Map<LocalDateTime, Boolean> differenceAlarmByBucket =
                differenceAlarmByBucket(rows, warningConfig, range.granularity());

        var vo = new WarehouseStorageArchiveTrendVO();
        vo.setUnit("m3");
        vo.setWarningValue(warningValue);
        vo.setGranularity(range.granularity());
        List<WarehouseStorageArchiveTrendVO.Item> items = new ArrayList<>();
        for (LocalDateTime bucket = range.displayBegin();
             !bucket.isAfter(range.displayEnd());
             bucket = nextBucket(bucket, range.granularity())) {
            items.add(toTrendItem(bucket, averageByBucket.get(bucket), warningValue, warningOp,
                                  differenceAlarmByBucket.getOrDefault(bucket, false), range.granularity()));
        }
        vo.setItems(items);
        return vo;
    }

    @Override
    public Pagination<WarehouseStorageArchiveScanRecordVO> scanRecordPage(WarehouseStorageArchiveScanRecordPageParam param) {
        if (param == null || param.getWarehouseId() == null) {
            return null;
        }
        WarningConfig warningConfig = loadWarningConfig(param.getWarehouseId());
        if (warningConfig == null) {
            return null;
        }
        var query = Wrappers.<WarehouseMeasureHistory>lambdaQuery()
                .eq(WarehouseMeasureHistory::getWarehouseId, param.getWarehouseId())
                .orderByDesc(WarehouseMeasureHistory::getAcquisitionDate);
        applyPeriod(query, param.getPeriod());

        if (StringUtils.isNotBlank(param.getStatus())) {
            List<WarehouseMeasureHistory> rows = warehouseMeasureHistoryService.list(query);
            if (ListUtils.isEmpty(rows)) {
                return null;
            }
            List<WarehouseStorageArchiveScanRecordVO> filtered = convertScanRecords(param.getWarehouseId(), rows, warningConfig).stream()
                    .filter(row -> statusMatched(row, param.getStatus()))
                    .toList();
            long total = filtered.size();
            long pages = total == 0 ? 0 : (total + param.getPageSize() - 1L) / param.getPageSize();
            return Pagination.<WarehouseStorageArchiveScanRecordVO>builder()
                    .total(total)
                    .pages(pages)
                    .records(param.list(filtered))
                    .build();
        }

        Page<WarehouseMeasureHistory> page = warehouseMeasureHistoryService.page(PagingUtils.convert(param), query);
        if (page == null || ListUtils.isEmpty(page.getRecords())) {
            return null;
        }
        List<WarehouseStorageArchiveScanRecordVO> records =
                convertScanRecords(param.getWarehouseId(), page.getRecords(), warningConfig);
        return Pagination.<WarehouseStorageArchiveScanRecordVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(records)
                .build();
    }

    private List<WarehouseStorageArchiveScanRecordVO> convertScanRecords(Long warehouseId,
                                                                         List<WarehouseMeasureHistory> rows,
                                                                         WarningConfig warningConfig
                                                                        ) {
        Map<LocalDateTime, WarehouseMeasureHistory> previousByTime = loadPreviousByTime(warehouseId, rows);
        String warehouseName = warehousePageApiClient.fetchPageItemById(warehouseId)
                .map(LiangcangWarehousePageItemVO::getName)
                .orElse(null);
        return rows.stream()
                .map(row -> toScanRecord(row, previousByTime, warehouseName, warningConfig))
                .toList();
    }

    @Override
    public void exportScanRecords(HttpServletResponse response, WarehouseStorageArchiveScanRecordPageParam param)
            throws IOException {
        if (param == null || param.getWarehouseId() == null) {
            return;
        }
        var exportParam = new WarehouseStorageArchiveScanRecordPageParam();
        exportParam.setWarehouseId(param.getWarehouseId());
        exportParam.setPeriod(param.getPeriod());
        exportParam.setStatus(param.getStatus());
        exportParam.setPageNumber(1);
        exportParam.setPageSize(EXPORT_PAGE_SIZE);
        List<WarehouseStorageArchiveScanRecordExportVO> rows = new ArrayList<>();
        while (true) {
            Pagination<WarehouseStorageArchiveScanRecordVO> page = scanRecordPage(exportParam);
            if (page == null || ListUtils.isEmpty(page.getRecords())) {
                break;
            }
            rows.addAll(page.getRecords().stream().map(this::toExportVO).toList());
            if (exportParam.getPageNumber() >= page.getPages()) {
                break;
            }
            exportParam.setPageNumber(exportParam.getPageNumber() + 1);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = URLEncoder.encode("储量档案扫描记录", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ExcelTypeEnum.XLSX.getValue());
        EasyExcel.write(response.getOutputStream(), WarehouseStorageArchiveScanRecordExportVO.class)
                .sheet("扫描记录")
                .doWrite(rows);
    }

    private WarehouseStorageArchiveTrendVO.Item toTrendItem(LocalDateTime bucket, BigDecimal value,
                                                            BigDecimal warningValue, String warningOp,
                                                            boolean differenceAlarm,
                                                            WarehouseStorageArchiveTrendVO.Granularity granularity
                                                           ) {
        var item = new WarehouseStorageArchiveTrendVO.Item();
        item.setTime(formatBucket(bucket, granularity));
        item.setStorageValue(value);
        item.setWarningValue(warningValue);
        item.setAlarm(differenceAlarm || isAlarm(value, warningValue, warningOp));
        return item;
    }

    private Map<LocalDateTime, BigDecimal> averageByBucket(
            List<WarehouseMeasureHistory> rows,
            WarehouseStorageArchiveTrendVO.Granularity granularity
                                                               ) {
        if (ListUtils.isEmpty(rows)) {
            return Map.of();
        }
        Map<LocalDateTime, BigDecimal> totalByBucket = new LinkedHashMap<>();
        Map<LocalDateTime, Integer> countByBucket = new LinkedHashMap<>();
        for (WarehouseMeasureHistory row : rows) {
            if (row.getAcquisitionDate() == null) {
                continue;
            }
            BigDecimal value = row.getMaterialVolume();
            if (value == null) {
                continue;
            }
            LocalDateTime bucket = toBucket(row.getAcquisitionDate(), granularity);
            totalByBucket.merge(bucket, value, BigDecimal::add);
            countByBucket.merge(bucket, 1, Integer::sum);
        }
        Map<LocalDateTime, BigDecimal> out = new LinkedHashMap<>();
        for (Map.Entry<LocalDateTime, BigDecimal> entry : totalByBucket.entrySet()) {
            Integer count = countByBucket.get(entry.getKey());
            if (count != null && count > 0) {
                out.put(entry.getKey(), entry.getValue().divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP));
            }
        }
        return out;
    }

    private Map<LocalDateTime, Boolean> differenceAlarmByBucket(
            List<WarehouseMeasureHistory> rows,
            WarningConfig warningConfig,
            WarehouseStorageArchiveTrendVO.Granularity granularity
                                                                    ) {
        if (ListUtils.isEmpty(rows) || warningConfig.volumeDifferenceThreshold() == null) {
            return Map.of();
        }
        Map<LocalDateTime, Boolean> out = new HashMap<>();
        WarehouseMeasureHistory previous = null;
        for (WarehouseMeasureHistory row : rows) {
            if (row.getAcquisitionDate() == null) {
                continue;
            }
            BigDecimal difference = subtract(row.getMaterialVolume(), previous == null ? null : previous.getMaterialVolume());
            if (!isAlarm(difference, warningConfig.volumeDifferenceThreshold(),
                            warningConfig.volumeDifferenceOp())) {
                previous = row;
                continue;
            }
            out.put(toBucket(row.getAcquisitionDate(), granularity), true);
            previous = row;
        }
        return out;
    }

    private TrendRange resolveTrendRange(WarehouseStorageArchiveTrendParam param) {
        LocalDateTime now = LocalDateTime.now();
        WarehouseStorageArchiveTrendTimeType timeType =
                param.getTimeType() == null ? WarehouseStorageArchiveTrendTimeType.TODAY : param.getTimeType();
        if (timeType == WarehouseStorageArchiveTrendTimeType.THIS_MONTH) {
            LocalDate firstDay = now.toLocalDate().withDayOfMonth(1);
            return new TrendRange(firstDay.atStartOfDay(), now, firstDay.atStartOfDay(),
                                  firstDay.plusMonths(1).minusDays(1).atStartOfDay(),
                                  WarehouseStorageArchiveTrendVO.Granularity.DAY);
        }
        if (timeType == WarehouseStorageArchiveTrendTimeType.THIS_YEAR) {
            LocalDate firstDay = now.toLocalDate().withDayOfYear(1);
            return new TrendRange(firstDay.atStartOfDay(), now, firstDay.atStartOfDay(),
                                  firstDay.plusYears(1).minusMonths(1).atStartOfDay(),
                                  WarehouseStorageArchiveTrendVO.Granularity.MONTH);
        }
        if (timeType == WarehouseStorageArchiveTrendTimeType.CUSTOM && param.getPeriod() != null) {
            LocalDate beginDate = param.getPeriod().getBegin() == null
                                  ? param.getPeriod().getEnd().toLocalDate()
                                  : param.getPeriod().getBegin().toLocalDate();
            LocalDate endDate = param.getPeriod().getEnd() == null
                                ? beginDate
                                : param.getPeriod().getEnd().toLocalDate();
            if (beginDate.isAfter(endDate)) {
                LocalDate temporary = beginDate;
                beginDate = endDate;
                endDate = temporary;
            }
            if (ChronoUnit.DAYS.between(beginDate, endDate) + 1 > MAX_CUSTOM_RANGE_DAYS) {
                throw new ServiceException("日期筛选最多支持30天");
            }
            LocalDateTime queryEnd = endDate.atTime(LocalTime.MAX).isBefore(now)
                                     ? endDate.atTime(LocalTime.MAX)
                                     : now;
            return new TrendRange(beginDate.atStartOfDay(), queryEnd, beginDate.atStartOfDay(),
                                  endDate.atStartOfDay(),
                                  WarehouseStorageArchiveTrendVO.Granularity.DAY);
        }
        LocalDate today = now.toLocalDate();
        return new TrendRange(today.atStartOfDay(), now, today.atStartOfDay(), today.atTime(23, 30),
                              WarehouseStorageArchiveTrendVO.Granularity.HALF_HOUR);
    }

    private LocalDateTime toBucket(LocalDateTime time, WarehouseStorageArchiveTrendVO.Granularity granularity) {
        return switch (granularity) {
            case HALF_HOUR -> time.withMinute(time.getMinute() < 30 ? 0 : 30).withSecond(0).withNano(0);
            case DAY -> time.toLocalDate().atStartOfDay();
            case MONTH -> time.toLocalDate().withDayOfMonth(1).atStartOfDay();
        };
    }

    private LocalDateTime nextBucket(LocalDateTime bucket,
                                     WarehouseStorageArchiveTrendVO.Granularity granularity) {
        return switch (granularity) {
            case HALF_HOUR -> bucket.plusMinutes(30);
            case DAY -> bucket.plusDays(1);
            case MONTH -> bucket.plusMonths(1);
        };
    }

    private String formatBucket(LocalDateTime bucket, WarehouseStorageArchiveTrendVO.Granularity granularity) {
        return switch (granularity) {
            case HALF_HOUR -> HALF_HOUR_FORMATTER.format(bucket);
            case DAY -> DAY_FORMATTER.format(bucket);
            case MONTH -> MONTH_FORMATTER.format(bucket);
        };
    }

    private WarehouseStorageArchiveScanRecordVO toScanRecord(WarehouseMeasureHistory row,
                                                             Map<LocalDateTime, WarehouseMeasureHistory> previousByTime,
                                                             String warehouseName,
                                                             WarningConfig warningConfig
                                                            ) {
        WarehouseMeasureHistory previous = previousByTime.get(row.getAcquisitionDate());
        BigDecimal calculatedDifference = subtract(row.getMaterialVolume(), previous == null ? null : previous.getMaterialVolume());
        BigDecimal volumeDifference = calculatedDifference;
        var vo = new WarehouseStorageArchiveScanRecordVO();
        vo.setId(row.getId());
        vo.setWarehouseId(row.getWarehouseId());
        vo.setWarehouseName(warehouseName);
        vo.setScanTime(row.getAcquisitionDate());
        vo.setCurrentVolume(row.getMaterialVolume());
        vo.setVolumeChange(calculatedDifference);
        vo.setVolumeWarningValue(warningConfig.volumeThreshold());
        vo.setVolumeDifference(volumeDifference);
        vo.setVolumeDifferenceWarningValue(warningConfig.volumeDifferenceThreshold());
        boolean alarm = isAlarm(row.getMaterialVolume(), warningConfig.volumeThreshold(), warningConfig.volumeOp())
                        || isAlarm(volumeDifference, warningConfig.volumeDifferenceThreshold(), warningConfig.volumeDifferenceOp());
        vo.setStatus(alarm ? STATUS_ALARM : STATUS_NORMAL);
        vo.setStatusName(alarm ? "告警" : "正常");
        return vo;
    }

    private Map<LocalDateTime, WarehouseMeasureHistory> loadPreviousByTime(Long warehouseId, List<WarehouseMeasureHistory> pageRows) {
        if (ListUtils.isEmpty(pageRows)) {
            return Map.of();
        }
        List<LocalDateTime> times = pageRows.stream()
                .map(WarehouseMeasureHistory::getAcquisitionDate)
                .filter(Objects::nonNull)
                .toList();
        if (ListUtils.isEmpty(times)) {
            return Map.of();
        }
        LocalDateTime minTime = times.stream().min(Comparator.naturalOrder()).orElse(null);
        LocalDateTime maxTime = times.stream().max(Comparator.naturalOrder()).orElse(null);
        List<WarehouseMeasureHistory> rows = warehouseMeasureHistoryService.list(
                Wrappers.<WarehouseMeasureHistory>lambdaQuery()
                        .eq(WarehouseMeasureHistory::getWarehouseId, warehouseId)
                        .le(WarehouseMeasureHistory::getAcquisitionDate, maxTime)
                        .orderByDesc(WarehouseMeasureHistory::getAcquisitionDate));
        Map<LocalDateTime, WarehouseMeasureHistory> byNextTime = new java.util.HashMap<>();
        WarehouseMeasureHistory previous = null;
        for (WarehouseMeasureHistory row : rows) {
            if (row.getAcquisitionDate() != null && row.getAcquisitionDate().compareTo(minTime) < 0 && previous == null) {
                previous = row;
            }
            if (previous != null && row.getAcquisitionDate() != null) {
                byNextTime.put(previous.getAcquisitionDate(), row);
            }
            previous = row;
        }
        return byNextTime;
    }

    private WarningConfig loadWarningConfig(Long warehouseId) {
        WarehouseStorageArchiveAlarmConfigDTO propCodeConfig = alarmConfigProvider.load();
        if (propCodeConfig == null || !propCodeConfig.hasAlarmConfig()) {
            return null;
        }
        BigDecimal volume = null;
        String volumeOp = null;
        BigDecimal volumeDifference = null;
        String volumeDifferenceOp = null;
        Long ruleId = propCodeConfig.ruleId(warehouseId);
        if (ruleId == null) {
            return null;
        }
        EmergencyRuleDetailVO detail = emergencyRuleClient.detail(ruleId);
        if (detail == null) {
            return null;
        }
        if (ListUtils.isEmpty(detail.getEmergencyRuleConditionGroups())) {
            return null;
        }
        for (EmergencyRuleConditionExpCollectionVO exp : flattenExps(detail.getEmergencyRuleConditionGroups())) {
            BigDecimal threshold = parseDecimal(exp.getOprand());
            if (threshold == null) {
                continue;
            }
            if (volume == null && propCodeConfig.getVolumePropCode().equalsIgnoreCase(exp.getPropCode())) {
                volume = threshold;
                volumeOp = exp.getOp();
            }
            if (volumeDifference == null
                && StringUtils.isNotBlank(propCodeConfig.getVolumeDifferencePropCode())
                && propCodeConfig.getVolumeDifferencePropCode().equalsIgnoreCase(exp.getPropCode())) {
                volumeDifference = threshold;
                volumeDifferenceOp = exp.getOp();
            }
        }
        Long volumeDifferenceRuleId = propCodeConfig.volumeDifferenceRuleId(warehouseId);
        if (volumeDifferenceRuleId != null && !Objects.equals(volumeDifferenceRuleId, ruleId)) {
            EmergencyRuleDetailVO volumeDifferenceDetail = emergencyRuleClient.detail(volumeDifferenceRuleId);
            if (volumeDifferenceDetail != null) {
                for (EmergencyRuleConditionExpCollectionVO exp : flattenExps(volumeDifferenceDetail.getEmergencyRuleConditionGroups())) {
                    BigDecimal threshold = parseDecimal(exp.getOprand());
                    if (threshold != null
                        && volumeDifference == null
                        && StringUtils.isNotBlank(propCodeConfig.getVolumeDifferencePropCode())
                        && propCodeConfig.getVolumeDifferencePropCode().equalsIgnoreCase(exp.getPropCode())) {
                        volumeDifference = threshold;
                        volumeDifferenceOp = exp.getOp();
                        break;
                    }
                }
            }
        }
        if (volume == null) {
            return null;
        }
        return new WarningConfig(volume, volumeOp, volumeDifference, volumeDifferenceOp);
    }

    private List<EmergencyRuleConditionExpCollectionVO> flattenExps(List<EmergencyRuleConditionGroupVO> groups) {
        List<EmergencyRuleConditionExpCollectionVO> out = new ArrayList<>();
        for (EmergencyRuleConditionGroupVO group : groups) {
            if (group == null || ListUtils.isEmpty(group.getEmergencyRuleConditions())) {
                continue;
            }
            for (EmergencyRuleConditionCollectionVO condition : group.getEmergencyRuleConditions()) {
                if (condition != null && ListUtils.nonEmpty(condition.getEmergencyRuleConditionExps())) {
                    out.addAll(condition.getEmergencyRuleConditionExps());
                }
            }
        }
        return out;
    }

    private void applyPeriod(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<WarehouseMeasureHistory> query,
                             Period period
                            ) {
        if (period == null) {
            return;
        }
        if (period.getBegin() != null) {
            query.ge(WarehouseMeasureHistory::getAcquisitionDate, period.getBegin());
        }
        if (period.getEnd() != null) {
            query.le(WarehouseMeasureHistory::getAcquisitionDate, period.getEnd());
        }
    }

    private boolean statusMatched(WarehouseStorageArchiveScanRecordVO row, String status) {
        return StringUtils.isBlank(status) || Objects.equals(row.getStatus(), status.trim().toUpperCase());
    }

    private BigDecimal subtract(BigDecimal current, BigDecimal previous) {
        return current == null || previous == null ? null : current.subtract(previous);
    }

    private boolean isAlarm(BigDecimal value, BigDecimal threshold, String op) {
        if (value == null || threshold == null) {
            return false;
        }
        int compare = value.compareTo(threshold);
        return switch (StringUtils.isBlank(op) ? "GE" : op.trim().toUpperCase()) {
            case "GT" -> compare > 0;
            case "GE" -> compare >= 0;
            case "LT" -> compare < 0;
            case "LE" -> compare <= 0;
            case "EQ" -> compare == 0;
            case "NE" -> compare != 0;
            default -> compare >= 0;
        };
    }

    private BigDecimal parseDecimal(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal toBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    private WarehouseStorageArchiveScanRecordExportVO toExportVO(WarehouseStorageArchiveScanRecordVO vo) {
        var out = new WarehouseStorageArchiveScanRecordExportVO();
        out.setScanTime(vo.getScanTime());
        out.setCurrentVolume(vo.getCurrentVolume());
        out.setVolumeChange(vo.getVolumeChange());
        out.setVolumeWarningValue(vo.getVolumeWarningValue());
        out.setVolumeDifference(vo.getVolumeDifference());
        out.setVolumeDifferenceWarningValue(vo.getVolumeDifferenceWarningValue());
        out.setStatusName(vo.getStatusName());
        return out;
    }

    private record WarningConfig(BigDecimal volumeThreshold, String volumeOp,
                                 BigDecimal volumeDifferenceThreshold, String volumeDifferenceOp) {}

    private record TrendRange(LocalDateTime queryBegin, LocalDateTime queryEnd,
                              LocalDateTime displayBegin, LocalDateTime displayEnd,
                              WarehouseStorageArchiveTrendVO.Granularity granularity) {}
}
