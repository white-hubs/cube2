package com.guhecloud.cube.srv.gen.safety.management.controller.statistics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmRankStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.alarm.AlarmMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SubGroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmChainVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmConfirmSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeActualRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeChainVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmHandlerSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmMarketTimeoutCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmRankVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusChainVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusGroupVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusTrendAnalysisVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmTimeoutCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmTrendVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmTypeVO;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyAlarmStatisticsService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 报警统计
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyAlarmStatisticsController {

    private final SafetyAlarmStatisticsService safetyAlarmStatisticsService;

    /**
     * 报警统计卡片
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/status")
    public AlarmStatusVO getAlarmStatus(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmStatus(query);
    }

    /**
     * 报警状态分组统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/status-group")
    public List<AlarmStatusGroupVO> getAlarmStatusGroup(AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmStatusGroup(query);
    }

    /**
     * 报警事件占比统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/event-type/ratio")
    public List<AlarmEventTypeRatioVO> getAlarmEventTypeRatio(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmEventTypeRatio(query);
    }

    /**
     * 模块报警事件占比
     *
     * @Author wql
     * @Description 模块报警事件占比
     * @Date 10:43 2023/10/16
     **/
    @GetMapping("/safety-alarm-statistics/model-event/ratio")
    public List<AlarmEventTypeRatioVO> getModelEventTypeRatio(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getModelEventTypeRatio(query);
    }

    /**
     * 报警响应情况统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/confirm-situation")
    public Pagination<AlarmConfirmSituationVO> getAlarmConfirmSituation(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmConfirmSituation(query);
    }

    /**
     * 报警趋势分析统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/trend-analysis")
    public AlarmStatusTrendAnalysisVO getAlarmTrendAnalysis(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmTrendAnalysis(query);
    }

    /**
     * 报警高发时段趋势统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/high-incidence-time/trend-analysis")
    public List<AlarmTrendVO> getAlarmHighIncidenceTimeTrend(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmHighIncidenceTimeTrend(query);
    }

    /**
     * 报警处理情况分析统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/handler-situation-analysis")
    public Pagination<StackedBarChartVO> getAlarmHandlerSituationAnalysis(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmHandlerSituationAnalysis(query);
    }

    /**
     * 报警情况统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/situation")
    public AlarmSituationVO getAlarmSituation(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmSituation(query);
    }

    /**
     * 报警排名
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/ranks")
    public Pagination<AlarmRankVO> getAlarmRanks(@Valid AlarmRankStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmRanks(query);
    }

    /**
     * 报警事件分类正确率统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/event/actual-ratio")
    public List<AlarmEventTypeActualRatioVO> getEventTypeActualRatio(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getEventTypeActualRatio(query);
    }

    /**
     * 模块报警事件正确率统计
     *
     * @Author wql
     * @Description 模块报警事件正确率统计
     * @Date 10:21 2023/10/17
     **/
    @GetMapping("/safety-alarm-statistics/model/actual-ratio")
    public List<AlarmEventTypeActualRatioVO> getModelEventTypeActualRatio(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getModelEventTypeActualRatio(query);
    }

    /**
     * 报警总数环比统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/chain-ratio")
    public AlarmChainVO getAlarmChainRatio(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmChainRatio(query);
    }

    /**
     * 报警类型环比率统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/type/chain-ratio")
    public List<AlarmTypeVO> getAlarmTypeChainRatio(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmTypeChainRatio(query);
    }

    /**
     * 报警事件分类环比率统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/event-type/chain-ratio")
    public List<AlarmEventTypeChainVO> getAlarmEventTypeChainRatio(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmEventTypeChainRatio(query);
    }

    /**
     * 报警状态环比率统计
     * 统计报警总数环比, 未处理数环比, 已处理数环比
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/custom-status/chain-ratio")
    public AlarmStatusChainVO getAlarmStatusChainRatio(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getAlarmStatusChainRatio(query);
    }

    /**
     * 报警处理分析统计
     * 统计报警响应率，处理率，超时率和环比
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/handler-situation")
    public AlarmHandlerSituationVO getHandlerSituation(@Valid AlarmStatisticsQuery query) {
        return this.safetyAlarmStatisticsService.getHandlerSituation(query);
    }

    /**
     * 统计各状态计数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/count/status")
    List<GroupCountVO> countStatus(AlarmMetricQuery query) {
        return safetyAlarmStatisticsService.countStatus(query);
    }

    /**
     * 统计各状态计数（按市场汇总）
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/count/status/group-by-market")
    List<SubGroupCountVO> countStatusGroupByMarket(AlarmMetricQuery query) {
        return safetyAlarmStatisticsService.countStatusGroupByMarket(query);
    }

    /**
     * 统计超时记录数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/count/timeout")
    AlarmTimeoutCountVO countTimeout(AlarmMetricQuery query) {
        return safetyAlarmStatisticsService.countTimeout(query);
    }

    /**
     * 统计各市场超时记录数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/count/timeout/group-by-market")
    List<AlarmMarketTimeoutCountVO> countTimeoutByMarket(AlarmMetricQuery query) {
        return safetyAlarmStatisticsService.countTimeoutByMarket(query);
    }
}
