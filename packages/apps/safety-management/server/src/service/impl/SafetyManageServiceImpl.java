package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountPropertyFilingNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountTaskNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.LooseWorksCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyMarketStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyWorkStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmChainVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenFinishedStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.inspect.InspectCompleteStatVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.patrol.PatrolCompleteStatVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.HiddenDangerStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.InspectionTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.PatrolTaskStateEnum;
import com.guhecloud.cube.srv.gen.safety.management.service.EmergencyFileStatisticService;
import com.guhecloud.cube.srv.gen.safety.management.service.HiddenDangerStatisticService;
import com.guhecloud.cube.srv.gen.safety.management.service.InspectionStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.PatrolStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.PropertyStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyAlarmStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyEducationStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyManageService;
import com.guhecloud.cube.srv.gen.safety.management.util.CommonUtil;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.MathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author wql
 * @describe 安全管理
 * @date 2023/9/7 19:54
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyManageServiceImpl implements SafetyManageService {
    private final HiddenDangerStatisticService hiddenDangerStatisticService;
    private final InspectionStatisticsService inspectionStatisticsService;
    private final PatrolStatisticsService patrolStatisticsService;
    private final SafetyEducationStatisticsService safetyEducationStatisticsService;
    private final SafetyAlarmStatisticsService safetyAlarmStatisticsService;
    private final PropertyStatisticsService propertyStatisticsService;
    private final LooseWorksStatisticServiceImpl looseWorksStatisticService;
    private final EmergencyFileStatisticService emergencyFileStatisticService;

    @Override
    public SafetyWorkStatusVO getSafetyWorkStatus(BaseQuery query) {
        Assert.isParameterError(query.getPeriod() == null, "查询时间范围不能为空!");
        LooseWorksCountVO vo = looseWorksStatisticService.countInfo(query);
        AlarmSituationVO alarmSituation = safetyAlarmStatisticsService.getAlarmSituation(AlarmStatisticsQuery.builder()
                                                                                                 .period(query.getPeriod())
                                                                                                 .marketTenant(query.getMarketTenant())
                                                                                                 .build());
        HiddenFinishedStatisticsVO hiddenFinishedStat = hiddenDangerStatisticService.getHiddenFinishedStat(query);
        InspectCompleteStatVO completeStat = inspectionStatisticsService.getCompleteStat(query);
        PatrolCompleteStatVO taskCompleteStat = patrolStatisticsService.getTaskCompleteStat(query);
        return SafetyWorkStatusVO.builder()
                //报警处理率
                .alarmDisposeNum(alarmSituation.getHandlerNum())
                .alarmDispose(alarmSituation.getHandlerRatio().doubleValue())
                //隐患处理率
                .hiddenDangerDisposeNum(hiddenFinishedStat.getTotalNum())
                .hiddenDangerDispose(hiddenFinishedStat.getRatio())
                //巡检任务完成率
                .inspectCompleteNum(completeStat.getTotalNum())
                .inspectComplete(completeStat.getRatio())
                //巡更任务完成率
                .patrolCompleteNum(taskCompleteStat.getTotalNum())
                .patrolComplete(taskCompleteStat.getRatio())
                .projectCompleteNum(vo.getCompletedNum())
                //小散工程异常率
                .projectError(CommonUtil.countProportion(BigDecimal.valueOf(vo.getTotal()), BigDecimal.valueOf(vo.getAbnormal())))
                //教育任务占比情况
                .educationProportion(safetyEducationStatisticsService.getSafetyEducationTaskRatio(SafetyEducationStatisticsQuery.builder()
                                                                                                          .period(query.getPeriod())
                                                                                                          .marketTenant(query.getMarketTenant())
                                                                                                          .build()).getRatio()
                                             .doubleValue())
                .build();
    }

    @Override
    public SafetyStatisticsCardVO getSafetyCardStatistics(BaseQuery query) {
        Integer hiddenDangerNum = hiddenDangerStatisticService.countTaskNum(
                CountTaskNumQuery.builder()
                        .status(Arrays.asList(HiddenDangerStatusEnum.WAITING_CONFIRM.name(), HiddenDangerStatusEnum.CONFIRMED.name()))
                        .period(query.getPeriod())
                        .build()
                                                                           );
        Integer inspectionNum = inspectionStatisticsService.countTaskNum(
                CountTaskNumQuery.builder()
                        .status(Arrays.asList(InspectionTaskStatusEnum.ACCEPT.name(), InspectionTaskStatusEnum.SPLIT.name(), InspectionTaskStatusEnum.EXECUTING.name()))
                        .period(query.getPeriod())
                        .build()
                                                                        );
        Integer patrolNum = patrolStatisticsService.countTaskNum(
                CountTaskNumQuery.builder()
                        .status(Arrays.asList(PatrolTaskStateEnum.TO_BE_RECEIVED.name(), PatrolTaskStateEnum.IN_EXECUTION.name()))
                        .period(query.getPeriod())
                        .build()
                                                                );
        return SafetyStatisticsCardVO.builder()
                //进行中的小散工程数
                .projectNum(looseWorksStatisticService.countUnderway(query))
                //未处理隐患数
                .hiddenDangerNum(hiddenDangerNum)
                //未完成的安全任务数
                .safetyTaskNum(inspectionNum + patrolNum)
                .build();
    }

    @Override
    public SafetyMarketStatisticsCardVO getSafetyMarketCardStatistics(BaseQuery cardQuery) {
        //报警任务数
        AlarmStatisticsQuery query = AlarmStatisticsQuery.builder().marketTenant(cardQuery.getMarketTenant()).period(cardQuery.getPeriod())
                .build();
        Integer alarmNum = safetyAlarmStatisticsService.getAlarmSituation(query).getTotalNum();
        //隐患任务数
        CountTaskNumQuery numQuery = CountTaskNumQuery.builder().marketTenant(cardQuery.getMarketTenant()).period(cardQuery.getPeriod())
                .build();
        Integer hiddenNum = hiddenDangerStatisticService.countTaskNum(numQuery);
        //巡检任务数
        Integer inspectNum = inspectionStatisticsService.countTaskNum(numQuery);
        //巡更任务数
        Integer patrolNum = patrolStatisticsService.countTaskNum(numQuery);
        //安全教育任务数
        SafetyEducationStatisticsQuery build = SafetyEducationStatisticsQuery.builder().marketTenant(cardQuery.getMarketTenant())
                .period(cardQuery.getPeriod()).build();
        Integer safetyEducationNum = safetyEducationStatisticsService.getSafetyEducationTaskRatio(build).getPeriodTotalNum();
        //小散工程数
        Integer projectNum = looseWorksStatisticService.countInfo(cardQuery).getTotal();
        //预案文件数
        cardQuery.setPeriod(null);
        Integer planFileNum = emergencyFileStatisticService.countNum(cardQuery);
        //设备建档数
        CountPropertyFilingNumQuery propertyQuery = CountPropertyFilingNumQuery.builder().marketTenant(cardQuery.getMarketTenant()).build();
        Integer deviceFilingNum = propertyStatisticsService.getDeviceFilingNum(propertyQuery);
        return SafetyMarketStatisticsCardVO.builder()
                .alarmTaskNum(alarmNum)
                .hiddenTaskNum(hiddenNum)
                .inspectTaskNum(inspectNum)
                .patrolTaskNum(patrolNum)
                .educationTaskNum(safetyEducationNum)
                .projectNum(projectNum)
                .planFileNum(planFileNum)
                .deviceFilingNum(deviceFilingNum)
                .safetyTaskNum(alarmNum + hiddenNum + inspectNum + patrolNum + safetyEducationNum)
                .build();
    }

    @Override
    public AppSafetyWorkSituationVO getAppSafetyWorkSituation(BaseQuery query) {
        //报警总数
        AlarmStatisticsQuery alarmQuery = AlarmStatisticsQuery.builder().marketTenant(query.getMarketTenant()).period(query.getPeriod())
                .build();
        Integer alarmCount = this.safetyAlarmStatisticsService.getAlarmCount(alarmQuery);
        //报警响应环比统计信息
        AlarmChainVO statusChainRatio = this.safetyAlarmStatisticsService.getStatusChainRatio(alarmQuery, null, AlarmStatusEnum.ALARM_UN_CONFIRMED);
        //隐患处理情况统计
        AppSafetyWorkSituationVO.WorkSituationVO handlerSituation = this.hiddenDangerStatisticService.getHandlerSituation(query);
        //预案文件总数
        Integer emergencyFileTotal = this.emergencyFileStatisticService.countNum(query);
        //安全教育统计
        AppSafetyWorkSituationVO.WorkSituationVO safetyEducationSituation = this.safetyEducationStatisticsService.getSafetyEducationSituation(query);
        //风险源建档统计
        AppSafetyWorkSituationVO.WorkSituationVO propertyFilingRatioByMarket = this.propertyStatisticsService.getPropertyFilingRatioByMarket();
        //小散工程统计
        AppSafetyWorkSituationVO.WorkSituationVO looseWorksAbnormalSituation = this.looseWorksStatisticService.getLooseWorksAbnormalSituation(query);
        return AppSafetyWorkSituationVO.builder()
                .alarmDispose(AppSafetyWorkSituationVO.WorkSituationVO.builder()
                                      .totalNum(statusChainRatio.getTotalNum())
                                      .ratio(MathUtils.calculateProportionPercentage(statusChainRatio.getTotalNum(), alarmCount))
                                      .chainRatio(statusChainRatio.getChainRatio())
                                      .build())
                .hiddenDangerDispose(handlerSituation)
                .emergencyFile(AppSafetyWorkSituationVO.WorkSituationVO.builder()
                                       .totalNum(emergencyFileTotal)
                                       .build())
                .educationFrequency(safetyEducationSituation)
                .riskSourcesFillDispose(propertyFilingRatioByMarket)
                .projectError(looseWorksAbnormalSituation)
                .build();
    }

}
