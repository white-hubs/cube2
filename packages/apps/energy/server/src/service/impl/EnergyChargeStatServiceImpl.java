package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.common.CommonAreaData;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.AreaProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.RouteProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AreaProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyGate;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.RouteProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.ChargeTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.DateEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.FPGTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.job.EnergyJob;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.ChargeComprehensiveParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.ChargeQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.TreeIds;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeConfigService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeStatService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyGateService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeStatisticsVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeConfigVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeStatVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.PeriodVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TimeDataVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyChargeStatServiceImpl implements EnergyChargeStatService {

    private final InfluxDBClient influxdbClient;
    private final EnergyGateService energyGateService;
    private final EnergyChargeConfigService energyChargeConfigService;
    private final AreaProjectMapper areaProjectMapper;
    private final RouteProjectMapper routeProjectMapper;

    private Map<String, List<EnergyChargeStatVo>> buildFPGAmount(String instrumentType, String timeType,
                                                                 List<EnergyChargeStatVo> fvalueList,
                                                                 List<EnergyChargeStatVo> gvalueList,
                                                                 List<EnergyChargeStatVo> pvalueList
                                                                ) {
        //查询峰平谷配置价格
        EnergyChargeConfigVO chargeConfig = energyChargeConfigService.getByType(instrumentType, ChargeTypeEnum.FPG.name());
        if (CollectionUtils.isNotEmpty(fvalueList)) {
            BigDecimal price = chargeConfig.getPrice(FPGTypeEnum.FV);
            fvalueList.stream().forEach(v -> {
                BigDecimal value = v.getFvalue() != null ? v.getFvalue() : BigDecimal.ZERO;
                v.setAmount(value.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP));
                v.setTimeFlag(getTimeFlag(timeType, v.getTime()));
            });
        }
        if (CollectionUtils.isNotEmpty(gvalueList)) {
            BigDecimal price = chargeConfig.getPrice(FPGTypeEnum.GV);
            gvalueList.stream().forEach(v -> {
                BigDecimal value = v.getGvalue() != null ? v.getGvalue() : BigDecimal.ZERO;
                v.setAmount(value.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP));
                v.setTimeFlag(getTimeFlag(timeType, v.getTime()));
            });
        }
        if (CollectionUtils.isNotEmpty(pvalueList)) {
            BigDecimal price = chargeConfig.getPrice(FPGTypeEnum.PV);
            pvalueList.stream().forEach(v -> {
                BigDecimal value = v.getPvalue() != null ? v.getPvalue() : BigDecimal.ZERO;
                v.setAmount(value.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP));
                v.setTimeFlag(getTimeFlag(timeType, v.getTime()));
            });
        }
        Map<String, List<EnergyChargeStatVo>> datas = new HashMap<>();
        datas.put("fValue", checkTrendData(fvalueList, timeType));
        datas.put("pValue", checkTrendData(pvalueList, timeType));
        datas.put("gValue", checkTrendData(gvalueList, timeType));
        return datas;
    }

    private List<EnergyChargeStatVo> checkTrendData(List<EnergyChargeStatVo> datas, String timeType) {
        int start = 0;
        int end = 31;
        if ("TODAY".equals(timeType)) {//按小时分组
            start = 0;
            end = 24;
        } else if ("THIS_MONTH".equals(timeType)) {
            start = 1;
            end = DateUtils.endOfMonth(LocalDateTime.now()).getDayOfMonth();
        } else if ("THIS_YEAR".equals(timeType)) {
            start = 1;
            end = 13;
        } else {
            for (int i = 0; i < datas.size(); i++) {
                EnergyChargeStatVo energyChargeStatVo = datas.get(i);
                energyChargeStatVo.setTimeFlag(i + 1);
            }
            return datas;
        }
        IntStream.range(start, end).forEach(num -> {
            EnergyChargeStatVo energyChargeStatVo = datas.stream().filter(d -> d.getTimeFlag() == num).findFirst().orElse(null);
            if (energyChargeStatVo == null) {
                EnergyChargeStatVo build = EnergyChargeStatVo.builder()
                        .timeFlag(num)
                        .amount(BigDecimal.ZERO)
                        .build();
                datas.add(build);
            }
        });
        datas.sort(Comparator.comparing(EnergyChargeStatVo::getTimeFlag));
        return datas;
    }

    private int getTimeFlag(String timeType, Instant time) {
        int timeFlag = 0;
        ZoneId zone = ZoneId.systemDefault();
        if ("TODAY".equals(timeType)) {//按小时分组
            timeFlag = LocalDateTime.ofInstant(time, zone).getHour();
        } else if ("THIS_MONTH".equals(timeType)) {
            timeFlag = LocalDateTime.ofInstant(time, zone).getDayOfMonth();
        } else if ("THIS_YEAR".equals(timeType)) {
            timeFlag = LocalDateTime.ofInstant(time, zone).getMonthValue();
        }
        return timeFlag;
    }

    @Override
    public List<EnergyChargeStatVo> getAreaDetailChargeByTime(String period, String instrumentType, String timeType, List<Long> areaIds) {

        if (CollectionUtils.isNotEmpty(areaIds)) {
            List<EnergyChargeStatVo> objects = new ArrayList<>();
            QueryApi queryApi = influxdbClient.getQueryApi();
            //查询峰平谷配置价格
            EnergyChargeConfigVO chargeConfig = energyChargeConfigService.getByType(instrumentType, null);
            List<Long> devicesInArea = new ArrayList<>();
            String fvalueQuery = this.getQueryAreaDetailCommandForInfluxdb2(period, timeType, devicesInArea, "fvalue");
            List<EnergyChargeStatVo> fvalueList = queryApi.query(fvalueQuery, EnergyChargeStatVo.class);
            Map<String, List<EnergyChargeStatVo>> fcollect = fvalueList.stream()
                    .collect(Collectors.groupingBy(EnergyChargeStatVo::getAreaId));
            String gvalueQuery = this.getQueryAreaDetailCommandForInfluxdb2(period, timeType, devicesInArea, "gvalue");
            List<EnergyChargeStatVo> gvalueList = queryApi.query(gvalueQuery, EnergyChargeStatVo.class);
            Map<String, List<EnergyChargeStatVo>> gcollect = gvalueList.stream()
                    .collect(Collectors.groupingBy(EnergyChargeStatVo::getAreaId));
            String pvalueQuery = this.getQueryAreaDetailCommandForInfluxdb2(period, timeType, devicesInArea, "pvalue");
            List<EnergyChargeStatVo> pvalueList = queryApi.query(pvalueQuery, EnergyChargeStatVo.class);
            Map<String, List<EnergyChargeStatVo>> pcollect = pvalueList.stream()
                    .collect(Collectors.groupingBy(EnergyChargeStatVo::getAreaId));
            for (Long areaId : areaIds) {
                //峰平谷费用总计
                if (chargeConfig.getChargeType() != null && chargeConfig.getChargeType().equals(ChargeTypeEnum.FPG.name())) {
                    List<EnergyChargeStatVo> flist = fcollect.get(areaId.toString());
                    BigDecimal fvalue = CollectionUtils.isNotEmpty(flist) ? flist.get(0).getFvalue() : BigDecimal.ZERO;
                    BigDecimal fprice = chargeConfig.getPrice(FPGTypeEnum.FV);
                    BigDecimal fAmount = fvalue != null ? fvalue.multiply(fprice) : BigDecimal.ZERO;

                    List<EnergyChargeStatVo> glist = gcollect.get(areaId.toString());
                    BigDecimal gvalue = CollectionUtils.isNotEmpty(glist) ? glist.get(0).getGvalue() : BigDecimal.ZERO;
                    BigDecimal gprice = chargeConfig.getPrice(FPGTypeEnum.GV);
                    BigDecimal gAmount = gvalue != null ? gvalue.multiply(gprice) : BigDecimal.ZERO;

                    List<EnergyChargeStatVo> plist = pcollect.get(areaId.toString());
                    BigDecimal pvalue = CollectionUtils.isNotEmpty(plist) ? plist.get(0).getPvalue() : BigDecimal.ZERO;
                    BigDecimal pprice = chargeConfig.getPrice(FPGTypeEnum.PV);
                    BigDecimal pAmount = pvalue != null ? pvalue.multiply(pprice) : BigDecimal.ZERO;

                    BigDecimal totalAmount = fAmount.add(gAmount).add(pAmount);
                    //获取区域名称
                    AreaProject areaProject = areaProjectMapper.selectById(areaId);
                    EnergyChargeStatVo build = EnergyChargeStatVo.builder()
                            .projectName(areaProject.getAreaName())
                            .amount(totalAmount)
                            .fvAmount(fAmount)
                            .gvAmount(gAmount)
                            .pvAmount(pAmount)
                            .build();
                    objects.add(build);
                } else {
                    String valueQuery = this.getQueryAreaDetailCommandForInfluxdb2(period, timeType, devicesInArea, "value");
                    List<EnergyChargeStatVo> valueList = queryApi.query(valueQuery, EnergyChargeStatVo.class);
                    BigDecimal value = new BigDecimal("0.00");
                    for (EnergyChargeStatVo energyChargeStatVo : valueList) {
                        value = value.add(energyChargeStatVo.getValue());
                    }

                    BigDecimal totalAmount = chargeConfig.getAmoutByUsage(value);
                    //获取区域名称
                    AreaProject areaProject = areaProjectMapper.selectById(areaId);
                    EnergyChargeStatVo build = EnergyChargeStatVo.builder()
                            .projectName(areaProject.getAreaName())
                            .amount(totalAmount)
                            .build();
                    objects.add(build);
                }
            }
            return objects;
        } else {
            return Collections.emptyList();
        }

    }

    @Override
    public Map<String, List<EnergyChargeStatVo>> getChargeForFPGTrendByTime(ChargeQueryParam param) {
        //根据仪表类型和时间查询趋势用量
        QueryApi queryApi = influxdbClient.getQueryApi();
        String fvalueQuery = this.getQueryCommandForInfluxdb3(param.getPeriod(), param.getTimeType(), param.getInstrumentType(), "fvalue");
        List<EnergyChargeStatVo> fvalueList = queryApi.query(fvalueQuery, EnergyChargeStatVo.class);
        String gvalueQuery = this.getQueryCommandForInfluxdb3(param.getPeriod(), param.getTimeType(), param.getInstrumentType(), "gvalue");
        List<EnergyChargeStatVo> gvalueList = queryApi.query(gvalueQuery, EnergyChargeStatVo.class);
        String pvalueQuery = this.getQueryCommandForInfluxdb3(param.getPeriod(), param.getTimeType(), param.getInstrumentType(), "pvalue");
        List<EnergyChargeStatVo> pvalueList = queryApi.query(pvalueQuery, EnergyChargeStatVo.class);

        return buildFPGAmount(param.getInstrumentType(), param.getTimeType(), fvalueList, gvalueList, pvalueList);
    }

    @Override
    public Map<String, Object> getComprehensiveExpenses(ChargeComprehensiveParam param) {
        Map<String, Object> remap = new HashMap<>();
        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                               .eq(EnergyGate::getOpen, true)
                                                               .ne(EnergyGate::getEnergyType, InstrumentTypeEnum.ENERGY_METER.getCode()));
        if (ListUtils.nonEmpty(list)) {
            EnergyChargeStatVo energyChargeStatVo = new EnergyChargeStatVo();
            TimeDataVo chargeTimeFlagPeriod = getChargeTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal qoqtotal = BigDecimal.ZERO;
            BigDecimal yoytotal = BigDecimal.ZERO;
            for (EnergyGate energyGate : list) {
                //当前值
                EnergyChargeStatVo instrumentTypeChargeStat = getInstrumentTypeChargeStat(energyGate.getEnergyType(), chargeTimeFlagPeriod.getTableFlag(), chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd());
                BigDecimal num = instrumentTypeChargeStat.getAmount();
                total = total.add(num);
                //环比值
                EnergyChargeStatVo instrumentTypeChargeStatQoq = getInstrumentTypeChargeStat(energyGate.getEnergyType(), chargeTimeFlagPeriod.getTableFlag(), chargeTimeFlagPeriod.getQoqBegin(), chargeTimeFlagPeriod.getQoqEnd());
                BigDecimal qoqNum = instrumentTypeChargeStatQoq.getAmount();
                qoqtotal = qoqtotal.add(qoqNum);
                if (qoqNum.compareTo(BigDecimal.ZERO) == 0) {
                    instrumentTypeChargeStat.setAmountQoQ(new BigDecimal("100"));
                } else {
                    instrumentTypeChargeStat.setAmountQoQ(num.subtract(qoqNum).divide(qoqNum, 2, RoundingMode.HALF_UP));
                }
                //同比值
                EnergyChargeStatVo instrumentTypeChargeStatYoy = getInstrumentTypeChargeStat(energyGate.getEnergyType(), chargeTimeFlagPeriod.getTableFlag(), chargeTimeFlagPeriod.getOldBegin(), chargeTimeFlagPeriod.getOldEnd());
                BigDecimal yoyNum = instrumentTypeChargeStatYoy.getAmount();
                yoytotal = yoytotal.add(yoyNum);
                if (yoyNum.compareTo(BigDecimal.ZERO) == 0) {
                    instrumentTypeChargeStat.setAmountYoY(new BigDecimal("100"));
                } else {
                    instrumentTypeChargeStat.setAmountYoY(num.subtract(yoyNum).divide(yoyNum, 2, RoundingMode.HALF_UP));
                }
                remap.put(energyGate.getEnergyType(), instrumentTypeChargeStat);
            }
            //环比
            if (qoqtotal.compareTo(BigDecimal.ZERO) == 0) {
                energyChargeStatVo.setAmountQoQ(new BigDecimal("100"));
            } else {
                energyChargeStatVo.setAmountQoQ(total.subtract(qoqtotal).divide(qoqtotal, 2, RoundingMode.HALF_UP));
            }
            //同比值
            if (yoytotal.compareTo(BigDecimal.ZERO) == 0) {
                energyChargeStatVo.setAmountYoY(new BigDecimal("100"));
            } else {
                energyChargeStatVo.setAmountYoY(total.subtract(yoytotal).divide(yoytotal, 2, RoundingMode.HALF_UP));
            }
            remap.put("total", energyChargeStatVo);
        } else {
            remap.put("total", new EnergyChargeStatVo());
        }
        return remap;
    }

    @Override
    public Map<String, List<ChargeStatisticsVo>> getComprehensiveExpensesTrend(ChargeComprehensiveParam param) throws Exception {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Map<String, List<ChargeStatisticsVo>> remap = null;

        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                               .eq(EnergyGate::getOpen, true)
                                                               .ne(EnergyGate::getEnergyType, InstrumentTypeEnum.ENERGY_METER.getCode()));
        if (ListUtils.nonEmpty(list)) {
            TimeDataVo chargeTimeFlagPeriod = getChargeTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());
            int size = list.size();
            ExecutorService executorService = Executors.newFixedThreadPool(size);
            List<Future<List<ChargeStatisticsVo>>> futures = new ArrayList<Future<List<ChargeStatisticsVo>>>(size);
            for (int i = 0; i < size; i++) {
                EnergyGate energyGate = list.get(i);
                Callable<List<ChargeStatisticsVo>> task = new Callable<List<ChargeStatisticsVo>>() {
                    @Override
                    public List<ChargeStatisticsVo> call() {
                        return getInstrumentTypeChargeTrend(energyGate.getEnergyType(), chargeTimeFlagPeriod, tenant);
                    }
                };
                //添加线程到队列
                futures.add(executorService.submit(task));
            }
            List<ChargeStatisticsVo> chargeStatisticsVoList = new ArrayList<>();
            for (int i = 0; i < futures.size(); i++) {
                Future<List<ChargeStatisticsVo>> listFuture = futures.get(i);
                List<ChargeStatisticsVo> chargeStatisticsVos = listFuture.get();
                chargeStatisticsVoList.addAll(chargeStatisticsVos);
            }
            Map<String, List<ChargeStatisticsVo>> collect = chargeStatisticsVoList.stream()
                    .collect(Collectors.groupingBy(ChargeStatisticsVo::getInstrumentType));
            return collect;
        } else {
            return remap;
        }
    }

    @Override
    public EnergyChargeStatVo getAreaComprehensiveExpenses(ChargeComprehensiveParam param) {
        TimeDataVo chargeTimeFlagPeriod = getChargeTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());
        getTreeIds(param);
        List<String> ids = param.getIds();
        String dataIds = "";
        if (ListUtils.nonEmpty(ids)) {
            String collect = ids.stream().map(s -> "r." + param.getFilterFlag() + "Id==\"" + s + "\"").collect(Collectors.joining(" or "));
            dataIds = "and (" + collect + ")";
        } else {
            dataIds = "and (" + param.getFilterFlag() + "Id==\"1111\")";
        }
        EnergyChargeStatVo instrumentTypeAreaOrRouteChargeStat = getInstrumentTypeAreaOrRouteChargeStat(param.getInstrumentType(), param.getFilterFlag(), chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), dataIds);
        EnergyChargeStatVo instrumentTypeAreaOrRouteChargeOldStat = getInstrumentTypeAreaOrRouteChargeStat(param.getInstrumentType(), param.getFilterFlag(), chargeTimeFlagPeriod.getQoqBegin(), chargeTimeFlagPeriod.getQoqEnd(), dataIds);

        if (param.getFilterFlag().equals(InstrumentTypeEnum.ELECTRICITY_METER.getCode())) {
            //环比
            if (Objects.isNull(instrumentTypeAreaOrRouteChargeOldStat)) {
                instrumentTypeAreaOrRouteChargeOldStat.setAmountQoQ(new BigDecimal(100));
                instrumentTypeAreaOrRouteChargeOldStat.setFvAmountQoQ(new BigDecimal(100));
                instrumentTypeAreaOrRouteChargeOldStat.setGvAmountQoQ(new BigDecimal(100));
                instrumentTypeAreaOrRouteChargeOldStat.setPvAmount(new BigDecimal(100));
            } else {
                instrumentTypeAreaOrRouteChargeOldStat.setAmountQoQ(instrumentTypeAreaOrRouteChargeStat.getAmount()
                                                                            .subtract(instrumentTypeAreaOrRouteChargeOldStat.getAmount())
                                                                            .divide(instrumentTypeAreaOrRouteChargeOldStat.getAmount(), 2, RoundingMode.HALF_UP));
                instrumentTypeAreaOrRouteChargeOldStat.setFvAmountQoQ(instrumentTypeAreaOrRouteChargeStat.getFvAmount()
                                                                              .subtract(instrumentTypeAreaOrRouteChargeOldStat.getFvAmount())
                                                                              .divide(instrumentTypeAreaOrRouteChargeOldStat.getFvAmount(), 2, RoundingMode.HALF_UP));
                instrumentTypeAreaOrRouteChargeOldStat.setGvAmountQoQ(instrumentTypeAreaOrRouteChargeStat.getGvAmount()
                                                                              .subtract(instrumentTypeAreaOrRouteChargeOldStat.getGvAmount())
                                                                              .divide(instrumentTypeAreaOrRouteChargeOldStat.getGvAmount(), 2, RoundingMode.HALF_UP));
                instrumentTypeAreaOrRouteChargeOldStat.setPvAmount(instrumentTypeAreaOrRouteChargeStat.getPvAmount()
                                                                           .subtract(instrumentTypeAreaOrRouteChargeOldStat.getPvAmount())
                                                                           .divide(instrumentTypeAreaOrRouteChargeOldStat.getPvAmount(), 2, RoundingMode.HALF_UP));
            }
        } else {
            //环比
            if (Objects.isNull(instrumentTypeAreaOrRouteChargeOldStat) ||
                instrumentTypeAreaOrRouteChargeOldStat.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                instrumentTypeAreaOrRouteChargeOldStat.setAmountQoQ(new BigDecimal(100));
            } else {
                instrumentTypeAreaOrRouteChargeOldStat.setAmountQoQ(instrumentTypeAreaOrRouteChargeStat.getAmount()
                                                                            .subtract(instrumentTypeAreaOrRouteChargeOldStat.getAmount())
                                                                            .divide(instrumentTypeAreaOrRouteChargeOldStat.getAmount(), 2, RoundingMode.HALF_UP));
            }
        }

        return instrumentTypeAreaOrRouteChargeStat;
    }

    @Override
    public List<ChargeStatisticsVo> getAreaOrRouteComprehensiveTrend(ChargeComprehensiveParam param) {
        TimeDataVo chargeTimeFlagPeriod = getChargeTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());
        getTreeIds(param);
        List<String> ids = param.getIds();
        String dataIds = "";
        if (ListUtils.nonEmpty(ids)) {
            String collect = ids.stream().map(s -> "r." + param.getFilterFlag() + "Id==\"" + s + "\"").collect(Collectors.joining(" or "));
            dataIds = "and (" + collect + ")";
        } else {
            dataIds = "and (" + param.getFilterFlag() + "Id==\"1111\")";
        }
        QueryApi queryApi = influxdbClient.getQueryApi();
        //目前只要不是电表 都是按阶梯价计算；电表有可能按峰平谷计算
        EnergyChargeConfigVO chargeConfig = energyChargeConfigService.getByType(param.getInstrumentType(), null);
        Assert.isNotAcceptable(Objects.isNull(chargeConfig) || Objects.isNull(chargeConfig.getChargeType()), "计费方式未配置");
        List<ChargeStatisticsVo> chargeTrend = null;
        //阶梯价
        if (chargeConfig.getChargeType() != null && chargeConfig.getChargeType().equals(ChargeTypeEnum.JTJ.name())) {
            String value = getChargeQueryCommandForInfluxdbAreaOrRouteTrend(param.getFilterFlag(), chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "value", dataIds, param.getInstrumentType());
            List<EnergyChargeStatVo> query = queryApi.query(value, EnergyChargeStatVo.class);
            Map<Instant, List<EnergyChargeStatVo>> collect = query.stream().collect(Collectors.groupingBy(EnergyChargeStatVo::getTime));
            chargeTrend = getChargeTrend(param.getInstrumentType(), chargeConfig, chargeTimeFlagPeriod, collect, null, null, null);
        } else {
            //如果是峰平谷，需要单独计算峰平谷的费用，再求和
            String fvalueQuery = this.getChargeQueryCommandForInfluxdbAreaOrRouteTrend(param.getFilterFlag(), chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "fvalue", dataIds, param.getInstrumentType());
            List<EnergyChargeStatVo> fvalueList = queryApi.query(fvalueQuery, EnergyChargeStatVo.class);
            Map<Instant, List<EnergyChargeStatVo>> fcollect = fvalueList.stream()
                    .collect(Collectors.groupingBy(EnergyChargeStatVo::getTime));

            String pvalueQuery = this.getChargeQueryCommandForInfluxdbAreaOrRouteTrend(param.getFilterFlag(), chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "pvalue", dataIds, param.getInstrumentType());
            List<EnergyChargeStatVo> pvalueList = queryApi.query(pvalueQuery, EnergyChargeStatVo.class);
            Map<Instant, List<EnergyChargeStatVo>> pcollect = pvalueList.stream()
                    .collect(Collectors.groupingBy(EnergyChargeStatVo::getTime));

            String gvalueQuery = this.getChargeQueryCommandForInfluxdbAreaOrRouteTrend(param.getFilterFlag(), chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "gvalue", dataIds, param.getInstrumentType());
            List<EnergyChargeStatVo> gvalueList = queryApi.query(gvalueQuery, EnergyChargeStatVo.class);
            Map<Instant, List<EnergyChargeStatVo>> gcollect = gvalueList.stream()
                    .collect(Collectors.groupingBy(EnergyChargeStatVo::getTime));
            chargeTrend = getChargeTrend(param.getInstrumentType(), chargeConfig, chargeTimeFlagPeriod, null, fcollect, pcollect, gcollect);
        }
        return chargeTrend;
    }

    @Override
    public List<ChargeStatisticsVo> getAreaOrRouteComprehensiveDetail(ChargeComprehensiveParam param) {
        TimeDataVo chargeTimeFlagPeriod = getChargeTimeFlagPeriod(param.getTimeFlag(), param.getPeriod());
        getTreeIds(param);
        List<String> ids = param.getIds();
        String dataIds = "";
        if (ListUtils.nonEmpty(ids)) {
            String collect = ids.stream().map(s -> "r." + param.getFilterFlag() + "Id==\"" + s + "\"").collect(Collectors.joining(" or "));
            dataIds = "and (" + collect + ")";
        } else {
            dataIds = "and (" + param.getFilterFlag() + "Id==\"1111\")";
        }
        QueryApi queryApi = influxdbClient.getQueryApi();
        //目前只要不是电表 都是按阶梯价计算；电表有可能按峰平谷计算
        EnergyChargeConfigVO chargeConfig = energyChargeConfigService.getByType(param.getInstrumentType(), null);
        List<ChargeStatisticsVo> chargeDetail = null;
        if (Objects.nonNull(chargeConfig)) {

            //阶梯价
            if (chargeConfig.getChargeType() != null && chargeConfig.getChargeType().equals(ChargeTypeEnum.JTJ.name())) {
                String value = getChargeQueryCommandForInfluxdbAreaOrRouteDetail(param.getFilterFlag(), chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "value", dataIds, param.getInstrumentType());
                List<EnergyChargeStatVo> query = queryApi.query(value, EnergyChargeStatVo.class);
                chargeDetail = getChargeDetail(param.getInstrumentType(), chargeConfig, chargeTimeFlagPeriod, query, null, null, ids);
            } else {
                //如果是峰平谷，需要单独计算峰平谷的费用，再求和
                String fvalueQuery = this.getChargeQueryCommandForInfluxdbAreaOrRouteTrend(param.getFilterFlag(), chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "fvalue", dataIds, param.getInstrumentType());
                List<EnergyChargeStatVo> fvalueList = queryApi.query(fvalueQuery, EnergyChargeStatVo.class);

                String pvalueQuery = this.getChargeQueryCommandForInfluxdbAreaOrRouteTrend(param.getFilterFlag(), chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "pvalue", dataIds, param.getInstrumentType());
                List<EnergyChargeStatVo> pvalueList = queryApi.query(pvalueQuery, EnergyChargeStatVo.class);

                String gvalueQuery = this.getChargeQueryCommandForInfluxdbAreaOrRouteTrend(param.getFilterFlag(), chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "gvalue", dataIds, param.getInstrumentType());
                List<EnergyChargeStatVo> gvalueList = queryApi.query(gvalueQuery, EnergyChargeStatVo.class);

                chargeDetail = getChargeDetail(param.getInstrumentType(), chargeConfig, chargeTimeFlagPeriod, fvalueList, pvalueList, gvalueList, ids);
            }
        }
        return chargeDetail;
    }

    private List<ChargeStatisticsVo> getInstrumentTypeChargeTrend(String instrumentType, TimeDataVo chargeTimeFlagPeriod, Integer tenant) {
        SessionContextHolder.requestTenant(tenant);
        QueryApi queryApi = influxdbClient.getQueryApi();
        //目前只要不是电表 都是按阶梯价计算；电表有可能按峰平谷计算
        EnergyChargeConfigVO chargeConfig = energyChargeConfigService.getByType(instrumentType, null);
        List<ChargeStatisticsVo> chargeTrend = new ArrayList<>();
        if (Objects.nonNull(chargeConfig) && Objects.nonNull(chargeConfig.getChargeType())) {
            String areaData = CommonAreaData.getAreaData(instrumentType);
            //阶梯价
            if (chargeConfig.getChargeType() != null && chargeConfig.getChargeType().equals(ChargeTypeEnum.JTJ.name())) {
                String value = getChargeQueryCommandForInfluxdbTrend(chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "value", areaData, instrumentType);
                List<EnergyChargeStatVo> query = queryApi.query(value, EnergyChargeStatVo.class);
                Map<Instant, List<EnergyChargeStatVo>> collect = query.stream().collect(Collectors.groupingBy(EnergyChargeStatVo::getTime));
                chargeTrend = getChargeTrend(instrumentType, chargeConfig, chargeTimeFlagPeriod, collect, null, null, null);
            } else {
                //如果是峰平谷，需要单独计算峰平谷的费用，再求和
                String fvalueQuery = this.getChargeQueryCommandForInfluxdbTrend(chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "fvalue", areaData, instrumentType);
                List<EnergyChargeStatVo> fvalueList = queryApi.query(fvalueQuery, EnergyChargeStatVo.class);
                Map<Instant, List<EnergyChargeStatVo>> fcollect = fvalueList.stream()
                        .collect(Collectors.groupingBy(EnergyChargeStatVo::getTime));

                String pvalueQuery = this.getChargeQueryCommandForInfluxdbTrend(chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "pvalue", areaData, instrumentType);
                List<EnergyChargeStatVo> pvalueList = queryApi.query(pvalueQuery, EnergyChargeStatVo.class);
                Map<Instant, List<EnergyChargeStatVo>> pcollect = pvalueList.stream()
                        .collect(Collectors.groupingBy(EnergyChargeStatVo::getTime));

                String gvalueQuery = this.getChargeQueryCommandForInfluxdbTrend(chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), chargeTimeFlagPeriod.getTableFlag(), "gvalue", areaData, instrumentType);
                List<EnergyChargeStatVo> gvalueList = queryApi.query(gvalueQuery, EnergyChargeStatVo.class);
                Map<Instant, List<EnergyChargeStatVo>> gcollect = gvalueList.stream()
                        .collect(Collectors.groupingBy(EnergyChargeStatVo::getTime));
                chargeTrend = getChargeTrend(instrumentType, chargeConfig, chargeTimeFlagPeriod, null, fcollect, pcollect, gcollect);
            }
        }
        return chargeTrend;
    }

    private List<ChargeStatisticsVo> getChargeTrend(String instrumentType, EnergyChargeConfigVO chargeConfig,
                                                    TimeDataVo chargeTimeFlagPeriod, Map<Instant, List<EnergyChargeStatVo>> collect,
                                                    Map<Instant, List<EnergyChargeStatVo>> fcollect,
                                                    Map<Instant, List<EnergyChargeStatVo>> pcollect,
                                                    Map<Instant, List<EnergyChargeStatVo>> gcollect
                                                   ) {
        LocalDateTime st = chargeTimeFlagPeriod.getSt();
        List<ChargeStatisticsVo> chargeStatisticsVos = new ArrayList<>();
        String tableFlag = chargeTimeFlagPeriod.getTableFlag();
        ChronoUnit dateField = null;
        DateEnum dateEnum = null;
        String timeFlag = "";
        if (tableFlag.equals("Day")) {
            dateField = ChronoUnit.DAYS;
            dateEnum = DateEnum.Day;
            timeFlag = "TODAY";
        } else if (tableFlag.equals("Month")) {
            dateField = ChronoUnit.MONTHS;
            dateEnum = DateEnum.Month;
            timeFlag = "THIS_MONTH";
        } else if (tableFlag.equals("Year")) {
            dateField = ChronoUnit.YEARS;
            dateEnum = DateEnum.Year;
            timeFlag = "THIS_YEAR";
        }
        for (int i = 0; i < chargeTimeFlagPeriod.getTimeDifference(); i++) {
            ChargeStatisticsVo chargeStatisticsVo = new ChargeStatisticsVo();
            LocalDateTime offset = st;
            if (DateEnum.Day.equals(dateEnum)) {
                offset = DateUtils.offsetDay(st, i);
            } else if (DateEnum.Month.equals(dateEnum)) {
                offset = DateUtils.offsetMonth(st, i);
            } else if (DateEnum.Year.equals(dateEnum)) {
                offset = DateUtils.offsetMonth(st, i * 12);
            }
            LocalDateTime time = EnergyJob.switchTime(dateEnum, offset);
            Instant instant = time.atZone(ZoneId.systemDefault()).toInstant();
            String format = EnergyJob.timeFormat(timeFlag);
            chargeStatisticsVo.setTimeFlag(DateUtils.formatDate(offset, format));
            BigDecimal total = BigDecimal.ZERO;
            if (chargeConfig.getChargeType().equals(ChargeTypeEnum.JTJ.name())) {
                List<EnergyChargeStatVo> listJTJ = collect.get(instant);
                total = total.add(chargeConfig.getAmoutByUsage(
                        ListUtils.isEmpty(listJTJ) ? BigDecimal.ZERO : listJTJ.get(0).getValue()));
            } else {
                List<EnergyChargeStatVo> flist = fcollect.get(instant);
                BigDecimal fAmount = chargeConfig.getFPGAmount(FPGTypeEnum.FV,
                                                               ListUtils.isEmpty(flist) ? BigDecimal.ZERO : flist.get(0).getFvalue());
                total = total.add(fAmount);
                chargeStatisticsVo.setFvAmount(fAmount);
                //平
                List<EnergyChargeStatVo> plist = pcollect.get(instant);
                BigDecimal pAmount = chargeConfig.getFPGAmount(FPGTypeEnum.PV,
                                                               ListUtils.isEmpty(plist) ? BigDecimal.ZERO : plist.get(0).getPvalue());
                total = total.add(pAmount);
                chargeStatisticsVo.setPvAmount(pAmount);
                //谷
                List<EnergyChargeStatVo> glist = gcollect.get(instant);
                BigDecimal gAmount = chargeConfig.getFPGAmount(FPGTypeEnum.GV,
                                                               ListUtils.isEmpty(glist) ? BigDecimal.ZERO : glist.get(0).getGvalue());
                total = total.add(gAmount);
                chargeStatisticsVo.setGvAmount(gAmount);
            }
            chargeStatisticsVo.setAmount(total);
            chargeStatisticsVo.setInstrumentType(instrumentType);
            chargeStatisticsVos.add(chargeStatisticsVo);
        }

        return chargeStatisticsVos;
    }

    private List<ChargeStatisticsVo> getChargeDetail(String instrumentType, EnergyChargeConfigVO chargeConfig,
                                                     TimeDataVo chargeTimeFlagPeriod, List<EnergyChargeStatVo> query,
                                                     List<EnergyChargeStatVo> pquery, List<EnergyChargeStatVo> gquery, List<String> ids
                                                    ) {
        List<ChargeStatisticsVo> chargeStatisticsVos = new ArrayList<>();
        Map<String, String> collect = null;
        Map<String, BigDecimal> pcollect = null;
        Map<String, BigDecimal> gcollect = null;
        if (ListUtils.nonEmpty(query)) {
            if (chargeTimeFlagPeriod.getTableFlag().equals("area")) {
                List<AreaProject> areaProjects = areaProjectMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                                      .in(AreaProject::getId, ids));
                collect = areaProjects.stream().collect(Collectors.toMap(s -> s.getId().toString(), s -> s.getAreaName()));
                if (ListUtils.nonEmpty(pquery) && ListUtils.nonEmpty(gquery)) {
                    pcollect = pquery.stream().collect(Collectors.toMap(EnergyChargeStatVo::getAreaId, s -> s.getPvalue()));
                    gcollect = gquery.stream().collect(Collectors.toMap(EnergyChargeStatVo::getAreaId, s -> s.getPvalue()));
                }
            } else {
                List<RouteProject> routeProjects = routeProjectMapper.selectList(Wrappers.<RouteProject>lambdaQuery()
                                                                                         .in(RouteProject::getId, ids));
                collect = routeProjects.stream().collect(Collectors.toMap(s -> s.getId().toString(), s -> s.getRouteName()));
                if (ListUtils.nonEmpty(pquery) && ListUtils.nonEmpty(gquery)) {
                    pcollect = pquery.stream().collect(Collectors.toMap(EnergyChargeStatVo::getRouteId, s -> s.getPvalue()));
                    gcollect = gquery.stream().collect(Collectors.toMap(EnergyChargeStatVo::getRouteId, s -> s.getPvalue()));
                }
            }
            for (EnergyChargeStatVo energyChargeStatVo : query) {
                ChargeStatisticsVo chargeStatisticsVo = new ChargeStatisticsVo();
                BigDecimal total = BigDecimal.ZERO;

                if (chargeConfig.getChargeType().equals(ChargeTypeEnum.JTJ.name())) {
                    total = total.add(chargeConfig.getAmoutByUsage(
                            Objects.isNull(energyChargeStatVo.getValue()) ? BigDecimal.ZERO : energyChargeStatVo.getValue()));
                    if (chargeTimeFlagPeriod.getTableFlag().equals("area")) {
                        chargeStatisticsVo.setName(collect.get(energyChargeStatVo.getAreaId()));
                    } else {
                        chargeStatisticsVo.setName(collect.get(energyChargeStatVo.getRouteId()));
                    }
                } else {
                    BigDecimal pvalue = BigDecimal.ZERO;
                    BigDecimal gvalue = BigDecimal.ZERO;
                    if (chargeTimeFlagPeriod.getTableFlag().equals("area")) {
                        pvalue = pvalue.add(pcollect.get(energyChargeStatVo.getAreaId()));
                        gvalue = gvalue.add(gcollect.get(energyChargeStatVo.getAreaId()));
                        chargeStatisticsVo.setName(collect.get(energyChargeStatVo.getAreaId()));
                    } else {
                        gvalue = gvalue.add(pcollect.get(energyChargeStatVo.getRouteId()));
                        gvalue = gvalue.add(gcollect.get(energyChargeStatVo.getRouteId()));
                        chargeStatisticsVo.setName(collect.get(energyChargeStatVo.getRouteId()));
                    }
                    //峰值
                    BigDecimal fAmount = chargeConfig.getFPGAmount(FPGTypeEnum.FV,
                                                                   Objects.isNull(energyChargeStatVo.getFvalue()) ? BigDecimal.ZERO
                                                                                                                  : energyChargeStatVo.getFvalue());
                    total = total.add(fAmount);
                    chargeStatisticsVo.setFvAmount(fAmount);
                    //平
                    BigDecimal pAmount = chargeConfig.getFPGAmount(FPGTypeEnum.PV, pvalue);
                    total = total.add(pAmount);
                    chargeStatisticsVo.setPvAmount(pAmount);
                    //谷
                    BigDecimal gAmount = chargeConfig.getFPGAmount(FPGTypeEnum.GV, gvalue);
                    total = total.add(gAmount);
                    chargeStatisticsVo.setGvAmount(gAmount);
                }
                chargeStatisticsVo.setAmount(total);
                chargeStatisticsVos.add(chargeStatisticsVo);
            }
        }
        return chargeStatisticsVos;
    }

    private EnergyChargeStatVo getInstrumentTypeChargeStat(String instrumentType, String tableFlag, String begin, String end) {
        QueryApi queryApi = influxdbClient.getQueryApi();
        //目前只要不是电表 都是按阶梯价计算；电表有可能按峰平谷计算
        EnergyChargeConfigVO chargeConfig = energyChargeConfigService.getByType(instrumentType, null);
        EnergyChargeStatVo energyChargeStatVo = new EnergyChargeStatVo();
        if (chargeConfig == null) {//如果没有费用配置，就无法计算费用
            return energyChargeStatVo;
        } else {
            String areaData = CommonAreaData.getAreaData(instrumentType);
            //阶梯价
            if (chargeConfig.getChargeType() != null && chargeConfig.getChargeType().equals(ChargeTypeEnum.JTJ.name())) {
                String value = getChargeQueryCommandForInfluxdb(begin, end, tableFlag, "value", areaData);
                List<EnergyChargeStatVo> query = queryApi.query(value, EnergyChargeStatVo.class);
                energyChargeStatVo.setAmount(chargeConfig.getAmoutByUsage(
                        ListUtils.isEmpty(query) ? BigDecimal.ZERO : query.get(0).getValue()));
            } else {
                //如果是峰平谷，需要单独计算峰平谷的费用，再求和
                BigDecimal sumAmount = BigDecimal.ZERO;

                String fvalueQuery = this.getChargeQueryCommandForInfluxdb(begin, end, tableFlag, "fvalue", areaData);
                List<EnergyChargeStatVo> fvalueList = queryApi.query(fvalueQuery, EnergyChargeStatVo.class);
                if (CollectionUtils.isNotEmpty(fvalueList)) {
                    sumAmount = sumAmount.add(chargeConfig.getFPGAmount(FPGTypeEnum.FV, fvalueList.get(0).getFvalue()));
                }
                String pvalueQuery = this.getChargeQueryCommandForInfluxdb(begin, end, tableFlag, "pvalue", areaData);
                List<EnergyChargeStatVo> pvalueList = queryApi.query(pvalueQuery, EnergyChargeStatVo.class);
                if (CollectionUtils.isNotEmpty(pvalueList)) {
                    sumAmount = sumAmount.add(chargeConfig.getFPGAmount(FPGTypeEnum.PV, pvalueList.get(0).getPvalue()));
                }
                String gvalueQuery = this.getChargeQueryCommandForInfluxdb(begin, end, tableFlag, "gvalue", areaData);
                List<EnergyChargeStatVo> gvalueList = queryApi.query(gvalueQuery, EnergyChargeStatVo.class);
                if (CollectionUtils.isNotEmpty(gvalueList)) {
                    sumAmount = sumAmount.add(chargeConfig.getFPGAmount(FPGTypeEnum.GV, gvalueList.get(0).getGvalue()));
                }
                energyChargeStatVo.setAmount(sumAmount);
            }
            return energyChargeStatVo;
        }

    }

    private EnergyChargeStatVo getInstrumentTypeAreaOrRouteChargeStat(String instrumentType, String tableFlag, String begin, String end,
                                                                      String ids
                                                                     ) {
        QueryApi queryApi = influxdbClient.getQueryApi();
        //目前只要不是电表 都是按阶梯价计算；电表有可能按峰平谷计算
        EnergyChargeConfigVO chargeConfig = energyChargeConfigService.getByType(instrumentType, null);
        EnergyChargeStatVo energyChargeStatVo = new EnergyChargeStatVo();
        if (chargeConfig == null) {//如果没有费用配置，就无法计算费用
            return energyChargeStatVo;
        } else {
            //阶梯价
            if (chargeConfig.getChargeType() != null && chargeConfig.getChargeType().equals(ChargeTypeEnum.JTJ.name())) {
                String value = getAreaOrRouteChargeQueryCommandForInfluxdb(tableFlag, instrumentType, begin, end, tableFlag, "value", ids);
                List<EnergyChargeStatVo> query = queryApi.query(value, EnergyChargeStatVo.class);
                energyChargeStatVo.setAmount(chargeConfig.getAmoutByUsage(
                        ListUtils.isEmpty(query) ? BigDecimal.ZERO : query.get(0).getValue()));
            } else {
                //如果是峰平谷，需要单独计算峰平谷的费用，再求和
                BigDecimal sumAmount = BigDecimal.ZERO;
                String fvalueQuery = this.getAreaOrRouteChargeQueryCommandForInfluxdb(tableFlag, instrumentType, begin, end, tableFlag, "fvalue", ids);
                List<EnergyChargeStatVo> fvalueList = queryApi.query(fvalueQuery, EnergyChargeStatVo.class);
                if (CollectionUtils.isNotEmpty(fvalueList)) {
                    BigDecimal fpgAmount = chargeConfig.getFPGAmount(FPGTypeEnum.FV, fvalueList.get(0).getFvalue());
                    sumAmount = sumAmount.add(fpgAmount);
                    energyChargeStatVo.setFvAmount(fpgAmount);
                }
                String pvalueQuery = this.getChargeQueryCommandForInfluxdb(begin, end, tableFlag, "pvalue", ids);
                List<EnergyChargeStatVo> pvalueList = queryApi.query(pvalueQuery, EnergyChargeStatVo.class);
                if (CollectionUtils.isNotEmpty(pvalueList)) {
                    BigDecimal fpgAmount = chargeConfig.getFPGAmount(FPGTypeEnum.PV, pvalueList.get(0).getPvalue());
                    sumAmount = sumAmount.add(fpgAmount);
                    energyChargeStatVo.setPvAmount(fpgAmount);
                }
                String gvalueQuery = this.getAreaOrRouteChargeQueryCommandForInfluxdb(tableFlag, instrumentType, begin, end, tableFlag, "gvalue", ids);
                List<EnergyChargeStatVo> gvalueList = queryApi.query(gvalueQuery, EnergyChargeStatVo.class);
                if (CollectionUtils.isNotEmpty(gvalueList)) {
                    BigDecimal fpgAmount = chargeConfig.getFPGAmount(FPGTypeEnum.GV, gvalueList.get(0).getGvalue());
                    sumAmount = sumAmount.add(fpgAmount);
                    energyChargeStatVo.setGvAmount(fpgAmount);
                }
                energyChargeStatVo.setAmount(sumAmount);
            }
            return energyChargeStatVo;
        }

    }

    @Override
    public List<EnergyChargeStatVo> getChargeByType(ChargeQueryParam param) {
        List<EnergyGate> energyGates = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                                      .eq(EnergyGate::getOpen, true));

        TimeDataVo chargeTimeFlagPeriod = getChargeTimeFlagPeriod(param.getTimeType(), param.getPeriod());
        String areaData = CommonAreaData.getAreaData(null);
        //按类型统计用量
        QueryApi queryApi = influxdbClient.getQueryApi();
        String query = this.getQueryCommandForInfluxdb4(chargeTimeFlagPeriod.getBegin(), chargeTimeFlagPeriod.getEnd(), param.getTimeType(), "value", "");
        List<EnergyChargeStatVo> valueList = queryApi.query(query, EnergyChargeStatVo.class);
        List<EnergyChargeStatVo> collect = energyGates.stream().map(t -> {
            EnergyChargeStatVo stat = valueList.stream().filter(v -> v.getInstrumentType().equals(t.getEnergyType())).findFirst()
                    .orElse(null);
            if (stat != null) {
                //目前只要不是电表 都是按阶梯价计算；电表有可能按峰平谷计算
                EnergyChargeConfigVO chargeConfig = energyChargeConfigService.getByType(stat.getInstrumentType(), null);
                if (chargeConfig == null) {
                    stat = null;//如果没有费用配置，就无法计算费用
                }
                if (stat != null) {
                    //阶梯价
                    if (chargeConfig.getChargeType() != null && chargeConfig.getChargeType().equals(ChargeTypeEnum.JTJ.name())) {
                        stat.setAmount(chargeConfig.getAmoutByUsage(stat.getValue()));
                    } else {
                        //如果是峰平谷，需要单独计算峰平谷的费用，再求和
                        BigDecimal sumAmount = BigDecimal.ZERO;
                        String fvalueQuery = this.getQueryCommandForInfluxdb5(param.getPeriod(), param.getTimeType(), stat.getInstrumentType(), "fvalue");
                        List<EnergyChargeStatVo> fvalueList = queryApi.query(fvalueQuery, EnergyChargeStatVo.class);
                        if (CollectionUtils.isNotEmpty(fvalueList)) {
                            sumAmount = sumAmount.add(chargeConfig.getFPGAmount(FPGTypeEnum.FV, fvalueList.get(0).getFvalue()));
                        }
                        String pvalueQuery = this.getQueryCommandForInfluxdb5(param.getPeriod(), param.getTimeType(), stat.getInstrumentType(), "pvalue");
                        List<EnergyChargeStatVo> pvalueList = queryApi.query(pvalueQuery, EnergyChargeStatVo.class);
                        if (CollectionUtils.isNotEmpty(pvalueList)) {
                            sumAmount = sumAmount.add(chargeConfig.getFPGAmount(FPGTypeEnum.PV, pvalueList.get(0).getPvalue()));
                        }
                        String gvalueQuery = this.getQueryCommandForInfluxdb5(param.getPeriod(), param.getTimeType(), stat.getInstrumentType(), "gvalue");
                        List<EnergyChargeStatVo> gvalueList = queryApi.query(gvalueQuery, EnergyChargeStatVo.class);
                        if (CollectionUtils.isNotEmpty(gvalueList)) {
                            sumAmount = sumAmount.add(chargeConfig.getFPGAmount(FPGTypeEnum.GV, gvalueList.get(0).getGvalue()));
                        }
                        stat.setAmount(sumAmount);
                    }
                }
            }
            if (stat == null) {
                stat = EnergyChargeStatVo.builder()
                        .instrumentType(t.getEnergyType())
                        .amount(BigDecimal.ZERO)
                        .build();
            }
            return stat;
        }).collect(Collectors.toList());
        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                               .eq(EnergyGate::getOpen, true));
        List<EnergyChargeStatVo> objects = new ArrayList<>();
        if (!list.isEmpty()) {
            for (EnergyGate energyGate : list) {
                for (EnergyChargeStatVo energyChargeStatVo : collect) {
                    if (energyGate.getEnergyType().equals(energyChargeStatVo.getInstrumentType())) {
                        objects.add(energyChargeStatVo);
                    }
                }
            }
        }
        return objects;
    }

    private String getQueryAreaDetailCommandForInfluxdb2(String period, String timeType, List<Long> areaIds, String sumField) {

        //初始化查询参数
        String startTimeStr = "";
        String endTimeStr = "";
        String statTableType = "";
        if (StringUtils.isNotEmpty(period)) {
            PeriodVo period1 = getPeriod(period);
            startTimeStr = period1.getTime().split(",")[0];
            endTimeStr = period1.getTime().split(",")[1];
            statTableType = "Day";
        } else {
            String[] params = this.initQueryParam(timeType);
            startTimeStr = params[0];
            endTimeStr = params[1];
            statTableType = params[2];
        }

        Requester requester = SessionContextHolder.getRequester(true);
        String tenantStr = requester != null ? requester.getTenant().toString() : "100";//默认值

        String fluxQuery = "from(bucket: \"energy\")\n" +
                           "  |> range(start: " + startTimeStr + ", stop: " + endTimeStr + ")\n" +
                           "  |> filter(fn: (r) => r[\"_measurement\"] == \"area" + statTableType + "\")\n" +
                           "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                           "  |> filter(fn: (r) => r.tenant==\"" + tenantStr + "\" " + joinAreaQueryCondition(areaIds) + ")\n" +
                           "  |> group(columns: [\"areaId\"])\n" +
                           "  |> sum(column: \"" + sumField + "\")";

        return fluxQuery;
    }

    private String getQueryCommandForInfluxdb3(String period, String timeType, String instrumentType, String sumField) {
        //初始化查询参数
        String startTimeStr = "";
        String endTimeStr = "";
        String statTableType = "";
        if (StringUtils.isNotEmpty(period)) {
            PeriodVo period1 = getPeriod(period);
            startTimeStr = period1.getTime().split(",")[0];
            endTimeStr = period1.getTime().split(",")[1];
            statTableType = "Day";
        } else {
            String[] params = this.initQueryParam(timeType);
            startTimeStr = params[0];
            endTimeStr = params[1];
            statTableType = params[2];
        }

        Requester requester = SessionContextHolder.getRequester(true);
        String tenantStr = requester != null ? requester.getTenant().toString() : "100";//默认值

        String fluxQuery = "from(bucket: \"energy\")\n" +
                           "  |> range(start: " + startTimeStr + ", stop: " + endTimeStr + ")\n" +
                           "  |> filter(fn: (r) => r[\"_measurement\"] == \"device" + statTableType + "\")\n" +
                           "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                           "  |> filter(fn: (r) => r.tenant == \"" + tenantStr + "\" and r.instrumentType == \"" + instrumentType +
                           "\")\n" +
                           "  |> group(columns: [\"_time\"])\n" +
                           "  |> sum(column: \"" + sumField + "\")";
        return fluxQuery;
    }

    private String getQueryCommandForInfluxdb4(String begin, String end, String timeType, String sumField, String areaIds) {

        String tenantStr = SessionContextHolder.getRequester().getTenant().toString();
        String fluxQuery = "from(bucket: \"energy\")\n" +
                           "  |> range(start: " + begin + ", stop: " + end + ")\n" +
                           "  |> filter(fn: (r) => r[\"_measurement\"] == \"area" + timeType + "\")\n" +
                           "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                           "  |> filter(fn: (r) => r.tenant == \"" + tenantStr + "\" " + areaIds + ")\n" +
                           "  |> group(columns: [\"instrumentType\"])\n" +
                           "  |> sum(column: \"" + sumField + "\")";
        return fluxQuery;
    }

    private String getChargeQueryCommandForInfluxdb(String begin, String end, String timeType, String sumField, String areaIds) {

        String tenantStr = SessionContextHolder.getRequester().getTenant().toString();
        String fluxQuery = "from(bucket: \"energy\")\n" +
                           "  |> range(start: " + begin + ", stop: " + end + ")\n" +
                           "  |> filter(fn: (r) => r[\"_measurement\"] == \"area" + timeType + "\")\n" +
                           "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                           "  |> filter(fn: (r) => r.tenant == \"" + tenantStr + "\" " + areaIds + ")\n" +
                           "  |> group(columns: [\"instrumentType\"])\n" +
                           "  |> sum(column: \"" + sumField + "\")";
        return fluxQuery;
    }

    private String getAreaOrRouteChargeQueryCommandForInfluxdb(String filter, String instrumentType, String begin, String end,
                                                               String timeType, String sumField, String Ids
                                                              ) {

        String tenantStr = SessionContextHolder.getRequester().getTenant().toString();
        String fluxQuery = "from(bucket: \"energy\")\n" +
                           "  |> range(start: " + begin + ", stop: " + end + ")\n" +
                           "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + filter + timeType + "\")\n" +
                           "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                           "  |> filter(fn: (r) => r.tenant == \"" + tenantStr + "\" " + "and r.instrumentType==\"" + instrumentType +
                           "\" " + Ids + ")\n" +
                           "  |> group(columns: [\"instrumentType\"])\n" +
                           "  |> sum(column: \"" + sumField + "\")";
        return fluxQuery;
    }

    private String getChargeQueryCommandForInfluxdbTrend(String begin, String end, String timeType, String sumField, String areaIds,
                                                         String instrumentType
                                                        ) {

        String tenantStr = SessionContextHolder.getRequester().getTenant().toString();
        String fluxQuery = "from(bucket: \"energy\")\n" +
                           "  |> range(start: " + begin + ", stop: " + end + ")\n" +
                           "  |> filter(fn: (r) => r[\"_measurement\"] == \"area" + timeType + "\")\n" +
                           "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                           "  |> filter(fn: (r) => r.tenant == \"" + tenantStr + "\" and r.instrumentType==\"" + instrumentType + "\" " +
                           areaIds + ")\n" +
                           "  |> group(columns: [\"_time\"])\n" +
                           "  |> sum(column: \"" + sumField + "\")";
        return fluxQuery;
    }

    private String getChargeQueryCommandForInfluxdbAreaOrRouteTrend(String table, String begin, String end, String timeType,
                                                                    String sumField, String areaIds, String instrumentType
                                                                   ) {

        String tenantStr = SessionContextHolder.getRequester().getTenant().toString();
        String fluxQuery = "from(bucket: \"energy\")\n" +
                           "  |> range(start: " + begin + ", stop: " + end + ")\n" +
                           "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + table + timeType + "\")\n" +
                           "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                           "  |> filter(fn: (r) => r.tenant == \"" + tenantStr + "\" and r.instrumentType==\"" + instrumentType + "\" " +
                           areaIds + ")\n" +
                           "  |> group(columns: [\"_time\"])\n" +
                           "  |> sum(column: \"" + sumField + "\")";
        return fluxQuery;
    }

    private String getChargeQueryCommandForInfluxdbAreaOrRouteDetail(String table, String begin, String end, String timeType,
                                                                     String sumField, String ids, String instrumentType
                                                                    ) {
        String tenantStr = SessionContextHolder.getRequester().getTenant().toString();
        String fluxQuery = "from(bucket: \"energy\")\n" +
                           "  |> range(start: " + begin + ", stop: " + end + ")\n" +
                           "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + table + timeType + "\")\n" +
                           "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                           "  |> filter(fn: (r) => r.tenant == \"" + tenantStr + "\" and r.instrumentType==\"" + instrumentType + "\" " +
                           ids + ")\n" +
                           "  |> group(columns: [\"" + table + "Id\"])\n" +
                           "  |> sum(column: \"" + sumField + "\")";
        return fluxQuery;
    }

    private String getQueryCommandForInfluxdb5(String period, String timeType, String instrumentType, String sumField) {
        //初始化查询参数
        String startTimeStr = "";
        String endTimeStr = "";
        String statTableType = "";
        if (StringUtils.isNotEmpty(period)) {
            PeriodVo period1 = getPeriod(period);
            startTimeStr = period1.getTime().split(",")[0];
            endTimeStr = period1.getTime().split(",")[1];
            statTableType = "Day";
        } else {
            String[] params = this.initQueryParam(timeType);
            startTimeStr = params[0];
            endTimeStr = params[1];
            statTableType = params[2];
        }

        Requester requester = SessionContextHolder.getRequester(true);
        String tenantStr = requester != null ? requester.getTenant().toString() : "100";//默认值

        String fluxQuery = "from(bucket: \"energy\")\n" +
                           "  |> range(start: " + startTimeStr + ", stop: " + endTimeStr + ")\n" +
                           "  |> filter(fn: (r) => r[\"_measurement\"] == \"device" + statTableType + "\")\n" +
                           "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                           "  |> filter(fn: (r) => r.tenant == \"" + tenantStr + "\" and r.instrumentType == \"" + instrumentType +
                           "\")\n" +
                           "  |> group(columns: [\"instrumentType\"])\n" +
                           "  |> sum(column: \"" + sumField + "\")";
        return fluxQuery;
    }

    private String[] initQueryParam(String timeType) {
        String startTimeStr = "";
        String endTimeStr = "";
        String statTableType = "";
        if ("TODAY".equals(timeType)) {//按小时分组
            startTimeStr = DateUtils.formatDate(DateUtils.offsetMinute(DateUtils.beginOfDay(LocalDateTime.now()),
                                                                       -8 * 60), DateUtils.DEFAULT_UTC_FORMATTER);
            endTimeStr = DateUtils.formatDate(DateUtils.endOfDay(LocalDateTime.now()), DateUtils.DEFAULT_UTC_FORMATTER);
            statTableType = DateEnum.Hour.name();
        } else if ("THIS_MONTH".equals(timeType)) {
            startTimeStr = DateUtils.formatDate(DateUtils.offsetMinute(DateUtils.beginOfMonth(LocalDateTime.now()),
                                                                       -8 * 60), DateUtils.DEFAULT_UTC_FORMATTER);
            DateUtils.formatDate(DateUtils.endOfMonth(LocalDateTime.now()), DateUtils.DEFAULT_UTC_FORMATTER);
            statTableType = DateEnum.Day.name();
        } else if ("THIS_YEAR".equals(timeType)) {
            startTimeStr = DateUtils.formatDate(DateUtils.offsetMinute(DateUtils.beginOfYear(LocalDateTime.now()),
                                                                       -8 * 60), DateUtils.DEFAULT_UTC_FORMATTER);
            DateUtils.formatDate(DateUtils.endOfYear(LocalDateTime.now()), DateUtils.DEFAULT_UTC_FORMATTER);
            statTableType = DateEnum.Month.name();
        } else if ("QOQ_TODAY".equals(timeType)) {
            LocalDateTime yesterday = DateUtils.offsetDay(LocalDateTime.now(), -1);
            startTimeStr = DateUtils.formatDate(DateUtils.beginOfDay(yesterday), DateUtils.DEFAULT_UTC_FORMATTER);

            statTableType = DateEnum.Hour.name();
        } else if ("QOQ_MONTH".equals(timeType)) {
            LocalDateTime lastMonthDay = DateUtils.offsetMonth(LocalDateTime.now(), -1);
            startTimeStr = DateUtils.formatDate(DateUtils.beginOfMonth(lastMonthDay), DateUtils.DEFAULT_UTC_FORMATTER);

            endTimeStr = DateUtils.formatDate(DateUtils.endOfDay(lastMonthDay), DateUtils.DEFAULT_UTC_FORMATTER);
            statTableType = DateEnum.Day.name();
        } else if ("QOQ_YEAR".equals(timeType)) {
            LocalDateTime lastYearDay = DateUtils.offsetMonth(LocalDateTime.now(), -1);
            startTimeStr = DateUtils.formatDate(DateUtils.beginOfYear(lastYearDay), DateUtils.DEFAULT_UTC_FORMATTER);

            endTimeStr = DateUtils.formatDate(DateUtils.endOfDay(lastYearDay), DateUtils.DEFAULT_UTC_FORMATTER);
            statTableType = DateEnum.Month.name();
        } else if ("YOY_TODAY".equals(timeType)) {
            LocalDateTime yoyDay = DateUtils.offsetMonth(LocalDateTime.now(), -12);
            startTimeStr = DateUtils.formatDate(DateUtils.beginOfDay(yoyDay), DateUtils.DEFAULT_UTC_FORMATTER);

            endTimeStr = DateUtils.formatDate(DateUtils.endOfDay(yoyDay), DateUtils.DEFAULT_UTC_FORMATTER);

            statTableType = DateEnum.Hour.name();
        } else if ("YOY_MONTH".equals(timeType)) {
            LocalDateTime yoyMonthDay = DateUtils.offsetMonth(LocalDateTime.now(), -12);
            startTimeStr = DateUtils.formatDate(DateUtils.beginOfMonth(yoyMonthDay), DateUtils.DEFAULT_UTC_FORMATTER);

            endTimeStr = DateUtils.formatDate(DateUtils.endOfDay(yoyMonthDay), DateUtils.DEFAULT_UTC_FORMATTER);

            statTableType = DateEnum.Day.name();
        } else if ("YOY_YEAR".equals(timeType)) {
            LocalDateTime lastYearDay = DateUtils.offsetMonth(LocalDateTime.now(), -12);
            startTimeStr = DateUtils.formatDate(DateUtils.beginOfYear(lastYearDay), DateUtils.DEFAULT_UTC_FORMATTER);

            endTimeStr = DateUtils.formatDate(DateUtils.endOfDay(lastYearDay), DateUtils.DEFAULT_UTC_FORMATTER);

            statTableType = DateEnum.Month.name();
        }
        return new String[]{ startTimeStr, endTimeStr, statTableType };
    }

    private String joinAreaQueryCondition(List<Long> areaIds) {
        String condition = "";
        if (CollectionUtils.isNotEmpty(areaIds)) {
            String collect = areaIds.stream().map(d -> "r.deviceId==\"" + d.toString() + "\"").collect(Collectors.joining(" or "));
            condition = "and (" + collect + ")";
        }
        return condition;
    }

    private String joinAreaTrendQueryCondition(List<Long> areaIds) {
        String condition = "";
        if (CollectionUtils.isNotEmpty(areaIds)) {
            String collect = areaIds.stream().map(d -> "r.areaId==\"" + d.toString() + "\"").collect(Collectors.joining(" or "));
            condition = "and (" + collect + ")";
        }
        return condition;
    }

    private PeriodVo getPeriod(String period) {
        String[] split = period.split(",");
        LocalDateTime parse = DateUtils.offsetMinute(DateUtils.parse(split[0]), -8 * 60);
        LocalDateTime parse1 = DateUtils.parse(split[1]);
        String time = DateUtils.formatDate(parse, DEFAULT_DATE_FORMAT) + "T" + DateUtils.formatDate(parse, "HH:mm:ss") + "Z," +
                      DateUtils.formatDate(parse1, DEFAULT_DATE_FORMAT) + "T23:59:59Z";
        int l = (int) DateUtils.between(parse, parse1, ChronoUnit.DAYS);
        PeriodVo periodVo = new PeriodVo();
        periodVo.setDay(l);
        periodVo.setTime(time);
        return periodVo;
    }

    private TimeDataVo getChargeTimeFlagPeriod(String timeFlag, String period) {
        String[] split = period.split(",");
        LocalDateTime st = DateUtils.parse(split[0]);
        LocalDateTime et = DateUtils.offsetMinute(DateUtils.parse(split[1]), -1);
        LocalDateTime ost = null;
        LocalDateTime oet = null;
        LocalDateTime qoqst = null;
        LocalDateTime qoqet = null;

        Integer flag = 0;
        String tableFlag = "Day";
        if (timeFlag.equals("TODAY")) {
            flag = (int) DateUtils.between(st, et, ChronoUnit.DAYS);
            ost = DateUtils.offsetMonth(st, -1);
            oet = DateUtils.offsetMonth(et, -1);
            qoqst = DateUtils.offsetDay(st, -flag);
            qoqet = DateUtils.offsetDay(et, -flag);
        } else if (timeFlag.equals("THIS_MONTH")) {
            tableFlag = "Month";
            Instant instant = st.atZone(ZoneId.systemDefault()).toInstant();
            Instant instantEnd = et.atZone(ZoneId.systemDefault()).toInstant();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            LocalDateTime localEndDateTime = LocalDateTime.ofInstant(instantEnd, ZoneId.systemDefault());
            LocalDate localDate = localDateTime.toLocalDate();
            LocalDate localEndDate = localEndDateTime.toLocalDate();
            flag = (int) Period.between(localDate, localEndDate).toTotalMonths();
            ost = DateUtils.offset(st, ChronoUnit.YEARS, -1);
            oet = DateUtils.offset(et, ChronoUnit.YEARS, -1);
            qoqst = DateUtils.offset(st, ChronoUnit.MONTHS, -flag);
            qoqet = DateUtils.offset(et, ChronoUnit.MONTHS, -flag);
        } else if (timeFlag.equals("THIS_YEAR")) {
            tableFlag = "Year";
            Instant instant = st.atZone(ZoneId.systemDefault()).toInstant();
            Instant instantEnd = et.atZone(ZoneId.systemDefault()).toInstant();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            LocalDateTime localEndDateTime = LocalDateTime.ofInstant(instantEnd, ZoneId.systemDefault());
            LocalDate localDate = localDateTime.toLocalDate();
            LocalDate localEndDate = localEndDateTime.toLocalDate();
            flag = Period.between(localDate, localEndDate).getYears();
            ost = DateUtils.offset(st, ChronoUnit.YEARS, -1);
            oet = DateUtils.offset(et, ChronoUnit.YEARS, -1);
            qoqst = DateUtils.offset(st, ChronoUnit.YEARS, -flag);
            qoqet = DateUtils.offset(et, ChronoUnit.YEARS, -flag);

        }

        LocalDateTime beginTime = DateUtils.offset(st, ChronoUnit.DAYS, -8);
        String time =
                DateUtils.formatDate(beginTime, DEFAULT_DATE_FORMAT) + "T" + DateUtils.formatDate(beginTime, "HH:00:00") + "Z";
        String endTime = DateUtils.formatDate(et, DEFAULT_DATE_FORMAT) + "T23:59:59Z";
        ;
        LocalDateTime oldBeginTime = DateUtils.offset(ost, ChronoUnit.DAYS, -8);
        String oldStartTime =
                DateUtils.formatDate(oldBeginTime, DEFAULT_DATE_FORMAT) + "T" + DateUtils.formatDate(oldBeginTime, "HH:00:00") +
                "Z";
        String oldEndTime = DateUtils.formatDate(oet, DEFAULT_DATE_FORMAT) + "T23:59:59Z";
        LocalDateTime beginQoqTime = DateUtils.offset(qoqst, ChronoUnit.DAYS, -8);
        String qoqStTime =
                DateUtils.formatDate(beginQoqTime, DEFAULT_DATE_FORMAT) + "T" + DateUtils.formatDate(beginQoqTime, "HH:00:00") +
                "Z";
        String oldQoqEndTime = DateUtils.formatDate(qoqet, DEFAULT_DATE_FORMAT) + "T23:59:59Z";
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
        timeDataVo.setQoqBegin(qoqStTime);
        timeDataVo.setQoqEnd(oldQoqEndTime);
        return timeDataVo;
    }

    private void getTreeIds(ChargeComprehensiveParam param) {
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
}
