package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.energymanagement.server.common.CommonAreaData;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.AreaProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.RouteProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AreaProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyGate;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProjectItem;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.RouteProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.BindTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.CalculationIndicatorsEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.ChargeTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.DateEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.TimePeriodQueryEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.job.EnergyJob;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.DosageAnalysisParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.CarbonQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyAnalysisQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.TreeIds;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeConfigService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyGateService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyProjectItemService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AreaProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AreaStatisticsVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.BindTypeVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataProjectVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataTimeFlagVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DosageAnalysisDistributionVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DosageAnalysisVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyAnalysisMenuVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeConfigVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyConsumptionDetailsVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.FPGDataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.InfluxdbAggregationVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.InstrumentTypeEnumVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.PeriodVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TimeDataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendResultVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTreeEntity;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings({ "OptionalOfRedundantMethod", "SelfAssignment" })
public class EnergyServiceImpl implements EnergyService {

    private final InfluxDBClient influxdbClient;
    private final AreaProjectMapper areaProjectMapper;
    private final EnergyProjectMapper energyProjectMapper;
    private final RouteProjectMapper routeProjectMapper;
    private final EnergyProjectItemService energyProjectItemService;
    private final EnergyGateService energyGateService;
    private final EnergyChargeConfigService energyChargeConfigService;
    private final MeterDeviceMapper meterDeviceMapper;
    private static BigDecimal carbonIndex = new BigDecimal("3.2");

    @Override
    public Map<String, Object> getComprehensiveEnergy() {
        HashMap<String, Object> remap = new HashMap<>();
        //获取当前时间
        LocalDateTime date = LocalDateTime.now();
        //本月数据
        LocalDateTime begin = DateUtils.offsetDay(date, -30);
        TimeDataVo timePeriod = getTimePeriod(begin, date);
        Map<String, Object> energyMap = getEnergyMap(timePeriod.getBegin(), timePeriod.getEnd());
        //上月数据
        LocalDateTime offsetLastEnd = DateUtils.offsetMonth(date, -1);
        LocalDateTime offsetLast = DateUtils.offsetDay(offsetLastEnd, -30);
        TimeDataVo timeOldPeriod = getTimePeriod(offsetLast, offsetLastEnd);
        Map<String, Object> energyLastMap = getEnergyMap(timeOldPeriod.getBegin(), timeOldPeriod.getEnd());
        remap.put("newData", energyMap);
        remap.put("oldData", energyLastMap);
        return remap;
    }

    @Override
    public Map<String, Object> getEnergyTrend() {
        HashMap<String, Object> remap = new HashMap<>();
        LocalDateTime date = LocalDateTime.now();
        QueryApi queryApi = influxdbClient.getQueryApi();
        List<DataTimeFlagVo> query = new ArrayList<>();
        String instrumentTypes = getInstrumentTypes();
        LocalDateTime begin = DateUtils.offsetDay(date, -30);
        TimeDataVo timePeriod = getTimePeriod(begin, date);
        String group = " |> group(columns: [\"_time\",\"instrumentType\"])\n";
        String flux = overviewTrendFlux(timePeriod, group, instrumentTypes);
        List<DataVo> query1 = queryApi.query(flux, DataVo.class);
        Map<Instant, List<DataVo>> collect = query1.stream().collect(Collectors.groupingBy(DataVo::getTime));
        for (int i = 0; i <= 30; i++) {
            LocalDateTime offset = DateUtils.offsetDay(date, i - 30);
            LocalDateTime parse = DateUtils.parse(DateUtils.formatDate(offset, "yyyy-MM-dd 00:00:00"));
            Instant instant = parse.atZone(ZoneId.systemDefault()).toInstant();
            List<DataVo> dataVos = collect.get(instant);
            BigDecimal value = new BigDecimal("0.00");
            if (ListUtils.nonEmpty(dataVos)) {
                for (DataVo dataVo : dataVos) {
                    BigDecimal indicators = new BigDecimal(CalculationIndicatorsEnum.getIndicatorsEnum(dataVo.getInstrumentType()));
                    BigDecimal singleValue = dataVo.getValue().multiply(indicators).multiply(carbonIndex).setScale(2, RoundingMode.HALF_UP);
                    value = value.add(singleValue);
                }

            }
            DataTimeFlagVo dataTimeFlagVo = new DataTimeFlagVo();
            dataTimeFlagVo.setTimeFlag(DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT));
            dataTimeFlagVo.setValue(value);
            query.add(dataTimeFlagVo);
        }
        query.remove(0);
        remap.put("carbonEmissions", query);

        String instrumentTypeGroup = "|> group(columns: [\"instrumentType\"])\n";
        String s = overviewTrendFlux(timePeriod, instrumentTypeGroup, instrumentTypes);
        List<DataVo> dataVos = queryApi.query(s, DataVo.class);
        if (dataVos.size() > 0) {
            for (DataVo dataVo : dataVos) {
                BigDecimal indicators = new BigDecimal(CalculationIndicatorsEnum.getIndicatorsEnum(dataVo.getInstrumentType()));
                dataVo.setValue(dataVo.getValue().multiply(indicators).multiply(carbonIndex));
            }

        }
        remap.put("categoryStatistics", dataVos);
        return remap;
    }

    @Override
    public Map<String, List<DataTimeFlagVo>> energyConsumptionTrend() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        LocalDateTime date = LocalDateTime.now();
        QueryApi queryApi = influxdbClient.getQueryApi();

        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                               .eq(EnergyGate::getOpen, true)
                                                               .ne(EnergyGate::getEnergyType, InstrumentTypeEnum.ENERGY_METER.getCode()));
        List<String> collect = list.stream().map(s -> {
            return s.getEnergyType();
        }).collect(Collectors.toList());

        List<DataTimeFlagVo> query = new ArrayList<>();
        for (String c : collect) {

            LocalDateTime offset1 = DateUtils.offsetMinute(DateUtils.beginOfMonth(date), -8 * 60);
            LocalDateTime eTime = DateUtils.endOfDay(date);
            String s = DateUtils.formatDate(offset1, DEFAULT_DATE_FORMAT) + "T" + DateUtils.formatDate(offset1, "HH:mm:ss") + "Z";
            String s1 = DateUtils.formatDate(eTime, DEFAULT_DATE_FORMAT) + "T23:59:59Z";
            String flux = "from(bucket: \"energy\")\n" +
                          "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                          "  |> filter(fn: (r) => r[\"_measurement\"] == \"deviceDay\")\n" +
                          "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.instrumentType==\"" + c + "\")\n" +
                          "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                          "  |> group(columns: [\"_time\",\"instrumentType\"])\n" +
                          "  |> sum(column: \"value\")";
            List<DataVo> dataVos = queryApi.query(flux, DataVo.class);
            Map<Instant, List<DataVo>> collectInstant = dataVos.stream().collect(Collectors.groupingBy(DataVo::getTime));
            List<DataTimeFlagVo> objects = new ArrayList<>();
            for (int i = 0; i <= date.getDayOfMonth(); i++) {
                LocalDateTime offset = DateUtils.offsetDay(date, -(date.getDayOfMonth() - i));
                LocalDateTime parse = DateUtils.parse(DateUtils.formatDate(offset, "yyyy-MM-dd 00:00:00"));
                Instant instant = parse.atZone(ZoneId.systemDefault()).toInstant();
                List<DataVo> dataVos1 = collectInstant.get(instant);
                DataTimeFlagVo dataTimeFlagVo = new DataTimeFlagVo();
                dataTimeFlagVo.setTimeFlag(String.valueOf(i));
                BigDecimal value = new BigDecimal("0.00");
                if (ListUtils.nonEmpty(dataVos1)) {
                    BigDecimal indicators = new BigDecimal(CalculationIndicatorsEnum.getIndicatorsEnum(dataVos.get(0).getInstrumentType()));
                    value = value.add(dataVos1.get(0).getValue().multiply(indicators).multiply(carbonIndex)
                                              .setScale(2, RoundingMode.HALF_UP));
                }
                dataTimeFlagVo.setValue(value);
                dataTimeFlagVo.setInstrumentType(c);
                objects.add(dataTimeFlagVo);
            }
            objects.remove(0);
            query.addAll(objects);

        }
        Map<String, List<DataTimeFlagVo>> collect1 = query.stream().collect(Collectors.groupingBy(DataTimeFlagVo::getInstrumentType));
        return collect1;
    }

    @Override
    public List<AreaStatisticsVo> electricityConsumptionRanking() {

        LocalDateTime date = LocalDateTime.now();
        LocalDateTime begin = DateUtils.offsetDay(date, -30);
        TimeDataVo timePeriod = getTimePeriod(begin, date);
        String areaData = CommonAreaData.getAreaData(InstrumentTypeEnum.ELECTRICITY_METER.getCode());
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + timePeriod.getBegin() + ", stop: " + timePeriod.getEnd() + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"areaDay\")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + SessionContextHolder.getRequester().getTenant() + "\"" + areaData + ")\n" +
                      "  |> group(columns: [\"areaId\"])\n" +
                      "  |> sum(column: \"value\")\n" +
                      "  |> group()\n" +
                      "  |> sort(columns: [\"value\"],desc: true)\n" +
                      "  |> limit(n: 10)";
        QueryApi queryApi = influxdbClient.getQueryApi();
        List<AreaStatisticsVo> query = queryApi.query(flux, AreaStatisticsVo.class);
        if (ListUtils.nonEmpty(query)) {
            List<AreaProject> areaProjects = areaProjectMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                                  .eq(AreaProject::getInstrumentType, InstrumentTypeEnum.ELECTRICITY_METER.getCode()));
            Map<Long, AreaProject> collect = areaProjects.stream().collect(Collectors.toMap(AreaProject::getId, a -> a));
            for (AreaStatisticsVo areaStatisticsVo : query) {
                AreaProject areaProject = collect.get(Long.valueOf(areaStatisticsVo.getAreaId()));
                areaStatisticsVo.setAreaName(Objects.isNull(areaProject) ? "" : areaProject.getAreaName());
            }
        }
        return query;
    }

    @Override
    public Map<String, Object> energyDirection() {
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime begin = DateUtils.offsetDay(date, -30);
        TimeDataVo timePeriod = getTimePeriod(begin, date);
        Map<String, Object> remap = new HashMap<>();
        QueryApi queryApi = influxdbClient.getQueryApi();
        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                               .eq(EnergyGate::getOpen, true)
                                                               .ne(EnergyGate::getEnergyType, InstrumentTypeEnum.ENERGY_METER.getCode()));
        List<String> collect = list.stream().map(s -> {
            return s.getEnergyType();
        }).collect(Collectors.toList());
        remap.put("node", collect);
        if (ListUtils.nonEmpty(collect)) {
            List<AreaProject> areaProjects = areaProjectMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                                  .in(AreaProject::getInstrumentType, collect));
            List<AreaStatisticsVo> objects = new ArrayList<>();
            if (ListUtils.nonEmpty(areaProjects)) {
                List<AreaStatisticsVo> areaStatisticsVos = ObjectUtils.copying(areaProjects, AreaStatisticsVo.class);
                Map<String, List<AreaStatisticsVo>> collect1 = areaStatisticsVos.stream()
                        .collect(Collectors.groupingBy(AreaStatisticsVo::getInstrumentType));
                for (String s : collect1.keySet()) {
                    List<AreaStatisticsVo> areaStatisticsVos1 = collect1.get(s);
                    Map<Long, List<AreaStatisticsVo>> collect2 = areaStatisticsVos1.stream()
                            .collect(Collectors.groupingBy(AreaStatisticsVo::getId));

                    String areaIds = "";
                    if (ListUtils.nonEmpty(areaStatisticsVos1)) {
                        String ids = areaStatisticsVos1.stream().map(d -> "r.areaId==\"" + d.getId().toString() + "\"")
                                .collect(Collectors.joining(" or "));
                        areaIds = "and (" + ids + ")";
                    } else {
                        areaIds = " and ( r.areaId==\"aaaaa\")";
                    }
                    String flux = "from(bucket: \"energy\")\n" +
                                  "  |> range(start: " + timePeriod.getBegin() + ", stop: " + timePeriod.getEnd() + ")\n" +
                                  "  |> filter(fn: (r) => r[\"_measurement\"] == \"areaDay\")\n" +
                                  "  |> filter(fn: (r) => r.instrumentType==\"" + s + "\"" + areaIds + ")\n" +
                                  "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                                  "  |> group(columns:[\"areaId\"])\n" +
                                  "  |> sum(column: \"value\")\n" +
                                  "  |> group()\n" +
                                  "  |> sort(columns: [\"value\"], desc: true)\n" +
                                  "  |> limit(n: 10)";

                    List<AreaStatisticsVo> query = queryApi.query(flux, AreaStatisticsVo.class);
                    if (ListUtils.nonEmpty(query)) {
                        for (AreaStatisticsVo areaStatisticsVo : query) {
                            AreaStatisticsVo areaStatisticsOutVo = new AreaStatisticsVo();
                            List<AreaStatisticsVo> areaStatisticsVos2 = collect2.get(Long.valueOf(areaStatisticsVo.getAreaId()));
                            areaStatisticsOutVo.setId(areaStatisticsVos2.get(0).getId());
                            areaStatisticsOutVo.setAreaName(areaStatisticsVos2.get(0).getAreaName());
                            areaStatisticsOutVo.setValue(areaStatisticsVo.getValue());
                            areaStatisticsOutVo.setParentId(areaStatisticsVos2.get(0).getParentId());
                            areaStatisticsOutVo.setInstrumentType(areaStatisticsVos2.get(0).getInstrumentType());
                            areaStatisticsOutVo.setLevel(areaStatisticsVos2.get(0).getLevel());
                            objects.add(areaStatisticsOutVo);
                        }
                    }
                }

                //数据过滤
                List<AreaStatisticsVo> collect2 = objects.stream().filter(s -> s.getValue().compareTo(BigDecimal.ZERO) == 1)
                        .collect(Collectors.toList());

                List<AreaProject> areaProjectsParent = areaProjectMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                                            .isNull(BasicEnhancedTreeEntity::getParentId));
                List<AreaStatisticsVo> copying = ObjectUtils.copying(areaProjectsParent, AreaStatisticsVo.class);
                collect2.addAll(copying);
                List<AreaStatisticsVo> collect3 = collect2.stream()
                        .filter(s -> Objects.nonNull(s.getValue()) && StringUtils.isEmpty(s.getParentId())).collect(Collectors.toList());
                //组装数据
                remap.put("areaStatisticsVos", collect3);

            } else {
                List<AreaStatisticsVo> areaStatisticsVos = new ArrayList<>();
                //组装数据
                remap.put("areaStatisticsVos", areaStatisticsVos);
            }
        }

        return remap;
    }

    @Override
    public Map<String, Object> carbonEmissionStatistics(Boolean isCarbon) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Map<String, Object> remap = new HashMap<>();
        String instrumentTypes = getInstrumentTypes();
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime dateTime = DateUtils.beginOfDay(date);
        TimeDataVo timePeriod = getTimePeriod(dateTime, date);
        BigDecimal data = getData(timePeriod.getBegin(), timePeriod.getEnd(), tenant, instrumentTypes, isCarbon);
        //今日量
        BigDecimal tnum = new BigDecimal("0.00");
        tnum = tnum.add(data);
        remap.put("tnum", tnum.setScale(2, RoundingMode.HALF_UP));
        LocalDateTime yDay = DateUtils.offsetDay(date, -1);
        LocalDateTime ybegin = DateUtils.beginOfDay(yDay);
        TimeDataVo ytimePeriod = getTimePeriod(ybegin, yDay);
        BigDecimal data1 = getData(ytimePeriod.getBegin(), ytimePeriod.getEnd(), tenant, instrumentTypes, isCarbon);
        BigDecimal ytnum = new BigDecimal("0.00");
        ytnum = ytnum.add(data1);
        //环比
        BigDecimal dyoy = new BigDecimal("0.00");
        if (ytnum.compareTo(new BigDecimal("0.00")) == 0) {
            dyoy = dyoy;
        } else {
            dyoy = dyoy.add(tnum.subtract(ytnum).divide(ytnum, 2, RoundingMode.HALF_UP));
        }
        remap.put("dyoy", dyoy);
        //同比时间
        LocalDateTime offset = DateUtils.offsetMonth(ybegin, -1);
        LocalDateTime offset1 = DateUtils.offsetMonth(date, -1);
        TimeDataVo timePeriod1 = getTimePeriod(offset, offset1);
        BigDecimal data2 = getData(timePeriod1.getBegin(), timePeriod1.getEnd(), tenant, instrumentTypes, isCarbon);
        BigDecimal tqoqnum = new BigDecimal("0.00");
        BigDecimal tqoq = new BigDecimal("0.00");
        tqoqnum = tqoqnum.add(data2);
        if (tqoq.compareTo(new BigDecimal("0.00")) == 0) {
            tqoq = tqoq;
        } else {
            tqoq = tqoq.add(tnum.subtract(tqoqnum).divide(tqoqnum, 2, RoundingMode.HALF_UP));
        }
        //同比
        remap.put("tqoq", tqoq);

        //今月量
        LocalDateTime ms = DateUtils.beginOfMonth(date);
        TimeDataVo timePeriod2 = getTimePeriod(ms, date);
        BigDecimal data3 = getData(timePeriod2.getBegin(), timePeriod2.getEnd(), tenant, instrumentTypes, isCarbon);
        BigDecimal mnum = new BigDecimal("0.00");
        mnum = mnum.add(data3);
        remap.put("mnum", mnum.setScale(2, RoundingMode.HALF_UP));
        LocalDateTime offset3 = DateUtils.offsetMonth(date, -1);
        LocalDateTime time = DateUtils.beginOfMonth(offset3);
        TimeDataVo timePeriod3 = getTimePeriod(time, offset3);
        BigDecimal data4 = getData(timePeriod3.getBegin(), timePeriod3.getEnd(), tenant, instrumentTypes, isCarbon);
        BigDecimal ymnum = new BigDecimal("0.00");
        ymnum = ymnum.add(data4);
        //月环比
        BigDecimal myoy = new BigDecimal("0.00");
        if (ymnum.compareTo(new BigDecimal("0.00")) == 0) {
            myoy = myoy;
        } else {
            myoy = myoy.add(mnum.subtract(ymnum).divide(ymnum, 2, RoundingMode.HALF_UP));
        }
        remap.put("myoy", myoy);
        //月同比
        LocalDateTime offset4 = DateUtils.offsetMonth(date, -12);
        LocalDateTime offset5 = DateUtils.offsetMonth(ms, -12);
        TimeDataVo timePeriod4 = getTimePeriod(offset5, offset4);
        BigDecimal data5 = getData(timePeriod4.getBegin(), timePeriod4.getEnd(), tenant, instrumentTypes, isCarbon);
        BigDecimal mqoqnum = new BigDecimal("0.00");
        BigDecimal mqoq = new BigDecimal("0.00");
        mqoqnum = mqoqnum.add(data5);
        if (mqoqnum.compareTo(new BigDecimal("0.00")) == 0) {
            mqoq = mqoq;
        } else {
            mqoq = tqoq.add(mnum.subtract(mqoqnum).divide(mqoqnum, 2, RoundingMode.HALF_UP));
        }
        //月同比
        remap.put("mqoq", mqoq);

        //今年量
        LocalDateTime ys = DateUtils.beginOfYear(date);
        TimeDataVo timePeriod6 = getTimePeriod(ys, date);
        BigDecimal data6 = getData(timePeriod6.getBegin(), timePeriod6.getEnd(), tenant, instrumentTypes, isCarbon);
        BigDecimal ynum = new BigDecimal("0.00");
        ynum = ynum.add(data6);
        remap.put("ynum", ynum.setScale(2, RoundingMode.HALF_UP));
        LocalDateTime ynte = DateUtils.offsetMonth(date, -12);
        LocalDateTime ynts = DateUtils.offsetMonth(ys, -12);
        TimeDataVo timePeriod7 = getTimePeriod(ynts, ynte);
        BigDecimal data7 = getData(timePeriod7.getBegin(), timePeriod7.getEnd(), tenant, instrumentTypes, isCarbon);
        BigDecimal ynnum = new BigDecimal("0.00");
        ynnum = ynnum.add(data7);

        BigDecimal yyoy = new BigDecimal("0.00");
        if (ynnum.compareTo(new BigDecimal("0.00")) == 0) {
            yyoy = yyoy;
        } else {
            yyoy = yyoy.add(ynum.subtract(ynnum).divide(ynnum, 2, RoundingMode.HALF_UP));
        }
        remap.put("yyoy", yyoy);
        return remap;
    }

    @Override
    public List<DataTimeFlagVo> carbonEmissionTrends(CarbonQueryParam carbonQueryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String instrumentTypes = getInstrumentTypes();
        String[] split = carbonQueryParam.getPeriod().split(",");
        LocalDateTime begin = DateUtils.parse(split[0]);
        TimeDataVo timeFlagPeriod = getTimeFlagPeriod(carbonQueryParam.getTimeFlag(), carbonQueryParam.getPeriod());
        Integer flag = timeFlagPeriod.getTimeDifference();
        String areaData = CommonAreaData.getAreaData(null);
        QueryApi queryApi = influxdbClient.getQueryApi();
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + timeFlagPeriod.getBegin() + ", stop: " + timeFlagPeriod.getEnd() + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"area" + timeFlagPeriod.getTableFlag() + "\")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" " + instrumentTypes + areaData + ")\n" +
                      "  |> group(columns: [\"instrumentType\",\"_time\"])\n" +
                      "  |> sum(column: \"value\")";

        List<DataTimeFlagVo> query = queryApi.query(flux, DataTimeFlagVo.class);
        List<DataTimeFlagVo> allDataTimeFlagVo = new ArrayList<>();
        //转成碳量
        String format = EnergyJob.timeFormat(carbonQueryParam.getTimeFlag());
        if (query.size() > 0) {
            Map<Instant, List<DataTimeFlagVo>> collect = query.stream().collect(Collectors.groupingBy(DataTimeFlagVo::getTime));
            for (Instant instant : collect.keySet()) {
                DataTimeFlagVo dataTimeFlagVo = new DataTimeFlagVo();
                List<DataTimeFlagVo> dataTimeFlagVos1 = collect.get(instant);
                BigDecimal total = new BigDecimal("0.00");
                for (DataTimeFlagVo timeFlagVo : dataTimeFlagVos1) {
                    if (carbonQueryParam.getIsCarbon()) {
                        total = total.add(timeFlagVo.getValue()
                                                  .multiply(new BigDecimal(CalculationIndicatorsEnum.getIndicatorsEnum(timeFlagVo.getInstrumentType())))
                                                  .multiply(new BigDecimal("3.2")));
                    } else {
                        total = total.add(timeFlagVo.getValue()
                                                  .multiply(new BigDecimal(CalculationIndicatorsEnum.getIndicatorsEnum(timeFlagVo.getInstrumentType()))));
                    }

                }
                LocalDateTime date = LocalDateTime.now();
                String formatTime = DateUtils.format(date, format);
                dataTimeFlagVo.setTimeFlag(formatTime);
                dataTimeFlagVo.setValue(total);
                allDataTimeFlagVo.add(dataTimeFlagVo);
            }
        }

        //时间推移
        Map<String, DataTimeFlagVo> collect = allDataTimeFlagVo.stream().collect(Collectors.toMap(DataTimeFlagVo::getTimeFlag, s -> s));
        List<DataTimeFlagVo> endDataTimeFlagVos = new ArrayList<>();
        for (int i = 0; i <= flag; i++) {
            DataTimeFlagVo endDataTimeFlagVo = new DataTimeFlagVo();
            endDataTimeFlagVo.setValue(new BigDecimal("0.00"));
            EnergyJob.timeMovingUnit(begin, carbonQueryParam.getTimeFlag(), i);
            String s = DateUtils.formatDate(begin, format);
            endDataTimeFlagVo.setTimeFlag(s);
            DataTimeFlagVo dataTimeFlagVo = collect.get(s);
            if (Objects.nonNull(dataTimeFlagVo)) {
                endDataTimeFlagVo.setValue(dataTimeFlagVo.getValue());
            }
            endDataTimeFlagVos.add(endDataTimeFlagVo);
        }
        return endDataTimeFlagVos;
    }

    @Override
    public List<DataTimeFlagVo> proportionCarbonEmissionsItems(CarbonQueryParam carbonQueryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String instrumentTypes = getInstrumentTypes();
        LocalDateTime date = LocalDateTime.now();
        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                               .eq(EnergyGate::getOpen, true)
                                                               .ne(EnergyGate::getEnergyType, InstrumentTypeEnum.ENERGY_METER.getCode()));
        TimeDataVo timeFlagPeriod = getTimeFlagPeriod(carbonQueryParam.getTimeFlag(), carbonQueryParam.getPeriod());
        String areaData = CommonAreaData.getAreaData(null);
        QueryApi queryApi = influxdbClient.getQueryApi();
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + timeFlagPeriod.getBegin() + ", stop: " + timeFlagPeriod.getEnd() + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"area" + timeFlagPeriod.getTableFlag() + "\")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\"" + instrumentTypes + areaData + ")\n" +
                      "  |> group(columns: [\"instrumentType\"])\n" +
                      "  |> sum(column: \"value\")";
        List<DataTimeFlagVo> query = queryApi.query(flux, DataTimeFlagVo.class);
        Map<String, DataTimeFlagVo> collect = query.stream().collect(Collectors.toMap(DataTimeFlagVo::getInstrumentType, s -> s));
        List<DataTimeFlagVo> objects = new ArrayList<>();
        for (EnergyGate energyGate : list) {
            DataTimeFlagVo dataTimeFlagVo = new DataTimeFlagVo();
            BigDecimal value = new BigDecimal("0.00");
            DataTimeFlagVo dataTimeFlagVoGate = collect.get(energyGate.getEnergyType());
            if (Objects.nonNull(dataTimeFlagVoGate)) {
                value = value.add(query.get(0).getValue()
                                          .multiply(new BigDecimal(CalculationIndicatorsEnum.getIndicatorsEnum(dataTimeFlagVoGate.getInstrumentType())))
                                          .multiply(carbonIndex));
            }
            dataTimeFlagVo.setInstrumentType(energyGate.getEnergyType());
            dataTimeFlagVo.setValue(value);
            objects.add(dataTimeFlagVo);
        }
        return objects;
    }

    @Override
    public Map<String, Object> getEnergyProject(EnergyProjectQueryParam param) {
        HashMap<String, Object> remap = new HashMap<>();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        LocalDateTime date = LocalDateTime.now();
        TimeDataVo timeFlagPeriod = getTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());
        QueryApi queryApi = influxdbClient.getQueryApi();
        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                               .eq(EnergyGate::getOpen, true)
                                                               .eq(StringUtils.isNotEmpty(param.getInstrumentType()), EnergyGate::getEnergyType, param.getInstrumentType()));
        List<String> collect = list.stream().map(EnergyGate::getEnergyType).collect(Collectors.toList());
        if (ListUtils.isEmpty(collect)) {
            remap.put("total", BigDecimal.ZERO);
            List<EnergyProjectVO> list1 = new ArrayList<>();
            remap.put("list", list1);
            return remap;
        }
        List<EnergyProject> energyProjects = energyProjectMapper.selectList(Wrappers.<EnergyProject>lambdaQuery()
                                                                                    .in(EnergyProject::getInstrumentType, collect));
        List<EnergyProjectVO> copying = ObjectUtils.copying(energyProjects, EnergyProjectVO.class);
        BigDecimal allTotal = new BigDecimal("0.00");
        for (EnergyProjectVO energyProjectVO : copying) {
            String flux = "from(bucket: \"energy\")\n" +
                          "  |> range(start: " + timeFlagPeriod.getBegin() + ", stop: " + timeFlagPeriod.getEnd() + ")\n" +
                          "  |> filter(fn: (r) => r[\"_measurement\"] == \"project" + timeFlagPeriod.getTableFlag() + "\")\n" +
                          "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.projectId==\"" + energyProjectVO.getId() + "\")\n" +
                          "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                          "  |> group(columns: [\"instrumentType\"])\n" +
                          "  |> sum(column: \"value\")";
            List<DataProjectVo> query = queryApi.query(flux, DataProjectVo.class);
            BigDecimal total = new BigDecimal(BigInteger.ZERO);
            if (ListUtils.nonEmpty(query)) {
                for (DataProjectVo dataProjectVo : query) {
                    total = total.add(dataProjectVo.getValue()
                                              .multiply(new BigDecimal(CalculationIndicatorsEnum.getIndicatorsEnum(energyProjectVO.getInstrumentType()))));
                }
            }
            energyProjectVO.setValue(total.setScale(2, BigDecimal.ROUND_HALF_UP));
            allTotal = allTotal.add(total);
        }

        remap.put("total", allTotal.setScale(2, RoundingMode.HALF_UP));
        remap.put("list", copying);
        return remap;
    }

    @Override
    public Map<String, Object> dosageAnalysis(DosageAnalysisParam param) {
        Map<String, Object> remap = new HashMap<>();
        if (ListUtils.nonEmpty(param.getTreeIds())) {
            getTreeIds(param);
        } else {
            remap.put("tnum", "0.00");
            remap.put("dyoy", "0.00");
            remap.put("tqoq", "0.00");
            remap.put("mnum", "0.00");
            remap.put("myoy", "0.00");
            remap.put("mqoq", "0.00");
            remap.put("ynum", "0.00");
            remap.put("yyoy", "0.00");
            remap.put("yqoq", "0.00");
            return remap;
        }

        Integer tenant = SessionContextHolder.getRequester().getTenant();
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime dateTime = DateUtils.beginOfDay(date);
        TimeDataVo timePeriod = getTimePeriod(dateTime, date);
        BigDecimal data = getDosageData(timePeriod.getBegin(), timePeriod.getEnd(), tenant, param);
        //今日量
        BigDecimal tnum = new BigDecimal("0.00");
        tnum = tnum.add(data);
        remap.put("tnum", tnum.setScale(2, RoundingMode.HALF_UP));

        LocalDateTime offset = DateUtils.offsetDay(date, -1);
        TimeDataVo timePeriod1 = getTimePeriod(DateUtils.beginOfDay(offset), DateUtils.endOfDay(offset));
        BigDecimal data1 = getDosageData(timePeriod1.getBegin(), timePeriod1.getEnd(), tenant, param);
        BigDecimal ytnum = new BigDecimal("0.00");
        ytnum = ytnum.add(data1);
        //同比
        BigDecimal dyoy = new BigDecimal("0.00");
        if (ytnum.compareTo(new BigDecimal("0.00")) == 0) {
            dyoy = dyoy;
        } else {
            dyoy = dyoy.add(tnum.subtract(ytnum).divide(ytnum, 2, RoundingMode.HALF_UP));
        }
        remap.put("dyoy", dyoy);
        //环比时间
        BigDecimal data2 = getDosageData(timePeriod1.getBegin(), timePeriod1.getEnd(), tenant, param);
        BigDecimal tqoqnum = new BigDecimal("0.00");
        BigDecimal tqoq = new BigDecimal("0.00");
        tqoqnum = tqoqnum.add(data2);
        if (tqoq.compareTo(new BigDecimal("0.00")) == 0) {
            tqoq = tqoq;
        } else {
            tqoq = tqoq.add(tnum.subtract(tqoqnum).divide(tqoqnum, 2, RoundingMode.HALF_UP));
        }
        //环比
        remap.put("tqoq", tqoq);

        //今月量
        TimeDataVo timePeriod2 = getTimePeriod(DateUtils.beginOfMonth(date), date);
        BigDecimal data3 = getDosageData(timePeriod2.getBegin(), timePeriod2.getEnd(), tenant, param);
        BigDecimal mnum = new BigDecimal("0.00");
        mnum = mnum.add(data3);
        remap.put("mnum", mnum.setScale(2, RoundingMode.HALF_UP));
        LocalDateTime offset1 = DateUtils.offsetMonth(date, -1);
        TimeDataVo timePeriod3 = getTimePeriod(DateUtils.beginOfMonth(offset1), DateUtils.endOfMonth(offset1));
        String mts = timePeriod3.getBegin();
        String mte = timePeriod3.getEnd();
        BigDecimal data4 = getDosageData(mts, mte, tenant, param);
        BigDecimal ymnum = new BigDecimal("0.00");
        ymnum = ymnum.add(data4);
        //月同比
        BigDecimal myoy = new BigDecimal("0.00");
        if (ymnum.compareTo(new BigDecimal("0.00")) == 0) {
            myoy = myoy;
        } else {
            myoy = myoy.add(mnum.subtract(ymnum).divide(ymnum, 2, RoundingMode.HALF_UP));
        }
        remap.put("myoy", myoy);
        TimeDataVo timePeriod4 = getTimePeriod(DateUtils.beginOfMonth(offset1), offset1);
        BigDecimal data5 = getDosageData(mts, timePeriod.getEnd(), tenant, param);
        BigDecimal mqoqnum = new BigDecimal("0.00");
        BigDecimal mqoq = new BigDecimal("0.00");
        mqoqnum = mqoqnum.add(data5);
        if (mqoqnum.compareTo(new BigDecimal("0.00")) == 0) {
            mqoq = mqoq;
        } else {
            mqoq = tqoq.add(mnum.subtract(mqoqnum).divide(mqoqnum, 2, RoundingMode.HALF_UP));
        }
        //月环比
        remap.put("mqoq", mqoq);

        //今年量

        TimeDataVo timePeriod5 = getTimePeriod(DateUtils.beginOfYear(date), date);
        String ys = timePeriod5.getBegin();
        String ye = timePeriod5.getEnd();
        BigDecimal data6 = getDosageData(ys, ye, tenant, param);
        BigDecimal ynum = new BigDecimal("0.00");
        ynum = ynum.add(data6);
        remap.put("ynum", ynum.setScale(2, RoundingMode.HALF_UP));
        LocalDateTime offset2 = DateUtils.offsetMonth(date, -12);
        TimeDataVo timePeriod6 = getTimePeriod(DateUtils.beginOfYear(offset2), DateUtils.beginOfYear(offset2));
        String ynts = timePeriod6.getBegin();
        String ynte = timePeriod6.getEnd();
        BigDecimal data7 = getDosageData(ynts, ynte, tenant, param);
        BigDecimal ynnum = new BigDecimal("0.00");
        ynnum = ynnum.add(data7);

        BigDecimal yyoy = new BigDecimal("0.00");
        if (ynnum.compareTo(new BigDecimal("0.00")) == 0) {
            yyoy = yyoy;
        } else {
            yyoy = yyoy.add(ynum.subtract(ynnum).divide(ynnum, 2, RoundingMode.HALF_UP));
        }
        //年同比
        remap.put("yyoy", yyoy);
        TimeDataVo timePeriod7 = getTimePeriod(DateUtils.beginOfYear(offset2), offset2);
        String ychainTime = timePeriod7.getEnd();
        BigDecimal data8 = getDosageData(ynts, ychainTime, tenant, param);
        BigDecimal yqoqnum = new BigDecimal("0.00");
        BigDecimal yqoq = new BigDecimal("0.00");
        yqoqnum = yqoqnum.add(data8);
        if (yqoqnum.compareTo(new BigDecimal("0.00")) == 0) {
            yqoq = yqoq;
        } else {
            yqoq = yqoq.add(ynum.subtract(yqoqnum).divide(yqoqnum, 2, RoundingMode.HALF_UP));
        }
        //年环比
        remap.put("yqoq", yqoq);
        return remap;
    }

    @Override
    public List<DosageAnalysisVo> energyAnalysis(DosageAnalysisParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        TimeDataVo timeFlagPeriod = getTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());
        String startTime = timeFlagPeriod.getBegin();
        String endTime = timeFlagPeriod.getEnd();
        String startOldTime = timeFlagPeriod.getOldBegin();
        String endOldTime = timeFlagPeriod.getOldEnd();
        Integer flag = timeFlagPeriod.getTimeDifference();
        List<DosageAnalysisVo> dosageAnalysisVos = new ArrayList<>();
        if (ListUtils.isEmpty(param.getTreeIds())) {
            return dosageAnalysisVos;
        } else {
            getTreeIds(param);
            if (ListUtils.isEmpty(param.getIds())) {
                return dosageAnalysisVos;
            }
        }

        QueryApi queryApi = influxdbClient.getQueryApi();
        String s = dosageAnalysisDistributeFlux(startTime, endTime, tenant, param.getInstrumentType(), param.getIds(), param.getFilterFlag(), param.getTimeFlag());
        List<DataVo> query = queryApi.query(s, DataVo.class);
        String s1 = dosageAnalysisDistributeFlux(startOldTime, endOldTime, tenant, param.getInstrumentType(), param.getIds(), param.getFilterFlag(), param.getTimeFlag());
        List<DataVo> query1 = queryApi.query(s1, DataVo.class);
        //温度
        String s2 = dosageAnalysisTemperatureFlux(startTime, endTime, param.getTimeFlag());
        List<DataVo> query2 = queryApi.query(s2, DataVo.class);
        LocalDateTime st = timeFlagPeriod.getSt();
        for (int i = 0; i <= flag; i++) {
            DosageAnalysisVo dosageAnalysisVo = new DosageAnalysisVo();
            String timeFlag = "";
            if (param.getTimeFlag().equals("TODAY")) {
                LocalDateTime offset = DateUtils.offsetDay(st, i);
                timeFlag = DateUtils.format(offset, EnergyJob.timeFormat(param.getTimeFlag()));
            } else if (param.getTimeFlag().equals("THIS_MONTH")) {
                LocalDateTime offset = DateUtils.offsetMonth(st, i);
                timeFlag = DateUtils.format(offset, EnergyJob.timeFormat(param.getTimeFlag()));
            } else if (param.getTimeFlag().equals("THIS_YEAR")) {
                LocalDateTime offset = DateUtils.offsetMonth(st, i * 12);
                timeFlag = DateUtils.format(offset, EnergyJob.timeFormat(param.getTimeFlag()));
            }
            //同期
            for (DataVo dataVo : query) {
                LocalDateTime date1 = LocalDateTime.now();
                String time = DateUtils.format(date1, EnergyJob.timeFormat(param.getTimeFlag()));
                if (timeFlag.equals(time)) {
                    BigDecimal bigDecimal = dataVo.getValue() == null ? new BigDecimal("0.00") : dataVo.getValue();
                    dosageAnalysisVo.setValue(bigDecimal);
                }
            }

            //上期
            for (DataVo dataVo : query1) {
                LocalDateTime date1 = LocalDateTime.now();
                String time = DateUtils.format(date1, EnergyJob.timeFormat(param.getTimeFlag()));
                if (timeFlag.equals(time)) {
                    dosageAnalysisVo.setLastValue(dataVo.getValue() == null ? new BigDecimal("0.00") : dataVo.getValue());
                }
            }

            //温度
            for (DataVo dataVo : query2) {
                LocalDateTime date1 = LocalDateTime.now();
                String time = DateUtils.format(date1, EnergyJob.timeFormat(param.getTimeFlag()));
                if (timeFlag.equals(time)) {
                    BigDecimal bigDecimal = dataVo.getValue() == null ? new BigDecimal("0.00") : dataVo.getValue();
                    dosageAnalysisVo.setTemperature(bigDecimal);
                }
            }
            dosageAnalysisVo.setTimeFlag(timeFlag);
            dosageAnalysisVos.add(dosageAnalysisVo);

        }
        return dosageAnalysisVos;
    }

    @Override
    public List<DosageAnalysisDistributionVo> energyAnalysisDistribution(DosageAnalysisParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        TimeDataVo timeFlagPeriod = getTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());
        String startTime = timeFlagPeriod.getBegin();
        String endTime = timeFlagPeriod.getEnd();
        Integer flag = timeFlagPeriod.getTimeDifference();
        List<DosageAnalysisDistributionVo> dosageAnalysisVos = new ArrayList<>();
        if (ListUtils.isEmpty(param.getTreeIds())) {
            return dosageAnalysisVos;
        } else {
            getTreeIds(param);
            if (ListUtils.isEmpty(param.getIds())) {
                return dosageAnalysisVos;
            }
        }

        QueryApi queryApi = influxdbClient.getQueryApi();
        String fs = analysisDistributeFlux(startTime, endTime, tenant, param.getInstrumentType(), param.getIds(), param.getFilterFlag(), param.getTimeFlag(), "fvalue");
        List<DataVo> query1 = queryApi.query(fs, DataVo.class);
        String ps = analysisDistributeFlux(startTime, endTime, tenant, param.getInstrumentType(), param.getIds(), param.getFilterFlag(), param.getTimeFlag(), "pvalue");
        List<DataVo> query2 = queryApi.query(ps, DataVo.class);
        String gs = analysisDistributeFlux(startTime, endTime, tenant, param.getInstrumentType(), param.getIds(), param.getFilterFlag(), param.getTimeFlag(), "gvalue");
        List<DataVo> query3 = queryApi.query(gs, DataVo.class);

        for (int i = 0; i <= flag; i++) {
            DosageAnalysisDistributionVo dosageAnalysisVo = new DosageAnalysisDistributionVo();
            String timeFlag = "";
            if (param.getTimeFlag().equals("TODAY")) {
                LocalDateTime offset = DateUtils.offsetDay(timeFlagPeriod.getSt(), i);
                String format = EnergyJob.timeFormat(param.getTimeFlag());
                timeFlag = DateUtils.formatDate(offset, format);
            } else if (param.getTimeFlag().equals("THIS_MONTH")) {
                LocalDateTime offset = DateUtils.offsetMonth(timeFlagPeriod.getSt(), i);
                String format = EnergyJob.timeFormat(param.getTimeFlag());
                timeFlag = DateUtils.formatDate(offset, format);
            } else if (param.getTimeFlag().equals("THIS_YEAR")) {
                LocalDateTime offset = DateUtils.offsetMonth(timeFlagPeriod.getSt(), i * 12);
                String format = EnergyJob.timeFormat(param.getTimeFlag());
                timeFlag = DateUtils.formatDate(offset, format);
            }

            //峰值
            for (DataVo dataVo : query1) {
                LocalDateTime date1 = LocalDateTime.now();
                String time = DateUtils.format(date1, EnergyJob.timeFormat(param.getTimeFlag()));
                if (timeFlag.equals(time)) {
                    BigDecimal bigDecimal = dataVo.getFvalue() == null ? new BigDecimal("0.00") : dataVo.getFvalue();
                    dosageAnalysisVo.setFvalue(bigDecimal);
                }
            }

            //平值
            for (DataVo dataVo : query2) {
                LocalDateTime date1 = LocalDateTime.now();
                String time = DateUtils.format(date1, EnergyJob.timeFormat(param.getTimeFlag()));

                if (timeFlag.equals(time)) {
                    dosageAnalysisVo.setPvalue(dataVo.getPvalue() == null ? new BigDecimal("0.00") : dataVo.getPvalue());
                }

            }

            //谷值
            for (DataVo dataVo : query3) {
                LocalDateTime date1 = LocalDateTime.now();
                String time = DateUtils.format(date1, EnergyJob.timeFormat(param.getTimeFlag()));
                if (timeFlag.equals(time)) {
                    dosageAnalysisVo.setGvalue(dataVo.getGvalue() == null ? new BigDecimal("0.00") : dataVo.getGvalue());
                }

            }
            dosageAnalysisVo.setTimeFlag(timeFlag);
            dosageAnalysisVos.add(dosageAnalysisVo);

        }
        return dosageAnalysisVos;
    }

    @Override
    public List<DosageAnalysisVo> energyConsumptionRanking(DosageAnalysisParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        TimeDataVo timeFlagPeriod = getTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());
        String startTime = timeFlagPeriod.getBegin();
        String endTime = timeFlagPeriod.getEnd();

        List<DosageAnalysisVo> dosageAnalysisVos = new ArrayList<>();
        if (ListUtils.isEmpty(param.getTreeIds())) {
            return dosageAnalysisVos;
        } else {
            getTreeIds(param);
        }
        QueryApi queryApi = influxdbClient.getQueryApi();
        String s = dosageAnalysisFlux(startTime, endTime, tenant, param.getInstrumentType(), param.getIds(), param.getFilterFlag(), param.getTimeFlag());
        List<DataVo> query = queryApi.query(s, DataVo.class);

        if (ListUtils.nonEmpty(query)) {
            if (param.getFilterFlag().equals("area")) {
                List<Long> collect = query.stream().map(a -> {
                    return Long.valueOf(a.getAreaId());
                }).collect(Collectors.toList());
                List<AreaProject> areaProjects = areaProjectMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                                      .in(AreaProject::getId, collect));
                Map<Long, List<AreaProject>> collect1 = areaProjects.stream().collect(Collectors.groupingBy(AreaProject::getId));
                for (DataVo dataVo : query) {
                    DosageAnalysisVo dosageAnalysisVo = new DosageAnalysisVo();
                    dosageAnalysisVo.setValue(dataVo.getValue());
                    dosageAnalysisVo.setName(collect1.get(Long.valueOf(dataVo.getAreaId())).get(0).getAreaName());
                    dosageAnalysisVos.add(dosageAnalysisVo);
                }
            } else {
                List<Long> collect = query.stream().map(a -> {
                    return Long.valueOf(a.getRouteId());
                }).collect(Collectors.toList());
                List<RouteProject> routeProjects = routeProjectMapper.selectList(Wrappers.<RouteProject>lambdaQuery()
                                                                                         .in(RouteProject::getId, collect));
                Map<Long, List<RouteProject>> collect1 = routeProjects.stream().collect(Collectors.groupingBy(RouteProject::getId));
                for (DataVo dataVo : query) {
                    DosageAnalysisVo dosageAnalysisVo = new DosageAnalysisVo();
                    dosageAnalysisVo.setValue(dataVo.getValue());
                    dosageAnalysisVo.setName(collect1.get(Long.valueOf(dataVo.getRouteId())).get(0).getRouteName());
                    dosageAnalysisVos.add(dosageAnalysisVo);
                }
            }

        }
        return dosageAnalysisVos;

    }

    @Override
    public List<EnergyConsumptionDetailsVo> getEnergyConsumptionDetails(DosageAnalysisParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        TimeDataVo timeFlagPeriod = getTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());
        String startTime = timeFlagPeriod.getBegin();
        String endTime = timeFlagPeriod.getEnd();
        if (ListUtils.isEmpty(param.getIds()) || param.getIds().size() == 0) {
            getTreeIds(param);
        }

        EnergyChargeConfigVO byType = energyChargeConfigService.getByType(param.getInstrumentType(), null);
        if (Objects.nonNull(byType) && byType.getChargeType() != null && byType.getChargeType().equals(ChargeTypeEnum.FPG.name())) {
            List<EnergyConsumptionDetailsVo> energyConsumptionDetailsVos = dosageAnalysisFPGDataDetail(startTime, endTime, tenant, param.getInstrumentType(), param.getIds(), param.getFilterFlag(), param.getTimeFlag());
            return energyConsumptionDetailsVos;
        } else {
            return dosageAnalysisDataDetail(startTime, endTime, tenant, param.getInstrumentType(), param.getIds(), param.getFilterFlag(), param.getTimeFlag());
        }
    }

    @Override
    public Map<String, Object> getEnergyArea(EnergyTrendParam param) {
        HashMap<String, Object> remap = new HashMap<>();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        QueryApi queryApi = influxdbClient.getQueryApi();
        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery().eq(EnergyGate::getOpen, true)
                                                               .eq(StringUtils.isNotEmpty(param.getInstrumentType()), EnergyGate::getEnergyType, param.getInstrumentType()));

        if (ListUtils.isEmpty(list)) {
            Map<String, Object> map = new HashMap<>();
            List<AreaProjectVO> list1 = new ArrayList<>();
            map.put("total", new BigDecimal("0.00"));
            map.put("list", list1);
            return map;
        }
        List<String> collect = list.stream().map(EnergyGate::getEnergyType).collect(Collectors.toList());
        List<Long> objects = new ArrayList<>();
        for (String s : collect) {
            List<Long> areaCount = areaProjectMapper.getAreaCount(s);
            if (ListUtils.nonEmpty(areaCount)) {
                objects.addAll(areaCount);
            }
        }
        List<String> ids = getIds(objects, param.getTreeIds(), new ArrayList<String>());
        if (ListUtils.isEmpty(ids)) {
            Map<String, Object> map = new HashMap<>();
            List<AreaProjectVO> list1 = new ArrayList<>();
            map.put("total", new BigDecimal("0.00"));
            map.put("list", list1);
            return map;
        }
        param.setAreaFlag(ids);

        List<AreaProject> areaProjects = areaProjectMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                              .in(AreaProject::getInstrumentType, collect)
                                                                              .in(ListUtils.nonEmpty(param.getAreaFlag()), AreaProject::getId, param.getAreaFlag()));
        List<AreaProjectVO> copying = ObjectUtils.copying(areaProjects, AreaProjectVO.class);

        //id拼接
        String areaIds = "";
        if (ListUtils.nonEmpty(ids)) {
            String areaCollect = ids.stream().map(d -> "r.areaId==\"" + d + "\"").collect(Collectors.joining(" or "));
            areaIds = "and (" + areaCollect + ")";
        }

        TimeDataVo timeFlagPeriod = getTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());

        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + timeFlagPeriod.getBegin() + ", stop: " + timeFlagPeriod.getEnd() + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"area" + timeFlagPeriod.getTableFlag() + "\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\"" + areaIds + ")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> group(columns: [\"instrumentType\",\"areaId\"])\n" +
                      "  |> sum(column: \"value\")";

        List<DataProjectVo> query = queryApi.query(flux, DataProjectVo.class);
        Map<String, List<DataProjectVo>> collect1 = query.stream().collect(Collectors.groupingBy(DataProjectVo::getAreaId));
        BigDecimal allTotal = new BigDecimal("0.00");
        for (AreaProjectVO areaProjectVO : copying) {
            List<DataProjectVo> dataProjectVos = collect1.get(areaProjectVO.getId().toString());
            BigDecimal total = new BigDecimal(BigInteger.ZERO);
            if (ListUtils.nonEmpty(dataProjectVos)) {
                for (DataProjectVo dataProjectVo : dataProjectVos) {
                    total = total.add(dataProjectVo.getValue()
                                              .multiply(new BigDecimal(CalculationIndicatorsEnum.getIndicatorsEnum(dataProjectVo.getInstrumentType()))));
                }

            }
            areaProjectVO.setValue(total.setScale(2, RoundingMode.HALF_UP));
            allTotal = allTotal.add(total);
        }

        remap.put("total", allTotal);
        remap.put("list", copying);
        return remap;
    }

    @Override
    public List<EnergyAnalysisMenuVo> getEnergyAnalysisMenu() {
        List<InstrumentTypeEnumVO> enumTypeVo = InstrumentTypeEnum.getEnumTypeVo();
        List<EnergyAnalysisMenuVo> menuVos = ObjectUtils.copying(enumTypeVo, EnergyAnalysisMenuVo.class);
        for (EnergyAnalysisMenuVo menuVo : menuVos) {
            menuVo.setLevel(1);
            ArrayList<BindTypeVo> bindTypeVos = new ArrayList<>();
            for (BindTypeEnum value : BindTypeEnum.values()) {
                BindTypeVo bindTypeVo = new BindTypeVo();
                bindTypeVo.setLevel(2);
                if (menuVo.getCode().equals("water_meter")) {
                    bindTypeVo.setCode(value.toString() + "-S");
                } else if (menuVo.getCode().equals("electricity_meter")) {
                    bindTypeVo.setCode(value.toString() + "-D");
                } else if (menuVo.getCode().equals("gas_meter")) {
                    bindTypeVo.setCode(value.toString() + "-Q");
                }

                bindTypeVo.setName(BindTypeEnum.getByCode(value.toString()));
                if (value.toString().equals("ITEM")) {
                    List<BindTypeVo> projects = energyProjectMapper.getProjects(menuVo.getCode());
                    for (BindTypeVo project : projects) {
                        project.setLevel(3);
                    }
                    bindTypeVo.setChildren(projects);
                } else if (value.toString().equals("AREA")) {
                    List<BindTypeVo> areas = areaProjectMapper.getAreas(menuVo.getCode());
                    for (BindTypeVo project : areas) {
                        project.setLevel(3);
                    }
                    bindTypeVo.setChildren(areas);
                }
                bindTypeVos.add(bindTypeVo);

            }
            menuVo.setChildren(bindTypeVos);

        }

        return menuVos;
    }

    @Override
    public List<EnergyAnalysisMenuVo> getAppEnergyAnalysisMenu() {

        List<InstrumentTypeEnumVO> enumTypeVo = InstrumentTypeEnum.getEnumTypeVo();
        List<EnergyAnalysisMenuVo> menuVos = ObjectUtils.copying(enumTypeVo, EnergyAnalysisMenuVo.class);
        for (EnergyAnalysisMenuVo menuVo : menuVos) {
            menuVo.setLevel(1);
            ArrayList<BindTypeVo> bindTypeVos = new ArrayList<>();
            for (BindTypeEnum value : BindTypeEnum.values()) {
                BindTypeVo bindTypeVo = new BindTypeVo();
                bindTypeVo.setLevel(2);
                if (menuVo.getCode().equals("water_meter")) {
                    bindTypeVo.setCode(value.toString());
                } else if (menuVo.getCode().equals("electricity_meter")) {
                    bindTypeVo.setCode(value.toString());
                } else if (menuVo.getCode().equals("gas_meter")) {
                    bindTypeVo.setCode(value.toString());
                }

                bindTypeVo.setName(BindTypeEnum.getByCode(value.toString()));
                if (value.toString().equals("ITEM")) {
                    List<BindTypeVo> projects = energyProjectMapper.getProjects(menuVo.getCode());
                    for (BindTypeVo project : projects) {
                        project.setLevel(3);
                    }
                    bindTypeVo.setChildren(projects);
                } else if (value.toString().equals("AREA")) {
                    List<BindTypeVo> areas = areaProjectMapper.getAreas(menuVo.getCode());
                    for (BindTypeVo project : areas) {
                        project.setLevel(3);
                    }
                    bindTypeVo.setChildren(areas);
                }
                bindTypeVos.add(bindTypeVo);

            }
            menuVo.setChildren(bindTypeVos);

        }

        return menuVos;
    }

    @Override
    public Map<String, Object> getEnergyAnalysisOverview(EnergyAnalysisQueryParam param) throws ParseException {
        if (param.getBindType().equals("ITEM")) {
            String bindType = "project";
            String itemId = param.getItemId();
            List<EnergyProjectItem> item = energyProjectItemService.list(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                 .eq(EnergyProjectItem::getBindType, "ITEM")
                                                                                 .eq(EnergyProjectItem::getProjectId, param.getItemId()));
            String deviceIds = "";
            if (item.size() > 0) {
                for (EnergyProjectItem energyProjectItem : item) {
                    deviceIds = deviceIds + " and r.deviceId==\"" + energyProjectItem.getId() + "\"";
                }
            }
            return getAnalysisOverview(itemId, bindType, deviceIds);
        } else if (param.equals("AREA")) {
            String bindType = "area";
            String itemId = param.getItemId();
            List<EnergyProjectItem> item = energyProjectItemService.list(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                 .eq(EnergyProjectItem::getBindType, "AREA")
                                                                                 .eq(EnergyProjectItem::getProjectId, param.getItemId()));
            String deviceIds = "";
            if (item.size() > 0) {
                for (EnergyProjectItem energyProjectItem : item) {
                    deviceIds = deviceIds + " and r.deviceId==\"" + energyProjectItem.getId() + "\"";
                }
            }
            return getAnalysisOverview(itemId, bindType, deviceIds);
        }
        return null;
    }

    @Override
    public List<DataTimeFlagVo> getEnergyAnalysisTrend(EnergyTrendParam param) throws ParseException {

        LocalDateTime date = LocalDateTime.now();
        LocalDateTime date1 = date;
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        QueryApi queryApi = influxdbClient.getQueryApi();
        String flux = "";
        List<DataTimeFlagVo> query = new ArrayList<>();
        String bindType = "";
        String deviceIds = "";
        if (param.getBindType().equals("ITEM")) {
            bindType = "project";
            List<EnergyProjectItem> item = energyProjectItemService.list(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                 .eq(EnergyProjectItem::getBindType, "ITEM")
                                                                                 .eq(EnergyProjectItem::getProjectId, param.getItemId()));
            if (item.size() > 0) {
                for (EnergyProjectItem energyProjectItem : item) {
                    deviceIds = deviceIds + " and r.deviceId==\"" + energyProjectItem.getId() + "\"";
                }
            }
        } else if (param.equals("AREA")) {
            bindType = "area";
            List<EnergyProjectItem> item = energyProjectItemService.list(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                 .eq(EnergyProjectItem::getBindType, "AREA")
                                                                                 .eq(EnergyProjectItem::getProjectId, param.getItemId()));
            if (item.size() > 0) {
                for (EnergyProjectItem energyProjectItem : item) {
                    deviceIds = deviceIds + " and r.deviceId==\"" + energyProjectItem.getId() + "\"";
                }
            }
        }
        if (param.getTimeFlag().equals("Year")) {
            LocalDateTime dateTime = DateUtils.beginOfYear(date1);
            String s = DateUtils.formatDate(DateUtils.offsetMinute(dateTime, -8 * 60), DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            LocalDateTime dateTime1 = DateUtils.endOfYear(date1);
            String s1 = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Month\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                   "  |> group(columns: [\"_time\",\"" + bindType + "Id\",\"" + bindType + "Name\"])\n" +
                   "  |> sum(column: \"value\")";
            List<DataVo> dataVos = queryApi.query(flux, DataVo.class);
            for (int i = 1; i <= 12; i++) {
                DataTimeFlagVo dataTimeFlagVo = new DataTimeFlagVo();
                dataTimeFlagVo.setTimeFlag(String.valueOf(i));
                dataTimeFlagVo.setValue(new BigDecimal("0.00"));
                if (dataVos.size() > 0) {
                    for (DataVo dataVo : dataVos) {
                        LocalDateTime localDateTime = DateUtils.parse(dataVo.getTime().toEpochMilli());
                        int monthValue = localDateTime.getMonthValue();
                        if (i == monthValue) {
                            dataTimeFlagVo.setValue(dataVo.getValue());
                        }
                    }
                }
                query.add(dataTimeFlagVo);

            }

        } else if (param.getTimeFlag().equals("Month")) {
            LocalDateTime dateTime = DateUtils.beginOfMonth(date1);
            String s = DateUtils.formatDate(DateUtils.offsetMinute(dateTime, -8 * 60), DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            LocalDateTime dateTime1 = DateUtils.endOfMonth(date1);
            String s1 = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Day\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                   "  |> group(columns: [\"_time\",\"" + bindType + "Id\",\"" + bindType + "Name\"])\n" +
                   "  |> sum(column: \"value\")";

            List<DataVo> dataVos = queryApi.query(flux, DataVo.class);
            int i1 = dateTime1.getDayOfMonth();
            for (int i = 1; i <= i1; i++) {
                DataTimeFlagVo dataTimeFlagVo = new DataTimeFlagVo();
                dataTimeFlagVo.setTimeFlag(String.valueOf(i));
                dataTimeFlagVo.setValue(new BigDecimal("0.00"));
                if (dataVos.size() > 0) {
                    for (DataVo dataVo : dataVos) {
                        LocalDateTime localDateTime = DateUtils.parse(dataVo.getTime().toEpochMilli());
                        int monthValue = localDateTime.getDayOfMonth();
                        if (i == monthValue) {
                            BigDecimal value = dataVo.getValue();
                            dataTimeFlagVo.setValue(value);
                        }
                    }
                }
                query.add(dataTimeFlagVo);

            }
        } else if (param.getTimeFlag().equals("Day")) {
            LocalDateTime dateTime = DateUtils.beginOfDay(date1);
            String s = DateUtils.formatDate(DateUtils.offsetMinute(dateTime, -8 * 60), DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            LocalDateTime dateTime1 = DateUtils.endOfDay(date1);
            String s1 = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T23:00:00Z";
            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> group(columns: [\"_time\",\"" + bindType + "Id\",\"" + bindType + "Name\"])\n" +
                   "  |> sum(column: \"value\")";
            List<DataVo> dataVos = queryApi.query(flux, DataVo.class);
            for (int i = 0; i <= 23; i++) {
                DataTimeFlagVo dataTimeFlagVo = new DataTimeFlagVo();
                dataTimeFlagVo.setTimeFlag(String.valueOf(i));
                dataTimeFlagVo.setValue(new BigDecimal("0.00"));
                if (dataVos.size() > 0) {
                    for (DataVo dataVo : dataVos) {
                        LocalDateTime localDateTime = DateUtils.parse(dataVo.getTime().toEpochMilli());
                        int monthValue = localDateTime.getHour();
                        if (i == monthValue) {
                            dataTimeFlagVo.setValue(dataVo.getValue());
                        }
                    }
                }
                query.add(dataTimeFlagVo);

            }
        } else if (StringUtils.isNotEmpty(param.getPeriod())) {
            String[] split = param.getPeriod().split(",");
            String begin = split[0];
            LocalDateTime parse = DateUtils.parse(begin, DEFAULT_DATE_FORMAT);
            String end = split[1];
            LocalDateTime parse1 = DateUtils.parse(end, DEFAULT_DATE_FORMAT);
            long between = DateUtils.between(parse, parse1, ChronoUnit.DAYS);
            String s = begin + "T00:00:00Z";
            String s1 = "";
            if (begin.equals(end)) {
                s1 = end + "T23:59:00Z";
            } else {
                s1 = end + "T00:00:00Z";
            }

            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Day\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                   "  |> group(columns: [\"_time\",\"" + bindType + "Id\",\"" + bindType + "Name\"])\n" +
                   "  |> sum(column: \"value\")";
            List<DataVo> dataVos = queryApi.query(flux, DataVo.class);
            for (int i = 0; i <= between; i++) {
                DataTimeFlagVo dataTimeFlagVo = new DataTimeFlagVo();
                dataTimeFlagVo.setTimeFlag(String.valueOf(i));
                dataTimeFlagVo.setValue(new BigDecimal("0.00"));
                if (dataVos.size() > 0) {
                    for (DataVo dataVo : dataVos) {
                        LocalDateTime localDateTime = DateUtils.parse(dataVo.getTime().toEpochMilli());
                        int monthValue = localDateTime.getHour();
                        if (i == monthValue) {
                            dataTimeFlagVo.setValue(dataVo.getValue());
                        }
                    }
                }
                query.add(dataTimeFlagVo);

            }
        }

        return query;

    }

    @Override
    public Page<DataVo> EnergyAnalysisHistory(EnergyAnalysisQueryParam param) throws ParseException {
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime date1 = date;
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        QueryApi queryApi = influxdbClient.getQueryApi();
        String flux = "";
        String flux1 = "";
        String bindType = "";
        String deviceIds = "";
        List<DataVo> dataVos = new ArrayList<>();
        List<DataVo> dataVos1 = new ArrayList<>();
        if (param.getBindType().equals("ITEM")) {
            bindType = "project";
            List<EnergyProjectItem> item = energyProjectItemService.list(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                 .eq(EnergyProjectItem::getBindType, "ITEM")
                                                                                 .eq(EnergyProjectItem::getProjectId, param.getItemId()));
            if (item.size() > 0) {
                for (EnergyProjectItem energyProjectItem : item) {
                    deviceIds = deviceIds + " and r.deviceId==\"" + energyProjectItem.getId() + "\"";
                }
            }
        } else if (param.equals("AREA")) {
            bindType = "area";
            List<EnergyProjectItem> item = energyProjectItemService.list(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                 .eq(EnergyProjectItem::getBindType, "AREA")
                                                                                 .eq(EnergyProjectItem::getProjectId, param.getItemId()));
            if (item.size() > 0) {
                for (EnergyProjectItem energyProjectItem : item) {
                    deviceIds = deviceIds + " and r.deviceId==\"" + energyProjectItem.getId() + "\"";
                }
            }
        }
        if (param.getTimeFlag().equals("Year")) {
            LocalDateTime dateTime = DateUtils.beginOfYear(date1);
            String s = DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            LocalDateTime dateTime1 = DateUtils.endOfYear(date1);
            String s1 = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Month\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> sort(columns: [\"_time\"],desc: true)\n" +
                   "  |> limit(n: " + param.getPageSize() + ", offset: " + (param.getPageNumber() - 1) * param.getPageSize() + ")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
            flux1 = "from(bucket: \"energy\")\n" +
                    "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                    "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Month\")\n" +
                    "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                    "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                    deviceIds + ")\n" +
                    "  |> sort(columns: [\"_time\"],desc: true)";
        } else if (param.getTimeFlag().equals("Month")) {
            LocalDateTime dateTime = DateUtils.beginOfMonth(date1);
            String s = DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            LocalDateTime dateTime1 = DateUtils.endOfMonth(date1);
            String s1 = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Day\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> sort(columns: [\"_time\"],desc: true)\n" +
                   "  |> limit(n: " + param.getPageSize() + ", offset: " + (param.getPageNumber() - 1) * param.getPageSize() + ")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
            flux1 = "from(bucket: \"energy\")\n" +
                    "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                    "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Day\")\n" +
                    "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                    "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                    deviceIds + ")\n" +
                    "  |> sort(columns: [\"_time\"],desc: true)";
        } else if (param.getTimeFlag().equals("Day")) {
            LocalDateTime dateTime = DateUtils.beginOfDay(date1);
            String s = DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            LocalDateTime dateTime1 = DateUtils.endOfDay(date1);
            String s1 = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T23:00:00Z";
            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> sort(columns: [\"_time\"],desc: true)\n" +
                   "  |> limit(n: " + param.getPageSize() + ", offset: " + (param.getPageNumber() - 1) * param.getPageSize() + ")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
            flux1 = "from(bucket: \"energy\")\n" +
                    "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                    "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                    "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                    "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                    deviceIds + ")\n" +
                    "  |> sort(columns: [\"_time\"],desc: true)";
        } else if (StringUtils.isNotEmpty(param.getPeriod())) {
            String[] split = param.getPeriod().split(",");
            String begin = split[0];
            String end = split[1];
            String s = begin + "T00:00:00Z";
            String s1 = "";
            if (begin.equals(end)) {
                s1 = end + "T23:59:00Z";
            } else {
                s1 = end + "T00:00:00Z";
            }

            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> sort(columns: [\"_time\"],desc: true)\n" +
                   "  |> limit(n: " + param.getPageSize() + ", offset: " + (param.getPageNumber() - 1) * param.getPageSize() + ")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
            flux1 = "from(bucket: \"energy\")\n" +
                    "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                    "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                    "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                    "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                    deviceIds + ")\n" +
                    "  |> sort(columns: [\"_time\"],desc: true)";
        }

        dataVos = queryApi.query(flux, DataVo.class);
        for (DataVo dataVo : dataVos) {
            if (param.getBindType().equals("ITEM")) {
                EnergyProject energyProject = energyProjectMapper.selectById(param.getItemId());
                dataVo.setProjectName(energyProject.getProjectName());
            } else if (param.getBindType().equals("AREA")) {
                AreaProject areaProject = areaProjectMapper.selectById(param.getItemId());
                dataVo.setProjectName(areaProject.getAreaName());
            }
        }
        dataVos1 = queryApi.query(flux1, DataVo.class);

        Page<DataVo> page = new Page<>();
        page.setTotal(dataVos1.size());
        page.setRecords(dataVos);
        return page;
    }

    @Override
    public Page<DataVo> EnergyAnalysisAppHistory(EnergyAnalysisQueryParam param) throws ParseException {
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime date1 = date;
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        QueryApi queryApi = influxdbClient.getQueryApi();
        String flux = "";
        String flux1 = "";
        String bindType = "";
        String deviceIds = "";
        List<DataVo> dataVos = new ArrayList<>();
        List<DataVo> dataVos1 = new ArrayList<>();
        if (param.getBindType().equals("ITEM")) {
            bindType = "project";
            List<EnergyProjectItem> item = energyProjectItemService.list(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                 .eq(EnergyProjectItem::getBindType, "ITEM")
                                                                                 .eq(EnergyProjectItem::getProjectId, param.getItemId()));
            if (item.size() > 0) {
                for (EnergyProjectItem energyProjectItem : item) {
                    deviceIds = deviceIds + " and r.deviceId==\"" + energyProjectItem.getId() + "\"";
                }
            }
        } else if (param.equals("AREA")) {
            bindType = "area";
            List<EnergyProjectItem> item = energyProjectItemService.list(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                 .eq(EnergyProjectItem::getBindType, "AREA")
                                                                                 .eq(EnergyProjectItem::getProjectId, param.getItemId()));
            if (item.size() > 0) {
                for (EnergyProjectItem energyProjectItem : item) {
                    deviceIds = deviceIds + " and r.deviceId==\"" + energyProjectItem.getId() + "\"";
                }
            }
        }
        if (param.getTimeFlag().equals("Year")) {
            LocalDateTime dateTime = DateUtils.beginOfYear(date1);
            String s = DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            LocalDateTime dateTime1 = DateUtils.endOfYear(date1);
            String s1 = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Month\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> filter(fn: (r) => r[\"_field\"] == \"readeValue\")\n" +
                   "  |> sort(columns: [\"_time\"],desc: true)\n" +
                   "  |> limit(n: " + param.getPageSize() + ", offset: " + (param.getPageNumber() - 1) * param.getPageSize() + ")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
            flux1 = "from(bucket: \"energy\")\n" +
                    "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                    "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Month\")\n" +
                    "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                    deviceIds + ")\n" +
                    "  |> filter(fn: (r) => r[\"_field\"] == \"readeValue\")\n" +
                    "  |> sort(columns: [\"_time\"],desc: true)";

        } else if (param.getTimeFlag().equals("Month")) {
            LocalDateTime dateTime = DateUtils.beginOfMonth(date1);
            String s = DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            LocalDateTime dateTime1 = DateUtils.endOfMonth(date1);
            String s1 = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Day\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> filter(fn: (r) => r[\"_field\"] == \"readeValue\")\n" +
                   "  |> sort(columns: [\"_time\"],desc: true)\n" +
                   "  |> limit(n: " + param.getPageSize() + ", offset: " + (param.getPageNumber() - 1) * param.getPageSize() + ")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
            flux1 = "from(bucket: \"energy\")\n" +
                    "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                    "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Day\")\n" +
                    "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                    deviceIds + ")\n" +
                    "  |> filter(fn: (r) => r[\"_field\"] == \"readeValue\")\n" +
                    "  |> sort(columns: [\"_time\"],desc: true)";

        } else if (param.getTimeFlag().equals("Day")) {
            LocalDateTime dateTime = DateUtils.beginOfDay(date1);
            String s = DateUtils.formatDate(dateTime, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
            LocalDateTime dateTime1 = DateUtils.endOfDay(date1);
            String s1 = DateUtils.formatDate(dateTime1, DEFAULT_DATE_FORMAT) + "T23:00:00Z";
            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> filter(fn: (r) => r[\"_field\"] == \"readeValue\")\n" +
                   "  |> sort(columns: [\"_time\"],desc: true)\n" +
                   "  |> limit(n: " + param.getPageSize() + ", offset: " + (param.getPageNumber() - 1) * param.getPageSize() + ")\n" +
                   "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
            flux1 = "from(bucket: \"energy\")\n" +
                    "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                    "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                    "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                    deviceIds + ")\n" +
                    "  |> filter(fn: (r) => r[\"_field\"] == \"readeValue\")\n" +
                    "  |> sort(columns: [\"_time\"],desc: true)";

        } else if (StringUtils.isNotEmpty(param.getPeriod())) {
            String[] split = param.getPeriod().split(",");
            String begin = split[0];
            String end = split[1];
            String s = begin + "T00:00:00Z";
            String s1 = "";
            if (begin.equals(end)) {
                s1 = end + "T23:59:00Z";
            } else {
                s1 = end + "T00:00:00Z";
            }

            flux = "from(bucket: \"energy\")\n" +
                   "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                   "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                   "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                   deviceIds + ")\n" +
                   "  |> filter(fn: (r) => r[\"_field\"] == \"readeValue\")\n" +
                   "  |> sort(columns: [\"_time\"],desc: true)\n" +
                   "  |> limit(n: " + param.getPageSize() + ", offset: " + (param.getPageNumber() - 1) * param.getPageSize() + ")";
            flux1 = "from(bucket: \"energy\")\n" +
                    "  |> range(start: " + s + ", stop: " + s1 + ")\n" +
                    "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                    "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + param.getItemId() + "\"" +
                    deviceIds + ")\n" +
                    "  |> filter(fn: (r) => r[\"_field\"] == \"readeValue\")\n" +
                    "  |> sort(columns: [\"_time\"],desc: true)";

        }

        dataVos = queryApi.query(flux, DataVo.class);
        dataVos1 = queryApi.query(flux1, DataVo.class);

        Page<DataVo> page = new Page<>();
        page.setTotal(dataVos1.size());
        page.setRecords(dataVos);
        return page;
    }

    /**
     * 设备趋势
     *
     * @param param diviceId 设备Id
     *
     * @return 趋势
     */
    @Override
    public List<TrendResultVO> getDeviceTrend(EnergyTrendUsageParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String flux = this.getInfluxdbGroupSql(param.getPeriod().getBegin(), param.getPeriod()
                .getEnd(), param.getDeviceId(), param.getType(), tenant);
        QueryApi queryApi = influxdbClient.getQueryApi();
        List<InfluxdbAggregationVO> queryList = queryApi.query(flux, InfluxdbAggregationVO.class);
        if (queryList.isEmpty()) {
            return Collections.emptyList();
        }
        return queryList.stream().sorted(Comparator.comparing(InfluxdbAggregationVO::getStart))
                .map(v -> TrendResultVO.builder().trendX(String.valueOf(v.getStart()).split("T")[0])
                        .trendY(v.getValue() == null ? "0" : String.valueOf(v.getValue().setScale(2, RoundingMode.HALF_UP)))
                        .build()).collect(Collectors.toList());
    }

    /**
     * 设备一段时间的用能排行
     *
     * @param param 条件
     *
     * @return
     */
    @Override
    public Pagination<KeyValueDTO> getDevicePeriodUseRanking(EnergyTrendUsageParam param) {
        if (param.getPageSize() <= 0) {
            return Pagination.empty();
        }
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        int offSet = (param.getPageNumber() - 1) * param.getPageSize() + 1;
        String fluxPage = this.getInfluxdbDeviceRankingPage(param.getPeriod().getBegin(), param.getPeriod()
                .getEnd(), param.getInstrumentType(), param.getPageSize(), offSet, tenant);
        QueryApi queryApi = influxdbClient.getQueryApi();
        List<InfluxdbAggregationVO> pageList = queryApi.query(fluxPage, InfluxdbAggregationVO.class);
        if (pageList.isEmpty()) {
            return Pagination.empty();
        }

        // 获取总数
        Long total = 0L;
        String fluxTotal = this.getInfluxdbDeviceRankingTotal(param.getPeriod().getBegin(), param.getPeriod()
                .getEnd(), param.getInstrumentType(), tenant);
        List<InfluxdbAggregationVO> totalList = queryApi.query(fluxTotal, InfluxdbAggregationVO.class);
        if (!totalList.isEmpty()) {
            total = totalList.get(0).getValue().toBigInteger().longValue();
        }
        long pages = total / param.getPageSize();
        if (total % param.getPageSize() != 0) {
            pages++;
        }

        // 获取设备名称
        Map<String, String> meterMap = new HashMap<>();
        Set<String> deviceIdList = new HashSet<>();
        List<KeyValueDTO> list = pageList.stream().map(vo -> {
            deviceIdList.add(vo.getDeviceId());
            return KeyValueDTO.builder().key(Long.parseLong(vo.getDeviceId()))
                    .value(vo.getValue() == null ? "0" : String.valueOf(vo.getValue().setScale(2, RoundingMode.HALF_UP))).build();
        }).collect(Collectors.toList());
        List<MeterDevice> meterDeviceList = meterDeviceMapper.selectList(Wrappers.<MeterDevice>lambdaQuery()
                                                                                 .in(MeterDevice::getDeviceId, deviceIdList)
                                                                        );
        Optional.of(meterDeviceList).ifPresent(value -> value.forEach(m -> meterMap.put(m.getDeviceId(), m.getDeviceName())));

        list.forEach(v -> v.setCode(meterMap.get(String.valueOf(v.getKey()))));
        return new Pagination<>(total, pages, list);
    }

    private Map<String, Object> getAnalysisOverview(String itemId, String bindType, String deviceIds) throws ParseException {
        HashMap<String, Object> remap = new HashMap<>();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        //获取当前时间
        LocalDateTime date = LocalDateTime.now();
        int hours = date.getHour();
        //获取今日能耗数据
        String s7 = DateUtils.formatDate(date, DEFAULT_DATE_FORMAT);
        String dayBegin = s7 + "T00:00:00Z";
        String dayEnd = "";
        if (hours < 10) {
            dayEnd = s7 + "T" + "0" + hours + ":59:00Z";
        } else {
            dayEnd = s7 + "T" + hours + ":59:00Z";
        }

        //获取今日能耗数据
        QueryApi queryApi = influxdbClient.getQueryApi();
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + dayBegin + ", stop: " + dayEnd + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + itemId + "\"" + deviceIds +
                      ")\n" +
                      "  |> group(columns: [\"" + bindType + "Id\",\"" + bindType + "Name\"])\n" +
                      "  |> sum(column: \"value\")";
        List<DataVo> query = queryApi.query(flux, DataVo.class);
        BigDecimal value = new BigDecimal("0.00");//今日能耗
        if (query.size() > 0) {
            DataVo dataVo = query.get(0);
            value = value.add(dataVo.getValue());
        }

        //昨日数据
        LocalDateTime offset = DateUtils.offsetDay(date, -1);
        String ysBegin = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T00:00:00Z";
        String ysEnd = "";
        if (hours < 10) {
            ysEnd = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T" + "0" + hours + ":59:00Z";
        } else {
            ysEnd = DateUtils.formatDate(offset, DEFAULT_DATE_FORMAT) + "T" + hours + ":59:00Z";
        }
        String flux1 = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + ysBegin + ", stop: " + ysEnd + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Hour\")\n" +
                       "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                       "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + itemId + "\"" + deviceIds +
                       ")\n" +
                       "  |> group(columns: [\"" + bindType + "Id\",\"" + bindType + "Name\"])\n" +
                       "  |> sum(column: \"value\")";
        List<DataVo> query1 = queryApi.query(flux1, DataVo.class);

        BigDecimal value1 = new BigDecimal("0.00");//昨日数据
        if (query1.size() > 0) {
            value1 = query1.get(0).getValue();
        }
        BigDecimal subtract = value.subtract(value1);//日数据差
        BigDecimal divide = new BigDecimal("0.00");//日环比
        if (value1.compareTo(new BigDecimal("0.00")) != 0) {
            if (value1.compareTo(new BigDecimal("0.00")) != 0) {
                divide = divide.add(subtract.abs().divide(value1, 2, RoundingMode.HALF_UP));
            }
        }

        //月数据
        LocalDateTime lastMonth = DateUtils.offsetMonth(date, -1);
        String lastMonthBegin = DateUtils.formatDate(lastMonth, "yyyy-MM-01") + "T00:00:00Z";
        String lastMonthEnd = DateUtils.formatDate(lastMonth, DEFAULT_DATE_FORMAT) + "T23:00:00Z";
        String flux2 = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + lastMonthBegin + ", stop: " + lastMonthEnd + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Day\")\n" +
                       "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                       "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + itemId + "\"" + deviceIds +
                       ")\n" +
                       "  |> group(columns: [\"" + bindType + "Id\",\"" + bindType + "Name\"])\n" +
                       "  |> sum(column: \"value\")";
        List<DataVo> query2 = queryApi.query(flux2, DataVo.class);
        //上月用量
        BigDecimal value2 = new BigDecimal("0.00");
        if (query2.size() > 0) {
            value2 = value2.add(query2.get(0).getValue());
        }

        //本月数据
        String s1 = DateUtils.formatDate(date, "yyyy-MM-01") + "T00:00:00Z";
        String s2 = DateUtils.formatDate(date, DEFAULT_DATE_FORMAT) + "T23:00:00Z";
        String flux3 = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + s1 + ", stop: " + s2 + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Day\")\n" +
                       "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                       "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + itemId + "\"" + deviceIds +
                       ")\n" +
                       "  |> group(columns: [\"" + bindType + "Id\",\"" + bindType + "Name\"])\n" +
                       "  |> sum(column: \"value\")";
        List<DataVo> query3 = queryApi.query(flux3, DataVo.class);
        //本月值
        BigDecimal value3 = new BigDecimal("0.00");
        if (query3.size() > 0) {
            value3 = value3.add(query3.get(0).getValue());
        }
        //月差值
        BigDecimal subtract2 = value3.subtract(value2);
        //月环比
        BigDecimal divide1 = new BigDecimal("0.00");
        if (value2.compareTo(new BigDecimal("0.00")) != 0) {
            if (value2.compareTo(new BigDecimal("0.00")) != 0) {
                divide1 = divide1.add(subtract2.abs().divide(value2, 2, RoundingMode.HALF_UP));
            }

        }

        //年数据
        String s3 = DateUtils.formatDate(date, "yyyy-01-01") + "T00:00:00Z";
        String s4 = DateUtils.formatDate(date, "yyyy-MM-12") + "T23:00:00Z";
        ;
        String flux4 = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + s3 + ", stop: " + s4 + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Month\")\n" +
                       "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                       "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + itemId + "\"" + deviceIds +
                       ")\n" +
                       "  |> group(columns: [\"" + bindType + "Id\",\"" + bindType + "Name\"])\n" +
                       "  |> sum(column: \"value\")";
        List<DataVo> query4 = queryApi.query(flux4, DataVo.class);
        //本年值
        BigDecimal value4 = new BigDecimal("0.00");
        if (query4.size() > 0) {
            value4 = value4.add(query4.get(0).getValue());
        }
        //去年数据
        LocalDateTime lastYear = DateUtils.offsetMonth(date, -12);
        String s5 = DateUtils.formatDate(lastYear, "yyyy-01-01") + "T00:00:00Z";
        String s6 = DateUtils.formatDate(lastYear, "yyyy-MM-dd") + "T23:00:00Z";
        String flux5 = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + s5 + ", stop: " + s6 + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + bindType + "Month\")\n" +
                       "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                       "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r." + bindType + "Id==\"" + itemId + "\"" + deviceIds +
                       ")\n" +
                       "  |> group(columns: [\"" + bindType + "Id\",\"" + bindType + "Name\"])\n" +
                       "  |> sum(column: \"value\")";
        List<DataVo> query5 = queryApi.query(flux5, DataVo.class);
        BigDecimal value5 = new BigDecimal("0.00");
        if (query5.size() > 0) {
            value5 = value5.add(query5.get(0).getValue());
        }
        //年差值
        BigDecimal subtract3 = value4.subtract(value5);
        //环比
        BigDecimal divide2 = new BigDecimal("0.00");
        if (value2.compareTo(new BigDecimal("0.00")) != 0) {
            if (value5.compareTo(new BigDecimal("0.00")) != 0) {
                divide2 = divide2.add(subtract3.abs().divide(value5, 2, RoundingMode.HALF_UP));
            }

        }

        remap.put("todayNum", value.setScale(2, RoundingMode.HALF_UP));
        remap.put("toDayProportion", divide.setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
        remap.put("toDayPoor", subtract.setScale(2, RoundingMode.HALF_UP));
        remap.put("monthNum", value3.setScale(2, RoundingMode.HALF_UP));
        remap.put("monthProportion", divide1.setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
        remap.put("montPoor", subtract2.setScale(2, RoundingMode.HALF_UP));
        remap.put("yearNum", value4.setScale(2, RoundingMode.HALF_UP));
        remap.put("yearProportion", divide2.setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
        remap.put("yearPoor", subtract3.setScale(2, RoundingMode.HALF_UP));
        return remap;
    }

    private String getInstrumentTypes() {
        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                               .eq(EnergyGate::getOpen, true)
                                                               .ne(EnergyGate::getEnergyType, InstrumentTypeEnum.ENERGY_METER.getCode()));
        String instrumentTypes = "";
        if (ListUtils.nonEmpty(list)) {
            String collect = list.stream().map(s -> "r.instrumentType==\"" + s.getEnergyType() + "\"").collect(Collectors.joining(" or "));
            instrumentTypes = "and (" + collect + ") ";
        } else {
            instrumentTypes = " and ( r.instrumentType==\"aaaaa\")";
        }

        return instrumentTypes;
    }

    private BigDecimal getData(String startTime, String endTime, Integer tenant, String instrumentTypes, Boolean isCarbon) {
        QueryApi queryApi = influxdbClient.getQueryApi();
        String areaData = CommonAreaData.getAreaData(null);
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"areaDay\")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" " + instrumentTypes + areaData + ")\n" +
                      "  |> group(columns: [\"instrumentType\"])\n" +
                      "  |> sum(column: \"value\")";
        //昨日量
        List<DataVo> query = queryApi.query(flux, DataVo.class);
        BigDecimal total = new BigDecimal("0.00");
        BigDecimal isCarbonValue = isCarbon == true ? carbonIndex : new BigDecimal("1.00");
        if (query.size() > 0) {
            for (DataVo dataVo : query) {
                total = total.add(dataVo.getValue()
                                          .multiply(new BigDecimal(CalculationIndicatorsEnum.getIndicatorsEnum(dataVo.getInstrumentType())))
                                          .multiply(isCarbonValue));
            }
        }
        return total;
    }

    private BigDecimal getDosageData(String startTime, String endTime, Integer tenant, DosageAnalysisParam param) {
        String table = param.getFilterFlag() + "Day";
        if (param.getTimeFlag().equals("TODAY")) {
            table = param.getFilterFlag() + "Day";
        } else if (param.getTimeFlag().equals("THIS_MONTH")) {
            table = param.getFilterFlag() + "Day";
        } else if (param.getTimeFlag().equals("THIS_YEAR")) {
            table = param.getFilterFlag() + "Day";
        } else if (StringUtils.isNotEmpty(param.getPeriod())) {
            table = param.getFilterFlag() + "Day";
        }

        BigDecimal total = new BigDecimal("0.00");
        String condition = "";
        if (CollectionUtils.isNotEmpty(param.getIds())) {
            String collect = param.getIds().stream().map(d -> "r." + param.getFilterFlag() + "Id==\"" + d + "\"")
                    .collect(Collectors.joining(" or "));
            condition = "and (" + collect + ")";
        } else {
            return total;
        }

        QueryApi queryApi = influxdbClient.getQueryApi();
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + table + "\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.instrumentType==\"" + param.getInstrumentType() + "\"" +
                      condition + ")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "   |> group()\n" +
                      "  |> sum(column: \"value\")";

        List<DataVo> query = queryApi.query(flux, DataVo.class);
        if (query.size() > 0) {
            for (DataVo dataVo : query) {
                total = total.add(dataVo.getValue());
            }

        }
        return total;
    }

    //递归处理数据
    private void getAreaStatisticsVo(List<AreaStatisticsVo> list) {
        List<AreaStatisticsVo> collect = list.stream().filter(areaStatisticsVo -> Objects.isNull(areaStatisticsVo.getParentId()))
                .collect(Collectors.toList());
        recursive(list, collect);
    }

    private static void recursive(List<AreaStatisticsVo> child, List<AreaStatisticsVo> collect) {
        for (AreaStatisticsVo areaListVO : collect) {
            List<AreaStatisticsVo> tree = getChild(areaListVO, child);
            areaListVO.setChildren(tree);
        }
    }

    private static List<AreaStatisticsVo> getChild(AreaStatisticsVo tree, List<AreaStatisticsVo> treeList) {
        //获取子节点
        List<AreaStatisticsVo> collect = treeList.stream()
                .filter(m -> Objects.nonNull(m.getParentId()) && m.getParentId().equals(tree.getId())).collect(Collectors.toList());
        collect.stream().forEach(m -> m.setChildren(getChild(m, treeList)));
        return collect;
    }

    private String dosageAnalysisFlux(String startTime, String endTime, Integer tenant, String instrumentType, List<String> ids,
                                      String filterFlag, String timeFlag
                                     ) {

        String measurement = filterFlag;
        if (timeFlag.equals("TODAY")) {
            measurement = measurement + DateEnum.Hour;
        } else if (timeFlag.equals("THIS_MONTH")) {
            measurement = measurement + DateEnum.Day;
        } else if (timeFlag.equals("THIS_YEAR")) {
            measurement = measurement + DateEnum.Month;
        } else {
            measurement = measurement + DateEnum.Day;
        }

        String filterIds = " and (";
        for (int i = 1; i <= ids.size(); i++) {
            if (i == 1) {
                filterIds = filterIds + "r." + filterFlag + "Id==\"" + ids.get(i - 1) + "\"";
            } else {
                filterIds = filterIds + " or r." + filterFlag + "Id==\"" + ids.get(i - 1) + "\"";
            }
            if (i == ids.size()) {
                filterIds = filterIds + ")";
            }
        }

        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + measurement + "\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.instrumentType==\"" + instrumentType + "\"" + filterIds +
                      ")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> group(columns: [\"" + filterFlag + "Id\"" + "])\n" +
                      "  |> sum(column: \"value\")";
        return flux;
    }

    private String dosageAnalysisDistributeFlux(String startTime, String endTime, Integer tenant, String instrumentType, List<String> ids,
                                                String filterFlag, String timeFlag
                                               ) {

        String measurement = filterFlag;
        if (timeFlag.equals("TODAY")) {
            measurement = measurement + DateEnum.Day;
        } else if (timeFlag.equals("THIS_MONTH")) {
            measurement = measurement + DateEnum.Month;
        } else if (timeFlag.equals("THIS_YEAR")) {
            measurement = measurement + DateEnum.Year;
        }

        String filterIds = " and (";
        for (int i = 1; i <= ids.size(); i++) {
            if (i == 1) {
                filterIds = filterIds + "r." + filterFlag + "Id==\"" + ids.get(i - 1) + "\"";
            } else {
                filterIds = filterIds + " or r." + filterFlag + "Id==\"" + ids.get(i - 1) + "\"";
            }
            if (i == ids.size()) {
                filterIds = filterIds + ")";
            }
        }

        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + measurement + "\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.instrumentType==\"" + instrumentType + "\"" + filterIds +
                      ")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> group(columns: [\"_time\"])\n" +
                      "  |> sum(column: \"value\")";
        return flux;
    }

    private String analysisDistributeFlux(String startTime, String endTime, Integer tenant, String instrumentType, List<String> ids,
                                          String filterFlag, String timeFlag, String fpg
                                         ) {
        String measurement = filterFlag;
        if (timeFlag.equals("TODAY")) {
            measurement = measurement + DateEnum.Hour;
        } else if (timeFlag.equals("THIS_MONTH")) {
            measurement = measurement + DateEnum.Day;
        } else if (timeFlag.equals("THIS_YEAR")) {
            measurement = measurement + DateEnum.Month;
        } else {
            measurement = measurement + DateEnum.Day;
        }

        String filterIds = " and (";
        for (int i = 1; i <= ids.size(); i++) {
            if (i == 1) {
                filterIds = filterIds + "r." + filterFlag + "Id==\"" + ids.get(i - 1) + "\"";
            } else {
                filterIds = filterIds + " or r." + filterFlag + "Id==\"" + ids.get(i - 1) + "\"";
            }
            if (i == ids.size()) {
                filterIds = filterIds + ")";
            }
        }

        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + measurement + "\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.instrumentType==\"" + instrumentType + "\"" + filterIds +
                      ")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> group(columns: [\"_time\"])\n" +
                      "  |> sum(column: \"" + fpg + "\")";
        return flux;
    }

    private String dosageAnalysisTemperatureFlux(String startTime, String endTime, String timeFlag) {

        String measurement = "temperature";
        if (timeFlag.equals("TODAY")) {
            measurement = measurement + DateEnum.Hour;
        } else if (timeFlag.equals("THIS_MONTH")) {
            measurement = measurement + DateEnum.Day;
        } else if (timeFlag.equals("THIS_YEAR")) {
            measurement = measurement + DateEnum.Month;
        }

        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + measurement + "\")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
        return flux;
    }

    private List<EnergyConsumptionDetailsVo> dosageAnalysisDataDetail(String startTime, String endTime, Integer tenant,
                                                                      String instrumentType, List<String> ids, String filterFlag,
                                                                      String timeFlag
                                                                     ) {

        String measurement = filterFlag;
        if (timeFlag.equals("TODAY")) {
            measurement = measurement + DateEnum.Day;
        } else if (timeFlag.equals("THIS_MONTH")) {
            measurement = measurement + DateEnum.Month;
        } else if (timeFlag.equals("THIS_YEAR")) {
            measurement = measurement + DateEnum.Year;
        }

        String filterId = ids.stream().map(s -> "r." + filterFlag + "Id==\"" + s + "\"").collect(Collectors.joining(" or "));
        String filterIds = "";
        if (StringUtils.isNotEmpty(filterId)) {
            filterIds = "and (" + filterId + ") ";
        }

        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + measurement + "\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.instrumentType==\"" + instrumentType + "\"" + filterIds +
                      ")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> group(columns: [\"" + filterFlag + "Id\"])\n" +
                      "  |> sum(column: \"value\")";
        QueryApi queryApi = influxdbClient.getQueryApi();
        List<FPGDataVo> query = queryApi.query(flux, FPGDataVo.class);
        List<EnergyConsumptionDetailsVo> energyConsumptionDetailsVos = new ArrayList<>();
        List<Long> collect = ids.stream().map(s -> {
            return Long.valueOf(s);
        }).collect(Collectors.toList());
        if (ListUtils.nonEmpty(query) && filterFlag.equals("area")) {
            List<AreaProject> areaProjects = areaProjectMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                                  .in(AreaProject::getId, collect));
            Map<Long, List<AreaProject>> collect1 = areaProjects.stream().collect(Collectors.groupingBy(AreaProject::getId));
            for (FPGDataVo fpgDataVo : query) {
                EnergyConsumptionDetailsVo energyConsumptionDetailsVo = new EnergyConsumptionDetailsVo();
                energyConsumptionDetailsVo.setName(collect1.get(Long.valueOf(fpgDataVo.getAreaId())).get(0).getAreaName());
                energyConsumptionDetailsVo.setValue(fpgDataVo.getValue().setScale(2, RoundingMode.HALF_UP));
                energyConsumptionDetailsVos.add(energyConsumptionDetailsVo);
            }

        } else if (ListUtils.nonEmpty(query) && filterFlag.equals("route")) {
            List<RouteProject> routeProjects = routeProjectMapper.selectList(Wrappers.<RouteProject>lambdaQuery()
                                                                                     .in(RouteProject::getId, collect));
            Map<Long, List<RouteProject>> collect1 = routeProjects.stream().collect(Collectors.groupingBy(RouteProject::getId));
            for (FPGDataVo fpgDataVo : query) {
                EnergyConsumptionDetailsVo energyConsumptionDetailsVo = new EnergyConsumptionDetailsVo();
                energyConsumptionDetailsVo.setName(collect1.get(Long.valueOf(fpgDataVo.getRouteId())).get(0).getRouteName());
                energyConsumptionDetailsVo.setValue(fpgDataVo.getValue().setScale(2, RoundingMode.HALF_UP));
                energyConsumptionDetailsVos.add(energyConsumptionDetailsVo);
            }
        }
        return energyConsumptionDetailsVos;
    }

    private List<EnergyConsumptionDetailsVo> dosageAnalysisFPGDataDetail(String startTime, String endTime, Integer tenant,
                                                                         String instrumentType, List<String> ids, String filterFlag,
                                                                         String timeFlag
                                                                        ) {
        QueryApi queryApi = influxdbClient.getQueryApi();
        String measurement = filterFlag;
        if (timeFlag.equals("TODAY")) {
            measurement = measurement + DateEnum.Day;
        } else if (timeFlag.equals("THIS_MONTH")) {
            measurement = measurement + DateEnum.Month;
        } else if (timeFlag.equals("THIS_YEAR")) {
            measurement = measurement + DateEnum.Year;
        }

        String filterId = ids.stream().map(s -> "r." + filterFlag + "Id==\"" + s + "\"").collect(Collectors.joining(" or "));
        String filterIds = "and (" + filterId + ") ";

        String pflux = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + measurement + "\")\n" +
                       "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.instrumentType==\"" + instrumentType + "\"" + filterIds +
                       ")\n" +
                       "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                       "  |> group(columns: [\"" + filterFlag + "Id\"])\n" +
                       "  |> sum(column: \"pvalue\")";

        String fflux = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + measurement + "\")\n" +
                       "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.instrumentType==\"" + instrumentType + "\"" + filterIds +
                       ")\n" +
                       "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                       "  |> group(columns: [\"" + filterFlag + "Id\"])\n" +
                       "  |> sum(column: \"fvalue\")";

        String gflux = "from(bucket: \"energy\")\n" +
                       "  |> range(start: " + startTime + ", stop: " + endTime + ")\n" +
                       "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + measurement + "\")\n" +
                       "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" and r.instrumentType==\"" + instrumentType + "\"" + filterIds +
                       ")\n" +
                       "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                       "  |> group(columns: [\"" + filterFlag + "Id\"])\n" +
                       "  |> sum(column: \"gvalue\")";
        List<FPGDataVo> pquery = queryApi.query(pflux, FPGDataVo.class);
        List<FPGDataVo> fquery = queryApi.query(fflux, FPGDataVo.class);
        List<FPGDataVo> gquery = queryApi.query(gflux, FPGDataVo.class);
        List<EnergyConsumptionDetailsVo> energyConsumptionDetailsVos = new ArrayList<>();
        List<Long> collect = ids.stream().map(s -> {
            return Long.valueOf(s);
        }).collect(Collectors.toList());
        if (ListUtils.nonEmpty(pquery) && filterFlag.equals("area")) {
            List<AreaProject> areaProjects = areaProjectMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                                  .in(AreaProject::getId, collect));
            Map<Long, List<AreaProject>> collect1 = areaProjects.stream().collect(Collectors.groupingBy(AreaProject::getId));
            for (FPGDataVo fpgDataVo : pquery) {
                EnergyConsumptionDetailsVo energyConsumptionDetailsVo = new EnergyConsumptionDetailsVo();
                String areaName = collect1.get(Long.valueOf(fpgDataVo.getAreaId())).get(0).getAreaName();
                energyConsumptionDetailsVo.setName(areaName);
                energyConsumptionDetailsVo.setPvalue(fpgDataVo.getPvalue());
                for (FPGDataVo dataVo : fquery) {
                    if (fpgDataVo.getAreaId().equals(dataVo.getAreaId())) {
                        energyConsumptionDetailsVo.setFvalue(dataVo.getFvalue());
                    }

                }
                for (FPGDataVo dataVo : gquery) {
                    if (fpgDataVo.getAreaId().equals(dataVo.getAreaId())) {
                        energyConsumptionDetailsVo.setGvalue(dataVo.getGvalue());
                    }

                }
                energyConsumptionDetailsVos.add(energyConsumptionDetailsVo);

            }

        } else if (ListUtils.nonEmpty(pquery) && filterFlag.equals("route")) {
            List<RouteProject> areaProjects = routeProjectMapper.selectList(Wrappers.<RouteProject>lambdaQuery()
                                                                                    .in(RouteProject::getId, collect));
            Map<Long, List<RouteProject>> collect1 = areaProjects.stream().collect(Collectors.groupingBy(RouteProject::getId));
            for (FPGDataVo fpgDataVo : pquery) {
                EnergyConsumptionDetailsVo energyConsumptionDetailsVo = new EnergyConsumptionDetailsVo();
                String routeName = collect1.get(Long.valueOf(fpgDataVo.getRouteId())).get(0).getRouteName();
                energyConsumptionDetailsVo.setName(routeName);
                energyConsumptionDetailsVo.setPvalue(fpgDataVo.getPvalue());
                for (FPGDataVo dataVo : fquery) {
                    if (fpgDataVo.getRouteId().equals(dataVo.getRouteId())) {
                        energyConsumptionDetailsVo.setFvalue(dataVo.getFvalue());
                    }

                }
                for (FPGDataVo dataVo : gquery) {
                    if (fpgDataVo.getRouteId().equals(dataVo.getRouteId())) {
                        energyConsumptionDetailsVo.setGvalue(dataVo.getGvalue());
                    }

                }
                energyConsumptionDetailsVos.add(energyConsumptionDetailsVo);
            }
        }

        for (EnergyConsumptionDetailsVo energyConsumptionDetailsVo : energyConsumptionDetailsVos) {
            BigDecimal total = BigDecimal.ZERO;
            total = total.add(energyConsumptionDetailsVo.getGvalue()).add(energyConsumptionDetailsVo.getFvalue())
                    .add(energyConsumptionDetailsVo.getPvalue());
            energyConsumptionDetailsVo.setValue(total);
        }

        return energyConsumptionDetailsVos;
    }

    private List<String> getIds(List<Long> ids, List<TreeIds> treeIds, List<String> areaOrRouteIds) {
        for (TreeIds treeId : treeIds) {
            if (ids.contains(treeId.getId())) {
                areaOrRouteIds.add(treeId.getId().toString());
            } else if (ListUtils.nonEmpty(treeId.getChildren())) {
                getIds(ids, treeId.getChildren(), areaOrRouteIds);
            }
        }
        return areaOrRouteIds;
    }

    private Map<String, Object> getEnergyMap(String begin, String end) {
        HashMap<String, Object> remap = new HashMap<>();
        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                               .eq(EnergyGate::getOpen, true)
                                                               .ne(EnergyGate::getEnergyType, InstrumentTypeEnum.ENERGY_METER.getCode())
                                                      );
        //综合能碳
        BigDecimal energyConsumption = new BigDecimal("0.00");
        //碳排量
        BigDecimal carbonEmissions = new BigDecimal("0.00");
        for (EnergyGate energyGate : list) {
            String areaData = CommonAreaData.getAreaData(energyGate.getEnergyType());
            String flux = "from(bucket: \"energy\")\n" +
                          "  |> range(start: " + begin + ", stop: " + end + ")\n" +
                          "  |> filter(fn: (r) => r[\"_measurement\"] == \"areaDay\")\n" +
                          "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                          "  |> filter(fn: (r) => r.tenant==\"" + SessionContextHolder.getRequester().getTenant() +
                          "\" and r.instrumentType==\"" + energyGate.getEnergyType() + "\"" + areaData + ")\n" +
                          "  |> group(columns: [\"instrumentType\",\"tenant\"])\n" +
                          "  |> sum(column: \"value\")";

            QueryApi queryApi = influxdbClient.getQueryApi();
            List<DataVo> query1 = queryApi.query(flux, DataVo.class);
            BigDecimal value = new BigDecimal("0.00");
            if (ListUtils.nonEmpty(query1)) {
                value = value.add(query1.get(0).getValue());
            }
            BigDecimal indicators = new BigDecimal(CalculationIndicatorsEnum.getIndicatorsEnum(energyGate.getEnergyType()));
            energyConsumption = energyConsumption.add(value.multiply(indicators));
            carbonEmissions = carbonEmissions.add(value.multiply(indicators).multiply(carbonIndex));
            remap.put(energyGate.getEnergyType(), value.setScale(2, RoundingMode.HALF_UP));
        }

        remap.put("energyConsumption", energyConsumption.setScale(2, RoundingMode.HALF_UP));
        remap.put("carbonEmissions", carbonEmissions.setScale(2, RoundingMode.HALF_UP));
        return remap;
    }

    private PeriodVo getPeriod(String period) {
        String[] split = period.split(",");
        LocalDateTime parse = DateUtils.offsetMinute(DateUtils.parse(split[0]), -8 * 60);
        LocalDateTime parse1 = DateUtils.parse(split[1]);
        String time = DateUtils.formatDate(parse, DEFAULT_DATE_FORMAT) + "T" + DateUtils.formatDate(parse, "HH:00:00") + "Z," +
                      DateUtils.formatDate(parse1, DEFAULT_DATE_FORMAT) + "T23:59:59Z";
        int l = (int) DateUtils.between(parse, parse1, ChronoUnit.DAYS);
        PeriodVo periodVo = new PeriodVo();
        periodVo.setDay(l);
        periodVo.setTime(time);
        return periodVo;
    }

    private TimeDataVo getTimePeriod(LocalDateTime begin, LocalDateTime end) {
        LocalDateTime beginTime = DateUtils.offsetMinute(begin, -8 * 60);
        String time =
                DateUtils.formatDate(beginTime, DEFAULT_DATE_FORMAT) + "T" + DateUtils.formatDate(beginTime, "HH:00:00") + "Z";
        String endTime = DateUtils.formatDate(end, DEFAULT_DATE_FORMAT) + "T23:59:59Z";
        TimeDataVo timeDataVo = new TimeDataVo();
        timeDataVo.setBegin(time);
        timeDataVo.setEnd(endTime);
        return timeDataVo;
    }

    public static TimeDataVo getTimeFlagPeriod(String timeFlag, String period) {
        String[] split = period.split(",");
        LocalDateTime st = DateUtils.parse(split[0]);
        LocalDateTime et = DateUtils.offsetMinute(DateUtils.parse(split[1]), -1);
        LocalDateTime ost = null;
        LocalDateTime oet = null;

        Integer flag = 0;
        String tableFlag = "Day";
        if (timeFlag.equals("TODAY")) {
            flag = (int) DateUtils.between(st, et, ChronoUnit.DAYS);
            ost = DateUtils.offsetMonth(st, -1);
            oet = DateUtils.offsetMonth(et, -1);
        } else if (timeFlag.equals("THIS_MONTH")) {
            tableFlag = "Month";
            Instant instant = st.atZone(ZoneId.systemDefault()).toInstant();
            Instant instantEnd = et.atZone(ZoneId.systemDefault()).toInstant();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            LocalDateTime localEndDateTime = LocalDateTime.ofInstant(instantEnd, ZoneId.systemDefault());
            LocalDate localDate = localDateTime.toLocalDate();
            LocalDate localEndDate = localEndDateTime.toLocalDate();
            flag = (int) Period.between(localDate, localEndDate).toTotalMonths();
            ost = DateUtils.offsetMonth(st, -12);
            oet = DateUtils.offsetMonth(et, -12);

        } else if (timeFlag.equals("THIS_YEAR")) {
            tableFlag = "Year";
            Instant instant = st.atZone(ZoneId.systemDefault()).toInstant();
            Instant instantEnd = et.atZone(ZoneId.systemDefault()).toInstant();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            LocalDateTime localEndDateTime = LocalDateTime.ofInstant(instantEnd, ZoneId.systemDefault());
            LocalDate localDate = localDateTime.toLocalDate();
            LocalDate localEndDate = localEndDateTime.toLocalDate();
            flag = Period.between(localDate, localEndDate).getYears();
            ost = DateUtils.offsetMonth(st, -12);
            oet = DateUtils.offsetMonth(et, -12);

        }

        LocalDateTime beginTime = DateUtils.offsetMinute(st, -8 * 60);
        String time =
                DateUtils.formatDate(beginTime, DEFAULT_DATE_FORMAT) + "T" + DateUtils.formatDate(beginTime, "HH:00:00") + "Z";
        String endTime = DateUtils.formatDate(et, DEFAULT_DATE_FORMAT) + "T23:59:59Z";
        LocalDateTime oldBeginTime = DateUtils.offsetMinute(ost, -8 * 60);
        String oldStartTime =
                DateUtils.formatDate(oldBeginTime, DEFAULT_DATE_FORMAT) + "T" + DateUtils.formatDate(oldBeginTime, "HH:00:00") +
                "Z";
        String oldEndTime = DateUtils.formatDate(oet, DEFAULT_DATE_FORMAT) + "T23:59:59Z";

        TimeDataVo timeDataVo = new TimeDataVo();
        timeDataVo.setTableFlag(tableFlag);
        timeDataVo.setBegin(time);
        timeDataVo.setEnd(endTime);
        timeDataVo.setOldBegin(oldStartTime);
        timeDataVo.setOldEnd(oldEndTime);
        timeDataVo.setTimeDifference(flag);
        timeDataVo.setSt(st);
        timeDataVo.setEt(et);
        timeDataVo.setOst(ost);
        timeDataVo.setOet(oet);
        return timeDataVo;
    }

    public String overviewTrendFlux(TimeDataVo timePeriod, String group, String instrumentTypes) {
        String areaData = CommonAreaData.getAreaData(null);
        String flux = "from(bucket: \"energy\")\n" +
                      "  |> range(start: " + timePeriod.getBegin() + ", stop: " + timePeriod.getEnd() + ")\n" +
                      "  |> filter(fn: (r) => r[\"_measurement\"] == \"areaDay\")\n" +
                      "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                      "  |> filter(fn: (r) => r.tenant==\"" + SessionContextHolder.getRequester().getTenant().toString() + "\" " +
                      instrumentTypes + areaData + ")\n" +
                      group +
                      "  |> sum(column: \"value\")";
        return flux;
    }

    private void getTreeIds(DosageAnalysisParam param) {
        List<String> objects = new ArrayList<>();
        if (param.getFilterFlag().equals("area")) {
            List<Long> areaCount = areaProjectMapper.getAreaCount(param.getInstrumentType());
            if (areaCount.size() > 0) {
                List<String> ids = getIds(areaCount, param.getTreeIds(), objects);
                if (ListUtils.nonEmpty(ids)) {
                    param.setIds(ids);
                }
            }
        } else if (param.getFilterFlag().equals("route")) {
            List<Long> routeCount = routeProjectMapper.getRouteCount(param.getInstrumentType());
            if (routeCount.size() > 0) {
                List<String> ids = getIds(routeCount, param.getTreeIds(), objects);
                if (ListUtils.nonEmpty(ids)) {
                    param.setIds(ids);
                }
            }
        }
    }

    private String getInfluxdbGroupSql(LocalDateTime start, LocalDateTime end, Long deviceId, String type, Integer tenant) {
        var utcStart = DateUtils.offset(start, ChronoUnit.HOURS, -8);
        var utcEnd = DateUtils.offset(end, ChronoUnit.HOURS, -8);
        String sql = "from(bucket: \"energy\")\n" +
                     "  |> range(start: " + DateUtils.formatDate(utcStart, DateUtils.DEFAULT_UTC_FORMATTER) + ", stop: " +
                     DateUtils.formatDate(utcEnd, DateUtils.DEFAULT_UTC_FORMATTER) + ")\n" +
                     "  |> filter(fn: (r) => r[\"_measurement\"] == \"deviceHour\")\n" +
                     "  |> filter(fn: (r) => r.tenant == \"" + tenant + "\"";
        if (deviceId != null) {
            sql = sql + " and r.deviceId == \"" + deviceId + "\"";
        }
        sql = sql + ")  \n" +
              "  |> filter(fn: (r) => r[\"_field\"]==\"value\" )\n" +
              "  |> keep(columns: [\"_start\",\"_stop\",\"_time\",\"_field\",\"_value\"])\n" +
              "  |> window(every: " + this.getInfluxdbGroupTime(type) + ", offset: -8h)\n" +
              "  |> sum()\n" +
              "  |> group(columns: [\"_time\",\"_start\",\"_stop\"], mode: \"except\")\n" +
              "  |> sort(columns: [\"_start\"], desc: true)\n" +
              "  |> pivot(rowKey: [\"_start\",\"_stop\"], columnKey: [\"_field\"], valueColumn: \"_value\")";
        return sql;
    }

    private String getInfluxdbGroupTime(String type) {
        String result = "1y";
        if (TimePeriodQueryEnum.TODAY.name().equals(type)) {
            result = "1d";
        } else if (TimePeriodQueryEnum.THIS_MONTH.name().equals(type)) {
            result = "1mo";
        }
        return result;
    }

    private String getInfluxdbDeviceRankingPage(LocalDateTime start, LocalDateTime end, String instrumentType, Integer size, Integer offset,
                                                Integer tenant
                                               ) {
        return this.getInfluxdbDeviceRanking(start, end, instrumentType, tenant) +
               "  |> sort(columns: [\"_value\"], desc: true)\n" +
               "  |> limit(n: " + size + ", offset: " + offset + ")";
    }

    private String getInfluxdbDeviceRankingTotal(LocalDateTime start, LocalDateTime end, String instrumentType, Integer tenant) {
        return this.getInfluxdbDeviceRanking(start, end, instrumentType, tenant) +
               "  |> count()";
    }

    private String getInfluxdbDeviceRanking(LocalDateTime start, LocalDateTime end, String instrumentType, Integer tenant) {
        String sql = "from(bucket: \"energy\")\n" +
                     "  |> range(start: " + DateUtils.formatDate(start, DateUtils.DEFAULT_UTC_FORMATTER) + ", stop: " +
                     DateUtils.formatDate(end, DateUtils.DEFAULT_UTC_FORMATTER) + ")\n" +
                     "  |> filter(fn: (r) => r[\"_measurement\"] == \"deviceHour\")\n" +
                     "  |> filter(fn: (r) => r.tenant == \"" + tenant + "\"";
        if (StringUtils.isNotBlank(instrumentType)) {
            sql = sql + " and r.instrumentType == \"" + instrumentType + "\"";
        }
        sql = sql + ")  \n" +
              "  |> filter(fn: (r) => r[\"_field\"]==\"value\" )\n" +
              "  |> group(columns: [\"deviceId\"])\n" +
              "  |> sum(column: \"_value\")\n" +
              "  |> group()\n";

        return sql;
    }

}
