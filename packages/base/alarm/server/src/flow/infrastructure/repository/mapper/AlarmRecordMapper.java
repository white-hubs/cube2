package com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordPageNumberQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordStandardQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.bo.UnConfirmedCountByStatusBO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmAboutToTimeoutVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmCurrencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmEventStatusStatisticsPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmHandlerCurrencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelHandlerUserEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelTrendStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordExportVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmResultStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusTotalByUserPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTrendStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTypeStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmUserHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.riskscore.dto.AlarmRiskIndexDTO;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBusinessKeyStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmDeviceStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmRecordAreaCountParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatusUserTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTargetTypeStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTrendStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.ColdWaterAlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmApplicationStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimApplicationStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimDeviceStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimHandlerResultStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimSituationVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsDeviceTypeVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsEventGroupVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsHandlerVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsRecordStatusVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsTopChainVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceEventRecordVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceStatusStatisticsPageVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceStatusStatisticsTrendVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmRecordReportVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmTargetTypeStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmUserHandlerRateTopVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmUserHandlerTimeoutRateStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AlarmRecordMapper extends BaseMapper<AlarmRecordPO> {

    /**
     * 设置报警业务快照
     *
     * @param id
     * @param businessSnapshot
     */
    void settingAlarmRecordBusinessSnapshot(@Param("id") Long id, @Param("businessSnapshot") String businessSnapshot);

    /**
     * 获取报警租户
     *
     * @return
     */
    List<Integer> selectAlarmTenant();

    /**
     * 按用户查询未处理的报警数量
     *
     * @param userId
     * @param terminal
     *
     * @return
     */
    Long selectUserUnConfirmedCount(@Param("userId") Long userId, @Param("terminal") String terminal);

    /**
     * 报警记录分页列表
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<AlarmRecordPageVO> selectAlarmPage(Page page, @Param("param") AlarmRecordQueryParam param);

    /**
     * 标准报警记录列表
     *
     * @param query
     * @param tenant
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<AlarmRecordPageVO> selectStandardAlarmRecords(@Param("param") AlarmRecordStandardQueryParam query, @Param("tenant") Integer tenant);

    /**
     * 报警记录未处理列表
     *
     * @param param
     *
     * @return
     */
    Page<AlarmRecordPageVO> selectUnHandlerList(@Param("param") AlarmRecordQueryParam param, Page page);

    /**
     * 报警导出列表
     *
     * @param param
     *
     * @return
     */
    List<AlarmRecordExportVO> selectExportList(@Param("param") AlarmRecordQueryParam param);

    /**
     * 按用户查询未处理的报警分页列表
     *
     * @param userId
     *
     * @return
     */
    Page<AlarmRecordPO> selectUserUnConfirmedAlarmRecords(Page page, @Param("userId") Long userId,
                                                          @Param("param") AlarmRecordQueryParam param
                                                         );

    /**
     * 按用户查询未处理的报警列表
     *
     * @param userId
     * @param param
     *
     * @return
     */
    List<AlarmRecordPO> selectUserUnConfirmedAlarmRecordList(@Param("userId") Long userId,
                                                             @Param("param") AlarmRecordQueryParam param
                                                            );

    /**
     * 一键处理报警
     *
     * @param userId
     * @param handlerDate
     */
    void oneClickHandlerAlarmRecords(@Param("userId") Long userId, @Param("userName") String userName,
                                     @Param("handlerDate") LocalDateTime handlerDate, @Param("alarmRecordIds") List<Long> alarmRecordIds
                                    );

    /**
     * 获取报警数量
     *
     * @param param
     *
     * @return
     */
    Long selectAlarmTotal(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 处理的报警数
     *
     * @param param
     *
     * @return
     */
    Long selectAlarmHandlerTotal(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 处理的报警数的总时长
     *
     * @param param
     *
     * @return
     */
    Long selectAlarmHandlerDurationTotal(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 处理的报警最大时长
     *
     * @param param
     *
     * @return
     */
    Long selectAlarmHandlerMaxDuration(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 处理的报警最小时长
     *
     * @param param
     *
     * @return
     */
    Long selectAlarmHandlerMinDuration(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 按报警状态统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmStatusStatisticsVO> selectAlarmStatusStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 查询超时未处理报警数量
     *
     * @param param
     *
     * @return
     */
    Long selectAlarmTimeoutUnHandlerCount(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 按报警结果统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmResultStatisticsVO> selectAlarmResultStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 按报警类型统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmTypeStatisticsVO> selectAlarmTypeStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 报警趋势统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmTrendStatisticsVO> selectAlarmTrendStatistics(@Param("param") AlarmTrendStatisticsQueryParam param);

    /**
     * 报警总数与已完成趋势统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmTrendStatisticsVO> selectAlarmConfirmedTrendStatistics(@Param("param") AlarmTrendStatisticsQueryParam param);

    /**
     * 按报警对象类型统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmTypeStatisticsVO> selectAlarmTargetTypeStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 按报警级别统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelStatisticsVO> selectAlarmLevelStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * @Description 设备类型统计
     * @Author lrj
     */
    List<AlarmBimStatisticsDeviceTypeVO> getAlarmBimStatisticsDeviceType(@Param("param") AlarmStatisticsQueryParam param);

    List<AlarmBimStatisticsDeviceTypeVO> getAlarmBimStatisticsPreviousDeviceType(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 报警事件分组统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmCurrencyStatisticsVO> selectAlarmEventAnalysisStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 报警事件分组统计top10
     *
     * @param param
     *
     * @return
     */
    List<AlarmCurrencyStatisticsVO> selectAlarmEventAnalysisStatisticsTop(@Param("param") AlarmStatisticsQueryParam param,
                                                                          @Param("topSize") Integer topSize
                                                                         );

    /**
     * 报警区域分组统计
     *
     * @return
     */
    List<AlarmCurrencyStatisticsVO> selectAlarmAreaAnalysisStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 报警设备TOP10
     *
     * @param param
     *
     * @return
     */
    List<AlarmCurrencyStatisticsVO> selectAlarmDeviceGroupTopStatistics(@Param("param") AlarmStatisticsQueryParam param,
                                                                        @Param("topSize") Integer topSize
                                                                       );

    /**
     * 报警级别处理效率统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelHandlerUserEfficiencyStatisticsVO> selectAlarmLevelHandlerUserEfficiencyStatistics(
            @Param("param") AlarmStatisticsQueryParam param
                                                                                                     );

    /**
     * 用户处理效率统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmUserHandlerEfficiencyStatisticsVO> selectAlarmUserHandlerUserEfficiencyStatistics(
            @Param("param") AlarmStatisticsQueryParam param
                                                                                               );

    /**
     * 报警处理分析
     *
     * @param param
     *
     * @return
     */
    List<AlarmHandlerCurrencyStatisticsVO> selectAlarmHandlerAnalysisStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 报警趋势分析
     *
     * @param param
     *
     * @return
     */
    List<AlarmTrendStatisticsVO> selectAlarmTrendAnalysisStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 报警级别趋势统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelTrendStatisticsVO> selectAlarmLevelTrendStatistics(@Param("param") AlarmTrendStatisticsQueryParam param);

    /**
     * 业务主键获取实际报警数量
     *
     * @param param
     *
     * @return
     */
    Long selectBusinessKeyActualTotal(@Param("param") AlarmBusinessKeyStatisticsQueryParam param);

    /**
     * 报警事件状态分组统计分页列表
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<AlarmEventStatusStatisticsPageVO> selectAlarmStatusTotalPage(Page page, @Param("param") AlarmStatusTotalQueryParam param
                                                                     );

    /**
     * 报警设备状态分组统计分页列表
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<AlarmDeviceStatusStatisticsPageVO> selectDeviceAlarmStatusTotalPage(Page page, @Param("param")
                                                                             AlarmDeviceStatusTotalQueryParam param
                                                                            );

    /**
     * 报警设备状态分组统计列表导表
     *
     * @param
     * @param param
     *
     * @return
     */
    List<AlarmDeviceStatusStatisticsPageVO> selectDeviceAlarmStatusTotalList(
            @Param("param") AlarmDeviceStatusTotalQueryParam param
                                                                            );

    /**
     * 报警事件状态分组统计列表
     *
     * @param param
     *
     * @return
     */
    List<AlarmEventStatusStatisticsPageVO> selectAlarmStatusTotalList(@Param("param") AlarmStatusTotalQueryParam param);

    /**
     * 按用户报警状态分组统计分页列表
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<AlarmStatusTotalByUserPageVO> selectAlarmStatusTotalByUserPage(Page page,
                                                                        @Param("param") AlarmStatusUserTotalQueryParam param
                                                                       );

    /**
     * 按用户报警状态分组统计列表
     *
     * @param param
     *
     * @return
     */
    List<AlarmStatusTotalByUserPageVO> selectAlarmStatusTotalByUserList(@Param("param") AlarmStatusUserTotalQueryParam param);

    /**
     * 部门分组统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsTopChainVO> selectAlarmDepartmentCount(@Param("param") AlarmStatusUserTotalQueryParam param);

    /**
     * 获取当前用户任务类型数据统计
     */
    List<UnConfirmedCountByStatusBO> getUserUnConfirmedCountByStatus(@Param("param") AlarmRecordQueryParam param);

    /**
     * 报警统计
     * 报警处理数量,报警真实数量
     *
     * @param param
     *
     * @return
     */
    AlarmBimStatisticsVO selectAlarmBimStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 报警记录状态统计(报警数,未处理数,超时报警数)
     *
     * @param param
     *
     * @return
     */
    AlarmBimStatisticsRecordStatusVO selectAlarmBimStatisticsRecordStatus(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 报警处理结果数量统计
     *
     * @param param
     *
     * @return
     */
    AlarmBimHandlerResultStatisticsVO selectAlarmBimHandlerResultStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 获取设备报警数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmCurrencyStatisticsVO> selectDeviceAlarmTotal(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 获取报警对象数量
     *
     * @param param
     *
     * @return
     */
    Integer selectAlarmTargetCount(@Param("param") AlarmStatisticsQueryParam param);

    Integer getAlarmAreaCount(@Param("param") AlarmRecordAreaCountParam param);

    /**
     * 范围内超时报警列表
     *
     * @param beginDate
     * @param endDate
     *
     * @return
     */
    List<AlarmAboutToTimeoutVO> selectAboutToTimeoutAlarmList(@Param("beginDate") LocalDateTime beginDate,
                                                              @Param("endDate") LocalDateTime endDate,
                                                              @Param("applicationCode") String applicationCode
                                                             );

    /**
     * 范围内未接收的报警
     *
     * @param beginDate
     * @param endDate
     *
     * @return
     */
    List<AlarmRecordPO> selectAboutToUnConfirmedAlarmList(@Param("beginDate") LocalDateTime beginDate,
                                                          @Param("endDate") LocalDateTime endDate
                                                         );

    /**
     * 即将超时的报警列表
     *
     * @param beginDate
     * @param endDate
     *
     * @return
     */
    List<AlarmRecordPO> selectImmediatelyTimeoutAlarmList(@Param("beginDate") LocalDateTime beginDate,
                                                          @Param("endDate") LocalDateTime endDate
                                                         );

    List<AlarmDeviceStatusStatisticsTrendVO> getColdWaterAlarmTrendAnalysisStatistics(
            @Param("param") AlarmStatisticsQueryParam param, @Param("dateFormat") String dateFormat
                                                                                     );

    Page<AlarmDeviceEventRecordVO> getColdWaterAlarmEventRecord(Page page, @Param("param") ColdWaterAlarmRecordQueryParam param);

    /**
     * 设备报警分组统计
     *
     * @return
     */
    List<AlarmBimDeviceStatisticsVO> getDeviceGroupStatistics(@Param("param") AlarmBimStatisticsQueryParam param);

    /**
     * 设备报警分组统计
     *
     * @return
     */
    List<AlarmBimDeviceStatisticsVO> getTimeGroupStatistics(@Param("param") AlarmBimStatisticsQueryParam param);

    /**
     * 未处理的报警记录行数
     *
     * @param param
     *
     * @return
     */
    Integer selectUnHandlerRowNumber(@Param("param") AlarmRecordPageNumberQueryParam param);

    //应用分组统计
    List<AlarmBimApplicationStatisticsVO> getApplicationGroupStatistics(@Param("param") AlarmBimStatisticsQueryParam param);

    /**
     * 应用分组总数统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmApplicationStatisticsVO> getApplicationGroupTotalStatistics(@Param("param") AlarmBimStatisticsQueryParam param);

    /**
     * 报警对象类型统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmTargetTypeStatisticsVO> selectInnerAlarmTargetTypeStatistics(
            @Param("param") AlarmTargetTypeStatisticsQueryParam param
                                                                          );

    /**
     * 报警应用
     *
     * @return
     */
    List<String> selectAlarmApplicationCode();

    /**
     * 部门排名统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsTopChainVO> selectDeptChainTopStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 事件分类处理列表统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsHandlerVO> selectEventTypeHandlerListStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 事件分类总数列表统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsHandlerVO> selectEventTypeListStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 报警情况统计
     *
     * @param param
     *
     * @return
     */
    AlarmBimSituationVO selectAlarmSituation(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 报警事件分类分组数量统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmBimStatisticsEventGroupVO> selectEventTypeGroupTotalStatistics(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 获取安全风险指数计算的报警记录
     *
     * @param period
     *
     * @return
     */
    List<AlarmRiskIndexDTO> selectRiskIndexAlarmRecord(@Param("period") Period period);

    /**
     * 获取指定应用报警数
     *
     * @param param
     *
     * @return
     */
    List<AlarmRecordReportVO> selectAlarmRecordByApplication(@Param("param") AlarmStatisticsQueryParam param);

    List<AlarmRecordReportVO> selectAlarmRecordByApplicationForDepartment(@Param("param") AlarmStatisticsQueryParam param);

    List<AlarmRecordReportVO> selectAlarmRecordByApplicationForUser(@Param("param") AlarmStatisticsQueryParam param);

    List<AlarmRecordReportVO> selectAlarmRecordByApplicationGroupDepartment(@Param("param") AlarmStatisticsQueryParam param);

    /**
     * 环比、同比数
     *
     * @param param
     *
     * @return
     */
    List<AlarmRecordReportVO> selectAlarmRecordRingAndSameByApplication(@Param("param") AlarmStatisticsQueryParam param);

    Page<AlarmStatusTotalByUserPageVO> selectAlarmRecordForSafetyReport(@Param("page") Page page,
                                                                        @Param("param") AlarmStatusUserTotalQueryParam param
                                                                       );

    List<AlarmStatusTotalByUserPageVO> selectAlarmRecordForSafetyReport(@Param("param") AlarmStatusUserTotalQueryParam param);

    Long selectAlarmRecordCountForSafetyReport(@Param("param") AlarmStatusUserTotalQueryParam param);

    Page<AlarmStatusTotalByUserPageVO> selectAlarmRecordForSafetyReportByDepartment(@Param("page") Page page, @Param("param")
                                                                                    AlarmStatusUserTotalQueryParam param
                                                                                   );

    List<AlarmStatusTotalByUserPageVO> selectAlarmRecordForSafetyReportByDepartment(
            @Param("param") AlarmStatusUserTotalQueryParam param
                                                                                   );

    List<AlarmLevelStatisticsVO> selectAlarmLevelHandlerRateStatistics(@Param("param") AlarmStatisticsQueryParam param);

    List<AlarmLevelHandlerEfficiencyStatisticsVO> selectAlarmLevelHandlerEfficiency(@Param("param") AlarmStatisticsQueryParam param);

    List<AlarmUserHandlerRateTopVO> selectUserAlarmNoHandlerStatistics(@Param("param") AlarmStatisticsQueryParam param);

    List<AlarmUserHandlerRateTopVO> selectUserAlarmHandlerStatistics(@Param("param") AlarmStatisticsQueryParam param);

    AlarmHandlerTimeoutRateStatisticsVO selectAlarmHandlerTimeoutRate(@Param("param") AlarmStatisticsQueryParam param);

    List<AlarmUserHandlerTimeoutRateStatisticsVO> selectAlarmUserHandlerTimeoutRate(@Param("param") AlarmStatisticsQueryParam param);

    List<AlarmHandlerTimeoutRateStatisticsVO> selectAlarmLevelHandlerTimeoutRate(@Param("param") AlarmStatisticsQueryParam param);

}
