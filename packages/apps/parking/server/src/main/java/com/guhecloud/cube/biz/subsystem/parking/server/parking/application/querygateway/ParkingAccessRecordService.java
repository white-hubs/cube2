package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.StatisticalChartDataVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingAccessQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingInfoQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingStatisticsQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.constants.ParkingPropCodes;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingAccessRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingHourlyVehicleFlowPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.device.ParkDeviceAppService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingHourlyVehicleFlowRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao.ParkingAccessRecordMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingAccessRecordTrendTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingAccessTypeTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingAccessTypeTrendTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingVehicleFlowDetailVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingVehicleFlowRankVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingVehicleFlowSummaryVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingVehicleFlowTrendVO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingAccessRecordService extends ServiceImpl<ParkingAccessRecordMapper, ParkingAccessRecordPO> {

    private static final int VEHICLE_FLOW_DETAIL_STEP_MINUTES = 10;

    private static final List<String> WEEK_LABELS = List.of("周一",
                                                            "周二",
                                                            "周三",
                                                            "周四",
                                                            "周五",
                                                            "周六",
                                                            "周天");

    private final ParkingInfoQueryService parkingInfoQueryService;

    private final ParkDeviceAppService parkDeviceAppService;

    private final ParkingHourlyVehicleFlowRepository parkingHourlyVehicleFlowRepository;

    public Page<ParkingAccessRecordPO> getPage(ParkingAccessQueryParam param) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(param.getAccessType()), ParkingAccessRecordPO::getAccessType, param.getAccessType())
                .eq(StringUtils.isNotEmpty(param.getParkingInfoCode()), ParkingAccessRecordPO::getParkingInfoCode, param.getParkingInfoCode())
                .like(StringUtils.isNotEmpty(param.getKeyword()), ParkingAccessRecordPO::getVehicleNo, param.getKeyword())
                .ge(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                  .getBegin()), ParkingAccessRecordPO::getAccessTime, ObjectUtils.ifNull(param.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                  .getEnd()), ParkingAccessRecordPO::getAccessTime, ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .orderByDesc(ParkingAccessRecordPO::getAccessTime)
                .page(PagingUtils.convert(param));
    }

    /**
     * 通行趋势：库内按小时聚合，最多回 24 行。
     * <p>
     * 输出与旧接口一致：X 轴为 {@code HH点}，仅补齐 {@code period.begin} 所在自然日（半开区间 [begin, nextDay)），
     * 且不超过 period.end。查询窗口与输出窗口对齐。
     */
    public List<ParkingAccessRecordTrendTotalVO> getRecordTrend(Period period, @NonNull ParkingAccessTypeEnum accessType,
                                                                LinkedHashSet<String> parkingInfoCodes
                                                               ) {
        var queryPeriod = resolveTrendQueryPeriod(period);
        var hourCounts = this.baseMapper.selectRecordTrendByHour(accessType, queryPeriod, parkingInfoCodes);
        Map<Integer, Long> countByHour = new HashMap<>(24);
        if (ObjectUtils.isNotEmpty(hourCounts)) {
            for (StatisticalChartDataVO row : hourCounts) {
                if (row.getName() == null || row.getValus() == null) {
                    continue;
                }
                countByHour.put(Integer.parseInt(row.getName().trim()), row.getValus().longValue());
            }
        }
        return fillHourTrend(queryPeriod, countByHour);
    }

    public Page<ParkingAccessRecordPO> getStrandedRecords(ParkingAccessQueryParam param) {
        return this.baseMapper.selectStrandedRecords(param, PagingUtils.convert(param));
    }

    public ParkingAccessTypeTotalVO getAccessTypeTotal(@NonNull Period period, LinkedHashSet<String> parkingInfoCodes) {
        Integer inNum = this.baseMapper.selectParkingAccessTotal(ParkingAccessTypeEnum.ACCESS_IN, period, parkingInfoCodes);
        Integer outNum = this.baseMapper.selectParkingAccessTotal(ParkingAccessTypeEnum.ACCESS_OUT, period, parkingInfoCodes);
        return new ParkingAccessTypeTotalVO(inNum, outNum);
    }

    public ParkingAccessTypeTrendTotalVO getAccessTypeTrendTotal(@NonNull Period period, LinkedHashSet<String> parkingInfoCodes) {
        var queryPeriod = resolveTrendQueryPeriod(period);
        var rows = this.baseMapper.selectRecordTrendByHourAllTypes(queryPeriod, parkingInfoCodes);
        Map<Integer, Long> inByHour = new HashMap<>(24);
        Map<Integer, Long> outByHour = new HashMap<>(24);
        if (ObjectUtils.isNotEmpty(rows)) {
            for (var row : rows) {
                if (row.getAccessType() == null || row.getHourOfDay() == null || row.getCount() == null) {
                    continue;
                }
                if (row.getAccessType() == ParkingAccessTypeEnum.ACCESS_IN) {
                    inByHour.put(row.getHourOfDay(), row.getCount().longValue());
                } else if (row.getAccessType() == ParkingAccessTypeEnum.ACCESS_OUT) {
                    outByHour.put(row.getHourOfDay(), row.getCount().longValue());
                }
            }
        }
        return new ParkingAccessTypeTrendTotalVO(fillHourTrend(queryPeriod, inByHour), fillHourTrend(queryPeriod, outByHour));
    }

    /**
     * 按请求时间维度统计停车出入趋势。
     * <p>
     * 今日维度继续复用原小时趋势逻辑，避免影响 {@code today-access-trend} 接口。
     */
    public ParkingAccessTypeTrendTotalVO getAccessTypeTrendTotal(@NonNull ParkingStatisticsQueryParam param) {
        var granularity = resolveAccessTrendGranularity(param);
        if ("HOUR".equals(granularity)) {
            return getAccessTypeTrendTotal(param.getPeriod(), param.getParkingInfoCodes());
        }
        var rows = this.baseMapper.selectRecordTrendByBucketAllTypes(param.getPeriod(),
                                                                     param.getParkingInfoCodes(),
                                                                     granularity);
        Map<String, Long> inByBucket = new HashMap<>();
        Map<String, Long> outByBucket = new HashMap<>();
        if (ObjectUtils.isNotEmpty(rows)) {
            for (var row : rows) {
                if (row.getAccessType() == null || row.getBucket() == null || row.getCount() == null) {
                    continue;
                }
                if (row.getAccessType() == ParkingAccessTypeEnum.ACCESS_IN) {
                    inByBucket.put(row.getBucket(), row.getCount());
                } else if (row.getAccessType() == ParkingAccessTypeEnum.ACCESS_OUT) {
                    outByBucket.put(row.getBucket(), row.getCount());
                }
            }
        }
        return new ParkingAccessTypeTrendTotalVO(fillAccessTrend(param.getPeriod(), param.getType(), granularity, inByBucket),
                                                 fillAccessTrend(param.getPeriod(), param.getType(), granularity, outByBucket));
    }

    /**
     * 趋势入口径：[period.begin, begin 次日 0 点)，再与 period.end 取更早者。
     * 上界用次日 0 点半开区间，避免 LocalTime.MAX 纳秒边界，利于索引 range。
     */
    private static Period resolveTrendQueryPeriod(Period period) {
        var beginTime = period.getBegin();
        var endTime = DateUtils.offset(DateUtils.beginOfDay(beginTime), ChronoUnit.DAYS, 1);
        if (period.getEnd() != null && period.getEnd().isBefore(endTime)) {
            endTime = period.getEnd();
        }
        return new Period(beginTime, endTime);
    }

    private static List<ParkingAccessRecordTrendTotalVO> fillHourTrend(Period queryPeriod, Map<Integer, Long> countByHour) {
        var beginTime = queryPeriod.getBegin();
        var endTime = queryPeriod.getEnd();
        List<ParkingAccessRecordTrendTotalVO> result = new ArrayList<>(24);
        for (var start = beginTime; start.isBefore(endTime); start = DateUtils.offset(start, ChronoUnit.HOURS, 1)) {
            long num = countByHour.getOrDefault(start.getHour(), 0L);
            result.add(new ParkingAccessRecordTrendTotalVO(num, DateUtils.format(start, "HH") + "点"));
        }
        return result;
    }

    private static String resolveAccessTrendGranularity(ParkingStatisticsQueryParam param) {
        if (param != null && StringUtils.isNotBlank(param.getGranularity())) {
            return param.getGranularity().toUpperCase();
        }
        var type = param == null || param.getType() == null ? "" : param.getType().toUpperCase();
        if ("YEAR".equals(type) || "THIS_YEAR".equals(type)
            || "RECENT_THREE_MONTH".equals(type) || "RECENT_HALF_YEAR".equals(type)) {
            return "MONTH";
        }
        if ("DAY".equals(type) || "WEEK".equals(type) || "THIS_WEEK".equals(type)
            || "MONTH".equals(type) || "THIS_MONTH".equals(type)) {
            return "DAY";
        }
        return "HOUR";
    }

    private static List<ParkingAccessRecordTrendTotalVO> fillAccessTrend(Period period,
                                                                         String type,
                                                                         String granularity,
                                                                         Map<String, Long> countByBucket) {
        var result = new ArrayList<ParkingAccessRecordTrendTotalVO>();
        var cursor = truncateAccessTrendBucket(period.getBegin(), granularity);
        while (cursor.isBefore(period.getEnd())) {
            var bucket = accessTrendBucket(cursor, granularity);
            result.add(new ParkingAccessRecordTrendTotalVO(countByBucket.getOrDefault(bucket, 0L),
                                                           accessTrendLabel(cursor, type, granularity)));
            cursor = nextAccessTrendBucket(cursor, granularity);
        }
        return result;
    }

    private static LocalDateTime truncateAccessTrendBucket(LocalDateTime dateTime, String granularity) {
        if ("MONTH".equals(granularity)) {
            return YearMonth.from(dateTime).atDay(1).atStartOfDay();
        }
        if ("DAY".equals(granularity)) {
            return dateTime.toLocalDate().atStartOfDay();
        }
        return dateTime.withMinute(0).withSecond(0).withNano(0);
    }

    private static LocalDateTime nextAccessTrendBucket(LocalDateTime dateTime, String granularity) {
        if ("MONTH".equals(granularity)) {
            return dateTime.plusMonths(1);
        }
        if ("DAY".equals(granularity)) {
            return dateTime.plusDays(1);
        }
        return dateTime.plusHours(1);
    }

    private static String accessTrendBucket(LocalDateTime dateTime, String granularity) {
        if ("MONTH".equals(granularity)) {
            return DateUtils.format(dateTime, "yyyy-MM");
        }
        if ("DAY".equals(granularity)) {
            return DateUtils.format(dateTime, DateUtils.DEFAULT_DATE_FORMATTER);
        }
        return DateUtils.format(dateTime, "yyyy-MM-dd HH");
    }

    private static String accessTrendLabel(LocalDateTime dateTime, String type, String granularity) {
        if ("MONTH".equals(granularity)) {
            return dateTime.getMonthValue() + "月";
        }
        if ("DAY".equals(granularity)) {
            if ("WEEK".equalsIgnoreCase(type) || "THIS_WEEK".equalsIgnoreCase(type)) {
                return WEEK_LABELS.get(dateTime.getDayOfWeek().getValue() - 1);
            }
            return dateTime.getDayOfMonth() + "日";
        }
        return DateUtils.format(dateTime, "HH") + "点";
    }

    /**
     * 车流统计汇总，数据源为设备属性小时采集记录。
     */
    public ParkingVehicleFlowSummaryVO queryVehicleFlowSummary(ParkingStatisticsQueryParam param) {
        var statContext = buildVehicleFlowStatContext(param);
        var deviceIds = listStatDeviceIds(param);
        var currentRecords = listVehicleFlowRecords(deviceIds, statContext.period());
        var currentBucketMap = aggregateByBucket(currentRecords, statContext.granularity());
        var total = sumVehicleNumber(currentRecords);
        var previousTotal = sumVehicleNumber(listVehicleFlowRecords(deviceIds, statContext.previousPeriod()));
        var yoyTotal = sumVehicleNumber(listVehicleFlowRecords(deviceIds, statContext.yoyPeriod()));
        var filledBucketMap = fillBuckets(statContext.period(), statContext.granularity(), currentBucketMap);
        var peakEntry = findPeak(filledBucketMap);
        return new ParkingVehicleFlowSummaryVO(total,
                                               calculateRate(total, previousTotal),
                                               calculateRate(total, yoyTotal),
                                               calculateAverage(total, filledBucketMap.size()),
                                               peakEntry.getValue(),
                                               peakEntry.getKey());
    }

    /**
     * 车流趋势。
     */
    public List<ParkingVehicleFlowTrendVO> getVehicleFlowTrend(ParkingStatisticsQueryParam param) {
        var statContext = buildVehicleFlowStatContext(param);
        var records = listVehicleFlowRecords(listStatDeviceIds(param), statContext.period());
        var bucketMap = fillBuckets(statContext.period(),
                                    statContext.granularity(),
                                    aggregateByBucket(records, statContext.granularity()));
        return bucketMap.entrySet().stream()
                .map(entry -> new ParkingVehicleFlowTrendVO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 车流详情趋势，按 10 分钟统计并补齐空白时间点。
     */
    public List<ParkingVehicleFlowTrendVO> getVehicleFlowDetailTrend(ParkingStatisticsQueryParam param) {
        var period = resolveVehicleFlowDetailPeriod(param);
        var bucketMap = listVehicleFlowDetail(param).stream()
                .collect(Collectors.groupingBy(ParkingVehicleFlowDetailVO::getReportTime,
                                               Collectors.summingInt(ParkingVehicleFlowDetailVO::getTotalVehicleNumber)));
        return fillDetailBuckets(period, bucketMap).entrySet().stream()
                .map(entry -> new ParkingVehicleFlowTrendVO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 车流详情列表，按停车场与 10 分钟分页统计。
     */
    public Pagination<ParkingVehicleFlowDetailVO> pageVehicleFlowDetail(ParkingStatisticsQueryParam param) {
        var queryParam = param == null ? new ParkingStatisticsQueryParam() : param;
        var rows = listVehicleFlowDetail(queryParam);
        if (CollectionUtils.isEmpty(rows)) {
            return Pagination.empty();
        }
        var total = (long) rows.size();
        var pages = (long) Math.ceil((double) total / queryParam.getPageSize());
        return Pagination.<ParkingVehicleFlowDetailVO>builder()
                .total(total)
                .pages(pages)
                .records(queryParam.list(rows))
                .build();
    }

    /**
     * 全量车流详情列表，供导出使用。
     */
    public List<ParkingVehicleFlowDetailVO> listVehicleFlowDetail(ParkingStatisticsQueryParam param) {
        var parkingInfos = listStatParkingInfos(param);
        if (CollectionUtils.isEmpty(parkingInfos)) {
            return Collections.emptyList();
        }
        var deviceIdToParkingId = new HashMap<Long, Long>();
        for (var parkingInfo : parkingInfos) {
            if (CollectionUtils.isEmpty(parkingInfo.getDevices())) {
                continue;
            }
            parkingInfo.getDevices().forEach(device -> deviceIdToParkingId.put(device.getDeviceId(), parkingInfo.getId()));
        }
        if (CollectionUtils.isEmpty(deviceIdToParkingId.keySet())) {
            return Collections.emptyList();
        }
        var deviceTSMap = parkDeviceAppService.getDeviceTSHistoriesMap(ParkingPropCodes.CURRENT_CAR_NUM,
                                                                        resolveVehicleFlowDetailPeriod(param),
                                                                        deviceIdToParkingId.keySet(),
                                                                        VEHICLE_FLOW_DETAIL_STEP_MINUTES,
                                                                        "avg");
        if (deviceTSMap == null || deviceTSMap.isEmpty()) {
            return Collections.emptyList();
        }
        var parkingReportTotals = new HashMap<Long, Map<String, Double>>();
        for (var entry : deviceTSMap.entrySet()) {
            long deviceId;
            try {
                deviceId = Long.parseLong(entry.getKey());
            } catch (NumberFormatException e) {
                continue;
            }
            var parkingInfoId = deviceIdToParkingId.get(deviceId);
            if (parkingInfoId == null) {
                continue;
            }
            var parkingMap = parkingReportTotals.computeIfAbsent(parkingInfoId, key -> new HashMap<>());
            sumByReportTimeFromDeviceTs(entry.getValue())
                    .forEach((reportTime, value) -> parkingMap.merge(reportTime, value, Double::sum));
        }
        return parkingInfos.stream()
                .flatMap(parkingInfo -> parkingReportTotals.getOrDefault(parkingInfo.getId(), Collections.emptyMap())
                        .entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(entry -> new ParkingVehicleFlowDetailVO(parkingInfo.getId(),
                                                                     parkingInfo.getName(),
                                                                     parkingInfo.getModelArea(),
                                                                     parkingInfo.getRemarks(),
                                                                     entry.getValue().intValue(),
                                                                     entry.getKey())))
                .collect(Collectors.toList());
    }

    /**
     * 停车场车辆排行。
     */
    public Pagination<ParkingVehicleFlowRankVO> getParkingVehicleFlowRank(ParkingStatisticsQueryParam param) {
        var queryParam = param == null ? new ParkingStatisticsQueryParam() : param;
        var rows = listParkingVehicleFlowRank(queryParam);
        if (CollectionUtils.isEmpty(rows)) {
            return Pagination.empty();
        }
        var total = (long) rows.size();
        var pages = (long) Math.ceil((double) total / queryParam.getPageSize());
        return Pagination.<ParkingVehicleFlowRankVO>builder()
                .total(total)
                .pages(pages)
                .records(queryParam.list(rows))
                .build();
    }

    /**
     * 全量停车场车辆排行，供导出使用。
     */
    public List<ParkingVehicleFlowRankVO> listParkingVehicleFlowRank(ParkingStatisticsQueryParam param) {
        var queryParam = param == null ? new ParkingStatisticsQueryParam() : param;
        var statContext = buildVehicleFlowStatContext(queryParam);
        var parkingInfos = listStatParkingInfos(queryParam);
        var records = listVehicleFlowRecords(listDeviceIds(parkingInfos), statContext.period());
        var bucketCount = countBuckets(statContext.period(), statContext.granularity());
        var defaultPeakPeriod = formatBucket(statContext.period().getBegin(), statContext.granularity());
        return parkingInfos.stream()
                .map(parking -> buildVehicleFlowRank(parking,
                                                     records,
                                                     statContext.granularity(),
                                                     bucketCount,
                                                     defaultPeakPeriod))
                .sorted(vehicleFlowRankComparator(queryParam.getSortField()))
                .collect(Collectors.toList());
    }

    private List<ParkingHourlyVehicleFlowPO> listVehicleFlowRecords(Set<Long> deviceIds, Period period) {
        return parkingHourlyVehicleFlowRepository.findByDeviceIdsAndRecordTimeBetween(deviceIds,
                                                                                       period.getBegin(),
                                                                                       period.getEnd());
    }

    private List<ParkingInfo> listStatParkingInfos(ParkingStatisticsQueryParam param) {
        var parkingQuery = new ParkingInfoQueryParam();
        parkingQuery.setParkingInfoCodes(param == null ? null : param.getParkingInfoCodes());
        return parkingInfoQueryService.getParkingInfo(parkingQuery);
    }

    private Set<Long> listStatDeviceIds(ParkingStatisticsQueryParam param) {
        return listDeviceIds(listStatParkingInfos(param));
    }

    private Set<Long> listDeviceIds(List<ParkingInfo> parkingInfos) {
        if (CollectionUtils.isEmpty(parkingInfos)) {
            return Collections.emptySet();
        }
        return parkingInfos.stream()
                .filter(parkingInfo -> CollectionUtils.isNotEmpty(parkingInfo.getDevices()))
                .flatMap(parkingInfo -> parkingInfo.getDevices().stream())
                .map(device -> device.getDeviceId())
                .collect(Collectors.toSet());
    }

    private Map<String, Integer> aggregateByBucket(List<ParkingHourlyVehicleFlowPO> records, String granularity) {
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyMap();
        }
        return records.stream()
                .filter(record -> record.getRecordTime() != null && record.getVehicleCount() != null)
                .collect(Collectors.groupingBy(record -> formatBucket(record.getRecordTime(), granularity),
                                               Collectors.summingInt(ParkingHourlyVehicleFlowPO::getVehicleCount)));
    }

    private int sumVehicleNumber(List<ParkingHourlyVehicleFlowPO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return 0;
        }
        return records.stream()
                .map(ParkingHourlyVehicleFlowPO::getVehicleCount)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private ParkingVehicleFlowRankVO buildVehicleFlowRank(ParkingInfo parking,
                                                           List<ParkingHourlyVehicleFlowPO> records,
                                                           String granularity,
                                                           int bucketCount,
                                                           String defaultPeakPeriod) {
        var deviceIds = CollectionUtils.isEmpty(parking.getDevices()) ? Collections.<Long>emptySet()
                : parking.getDevices().stream().map(device -> device.getDeviceId()).collect(Collectors.toSet());
        var bucketMap = records.stream()
                .filter(record -> deviceIds.contains(record.getDeviceId()))
                .filter(record -> record.getRecordTime() != null && record.getVehicleCount() != null)
                .collect(Collectors.toMap(record -> formatBucket(record.getRecordTime(), granularity),
                                          ParkingHourlyVehicleFlowPO::getVehicleCount,
                                          Integer::sum));
        var total = bucketMap.values().stream().mapToInt(Integer::intValue).sum();
        var peakEntry = bucketMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(new AbstractMap.SimpleEntry<>(defaultPeakPeriod, 0));
        return new ParkingVehicleFlowRankVO(parking.getId(),
                                            parking.getName(),
                                            parking.getModelArea(),
                                            parking.getRemarks(),
                                            total,
                                            calculateAverage(total, bucketCount),
                                            peakEntry.getValue(),
                                            peakEntry.getKey());
    }

    private Map<String, Double> sumByReportTimeFromDeviceTs(DeviceTSDTO deviceTs) {
        if (deviceTs == null || CollectionUtils.isEmpty(deviceTs.getPropertyTsBoList())) {
            return Collections.emptyMap();
        }
        return deviceTs.getPropertyTsBoList().stream()
                .filter(propertyTs -> CollectionUtils.isNotEmpty(propertyTs.getValueTsBoList()))
                .flatMap(propertyTs -> propertyTs.getValueTsBoList().stream())
                .map(valueTs -> {
                    var reportTime = formatReportTime(valueTs.getTs());
                    var value = parseDoubleValue(valueTs.getValue());
                    if (reportTime == null || value == null) {
                        return null;
                    }
                    return new AbstractMap.SimpleEntry<>(reportTime, value);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)));
    }

    private String formatReportTime(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return DateUtils.format(DateUtils.parse(timestamp), DateUtils.DEFAULT_DATETIME_FORMATTER);
    }

    private Double parseDoubleValue(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Comparator<ParkingVehicleFlowRankVO> vehicleFlowRankComparator(String sortField) {
        var totalComparator = Comparator.comparing(ParkingVehicleFlowRankVO::getTotalVehicleNumber,
                                                   Comparator.nullsFirst(Integer::compareTo))
                .reversed();
        if ("AVG".equalsIgnoreCase(sortField)) {
            return Comparator.comparing(ParkingVehicleFlowRankVO::getAvgVehicleNumber,
                                        Comparator.nullsFirst(Integer::compareTo))
                    .reversed()
                    .thenComparing(totalComparator);
        }
        return totalComparator;
    }

    private VehicleFlowStatContext buildVehicleFlowStatContext(ParkingStatisticsQueryParam param) {
        var granularity = resolveGranularity(param);
        var period = resolvePeriod(param, granularity);
        return new VehicleFlowStatContext(granularity,
                                          period,
                                          resolvePreviousPeriod(period, granularity),
                                          new Period(period.getBegin().minusYears(1), period.getEnd().minusYears(1)));
    }

    private String resolveGranularity(ParkingStatisticsQueryParam param) {
        if (param != null && StringUtils.isNotBlank(param.getGranularity())) {
            return param.getGranularity().toUpperCase();
        }
        var type = param == null || param.getType() == null ? "" : param.getType();
        if ("YEAR".equals(type) || "THIS_YEAR".equals(type)) {
            return "MONTH";
        }
        if ("MONTH".equals(type) || "THIS_MONTH".equals(type)) {
            return "DAY";
        }
        return "HOUR";
    }

    private Period resolvePeriod(ParkingStatisticsQueryParam param, String granularity) {
        if (param != null && param.getPeriod() != null) {
            return param.getPeriod();
        }
        var now = LocalDateTime.now();
        var type = param == null || param.getType() == null ? "" : param.getType();
        if ("YEAR".equals(type) || "THIS_YEAR".equals(type) || "MONTH".equals(granularity)) {
            var begin = LocalDateTime.of(now.getYear(), 1, 1, 0, 0);
            return new Period(begin, begin.plusYears(1));
        }
        if ("MONTH".equals(type) || "THIS_MONTH".equals(type) || "DAY".equals(granularity)) {
            var begin = YearMonth.from(now).atDay(1).atStartOfDay();
            return new Period(begin, begin.plusMonths(1));
        }
        var begin = now.toLocalDate().atStartOfDay();
        return new Period(begin, begin.plusDays(1));
    }

    private Period resolveVehicleFlowDetailPeriod(ParkingStatisticsQueryParam param) {
        if (param != null && param.getPeriod() != null) {
            return param.getPeriod();
        }
        var begin = LocalDateTime.now().toLocalDate().atStartOfDay();
        return new Period(begin, begin.plusDays(1));
    }

    private Period resolvePreviousPeriod(Period period, String granularity) {
        if ("MONTH".equals(granularity)) {
            return new Period(period.getBegin().minusYears(1), period.getEnd().minusYears(1));
        }
        if ("DAY".equals(granularity)) {
            return new Period(period.getBegin().minusMonths(1), period.getEnd().minusMonths(1));
        }
        if ("HOUR".equals(granularity)) {
            return new Period(period.getBegin().minusDays(1), period.getEnd().minusDays(1));
        }
        var duration = Duration.between(period.getBegin(), period.getEnd());
        return new Period(period.getBegin().minus(duration), period.getEnd().minus(duration));
    }

    private LinkedHashMap<String, Integer> fillBuckets(Period period,
                                                        String granularity,
                                                        Map<String, Integer> bucketMap) {
        var result = new LinkedHashMap<String, Integer>();
        var cursor = truncate(period.getBegin(), granularity);
        while (cursor.isBefore(period.getEnd())) {
            var bucket = formatBucket(cursor, granularity);
            result.put(bucket, bucketMap.getOrDefault(bucket, 0));
            cursor = nextBucket(cursor, granularity);
        }
        if (result.isEmpty()) {
            var bucket = formatBucket(period.getBegin(), granularity);
            result.put(bucket, bucketMap.getOrDefault(bucket, 0));
        }
        return result;
    }

    private LinkedHashMap<String, Integer> fillDetailBuckets(Period period, Map<String, Integer> bucketMap) {
        var result = new LinkedHashMap<String, Integer>();
        var cursor = period.getBegin().withMinute(period.getBegin().getMinute() / VEHICLE_FLOW_DETAIL_STEP_MINUTES
                                                  * VEHICLE_FLOW_DETAIL_STEP_MINUTES)
                .withSecond(0)
                .withNano(0);
        while (cursor.isBefore(period.getEnd())) {
            var bucket = DateUtils.format(cursor, DateUtils.DEFAULT_DATETIME_FORMATTER);
            result.put(bucket, bucketMap.getOrDefault(bucket, 0));
            cursor = cursor.plusMinutes(VEHICLE_FLOW_DETAIL_STEP_MINUTES);
        }
        return result;
    }

    private int countBuckets(Period period, String granularity) {
        var count = 0;
        var cursor = truncate(period.getBegin(), granularity);
        while (cursor.isBefore(period.getEnd())) {
            count++;
            cursor = nextBucket(cursor, granularity);
        }
        return Math.max(count, 1);
    }

    private Map.Entry<String, Integer> findPeak(LinkedHashMap<String, Integer> bucketMap) {
        return bucketMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(new AbstractMap.SimpleEntry<>(null, 0));
    }

    private int calculateAverage(int total, int bucketCount) {
        if (bucketCount <= 0) {
            return 0;
        }
        return BigDecimal.valueOf(total)
                .divide(BigDecimal.valueOf(bucketCount), 0, RoundingMode.HALF_UP)
                .intValue();
    }

    private BigDecimal calculateRate(int current, int compared) {
        if (compared <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(current - compared)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(compared), 2, RoundingMode.HALF_UP);
    }

    private LocalDateTime truncate(LocalDateTime dateTime, String granularity) {
        if ("YEAR".equals(granularity)) {
            return LocalDateTime.of(dateTime.getYear(), 1, 1, 0, 0);
        }
        if ("MONTH".equals(granularity)) {
            return YearMonth.from(dateTime).atDay(1).atStartOfDay();
        }
        if ("DAY".equals(granularity)) {
            return dateTime.toLocalDate().atStartOfDay();
        }
        return dateTime.withMinute(0).withSecond(0).withNano(0);
    }

    private LocalDateTime nextBucket(LocalDateTime dateTime, String granularity) {
        if ("YEAR".equals(granularity)) {
            return dateTime.plusYears(1);
        }
        if ("MONTH".equals(granularity)) {
            return dateTime.plusMonths(1);
        }
        if ("DAY".equals(granularity)) {
            return dateTime.plusDays(1);
        }
        return dateTime.plusHours(1);
    }

    private String formatBucket(LocalDateTime dateTime, String granularity) {
        if ("YEAR".equals(granularity)) {
            return DateUtils.format(dateTime, "yyyy");
        }
        if ("MONTH".equals(granularity)) {
            return DateUtils.format(dateTime, "yyyy-MM");
        }
        if ("DAY".equals(granularity)) {
            return DateUtils.format(dateTime, DateUtils.DEFAULT_DATE_FORMATTER);
        }
        return DateUtils.format(dateTime, "yyyy-MM-dd HH:00");
    }

    private record VehicleFlowStatContext(String granularity, Period period, Period previousPeriod, Period yoyPeriod) {
    }
}
