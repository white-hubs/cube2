package com.guhecloud.cube.srv.gen.safety.management.service;

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
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmHandlerTimeoutStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmStatusEnum;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface SafetyAlarmStatisticsService {

    /**
     * 报警总数
     *
     * @param query
     *
     * @return
     */
    Integer getAlarmCount(AlarmStatisticsQuery query);

    /**
     * 报警状态统计
     *
     * @param query
     *
     * @return
     */
    AlarmStatusVO getAlarmStatus(AlarmStatisticsQuery query);

    /**
     * 报警状态统计
     *
     * @param query
     *
     * @return
     */
    List<AlarmStatusGroupVO> getAlarmStatusGroup(AlarmStatisticsQuery query);

    /**
     * 报警事件分类占比
     *
     * @param query
     *
     * @return
     */
    List<AlarmEventTypeRatioVO> getAlarmEventTypeRatio(AlarmStatisticsQuery query);

    /**
     * 报警响应情况
     *
     * @param query
     *
     * @return
     */
    Pagination<AlarmConfirmSituationVO> getAlarmConfirmSituation(AlarmStatisticsQuery query);

    /**
     * 报警趋势
     *
     * @param query
     *
     * @return
     */
    AlarmStatusTrendAnalysisVO getAlarmTrendAnalysis(AlarmStatisticsQuery query);

    /**
     * 报警高发
     *
     * @param query
     *
     * @return
     */
    List<AlarmTrendVO> getAlarmHighIncidenceTimeTrend(AlarmStatisticsQuery query);

    /**
     * 报警处理分析
     *
     * @param query
     *
     * @return
     */
    Pagination<StackedBarChartVO> getAlarmHandlerSituationAnalysis(AlarmStatisticsQuery query);

    /**
     * 报警情况
     *
     * @param query
     *
     * @return
     */
    AlarmSituationVO getAlarmSituation(AlarmStatisticsQuery query);

    /**
     * 报警排名
     *
     * @param query
     *
     * @return
     */
    Pagination<AlarmRankVO> getAlarmRanks(AlarmRankStatisticsQuery query);

    /**
     * 报警事件分类正确率
     *
     * @param query
     *
     * @return
     */
    List<AlarmEventTypeActualRatioVO> getEventTypeActualRatio(AlarmStatisticsQuery query);

    /**
     * 报警类型环比
     *
     * @param query
     *
     * @return
     */
    List<AlarmTypeVO> getAlarmTypeChainRatio(AlarmStatisticsQuery query);

    /**
     * 报警事件分类环比
     *
     * @param query
     *
     * @return
     */
    List<AlarmEventTypeChainVO> getAlarmEventTypeChainRatio(AlarmStatisticsQuery query);

    /**
     * 报警总数环比
     *
     * @param query
     *
     * @return
     */
    AlarmChainVO getAlarmChainRatio(AlarmStatisticsQuery query);

    /**
     * 报警状态环比
     *
     * @param query
     *
     * @return
     */
    AlarmStatusChainVO getAlarmStatusChainRatio(AlarmStatisticsQuery query);

    /**
     * 报警环比统计
     *
     * @param query
     * @param alarmStatus
     * @param filterAlarmStatus
     *
     * @return
     */
    AlarmChainVO getStatusChainRatio(AlarmStatisticsQuery query, AlarmStatusEnum alarmStatus, AlarmStatusEnum filterAlarmStatus);

    /**
     * 报警环比统计
     *
     * @param query
     * @param alarmStatus
     * @param filterAlarmStatus
     *
     * @return
     */
    AlarmChainVO getStatusChainRatio(AlarmStatisticsQuery query,
                                     AlarmStatusEnum alarmStatus,
                                     AlarmStatusEnum filterAlarmStatus,
                                     AlarmHandlerTimeoutStatusEnum timeoutStatus
                                    );

    /**
     * 报警处理分析统计
     *
     * @param query
     *
     * @return
     */
    AlarmHandlerSituationVO getHandlerSituation(AlarmStatisticsQuery query);

    /**
     * @Author wql
     * @Description 模块报警事件占比
     * @Date 10:45 2023/10/16
     **/
    List<AlarmEventTypeRatioVO> getModelEventTypeRatio(AlarmStatisticsQuery query);

    /**
     * @Author wql
     * @Description 模块报警事件正确率统计
     * @Date 10:22 2023/10/17
     **/
    List<AlarmEventTypeActualRatioVO> getModelEventTypeActualRatio(AlarmStatisticsQuery query);

    /**
     * 统计各状态计数
     *
     * @param query
     *
     * @return
     */
    List<GroupCountVO> countStatus(AlarmMetricQuery query);

    /**
     * 统计各状态计数（按市场汇总）
     *
     * @return
     */
    List<SubGroupCountVO> countStatusGroupByMarket(AlarmMetricQuery query);

    /**
     * 根据查询统计记录数
     *
     * @param query
     *
     * @return
     */
    Long count(AlarmMetricQuery query);

    /**
     * 统计超时记录数
     *
     * @param query
     *
     * @return
     */
    AlarmTimeoutCountVO countTimeout(AlarmMetricQuery query);

    /**
     * 统计各市场超时记录数
     *
     * @param query
     *
     * @return
     */
    List<AlarmMarketTimeoutCountVO> countTimeoutByMarket(AlarmMetricQuery query);
}
