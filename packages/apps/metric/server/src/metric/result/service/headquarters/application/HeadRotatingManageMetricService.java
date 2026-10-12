package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.application;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.AlarmMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.HeadquartersAlarmMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.HeadquartersDeviceMetricService;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadHandlerRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadHandlerTimeoutRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.device.HeadDeviceExpireRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.device.HeadDeviceHealthLevelMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.device.HeadOnlineRateMetricDTO;
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
public class HeadRotatingManageMetricService {
    private final HeadquartersAlarmMetricService headquartersAlarmMetricService;
    private final HeadquartersDeviceMetricService headquartersDeviceMetricService;

    public HeadRotatingManageMetricService(HeadquartersAlarmMetricService headquartersAlarmMetricService,
                                           HeadquartersDeviceMetricService headquartersDeviceMetricService
                                          ) {
        this.headquartersAlarmMetricService = headquartersAlarmMetricService;
        this.headquartersDeviceMetricService = headquartersDeviceMetricService;
    }

    /**
     * 设备在线率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_DEVICE_ONLINE_RATE", description = "设备在线率指标")
    public HeadOnlineRateMetricDTO getDeviceOnlineRateMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersDeviceMetricService.getDeviceOnlineRateMetric(param);
    }

    /**
     * 设备健康度指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_DEVICE_HEALTH_LEVEL", description = "总部设备健康度指标")
    public HeadDeviceHealthLevelMetricDTO getDeviceHealthLevelMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersDeviceMetricService.getDeviceHealthLevelMetric(param);
    }

    /**
     * 总部设备质保到期率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_DEVICE_EXPIRATION_RATE", description = "总部设备质保到期率指标")
    public HeadDeviceExpireRateMetricDTO getDeviceExpireRateMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersDeviceMetricService.getDeviceExpireRateMetric(param);
    }

    /**
     * 处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_ALARM_USER_HANDLER_RATE_RANK_METRIC", description = "总报警处理率指标")
    public HeadHandlerRateMetricDTO getAlarmUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getAlarmUserHandlerRateRankMetric(param);
    }

    /**
     * 一级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_ONE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "一级报警处理率指标排名")
    public HeadHandlerRateMetricDTO getOneLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getOneLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 二级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_TWO_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "二级报警处理率指标排名")
    public HeadHandlerRateMetricDTO getTwoLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getTwoLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 三级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_THREE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "三级报警处理率指标排名")
    public HeadHandlerRateMetricDTO getThreeLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getThreeLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 一级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_ONE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getOneLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getOneLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 二级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_TWO_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 三级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_THREE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * UPS报警处理率指标
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_UPS_ALARM_HANDLER_RATE_RANK_METRIC", description = "UPS报警处理率指标")
    public HeadHandlerRateMetricDTO getUpsAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getUpsAlarmHandlerRateRankMetric(param);
    }

    /**
     * 温度报警处理率指标
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_TEMPERATURE_ALARM_HANDLER_RATE_RANK_METRIC", description = "温度报警处理率指标")
    public HeadHandlerRateMetricDTO getTemperatureAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getTemperatureAlarmHandlerRateRankMetric(param);
    }

    /**
     * ups报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_UPS_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "ups报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getUPSLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getUPSLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 温度报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ROTATINGMANAGE_HEADQUARTERS_TEMPERATURE_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "温度报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getTemperatureLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ROTATINGMANAGE");
        return headquartersAlarmMetricService.getTemperatureLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }
}
