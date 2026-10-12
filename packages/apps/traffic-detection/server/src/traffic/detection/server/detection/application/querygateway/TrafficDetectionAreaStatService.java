package com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway;

import com.guhecloud.cube.biz.traffic.detection.server.common.constants.TrafficDetectionPropCodes;
import com.guhecloud.cube.biz.traffic.detection.server.common.util.DateUnitUtils;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.query.TrafficDetectionAreaStatQueryParam;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.aggregates.TrafficDetectionArea;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionHourlyPersonFlowPO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaDevice;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device.TrafficDetectionAreaDeviceAppService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device.dto.TrafficDetectionDevicePropDTO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionAreaDeviceRepository;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionAreaRepository;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionHourlyPersonFlowRepository;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionAreaListStatVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionAreaPersonFlowRankVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionAreaPersonTrendStatVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionDeviceStatVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionPersonFlowDetailVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionPersonFlowSummaryVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionPersonFlowTrendVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionPersonTotalStatVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionTrendStatVO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrafficDetectionAreaStatService {

    private static final int PERSON_FLOW_DETAIL_STEP_MINUTES = 10;

    private final TrafficDetectionAreaRepository trafficDetectionAreaRepository;

    private final TrafficDetectionAreaDeviceAppService trafficDetectionAreaDeviceAppService;

    private final TrafficDetectionAreaDeviceRepository trafficDetectionAreaDeviceRepository;

    private final TrafficDetectionAreaPersonFlowService trafficDetectionAreaPersonFlowService;

    private final TrafficDetectionHourlyPersonFlowRepository trafficDetectionHourlyPersonFlowRepository;

    public TrafficDetectionAreaStatService(TrafficDetectionAreaRepository trafficDetectionAreaRepository,
                                           TrafficDetectionAreaDeviceAppService trafficDetectionAreaDeviceAppService,
                                           TrafficDetectionAreaDeviceRepository trafficDetectionAreaDeviceRepository,
                                           TrafficDetectionAreaPersonFlowService trafficDetectionAreaPersonFlowService,
                                           TrafficDetectionHourlyPersonFlowRepository trafficDetectionHourlyPersonFlowRepository
                                          ) {
        this.trafficDetectionAreaRepository = trafficDetectionAreaRepository;
        this.trafficDetectionAreaDeviceAppService = trafficDetectionAreaDeviceAppService;
        this.trafficDetectionAreaDeviceRepository = trafficDetectionAreaDeviceRepository;
        this.trafficDetectionAreaPersonFlowService = trafficDetectionAreaPersonFlowService;
        this.trafficDetectionHourlyPersonFlowRepository = trafficDetectionHourlyPersonFlowRepository;
    }

    /**
     * 人流量数
     *
     * @param param 查询参数
     *
     * @return 人流量数
     */
    public TrafficDetectionPersonTotalStatVO queryPersonTotalStat(TrafficDetectionAreaStatQueryParam param) {
        var trafficDetectionAreaDevices = trafficDetectionAreaDeviceRepository.findList(param.getTrafficDetectionAreaIds());
        if (CollectionUtils.isEmpty(trafficDetectionAreaDevices)) {
            return new TrafficDetectionPersonTotalStatVO();
        }
        var totalNumber = trafficDetectionAreaDeviceAppService.getDeviceSinglePropValueSum(
                trafficDetectionAreaDevices.stream().map(TrafficDetectionAreaDevice::getDeviceId).collect(Collectors.toSet()),
                TrafficDetectionPropCodes.CURRENT_PEOPLE_NUM);
        return new TrafficDetectionPersonTotalStatVO(totalNumber);
    }

    /**
     * 流量检测区域人流量数实时列表
     *
     * @param param 查询参数
     *
     * @return 流量检测区域人流量数列表
     */
    public List<TrafficDetectionAreaListStatVO> getAreaPersonTotalList(TrafficDetectionAreaStatQueryParam param) {
        var trafficDetectionAreas = trafficDetectionAreaRepository.findList(null, null);
        if (CollectionUtils.isEmpty(trafficDetectionAreas)) {
            return Collections.emptyList();
        }
        if (Objects.isNull(param.getPeriod())) {
            var areaIdToPersonFlow = trafficDetectionAreaPersonFlowService.getAreaIdToPersonFlowMap(trafficDetectionAreas);
            return trafficDetectionAreas.stream()
                    .map(area -> new TrafficDetectionAreaListStatVO(area.getId(), area.getName(), area.getModelArea(),
                                                                    areaIdToPersonFlow.getOrDefault(area.getId(), 0)))
                    .sorted(Comparator.comparing(TrafficDetectionAreaListStatVO::getTotalPersonNumber).reversed())
                    .collect(Collectors.toList());
        }
        var deviceIds = trafficDetectionAreas.stream()
                .filter(area -> CollectionUtils.isNotEmpty(area.getDevices()))
                .flatMap(area -> area.getDevices().stream())
                .map(TrafficDetectionAreaDevice::getDeviceId)
                .collect(Collectors.toSet());
        var devicePropMap = trafficDetectionAreaDeviceAppService.getDevicesSinglePropHistoryAvgValue(deviceIds, TrafficDetectionPropCodes.CURRENT_PEOPLE_NUM, param.getPeriod());
        Map<Long, Integer> finalDevicePropMap = devicePropMap;
        return trafficDetectionAreas.stream()
                .map(area -> {
                    var totalPersonNumber = 0;
                    if (CollectionUtils.isNotEmpty(area.getDevices())) {
                        totalPersonNumber = area.getDevices().stream()
                                .mapToInt(device -> finalDevicePropMap.getOrDefault(device.getDeviceId(), 0))
                                .sum();
                    }
                    return new TrafficDetectionAreaListStatVO(area.getId(), area.getName(), area.getModelArea(), totalPersonNumber);
                }).sorted(Comparator.comparing(TrafficDetectionAreaListStatVO::getTotalPersonNumber).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 流量检测区域设备人流量数列表
     *
     * @param param 查询参数
     *
     * @return 流量检测区域设备人流量数列表
     */
    public List<TrafficDetectionDeviceStatVO> getDeviceStatList(TrafficDetectionAreaStatQueryParam param) {
        var trafficDetectionAreaDevices = trafficDetectionAreaDeviceRepository.findList(param.getTrafficDetectionAreaIds());
        if (CollectionUtils.isEmpty(trafficDetectionAreaDevices)) {
            return Collections.emptyList();
        }
        List<TrafficDetectionDevicePropDTO> devicePropList;
        if (Objects.isNull(param.getPeriod())) {
            devicePropList = trafficDetectionAreaDeviceAppService.getTrafficDetectionDevicePropList(trafficDetectionAreaDevices.stream()
                                                                                                            .map(TrafficDetectionAreaDevice::getDeviceId)
                                                                                                            .collect(Collectors.toSet()), TrafficDetectionPropCodes.CURRENT_PEOPLE_NUM);
        } else {
            devicePropList = trafficDetectionAreaDeviceAppService.getTrafficDetectionDevicePropHistoryAvgValueList(trafficDetectionAreaDevices
                                                                                                                           .stream()
                                                                                                                           .map(TrafficDetectionAreaDevice::getDeviceId)
                                                                                                                           .collect(Collectors.toSet()), TrafficDetectionPropCodes.CURRENT_PEOPLE_NUM, param.getPeriod());
        }
        return devicePropList.stream()
                .map(device -> {
                    var trafficDetectionAreaDevice = trafficDetectionAreaDevices.stream()
                            .filter(areaDevice -> areaDevice.getDeviceId().equals(device.getId())).findAny();
                    return new TrafficDetectionDeviceStatVO(device.getId(), device.getName(), trafficDetectionAreaDevice
                            .map(TrafficDetectionAreaDevice::getDeviceConfig).orElse(null), device.getTotalPersonNumber());
                })
                .sorted(Comparator.comparing(TrafficDetectionDeviceStatVO::getTotalPersonNumber).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 各区域人流量趋势（与 {@link #getTrafficDetectionTrend} 相同数据源与时间点规则；结果铺平，一行一个区域的一个时间点）
     */
    public List<TrafficDetectionAreaPersonTrendStatVO> getTrafficDetectionAreaPersonTrend(TrafficDetectionAreaStatQueryParam param) {
        var areas = resolveStatAreas(param);
        if (CollectionUtils.isEmpty(areas)) {
            return Collections.emptyList();
        }
        Map<Long, Map<String, Double>> areaPeriodTotals = new HashMap<>();
        var deviceIdToAreaId = new HashMap<Long, Long>();
        for (var area : areas) {
            if (CollectionUtils.isEmpty(area.getDevices())) {
                continue;
            }
            var areaId = area.getId();
            for (var d : area.getDevices()) {
                deviceIdToAreaId.put(d.getDeviceId(), areaId);
            }
        }
        if (CollectionUtils.isNotEmpty(deviceIdToAreaId.keySet())) {
            var deviceIds = deviceIdToAreaId.keySet();
            var step = 60;
            DateUnitUtils.handleDateParam(param);
            if (!"TODAY".equals(param.getType())) {
                step = 60 * 24;
            }
            var deviceTSMap = trafficDetectionAreaDeviceAppService.getDeviceTSHistoriesMap(TrafficDetectionPropCodes.CURRENT_PEOPLE_NUM, param.getPeriod(), deviceIds, step);
            for (var entry : deviceTSMap.entrySet()) {
                long deviceId;
                try {
                    deviceId = Long.parseLong(entry.getKey());
                } catch (NumberFormatException e) {
                    continue;
                }
                var areaId = deviceIdToAreaId.get(deviceId);
                if (areaId == null) {
                    continue;
                }
                var periodToSum = sumByPeriodFromDeviceTsForAreaPersonTrend(entry.getValue(), param.getType());
                var areaMap = areaPeriodTotals.computeIfAbsent(areaId, k -> new HashMap<>());
                periodToSum.forEach((period, v) -> areaMap.merge(period, v, Double::sum));
            }
        }
        return buildFlatAreaPersonTrendRows(areas, areaPeriodTotals);
    }

    /**
     * 区域人流量趋势专用：按统计 {@code type} 决定展示格式，与全量趋势接口中 {@link #sumByPeriodFromDeviceTs} 使用的 {@code viewDateString} 独立。
     * <ul>
     *     <li>今天（TODAY、HOUR）：{@code yyyy-MM-dd HH:mm:ss}</li>
     *     <li>本月等按日（THIS_MONTH、DAY、WEEK、THIS_WEEK、MONTH）：{@code yyyy-MM-dd}</li>
     *     <li>今年等按月（THIS_YEAR、YEAR、RECENT_THREE_MONTH、RECENT_HALF_YEAR）：{@code yyyy-MM}</li>
     *     <li>其它：{@code yyyy-MM-dd HH:mm:ss}</li>
     * </ul>
     */
    private String formatAreaPersonTrendPeriod(Long timestampMillis, String statisticsType) {
        if (timestampMillis == null) {
            return null;
        }
        var dateTime = DateUtils.parse(timestampMillis);
        var t = statisticsType == null ? "" : statisticsType;
        if ("TODAY".equals(t) || "HOUR".equals(t)) {
            return DateUtils.format(dateTime, DateUtils.DEFAULT_DATETIME_FORMATTER);
        }
        if ("THIS_MONTH".equals(t) || "MONTH".equals(t) || "DAY".equals(t) || "WEEK".equals(t) || "THIS_WEEK".equals(t)) {
            return DateUtils.format(dateTime, DateUtils.DEFAULT_DATE_FORMATTER);
        }
        if ("THIS_YEAR".equals(t) || "YEAR".equals(t) || "RECENT_THREE_MONTH".equals(t) || "RECENT_HALF_YEAR".equals(t)) {
            return DateUtils.format(dateTime, DateUtils.DEFAULT_MONTH_FORMATTER);
        }
        return DateUtils.format(dateTime, DateUtils.DEFAULT_DATETIME_FORMATTER);
    }

    private Map<String, Double> sumByPeriodFromDeviceTsForAreaPersonTrend(DeviceTSDTO deviceTs, String statisticsType) {
        if (deviceTs == null || CollectionUtils.isEmpty(deviceTs.getPropertyTsBoList())) {
            return Collections.emptyMap();
        }
        return deviceTs.getPropertyTsBoList().stream()
                .filter(propertyTs -> propertyTs.getValueTsBoList() != null && !propertyTs.getValueTsBoList().isEmpty())
                .flatMap(propertyTs -> propertyTs.getValueTsBoList().stream())
                .map(valueTs -> {
                    var period = formatAreaPersonTrendPeriod(valueTs.getTs(), statisticsType);
                    var val = parseDoubleValue(valueTs.getValue());
                    if (period == null || val == null) {
                        return null;
                    }
                    return new AbstractMap.SimpleEntry<>(period, val);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)));
    }

    private List<TrafficDetectionArea> resolveStatAreas(TrafficDetectionAreaStatQueryParam param) {
        var trafficDetectionAreas = trafficDetectionAreaRepository.findList(null, null);
        if (CollectionUtils.isEmpty(trafficDetectionAreas)) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isNotEmpty(param.getTrafficDetectionAreaIds())) {
            return trafficDetectionAreas.stream()
                    .filter(a -> param.getTrafficDetectionAreaIds().contains(a.getId()))
                    .sorted(Comparator.comparing(TrafficDetectionArea::getId))
                    .collect(Collectors.toList());
        }
        return trafficDetectionAreas.stream()
                .sorted(Comparator.comparing(TrafficDetectionArea::getId))
                .collect(Collectors.toList());
    }

    private List<TrafficDetectionAreaPersonTrendStatVO> buildFlatAreaPersonTrendRows(List<TrafficDetectionArea> areas,
                                                                                     Map<Long, Map<String, Double>> areaPeriodTotals
                                                                                    ) {
        return areas.stream()
                .flatMap(area -> {
                    var periodTotals = areaPeriodTotals.getOrDefault(area.getId(), Collections.emptyMap());
                    return periodTotals.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .map(e -> new TrafficDetectionAreaPersonTrendStatVO(
                                    area.getId(),
                                    area.getName(),
                                    area.getModelArea(),
                                    area.getRemarks(),
                                    e.getKey(),
                                    e.getValue().intValue()));
                })
                .collect(Collectors.toList());
    }

    /**
     * 人流统计汇总，数据源为小时采集记录。
     */
    public TrafficDetectionPersonFlowSummaryVO queryPersonFlowSummary(TrafficDetectionAreaStatQueryParam param) {
        var statContext = buildPersonFlowStatContext(param);
        var deviceIds = listStatDeviceIds(param);
        var currentRecords = trafficDetectionHourlyPersonFlowRepository.findByDeviceIdsAndRecordTimeBetween(
                deviceIds, statContext.period().getBegin(), statContext.period().getEnd());
        var currentBucketMap = aggregateByBucket(currentRecords, statContext.granularity());
        var total = currentBucketMap.values().stream().mapToInt(Integer::intValue).sum();

        var previousRecords = trafficDetectionHourlyPersonFlowRepository.findByDeviceIdsAndRecordTimeBetween(
                deviceIds, statContext.previousPeriod().getBegin(), statContext.previousPeriod().getEnd());
        var previousTotal = previousRecords.stream().mapToInt(TrafficDetectionHourlyPersonFlowPO::getPersonCount).sum();
        var yoyRecords = trafficDetectionHourlyPersonFlowRepository.findByDeviceIdsAndRecordTimeBetween(
                deviceIds, statContext.yoyPeriod().getBegin(), statContext.yoyPeriod().getEnd());
        var yoyTotal = yoyRecords.stream().mapToInt(TrafficDetectionHourlyPersonFlowPO::getPersonCount).sum();

        var filledBucketMap = fillBuckets(statContext.period(), statContext.granularity(), currentBucketMap);
        var peakEntry = findPeak(filledBucketMap);
        return new TrafficDetectionPersonFlowSummaryVO(
                total,
                calculateRate(total, previousTotal),
                calculateRate(total, yoyTotal),
                calculateAverage(total, filledBucketMap.size()),
                peakEntry.getValue(),
                peakEntry.getKey()
        );
    }

    /**
     * 累计人次趋势，数据源为小时采集记录。
     */
    public List<TrafficDetectionPersonFlowTrendVO> getPersonFlowTrend(TrafficDetectionAreaStatQueryParam param) {
        var statContext = buildPersonFlowStatContext(param);
        var deviceIds = listStatDeviceIds(param);
        var records = trafficDetectionHourlyPersonFlowRepository.findByDeviceIdsAndRecordTimeBetween(
                deviceIds, statContext.period().getBegin(), statContext.period().getEnd());
        var bucketMap = fillBuckets(statContext.period(), statContext.granularity(), aggregateByBucket(records, statContext.granularity()));
        return bucketMap.entrySet().stream()
                .map(entry -> new TrafficDetectionPersonFlowTrendVO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 人流详情趋势，按设备历史属性值以 10 分钟粒度求和。
     */
    public List<TrafficDetectionPersonFlowTrendVO> getPersonFlowDetailTrend(TrafficDetectionAreaStatQueryParam param) {
        return listPersonFlowDetail(param).stream()
                .collect(Collectors.groupingBy(TrafficDetectionPersonFlowDetailVO::getReportTime,
                                               Collectors.summingInt(TrafficDetectionPersonFlowDetailVO::getTotalPersonNumber)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new TrafficDetectionPersonFlowTrendVO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 人流详情分页列表，按区域与 10 分钟上报时间铺平。
     */
    public Pagination<TrafficDetectionPersonFlowDetailVO> pagePersonFlowDetail(TrafficDetectionAreaStatQueryParam param) {
        var rows = listPersonFlowDetail(param);
        if (CollectionUtils.isEmpty(rows)) {
            return Pagination.empty();
        }
        var pageNumber = param == null ? 1 : param.getPageNumber();
        var pageSize = param == null ? 10 : param.getPageSize();
        var total = (long) rows.size();
        var pages = (long) Math.ceil((double) total / pageSize);
        return Pagination.<TrafficDetectionPersonFlowDetailVO>builder()
                .total(total)
                .pages(pages)
                .records(TrafficDetectionAreaStatQueryParam.list(rows, pageNumber, pageSize))
                .build();
    }

    /**
     * 人流详情列表，按区域与 10 分钟上报时间铺平。
     */
    public List<TrafficDetectionPersonFlowDetailVO> listPersonFlowDetail(TrafficDetectionAreaStatQueryParam param) {
        var areas = resolveStatAreas(param);
        if (CollectionUtils.isEmpty(areas)) {
            return Collections.emptyList();
        }
        var deviceIdToAreaId = new HashMap<Long, Long>();
        for (var area : areas) {
            if (CollectionUtils.isEmpty(area.getDevices())) {
                continue;
            }
            for (var device : area.getDevices()) {
                deviceIdToAreaId.put(device.getDeviceId(), area.getId());
            }
        }
        if (CollectionUtils.isEmpty(deviceIdToAreaId.keySet())) {
            return Collections.emptyList();
        }

        var period = resolvePersonFlowDetailPeriod(param);
        var deviceTSMap = trafficDetectionAreaDeviceAppService.getDeviceTSHistoriesMap(TrafficDetectionPropCodes.CURRENT_PEOPLE_NUM,
                                                                                       period,
                                                                                       deviceIdToAreaId.keySet(),
                                                                                       PERSON_FLOW_DETAIL_STEP_MINUTES,
                                                                                       "avg");
        if (deviceTSMap == null || deviceTSMap.isEmpty()) {
            return Collections.emptyList();
        }

        var areaReportTotals = new HashMap<Long, Map<String, Double>>();
        for (var entry : deviceTSMap.entrySet()) {
            long deviceId;
            try {
                deviceId = Long.parseLong(entry.getKey());
            } catch (NumberFormatException e) {
                continue;
            }
            var areaId = deviceIdToAreaId.get(deviceId);
            if (areaId == null) {
                continue;
            }
            var reportTimeToSum = sumByReportTimeFromDeviceTs(entry.getValue());
            var areaMap = areaReportTotals.computeIfAbsent(areaId, k -> new HashMap<>());
            reportTimeToSum.forEach((reportTime, value) -> areaMap.merge(reportTime, value, Double::sum));
        }

        return buildPersonFlowDetailRows(areas, areaReportTotals);
    }

    /**
     * 区域人流排行，数据源为小时采集记录。
     */
    public Pagination<TrafficDetectionAreaPersonFlowRankVO> getAreaPersonFlowRank(TrafficDetectionAreaStatQueryParam param) {
        var statContext = buildPersonFlowStatContext(param);
        return trafficDetectionHourlyPersonFlowRepository.findAreaPersonFlowRankPage(param,
                                                                                     statContext.period(),
                                                                                     statContext.granularity(),
                                                                                     countBuckets(statContext.period(), statContext.granularity()),
                                                                                     formatBucket(statContext.period()
                                                                                                          .getBegin(), statContext.granularity()));
    }

    /**
     * 区域人流排行全量列表，供导出使用。
     */
    public List<TrafficDetectionAreaPersonFlowRankVO> listAreaPersonFlowRank(TrafficDetectionAreaStatQueryParam param) {
        var statContext = buildPersonFlowStatContext(param);
        return trafficDetectionHourlyPersonFlowRepository.findAreaPersonFlowRankList(param,
                                                                                     statContext.period(),
                                                                                     statContext.granularity(),
                                                                                     countBuckets(statContext.period(), statContext.granularity()),
                                                                                     formatBucket(statContext.period()
                                                                                                          .getBegin(), statContext.granularity()));
    }

    private Set<Long> listStatDeviceIds(TrafficDetectionAreaStatQueryParam param) {
        var areaIds = param == null ? null : param.getTrafficDetectionAreaIds();
        return trafficDetectionAreaDeviceRepository.findList(areaIds).stream()
                .map(TrafficDetectionAreaDevice::getDeviceId)
                .collect(Collectors.toSet());
    }

    private PersonFlowStatContext buildPersonFlowStatContext(TrafficDetectionAreaStatQueryParam param) {
        var granularity = resolveGranularity(param);
        var period = resolvePeriod(param, granularity);
        return new PersonFlowStatContext(granularity,
                                         period,
                                         resolvePreviousPeriod(period, granularity),
                                         new Period(period.getBegin().minusYears(1), period.getEnd().minusYears(1)));
    }

    private String resolveGranularity(TrafficDetectionAreaStatQueryParam param) {
        if (param != null && param.getGranularity() != null && !param.getGranularity().isBlank()) {
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

    private Period resolvePeriod(TrafficDetectionAreaStatQueryParam param, String granularity) {
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

    private Period resolvePersonFlowDetailPeriod(TrafficDetectionAreaStatQueryParam param) {
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
        var duration = java.time.Duration.between(period.getBegin(), period.getEnd());
        return new Period(period.getBegin().minus(duration), period.getEnd().minus(duration));
    }

    private Map<String, Integer> aggregateByBucket(List<TrafficDetectionHourlyPersonFlowPO> records, String granularity) {
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyMap();
        }
        return records.stream()
                .filter(record -> record.getRecordTime() != null)
                .collect(Collectors.groupingBy(record -> formatBucket(record.getRecordTime(), granularity),
                                               Collectors.summingInt(TrafficDetectionHourlyPersonFlowPO::getPersonCount)));
    }

    private LinkedHashMap<String, Integer> fillBuckets(Period period, String granularity, Map<String, Integer> bucketMap) {
        var result = new LinkedHashMap<String, Integer>();
        var cursor = truncate(period.getBegin(), granularity);
        var end = period.getEnd();
        while (cursor.isBefore(end)) {
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

    private Integer countBuckets(Period period, String granularity) {
        var count = 0;
        var cursor = truncate(period.getBegin(), granularity);
        var end = period.getEnd();
        while (cursor.isBefore(end)) {
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

    private Integer calculateAverage(Integer total, Integer bucketCount) {
        if (bucketCount == null || bucketCount <= 0) {
            return 0;
        }
        return BigDecimal.valueOf(total)
                .divide(BigDecimal.valueOf(bucketCount), 0, RoundingMode.HALF_UP)
                .intValue();
    }

    private BigDecimal calculateRate(Integer current, Integer compared) {
        if (compared == null || compared <= 0) {
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

    private record PersonFlowStatContext(String granularity, Period period, Period previousPeriod, Period yoyPeriod) {
    }

    public List<TrafficDetectionTrendStatVO> getTrafficDetectionTrend(TrafficDetectionAreaStatQueryParam param) {
        var trafficDetectionAreaDevices = trafficDetectionAreaDeviceRepository.findList(param.getTrafficDetectionAreaIds());
        if (CollectionUtils.isEmpty(trafficDetectionAreaDevices)) {
            return Collections.emptyList();
        }
        var step = 60;
        DateUnitUtils.handleDateParam(param);
        if (!"TODAY".equals(param.getType())) {
            step = 60 * 24;
        }
        var deviceTSMap = trafficDetectionAreaDeviceAppService.getDeviceTSHistoriesMap(TrafficDetectionPropCodes.CURRENT_PEOPLE_NUM, param.getPeriod(), trafficDetectionAreaDevices
                .stream()
                .map(TrafficDetectionAreaDevice::getDeviceId)
                .collect(Collectors.toSet()), step);
        var hourlySumMap = deviceTSMap.entrySet().stream()
                .flatMap(entry -> sumByPeriodFromDeviceTs(entry.getValue(), param.getViewDateString()).entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)));
        return hourlySumMap.entrySet().stream()
                .map(entry -> {
                    TrafficDetectionTrendStatVO vo = new TrafficDetectionTrendStatVO();
                    vo.setPeriod(entry.getKey());
                    vo.setTotalPersonNumber(entry.getValue().intValue());
                    return vo;
                })
                .sorted(Comparator.comparing(TrafficDetectionTrendStatVO::getPeriod))
                .collect(Collectors.toList());
    }

    private Map<String, Double> sumByPeriodFromDeviceTs(DeviceTSDTO deviceTs, String viewDateString) {
        if (deviceTs == null || CollectionUtils.isEmpty(deviceTs.getPropertyTsBoList())) {
            return Collections.emptyMap();
        }
        return deviceTs.getPropertyTsBoList().stream()
                .filter(propertyTs -> propertyTs.getValueTsBoList() != null && !propertyTs.getValueTsBoList().isEmpty())
                .flatMap(propertyTs -> propertyTs.getValueTsBoList().stream())
                .map(valueTs -> new AbstractMap.SimpleEntry<>(
                        format(valueTs.getTs(), viewDateString),
                        parseDoubleValue(valueTs.getValue())
                ))
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)));
    }

    private String format(Long timestamp, String pattern) {
        if (timestamp == null) {
            return "00";
        }
        return DateUtils.format(DateUtils.parse(timestamp), pattern);
    }

    private Map<String, Double> sumByReportTimeFromDeviceTs(DeviceTSDTO deviceTs) {
        if (deviceTs == null || CollectionUtils.isEmpty(deviceTs.getPropertyTsBoList())) {
            return Collections.emptyMap();
        }
        return deviceTs.getPropertyTsBoList().stream()
                .filter(propertyTs -> propertyTs.getValueTsBoList() != null && !propertyTs.getValueTsBoList().isEmpty())
                .flatMap(propertyTs -> propertyTs.getValueTsBoList().stream())
                .map(valueTs -> {
                    var reportTime = formatReportTime(valueTs.getTs());
                    var val = parseDoubleValue(valueTs.getValue());
                    if (reportTime == null || val == null) {
                        return null;
                    }
                    return new AbstractMap.SimpleEntry<>(reportTime, val);
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

    private List<TrafficDetectionPersonFlowDetailVO> buildPersonFlowDetailRows(List<TrafficDetectionArea> areas,
                                                                               Map<Long, Map<String, Double>> areaReportTotals
                                                                              ) {
        return areas.stream()
                .flatMap(area -> {
                    var reportTotals = areaReportTotals.getOrDefault(area.getId(), Collections.emptyMap());
                    return reportTotals.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .map(entry -> new TrafficDetectionPersonFlowDetailVO(area.getId(),
                                                                                 area.getName(),
                                                                                 area.getModelArea(),
                                                                                 area.getRemarks(),
                                                                                 entry.getValue().intValue(),
                                                                                 entry.getKey()));
                })
                .collect(Collectors.toList());
    }

    private Double parseDoubleValue(Object value) {
        if (value == null) {
            return null;
        }
        try {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else {
                return Double.parseDouble(value.toString());
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
