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
 * 安防监控应用指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/8 14:01
 */
@Slf4j
@Service
public class SecurityMonitoringMetricService {
    private final AlarmMetricService alarmMetricService;
    private final DeviceMetricService deviceMetricService;

    public SecurityMonitoringMetricService(AlarmMetricService alarmMetricService, DeviceMetricService deviceMetricService) {
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
    @MetricTopic(topic = "SECURITY_MONITORING_DEVICE_ONLINE_RATE", description = "设备在线率指标")
    public DeviceOnlineRateMetricDTO getDeviceOnlineRateMetric(DeviceMetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return deviceMetricService.getDeviceOnlineRateMetric(param);
    }

    /**
     * 设备质保到期率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_DEVICE_EXPIRATION_RATE", description = "设备质保到期率指标")
    public DeviceExpireRateMetricDTO getDeviceExpireRateMetric(MetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return deviceMetricService.getDeviceExpireRateMetric(param);
    }

    /**
     * 设备健康度指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_DEVICE_HEALTH_LEVEL", description = "设备健康度指标")
    public DeviceHealthLevelMetricDTO getDeviceHealthLevelMetric(MetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return deviceMetricService.getDeviceHealthLevelMetric(param);
    }

    /**
     * 处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_ALARM_USER_HANDLER_RATE_RANK_METRIC", description = "总报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getAlarmUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getAlarmUserHandlerRateRankMetric(param);
    }

    /**
     * 一级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_ONE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "一级报警处理率指标排名")
    public AlarmUserHandlerRateTopMetricDTO getOneLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getOneLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 二级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_TWO_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "二级报警处理率指标排名")
    public AlarmUserHandlerRateTopMetricDTO getTwoLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getTwoLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 三级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_THREE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "三级报警处理率指标排名")
    public AlarmUserHandlerRateTopMetricDTO getThreeLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getThreeLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 一级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_ONE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getOneLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getOneLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 二级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_TWO_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 三级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_THREE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 明火烟雾报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_FLAME_SMOG_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "明火烟雾报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getFlameSmogAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getFlameSmogAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 违章占道报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_ILOR_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "违章占道报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getIlorAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getIlorAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 交通拥堵报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_TC_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "交通拥堵报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getTcAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getTCAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 违规占用消防通道报警处理效率指标
     */
    @MetricTopic(topic = "SECURITY_MONITORING_UPFL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "违规占用消防通道报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getUpflAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getUpflAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 明火烟雾报警处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "SECURITY_MONITORING_FLAME_SMOG_ALARM_HANDLER_RATE_RANK_METRIC", description = "明火烟雾报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getFlameSmogAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getFlameSmogAlarmHandlerRateRankMetric(param);
    }

    /**
     * 违章占道报警处理率指标
     */
    @MetricTopic(topic = "SECURITY_MONITORING_ILLEGAL_PARKING_ALARM_HANDLER_RATE_RANK_METRIC", description = "违章占道报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getIllegalParkingAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getIllegalParkingAlarmHandlerRateRankMetric(param);
    }

    /**
     * 交通拥堵报警处理率指标
     */
    @MetricTopic(topic = "SECURITY_MONITORING_TRAFFIC_JAM_ALARM_HANDLER_RATE_RANK_METRIC", description = "交通拥堵报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getTrafficJamAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getTrafficJamAlarmHandlerRateRankMetric(param);
    }

    /**
     * 违规占用消防通道
     */
    @MetricTopic(topic = "SECURITY_MONITORING_UPFL_ALARM_HANDLER_RATE_RANK_METRIC", description = "违规占用消防通道报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getUpflAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("SECURITY_MONITORING");
        return alarmMetricService.getUpflAlarmHandlerRateRankMetric(param);
    }
}
