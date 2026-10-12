package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmRankStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmRecordQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.alarm.AlarmMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmRecordPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmReportPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmRiskIndexVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SubGroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmConfirmSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeActualRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmHandlerSituationAnalysisVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmRankVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusGroupVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmTimeoutCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmTrendVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmTypeVO;
import com.guhecloud.cube.srv.gen.safety.management.po.AlarmPushConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyAlarmRecordPO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SafetyAlarmRecordMapper extends BaseMapper<SafetyAlarmRecordPO> {

    /**
     * 报警分页列表
     *
     * @param page
     * @param query
     *
     * @return
     */
    Page<AlarmRecordPageVO> selectAlarmRecordPage(Page page, @Param("query") AlarmRecordQuery query);

    /**
     * 报警列表
     *
     * @param query
     *
     * @return
     */
    List<AlarmRecordPageVO> selectAlarmRecords(@Param("query") AlarmRecordQuery query);

    /**
     * 报警报表分页列表
     *
     * @param page
     * @param query
     *
     * @return
     */
    Page<AlarmReportPageVO> selectAlarmReportPage(Page page, @Param("query") AlarmRecordQuery query);

    /**
     * 报警报表列表
     *
     * @param query
     *
     * @return
     */
    List<AlarmReportPageVO> selectAlarmReports(@Param("query") AlarmRecordQuery query);

    /**
     * 查询报警总数
     *
     * @param query
     *
     * @return
     */
    Integer selectAlarmCount(@Param("query") AlarmStatisticsQuery query);

    /**
     * 报警卡片统计
     *
     * @param query
     *
     * @return
     */
    AlarmStatusVO selectAlarmStatus(@Param("query") AlarmStatisticsQuery query);

    /**
     * 事件分类分组统计
     *
     * @param query
     *
     * @return
     */
    List<AlarmEventTypeRatioVO> selectAlarmEventTypeRatio(@Param("query") AlarmStatisticsQuery query);

    /**
     * 报警响应情况统计
     *
     * @param query
     *
     * @return
     */
    Page<AlarmConfirmSituationVO> selectAlarmConfirmSituation(Page page, @Param("query") AlarmStatisticsQuery query);

    /**
     * 报警趋势分析
     *
     * @param query
     *
     * @return
     */
    List<AlarmTrendVO> selectAlarmTrendAnalysis(@Param("query") AlarmStatisticsQuery query);

    /**
     * 报警处理情况分析
     *
     * @param query
     *
     * @return
     */
    List<AlarmHandlerSituationAnalysisVO> selectAlarmHandlerSituationAnalysis(@Param("query") AlarmStatisticsQuery query);

    /**
     * 报警情况统计
     *
     * @param query
     *
     * @return
     */
    AlarmSituationVO selectAlarmSituation(@Param("query") AlarmStatisticsQuery query);

    /**
     * 报警排名统计
     *
     * @param query
     *
     * @return
     */
    Page<AlarmRankVO> selectAlarmRanks(Page page, @Param("query") AlarmRankStatisticsQuery query);

    /**
     * 报警事件分类正确率统计
     *
     * @param query
     *
     * @return
     */
    List<AlarmEventTypeActualRatioVO> selectEventTypeActualRatio(@Param("query") AlarmStatisticsQuery query);

    /**
     * 报警类型占比
     *
     * @param query
     *
     * @return
     */
    List<AlarmTypeVO> selectAlarmTypeChainRatio(@Param("query") AlarmStatisticsQuery query);

    /**
     * 报警状态分组统计
     *
     * @param query
     *
     * @return
     */
    List<AlarmStatusGroupVO> selectAlarmStatusGroup(@Param("query") AlarmStatisticsQuery query);

    /**
     * 获取报警级别下超时的报警列表
     *
     * @param alarmLevelCode
     * @param begin
     * @param end
     *
     * @return
     */
    List<SafetyAlarmRecordPO> selectLevelTimeoutAlarms(@Param("alarmLevelCode") String alarmLevelCode, @Param("begin") LocalDateTime begin,
                                                       @Param("end") LocalDateTime end
                                                      );

    /**
     * @Author wql
     * @Description 模块报警事件数量统计
     * @Date 10:55 2023/10/16
     **/
    List<AlarmEventTypeRatioVO> getModelEventTypeRatio(@Param("query") AlarmStatisticsQuery query);

    /**
     * @Author wql
     * @Description 模块报警事件正确率统计
     * @Date 10:24 2023/10/17
     **/
    List<AlarmEventTypeActualRatioVO> getModelEventTypeActualRatio(@Param("query") AlarmStatisticsQuery query);

    List<SafetyAlarmRecordPO> getTimeoutAlarmRecords(@Param("timeoutPushConfigs") List<AlarmPushConfigPO> timeoutPushConfig);

    /**
     * 只更新报警展示状态，不刷新审计字段，避免污染数据同步水位。
     *
     * @param id        报警记录主键
     * @param displayed 是否展示
     */
    void updateDisplayedKeepUpdateTime(@Param("id") Long id, @Param("displayed") Boolean displayed);

    /**
     * @Author wql
     * @Description 获取安全风险指数计算的报警记录
     * @Date 10:30 2023/10/16
     **/
    List<AlarmRiskIndexVO> selectRiskIndexAlarmRecord(@Param("period") Period period, @Param("marketTenant") Integer marketTenant);

    String AlarmMetricQueryFilterSQL = "sar.displayed = true and mc.status = true" +
                                       "<if test=\"query.marketTenantIDs != null and query.marketTenantIDs.size() > 0\">" +
                                       "AND sar.market_tenant IN " +
                                       "<foreach item=\"id\" collection=\"query.marketTenantIDs\" open=\"(\" separator=\",\" close=\")\">" +
                                       "#{id}" +
                                       "</foreach>" +
                                       "</if>" +
                                       "<if test=\"query.createdPeriod != null\">" +
                                       "AND sar.create_time BETWEEN #{query.createdPeriod.begin} AND #{query.createdPeriod.end}" +
                                       "</if>" +
                                       "<if test=\"query.applicationCodes != null and query.applicationCodes.size() > 0\">" +
                                       "AND sar.application_code IN " +
                                       "<foreach item=\"code\" collection=\"query.applicationCodes\" open=\"(\" separator=\",\" close=\")\">" +
                                       "#{code}" +
                                       "</foreach>" +
                                       "</if>" +
                                       "<if test=\"query.statusCodes != null and query.statusCodes.size() > 0\">" +
                                       "AND sar.status IN " +
                                       "<foreach item=\"code\" collection=\"query.statusCodes\" open=\"(\" separator=\",\" close=\")\">" +
                                       "#{code}" +
                                       "</foreach>" +
                                       "</if>" +
                                       "";

    @Select({
            "<script>",
            "SELECT",
            "sar.alarm_status AS groupKey,",
            "COUNT(1) AS count",
            "FROM safety_alarm_record sar",
            "LEFT JOIN market_config mc ON mc.market_tenant = sar.market_tenant",
            "<where>",
            AlarmMetricQueryFilterSQL,
            "</where>",
            "GROUP BY sar.alarm_status",
            "</script>"
    }
    )
    List<GroupCountVO> countStatus(@Param("query") AlarmMetricQuery query);

    @Select({
            "<script>",
            "SELECT",
            "       COUNT(1) AS total," +
            "SUM(timeout_config_enabled) AS timeoutConfigEnabled," +
            "SUM(timeout) AS timeout",
            "FROM",
            "(",
            "SELECT",
            "IF(t.timeout_config_enabled AND t.handler_time > t.timeout_end, 1, 0) AS timeout,",
            "t.timeout_config_enabled AS timeout_config_enabled",
            "FROM",
            "(",
            "SELECT " +
            "IF(sar.timeout_process_time IS NULL, CURRENT_TIMESTAMP, sar.timeout_process_time) AS timeout_end," +
            "IF(sar.alarm_handler_time IS NULL, CURRENT_TIMESTAMP, sar.alarm_handler_time) AS handler_time," +
            "sar.timeout_equipped AS timeout_config_enabled," +
            "sar.market_tenant," +
            "sar.application_code," +
            "sar.create_time " +
            "FROM safety_alarm_record sar",
            "LEFT JOIN market_config mc ON mc.market_tenant = sar.market_tenant",
            "<where>",
            AlarmMetricQueryFilterSQL,
            "</where>",
            ") t",
            ") a",
            "</script>"
    }
    )
    AlarmTimeoutCountVO countTimeout(@Param("query") AlarmMetricQuery query);

    @Select({
            "<script>",
            "SELECT",
            "sar.market_tenant AS subGroupKey,",
            "mc.market_name AS subGroupName,",
            "sar.alarm_status AS groupKey,",
            "COUNT(1) AS count",
            "FROM safety_alarm_record sar",
            "LEFT JOIN market_config mc ON mc.market_tenant = sar.market_tenant",
            "<where>",
            AlarmMetricQueryFilterSQL,
            "</where>",
            "GROUP BY sar.market_tenant, sar.alarm_status",
            "</script>"
    }
    )
    List<SubGroupCountVO> countStatusGroupByMarket(@Param("query") AlarmMetricQuery query);

    @Select({
            "<script>",
            "SELECT",
            "*",
            "FROM safety_alarm_record sar",
            "LEFT JOIN market_config mc ON mc.market_tenant = sar.market_tenant",
            "<where>",
            "${ew.customSqlSegment}",
            AlarmMetricQueryFilterSQL,
            "</where>",
            "</script>"
    }
    )
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 100)
    @ResultType(SafetyAlarmRecordPO.class)
    void streamQuery(@Param(Constants.WRAPPER) QueryWrapper<SafetyAlarmRecordPO> wrapper,
                     @Param("query") AlarmMetricQuery query,
                     ResultHandler<SafetyAlarmRecordPO> handler
                    );
}
