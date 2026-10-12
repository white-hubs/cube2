package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.alarm.client.dto.AlarmBasicAnalysisStatisticsDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmStatusStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerRateTopVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.AlarmMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.FlAME_SMOG_EVENT_CODES;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.HANDLER_STATUS_CODE;
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
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_HANDLER_TIMEOUT_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_STATUS_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_USER_HANDLER_RATE_LAST_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_USER_HANDLER_RATE_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_USER_HANDLER_TIMEOUT_RATE_REQUEST_URL;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/15 20:16
 * @Description:
 */
@Slf4j
@Service
public class HeadquartersAlarmBaseMetricService {

    private final AsyncHttpClientUtils asyncHttpClientUtils;

    public HeadquartersAlarmBaseMetricService(AsyncHttpClientUtils asyncHttpClientUtils) {this.asyncHttpClientUtils = asyncHttpClientUtils;}

    /**
     * 报警总数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_NUMBER", description = "总部报警总数指标")
    public Long getAlarmNumberMetric(AlarmMetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, null));

    }

    /**
     * 报警处理数指标
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部报警处理数指标")
    public Long getAlarmHandleNumberMetric(AlarmMetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, null));
    }

    /**
     * 报警处理率指标
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_HANDLE_RATE", description = "总部报警处理率指标")
    public Double getAlarmHandleRateMetric(AlarmMetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, null));

    }

    /**
     * 用户报警处理率排名前五位
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部用户报警处理率排名前五位")
    public List<HandlerRateDTO> getAlarmHandleRankMetric(AlarmMetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, null));
    }

    /**
     * 用户报警处理率排名后五位
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部用户报警处理率排名后五位")
    public List<HandlerRateDTO> getAlarmHandleLastRankMetric(AlarmMetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, null));
    }

    /**
     * 报警处理率排名前五的市场
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "报警处理率排名前五的市场")
    public List<HandlerRateDTO> getAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, null));
    }

    /**
     * 报警处理率排名后五的市场
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "报警处理率排名后五的市场")
    public List<HandlerRateDTO> getAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, null));
    }

    /**
     * 一级报警总数指标
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_NUMBER", description = "总部一级报警总数指标")
    public Long getOneLevelAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警处理数指标
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部一级报警处理数指标")
    public Long getOneLevelAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警处理率指标
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部一级报警处理率指标")
    public Double getOneLevelAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警用户处理率排名前五位
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部一级报警用户处理率排名前五位")
    public List<HandlerRateDTO> getOneLevelAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));

    }

    /**
     * 一级报警用户处理率排名后五位
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部一级报警用户处理率排名后五位")
    public List<HandlerRateDTO> getOneLevelAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警处理率排名前五的市场
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "一级报警处理率排名前五的市场")
    public List<HandlerRateDTO> getOneLevelAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警处理率排名后五的市场
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "一级报警处理率排名后五的市场")
    public List<HandlerRateDTO> getOneLevelAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 二级报警总数指标
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_NUMBER", description = "总部二级报警总数指标")
    public Long getTwoLevelAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警处理数指标
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部二级报警处理数指标")
    public Long getTwoLevelAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警处理率指标
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部二级报警处理率指标")
    public Double getTwoLevelAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警用户处理率排名前五位
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部二级报警用户处理率排名前五位")
    public List<HandlerRateDTO> getTwoLevelAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警用户处理率排名后五位
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部二级报警用户处理率排名后五位")
    public List<HandlerRateDTO> getTwoLevelAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警处理率排名前五的市场
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "二级报警处理率排名前五的市场")
    public List<HandlerRateDTO> getTwoLevelAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警处理率排名后五的市场
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "二级报警处理率排名后五的市场")
    public List<HandlerRateDTO> getTwoLevelAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 三级报警总数指标
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_NUMBER", description = "总部三级报警总数指标")
    public Long getThreeLevelAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警处理数指标
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部三级报警处理数指标")
    public Long getThreeLevelAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警处理率指标
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部三级报警处理率指标")
    public Double getThreeLevelAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警用户处理率排名前五位
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部三级报警用户处理率排名前五位")
    public List<HandlerRateDTO> getThreeLevelAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警用户处理率排名后五位
     */

    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部三级报警用户处理率排名后五位")
    public List<HandlerRateDTO> getThreeLevelAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警处理率排名前五的市场
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "二级报警处理率排名前五的市场")
    public List<HandlerRateDTO> getThreeLevelAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警处理率排名后五的市场
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "三级报警处理率排名后五的市场")
    public List<HandlerRateDTO> getThreeLevelAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 明火烟雾报警总数指标
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_NUMBER", description = "总部明火烟雾报警总数指标")
    public Long getFlameSmogAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警处理数指标
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部明火烟雾报警处理数指标")
    public Long getFlameSmogAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警处理率指标
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部明火烟雾报警处理率指标")
    public Double getFlameSmogAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警用户处理率排名前五位
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部明火烟雾报警用户处理率排名前五位")
    public List<HandlerRateDTO> getFlameSmogAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警用户处理率排名后五位
     */

    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部明火烟雾报警用户处理率排名后五位")
    public List<HandlerRateDTO> getFlameSmogAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警处理率排名前五的市场
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "明火烟雾报警处理率排名前五的市场")
    public List<HandlerRateDTO> getFlameSmogAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警处理率排名后五的市场
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "明火烟雾报警处理率排名后五的市场")
    public List<HandlerRateDTO> getFlameSmogAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 违章占道报警总数指标
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_HEAD_BASE_ALARM_NUMBER", description = "总部违章占道报警总数指标")
    public Long getIllegalParkingAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警处理数指标
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部违章占道报警处理数指标")
    public Long getIllegalParkingAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警处理率指标
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部违章占道报警处理率指标")
    public Double getIllegalParkingAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警用户处理率排名前五位
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部违章占道报警用户处理率排名前五位")
    public List<HandlerRateDTO> getIllegalParkingAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警用户处理率排名后五位
     */

    @MetricTopic(topic = "ILLEGAL_PARKING_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部违章占道报警用户处理率排名后五位")
    public List<HandlerRateDTO> getIllegalParkingAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警处理率排名前五的市场
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "违章占道报警处理率排名前五的市场")
    public List<HandlerRateDTO> getIllegalParkingAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警处理率排名后五的市场
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "违章占道报警处理率排名后五的市场")
    public List<HandlerRateDTO> getIllegalParkingAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警总数指标
     */
    @MetricTopic(topic = "TRAFFIC_JAM_HEAD_BASE_ALARM_NUMBER", description = "总部交通拥堵报警总数指标")
    public Long getTrafficJamAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理数指标
     */
    @MetricTopic(topic = "TRAFFIC_JAM_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部交通拥堵报警处理数指标")
    public Long getTrafficJamAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理率指标
     */
    @MetricTopic(topic = "TRAFFIC_JAM_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部交通拥堵报警处理率指标")
    public Double getTrafficJamAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警用户处理率排名前五位
     */
    @MetricTopic(topic = "TRAFFIC_JAM_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部交通拥堵报警用户处理率排名前五位")
    public List<HandlerRateDTO> getTrafficJamAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警用户处理率排名后五位
     */

    @MetricTopic(topic = "TRAFFIC_JAM_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部交通拥堵报警用户处理率排名后五位")
    public List<HandlerRateDTO> getTrafficJamAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理率排名前五的市场
     */
    @MetricTopic(topic = "TRAFFIC_JAM_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "交通拥堵报警处理率排名前五的市场")
    public List<HandlerRateDTO> getTrafficJamAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理率排名后五的市场
     */
    @MetricTopic(topic = "TRAFFIC_JAM_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "交通拥堵报警处理率排名后五的市场")
    public List<HandlerRateDTO> getTrafficJamAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 水压报警总数指标
     */
    @MetricTopic(topic = "WATER_PRESSURE_HEAD_BASE_ALARM_NUMBER", description = "总部水压报警总数指标")
    public Long getWaterPressureAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警处理数指标
     */
    @MetricTopic(topic = "WATER_PRESSURE_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部水压报警处理数指标")
    public Long getWaterPressureAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警处理率指标
     */
    @MetricTopic(topic = "WATER_PRESSURE_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部水压报警处理率指标")
    public Double getWaterPressureAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警用户处理率排名前五位
     */
    @MetricTopic(topic = "WATER_PRESSURE_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部水压报警用户处理率排名前五位")
    public List<HandlerRateDTO> getWaterPressureAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警用户处理率排名后五位
     */

    @MetricTopic(topic = "WATER_PRESSURE_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部水压报警用户处理率排名后五位")
    public List<HandlerRateDTO> getWaterPressureAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警处理率排名前五的市场
     */
    @MetricTopic(topic = "WATER_PRESSURE_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "水压报警处理率排名前五的市场")
    public List<HandlerRateDTO> getWaterPressureAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警处理率排名后五的市场
     */
    @MetricTopic(topic = "WATER_PRESSURE_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "水压报警处理率排名后五的市场")
    public List<HandlerRateDTO> getWaterPressureAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 手报报警总数指标
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_NUMBER", description = "总部手报报警总数指标")
    public Long getMcpAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警处理数指标
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部手报报警处理数指标")
    public Long getMcpAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警处理率指标
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部手报报警处理率指标")
    public Double getMcpAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警用户处理率排名前五位
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部手报报警用户处理率排名前五位")
    public List<HandlerRateDTO> getMcpAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警用户处理率排名后五位
     */

    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部手报报警用户处理率排名后五位")
    public List<HandlerRateDTO> getMcpAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警处理率排名前五的市场
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "手报报警处理率排名前五的市场")
    public List<HandlerRateDTO> getMcpAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警处理率排名后五的市场
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "手报报警处理率排名后五的市场")
    public List<HandlerRateDTO> getMcpAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * UPS总数指标
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_NUMBER", description = "总部UPS总数指标")
    public Long getUPSAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警处理数指标
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部UPS报警处理数指标")
    public Long getUPSAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警处理率指标
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部UPS报警处理率指标")
    public Double getUPSAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警用户处理率排名前五位
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部UPS报警用户处理率排名前五位")
    public List<HandlerRateDTO> getUPSAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警用户处理率排名后五位
     */

    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部UPS报警用户处理率排名后五位")
    public List<HandlerRateDTO> getUPSAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警处理率排名前五的市场
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "UPS报警处理率排名前五的市场")
    public List<HandlerRateDTO> getUPSAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警处理率排名后五的市场
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "UPS报警处理率排名后五的市场")
    public List<HandlerRateDTO> getUPSAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * 温度报警总数指标
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_NUMBER", description = "总部温度报警总数指标")
    public Long getTemperatureAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警处理数指标
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部温度报警处理数指标")
    public Long getTemperatureAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警处理率指标
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部温度报警处理率指标")
    public Double getTemperatureAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警用户处理率排名前五位
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部温度报警用户处理率排名前五位")
    public List<HandlerRateDTO> getTemperatureAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警用户处理率排名后五位
     */

    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部温度报警用户处理率排名后五位")
    public List<HandlerRateDTO> getTemperatureAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警处理率排名前五的市场
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "温度报警处理率排名前五的市场")
    public List<HandlerRateDTO> getTemperatureAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警处理率排名后五的市场
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "温度报警处理率排名后五的市场")
    public List<HandlerRateDTO> getTemperatureAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警总数指标
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_NUMBER", description = "总部违规占用消防通道报警总数指标")
    public Long getUPFLAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理数指标
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部违规占用消防通道报警处理数指标")
    public Long getUPFLAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理率指标
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部违规占用消防通道报警处理率指标")
    public Double getUPFLAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警用户处理率排名前五位
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部违规占用消防通道报警用户处理率排名前五位")
    public List<HandlerRateDTO> getUPFLAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警用户处理率排名后五位
     */

    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部违规占用消防通道报警用户处理率排名后五位")
    public List<HandlerRateDTO> getUPFLAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理率排名前五的市场
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "违规占用消防通道报警处理率排名前五的市场")
    public List<HandlerRateDTO> getUPFLAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理率排名后五的市场
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "违规占用消防通道报警处理率排名后五的市场")
    public List<HandlerRateDTO> getUPFLAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 温湿度报警总数指标
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_NUMBER", description = "总部温湿度报警总数指标")
    public Long getWSDAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警处理数指标
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部温湿度报警处理数指标")
    public Long getWSDAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警处理率指标
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部温湿度报警处理率指标")
    public Double getWSDAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警用户处理率排名前五位
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部温湿度报警用户处理率排名前五位")
    public List<HandlerRateDTO> getWSDAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警用户处理率排名后五位
     */

    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部温湿度报警用户处理率排名后五位")
    public List<HandlerRateDTO> getWSDAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理率排名前五的市场
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "违规占用消防通道报警处理率排名前五的市场")
    public List<HandlerRateDTO> getWSDAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理率排名后五的市场
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "违规占用消防通道报警处理率排名后五的市场")
    public List<HandlerRateDTO> getWSDAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 电压报警总数指标
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_NUMBER", description = "总部电压报警总数指标")
    public Long getVALAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警处理数指标
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部电压报警处理数指标")
    public Long getVALAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警处理率指标
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部电压报警处理率指标")
    public Double getVALAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警用户处理率排名前五位
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部电压报警用户处理率排名前五位")
    public List<HandlerRateDTO> getVALAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警用户处理率排名后五位
     */

    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部电压报警用户处理率排名后五位")
    public List<HandlerRateDTO> getVALAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警处理率排名前五的市场
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "电压报警处理率排名前五的市场")
    public List<HandlerRateDTO> getVALAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警处理率排名后五的市场
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "电压报警处理率排名后五的市场")
    public List<HandlerRateDTO> getVALAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 漏电报警总数指标
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_NUMBER", description = "总部漏电报警总数指标")
    public Long getLCAAlarmNumberMetric(MetricParam param) {
        return this.getAlarmNumberCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警处理数指标
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_HANDLE_NUMBER", description = "总部漏电报警处理数指标")
    public Long getLCAAlarmHandleNumberMetric(MetricParam param) {
        return this.getAlarmHandlerNumberCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警处理率指标
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_HANDLE_RATE", description = "总部漏电报警处理率指标")
    public Double getLCAAlarmHandleRateMetric(MetricParam param) {
        return this.getAlarmHandlerRateCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警用户处理率排名前五位
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_HANDLE_RATE_RANK", description = "总部漏电报警用户处理率排名前五位")
    public List<HandlerRateDTO> getLCAAlarmHandleRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankTopCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警用户处理率排名后五位
     */

    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_HANDLE_RATE_LAST_RANK", description = "总部漏电报警用户处理率排名后五位")
    public List<HandlerRateDTO> getLCAAlarmHandleLastRankMetric(MetricParam param) {
        return this.getAlarmUserHandleRateRankLastCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警处理率排名前五的市场
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_RANK", description = "漏电报警处理率排名前五的市场")
    public List<HandlerRateDTO> geLCAAlarmHandleMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankTopCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警处理率排名后五的市场
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_HANDLE_MARKET_RATE_LAST_RANK", description = "漏电报警处理率排名后五的市场")
    public List<HandlerRateDTO> getLCAAlarmHandleLastMarketRankMetric(AlarmMetricParam param) {
        return this.getHandlerRateDTORankLastCommon(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部报警按时处理数指标")
    public Integer getAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, null));
    }

    /**
     * 报警处理效率指标
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部处理效率指标")
    public Double getAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, null));
    }

    /**
     * 用户报警处理效率排名前五位
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "总部用户处理效率排名前五位")
    public List<HandlerRateDTO> getAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, null));
    }

    /**
     * 用户报警处理效率排名后五位
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "总部用户处理效率排名后五位")
    public List<HandlerRateDTO> getAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, null));
    }

    /**
     * 报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "报警处理效率排名前五的市场")
    public List<HandlerRateDTO> getAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, null));
    }

    /**
     * 报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "报警处理效率排名后五的市场")
    public List<HandlerRateDTO> getAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, null));
    }

    /**
     * 一级报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部一级报警按时处理数指标")
    public Integer getOneLevelAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警处理效率指标
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部一级报警处理效率指标")
    public Double getOneLevelAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "总部一级报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getOneLevelAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部一级报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getOneLevelAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "ONE_LEVEL_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "一级报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getOneLevelAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 一级报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "ONE_LEVEL_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "一级报警处理效率排名后五的市场")
    public List<HandlerRateDTO> getOneLevelAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 二级报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部二级报警按时处理数指标")
    public Integer getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警处理效率指标
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部二级报警处理效率指标")
    public Double getTwoLevelAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "总部二级报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getTwoLevelAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部二级报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getTwoLevelAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "TWO_LEVEL_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "二级报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getTwoLevelAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 二级报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "TWO_LEVEL_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "二级报警处理效率排名后五的市场")
    public List<HandlerRateDTO> getTwoLevelAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 三级报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部三级报警按时处理数指标")
    public Integer getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警处理效率指标
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部三级报警处理效率指标")
    public Double getThreeLevelAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "总部三级报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getThreeLevelAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部三级报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getThreeLevelAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "THREE_LEVEL_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "三级报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getThreeLevelAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 三级报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "THREE_LEVEL_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "二级报警处理效率排名后五的市场")
    public List<HandlerRateDTO> getThreeLevelAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 明火烟雾报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部明火烟雾报警按时处理数指标")
    public Integer getFlameSmogAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警处理效率指标
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部明火烟雾报警处理效率指标")
    public Double getFlameSmogAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "总部明火烟雾报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getFlameSmogAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部明火烟雾报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getFlameSmogAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "FLAME_SMOG_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "明火烟雾报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getFlameSmogAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 明火烟雾报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "FLAME_SMOG_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "明火烟雾报警处理效率排名后五的市场")
    public List<HandlerRateDTO> getFlameSmogAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 违章占道报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ILOR_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部违章占道报警按时处理数指标")
    public Integer getIlorAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警处理效率指标
     */
    @MetricTopic(topic = "ILOR_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部违章占道报警处理效率指标")
    public Double getIlorAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "ILOR_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "总部违章占道报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getIlorAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "ILOR_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部违章占道报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getIlorAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "ILOR_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "明火烟雾处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getIlorAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 违章占道报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "ILOR_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "违章占道报警处理效率排名后五的市场")
    public List<HandlerRateDTO> getIlorAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "TC_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部交通拥堵报警按时处理数指标")
    public Integer getTCAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理效率指标
     */
    @MetricTopic(topic = "TC_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部交通拥堵报警处理效率指标")
    public Double getTCAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "TC_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "总部交通拥堵报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getTCAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "TC_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部交通拥堵报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getTCAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "TC_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "交通拥堵报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getTCAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "TC_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "交通拥堵报警处理效率排名后五的市场")
    public List<HandlerRateDTO> getTCAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 水压报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "WP_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部水压报警按时处理数指标")
    public Integer getWPAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警处理效率指标
     */
    @MetricTopic(topic = "WP_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部水压报警处理效率指标")
    public Double getWPAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "WP_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "总部水压报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getWPAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "WP_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "总部水压报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getWPAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "TC_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "水压报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getWPAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 水压报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "TC_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "水压报警处理效率排名后五的市场")
    public List<HandlerRateDTO> getWPAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 手报报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部手报报警按时处理数指标")
    public Integer getMCPAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警处理效率指标
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部手报报警处理效率指标")
    public Double getMCPAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "总部手报报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getMCPAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部手报报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getMCPAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "MCP_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "手报报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getMCPAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 手报报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "MCP_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "手报报警处理效率排名后五的市场")
    public List<HandlerRateDTO> getMCPAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * 温度报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部温度报警按时处理数指标")
    public Integer getTemperatureAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警处理效率指标
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部温度报警处理效率指标")
    public Double getTemperatureAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "总部温度报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getTemperatureAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部温度报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getTemperatureAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "TEMPERATURE_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "温度报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getTemperatureAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 温度报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "TEMPERATURE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "温度报警处理效率排名后五的市场")
    public List<HandlerRateDTO> getTemperatureAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部违规占用消防通道报警按时处理数指标")
    public Integer getUPFLAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理效率指标
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部违规占用消防通道报警处理效率指标")
    public Double getUPFLAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "总部违规占用消防通道报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getUPFLAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部违规占用消防通道报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getUPFLAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "UPFL_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "违规占用消防通道报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getUPFLAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "UPFL_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "违规占用消防通道报警理效率排名后五的市场")
    public List<HandlerRateDTO> getUPFLAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * UPS报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部UPS报警按时处理数指标")
    public Integer getUPSAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警处理效率指标
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部UPS报警处理效率指标")
    public Double getUPSAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "总部UPS报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getUPSAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "总部UPS报警用户处理效率排名后五位")
    public List<HandlerRateDTO> getUPSAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "UPS_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "UPS报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getUPSAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * UPS报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "UPS_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "UPS报警理效率排名后五的市场")
    public List<HandlerRateDTO> getUPSAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * 温湿度报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部温湿度报警按时处理数指标")
    public Integer getWSDAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警处理效率指标
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部温湿度报警处理效率指标")
    public Double getWSDAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "总部温湿度报警用户处理效率排名前五位"
    )
    public List<HandlerRateDTO> getWSDAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部温湿度报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getWSDAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "WSD_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "温湿度报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getWSDAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 温湿度报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "UPS_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "温湿度报警理效率排名后五的市场")
    public List<HandlerRateDTO> getWSDAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 电压报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部电压报警按时处理数指标")
    public Integer getVALAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警处理效率指标
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部电压报警处理效率指标")
    public Double getVALAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "总部电压报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getVALAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部电压报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getVALAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "VAL_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "电压报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getVALAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 电压报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "VAL_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "电压报警理效率排名后五的市场")
    public List<HandlerRateDTO> getVALAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 漏电报警按时处理数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_HANDLER_ON_TIME_NUMBER", description = "总部漏电报警按时处理数指标")
    public Integer getLCAAlarmHandlerTimeoutEfficiencyMetric(MetricParam param) {
        return getTotalHandlerTimeoutNumCommon(this.convertQueryBimParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警处理效率指标
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_HANDLER_ON_TIME_RATE", description = "总部漏电报警处理效率指标")
    public Double getLCAAlarmHandlerTimeoutEfficiencyRateMetric(MetricParam param) {
        return getTotalHandlerTimeoutRateCommon(this.convertQueryBimParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警用户处理效率排名前五位
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK", description = "总部漏电报警用户处理效率排名前五位")
    public List<HandlerRateDTO> getLCAAlarmUserHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警用户处理效率排名后五位
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_USER_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK",
                 description = "总部漏电报警用户处理效率排名后五位"
    )
    public List<HandlerRateDTO> getLCAAlarmUserHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return getAlarmUserHandlerTimeoutRateRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警处理效率排名前五的市场
     */
    @MetricTopic(topic = "LCA_HEAD_BASE_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_TOP_RANK",
                 description = "漏电报警处理效率排名前五的市场"
    )
    public List<HandlerRateDTO> getLCAAlarmMarketHandlerTimeoutRateRankTopMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankTopCommon(this.convertQueryBimParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 漏电报警处理效率排名后五的市场
     */
    @MetricTopic(topic = "VAL_ALARM_MARKET_HANDLER_TIMEOUT_EFFICIENCY_LAST_RANK", description = "漏电报警理效率排名后五的市场")
    public List<HandlerRateDTO> getLCAAlarmMarketHandlerTimeoutRateRankLastMetric(MetricParam param) {
        return this.getAlarmHandlerEfficiencyRankLastCommon(this.convertQueryBimParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 按时时处理报警数
     *
     * @param param
     *
     * @return
     */

    private Integer getTotalHandlerTimeoutNumCommon(AlarmBimStatisticsQueryParam param) {
        List<MarketMetricResult<AlarmHandlerTimeoutRateStatisticsVO>> marketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_HANDLER_TIMEOUT_RATE_REQUEST_URL, HttpMethod.POST, param, param,
                                     new TypeReference<AlarmHandlerTimeoutRateStatisticsVO>() {
                                     });
        //获取市场总处理数
        int totalOnTimeHandlerNum = 0;
        //获取市场按时处理的报警数量
        for (MarketMetricResult<AlarmHandlerTimeoutRateStatisticsVO> marketMetricResult : marketMetrics) {
            int marketOnTimeHandlerNum = 0;
            AlarmHandlerTimeoutRateStatisticsVO metricStruct = marketMetricResult.getMetricStruct();
            if (Objects.nonNull(metricStruct.getTotalHandlerTimeoutNum()) && Objects.nonNull(metricStruct.getTotalHandlerNum())) {
                marketOnTimeHandlerNum = marketMetricResult.getMetricStruct().getTotalHandlerNum() -
                                         marketMetricResult.getMetricStruct().getTotalHandlerTimeoutNum();
            }
            totalOnTimeHandlerNum += marketOnTimeHandlerNum;
        }
        return totalOnTimeHandlerNum;
    }

    /**
     * 按时处理占比指标
     *
     * @param param
     *
     * @return
     */

    private Double getTotalHandlerTimeoutRateCommon(AlarmBimStatisticsQueryParam param) {
        List<MarketMetricResult<AlarmHandlerTimeoutRateStatisticsVO>> marketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_HANDLER_TIMEOUT_RATE_REQUEST_URL, HttpMethod.POST, param, param,
                                     new TypeReference<AlarmHandlerTimeoutRateStatisticsVO>() {
                                     });
        //获取市场总处理数
        int totalHandlerNum = 0;
        int totalOnTimeHandlerNum = 0;
        //获取市场按时处理的报警数量
        for (MarketMetricResult<AlarmHandlerTimeoutRateStatisticsVO> marketMetricResult : marketMetrics) {
            int marketOnTimeHandlerNum = 0;
            AlarmHandlerTimeoutRateStatisticsVO metricStruct = marketMetricResult.getMetricStruct();
            if (Objects.nonNull(metricStruct.getTotalHandlerTimeoutNum()) && Objects.nonNull(metricStruct.getTotalHandlerNum())) {
                marketOnTimeHandlerNum = marketMetricResult.getMetricStruct().getTotalHandlerNum() -
                                         marketMetricResult.getMetricStruct().getTotalHandlerTimeoutNum();
            }
            totalHandlerNum += metricStruct.getTotalHandlerNum();
            totalOnTimeHandlerNum += marketOnTimeHandlerNum;
        }
        return 0 == totalHandlerNum ? 0 : BigDecimal.valueOf(totalOnTimeHandlerNum)
                .divide(BigDecimal.valueOf(totalHandlerNum), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
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

    /**
     * 市场处理效率排名前五
     */
    private List<HandlerRateDTO> getAlarmHandlerEfficiencyRankTopCommon(AlarmBimStatisticsQueryParam param) {
        return this.getAlarmHandlerEfficiencyRankCommon(param)
                .stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                .limit(5).collect(Collectors.toList());
    }

    /**
     * 市场处理效率排名后五
     */
    private List<HandlerRateDTO> getAlarmHandlerEfficiencyRankLastCommon(AlarmBimStatisticsQueryParam param) {
        return this.getAlarmHandlerEfficiencyRankCommon(param)
                .stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate))
                .limit(5).collect(Collectors.toList());
    }

    /**
     * 市场处理效率排名
     *
     * @param param
     *
     * @return
     */
    private List<HandlerRateDTO> getAlarmHandlerEfficiencyRankCommon(AlarmBimStatisticsQueryParam param) {
        List<MarketMetricResult<AlarmHandlerTimeoutRateStatisticsVO>> marketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_HANDLER_TIMEOUT_RATE_REQUEST_URL, HttpMethod.POST, param, param,
                                     new TypeReference<AlarmHandlerTimeoutRateStatisticsVO>() {
                                     });
        List<HandlerRateDTO> HandlerRateDTOs = new ArrayList<>();
        for (MarketMetricResult<AlarmHandlerTimeoutRateStatisticsVO> marketMetricResult : marketMetrics) {
            int marketOnTimeHandlerNum = 0;
            AlarmHandlerTimeoutRateStatisticsVO metricStruct = marketMetricResult.getMetricStruct();
            if (Objects.nonNull(metricStruct.getTotalHandlerTimeoutNum()) && Objects.nonNull(metricStruct.getTotalHandlerNum())) {
                marketOnTimeHandlerNum = marketMetricResult.getMetricStruct().getTotalHandlerNum() -
                                         marketMetricResult.getMetricStruct().getTotalHandlerTimeoutNum();
            }
            HandlerRateDTOs.add(HandlerRateDTO.builder()
                                        .marketName(marketMetricResult.getMarketName())
                                        .num((long) marketOnTimeHandlerNum)
                                        .rate(
                                                0 == marketOnTimeHandlerNum ? 0 : BigDecimal.valueOf(marketOnTimeHandlerNum)
                                                        .divide(BigDecimal.valueOf(metricStruct.getTotalHandlerNum()), 2, RoundingMode.HALF_UP)
                                                        .multiply(BigDecimal.valueOf(100))
                                                        .doubleValue())
                                        .build());
        }
        return HandlerRateDTOs;
    }

    private List<HandlerRateDTO> getTimeOutRateUserList(AlarmBimStatisticsQueryParam param) {
        List<MarketMetricResult<List<AlarmUserHandlerTimeoutRateStatisticsVO>>> alarmUserHandlerTimeoutRate = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_USER_HANDLER_TIMEOUT_RATE_REQUEST_URL, HttpMethod.POST, param, param,
                                     new TypeReference<List<AlarmUserHandlerTimeoutRateStatisticsVO>>() {
                                     });
        return alarmUserHandlerTimeoutRate.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().stream().map(user -> {
                    int usertOnTimeHandlerNum = 0;
                    if (Objects.nonNull(user.getTotalHandlerTimeoutNum()) && Objects.nonNull(user.getTotalHandlerNum())) {
                        usertOnTimeHandlerNum = user.getTotalHandlerNum() -
                                                user.getTotalHandlerTimeoutNum();
                    }
                    return HandlerRateDTO.builder()
                            .marketName(t.getMarketName())
                            .name(user.getUsername())
                            .num((long) (user.getTotalHandlerNum() - user.getTotalHandlerTimeoutNum()))
                            .rate(0 == usertOnTimeHandlerNum ? 0 : BigDecimal.valueOf(usertOnTimeHandlerNum)
                                    .divide(BigDecimal.valueOf(user.getTotalHandlerNum()), 2, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100))
                                    .doubleValue())
                            .build();
                }).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
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
        List<MarketMetricResult<AlarmBasicAnalysisStatisticsDTO>> allAlarmMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_NUMBER_REQUEST_URL, HttpMethod.GET, null, param,
                                     new TypeReference<AlarmBasicAnalysisStatisticsDTO>() {
                                     });
        return allAlarmMarketMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getAlarmNum())
                .reduce(Long::sum).orElse(0L);
    }

    private Long getAlarmHandlerNumberCommon(AlarmStatisticsQueryParam queryParam) {
        List<MarketMetricResult<List<AlarmStatusStatisticsVO>>> allMarketInspectionTotalMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_STATUS_NUMBER_REQUEST_URL, HttpMethod.GET, null, queryParam, new TypeReference<List<AlarmStatusStatisticsVO>>() {
                });

        List<AlarmStatusStatisticsVO> alarmStatusStatisticsVOS = allMarketInspectionTotalMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(MarketMetricResult::getMetricStruct)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return alarmStatusStatisticsVOS.stream()
                .filter(status -> "ALARM_HANDLER"
                        .equals(status.getAlarmStatus()))
                .map(AlarmStatusStatisticsVO::getAlarmNum)
                .reduce(Long::sum).orElse(0L);
    }

    private Double getAlarmHandlerRateCommon(AlarmStatisticsQueryParam alarmStatisticsQueryParam) {
        Long alarmNumberCommon = getAlarmNumberCommon(alarmStatisticsQueryParam);
        Long handlerNumberCommon = getAlarmHandlerNumberCommon(alarmStatisticsQueryParam);

        return Objects.isNull(alarmNumberCommon) || 0 == alarmNumberCommon ? 0 : BigDecimal
                .valueOf(handlerNumberCommon)
                .divide(BigDecimal.valueOf(alarmNumberCommon), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();

    }

    private List<HandlerRateDTO> getAlarmUserHandleRateRankTopCommon(
            AlarmStatisticsQueryParam queryParam
                                                                    ) {
        queryParam.setPageSize(5);
        List<MarketMetricResult<List<AlarmUserHandlerRateTopVO>>> topMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_USER_HANDLER_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<List<AlarmUserHandlerRateTopVO>>() {
                                     });
        return topMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().stream().map(user ->
                                                                   HandlerRateDTO.builder()
                                                                           .marketName(t.getMarketName())
                                                                           .num(user.getUserHandlerAlarmNumber())
                                                                           .name(user.getAlarmHandlerUserName())
                                                                           .rate(user.getUserHandlerRate())
                                                                           .build()
                                                          ).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream().sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                .limit(queryParam.getPageSize())
                .collect(Collectors.toList());
    }

    private List<HandlerRateDTO> getAlarmUserHandleRateRankLastCommon(
            AlarmStatisticsQueryParam queryParam
                                                                     ) {
        queryParam.setPageSize(5);
        List<MarketMetricResult<List<AlarmUserHandlerRateTopVO>>> lastTopMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_USER_HANDLER_RATE_LAST_TOP_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<List<AlarmUserHandlerRateTopVO>>() {
                                     });
        return lastTopMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().stream().map(user ->
                                                                   HandlerRateDTO.builder()
                                                                           .marketName(t.getMarketName())
                                                                           .num(user.getUserHandlerAlarmNumber())
                                                                           .name(user.getAlarmHandlerUserName())
                                                                           .rate(user.getUserHandlerRate())
                                                                           .build()
                                                          ).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream().sorted(Comparator.comparing(HandlerRateDTO::getRate))
                .limit(queryParam.getPageSize())
                .collect(Collectors.toList());
    }

    /**
     * 市场处理率排名前五位
     */
    private List<HandlerRateDTO> getHandlerRateDTORankTopCommon(AlarmStatisticsQueryParam queryParam) {
        return this.getHandlerRateDTOsCommon(queryParam)
                .stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 市场处理率排名后五位
     */
    private List<HandlerRateDTO> getHandlerRateDTORankLastCommon(AlarmStatisticsQueryParam queryParam) {
        return this.getHandlerRateDTOsCommon(queryParam)
                .stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate))
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 市场处理率排名
     *
     * @param queryParam
     *
     * @return
     */
    private List<HandlerRateDTO> getHandlerRateDTOsCommon(AlarmStatisticsQueryParam queryParam) {
        List<MarketMetricResult<List<AlarmStatusStatisticsVO>>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_STATUS_NUMBER_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<List<AlarmStatusStatisticsVO>>() {
                                     });
        List<HandlerRateDTO> HandlerRateDTOs = new ArrayList<>();
        for (MarketMetricResult<List<AlarmStatusStatisticsVO>> result : allMarketMetrics) {
            if (CollectionUtils.isEmpty(result.getMetricStruct()) ||
                result.getMetricStruct().stream().noneMatch(status -> status.getAlarmStatus()
                        .equals(HANDLER_STATUS_CODE))) {
                continue;
            }
            long totalNum = result.getMetricStruct().stream().mapToLong(AlarmStatusStatisticsVO::getAlarmNum).sum();
            //已处理报警数量
            AlarmStatusStatisticsVO handler = result.getMetricStruct().stream().filter(status -> status.getAlarmStatus()
                            .equals(HANDLER_STATUS_CODE))
                    .findFirst().get();
            double rate = 0;
            if (totalNum > 0) {
                rate = BigDecimal.valueOf(handler.getAlarmNum())
                        .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
            }
            HandlerRateDTOs.add(HandlerRateDTO.builder()
                                        .num(handler.getAlarmNum())
                                        .marketName(result.getMarketName())
                                        .rate(rate)
                                        .build());
        }
        return HandlerRateDTOs;
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
