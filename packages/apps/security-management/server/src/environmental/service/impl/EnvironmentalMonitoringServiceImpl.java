package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.dao.EnvironmentalMonitoringMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.entity.EnvironmentalMonitoring;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.enumeration.EnvironmentalDataTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.enumeration.ParagraphEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.param.DateParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.service.EnvironmentalMonitoringService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.vo.EnvironmentalMonitoringVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.vo.EnvironmentalVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnvironmentalMonitoringServiceImpl extends ServiceImpl<EnvironmentalMonitoringMapper, EnvironmentalMonitoring> implements EnvironmentalMonitoringService {

    private final EnvironmentalMonitoringMapper environmentalMonitoringMapper;

    @Override
    public Map<String, Object> getEnvironmentalIndicators() {
        Map<String, Object> remap = new HashMap<>();
        DateParam dateParam = getDateParam();
        var startTime = dateParam.getStartTime();
        var endTime = dateParam.getEndTime();
        List<EnvironmentalMonitoring> environmentalMonitorings = environmentalMonitoringMapper.selectList(Wrappers
                                                                                                                  .<EnvironmentalMonitoring>lambdaQuery()
                                                                                                                  .between(EnvironmentalMonitoring::getDataTime, startTime, endTime)
                                                                                                                  .orderByDesc(EnvironmentalMonitoring::getDataTime));
        if (CollectionUtils.isNotEmpty(environmentalMonitorings)) {
            EnvironmentalMonitoring environmentalMonitoring = environmentalMonitorings.get(0);
            remap.put("co", environmentalMonitoring.getCo());
            remap.put("visibility", environmentalMonitoring.getVisibility());
            remap.put("brightness", environmentalMonitoring.getBrightness());
            remap.put("windSpeed", environmentalMonitoring.getWindSpeed());
            //co今日最高
            List<EnvironmentalVo> maxData = environmentalMonitoringMapper.getMaxData("co", "co_time", startTime, endTime);
            remap.put("coMax", maxData.get(0).getFieldData());
            remap.put("coTime", maxData.get(0).getTimeData());
            //能见度
            List<EnvironmentalVo> visibility = environmentalMonitoringMapper.getMinData("visibility", "visibility_time", startTime, endTime);
            remap.put("visibilityMin", visibility.get(0).getFieldData());
            remap.put("visibilityTime", visibility.get(0).getTimeData());
            //亮度
            List<EnvironmentalVo> brightness = environmentalMonitoringMapper.getMinData("brightness", "brightness_time", startTime, endTime);
            remap.put("brightnessMin", brightness.get(0).getFieldData());
            remap.put("brightnessTime", brightness.get(0).getTimeData());
            //风速
            List<EnvironmentalVo> windSpeed = environmentalMonitoringMapper.getMaxData("wind_speed", "wind_speed_time", startTime, endTime);
            remap.put("windSpeedMax", windSpeed.get(0).getFieldData());
            remap.put("windSpeedTime", windSpeed.get(0).getTimeData());
        }
        return remap;
    }

    @Override
    public Map<String, Object> getCotRendAnalysis() {
        Map<String, Object> remap = new HashMap<>();
        DateParam dateParam = getDateParam();
        Map<String, Object> today = SecurityManagementCommonUtil.getDateFormat("TODAY", dateParam.getStartTime(), dateParam.getEndTime());
        var st = (LocalDateTime) today.get("st");
        var et = (LocalDateTime) today.get("et");
        String timeFlag = (String) today.get("timeFlag");
        List<String> xtitle = (List<String>) today.get("xtitle");
        //基本端
        List<EnvironmentalVo> basicSegment = environmentalMonitoringMapper.getTrendAnalysisCoConcentrationTunnels(st, et, ParagraphEnum.BASIC_SEGMENT.name(), timeFlag);
        //入口段
        List<EnvironmentalVo> inletSection = environmentalMonitoringMapper.getTrendAnalysisCoConcentrationTunnels(st, et, ParagraphEnum.INLET_SECTION.name(), timeFlag);
        //入口段
        List<EnvironmentalVo> exitSection = environmentalMonitoringMapper.getTrendAnalysisCoConcentrationTunnels(st, et, ParagraphEnum.EXIT_SECTION.name(), timeFlag);
        remap.put("xtitle", xtitle);
        remap.put("basicSegment", basicSegment);
        remap.put("inletSection", inletSection);
        remap.put("exitSection", exitSection);
        return remap;
    }

    @Override
    public Map<String, Object> trendAnalysisRealTime(String paragraph) {
        Map<String, Object> remap = new HashMap<>();
        DateParam dateParam = getDateParam();
        Map<String, Object> today = SecurityManagementCommonUtil.getDateFormat("TODAY", dateParam.getStartTime(), dateParam.getEndTime());
        var st = (LocalDateTime) today.get("st");
        var et = (LocalDateTime) today.get("et");
        String timeFlag = (String) today.get("timeFlag");
        List<String> xtitle = (List<String>) today.get("xtitle");
        //co
        List<EnvironmentalVo> co = environmentalMonitoringMapper.trendAnalysisRealTime("co", st, et, paragraph, timeFlag);
        //能见度
        List<EnvironmentalVo> visibility = environmentalMonitoringMapper.trendAnalysisRealTime("visibility", st, et, paragraph, timeFlag);
        //亮度
        List<EnvironmentalVo> brightness = environmentalMonitoringMapper.trendAnalysisRealTime("brightness", st, et, paragraph, timeFlag);
        //风速
        List<EnvironmentalVo> windSpeed = environmentalMonitoringMapper.trendAnalysisRealTime("wind_speed", st, et, paragraph, timeFlag);
        remap.put("xtitle", xtitle);
        remap.put("co", co);
        remap.put("visibility", visibility);
        remap.put("brightness", brightness);
        remap.put("windSpeed", windSpeed);
        return remap;
    }

    @Override
    public Pagination<EnvironmentalMonitoringVo> getEnvironmentalPage(DateParam param) {
        String dataField = "";
        if (EnvironmentalDataTypeEnum.CO.name().equals(param.getDataTypeEnum().name())) {
            dataField = "co";
        } else if (EnvironmentalDataTypeEnum.VISIBILITY.name().equals(param.getDataTypeEnum().name())) {
            dataField = "visibility";
        } else if (EnvironmentalDataTypeEnum.BRIGHTNESS.name().equals(param.getDataTypeEnum().name())) {
            dataField = "brightness";
        } else if (EnvironmentalDataTypeEnum.WIND_SPEED.name().equals(param.getDataTypeEnum().name())) {
            dataField = "wind_speed";
        }
        Page<EnvironmentalMonitoringVo> environmentalPage = environmentalMonitoringMapper.getEnvironmentalPage(PagingUtils.convert(param), param, dataField);
        return PagingUtils.convert(environmentalPage);
    }

    private DateParam getDateParam() {
        var date = LocalDateTime.now();
        var startTime = DateUtils.beginOfDay(date);
        var endTime = DateUtils.endOfDay(date);
        DateParam dateParam = new DateParam();
        dateParam.setStartTime(startTime);
        dateParam.setEndTime(endTime);
        return dateParam;
    }
}
