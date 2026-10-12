package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.application;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.AlarmMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.DeviceMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.AlarmMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmHandlerTimeoutRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmUserHandlerRateTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.DeviceMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.DeviceExpireRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.DeviceHealthLevelMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto.DeviceOnlineRateMetricDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 动环监控指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/8 14:41
 * @Description:
 */
@Slf4j
@Service
public class RotatingManageMetricService {
    private final AlarmMetricService alarmMetricService;
    private final DeviceMetricService deviceMetricService;

    public RotatingManageMetricService(AlarmMetricService alarmMetricService, DeviceMetricService deviceMetricService) {
        this.alarmMetricService = alarmMetricService;
        this.deviceMetricService = deviceMetricService;
    }

    /**
     * 设备在线率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_DEVICE_ONLINE_RATE", description = "设备在线率指标")
    public DeviceOnlineRateMetricDTO getDeviceOnlineRateMetric(DeviceMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return deviceMetricService.getDeviceOnlineRateMetric(param);
    }

    /**
     * 设备质保到期率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_DEVICE_EXPIRATION_RATE", description = "设备质保到期率指标")
    public DeviceExpireRateMetricDTO getDeviceExpireRateMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return deviceMetricService.getDeviceExpireRateMetric(param);
    }

    /**
     * 设备健康度指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_DEVICE_HEALTH_LEVEL", description = "设备健康度指标")
    public DeviceHealthLevelMetricDTO getDeviceHealthLevelMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return deviceMetricService.getDeviceHealthLevelMetric(param);
    }

    /**
     * 处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_ALARM_USER_HANDLER_RATE_RANK_METRIC", description = "总报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getAlarmUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getAlarmUserHandlerRateRankMetric(param);
    }

    /**
     * 一级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_ONE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "一级报警处理率指标排名")
    public AlarmUserHandlerRateTopMetricDTO getOneLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getOneLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 二级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_TWO_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "二级报警处理率指标排名")
    public AlarmUserHandlerRateTopMetricDTO getTwoLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getTwoLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 三级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_THREE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "三级报警处理率指标排名")
    public AlarmUserHandlerRateTopMetricDTO getThreeLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getThreeLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 一级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_ONE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getOneLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getOneLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 二级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_TWO_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 三级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_THREE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * UPS报警处理率指标
     */
    @MetricTopic(topic = "ROTATINGMANAGE_UPS_ALARM_HANDLER_RATE_RANK_METRIC", description = "UPS报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getUpsAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getUpsAlarmHandlerRateRankMetric(param);
    }

    /**
     * 温度报警处理率指标
     */
    @MetricTopic(topic = "ROTATINGMANAGE_TEMPERATURE_ALARM_HANDLER_RATE_RANK_METRIC", description = "温度报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getTemperatureAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getTemperatureAlarmHandlerRateRankMetric(param);
    }

    /**
     * ups报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_UPS_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "ups报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getUpsAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getUPSAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 温度报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_TEMPERATURE_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "温度报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getTemperatureAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return alarmMetricService.getTemperatureAlarmHandlerTimeoutEfficiencyMetric(param);
    }
}
