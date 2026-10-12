package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.lang.AttributePermissionEnum;
import com.guhecloud.cube.base.device.center.metadata.util.DevicePropUtils;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.PropertyTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropHistoryPageParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.HistorySampleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.ColdWaterAttributeMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.ColdWholesalerMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.GearPositionMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.WaterDataDayMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.WaterDataMonthMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.WaterDataYearMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.WaterMapTypeMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ColdWaterAttribute;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterMapType;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.CommonTimeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.DeviceHistoryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.StatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWaterStatisticsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataKeyValueVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DeviceAttributeRunningResponseVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WaterDataDetailVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.dto.DevicePropWithTsDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ColdWaterStatisticsServiceImpl implements ColdWaterStatisticsService {
    private final ColdWholesalerMapper coldWholesalerMapper;

    private final GearPositionMapper gearPositionMapper;

    private final WaterDataMonthMapper waterDataMonthMapper;

    private final SecurityCommonMapper securityCommonMapper;

    private final ColdWaterAttributeMapper coldWaterAttributeMapper;

    private final WaterDataYearMapper waterDataYearMapper;

    private final WaterDataDayMapper waterDataDayMapper;

    private final WaterMapTypeMapper waterMapTypeMapper;

    private final IotDeviceClient iotDeviceClient;

    @Override
    public Map<String, Object> basicDataReportAnalysis() {
        Map<String, Object> remap = new HashMap<>();
        long integer = coldWholesalerMapper.selectCount(null);
        long integer1 = gearPositionMapper.selectCount(null);
        //TODO code待定
        WaterMapType chilledWaterTimer = waterMapTypeMapper.selectOne(Wrappers.<WaterMapType>lambdaQuery()
                                                                              .eq(WaterMapType::getMapCode, "chilled-water-timer"));
        long integer2 = securityCommonMapper.selectCount(Wrappers.<SecurityDevice>lambdaQuery()
                                                                 .eq(SecurityDevice::getApplicationCode, "COOLINGWATER")
                                                                 .eq(SecurityDevice::getDeviceProdCategoryCode, chilledWaterTimer.getDeviceProdCategoryCode()));
        WaterMapType networkController = waterMapTypeMapper.selectOne(Wrappers.<WaterMapType>lambdaQuery()
                                                                              .eq(WaterMapType::getMapCode, "network-controller"));
        long integer3 = securityCommonMapper.selectCount(Wrappers.<SecurityDevice>lambdaQuery()
                                                                 .eq(SecurityDevice::getApplicationCode, "COOLINGWATER")
                                                                 .eq(SecurityDevice::getDeviceProdCategoryCode, networkController.getDeviceProdCategoryCode()));

        remap.put("wholesaler", integer);
        remap.put("gearPosition", integer1);
        remap.put("frontController", integer2);
        remap.put("network", integer3);
        return remap;
    }

    @Override
    public Map<String, Object> merchantActivityData(StatisticsParam statisticsParam) {
        Map<String, Object> remap = new HashMap<>();
        CommonTimeParam month = getTimeParam("Month", statisticsParam.getStartTime(), statisticsParam.getEndTime());
        //商户
        Integer numberOfActive = waterDataMonthMapper.numberOfActiveMerchants(month.getStartTime(), month.getEndTime());
        //环比
        Integer numberOfActiveQoq = waterDataMonthMapper.numberOfActiveMerchants(month.getQoqStartTime(), month.getQoqEndTime());
        BigDecimal rateMerchantsQoq = getRate(numberOfActiveQoq.toString(), numberOfActive.toString());
        //同比
        Integer numberOfActiveYoy = waterDataMonthMapper.numberOfActiveMerchants(month.getYoyStartTime(), month.getYoyEndTime());
        BigDecimal rateMerchantsYoy = getRate(numberOfActiveYoy.toString(), numberOfActive.toString());
        //设备
        Integer numberOfActiveDevice = waterDataMonthMapper.numberOfActiveDevice(month.getStartTime(), month.getEndTime());
        Integer numberOfDevice = waterDataMonthMapper.numberOfDevice(month.getStartTime(), month.getEndTime());
        BigDecimal deviceNum = BigDecimal.ZERO;
        if (!numberOfDevice.equals(0)) {
            BigDecimal bigDecimalActive = new BigDecimal(numberOfActiveDevice);
            BigDecimal bigDecimalNumber = new BigDecimal(numberOfDevice);
            BigDecimal divide = bigDecimalActive.divide(bigDecimalNumber, 2, RoundingMode.HALF_UP);
            BigDecimal multiply = divide.multiply(new BigDecimal(100));
            deviceNum = deviceNum.add(multiply);
        }
        //环比
        Integer numberOfActiveDeviceQoq = waterDataMonthMapper.numberOfActiveDevice(month.getQoqStartTime(), month.getQoqEndTime());
        Integer numberOfDeviceQoq = waterDataMonthMapper.numberOfDevice(month.getQoqStartTime(), month.getQoqEndTime());
        BigDecimal deviceNumQoq = BigDecimal.ZERO;
        if (!numberOfDeviceQoq.equals(0)) {
            BigDecimal numberOfActiveDeviceQoqValue = new BigDecimal(numberOfActiveDeviceQoq);
            BigDecimal numberOfDeviceQoqValue = new BigDecimal(numberOfDeviceQoq);
            BigDecimal divide = numberOfActiveDeviceQoqValue.divide(numberOfDeviceQoqValue, 2, RoundingMode.HALF_UP);
            deviceNumQoq = deviceNumQoq.add(divide.multiply(new BigDecimal(100)));
        }
        BigDecimal rateDeviceQoq = getRate(deviceNumQoq.toString(), deviceNum.toString());
        //同比
        Integer numberOfActiveDeviceYoy = waterDataMonthMapper.numberOfActiveDevice(month.getYoyStartTime(), month.getYoyEndTime());
        Integer numberOfDeviceYoy = waterDataMonthMapper.numberOfDevice(month.getYoyStartTime(), month.getYoyEndTime());
        BigDecimal deviceNumYoy = BigDecimal.ZERO;
        if (!numberOfDeviceYoy.equals(0)) {
            BigDecimal bigDecimal1 = new BigDecimal(numberOfActiveDeviceYoy);
            BigDecimal bigDecimal2 = new BigDecimal(numberOfDeviceYoy);
            BigDecimal divide = bigDecimal1.divide(bigDecimal2, 2, RoundingMode.HALF_UP);
            deviceNumYoy = deviceNumYoy.add(divide.multiply(new BigDecimal(100)));
        }
        BigDecimal rateDeviceYoy = getRate(deviceNumYoy.toString(), deviceNum.toString());
        //活跃商户
        LocalDateTime dateTime = DateUtils.parse(statisticsParam.getTimeFlag());
        LocalDateTime offset1 = DateUtils.offset(dateTime, ChronoUnit.MONTHS, -6);
        String stMonth = DateUtils.format(offset1, "yyyy-MM");
        List<DataKeyValueVo> numberOfActiveMerchantsList = waterDataMonthMapper.getNumberOfActiveMerchants(stMonth, month.getEndTime());
        List<DataKeyValueVo> numberOfActiveDeviceList = waterDataMonthMapper.getNumberOfActiveDevice(stMonth, month.getEndTime());
        List<String> xTitle = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            LocalDateTime parse = DateUtils.parse(statisticsParam.getStartTime());
            LocalDateTime offset = DateUtils.offset(parse, ChronoUnit.MONTHS, -(6 - i));
            String format = DateUtils.format(offset, "yyyy-MM");
            xTitle.add(format);
        }
        remap.put("numberOfActive", numberOfActive);//商户活跃数
        remap.put("rateMerchantsQoq", rateMerchantsQoq);//环比
        remap.put("rateMerchantsYoy", rateMerchantsYoy);//同比
        remap.put("deviceNum", deviceNum);//设备数使用率
        remap.put("rateDeviceQoq", rateDeviceQoq);//环比
        remap.put("rateDeviceYoy", rateDeviceYoy);//同比
        remap.put("xTitle", xTitle);//横轴
        remap.put("numberOfActiveMerchantsList", numberOfActiveMerchantsList);//商户
        remap.put("numberOfActiveDeviceList", numberOfActiveDeviceList);//设备使用率
        return remap;
    }

    @Override
    public Map<String, Object> getChilledWaterData(StatisticsParam statisticsParam) {
        if (statisticsParam.getTimeType().equals("Year")) {
            LocalDateTime parse = DateUtils.endOfYear(DateUtils.parse(statisticsParam.getStartTime()));
            statisticsParam.setEndTime(DateUtils.format(parse, "yyyy-MM-dd"));
        }
        Map<String, Object> remap = new HashMap<>();
        CommonTimeParam timeParam = getTimeParam(statisticsParam.getTimeType(), statisticsParam.getStartTime(), statisticsParam.getEndTime());
        if (statisticsParam.getTimeType().equals("Year")) {
            String yyyy = DateUtils.format(DateUtils.parse(timeParam.getStartTime()), "yyyy");
            timeParam.setStartTime(yyyy);
            String enyyyy = DateUtils.format(DateUtils.parse(timeParam.getEndTime()), "yyyy");
            timeParam.setEndTime(enyyyy);
        }
        //累计费用
        BigDecimal total = gearPositionMapper.getStatisticsData("total", timeParam.getTableName(), timeParam.getStartTime(), timeParam.getEndTime(), statisticsParam.getDeviceCode(), statisticsParam);
        BigDecimal costRateQoq = BigDecimal.ZERO;
        BigDecimal costRateYoy = BigDecimal.ZERO;
        //运行时长
        BigDecimal duration = gearPositionMapper.getStatisticsData("duration", timeParam.getTableName(), timeParam.getStartTime(), timeParam.getEndTime(), statisticsParam.getDeviceCode(), statisticsParam);
        BigDecimal durationRateQoq = BigDecimal.ZERO;
        BigDecimal durationRateYoy = BigDecimal.ZERO;
        //流量
        BigDecimal flow = gearPositionMapper.getStatisticsData("flow", timeParam.getTableName(), timeParam.getStartTime(), timeParam.getEndTime(), statisticsParam.getDeviceCode(), statisticsParam);
        BigDecimal flowRateQoq = BigDecimal.ZERO;
        BigDecimal flowRateYoy = BigDecimal.ZERO;
        //横轴
        List<String> xtitle = new ArrayList<>();
        long timeSlot = timeParam.getTimeSlot() + 1;
        if (!statisticsParam.getTimeType().equals("Year")) {
            //费用环比
            BigDecimal totalQoq = gearPositionMapper.getStatisticsData("total", timeParam.getTableName(), timeParam.getQoqStartTime(), timeParam.getQoqEndTime(), statisticsParam.getDeviceCode(), statisticsParam);
            costRateQoq = costRateQoq.add(getRate(totalQoq.toString(), total.toString()));
            //费用同比
            BigDecimal totalYoy = gearPositionMapper.getStatisticsData("total", timeParam.getTableName(), timeParam.getYoyStartTime(), timeParam.getYoyEndTime(), statisticsParam.getDeviceCode(), statisticsParam);
            costRateYoy = costRateYoy.add(getRate(totalYoy.toString(), total.toString()));
            //时长环比
            BigDecimal durationQoq = gearPositionMapper.getStatisticsData("duration", timeParam.getTableName(), timeParam.getQoqStartTime(), timeParam.getQoqEndTime(), statisticsParam.getDeviceCode(), statisticsParam);
            durationRateQoq = durationRateQoq.add(getRate(durationQoq.toString(), duration.toString()));
            //时长同比
            BigDecimal durationYoy = gearPositionMapper.getStatisticsData("duration", timeParam.getTableName(), timeParam.getYoyStartTime(), timeParam.getYoyEndTime(), statisticsParam.getDeviceCode(), statisticsParam);
            durationRateYoy = durationRateYoy.add(getRate(durationYoy.toString(), duration.toString()));
            //流量环比
            BigDecimal flowQoq = gearPositionMapper.getStatisticsData("flow", timeParam.getTableName(), timeParam.getQoqStartTime(), timeParam.getQoqEndTime(), statisticsParam.getDeviceCode(), statisticsParam);
            flowRateQoq = flowRateQoq.add(getRate(flowQoq.toString(), flow.toString()));
            //流量同比
            BigDecimal flowYoy = gearPositionMapper.getStatisticsData("flow", timeParam.getTableName(), timeParam.getYoyStartTime(), timeParam.getYoyEndTime(), statisticsParam.getDeviceCode(), statisticsParam);
            flowRateYoy = flowRateYoy.add(getRate(flowYoy.toString(), flow.toString()));
            //横向坐标
            if (statisticsParam.getTimeType().equals("Month")) {
                for (int i = 0; i < timeSlot; i++) {
                    LocalDateTime offset = DateUtils.offset(DateUtils.parse(statisticsParam.getStartTime()), ChronoUnit.MONTHS, i);
                    xtitle.add(DateUtils.format(offset, "yyyy-MM"));
                }
            } else if (statisticsParam.getTimeType().equals("TODAY")) {
                for (int i = 0; i < timeSlot; i++) {
                    LocalDateTime offset = DateUtils.offset(DateUtils.parse(statisticsParam.getStartTime()), ChronoUnit.DAYS, i);
                    xtitle.add(DateUtils.formatDate(offset));
                }
            }
        } else {
            for (int i = 0; i < timeSlot; i++) {
                LocalDateTime offset = DateUtils.offset(DateUtils.parse(statisticsParam.getStartTime()), ChronoUnit.YEARS, i);
                xtitle.add(DateUtils.format(offset, "yyyy"));
            }
        }
        remap.put("costTotal", total);
        remap.put("costRateQoq", costRateQoq);
        remap.put("costRateYoy", costRateYoy);
        remap.put("duration", duration);
        remap.put("durationRateQoq", durationRateQoq);
        remap.put("durationRateYoy", durationRateYoy);
        remap.put("flow", flow);
        remap.put("flowRateQoq", flowRateQoq);
        remap.put("flowRateYoy", flowRateYoy);
        remap.put("xtitle", xtitle);
        List<DataKeyValueVo> costTrend = gearPositionMapper.getStatisticsDataTrend("total", timeParam.getTableName(), timeParam.getStartTime(), timeParam.getEndTime(), statisticsParam);
        List<DataKeyValueVo> durationTrend = gearPositionMapper.getStatisticsDataTrend("duration", timeParam.getTableName(), timeParam.getStartTime(), timeParam.getEndTime(), statisticsParam);
        List<DataKeyValueVo> flowTrend = gearPositionMapper.getStatisticsDataTrend("flow", timeParam.getTableName(), timeParam.getStartTime(), timeParam.getEndTime(), statisticsParam);
        remap.put("costTrend", costTrend);
        remap.put("durationTrend", durationTrend);
        remap.put("flowTrend", flowTrend);
        return remap;
    }

    @Override
    public Map<String, Object> getDeviceHistory(DeviceHistoryParam param) {
        Map<String, Object> remap = new HashMap<>();
        //比值数据
        Map<String, Object> map = deviceDataRatio(remap, param);
        //温度趋势图
        Map<String, Object> map1 = temperatureTrend(map, param);
        //统计明细
        Map<String, Object> statisticalDetails = getStatisticalDetails(map1, param);
        return statisticalDetails;
    }

    @Override
    public Pagination<KeyValueWithTsDTO> getDeviceAttributeDataHistory(DeviceHistoryParam param) {
        IotDevicePropClient iotDevicePropClient = ApplicationContextHolder.getBean(IotDevicePropClient.class);
        DevicePropHistoryPageParam devicePropHistoryPageParam = new DevicePropHistoryPageParam();
        if (StringUtils.isEmpty(param.getStartTime()) || StringUtils.isEmpty(param.getEndTime())) {
            var date = LocalDateTime.now();
            var st = DateUtils.beginOfDay(date);
            var et = DateUtils.endOfDay(date);
            devicePropHistoryPageParam.setStartTs(DateUtils.toTimestamp(st));
            devicePropHistoryPageParam.setEndTs(DateUtils.toTimestamp(et));
        } else {

            devicePropHistoryPageParam.setStartTs(DateUtils.toTimestamp(DateUtils.parse(param.getStartTime())));
            devicePropHistoryPageParam.setEndTs(DateUtils.toTimestamp(DateUtils.parse(param.getEndTime())));
        }
        devicePropHistoryPageParam.setDeviceId(param.getDeviceId());
        devicePropHistoryPageParam.setPropCodes(Arrays.asList(param.getAttribute()));
        Pagination<KeyValueWithTsDTO> keyValueWithTsDTOPagination = iotDevicePropClient.historyPropPage(devicePropHistoryPageParam);
        keyValueWithTsDTOPagination.getRecords().forEach(s -> {
            String deviceAttributeTranslated = getDeviceAttributeTranslated(param.getDeviceId(), param.getAttribute(), s.getValue()
                    .toString());
            s.setValue(deviceAttributeTranslated);
        });
        return keyValueWithTsDTOPagination;
    }

    public String getDeviceAttributeTranslated(Long deviceId, String prodCode, String prodValue) {
        List<DeviceAttributeRunningResponseVo> deviceAttributeRunning = this.getDeviceAttributeRunning(deviceId);
        Optional<DeviceAttributeRunningResponseVo> attributeRunningResponse = deviceAttributeRunning.stream()
                .filter(deviceAttributeRunningResponse -> prodCode.equals(deviceAttributeRunningResponse.getAttributeCode()))
                .findAny();
        if (attributeRunningResponse.isPresent()) {
            DeviceAttributeRunningResponseVo deviceAttributeRunningResponse = attributeRunningResponse.get();
            return DevicePropUtils.iotPropVal2Chinese(deviceAttributeRunningResponse.getDataType(),
                                                      prodValue,
                                                      deviceAttributeRunningResponse.getDataConstraint());
        }
        return StringUtils.EMPTY_STRING;
    }

    public List<DeviceAttributeRunningResponseVo> getDeviceAttributeRunning(Long deviceId) {
        DeviceDetailParam param = new DeviceDetailParam();
        param.setWithPropertySpec(true);
        param.setWithSnapShot(true);
        param.setDeviceId(deviceId.toString());
        DeviceDetailDTO detail = this.iotDeviceClient.detail(param);
        if (Objects.nonNull(detail)) {
            List<DeviceAttributeRunningResponseVo> deviceAttributeRunningResponses = detail.getProductPropVoList().stream()
                    .map(productPropDTO ->
                                 getDeviceAttributeRunningResponse(detail, productPropDTO)
                        ).collect(Collectors.toList());
            return deviceAttributeRunningResponses;
        }
        return Collections.emptyList();
    }

    public DeviceAttributeRunningResponseVo getDeviceAttributeRunningResponse(DeviceDetailDTO detail, ProductPropDTO productPropDTO) {
        Optional<KeyValueWithTsDTO> propertyValueOptional = detail.getSnapshots().stream()
                .filter(keyValueWithTsDTO -> productPropDTO.getPropCode().equals(keyValueWithTsDTO.getKey()))
                .findFirst();
        DeviceAttributeRunningResponseVo deviceAttributeRunningResponse = DeviceAttributeRunningResponseVo.builder()
                .attributeCode(productPropDTO.getPropCode())
                .attributeName(productPropDTO.getPropName())
                .permission(AttributePermissionEnum.valueOf(DevicePropUtils.convertReadWriteEnum(productPropDTO.getDataWrite()).name()))
                .dataType(productPropDTO.getDataType())
                .attributeUnitName(productPropDTO.getUnitName())
                .dataConstraint(productPropDTO.getDataConstraint())
                .build();
        propertyValueOptional.ifPresent(propertyValue -> {
            deviceAttributeRunningResponse.setOriginalValue(propertyValue.getValue());
            deviceAttributeRunningResponse.setTranslatedValue(DevicePropUtils.iotPropVal2Chinese(productPropDTO.getDataType(),
                                                                                                 propertyValue.getValue().toString(),
                                                                                                 productPropDTO.getDataConstraint()));
        });
        return deviceAttributeRunningResponse;
    }

    //比值数据
    private Map<String, Object> deviceDataRatio(Map<String, Object> map, DeviceHistoryParam param) {
        CommonTimeParam timeParam = getTimeParam(param.getTimeType(), param.getStartTime(), param.getEndTime());
        SecurityDevice coolingWater = securityCommonMapper.selectOne(Wrappers.<SecurityDevice>lambdaQuery()
                                                                             .eq(SecurityDevice::getDeviceId, param.getDeviceId())
                                                                             .eq(SecurityDevice::getApplicationCode, "COOLINGWATER"));
        //累计费用
        BigDecimal total = gearPositionMapper.getStatisticsData("total", timeParam.getTableName(), timeParam.getStartTime(), timeParam.getEndTime(), coolingWater.getCode(), new StatisticsParam());
        BigDecimal costRateQoq = BigDecimal.ZERO;
        BigDecimal costRateYoy = BigDecimal.ZERO;
        //运行时长
        BigDecimal duration = gearPositionMapper.getStatisticsData("duration", timeParam.getTableName(), timeParam.getStartTime(), timeParam.getEndTime(), coolingWater.getCode(), new StatisticsParam());
        BigDecimal durationRateQoq = BigDecimal.ZERO;
        BigDecimal durationRateYoy = BigDecimal.ZERO;
        //流量
        BigDecimal flow = gearPositionMapper.getStatisticsData("flow", timeParam.getTableName(), timeParam.getStartTime(), timeParam.getEndTime(), coolingWater.getCode(), new StatisticsParam());
        BigDecimal flowRateQoq = BigDecimal.ZERO;
        BigDecimal flowRateYoy = BigDecimal.ZERO;

        if (!param.getTimeType().equals("Year")) {
            //费用环比
            BigDecimal totalQoq = gearPositionMapper.getStatisticsData("total", timeParam.getTableName(), timeParam.getQoqStartTime(), timeParam.getQoqEndTime(), null, new StatisticsParam());
            costRateQoq = costRateQoq.add(getRate(totalQoq.toString(), total.toString()));
            //费用同比
            BigDecimal totalYoy = gearPositionMapper.getStatisticsData("total", timeParam.getTableName(), timeParam.getYoyStartTime(), timeParam.getYoyEndTime(), null, new StatisticsParam());
            costRateYoy = costRateYoy.add(getRate(totalYoy.toString(), total.toString()));
            //时长环比
            BigDecimal durationQoq = gearPositionMapper.getStatisticsData("duration", timeParam.getTableName(), timeParam.getQoqStartTime(), timeParam.getQoqEndTime(), null, new StatisticsParam());
            durationRateQoq = durationRateQoq.add(getRate(durationQoq.toString(), duration.toString()));
            //时长同比
            BigDecimal durationYoy = gearPositionMapper.getStatisticsData("duration", timeParam.getTableName(), timeParam.getYoyStartTime(), timeParam.getYoyEndTime(), null, new StatisticsParam());
            durationRateYoy = durationRateYoy.add(getRate(durationYoy.toString(), duration.toString()));
            //流量环比
            BigDecimal flowQoq = gearPositionMapper.getStatisticsData("flow", timeParam.getTableName(), timeParam.getQoqStartTime(), timeParam.getQoqEndTime(), null, new StatisticsParam());
            flowRateQoq = flowRateQoq.add(getRate(flowQoq.toString(), flow.toString()));
            //流量同比
            BigDecimal flowYoy = gearPositionMapper.getStatisticsData("flow", timeParam.getTableName(), timeParam.getYoyStartTime(), timeParam.getYoyEndTime(), null, new StatisticsParam());
            flowRateYoy = flowRateYoy.add(getRate(flowYoy.toString(), flow.toString()));

        }
        map.put("costTotal", total);
        map.put("costRateQoq", costRateQoq);
        map.put("costRateYoy", costRateYoy);
        map.put("duration", duration);
        map.put("durationRateQoq", durationRateQoq);
        map.put("durationRateYoy", durationRateYoy);
        map.put("flow", flow);
        map.put("flowRateQoq", flowRateQoq);
        map.put("flowRateYoy", flowRateYoy);
        return map;
    }

    //温度趋势图
    private Map<String, Object> temperatureTrend(Map<String, Object> map, DeviceHistoryParam param) {
        CommonTimeParam timeParam = getTimeParam(param.getTimeType(), param.getStartTime(), param.getEndTime());
        List<ColdWaterAttribute> coldWaterAttributes = coldWaterAttributeMapper.selectList(null);
        List<String> xtitle = new ArrayList<>();
        LocalDateTime startTime = DateUtils.parse(param.getStartTime());
        LocalDateTime endTime = DateUtils.parse(param.getEndTime());
        if (param.getTimeType().equals("TODAY")) {
            int between = (int) DateUtils.between(startTime, endTime, ChronoUnit.HOURS);
            if (between < 24) {
                for (int i = 0; i < between / 2; i++) {
                    LocalDateTime parse = DateUtils.offset(DateUtils.parse(param.getStartTime()), ChronoUnit.HOURS, i * 2);
                    xtitle.add(DateUtils.format(parse, "yyyy-MM-dd HH:00:00"));
                }
                for (ColdWaterAttribute coldWaterAttribute : coldWaterAttributes) {
                    IotDevicePropClient iotDevicePropClient = ApplicationContextHolder.getBean(IotDevicePropClient.class);
                    HistorySampleParam historySampleParam = new HistorySampleParam();
                    historySampleParam.setProperties(Arrays.asList(coldWaterAttribute.getAttributeCode()));
                    historySampleParam.setMaxRecords(2000);
                    historySampleParam.setAggType("avg");
                    historySampleParam.setStartTs(DateUtils.toTimestamp(DateUtils.parse(timeParam.getStartTime())));
                    historySampleParam.setEndTs(DateUtils.toTimestamp(DateUtils.parse(timeParam.getEndTime())));
                    DeviceTSDTO deviceTSDTO = iotDevicePropClient.historySampleProp(historySampleParam);
                    List<PropertyTsDTO> propertyTsList = deviceTSDTO.getPropertyTsBoList();
                    List<KeyValueWithTsDTO> valueTsBoList = propertyTsList.get(0).getValueTsBoList();
                    List<DevicePropWithTsDTO> devicePropWithTsDTOS = new ArrayList<>();
                    // 为避免有坑，新创建对象返回去
                    for (KeyValueWithTsDTO valueTs : valueTsBoList) {
                        Long ts = valueTs.getTs();

                        String s = DateUtils.format(DateUtils.parse(ts), "yyyy-MM-dd HH:00:00");
                        DevicePropWithTsDTO devicePropWithTsDTO = new DevicePropWithTsDTO();
                        devicePropWithTsDTO.setKey(valueTs.getKey());
                        devicePropWithTsDTO.setValue(valueTs.getValue());
                        devicePropWithTsDTO.setTs(s);
                        devicePropWithTsDTOS.add(devicePropWithTsDTO);
                    }

                    map.put(coldWaterAttribute.getAttributeCode(), devicePropWithTsDTOS);
                }
            } else {
                int betweenDay = (int) DateUtils.between(startTime, endTime, ChronoUnit.DAYS);
                for (int i = 0; i < betweenDay; i++) {
                    LocalDateTime parse = DateUtils.offset(DateUtils.parse(param.getStartTime()), ChronoUnit.DAYS, i);
                    xtitle.add(DateUtils.formatDate(parse, "yyyy-MM-dd"));
                }
                for (ColdWaterAttribute coldWaterAttribute : coldWaterAttributes) {
                    IotDevicePropClient iotDevicePropClient = ApplicationContextHolder.getBean(IotDevicePropClient.class);
                    HistorySampleParam historySampleParam = new HistorySampleParam();
                    historySampleParam.setProperties(Arrays.asList(coldWaterAttribute.getAttributeCode()));
                    historySampleParam.setMaxRecords(10000);
                    historySampleParam.setAggType("avg");
                    historySampleParam.setStep(1440);
                    historySampleParam.setStartTs(DateUtils.toTimestamp(DateUtils.parse(timeParam.getStartTime())));
                    historySampleParam.setEndTs(DateUtils.toTimestamp(DateUtils.parse(timeParam.getEndTime())));
                    DeviceTSDTO deviceTSDTO = iotDevicePropClient.historySampleProp(historySampleParam);
                    List<PropertyTsDTO> propertyTsBoList = deviceTSDTO.getPropertyTsBoList();
                    List<DevicePropWithTsDTO> devicePropWithTsDTOS = new ArrayList<>();
                    List<KeyValueWithTsDTO> valueTsBoList = propertyTsBoList.get(0).getValueTsBoList();
                    for (KeyValueWithTsDTO valueTs : valueTsBoList) {
                        Long ts = valueTs.getTs();
                        DevicePropWithTsDTO devicePropWithTsDTO = new DevicePropWithTsDTO();
                        devicePropWithTsDTO.setKey(valueTs.getKey());
                        devicePropWithTsDTO.setValue(valueTs.getValue());
                        String s = DateUtils.format(DateUtils.parse(ts), "yyyy-MM-dd");
                        devicePropWithTsDTO.setTs(s);
                        devicePropWithTsDTOS.add(devicePropWithTsDTO);
                    }
                    map.put(coldWaterAttribute.getAttributeCode(), devicePropWithTsDTOS);
                }
            }
        } else if (param.getTimeType().equals("Month")) {
            long timeSlot = timeParam.getTimeSlot();
            for (int i = 0; i < timeSlot; i++) {
                LocalDateTime parse = DateUtils.offset(DateUtils.parse(param.getStartTime()), ChronoUnit.MONTHS, i);
                xtitle.add(DateUtils.format(parse, "yyyy-MM"));
            }
            for (ColdWaterAttribute coldWaterAttribute : coldWaterAttributes) {

                IotDevicePropClient iotDevicePropClient = ApplicationContextHolder.getBean(IotDevicePropClient.class);
                HistorySampleParam historySampleParam = new HistorySampleParam();
                historySampleParam.setProperties(Arrays.asList(coldWaterAttribute.getAttributeCode()));
                historySampleParam.setMaxRecords(10000);
                historySampleParam.setStep(43200);
                historySampleParam.setAggType("avg");
                historySampleParam.setStartTs(DateUtils.toTimestamp(DateUtils.parse(timeParam.getStartTime())));
                historySampleParam.setEndTs(DateUtils.toTimestamp(DateUtils.parse(timeParam.getEndTime())));
                DeviceTSDTO deviceTSDTO = iotDevicePropClient.historySampleProp(historySampleParam);
                List<PropertyTsDTO> propertyTsBoList = deviceTSDTO.getPropertyTsBoList();
                List<KeyValueWithTsDTO> valueTsBoList = propertyTsBoList.get(0).getValueTsBoList();
                List<DevicePropWithTsDTO> devicePropWithTsDTOS = new ArrayList<>();
                for (KeyValueWithTsDTO valueTs : valueTsBoList) {
                    Long ts = valueTs.getTs();
                    String s = DateUtils.format(DateUtils.parse(ts), "yyyy-MM");
                    DevicePropWithTsDTO devicePropWithTsDTO = new DevicePropWithTsDTO();
                    devicePropWithTsDTO.setTs(s);
                    devicePropWithTsDTO.setKey(valueTs.getKey());
                    devicePropWithTsDTO.setValue(valueTs.getValue());
                    devicePropWithTsDTOS.add(devicePropWithTsDTO);
                }
                map.put(coldWaterAttribute.getAttributeCode(), devicePropWithTsDTOS);
            }
        } else {
            var localLocalDateTime = DateUtils.parse(param.getStartTime());
            var localEndLocalDateTime = DateUtils.parse(param.getEndTime());
            LocalDate localDate = localLocalDateTime.toLocalDate();
            LocalDate localEndDate = localEndLocalDateTime.toLocalDate();
            Integer timeSlot = (int) Period.between(localDate, localEndDate).toTotalMonths();
            for (int i = 0; i < timeSlot; i++) {
                LocalDateTime parse = DateUtils.offset(DateUtils.parse(param.getStartTime()), ChronoUnit.MONTHS, i);
                xtitle.add(DateUtils.format(parse, "yyyy-MM"));
            }
            for (ColdWaterAttribute coldWaterAttribute : coldWaterAttributes) {

                IotDevicePropClient iotDevicePropClient = ApplicationContextHolder.getBean(IotDevicePropClient.class);
                HistorySampleParam historySampleParam = new HistorySampleParam();
                historySampleParam.setProperties(Arrays.asList(coldWaterAttribute.getAttributeCode()));
                historySampleParam.setMaxRecords(10000);
                historySampleParam.setStep(43200);
                historySampleParam.setAggType("avg");
                historySampleParam.setStartTs(DateUtils.toTimestamp(DateUtils.parse(timeParam.getStartTime())));
                historySampleParam.setEndTs(DateUtils.toTimestamp(DateUtils.parse(timeParam.getEndTime())));
                DeviceTSDTO deviceTSDTO = iotDevicePropClient.historySampleProp(historySampleParam);
                List<PropertyTsDTO> propertyTsBoList = deviceTSDTO.getPropertyTsBoList();
                List<KeyValueWithTsDTO> valueTsBoList = propertyTsBoList.get(0).getValueTsBoList();
                List<DevicePropWithTsDTO> devicePropWithTsDTOS = new ArrayList<>();
                for (KeyValueWithTsDTO valueTs : valueTsBoList) {
                    Long ts = valueTs.getTs();
                    String s = DateUtils.format(DateUtils.parse(ts), "yyyy-MM");

                    DevicePropWithTsDTO keyValueWithTsDTO = new DevicePropWithTsDTO();
                    keyValueWithTsDTO.setTs(s);
                    keyValueWithTsDTO.setValue(valueTs.getValue());
                    keyValueWithTsDTO.setKey(valueTs.getKey());
                    devicePropWithTsDTOS.add(keyValueWithTsDTO);
                }
                map.put(coldWaterAttribute.getAttributeCode(), devicePropWithTsDTOS);
            }
        }
        return map;
    }

    //统计明细
    private Map<String, Object> getStatisticalDetails(Map<String, Object> map, DeviceHistoryParam param) {
        SecurityDevice coolingWater = securityCommonMapper.selectOne(Wrappers.<SecurityDevice>lambdaQuery()
                                                                             .eq(SecurityDevice::getDeviceId, param.getDeviceId())
                                                                             .eq(SecurityDevice::getApplicationCode, "COOLINGWATER"));
        if (param.getTimeType().equals("Year")) {
            String yyyy = DateUtils.format(DateUtils.parse(param.getStartTime()), "yyyy");
            LocalDateTime parse = DateUtils.parse(param.getStartTime());
            String st = DateUtils.format(DateUtils.beginOfYear(parse), "yyyy-MM");
            String end = DateUtils.format(DateUtils.endOfYear(parse), "yyyy-MM");
            Page<WaterDataDetailVo> reportDetailYear = waterDataYearMapper.getReportDetailYear(PagingUtils.convert(param), st, end, coolingWater.getCode());
            for (WaterDataDetailVo record : reportDetailYear.getRecords()) {
                record.setDataTime(yyyy);
            }
            Pagination<WaterDataDetailVo> convert = PagingUtils.convert(reportDetailYear);
            map.put("page", convert);
            return map;
        } else if (param.getTimeType().equals("Month")) {
            String st = DateUtils.format(DateUtils.parse(param.getStartTime()), "yyyy-MM");
            String end = DateUtils.format(DateUtils.parse(param.getEndTime()), "yyyy-MM");
            Page<WaterDataDetailVo> reportDetailMonth = waterDataYearMapper.getReportDetailYear(PagingUtils.convert(param), st, end, coolingWater.getCode());
            Pagination<WaterDataDetailVo> convert = PagingUtils.convert(reportDetailMonth);
            map.put("page", convert);
            return map;
        } else {
            String st = DateUtils.format(DateUtils.parse(param.getStartTime()), "yyyy-MM-dd");
            String end = DateUtils.format(DateUtils.parse(param.getEndTime()), "yyyy-MM-dd");
            Page<WaterDataDetailVo> reportDetailDay = waterDataDayMapper.getReportDetailDay(PagingUtils.convert(param), st, end, coolingWater.getCode());
            Pagination<WaterDataDetailVo> convert = PagingUtils.convert(reportDetailDay);
            map.put("page", convert);
            return map;
        }
    }

    private CommonTimeParam getTimeParam(String flag, String startTime, String endTime) {//环比时间
        CommonTimeParam commonTimeParam = new CommonTimeParam();
        //环比开始
        String qoqStartTime = "";
        //环比结束
        String qoqEndTime = "";
        //同比开始
        String yoyStartTime = "";
        //同比结束
        String yoyEndTime = "";
        //表名
        String tableName = "";

        //时期转换
        LocalDateTime st = DateUtils.parse(startTime);
        LocalDateTime et = DateUtils.parse(endTime);
        long timeSlot;
        if (flag.equals("Month")) {
            var localLocalDateTime = st;
            var localEndLocalDateTime = et;
            LocalDate localDate = localLocalDateTime.toLocalDate();
            LocalDate localEndDate = localEndLocalDateTime.toLocalDate();
            timeSlot = (int) Period.between(localDate, localEndDate).toTotalMonths();
            qoqStartTime = DateUtils.format(DateUtils.offset(DateUtils.parse(startTime), ChronoUnit.MONTHS, -1), "yyyy-MM");
            qoqEndTime = DateUtils.format(DateUtils.offset(DateUtils.parse(endTime), ChronoUnit.MONTHS, -1), "yyyy-MM");
            yoyStartTime = DateUtils.format(DateUtils.offset(DateUtils.parse(startTime), ChronoUnit.YEARS, -1), "yyyy-MM");
            yoyEndTime = DateUtils.format(DateUtils.offset(DateUtils.parse(endTime), ChronoUnit.YEARS, -1), "yyyy-MM");
            tableName = "water_data_month";
            startTime = DateUtils.format(st, "yyyy-MM");
            endTime = DateUtils.format(et, "yyyy-MM");
        } else if (flag.equals("TODAY")) {
            timeSlot = (int) DateUtils.between(DateUtils.parse(startTime), DateUtils.parseDate(endTime), ChronoUnit.DAYS);
            qoqStartTime = DateUtils.format(DateUtils.offset(DateUtils.parse(startTime), ChronoUnit.DAYS, -1), "yyyy-MM-dd");
            qoqEndTime = DateUtils.format(DateUtils.offset(DateUtils.parse(endTime), ChronoUnit.DAYS, -1), "yyyy-MM-dd");
            yoyStartTime = DateUtils.format(DateUtils.offset(DateUtils.parse(startTime), ChronoUnit.MONTHS, -1), "yyyy-MM-dd");
            yoyEndTime = DateUtils.format(DateUtils.offset(DateUtils.parse(endTime), ChronoUnit.MONTHS, -1), "yyyy-MM-dd");
            tableName = "water_data_day";
        } else {
            tableName = "water_data_year";
            timeSlot = DateUtils.between(DateUtils.parse(startTime), DateUtils.parse(endTime), ChronoUnit.YEARS);
        }
        commonTimeParam.setQoqEndTime(qoqEndTime);
        commonTimeParam.setQoqStartTime(qoqStartTime);
        commonTimeParam.setYoyEndTime(yoyEndTime);
        commonTimeParam.setYoyStartTime(yoyStartTime);
        commonTimeParam.setStartTime(startTime);
        commonTimeParam.setEndTime(endTime);
        commonTimeParam.setTimeSlot(timeSlot);
        commonTimeParam.setTableName(tableName);
        return commonTimeParam;
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

}
