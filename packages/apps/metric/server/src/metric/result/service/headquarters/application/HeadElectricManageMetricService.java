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
 * 电气管理指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/8 14:39
 * @Description:
 */
@Slf4j
@Service
public class HeadElectricManageMetricService {
    private final HeadquartersAlarmMetricService headquartersAlarmMetricService;
    private final HeadquartersDeviceMetricService headquartersDeviceMetricService;

    public HeadElectricManageMetricService(HeadquartersAlarmMetricService headquartersAlarmMetricService,
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
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_DEVICE_ONLINE_RATE", description = "设备在线率指标")
    public HeadOnlineRateMetricDTO getDeviceOnlineRateMetric(MetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersDeviceMetricService.getDeviceOnlineRateMetric(param);
    }

    /**
     * 设备健康度指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_DEVICE_HEALTH_LEVEL", description = "总部设备健康度指标")
    public HeadDeviceHealthLevelMetricDTO getDeviceHealthLevelMetric(MetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersDeviceMetricService.getDeviceHealthLevelMetric(param);
    }

    /**
     * 总部设备质保到期率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_DEVICE_EXPIRATION_RATE", description = "总部设备质保到期率指标")
    public HeadDeviceExpireRateMetricDTO getDeviceExpireRateMetric(MetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersDeviceMetricService.getDeviceExpireRateMetric(param);
    }

    /**
     * 处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_ALARM_USER_HANDLER_RATE_RANK_METRIC", description = "总报警处理率指标")
    public HeadHandlerRateMetricDTO getAlarmUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getAlarmUserHandlerRateRankMetric(param);
    }

    /**
     * 一级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_ONE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "一级报警处理率指标排名")
    public HeadHandlerRateMetricDTO getOneLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getOneLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 二级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_TWO_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "二级报警处理率指标排名")
    public HeadHandlerRateMetricDTO getTwoLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getTwoLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 三级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_THREE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "三级报警处理率指标排名")
    public HeadHandlerRateMetricDTO getThreeLevelUserHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getThreeLevelUserHandlerRateRankMetric(param);
    }

    /**
     * 处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 一级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_ONE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getOneLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getOneLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 二级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_TWO_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 三级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_THREE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 电压报警处理率指标
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_VAL_ALARM_HANDLER_RATE_RANK_METRIC", description = "电压报警报警处理率指标")
    public HeadHandlerRateMetricDTO getValAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getValAlarmHandlerRateRankMetric(param);
    }

    /**
     * 漏电报警处理率指标
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_LCA_ALARM_HANDLER_RATE_RANK_METRIC", description = "漏电报警报警处理率指标")
    public HeadHandlerRateMetricDTO getLcaAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getLcaAlarmHandlerRateRankMetric(param);
    }

    /**
     * 温度报警处理率指标
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_TEMPERATURE_ALARM_HANDLER_RATE_RANK_METRIC", description = "温度报警处理率指标")
    public HeadHandlerRateMetricDTO getTemperatureAlarmHandlerRateRankMetric(MetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getTemperatureAlarmHandlerRateRankMetric(param);
    }

    /**
     * 电压报警处理效率指标
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_VAL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "电压报警报警处理率指标")
    public HeadHandlerTimeoutRateMetricDTO getValAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getValAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 漏电报警处理效率指标
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_LCA_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "漏电报警报警处理率指标")
    public HeadHandlerTimeoutRateMetricDTO getLcaAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getLcaAlarmHandlerTimeoutEfficiencyMetric(param);
    }

    /**
     * 温度报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRICMANAGE_HEADQUARTERS_TEMPERATURE_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "温度报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getTemperatureLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        param.setAppCode("ELECTRICMANAGE");
        return headquartersAlarmMetricService.getTemperatureLevelAlarmHandlerTimeoutEfficiencyMetric(param);
    }

}
