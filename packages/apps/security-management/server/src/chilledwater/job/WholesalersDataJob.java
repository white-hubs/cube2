package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.job;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropHistoryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.ColdWaterElectricityMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.WaterDataDayMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.WaterMapTypeMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ColdWaterElectricity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataDay;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataMonth;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataYear;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterMapType;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WholesalersDataJobParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWholesalerService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataDayService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataMonthService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataYearService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataDeviceVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WaterDataDetailVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.SecurityCommonService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pair;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Slf4j
@Component
public class WholesalersDataJob {

    @Autowired
    private ColdWholesalerService wholesalerService;
    @Autowired
    private WaterDataYearService waterDataYearService;
    @Autowired
    private WaterDataMonthService waterDataMonthService;
    @Autowired
    private WaterDataDayService waterDataDayService;
    @Autowired
    private SecurityCommonService securityCommonService;
    @Autowired
    private ColdWaterElectricityMapper coldWaterElectricityMapper;
    @Autowired
    private IotDevicePropClient iotDevicePropClient;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private WaterMapTypeMapper waterMapTypeMapper;
    @Autowired
    private WaterDataDayMapper waterDataDayMapper;

    /**
     * @Description 冷冻水采集
     * @Author lrj
     */
    @JobAnnotation(name = "wholesalersData", cron = "0 30 0 * * ?", desc = "冷冻水数据采集",
                   param = "{\"tenant\": 58197260}"
    )
    public void wholesalersData(String param) {
        if (StringUtils.isBlank(param)) {
            throw new ServiceException("参数为空");
        }
        var map = JSONObject.parseObject(param, new TypeReference<Map<String, Object>>() {});
        var jobParam = WholesalersDataJobParam.builder()
                .tenant(Objects.isNull(map.get("tenant")) ? null : Integer.parseInt(map.get("tenant").toString()))
                .startTime(Objects.isNull(map.get("startTime")) ? null : DateUtils.parse(map.get("startTime").toString()))
                .endTime(Objects.isNull(map.get("endTime")) ? null : DateUtils.parse(map.get("endTime").toString()))
                .build();
        SessionContextHolder.requestTenant(jobParam.getTenant());
        var wholesalerDevices = wholesalerService.getWholesalerDevices();
        if (CollectionUtils.isNotEmpty(wholesalerDevices)) {
            addWholesalersData(wholesalerDevices, jobParam);
        }
    }

    /**
     * @Description 冷冻水采集重构
     * @Author lrj
     */
    @JobAnnotation(name = "wholesalersDataNew", cron = "0 30 0 * * ?", desc = "冷冻水数据采集",
                   param = "{\"tenant\": 58197260}"
    )
    public void wholesalersDataNew(String param) {
        if (StringUtils.isBlank(param)) {
            throw new ServiceException("参数为空");
        }
        var map = JSONObject.parseObject(param, new TypeReference<Map<String, Object>>() {});

        var jobParam = WholesalersDataJobParam.builder()
                .tenant(Objects.isNull(map.get("tenant")) ? null : Integer.parseInt(map.get("tenant").toString()))
                .startTime(Objects.isNull(map.get("startTime")) ? null : DateUtils.parse(map.get("startTime").toString()))
                .endTime(Objects.isNull(map.get("endTime")) ? null : DateUtils.parse(map.get("endTime").toString()))
                .build();
        SessionContextHolder.requestTenant(jobParam.getTenant());
        List<DataDeviceVo> wholesalerDevices = wholesalerService.getWholesalerDevices();
        if (CollectionUtils.isNotEmpty(wholesalerDevices)) {
            addWholesalersDataNew(wholesalerDevices, jobParam);
        }
    }

    private void addWholesalersDataNew(List<DataDeviceVo> wholesalerDevices, WholesalersDataJobParam jobParam) {
        var tenant = jobParam.getTenant();
        var chilledWaterTime = (Long) redisTemplate.opsForValue().get("chilledWaterTime");
        var date = LocalDateTime.now();
        if (Objects.nonNull(jobParam.getEndTime())) {
            date = jobParam.getEndTime();
        }
        if (Objects.nonNull(chilledWaterTime)) {
            var stParse = DateUtils.parse(chilledWaterTime);
            //传入开始时间则按传入的时间进行采集
            if (Objects.nonNull(jobParam.getStartTime())) {
                stParse = jobParam.getStartTime();
            }
            LocalDateTime beginOfDay = DateUtils.beginOfDay(stParse);
            long between = DateUtils.between(beginOfDay, date, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(beginOfDay, ChronoUnit.DAYS, i);
                var beginTime = DateUtils.beginOfDay(offset);
                var endTime = DateUtils.endOfDay(offset);
                handleDataNew(beginTime, endTime, wholesalerDevices, tenant);
            }
            this.saveOrUpdateMonthAndYearData(wholesalerDevices, beginOfDay, date);
        } else {
            var offset = DateUtils.offset(date, ChronoUnit.DAYS, -10);
            //传入开始时间则按传入的时间进行采集
            if (Objects.nonNull(jobParam.getStartTime())) {
                offset = jobParam.getStartTime();
            }
            var beginTime = DateUtils.beginOfDay(offset);
            var between = DateUtils.between(beginTime, date, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offsetDay = DateUtils.offset(beginTime, ChronoUnit.DAYS, i);
                var beginDay = DateUtils.beginOfDay(offsetDay);
                var endDay = DateUtils.endOfDay(offsetDay);
                handleDataNew(beginDay, endDay, wholesalerDevices, tenant);
            }
            this.saveOrUpdateMonthAndYearData(wholesalerDevices, beginTime, date);
        }
        redisTemplate.opsForValue().set("chilledWaterTime", DateUtils.toTimestamp(date));
    }

    private void addWholesalersData(List<DataDeviceVo> wholesalerDevices, WholesalersDataJobParam jobParam) {
        Integer tenant = jobParam.getTenant();
        var chilledWaterTime = (Long) redisTemplate.opsForValue().get("chilledWaterTime");
        var date = LocalDateTime.now();
        if (Objects.nonNull(jobParam.getEndTime())) {
            date = jobParam.getEndTime();
        }
        if (Objects.nonNull(chilledWaterTime)) {
            var stParse = DateUtils.parse(chilledWaterTime);
            //传入开始时间则按传入的时间进行采集
            if (Objects.nonNull(jobParam.getStartTime())) {
                stParse = jobParam.getStartTime();
            }
            var beginOfDay = DateUtils.beginOfDay(stParse);
            var between = DateUtils.between(beginOfDay, date, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(beginOfDay, ChronoUnit.DAYS, i);
                var beginTime = DateUtils.beginOfDay(offset);
                var endTime = DateUtils.endOfDay(offset);
                handleData(beginTime, endTime, wholesalerDevices, tenant);
            }
        } else {
            var offset = DateUtils.offset(date, ChronoUnit.DAYS, -10);
            //传入开始时间则按传入的时间进行采集
            if (Objects.nonNull(jobParam.getStartTime())) {
                offset = jobParam.getStartTime();
            }
            var beginTime = DateUtils.beginOfDay(offset);
            var between = DateUtils.between(beginTime, date, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offsetDay = DateUtils.offset(beginTime, ChronoUnit.DAYS, i);
                var beginDay = DateUtils.beginOfDay(offsetDay);
                var endDay = DateUtils.endOfDay(offsetDay);
                handleData(beginDay, endDay, wholesalerDevices, tenant);
            }
        }
        redisTemplate.opsForValue().set("chilledWaterTime", DateUtils.toTimestamp(date));
    }

    /**
     * 新增和更新月份和年数据
     *
     * @param wholesalerDevices
     * @param beginOfDay
     * @param date
     */
    private void saveOrUpdateMonthAndYearData(List<DataDeviceVo> wholesalerDevices, LocalDateTime beginOfDay, LocalDateTime date) {
        List<String> ids = wholesalerDevices.stream().filter(t -> Objects.nonNull(t.getDeviceCode())).map(DataDeviceVo::getDeviceCode)
                .collect(Collectors.toList());
        //获取时间段内的所有月份
        int betweenMonth = (int) DateUtils.betweenMonth(beginOfDay, date, true);
        Set<String> dateTimes = new HashSet<>();
        dateTimes.add(DateUtils.format(date, DateUtils.DEFAULT_MONTH_FORMATTER));
        for (int i = 0; i < betweenMonth; i++) {
            var dateTime = DateUtils.offsetMonth(beginOfDay, i);
            dateTimes.add(DateUtils.format(dateTime, DateUtils.DEFAULT_MONTH_FORMATTER));
        }
        //获取指定月份的每天的数据之和
        List<WaterDataDetailVo> waterDataDetailVoList = waterDataDayMapper.selectTotalDataByDateTime(new ArrayList<>(dateTimes));
        Map<String, List<WaterDataDetailVo>> waterDayMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(waterDataDetailVoList)) {
            waterDayMap = waterDataDetailVoList.stream().collect(Collectors.groupingBy(WaterDataDetailVo::getDeviceCode));
        }
        //存在的月份数据
        List<WaterDataMonth> months = waterDataMonthService.list(Wrappers.<WaterDataMonth>lambdaQuery()
                                                                         .in(WaterDataMonth::getDeviceCode, ids)
                                                                         .in(WaterDataMonth::getDataTime, dateTimes));
        Map<Pair<String, String>, WaterDataMonth> pairWaterDataMonthMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(months)) {
            pairWaterDataMonthMap = months.stream().collect(Collectors.toMap(t -> Pair.of(t.getDataTime(), t.getDeviceCode()), t -> t));
        }
        //获取开始和结束时间所有的年份
        long betweenYear = DateUtils.between(beginOfDay, date, ChronoUnit.YEARS);
        Set<String> formatYears = new HashSet<>();
        formatYears.add(DateUtils.format(date, DateUtils.DEFAULT_YEAR_FORMATTER));
        for (int i = 0; i < betweenYear; i++) {
            var dateTime = DateUtils.offset(beginOfDay, ChronoUnit.YEARS, i);
            formatYears.add(DateUtils.format(dateTime, DateUtils.DEFAULT_YEAR_FORMATTER));
        }
        //存在的年份数据
        List<WaterDataYear> years = waterDataYearService.list(Wrappers.<WaterDataYear>lambdaQuery()
                                                                      .in(WaterDataYear::getDeviceCode, ids)
                                                                      .in(WaterDataYear::getDataTime, formatYears));
        Map<Pair<String, String>, WaterDataYear> pairWaterDataYearMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(years)) {
            pairWaterDataYearMap = years.stream().collect(Collectors.toMap(t -> Pair.of(t.getDataTime(), t.getDeviceCode()), t -> t));
        }
        for (DataDeviceVo dataDeviceVo : wholesalerDevices) {

            List<WaterDataDetailVo> dataDetailVos = waterDayMap.get(dataDeviceVo.getDeviceCode());
            if (CollectionUtils.isEmpty(dataDetailVos)) {
                continue;
            }
            //不同月份数据集合
            for (WaterDataDetailVo dataDetailVo : dataDetailVos) {
                WaterDataMonth waterDataMonth = pairWaterDataMonthMap.get(Pair.of(dataDetailVo.getDataTime(), dataDeviceVo.getDeviceCode()));
                String[] split1 = dataDetailVo.getDataTime().split("-");
                int monthFlag = Integer.parseInt(split1[1]);
                String endDay = DateUtils.format(date, DateUtils.DEFAULT_MONTH_FORMATTER);
                //判断时间是否相等，相等说明在同一个月，不相等在上一个月，
                boolean sameMonth = StringUtils.equals(endDay, dataDetailVo.getDataTime());
                var lengthOfMonth = DateUtils.lengthOfMonth(monthFlag, DateUtils.isLeapYear(LocalDateTime.now().getYear()));
                if (Objects.nonNull(waterDataMonth)) {
                    int month1 = sameMonth ? date.getDayOfMonth() : lengthOfMonth;
                    String statisticalCycle = waterDataMonth.getStatisticalCycle();
                    String[] split = statisticalCycle.split("-");
                    String monthCycle = split[0] + "-" + month1;
                    waterDataMonth.setStatisticalCycle(monthCycle);
                    waterDataMonth.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                    waterDataMonth.setFlow(dataDetailVo.getFlow());
                    waterDataMonth.setTotal(dataDetailVo.getTotal());
                    waterDataMonth.setUnitPrice(dataDeviceVo.getUnitPrice());
                    waterDataMonth.setDuration(dataDetailVo.getDuration());
                } else {
                    WaterDataMonth waterDataMonth1 = new WaterDataMonth();
                    waterDataMonth1.setDataTime(dataDetailVo.getDataTime());
                    waterDataMonth1.setFlow(dataDetailVo.getFlow());
                    waterDataMonth1.setUnitPrice(dataDeviceVo.getUnitPrice());
                    waterDataMonth1.setCode(dataDeviceVo.getCode());
                    waterDataMonth1.setDuration(dataDetailVo.getDuration());
                    waterDataMonth1.setDepartmentId(dataDeviceVo.getDepartmentId());
                    waterDataMonth1.setGearPositionAreaId(dataDeviceVo.getGearPositionAreaId());
                    waterDataMonth1.setTotal(dataDetailVo.getTotal());
                    waterDataMonth1.setDepartment(dataDeviceVo.getDepartment());
                    waterDataMonth1.setGearPositionArea(dataDeviceVo.getGearPositionArea());
                    waterDataMonth1.setDeviceCode(dataDeviceVo.getDeviceCode());
                    waterDataMonth1.setShiftMaster(dataDeviceVo.getShiftMaster());
                    waterDataMonth1.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                    waterDataMonth1.setShiftMasterPhone(dataDeviceVo.getShiftMasterPhone());

                    int month1 = 1;
                    int month2 = sameMonth ? date.getDayOfMonth() : lengthOfMonth;
                    String format1 = DateUtils.format(dataDeviceVo.getBillableTime(), "yyyy-MM");
                    String format2 = dataDetailVo.getDataTime();
                    if (format1.equals(format2)) {
                        month1 = dataDeviceVo.getBillableTime().getDayOfMonth();
                    }
                    waterDataMonth1.setStatisticalCycle(month1 + "-" + month2);
                    waterDataMonth1.setStatisticalFlag(monthFlag + "月");
                    waterDataMonth1.setGearPositionCode(dataDeviceVo.getGearPositionCode());
                    pairWaterDataMonthMap.put(Pair.of(dataDetailVo.getDataTime(), dataDeviceVo.getDeviceCode()), waterDataMonth1);
                }
                //年统计
                WaterDataYear waterDataYear = pairWaterDataYearMap.get(Pair.of(split1[0], dataDetailVo.getDeviceCode()));
                if (Objects.nonNull(waterDataYear)) {
                    waterDataYear.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                    waterDataYear.setFlow(waterDataYear.getFlow().add(dataDetailVo.getFlow()));
                    waterDataYear.setDuration(waterDataYear.getDuration().add(dataDetailVo.getDuration()));
                    setMonthData(dataDetailVo, monthFlag, waterDataYear);
                } else {
                    WaterDataYear waterDataYear1 = new WaterDataYear();
                    waterDataYear1.setCode(dataDeviceVo.getCode());
                    waterDataYear1.setDataTime(split1[0]);
                    waterDataYear1.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                    waterDataYear1.setGearPositionCode(dataDeviceVo.getGearPositionCode());
                    waterDataYear1.setDepartmentId(dataDeviceVo.getDepartmentId());
                    waterDataYear1.setGearPositionAreaId(dataDeviceVo.getGearPositionAreaId());
                    waterDataYear1.setDeviceCode(dataDeviceVo.getDeviceCode());
                    waterDataYear1.setDepartment(dataDeviceVo.getDepartment());
                    waterDataYear1.setGearPositionArea(dataDeviceVo.getGearPositionArea());
                    waterDataYear1.setShiftMaster(dataDeviceVo.getShiftMaster());
                    waterDataYear1.setFlow(dataDetailVo.getFlow());
                    waterDataYear1.setDuration(dataDetailVo.getDuration());
                    setMonthData(dataDetailVo, monthFlag, waterDataYear1);
                    pairWaterDataYearMap.put(Pair.of(split1[0], dataDetailVo.getDeviceCode()), waterDataYear1);
                }
            }
        }
        //月新增更新
        waterDataMonthService.saveOrUpdateBatch(new ArrayList<>(pairWaterDataMonthMap.values()));
        //年新增更新
        List<WaterDataYear> waterDataYears = new ArrayList<>(pairWaterDataYearMap.values());
        waterDataYears.forEach(waterDataYear -> {
            BigDecimal yearTotal = BigDecimal.ZERO;
            BigDecimal bigDecimal = Objects.isNull(waterDataYear.getOne()) ? BigDecimal.ZERO : waterDataYear.getOne();
            BigDecimal bigDecimal1 = Objects.isNull(waterDataYear.getTwo()) ? BigDecimal.ZERO : waterDataYear.getTwo();
            BigDecimal bigDecimal2 = Objects.isNull(waterDataYear.getThree()) ? BigDecimal.ZERO : waterDataYear.getThree();
            BigDecimal bigDecimal3 = Objects.isNull(waterDataYear.getFour()) ? BigDecimal.ZERO : waterDataYear.getFour();
            BigDecimal bigDecimal4 = Objects.isNull(waterDataYear.getFive()) ? BigDecimal.ZERO : waterDataYear.getFive();
            BigDecimal bigDecimal5 = Objects.isNull(waterDataYear.getSix()) ? BigDecimal.ZERO : waterDataYear.getSix();
            BigDecimal bigDecimal6 = Objects.isNull(waterDataYear.getSeven()) ? BigDecimal.ZERO : waterDataYear.getSeven();
            BigDecimal bigDecimal7 = Objects.isNull(waterDataYear.getEight()) ? BigDecimal.ZERO : waterDataYear.getEight();
            BigDecimal bigDecimal8 = Objects.isNull(waterDataYear.getNine()) ? BigDecimal.ZERO : waterDataYear.getNine();
            BigDecimal bigDecimal9 = Objects.isNull(waterDataYear.getTen()) ? BigDecimal.ZERO : waterDataYear.getTen();
            BigDecimal bigDecimal10 =
                    Objects.isNull(waterDataYear.getEleven()) ? BigDecimal.ZERO : waterDataYear.getEleven();
            BigDecimal bigDecimal11 =
                    Objects.isNull(waterDataYear.getTwelve()) ? BigDecimal.ZERO : waterDataYear.getTwelve();
            yearTotal = yearTotal.add(bigDecimal).add(bigDecimal1).add(bigDecimal2).add(bigDecimal3).add(bigDecimal4)
                    .add(bigDecimal5).add(bigDecimal6).add(bigDecimal7).add(bigDecimal8).add(bigDecimal9).add(bigDecimal10)
                    .add(bigDecimal11);
            waterDataYear.setTotal(yearTotal);
        });
        waterDataYearService.saveOrUpdateBatch(waterDataYears);
    }

    private static void setMonthData(WaterDataDetailVo dataDetailVo, int monthFlag, WaterDataYear waterDataYear1) {
        if (monthFlag == 1) {
            waterDataYear1.setOne(dataDetailVo.getTotal());
        } else if (monthFlag == 2) {
            waterDataYear1.setTwo(dataDetailVo.getTotal());
        } else if (monthFlag == 3) {
            waterDataYear1.setThree(dataDetailVo.getTotal());
        } else if (monthFlag == 4) {
            waterDataYear1.setFour(dataDetailVo.getTotal());
        } else if (monthFlag == 5) {
            waterDataYear1.setFive(dataDetailVo.getTotal());
        } else if (monthFlag == 6) {
            waterDataYear1.setSix(dataDetailVo.getTotal());
        } else if (monthFlag == 7) {
            waterDataYear1.setSeven(dataDetailVo.getTotal());
        } else if (monthFlag == 8) {
            waterDataYear1.setEight(dataDetailVo.getTotal());
        } else if (monthFlag == 9) {
            waterDataYear1.setNine(dataDetailVo.getTotal());
        } else if (monthFlag == 10) {
            waterDataYear1.setTen(dataDetailVo.getTotal());
        } else if (monthFlag == 11) {
            waterDataYear1.setEleven(dataDetailVo.getTotal());
        } else if (monthFlag == 12) {
            waterDataYear1.setTwelve(dataDetailVo.getTotal());
        }
    }

    /**
     * @Description 采集处理重构
     * @Author lrj
     */
    private void handleDataNew(LocalDateTime beginTime, LocalDateTime endTime, List<DataDeviceVo> dataDeviceVos, Integer tenant) {
        log.info("冷冻水采集================================");
        SessionContextHolder.requestTenant(tenant);
        for (DataDeviceVo dataDeviceVo : dataDeviceVos) {
            try {
                WaterDataDay waterDataDayExist = waterDataDayService.getOne(Wrappers.<WaterDataDay>lambdaQuery()
                                                                                    .eq(WaterDataDay::getDataTime, beginTime)
                                                                                    .eq(WaterDataDay::getDeviceCode, dataDeviceVo.getDeviceCode()));
                if (Objects.isNull(waterDataDayExist)) {
                    DevicePropHistoryParam historicalAttributeChangeQuery = new DevicePropHistoryParam();
                    historicalAttributeChangeQuery.setDeviceId(dataDeviceVo.getDeviceId());
                    historicalAttributeChangeQuery.setStartTs(DateUtils.toTimestamp(beginTime));
                    historicalAttributeChangeQuery.setEndTs(DateUtils.toTimestamp(endTime));
                    historicalAttributeChangeQuery.setMaxRecords(10);
                    List<String> propertyCodes = new ArrayList<>();
                    propertyCodes.add("day_time");
                    historicalAttributeChangeQuery.setPropCodes(propertyCodes);
                    List<KeyValueWithTsDTO> list = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                    KeyValueWithTsDTO historicalAttributeChangeDTO = null;
                    if (CollectionUtils.isNotEmpty(list)) {
                        Set<KeyValueWithTsDTO> distinctList = list.stream()
                                .filter(t -> Objects.nonNull(t.getValue()))
                                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(t -> t.getValue()
                                                                   .toString()))),
                                                           HashSet::new));
                        //根据属性值去重后只有一条则设备没有使用，忽略累计
                        if (distinctList.size() == 1) {
                            historicalAttributeChangeDTO = new KeyValueWithTsDTO();
                        } else {
                            historicalAttributeChangeDTO = list.stream().filter(t -> Objects.nonNull(t.getValue()))
                                    .max(Comparator.comparing(t -> new BigDecimal(t.getValue().toString())))
                                    .orElse(null);
                        }

                    }
                    if (Objects.nonNull(historicalAttributeChangeDTO) && Objects.nonNull(historicalAttributeChangeDTO.getValue())) {
                        BigDecimal readeValue = new BigDecimal(historicalAttributeChangeDTO.getValue()
                                                                       .toString()).setScale(2, RoundingMode.UP);
                        BigDecimal hour = readeValue.divide(new BigDecimal("60"), 2, RoundingMode.UP);
                        BigDecimal flow = hour.multiply(new BigDecimal(dataDeviceVo.getUnitTraffic())).setScale(2, RoundingMode.UP);
                        BigDecimal dayTotal = flow.multiply(dataDeviceVo.getUnitPrice()).setScale(2, RoundingMode.UP);
                        //天统计
                        WaterDataDay waterDataDay = new WaterDataDay();
                        waterDataDay.setCode(dataDeviceVo.getCode());
                        waterDataDay.setDepartmentId(dataDeviceVo.getDepartmentId());
                        waterDataDay.setGearPositionAreaId(dataDeviceVo.getGearPositionAreaId());
                        waterDataDay.setGearPositionCode(dataDeviceVo.getGearPositionCode());
                        waterDataDay.setDeviceCode(dataDeviceVo.getDeviceCode());
                        waterDataDay.setShiftMaster(dataDeviceVo.getShiftMaster());
                        waterDataDay.setUnitPrice(dataDeviceVo.getUnitPrice());
                        waterDataDay.setDepartment(dataDeviceVo.getDepartment());
                        waterDataDay.setGearPositionArea(dataDeviceVo.getGearPositionArea());
                        waterDataDay.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                        waterDataDay.setFlow(flow);
                        waterDataDay.setDuration(hour);
                        waterDataDay.setTotal(dayTotal);
                        waterDataDay.setDataTime(DateUtils.formatDate(beginTime));
                        Integer month = beginTime.getMonthValue();
                        Integer day = beginTime.getDayOfMonth();
                        waterDataDay.setStatisticalFlag(month + "月" + day + "日");
                        waterDataDayService.save(waterDataDay);
                    }
                    log.debug("冷冻水采集结束================================");
                }
            } catch (Exception e) {
                log.error("采集失败==={}", e);
            }
        }

    }

    /**
     * @Description 采集处理
     * @Author lrj
     */
    private void handleData(LocalDateTime beginTime, LocalDateTime endTime, List<DataDeviceVo> dataDeviceVos, Integer tenant) {
        log.info("冷冻水采集================================");
        SessionContextHolder.requestTenant(tenant);
        String format = DateUtils.format(beginTime, "yyyy-MM");
        String formatYear = DateUtils.format(beginTime, "yyyy");
        List<String> ids = dataDeviceVos.stream().map(DataDeviceVo::getDeviceCode).collect(Collectors.toList());
        List<WaterDataMonth> months = waterDataMonthService.list(Wrappers.<WaterDataMonth>lambdaQuery()
                                                                         .in(WaterDataMonth::getDeviceCode, ids)
                                                                         .eq(WaterDataMonth::getDataTime, format));
        List<WaterDataYear> years = waterDataYearService.list(Wrappers.<WaterDataYear>lambdaQuery()
                                                                      .in(WaterDataYear::getDeviceCode, ids)
                                                                      .eq(WaterDataYear::getDataTime, formatYear));
        Map<String, WaterDataMonth> monthMap = months.stream().collect(Collectors.toMap(WaterDataMonth::getDeviceCode, s -> s));
        Map<String, WaterDataYear> yearMap = years.stream().collect(Collectors.toMap(WaterDataYear::getDeviceCode, s -> s));
        for (DataDeviceVo dataDeviceVo : dataDeviceVos) {
            try {
                WaterDataDay waterDataDayExist = waterDataDayService.getOne(Wrappers.<WaterDataDay>lambdaQuery()
                                                                                    .eq(WaterDataDay::getDataTime, beginTime)
                                                                                    .eq(WaterDataDay::getDeviceCode, dataDeviceVo.getDeviceCode()));
                if (Objects.isNull(waterDataDayExist)) {
                    DevicePropHistoryParam historicalAttributeChangeQuery = new DevicePropHistoryParam();
                    historicalAttributeChangeQuery.setDeviceId(dataDeviceVo.getDeviceId());
                    historicalAttributeChangeQuery.setStartTs(DateUtils.toTimestamp(beginTime));
                    historicalAttributeChangeQuery.setEndTs(DateUtils.toTimestamp(endTime));
                    historicalAttributeChangeQuery.setMaxRecords(10);
                    List<String> propertyCodes = new ArrayList<>();
                    propertyCodes.add("day_time");
                    historicalAttributeChangeQuery.setPropCodes(propertyCodes);
                    List<KeyValueWithTsDTO> list = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                    KeyValueWithTsDTO historicalAttributeChangeDTO = null;
                    if (CollectionUtils.isNotEmpty(list)) {
                        Set<KeyValueWithTsDTO> distinctList = list.stream()
                                .filter(t -> Objects.nonNull(t.getValue()))
                                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(t -> t.getValue()
                                                                   .toString()))),
                                                           HashSet::new));
                        //根据属性值去重后只有一条则设备没有使用，忽略累计
                        if (distinctList.size() == 1) {
                            historicalAttributeChangeDTO = new KeyValueWithTsDTO();
                        } else {
                            historicalAttributeChangeDTO = list.stream().filter(t -> Objects.nonNull(t.getValue()))
                                    .max(Comparator.comparing(t -> new BigDecimal(t.getValue().toString())))
                                    .orElse(null);
                        }
                    }
                    if (Objects.nonNull(historicalAttributeChangeDTO) && Objects.nonNull(historicalAttributeChangeDTO.getValue())) {
                        BigDecimal readeValue = new BigDecimal(historicalAttributeChangeDTO.getValue()
                                                                       .toString()).setScale(2, RoundingMode.UP);
                        BigDecimal hour = readeValue.divide(new BigDecimal("60"), 2, RoundingMode.UP);
                        BigDecimal flow = hour.multiply(new BigDecimal(dataDeviceVo.getUnitTraffic())).setScale(2, RoundingMode.UP);
                        BigDecimal dayTotal = flow.multiply(dataDeviceVo.getUnitPrice()).setScale(2, RoundingMode.UP);
                        //天统计
                        WaterDataDay waterDataDay = new WaterDataDay();
                        waterDataDay.setCode(dataDeviceVo.getCode());
                        waterDataDay.setDepartmentId(dataDeviceVo.getDepartmentId());
                        waterDataDay.setGearPositionAreaId(dataDeviceVo.getGearPositionAreaId());
                        waterDataDay.setGearPositionCode(dataDeviceVo.getGearPositionCode());
                        waterDataDay.setDeviceCode(dataDeviceVo.getDeviceCode());
                        waterDataDay.setShiftMaster(dataDeviceVo.getShiftMaster());
                        waterDataDay.setUnitPrice(dataDeviceVo.getUnitPrice());
                        waterDataDay.setDepartment(dataDeviceVo.getDepartment());
                        waterDataDay.setGearPositionArea(dataDeviceVo.getGearPositionArea());
                        waterDataDay.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                        waterDataDay.setFlow(flow);
                        waterDataDay.setDuration(hour);
                        waterDataDay.setTotal(dayTotal);
                        waterDataDay.setDataTime(DateUtils.formatDate(beginTime));
                        Integer month = beginTime.getMonthValue();
                        Integer day = beginTime.getDayOfMonth();
                        waterDataDay.setStatisticalFlag(month + "月" + day + "日");
                        waterDataDayService.save(waterDataDay);
                        //月统计
                        WaterDataMonth waterDataMonth = monthMap.get(dataDeviceVo.getDeviceCode());
                        int monthFlag = beginTime.getMonthValue();
                        if (Objects.nonNull(waterDataMonth)) {
                            int month1 = beginTime.getDayOfMonth();
                            String statisticalCycle = waterDataMonth.getStatisticalCycle();
                            String[] split = statisticalCycle.split("-");
                            String monthCycle = split[0] + "-" + month1;
                            waterDataMonth.setStatisticalCycle(monthCycle);
                            waterDataMonth.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                            waterDataMonth.setFlow(waterDataMonth.getFlow().add(flow));
                            waterDataMonth.setTotal(waterDataMonth.getTotal().add(dayTotal));
                            waterDataMonth.setUnitPrice(dataDeviceVo.getUnitPrice());
                            waterDataMonth.setDuration(waterDataMonth.getDuration().add(hour));
                            waterDataMonthService.updateById(waterDataMonth);
                        } else {
                            WaterDataMonth waterDataMonth1 = new WaterDataMonth();
                            waterDataMonth1.setDataTime(format);
                            waterDataMonth1.setFlow(flow);
                            waterDataMonth1.setUnitPrice(dataDeviceVo.getUnitPrice());
                            waterDataMonth1.setCode(dataDeviceVo.getCode());
                            waterDataMonth1.setDuration(hour);
                            waterDataMonth1.setDepartmentId(dataDeviceVo.getDepartmentId());
                            waterDataMonth1.setGearPositionAreaId(dataDeviceVo.getGearPositionAreaId());
                            waterDataMonth1.setTotal(dayTotal);
                            waterDataMonth1.setDepartment(dataDeviceVo.getDepartment());
                            waterDataMonth1.setGearPositionArea(dataDeviceVo.getGearPositionArea());
                            waterDataMonth1.setDeviceCode(dataDeviceVo.getDeviceCode());
                            waterDataMonth1.setShiftMaster(dataDeviceVo.getShiftMaster());
                            waterDataMonth1.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                            waterDataMonth1.setShiftMasterPhone(dataDeviceVo.getShiftMasterPhone());
                            var time = DateUtils.beginOfMonth(beginTime);
                            int month1 = time.getDayOfMonth();
                            int month2 = beginTime.getDayOfMonth();
                            String format1 = DateUtils.format(dataDeviceVo.getBillableTime(), "yyyy-MM");
                            String format2 = DateUtils.format(beginTime, "yyyy-MM");
                            if (format1.equals(format2)) {
                                month1 = dataDeviceVo.getBillableTime().getDayOfMonth();
                            }
                            waterDataMonth1.setStatisticalCycle(month1 + "-" + month2);
                            waterDataMonth1.setStatisticalFlag(monthFlag + "月");
                            waterDataMonth1.setGearPositionCode(dataDeviceVo.getGearPositionCode());
                            waterDataMonthService.save(waterDataMonth1);
                        }
                        //年统计
                        WaterDataYear waterDataYear = yearMap.get(dataDeviceVo.getDeviceCode());
                        if (Objects.nonNull(waterDataYear)) {
                            waterDataYear.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                            waterDataYear.setFlow(waterDataYear.getFlow().add(flow));
                            waterDataYear.setDuration(waterDataYear.getDuration().add(hour));
                            if (monthFlag == 1) {
                                waterDataYear.setOne(waterDataYear.getOne().add(dayTotal));
                            } else if (monthFlag == 2) {
                                waterDataYear.setTwo(waterDataYear.getTwo().add(dayTotal));
                            } else if (monthFlag == 3) {
                                waterDataYear.setThree(waterDataYear.getThree().add(dayTotal));
                            } else if (monthFlag == 4) {
                                waterDataYear.setFour(waterDataYear.getFour().add(dayTotal));
                            } else if (monthFlag == 5) {
                                waterDataYear.setFive(waterDataYear.getFive().add(dayTotal));
                            } else if (monthFlag == 6) {
                                waterDataYear.setSix(waterDataYear.getSix().add(dayTotal));
                            } else if (monthFlag == 7) {
                                waterDataYear.setSeven(waterDataYear.getSeven().add(dayTotal));
                            } else if (monthFlag == 8) {
                                waterDataYear.setEight(waterDataYear.getEight().add(dayTotal));
                            } else if (monthFlag == 9) {
                                waterDataYear.setNine(waterDataYear.getNine().add(dayTotal));
                            } else if (monthFlag == 10) {
                                waterDataYear.setTen(waterDataYear.getTen().add(dayTotal));
                            } else if (monthFlag == 11) {
                                waterDataYear.setEleven(waterDataYear.getEleven().add(dayTotal));
                            } else if (monthFlag == 12) {
                                waterDataYear.setTwelve(waterDataYear.getTwelve().add(dayTotal));
                            }
                            BigDecimal yearTotal = BigDecimal.ZERO;
                            BigDecimal bigDecimal = Objects.isNull(waterDataYear.getOne()) ? BigDecimal.ZERO : waterDataYear.getOne();
                            BigDecimal bigDecimal1 = Objects.isNull(waterDataYear.getTwo()) ? BigDecimal.ZERO : waterDataYear.getTwo();
                            BigDecimal bigDecimal2 = Objects.isNull(waterDataYear.getThree()) ? BigDecimal.ZERO : waterDataYear.getThree();
                            BigDecimal bigDecimal3 = Objects.isNull(waterDataYear.getFour()) ? BigDecimal.ZERO : waterDataYear.getFour();
                            BigDecimal bigDecimal4 = Objects.isNull(waterDataYear.getFive()) ? BigDecimal.ZERO : waterDataYear.getFive();
                            BigDecimal bigDecimal5 = Objects.isNull(waterDataYear.getSix()) ? BigDecimal.ZERO : waterDataYear.getSix();
                            BigDecimal bigDecimal6 = Objects.isNull(waterDataYear.getSeven()) ? BigDecimal.ZERO : waterDataYear.getSeven();
                            BigDecimal bigDecimal7 = Objects.isNull(waterDataYear.getEight()) ? BigDecimal.ZERO : waterDataYear.getEight();
                            BigDecimal bigDecimal8 = Objects.isNull(waterDataYear.getNine()) ? BigDecimal.ZERO : waterDataYear.getNine();
                            BigDecimal bigDecimal9 = Objects.isNull(waterDataYear.getTen()) ? BigDecimal.ZERO : waterDataYear.getTen();
                            BigDecimal bigDecimal10 =
                                    Objects.isNull(waterDataYear.getEleven()) ? BigDecimal.ZERO : waterDataYear.getEleven();
                            BigDecimal bigDecimal11 =
                                    Objects.isNull(waterDataYear.getTwelve()) ? BigDecimal.ZERO : waterDataYear.getTwelve();
                            yearTotal = yearTotal.add(bigDecimal).add(bigDecimal1).add(bigDecimal2).add(bigDecimal3).add(bigDecimal4)
                                    .add(bigDecimal5).add(bigDecimal6).add(bigDecimal7).add(bigDecimal8).add(bigDecimal9).add(bigDecimal10)
                                    .add(bigDecimal11);
                            waterDataYear.setTotal(yearTotal);
                            waterDataYearService.updateById(waterDataYear);
                        } else {
                            WaterDataYear waterDataYear1 = new WaterDataYear();
                            waterDataYear1.setCode(dataDeviceVo.getCode());
                            waterDataYear1.setDataTime(formatYear);
                            waterDataYear1.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                            waterDataYear1.setGearPositionCode(dataDeviceVo.getGearPositionCode());
                            waterDataYear1.setDepartmentId(dataDeviceVo.getDepartmentId());
                            waterDataYear1.setGearPositionAreaId(dataDeviceVo.getGearPositionAreaId());
                            waterDataYear1.setDeviceCode(dataDeviceVo.getDeviceCode());
                            waterDataYear1.setDepartment(dataDeviceVo.getDepartment());
                            waterDataYear1.setGearPositionArea(dataDeviceVo.getGearPositionArea());
                            waterDataYear1.setShiftMaster(dataDeviceVo.getShiftMaster());
                            waterDataYear1.setFlow(flow);
                            waterDataYear1.setDuration(hour);
                            if (monthFlag == 1) {
                                waterDataYear1.setOne(dayTotal);
                            } else if (monthFlag == 2) {
                                waterDataYear1.setTwo(dayTotal);
                            } else if (monthFlag == 3) {
                                waterDataYear1.setThree(dayTotal);
                            } else if (monthFlag == 4) {
                                waterDataYear1.setFour(dayTotal);
                            } else if (monthFlag == 5) {
                                waterDataYear1.setFive(dayTotal);
                            } else if (monthFlag == 6) {
                                waterDataYear1.setSix(dayTotal);
                            } else if (monthFlag == 7) {
                                waterDataYear1.setSeven(dayTotal);
                            } else if (monthFlag == 8) {
                                waterDataYear1.setEight(dayTotal);
                            } else if (monthFlag == 9) {
                                waterDataYear1.setNine(dayTotal);
                            } else if (monthFlag == 10) {
                                waterDataYear1.setTen(dayTotal);
                            } else if (monthFlag == 11) {
                                waterDataYear1.setEleven(dayTotal);
                            } else if (monthFlag == 12) {
                                waterDataYear1.setTwelve(dayTotal);
                            }
                            waterDataYear1.setTotal(dayTotal);
                            waterDataYearService.save(waterDataYear1);
                        }
                    }
                    log.debug("冷冻水采集结束================================");
                }
            } catch (Exception e) {
                log.error("采集失败==={}", e);
            }
        }
    }

    /**
     * @Description 冷冻水耗电采集
     * @Author lrj
     */
    @JobAnnotation(name = "consumePowerData", cron = "0 0/30 * * * ?", desc = "冷冻水耗电采集", param = "58197260")
    public void coldWaterConsumePowerData(String param) {
        SessionContextHolder.requestTenant(Integer.valueOf(param));
        var electricEnergyAnalyzer = waterMapTypeMapper.selectOne(Wrappers.<WaterMapType>lambdaQuery()
                                                                          .eq(WaterMapType::getMapCode, "electric-energy-analyzer"));
        var list = securityCommonService.list(Wrappers.<SecurityDevice>lambdaQuery()
                                                      .eq(SecurityDevice::getApplicationCode, "COOLINGWATER")
                                                      .eq(SecurityDevice::getDeviceProdCategoryCode, electricEnergyAnalyzer.getDeviceProdCategoryCode()));
        var nowDate = DateUtils.now();
        var date = DateUtils.formatDate(nowDate);
        if (CollectionUtils.isNotEmpty(list)) {
            for (SecurityDevice securityDevice : list) {
                try {
                    var offset = DateUtils.offset(nowDate, ChronoUnit.DAYS, -2);
                    var historicalAttributeChangeQuery = new DevicePropHistoryParam();
                    historicalAttributeChangeQuery.setDeviceId(securityDevice.getDeviceId());
                    historicalAttributeChangeQuery.setStartTs(DateUtils.toTimestamp(offset));
                    historicalAttributeChangeQuery.setEndTs(DateUtils.toTimestamp(nowDate));
                    historicalAttributeChangeQuery.setMaxRecords(30);
                    List<String> propertyCodes = new ArrayList<>();
                    propertyCodes.add("EPi");
                    historicalAttributeChangeQuery.setPropCodes(propertyCodes);
                    var listValue = iotDevicePropClient.devicePropHistoryProp(historicalAttributeChangeQuery);
                    var one = coldWaterElectricityMapper.selectOne(Wrappers.<ColdWaterElectricity>lambdaQuery()
                                                                           .eq(ColdWaterElectricity::getDeviceId, securityDevice.getDeviceId())
                                                                           .eq(ColdWaterElectricity::getCollectionTime, date));
                    var electricTotalValue = listValue.stream().filter(s -> s.getKey().equals("EPi")).findFirst()
                            .orElse(null);
                    var electricTotal = BigDecimal.ZERO;
                    if (Objects.nonNull(electricTotalValue)) {
                        electricTotal = electricTotal.add(electricTotalValue.getValue() == null ? BigDecimal.ZERO
                                                                                                : new BigDecimal(electricTotalValue
                                                                                                                         .getValue()
                                                                                                                         .toString()));
                    }

                    if (Objects.isNull(one)) {
                        var coldWaterElectricity = new ColdWaterElectricity();
                        coldWaterElectricity.setDeviceId(securityDevice.getDeviceId());
                        coldWaterElectricity.setLastReade(electricTotal);
                        coldWaterElectricity.setQuantityElectricity(BigDecimal.ZERO);
                        coldWaterElectricity.setCollectionTime(date);
                        coldWaterElectricityMapper.insert(coldWaterElectricity);
                    } else {
                        if (one.getCollectionTime().equals(date)) {
                            var quantityElectricity = one.getQuantityElectricity();
                            var lastReade = one.getLastReade();
                            if (!(electricTotal.compareTo(lastReade) == -1)) {
                                var subtract = electricTotal.subtract(lastReade);
                                var add = quantityElectricity.add(subtract);
                                one.setQuantityElectricity(add);
                                one.setLastReade(electricTotal);
                            }
                            coldWaterElectricityMapper.updateById(one);
                        } else {
                            var lastReade = one.getLastReade();
                            if (!(lastReade.compareTo(electricTotal) == -1)) {
                                var subtract = electricTotal.subtract(lastReade);
                                var coldWaterElectricity = new ColdWaterElectricity();
                                coldWaterElectricity.setQuantityElectricity(subtract);
                                coldWaterElectricity.setDeviceId(securityDevice.getDeviceId());
                                coldWaterElectricity.setCollectionTime(date);
                                coldWaterElectricity.setLastReade(electricTotal);
                                coldWaterElectricityMapper.insert(coldWaterElectricity);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.info("耗电采集失败", e);
                }
            }
        }
    }

}
