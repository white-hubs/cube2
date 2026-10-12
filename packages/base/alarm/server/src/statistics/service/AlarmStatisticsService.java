package com.guhecloud.cube.base.alarm.server.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmBasicAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmCurrencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmEventStatusStatisticsPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmHandlerAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelHandlerUserEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelTrendStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmResultStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusTotalByUserPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTrendAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTrendStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTypeStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmUserHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBusinessKeyStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmDeviceStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatusUserTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTargetTypeStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTrendStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.ColdWaterAlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmApplicationStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmAvgHandlerEfficiencyVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceEventRecordVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceStatusStatisticsPageVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceStatusStatisticsTrendVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmLevelTopStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmRecordReportVO1;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmTargetTypeStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmUserHandlerRateTopVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface AlarmStatisticsService {

    /**
     * 报警数量
     *
     * @param param
     *
     * @return
     */
    Long getAlarmTotal(AlarmStatisticsQueryParam param);

    /**
     * 处理的报警数
     *
     * @param param
     *
     * @return
     */
    Long getAlarmHandlerTotal(AlarmStatisticsQueryParam param);

    /**
     * 按报警状态统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmStatusStatisticsVO> getAlarmStatusStatistics(AlarmStatisticsQueryParam param);

    /**
     * 按报警结果统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmResultStatisticsVO> getAlarmResultStatistics(AlarmStatisticsQueryParam param);

    /**
     * 按报警类型统计数量并排名
     *
     * @param param
     *
     * @return
     */
    List<AlarmTypeStatisticsVO> getAlarmTypeRankStatistics(AlarmStatisticsQueryParam param);

    /**
     * 报警趋势统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmTrendStatisticsVO> getAlarmTrendStatistics(AlarmTrendStatisticsQueryParam param);

    /**
     * 报警总数与已完成趋势统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmTrendStatisticsVO> getAlarmConfirmedTrendStatistics(AlarmTrendStatisticsQueryParam param);

    /**
     * 按报警对象类型统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmTypeStatisticsVO> getAlarmTargetTypeStatistics(AlarmStatisticsQueryParam param);

    /**
     * 按报警级别统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelStatisticsVO> getAlarmLevelStatistics(AlarmStatisticsQueryParam param);

    /**
     * 报警事件分组统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmCurrencyStatisticsVO> getAlarmEventGroupStatistics(AlarmStatisticsQueryParam param);

    /**
     * 报警事件分组统计top10
     *
     * @param param
     *
     * @return
     */
    List<AlarmCurrencyStatisticsVO> getAlarmEventGroupStatisticsTopTen(AlarmStatisticsQueryParam param);

    /**
     * 报警区域分组统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmCurrencyStatisticsVO> getAlarmAreaGroupStatistics(AlarmStatisticsQueryParam param);

    /**
     * 报警设备分组统计前10
     *
     * @param param
     *
     * @return
     */
    List<AlarmCurrencyStatisticsVO> getAlarmDeviceGroupTopTenStatistics(AlarmStatisticsQueryParam param);

    /**
     * 报警处理分析
     *
     * @param param
     *
     * @return
     */
    AlarmHandlerAnalysisStatisticsVO getAlarmHandlerAnalysisStatistics(AlarmStatisticsQueryParam param);

    /**
     * 报警级别处理效率统计
     *
     * @return
     */
    List<AlarmLevelHandlerEfficiencyStatisticsVO> getAlarmLevelHandlerEfficiencyStatistics(AlarmStatisticsQueryParam param);

    /**
     * 用户报警处理效率排名前10
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelHandlerUserEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyTopTenStatistics(
            AlarmStatisticsQueryParam param
                                                                                                   );

    /**
     * 用户报警处理效率排名倒数前10
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelHandlerUserEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyLastTopTenStatistics(
            AlarmStatisticsQueryParam param
                                                                                                       );

    /**
     * 设备报警基本情况统计计算环比
     *
     * @return
     */
    AlarmBasicAnalysisStatisticsVO getAlarmBasicAnalysisRingStatistics(AlarmStatisticsQueryParam param);

    /**
     * 报警趋势分析
     *
     * @param param
     *
     * @return
     */
    AlarmTrendAnalysisStatisticsVO getAlarmTrendAnalysisStatistics(AlarmStatisticsQueryParam param);

    /**
     * 报警对象类型统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmTargetTypeStatisticsVO> getAlarmTargetTypeStatistics(AlarmTargetTypeStatisticsQueryParam param);

    /**
     * 报警应用统计数量
     *
     * @param param
     *
     * @return
     */
    List<AlarmApplicationStatisticsVO> getApplicationGroupStatistics(AlarmBimStatisticsQueryParam param);

    /**
     * 冷冻水报警趋势分析
     *
     * @param param
     *
     * @return
     */
    List<AlarmDeviceStatusStatisticsTrendVO> getColdWaterAlarmTrendAnalysisStatistics(AlarmStatisticsQueryParam param);

    /**
     * 按报警级别进行趋势统计
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelTrendStatisticsVO> getAlarmLevelTrendStatistics(AlarmTrendStatisticsQueryParam param);

    /**
     * 业务主键获取实际报警数量
     *
     * @param param
     *
     * @return
     */
    Long getBusinessKeyActualTotal(AlarmBusinessKeyStatisticsQueryParam param);

    /**
     * 报警事件按状态统计分页列表
     *
     * @param param
     *
     * @return
     */
    Page<AlarmEventStatusStatisticsPageVO> getAlarmStatusTotalPage(AlarmStatusTotalQueryParam param);

    /**
     * 报警设备按状态统计分页列表
     *
     * @param param
     *
     * @return
     */
    Page<AlarmDeviceStatusStatisticsPageVO> getDeviceAlarmStatusTotalPage(AlarmDeviceStatusTotalQueryParam param);

    /**
     * 报警设备按状态统计列表
     *
     * @param param
     *
     * @return
     */
    List<AlarmEventStatusStatisticsPageVO> getAlarmStatusTotalList(AlarmStatusTotalQueryParam param);

    /**
     * 报警事件按状态统计列表
     *
     * @param param
     *
     * @return
     */
    List<AlarmDeviceStatusStatisticsPageVO> getDeviceAlarmStatusTotalList(AlarmDeviceStatusTotalQueryParam param);

    /**
     * 按用户分组统计报警处理状态分页列表
     *
     * @param param
     *
     * @return
     */
    Page<AlarmStatusTotalByUserPageVO> getAlarmStatusTotalByUserPage(AlarmStatusUserTotalQueryParam param);

    /**
     * 按用户分组统计报警处理状态列表
     *
     * @param param
     *
     * @return
     */
    List<AlarmStatusTotalByUserPageVO> getAlarmStatusTotalByUserList(AlarmStatusUserTotalQueryParam param);

    //冷冻水报警事件记录
    Pagination<AlarmDeviceEventRecordVO> getColdWaterAlarmEventRecord(ColdWaterAlarmRecordQueryParam param);

    AlarmRecordReportVO1 getAlarmRecordByApplication(AlarmStatisticsQueryParam param);

    Page<AlarmStatusTotalByUserPageVO> getAlarmRecordForSafetyReport(AlarmStatusUserTotalQueryParam param);

    Page<AlarmStatusTotalByUserPageVO> getAlarmRecordForSafetyReportByDepartment(AlarmStatusUserTotalQueryParam param);

    void getAlarmRecordForSafetyReportByDepartmentExport(AlarmStatusUserTotalQueryParam param);

    void getAlarmRecordForSafetyReportExport(AlarmStatusUserTotalQueryParam param);

    List<AlarmLevelStatisticsVO> getAlarmLevelHandlerRateStatistics(AlarmStatisticsQueryParam param);

    /**
     * 报警平均处理时长
     *
     * @param param
     *
     * @return
     */
    AlarmAvgHandlerEfficiencyVO getAlarmAvgHandlerEfficiency(AlarmStatisticsQueryParam param);

    /**
     * 报警级别处理效率统计
     *
     * @return
     */
    List<AlarmLevelHandlerEfficiencyStatisticsVO> getAlarmLevelHandlerEfficiency(AlarmStatisticsQueryParam param);

    /**
     * 用户报警处理率排名
     *
     * @param param
     *
     * @return
     */
    List<AlarmUserHandlerRateTopVO> getAlarmUserHandlerRateTopStatistics(AlarmStatisticsQueryParam param);

    /**
     * 用户报警处理率倒数排名
     *
     * @param param
     *
     * @return
     */
    List<AlarmUserHandlerRateTopVO> getAlarmUserHandlerRateLastTopStatistics(AlarmStatisticsQueryParam param);

    /**
     * 用户报警处理效率排名
     *
     * @param param
     *
     * @return
     */
    List<AlarmUserHandlerEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyTopStatistics(AlarmStatisticsQueryParam param);

    /**
     * 用户报警处理效率排名
     *
     * @param param
     *
     * @return
     */
    List<AlarmUserHandlerEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyLastTopStatistics(AlarmStatisticsQueryParam param);

    /**
     * 报警级别统计数量
     *
     * @param param
     *
     * @return
     */
    AlarmLevelTopStatisticsVO getAlarmLevelTopStatistics(AlarmStatisticsQueryParam param);
}
