package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.dao.EnvironmentHourStatMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.entity.EnvironmentHourStat;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.enumeration.EnvironmentMonitorTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.enumeration.EnvironmentalAirQualityEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.param.EnvironmentHourParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.ContrastResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.HourStatAvgResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.PieGraphResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.PmTwoPointFiveStatResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.PmTwoPointFiveTopResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.QoqStatResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.TrendResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.service.EnvironmentHourStatService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.utils.TrendTimeUtils;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 环境监测小时服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnvironmentHourStatServiceImpl extends ServiceImpl<EnvironmentHourStatMapper, EnvironmentHourStat> implements EnvironmentHourStatService {

    private final EnvironmentHourStatMapper environmentHourStatMapper;
    private final IotDevicePropClient iotDevicePropClient;
    private final SecurityCommonMapper securityCommonMapper;

    private static final String APPLICATION_CODE = "ENVIRONMENT_MONITORI";

    /**
     * 采集入库
     */
    @Override
    public void gatherInto(LocalDateTime nowTime) {
        List<SecurityDevice> deviceList = securityCommonMapper.selectList(Wrappers.<SecurityDevice>lambdaQuery()
                                                                                  .select(SecurityDevice::getDeviceId)
                                                                                  .eq(SecurityDevice::getApplicationCode, APPLICATION_CODE)
                                                                         );
        if (CollectionUtils.isEmpty(deviceList)) {
            return;
        }
        List<Long> deviceIdList = deviceList.stream().map(SecurityDevice::getDeviceId).filter(Objects::nonNull).distinct()
                .collect(Collectors.toList());
        // 查询当前页设备属性的入参
        MultiDevicePropParam propParam = new MultiDevicePropParam();

        StringBuilder stringBuilder = new StringBuilder();
        deviceIdList.forEach(deviceId -> stringBuilder.append(deviceId).append(".")
                .append(EnvironmentMonitorTypeEnum.PM_TWO_POINT_FIVE.name()).append(","));
        String devIdAndPropCodes = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();

        propParam.setDevIdAndPropCodes(devIdAndPropCodes);
        Map<String, List<KeyValueWithTsDTO>> multiedPropMap = iotDevicePropClient.multiProp(propParam);
        if (multiedPropMap == null || multiedPropMap.isEmpty()) {
            return;
        }

        BigDecimal num = BigDecimal.ZERO;
        BigDecimal sumValue = BigDecimal.ZERO;
        for (SecurityDevice securityDevice : deviceList) {
            // 设备的属性集合
            List<KeyValueWithTsDTO> list = multiedPropMap.get(String.valueOf(securityDevice.getDeviceId()));
            if (!CollectionUtils.isEmpty(list)) {
                Map<String, String> map = list.stream()
                        .collect(Collectors.toMap(KeyValueWithTsDTO::getKey, v -> String.valueOf(v.getValue())));
                // 获取pm2.5
                String pmTwoPointFive = map.get(EnvironmentMonitorTypeEnum.PM_TWO_POINT_FIVE.name());
                if (StringUtils.isNotEmpty(pmTwoPointFive)) {
                    sumValue = sumValue.add(new BigDecimal(pmTwoPointFive));
                    num = num.add(BigDecimal.ONE);
                }
            }
        }

        BigDecimal agv = num.compareTo(BigDecimal.ZERO) > 0 ? sumValue.divide(num, RoundingMode.CEILING) : num;
        EnvironmentHourStat stat = new EnvironmentHourStat();
        stat.setStatDay(DateUtils.formatDate(nowTime));
        stat.setStatTime(nowTime);
        stat.setId(IdWorker.getId());
        stat.setPmTwoPointFive(agv.toString());

        this.save(stat);
    }

    /**
     * PM2.5实时统计
     *
     * @return 统计
     */
    @Override
    public PmTwoPointFiveStatResult realtimePmTwoPointFiveStat() {
        long startTimeLong = System.currentTimeMillis();
        PmTwoPointFiveStatResult result = new PmTwoPointFiveStatResult();

        List<SecurityDevice> deviceList = securityCommonMapper.selectList(Wrappers.<SecurityDevice>lambdaQuery()
                                                                                  .select(SecurityDevice::getDeviceId, SecurityDevice::getDeviceName)
                                                                                  .eq(SecurityDevice::getApplicationCode, APPLICATION_CODE)
                                                                         );
        if (CollectionUtils.isEmpty(deviceList)) {
            return result;
        }
        // 查询当前页设备属性的入参
        MultiDevicePropParam propParam = new MultiDevicePropParam();
        StringBuilder stringBuilder = new StringBuilder();
        deviceList.forEach(device -> stringBuilder.append(device.getDeviceId()).append(","));
        String devIdAndPropCodes = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        propParam.setDevIdAndPropCodes(devIdAndPropCodes);
        Map<String, List<KeyValueWithTsDTO>> multiedPropMap = iotDevicePropClient.multiProp(propParam);
        if (multiedPropMap == null || multiedPropMap.isEmpty()) {
            return result;
        }
        List<PmTwoPointFiveTopResult> pm25List = new ArrayList<>();
        log.warn("iot接口耗时：" + (System.currentTimeMillis() - startTimeLong));
        for (SecurityDevice securityDevice : deviceList) {
            List<KeyValueWithTsDTO> list = multiedPropMap.get(String.valueOf(securityDevice.getDeviceId()));
            if (!CollectionUtils.isEmpty(list)) {
                Map<String, String> map = list.stream()
                        .filter(dto -> dto != null && StringUtils.isNotBlank(dto.getKey()) && dto.getValue() != null)
                        .collect(Collectors.toMap(KeyValueWithTsDTO::getKey, v -> String.valueOf(v.getValue())));
                String pm25 = map.get(EnvironmentMonitorTypeEnum.PM_TWO_POINT_FIVE.name());
                if (StringUtils.isNotBlank(pm25)) {
                    PmTwoPointFiveTopResult topTemp = new PmTwoPointFiveTopResult();
                    topTemp.setDeviceId(securityDevice.getDeviceId());
                    topTemp.setDeviceName(securityDevice.getDeviceName());
                    topTemp.setValue(pm25);
                    pm25List.add(topTemp);
                }
            }
        }
        if (CollectionUtils.isEmpty(pm25List)) {
            return result;
        }
        pm25List = pm25List.parallelStream().sorted(
                Comparator.comparing(PmTwoPointFiveTopResult::getValue).reversed()
                                                   ).collect(Collectors.toList());

        int size = pm25List.size();
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.ZERO;
        List<PmTwoPointFiveTopResult> resultList = new ArrayList<>();
        log.warn("第一次遍历：" + (System.currentTimeMillis() - startTimeLong));
        for (int i = 0; i < size; i++) {
            PmTwoPointFiveTopResult top = pm25List.get(i);
            if (i < 10) {
                resultList.add(top);
            }
            BigDecimal bigDecimal = new BigDecimal(top.getValue());
            // 获取最大值
            if (max.compareTo(bigDecimal) < 0) {
                max = bigDecimal;
            }
            // 计算总值
            total = total.add(bigDecimal);
        }
        String avg = total.divide(new BigDecimal(size), 2, RoundingMode.HALF_UP).toString();
        String quality = EnvironmentalAirQualityEnum.getAirQualityDescribe(avg);
        result.setList(resultList);
        result.setMax(max.setScale(2, RoundingMode.HALF_UP).toString());
        result.setAvg(avg);
        result.setQuality(StringUtils.isNotBlank(quality) ? quality : EnvironmentalAirQualityEnum.EXCELLENT.getDescribe());
        log.warn("第二次遍历：" + (System.currentTimeMillis() - startTimeLong));
        return result;
    }

    /**
     * PM2.5平均值统计
     *
     * @param param 条件入参
     *
     * @return PM2.5平均值统计
     */
    @Override
    public HourStatAvgResult hourStatAvg(EnvironmentHourParam param) {
        TrendTimeUtils.setFormatAndPeriod(param);
        HourStatAvgResult result = environmentHourStatMapper.hourStatAvg(param);
        if (result == null) {
            return new HourStatAvgResult();
        }
        return result;
    }

    /**
     * 环境监测趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @Override
    public List<TrendResult> getAirQualityTrend(EnvironmentHourParam param) {
        TrendTimeUtils.setFormatAndPeriod(param);
        return environmentHourStatMapper.selectTrend(param.getFormat(), param.getPeriod());
    }

    /**
     * 质量统计饼图
     *
     * @param param 条件入参
     *
     * @return 统计
     */
    @Override
    public List<PieGraphResult> getPieGraph(EnvironmentHourParam param) {
        TrendTimeUtils.setFormatAndPeriod(param);
        List<TrendResult> list = environmentHourStatMapper.selectTrend(param.getFormat(), param.getPeriod());
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<PieGraphResult> resultList = new ArrayList<>();
        list.stream().map(trend -> {
            TrendResult trendResult = new TrendResult();
            trendResult.setTrendX(trend.getTrendX());
            trendResult.setTrendY(EnvironmentalAirQualityEnum.getAirQualityDescribe(trend.getTrendY()));
            return trendResult;
        }).collect(Collectors.groupingBy(TrendResult::getTrendY)).forEach((k, v) -> {
            PieGraphResult result = new PieGraphResult();
            result.setDescribe(k);
            result.setNumber(v.size());
            resultList.add(result);
        });

        return resultList;
    }

    /**
     * 统计比率
     *
     * @param period 时间段
     * @param type   比率类型(DateField枚举对应的int值，天是DAY_OF_MONTH即5)
     *
     * @return 比率
     */
    @Override
    public ContrastResult getHourStatRatio(Period period, ChronoUnit type) {
        ContrastResult result = new ContrastResult();
        result.setCurrentValue(BigDecimal.ZERO.toString());
        result.setCompareValue(BigDecimal.ZERO.toString());
        result.setRatioValue(BigDecimal.ZERO.setScale(BigDecimal.ROUND_CEILING, RoundingMode.HALF_UP).toString());
        if (type == null || period == null || period.getBegin() == null || period.getEnd() == null) {
            return result;
        }
        EnvironmentHourParam currentParam = new EnvironmentHourParam();
        currentParam.setPeriod(period);
        HourStatAvgResult currentStat = environmentHourStatMapper.hourStatAvg(currentParam);
        if (currentStat != null && StringUtils.isNotBlank(currentStat.getPmTwoPointFile())) {
            result.setCurrentValue(currentStat.getPmTwoPointFile());
        }

        var startTime = DateUtils.offset(period.getBegin(), type, -BigDecimal.ONE.intValue());
        var endTime = DateUtils.offset(period.getEnd(), type, -BigDecimal.ONE.intValue());
        EnvironmentHourParam compareParam = new EnvironmentHourParam();
        compareParam.setPeriod(new Period(startTime, endTime));
        HourStatAvgResult compareStat = environmentHourStatMapper.hourStatAvg(compareParam);
        if (compareStat != null && StringUtils.isNotBlank(compareStat.getPmTwoPointFile())) {
            result.setCompareValue(compareStat.getPmTwoPointFile());
            BigDecimal current = new BigDecimal(result.getCurrentValue());
            BigDecimal compare = new BigDecimal(result.getCompareValue());
            result.setRatioValue(current.subtract(compare).divide(compare, BigDecimal.ROUND_CEILING, RoundingMode.HALF_UP).toString());
        }
        return result;
    }

    /**
     * pm2.5小时统计环比(日环比+周环比)
     * 游仙区小微产业驾驶舱定制接口
     *
     * @return 环比
     */
    @Override
    public QoqStatResult dayAndWeekRatio() {
        QoqStatResult result = new QoqStatResult();
        var nowTime = LocalDateTime.now();
        ContrastResult dayContrast = this.getHourStatRatio(new Period(DateUtils.beginOfDay(nowTime), DateUtils.endOfDay(nowTime)), ChronoUnit.DAYS);
        ContrastResult weekContrast = this.getHourStatRatio(new Period(DateUtils.beginOfWeek(nowTime), DateUtils.endOfWeek(nowTime)), ChronoUnit.WEEKS);
        result.setDayContrast(dayContrast);
        result.setWeekContrast(weekContrast);
        return result;
    }
}
