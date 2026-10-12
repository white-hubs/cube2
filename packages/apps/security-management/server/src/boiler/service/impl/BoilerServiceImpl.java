package com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.client.AlarmBimStatisticsClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmBimDeviceStatisticsDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceBimSceneStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.dao.BoilerDataMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.param.BoilerParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.param.BoilerStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.service.BoilerService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo.BoilerDataVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo.BoilerRecordPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo.BoilerResultVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BoilerServiceImpl implements BoilerService {

    private final DeviceBimSceneStdClient deviceBimSceneClient;

    private final AlarmBimStatisticsClient alarmBimStatisticsClient;

    private final BoilerDataMapper boilerDataMapper;

    @Override
    public Map<String, Object> getAlarmBimDeviceStatistics(BoilerStatisticsParam param) {
        Map<String, Object> remap = new HashMap<>();
        List<Long> deviceIds = getDeviceIds(param);
        List<AlarmBimDeviceStatisticsDTO> alarmBimDeviceStatisticsDTOS = new ArrayList<>();
        List<AlarmBimDeviceStatisticsDTO> timeGroupStatistics = new ArrayList<>();
        Map<String, Object> dateFormat = getDateFormat(param);
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            AlarmBimStatisticsQueryParam copying = ObjectUtils.copying(param, AlarmBimStatisticsQueryParam.class);
            copying.setApplicationCode("BOLIER");
            copying.setEventName("超温报警事件");
            copying.setDeviceIds(deviceIds);
            alarmBimDeviceStatisticsDTOS = alarmBimStatisticsClient.getDeviceGroupStatistics(copying);
            copying.setTimeFormat(dateFormat.get("timeFlag").toString());
            timeGroupStatistics = alarmBimStatisticsClient.getTimeGroupStatistics(copying);
        }
        List<String> xtitle = (List<String>) dateFormat.get("xtitle");
        remap.put("xtitle", xtitle);
        remap.put("deviceStatistics", alarmBimDeviceStatisticsDTOS);
        remap.put("timeGroupStatistics", timeGroupStatistics);
        return remap;
    }

    @Override
    public Map<String, Object> getDataOverview(BoilerStatisticsParam param) {
        Map<String, Object> remap = new HashMap<>();
        List<Long> list = getDeviceIds(param);
        Map<String, Object> dateFormat = getDateFormat(param);
        LocalDateTime st = (LocalDateTime) dateFormat.get("st");
        LocalDateTime et = (LocalDateTime) dateFormat.get("et");
        LocalDateTime ost = (LocalDateTime) dateFormat.get("ost");
        LocalDateTime oet = (LocalDateTime) dateFormat.get("oet");
        BigDecimal waterValue = boilerDataMapper.getWaterValue(list, st, et);
        BigDecimal waterOldValue = boilerDataMapper.getWaterValue(list, ost, oet);
        BigDecimal waterRate = getRate(waterOldValue.toString(), waterValue.toString());
        BigDecimal electricityValue = boilerDataMapper.getElectricityValue(list, st, et);
        BigDecimal electricityOldValue = boilerDataMapper.getElectricityValue(list, ost, ost);
        BigDecimal electricityRate = getRate(electricityOldValue.toString(), electricityValue.toString());
        remap.put("waterValue", waterValue);
        remap.put("electricityValue", electricityValue);
        remap.put("waterRate", waterRate);
        remap.put("electricityRate", electricityRate);
        return remap;
    }

    @Override
    public BoilerResultVo getDataTemperature(BoilerStatisticsParam param) {
        BoilerResultVo boilerResultVo = new BoilerResultVo();
        List<Long> deviceIds = getDeviceIds(param);
        Map<String, Object> dateFormat = getDateFormat(param);
        LocalDateTime st = (LocalDateTime) dateFormat.get("st");
        LocalDateTime et = (LocalDateTime) dateFormat.get("et");
        String timeFlag = (String) dateFormat.get("timeFlag");
        List<String> xtitle = (List<String>) dateFormat.get("xtitle");
        List<BoilerDataVo> dataTemperature = boilerDataMapper.getDataTemperature(deviceIds, st, et, timeFlag);
        Map<String, List<BoilerDataVo>> collect = dataTemperature.stream().collect(Collectors.groupingBy(BoilerDataVo::getDeviceName));
        boilerResultVo.setXtitle(xtitle);
        boilerResultVo.setDataMap(collect);
        return boilerResultVo;
    }

    @Override
    public BoilerResultVo getDataWater(BoilerStatisticsParam param) {
        BoilerResultVo boilerResultVo = new BoilerResultVo();
        List<Long> list = getDeviceIds(param);
        Map<String, Object> dateFormat = getDateFormat(param);
        LocalDateTime st = (LocalDateTime) dateFormat.get("st");
        LocalDateTime et = (LocalDateTime) dateFormat.get("et");
        String timeFlag = (String) dateFormat.get("timeFlag");
        List<String> xtitle = (List<String>) dateFormat.get("xtitle");
        List<BoilerDataVo> dataWater = boilerDataMapper.getDataWater(list, st, et, timeFlag);
        Map<String, List<BoilerDataVo>> collect = dataWater.stream().collect(Collectors.groupingBy(BoilerDataVo::getDeviceName));
        boilerResultVo.setXtitle(xtitle);
        boilerResultVo.setDataMap(collect);
        return boilerResultVo;
    }

    @Override
    public BoilerResultVo getDataElectricity(BoilerStatisticsParam param) {
        BoilerResultVo boilerResultVo = new BoilerResultVo();
        List<Long> deviceIds = getDeviceIds(param);
        Map<String, Object> dateFormat = getDateFormat(param);
        LocalDateTime st = (LocalDateTime) dateFormat.get("st");
        LocalDateTime et = (LocalDateTime) dateFormat.get("et");
        String timeFlag = (String) dateFormat.get("timeFlag");
        List<String> xtitle = (List<String>) dateFormat.get("xtitle");
        List<BoilerDataVo> dataWater = boilerDataMapper.getDataElectricity(deviceIds, st, et, timeFlag);
        Map<String, List<BoilerDataVo>> collect = dataWater.stream().collect(Collectors.groupingBy(BoilerDataVo::getDeviceName));
        boilerResultVo.setXtitle(xtitle);
        boilerResultVo.setDataMap(collect);
        return boilerResultVo;
    }

    @Override
    public Pagination<BoilerRecordPageVo> getBoilerDataRecordPage(BoilerParam param) {
        Page<BoilerRecordPageVo> boilerDataRecordPage = boilerDataMapper.getBoilerDataRecordPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(boilerDataRecordPage);
    }

    /**
     * @Description 时间格式
     * @Author lrj
     */
    private Map<String, Object> getDateFormat(BoilerStatisticsParam param) {
        Map<String, Object> remap = new HashMap<>();
        String timeFlag = "";
        String[] split = param.getPeriod().split(",");
        var st = DateUtils.parse(split[0]);
        var et = DateUtils.parse(split[1]);
        LocalDateTime ost = null;
        LocalDateTime oet = null;
        List<String> xtitle = new ArrayList<>();

        if (param.getType().equals("TODAY")) {
            timeFlag = "'HH'";
            int between = (int) DateUtils.between(st, et, ChronoUnit.HOURS);
            ost = DateUtils.offset(st, ChronoUnit.DAYS, -1);
            oet = DateUtils.offset(et, ChronoUnit.DAYS, -1);
            for (int i = 0; i <= between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.HOURS, i);
                int hour = offset.getHour();
                if (hour < 10) {
                    xtitle.add("0" + hour);
                } else {
                    xtitle.add(String.valueOf(hour));
                }
            }
        } else if (param.getType().equals("THIS_WEEK")) {
            ost = DateUtils.offset(st, ChronoUnit.DAYS, -7);
            oet = DateUtils.offset(et, ChronoUnit.DAYS, -7);
            timeFlag = "'yyyy-MM-dd'";
            int between = (int) DateUtils.between(st, et, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.DAYS, i);
                xtitle.add(DateUtils.formatDate(offset));
            }
        } else if (param.getType().equals("THIS_MONTH")) {
            ost = DateUtils.offset(st, ChronoUnit.MONTHS, -1);
            oet = DateUtils.offset(et, ChronoUnit.MONTHS, -1);
            timeFlag = "'yyyy-MM-dd'";
            int between = (int) DateUtils.between(st, et, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.DAYS, i);
                xtitle.add(DateUtils.formatDate(offset));
            }
        } else if (param.getType().equals("THIS_YEAR")) {
            ost = DateUtils.offset(st, ChronoUnit.YEARS, -1);
            oet = DateUtils.offset(et, ChronoUnit.YEARS, -1);
            timeFlag = "'MM'";
            long between = DateUtils.between(st, et, ChronoUnit.MONTHS);

            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.MONTHS, i);
                xtitle.add(DateUtils.format(offset, "MM"));
            }
        }
        remap.put("st", st);
        remap.put("et", et);
        remap.put("ost", ost);
        remap.put("oet", oet);
        remap.put("timeFlag", timeFlag);
        remap.put("xtitle", xtitle);
        return remap;
    }

    private BigDecimal getRate(String a, String b) {
        BigDecimal previous = new BigDecimal(a);
        BigDecimal postValue = new BigDecimal(b);
        BigDecimal subtract = postValue.subtract(previous);
        BigDecimal divide = BigDecimal.ZERO;
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            divide = divide.add(new BigDecimal(100));
        } else {
            divide = subtract.divide(previous, 2, RoundingMode.HALF_UP);
        }
        return divide;
    }

    private List<Long> getDeviceIds(BoilerStatisticsParam param) {
        Set<Long> bimDeviceIds = deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        List<Long> list = new ArrayList<>(bimDeviceIds);
        return list;
    }
}
