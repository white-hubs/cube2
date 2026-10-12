package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm;

import com.guhecloud.cube.base.alarm.client.AlarmBimStatisticsClient;
import com.guhecloud.cube.base.alarm.client.AlarmStatisticsClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmBasicAnalysisStatisticsDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerRateTopVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.AlarmMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.FlAME_SMOG_EVENT_CODES;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.ILOR_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.LCA_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.MCP_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.ONE_LEVEL_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.TC_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.TEMPERATURE_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.THREE_LEVEL_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.TWO_LEVEL_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.UPFL_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.UPS_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.VAL_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.WP_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.WSD_EVENT_CODE;

/**
 * 报警基础指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/15 15:37
 * @Description:
 */
@Slf4j
@Service
public class AlarmBaseMetricService {

    private final AlarmStatisticsClient alarmStatisticsClient;

    private final AlarmBimStatisticsClient alarmBimStatisticsClient;

    public AlarmBaseMetricService(AlarmStatisticsClient alarmStatisticsClient, AlarmBimStatisticsClient alarmBimStatisticsClient) {
        this.alarmStatisticsClient = alarmStatisticsClient;
        this.alarmBimStatisticsClient = alarmBimStatisticsClient;
    }

    /**
     * 报警总数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "BASE_ALARM_NUMBER", description = "报警总数指标")
    public Long getAlarmNumberMetric(AlarmMetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, null));

    }

    /**
     * 报警处理数指标
     */
    @MetricTopic(topic = "BASE_ALARM_HANDLE_NUMBER", description = "报警处理数指标")
    public Long getAlarmHandleNumberMetric(AlarmMetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, null));
    }

    /**
     * 报警处理率指标
     */
    @MetricTopic(topic = "BASE_ALARM_HANDLE_RATE", description = "报警处理率指标")
    public Double getAlarmHandleRateMetric(AlarmMetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, null));

    }

    /**
     * 用户报警处理率排名前五位
     */
    @MetricTopic(topic = "BASE_ALARM_HANDLE_RATE_RANK", description = "用户报警处理率排名前五位")
    public List<HandlerRateDTO> getAlarmHandleRankMetric(AlarmMetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, null));
    }

    /**
     * 用户报警处理率排名后五位
     */
    @MetricTopic(topic = "BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "用户报警处理率排名后五位")
    public List<HandlerRateDTO> getAlarmHandleLastRankMetric(AlarmMetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, null));
    }

    /**
     * 一级报警总数指标
     */
    @MetricTopic(topic = "ONE_LEVEL_BASE_ALARM_NUMBER", description = "一级报警总数指标")
    public Long getOneLevelAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警处理数指标
     */
    @MetricTopic(topic = "ONE_LEVEL_BASE_ALARM_HANDLE_NUMBER", description = "一级报警处理数指标")
    public Long getOneLevelAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警处理率指标
     */
    @MetricTopic(topic = "ONE_LEVEL_BASE_ALARM_HANDLE_RATE", description = "一级报警处理率指标")
    public Double getOneLevelAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警用户处理率排名前五位
     */
    @MetricTopic(topic = "ONE_LEVEL_BASE_ALARM_HANDLE_RATE_RANK", description = "一级报警用户处理率排名前五位")
    public List<HandlerRateDTO> getOneLevelAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));

    }

    /**
     * 一级报警用户处理率排名后五位
     */
    @MetricTopic(topic = "ONE_LEVEL_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "一级报警用户处理率排名后五位")
    public List<HandlerRateDTO> getOneLevelAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 二级报警总数指标
     */
    @MetricTopic(topic = "TWO_LEVEL_BASE_ALARM_NUMBER", description = "二级报警总数指标")
    public Long getTwoLevelAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警处理数指标
     */
    @MetricTopic(topic = "TWO_LEVEL_BASE_ALARM_HANDLE_NUMBER", description = "二级报警处理数指标")
    public Long getTwoLevelAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警处理率指标
     */
    @MetricTopic(topic = "TWO_LEVEL_BASE_ALARM_HANDLE_RATE", description = "二级报警处理率指标")
    public Double getTwoLevelAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警用户处理率排名前五位
     */
    @MetricTopic(topic = "TWO_LEVEL_BASE_ALARM_HANDLE_RATE_RANK", description = "二级报警用户处理率排名前五位")
    public List<HandlerRateDTO> getTwoLevelAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警用户处理率排名后五位
     */
    @MetricTopic(topic = "TWO_LEVEL_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "二级报警用户处理率排名后五位")
    public List<HandlerRateDTO> getTwoLevelAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 三级报警总数指标
     */
    @MetricTopic(topic = "THREE_LEVEL_BASE_ALARM_NUMBER", description = "三级报警总数指标")
    public Long getThreeLevelAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警处理数指标
     */
    @MetricTopic(topic = "THREE_LEVEL_BASE_ALARM_HANDLE_NUMBER", description = "三级报警处理数指标")
    public Long getThreeLevelAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警处理率指标
     */
    @MetricTopic(topic = "THREE_LEVEL_BASE_ALARM_HANDLE_RATE", description = "三级报警处理率指标")
    public Double getThreeLevelAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警用户处理率排名前五位
     */
    @MetricTopic(topic = "THREE_LEVEL_BASE_ALARM_HANDLE_RATE_RANK", description = "三级报警用户处理率排名前五位")
    public List<HandlerRateDTO> getThreeLevelAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警用户处理率排名后五位
     */

    @MetricTopic(topic = "THREE_LEVEL_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "三级报警用户处理率排名后五位")
    public List<HandlerRateDTO> getThreeLevelAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 明火烟雾报警总数指标
     */
    @MetricTopic(topic = "FLAME_SMOG_BASE_ALARM_NUMBER", description = "明火烟雾报警总数指标")
    public Long getFlameSmogAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警处理数指标
     */
    @MetricTopic(topic = "FLAME_SMOG_BASE_ALARM_HANDLE_NUMBER", description = "明火烟雾报警处理数指标")
    public Long getFlameSmogAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警处理率指标
     */
    @MetricTopic(topic = "FLAME_SMOG_BASE_ALARM_HANDLE_RATE", description = "明火烟雾报警处理率指标")
    public Double getFlameSmogAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警用户处理率排名前五位
     */
    @MetricTopic(topic = "FLAME_SMOG_BASE_ALARM_HANDLE_RATE_RANK", description = "明火烟雾报警用户处理率排名前五位")
    public List<HandlerRateDTO> getFlameSmogAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警用户处理率排名后五位
     */

    @MetricTopic(topic = "FLAME_SMOG_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "明火烟雾报警用户处理率排名后五位")
    public List<HandlerRateDTO> getFlameSmogAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 违章占道报警总数指标
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_BASE_ALARM_NUMBER", description = "违章占道报警总数指标")
    public Long getIllegalParkingAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警处理数指标
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_BASE_ALARM_HANDLE_NUMBER", description = "违章占道报警处理数指标")
    public Long getIllegalParkingAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警处理率指标
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_BASE_ALARM_HANDLE_RATE", description = "违章占道报警处理率指标")
    public Double getIllegalParkingAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警用户处理率排名前五位
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_BASE_ALARM_HANDLE_RATE_RANK", description = "违章占道报警用户处理率排名前五位")
    public List<HandlerRateDTO> getIllegalParkingAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警用户处理率排名后五位
     */

    @MetricTopic(topic = "ILLEGAL_PARKING_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "违章占道报警用户处理率排名后五位")
    public List<HandlerRateDTO> getIllegalParkingAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警总数指标
     */
    @MetricTopic(topic = "TRAFFIC_JAM_BASE_ALARM_NUMBER", description = "交通拥堵报警总数指标")
    public Long getTrafficJamAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理数指标
     */
    @MetricTopic(topic = "TRAFFIC_JAM_BASE_ALARM_HANDLE_NUMBER", description = "交通拥堵报警处理数指标")
    public Long getTrafficJamAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理率指标
     */
    @MetricTopic(topic = "TRAFFIC_JAM_BASE_ALARM_HANDLE_RATE", description = "交通拥堵报警处理率指标")
    public Double getTrafficJamAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警用户处理率排名前五位
     */
    @MetricTopic(topic = "TRAFFIC_JAM_BASE_ALARM_HANDLE_RATE_RANK", description = "交通拥堵报警用户处理率排名前五位")
    public List<HandlerRateDTO> getTrafficJamAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警用户处理率排名后五位
     */

    @MetricTopic(topic = "TRAFFIC_JAM_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "交通拥堵报警用户处理率排名后五位")
    public List<HandlerRateDTO> getTrafficJamAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 水压报警总数指标
     */
    @MetricTopic(topic = "WATER_PRESSURE_BASE_ALARM_NUMBER", description = "水压报警总数指标")
    public Long getWaterPressureAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警处理数指标
     */
    @MetricTopic(topic = "WATER_PRESSURE_BASE_ALARM_HANDLE_NUMBER", description = "水压报警处理数指标")
    public Long getWaterPressureAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警处理率指标
     */
    @MetricTopic(topic = "WATER_PRESSURE_BASE_ALARM_HANDLE_RATE", description = "水压报警处理率指标")
    public Double getWaterPressureAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警用户处理率排名前五位
     */
    @MetricTopic(topic = "WATER_PRESSURE_BASE_ALARM_HANDLE_RATE_RANK", description = "水压报警用户处理率排名前五位")
    public List<HandlerRateDTO> getWaterPressureAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警用户处理率排名后五位
     */

    @MetricTopic(topic = "WATER_PRESSURE_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "水压报警用户处理率排名后五位")
    public List<HandlerRateDTO> getWaterPressureAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 手报报警总数指标
     */
    @MetricTopic(topic = "MCP_BASE_ALARM_NUMBER", description = "手报报警总数指标")
    public Long getMcpAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警处理数指标
     */
    @MetricTopic(topic = "MCP_BASE_ALARM_HANDLE_NUMBER", description = "手报报警处理数指标")
    public Long getMcpAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警处理率指标
     */
    @MetricTopic(topic = "MCP_BASE_ALARM_HANDLE_RATE", description = "手报报警处理率指标")
    public Double getMcpAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警用户处理率排名前五位
     */
    @MetricTopic(topic = "MCP_BASE_ALARM_HANDLE_RATE_RANK", description = "手报报警用户处理率排名前五位")
    public List<HandlerRateDTO> getMcpAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警用户处理率排名后五位
     */

    @MetricTopic(topic = "MCP_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "手报报警用户处理率排名后五位")
    public List<HandlerRateDTO> getMcpAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * UPS总数指标
     */
    @MetricTopic(topic = "UPS_BASE_ALARM_NUMBER", description = "UPS总数指标")
    public Long getUPSAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警处理数指标
     */
    @MetricTopic(topic = "UPS_BASE_ALARM_HANDLE_NUMBER", description = "UPS报警处理数指标")
    public Long getUPSAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警处理率指标
     */
    @MetricTopic(topic = "UPS_BASE_ALARM_HANDLE_RATE", description = "UPS报警处理率指标")
    public Double getUPSAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警用户处理率排名前五位
     */
    @MetricTopic(topic = "UPS_BASE_ALARM_HANDLE_RATE_RANK", description = "UPS报警用户处理率排名前五位")
    public List<HandlerRateDTO> getUPSAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警用户处理率排名后五位
     */

    @MetricTopic(topic = "UPS_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "UPS报警用户处理率排名后五位")
    public List<HandlerRateDTO> getUPSAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * 温度报警总数指标
     */
    @MetricTopic(topic = "TEMPERATURE_BASE_ALARM_NUMBER", description = "温度报警总数指标")
    public Long getTemperatureAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警处理数指标
     */
    @MetricTopic(topic = "TEMPERATURE_BASE_ALARM_HANDLE_NUMBER", description = "温度报警处理数指标")
    public Long getTemperatureAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警处理率指标
     */
    @MetricTopic(topic = "TEMPERATURE_BASE_ALARM_HANDLE_RATE", description = "温度报警处理率指标")
    public Double getTemperatureAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警用户处理率排名前五位
     */
    @MetricTopic(topic = "TEMPERATURE_BASE_ALARM_HANDLE_RATE_RANK", description = "温度报警用户处理率排名前五位")
    public List<HandlerRateDTO> getTemperatureAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警用户处理率排名后五位
     */

    @MetricTopic(topic = "TEMPERATURE_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "温度报警用户处理率排名后五位")
    public List<HandlerRateDTO> getTemperatureAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警总数指标
     */
    @MetricTopic(topic = "UPFL_BASE_ALARM_NUMBER", description = "违规占用消防通道报警总数指标")
    public Long getUPFLAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理数指标
     */
    @MetricTopic(topic = "UPFL_BASE_ALARM_HANDLE_NUMBER", description = "违规占用消防通道报警处理数指标")
    public Long getUPFLAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理率指标
     */
    @MetricTopic(topic = "UPFL_BASE_ALARM_HANDLE_RATE", description = "违规占用消防通道报警处理率指标")
    public Double getUPFLAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警用户处理率排名前五位
     */
    @MetricTopic(topic = "UPFL_BASE_ALARM_HANDLE_RATE_RANK", description = "违规占用消防通道报警用户处理率排名前五位")
    public List<HandlerRateDTO> getUPFLAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警用户处理率排名后五位
     */

    @MetricTopic(topic = "UPFL_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "违规占用消防通道报警用户处理率排名后五位")
    public List<HandlerRateDTO> getUPFLAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 温湿度报警总数指标
     */
    @MetricTopic(topic = "WSD_BASE_ALARM_NUMBER", description = "温湿度报警总数指标")
    public Long getWSDAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警处理数指标
     */
    @MetricTopic(topic = "WSD_BASE_ALARM_HANDLE_NUMBER", description = "温湿度报警处理数指标")
    public Long getWSDAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警处理率指标
     */
    @MetricTopic(topic = "WSD_BASE_ALARM_HANDLE_RATE", description = "温湿度报警处理率指标")
    public Double getWSDAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警用户处理率排名前五位
     */
    @MetricTopic(topic = "WSD_BASE_ALARM_HANDLE_RATE_RANK", description = "温湿度报警用户处理率排名前五位")
    public List<HandlerRateDTO> getWSDAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警用户处理率排名后五位
     */

    @MetricTopic(topic = "WSD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "温湿度报警用户处理率排名后五位")
    public List<HandlerRateDTO> getWSDAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 电压报警总数指标
     */
    @MetricTopic(topic = "VAL_BASE_ALARM_NUMBER", description = "电压报警总数指标")
    public Long getVALAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警处理数指标
     */
    @MetricTopic(topic = "VAL_BASE_ALARM_HANDLE_NUMBER", description = "电压报警处理数指标")
    public Long getVALAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警处理率指标
     */
    @MetricTopic(topic = "VAL_BASE_ALARM_HANDLE_RATE", description = "电压报警处理率指标")
    public Double getVALAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警用户处理率排名前五位
     */
    @MetricTopic(topic = "VAL_BASE_ALARM_HANDLE_RATE_RANK", description = "电压报警用户处理率排名前五位")
    public List<HandlerRateDTO> getVALAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警用户处理率排名后五位
     */

    @MetricTopic(topic = "VAL_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "电压报警用户处理率排名后五位")
    public List<HandlerRateDTO> getVALAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 漏电报警总数指标
     */
    @MetricTopic(topic = "LCA_BASE_ALARM_NUMBER", description = "漏电报警总数指标")
    public Long getLCAAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警处理数指标
     */
    @MetricTopic(topic = "LCA_BASE_ALARM_HANDLE_NUMBER", description = "漏电报警处理数指标")
    public Long getLCAAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警处理率指标
     */
    @MetricTopic(topic = "LCA_BASE_ALARM_HANDLE_RATE", description = "漏电报警处理率指标")
    public Double getLCAAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警用户处理率排名前五位
     */
    @MetricTopic(topic = "LCA_BASE_ALARM_HANDLE_RATE_RANK", description = "漏电报警用户处理率排名前五位")
    public List<HandlerRateDTO> getLCAAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警用户处理率排名后五位
     */

    @MetricTopic(topic = "LCA_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "漏电报警用户处理率排名后五位")
    public List<HandlerRateDTO> getLCAAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "报警按时处理数指标")
    public Integer getAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, null));
    }

    /**
     * 报警处理效率指标
     */
    @MetricTopic(topic = "BASE_ALARM_HANDLER_ON_TIME_RATE", description = "处理效率指标")
    public Double getAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, null));
    }

    /**
     * 用户报警处理效率排名前五位
     */
    @MetricTopic(topic = "BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "用户处理效率排名前五位")
    public List<HandlerRateDTO> getAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, null));
    }

    /**
     * 用户报警处理效率排名后五位
     */
    @MetricTopic(topic = "BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "用户处理效率排名后五位")
    public List<HandlerRateDTO> getAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, null));
    }

    /**
     * 一级报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ONE_LEVEL_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "一级报警按时处理数指标")
    public Integer getOneLevelAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警处理效率指标
     */
    @MetricTopic(topic = "ONE_LEVEL_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "一级报警处理效率指标")
    public Double getOneLevelAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "ONE_LEVEL_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "一级报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getOneLevelAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "ONE_LEVEL_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "一级报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getOneLevelAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 二级报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "TWO_LEVEL_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "二级报警按时处理数指标")
    public Integer getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警处理效率指标
     */
    @MetricTopic(topic = "TWO_LEVEL_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "二级报警处理效率指标")
    public Double getTwoLevelAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "TWO_LEVEL_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "二级报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getTwoLevelAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "TWO_LEVEL_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "二级报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getTwoLevelAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 三级报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "THREE_LEVEL_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "三级报警按时处理数指标")
    public Integer getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警处理效率指标
     */
    @MetricTopic(topic = "THREE_LEVEL_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "三级报警处理效率指标")
    public Double getThreeLevelAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "THREE_LEVEL_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "三级报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getThreeLevelAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "THREE_LEVEL_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "三级报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getThreeLevelAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 明火烟雾报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "FLAME_SMOG_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "明火烟雾报警按时处理数指标")
    public Integer getFlameSmogAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警处理效率指标
     */
    @MetricTopic(topic = "FLAME_SMOG_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "明火烟雾报警处理效率指标")
    public Double getFlameSmogAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "FLAME_SMOG_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "明火烟雾报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getFlameSmogAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "FLAME_SMOG_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "明火烟雾报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getFlameSmogAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 违章占道报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ILOR_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "违章占道报警按时处理数指标")
    public Integer getIlorAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警处理效率指标
     */
    @MetricTopic(topic = "ILOR_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "违章占道报警处理效率指标")
    public Double getIlorAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "ILOR_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "违章占道报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getIlorAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "ILOR_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "违章占道报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getIlorAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "TC_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "交通拥堵报警按时处理数指标")
    public Integer getTCAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理效率指标
     */
    @MetricTopic(topic = "TC_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "交通拥堵报警处理效率指标")
    public Double getTCAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "TC_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "交通拥堵报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getTCAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "TC_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "交通拥堵报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getTCAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 水压报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "WP_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "水压报警按时处理数指标")
    public Integer getWPAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警处理效率指标
     */
    @MetricTopic(topic = "WP_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "水压报警处理效率指标")
    public Double getWPAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "WP_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "水压报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getWPAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "WP_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "水压报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getWPAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 手报报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "MCP_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "手报报警按时处理数指标")
    public Integer getMCPAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警处理效率指标
     */
    @MetricTopic(topic = "MCP_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "手报报警处理效率指标")
    public Double getMCPAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "MCP_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "手报报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getMCPAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "MCP_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "手报报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getMCPAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 温度报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "TEMPERATURE_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "温度报警按时处理数指标")
    public Integer getTemperatureAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警处理效率指标
     */
    @MetricTopic(topic = "TEMPERATURE_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "温度报警处理效率指标")
    public Double getTemperatureAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "TEMPERATURE_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "温度报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getTemperatureAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "TEMPERATURE_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "温度报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getTemperatureAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "UPFL_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "违规占用消防通道报警按时处理数指标")
    public Integer getUPFLAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理效率指标
     */
    @MetricTopic(topic = "UPFL_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "违规占用消防通道报警处理效率指标")
    public Double getUPFLAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "UPFL_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "违规占用消防通道报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getUPFLAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "UPFL_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "违规占用消防通道报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getUPFLAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * UPS报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "UPS_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "UPS报警按时处理数指标")
    public Integer getUPSAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警处理效率指标
     */
    @MetricTopic(topic = "UPS_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "UPS报警处理效率指标")
    public Double getUPSAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "UPS_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "UPS报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getUPSAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "UPS_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "UPS报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getUPSAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * 温湿度报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "WSD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "温湿度报警按时处理数指标")
    public Integer getWSDAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警处理效率指标
     */
    @MetricTopic(topic = "WSD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "温湿度报警处理效率指标")
    public Double getWSDAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "WSD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "温湿度报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getWSDAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "WSD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "温湿度报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getWSDAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 电压报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "VAL_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "电压报警按时处理数指标")
    public Integer getVALAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警处理效率指标
     */
    @MetricTopic(topic = "VAL_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "电压报警处理效率指标")
    public Double getVALAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "VAL_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "电压报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getVALAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "VAL_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "电压报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getVALAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 漏电报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "LCA_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "漏电报警按时处理数指标")
    public Integer getLCAAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警处理效率指标
     */
    @MetricTopic(topic = "LCA_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "漏电报警处理效率指标")
    public Double getLCAAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "LCA_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "漏电报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getLCAAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "LCA_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "漏电报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getLCAAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 按时时处理报警数
     *
     * @param param
     *
     * @return
     */

    private Integer getTotalHandlerTimeoutNumCommon(AlarmBimStatisticsQueryParam param) {
        AlarmHandlerTimeoutRateStatisticsVO alarmHandlerTimeoutRate = this.alarmBimStatisticsClient.getAlarmHandlerTimeoutRate(param);
        return alarmHandlerTimeoutRate.getTotalHandlerNum() - alarmHandlerTimeoutRate.getTotalHandlerTimeoutNum();
    }

    /**
     * 按时处理占比指标
     *
     * @param param
     *
     * @return
     */

    private Double getTotalHandlerTimeoutRateCommon(AlarmBimStatisticsQueryParam param) {
        AlarmHandlerTimeoutRateStatisticsVO alarmHandlerTimeoutRate = this.alarmBimStatisticsClient.getAlarmHandlerTimeoutRate(param);
        return Objects.isNull(alarmHandlerTimeoutRate.getTotalHandlerNum()) || 0 == alarmHandlerTimeoutRate.getTotalHandlerNum()
               ? 0 :
               Objects.isNull(alarmHandlerTimeoutRate.getTotalHandlerTimeoutNum()) ||
               0 == alarmHandlerTimeoutRate.getTotalHandlerTimeoutNum() ? 100 : 100 - alarmHandlerTimeoutRate.getTotalHandlerTimeoutRate()
                       .doubleValue();
    }

    /**
     * 用户超时占比排名前五
     *
     * @param param
     *
     * @return
     */
    private List<HandlerRateDTO> getAlarmUserHandlerTimeoutRateRankTopCommon(AlarmBimStatisticsQueryParam param) {
        //用户超时占比数据
        List<HandlerRateDTO> users = getTimeOutRateUserList(param);
        return users.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 用户超时占比排名后五
     *
     * @param param
     *
     * @return
     */
    private List<HandlerRateDTO> getAlarmUserHandlerTimeoutRateRankLastCommon(AlarmBimStatisticsQueryParam param) {
        //用户超时占比数据
        List<HandlerRateDTO> users = getTimeOutRateUserList(param);
        return users.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate))
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<HandlerRateDTO> getTimeOutRateUserList(AlarmBimStatisticsQueryParam param) {
        List<AlarmUserHandlerTimeoutRateStatisticsVO> alarmUserHandlerTimeoutRate = this.alarmBimStatisticsClient.getAlarmUserHandlerTimeoutRate(param);
        List<HandlerRateDTO> users = alarmUserHandlerTimeoutRate.stream()
                .map(user ->
                             HandlerRateDTO.builder()
                                     .name(user.getUsername())
                                     .num((long) (user.getTotalHandlerNum() - user.getTotalHandlerTimeoutNum()))
                                     .rate(Objects.isNull(user.getTotalHandlerNum()) || 0 == user.getTotalHandlerNum() ? 0 :
                                           Objects.isNull(user.getTotalHandlerTimeoutRate()) ||
                                           0 == user.getTotalHandlerTimeoutRate().doubleValue() ? 100 : 100 - user
                                                   .getTotalHandlerTimeoutRate().doubleValue())
                                     .build()
                    ).collect(Collectors.toList());
        return users;
    }

    private AlarmBimStatisticsQueryParam convertQueryBimParam(MetricParam param, String level, Set<String> eventTypes) {
        AlarmBimStatisticsQueryParam queryParam = new AlarmBimStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        if (Objects.nonNull(param.getPeriod())) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin(), "yyyy-MM-dd HH:mm:ss") + "," +
                                 DateUtils.format(param.getPeriod().getEnd(), "yyyy-MM-dd HH:mm:ss"));
        }
        queryParam.setAlarmLevelCode(level);
        queryParam.setAlarmEventTypeIds(eventTypes);
        return queryParam;
    }

    /**
     * 获取报警数通用方法
     *
     * @param param
     *
     * @return
     */
    private Long getAlarmNumberCommon(AlarmStatisticsQueryParam param) {
        AlarmBasicAnalysisStatisticsDTO statisticsDTO = this.alarmStatisticsClient
                .getAlarmBasicAnalysisNumberStatistics(param);
        return Objects.nonNull(statisticsDTO) ? statisticsDTO.getAlarmNum() : 0L;
    }

    private Long getAlarmHandlerNumberCommon(AlarmStatisticsQueryParam alarmStatisticsQueryParam) {
        AlarmBasicAnalysisStatisticsDTO statisticsDTO = this.alarmStatisticsClient
                .getAlarmBasicAnalysisNumberStatistics(alarmStatisticsQueryParam);
        return Objects.nonNull(statisticsDTO) ? statisticsDTO.getHandlerNum() : 0L;
    }

    private Double getAlarmHandlerRateCommon(AlarmStatisticsQueryParam alarmStatisticsQueryParam) {
        AlarmBasicAnalysisStatisticsDTO statisticsDTO = this.alarmStatisticsClient
                .getAlarmBasicAnalysisNumberStatistics(alarmStatisticsQueryParam);
        return Objects.isNull(statisticsDTO.getAlarmNum()) || 0 == statisticsDTO.getAlarmNum() ? 0 : BigDecimal
                .valueOf(statisticsDTO.getHandlerNum())
                .divide(BigDecimal.valueOf(statisticsDTO.getAlarmNum()), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();

    }

    private List<HandlerRateDTO> getAlarmUserHandleRateRankTopCommon(
            AlarmStatisticsQueryParam alarmStatisticsQueryParam
                                                                    ) {
        alarmStatisticsQueryParam.setPageSize(5);
        List<AlarmUserHandlerRateTopVO> alarmUserHandlerRateTopStatistics = this.alarmStatisticsClient.getAlarmUserHandlerRateTopStatistics(alarmStatisticsQueryParam);
        return alarmUserHandlerRateTopStatistics.stream().map(top ->
                                                                      HandlerRateDTO
                                                                              .builder()
                                                                              .num(top.getUserHandlerAlarmNumber())
                                                                              .name(top.getAlarmHandlerUserName())
                                                                              .rate(top.getUserHandlerRate())
                                                                              .build())
                .collect(Collectors.toList());
    }

    private List<HandlerRateDTO> getAlarmUserHandleRateRankLastCommon(
            AlarmStatisticsQueryParam alarmStatisticsQueryParam
                                                                     ) {
        alarmStatisticsQueryParam.setPageSize(5);
        List<AlarmUserHandlerRateTopVO> alarmUserHandlerRateLastTopStatistics = this.alarmStatisticsClient
                .getAlarmUserHandlerRateLastTopStatistics(alarmStatisticsQueryParam);
        return alarmUserHandlerRateLastTopStatistics.stream().map(top ->
                                                                          HandlerRateDTO
                                                                                  .builder()
                                                                                  .num(top.getUserHandlerAlarmNumber())
                                                                                  .name(top.getAlarmHandlerUserName())
                                                                                  .rate(top.getUserHandlerRate())
                                                                                  .build())
                .collect(Collectors.toList());
    }

    /**
     * 请求参数convert
     *
     * @param param      参数
     * @param level      报警级别
     * @param eventTypes 事件类型
     *
     * @return AlarmStatisticsQueryParam
     */
    private AlarmStatisticsQueryParam convertQueryParam(MetricParam param, String level, Set<String> eventTypes) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        queryParam.setAlarmLevelCode(level);
        queryParam.setAlarmEventTypeIds(eventTypes);
        queryParam.setPeriod(param.getPeriod());
        return queryParam;
    }
}
