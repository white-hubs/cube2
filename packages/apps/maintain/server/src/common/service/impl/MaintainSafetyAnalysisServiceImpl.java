package com.guhecloud.cube.srv.gen.maintain.server.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.MaintainSafetyAnalysisService;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.MaintainSafetyAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.WorkSituationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolTaskStateEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl.PatrolTaskServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration.HiddenDangerStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerNew;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintainSafetyAnalysisServiceImpl implements MaintainSafetyAnalysisService {
    private final HiddenDangerNewService hiddenDangerNewService;
    private final InspectRecordMapper inspectRecordMapper;
    private final PatrolTaskServiceImpl patrolTaskService;

    @Override
    public MaintainSafetyAnalysisVO maintainSafetyAnalysis(Period period, String type) {
        return MaintainSafetyAnalysisVO.builder()
                .hiddenDangerDispose(this.hiddenDangerForSafetyAnalysis(period, type))
                .inspectFrequency(this.inspectAnalysisSafetyAnalysis(period, type, null))
                .patrolFrequency(this.patrolTaskSafetyAnalysis(period, type))
                .build();
    }

    @Override
    public WorkSituationVO hiddenDangerForSafetyAnalysis(Period period, String type) {
        long nowNum = hiddenDangerNewService.lambdaQuery()
                .between(HiddenDangerNew::getSubmitTime, period.getBegin(),
                         period.getEnd()).count();
        long finishedNum = hiddenDangerNewService.lambdaQuery()
                .between(HiddenDangerNew::getSubmitTime, period.getBegin(),
                         period.getEnd())
                .eq(HiddenDangerNew::getState, HiddenDangerStatusEnum.FINISHED.name())
                .count();
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(type, period);
        long ringNum = hiddenDangerNewService.lambdaQuery()
                .between(HiddenDangerNew::getSubmitTime, previousPeriod.getBegin(),
                         previousPeriod.getEnd())
                .eq(HiddenDangerNew::getState, HiddenDangerStatusEnum.FINISHED.name())
                .count();

        return WorkSituationVO.builder()
                .totalNum(Long.valueOf(finishedNum))
                .ratio(MathUtils.calculateProportionPercentage(finishedNum, nowNum).floatValue())
                .chainRatio(MathUtils.calculateRingRatioPercentage(finishedNum, ringNum).floatValue())
                .build();
    }

    @Override
    public WorkSituationVO inspectAnalysisSafetyAnalysis(Period period, String type, String result) {
        long totalNum = inspectRecordMapper.countInspectionNum(Wrappers.query()
                                                                       .eq(StringUtils.isNotBlank(result), "a.result", result)
                                                                       .between(ObjectUtils.isNotNull(period), "a.time", period.getBegin(), period.getEnd())
                                                              );
        //环比
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(type, period);
        long ringNum = inspectRecordMapper.countInspectionNum(Wrappers.query()
                                                                      .eq(StringUtils.isNotBlank(result), "a.result", result)
                                                                      .between(ObjectUtils.isNotNull(period), "a.time", previousPeriod.getBegin(), previousPeriod.getEnd())
                                                             );
        if (Objects.isNull(ringNum)) {
            ringNum = 0;
        }

        return WorkSituationVO.builder()
                .totalNum(Long.valueOf(totalNum))
                .chainRatio(MathUtils.calculateRingRatioPercentage(totalNum, ringNum).floatValue())
                .build();
    }

    @Override
    @DataPermissionSwitch(dataModel = "inspect:inspectRecord")
    public WorkSituationVO inspectAnalysisPermission(Period period, String type, String result) {
        return this.inspectAnalysisSafetyAnalysis(period, type, result);
    }

    @Override
    public WorkSituationVO patrolTaskSafetyAnalysis(Period period, String type) {
        long totalNum = patrolTaskService.lambdaQuery()
                .eq(PatrolTask::getStatus, PatrolTaskStateEnum.FINISHED.name())
                .between(PatrolTask::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin)
                        , ObjectUtils.ifNull(period, Period::getEnd))
                .count();
        //环比
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(type, period);
        long ringNum = patrolTaskService.lambdaQuery()
                .eq(PatrolTask::getStatus, PatrolTaskStateEnum.FINISHED.name())
                .between(PatrolTask::getCreateTime, previousPeriod.getBegin()
                        , previousPeriod.getEnd())
                .count();
        return WorkSituationVO.builder()
                .totalNum(Long.valueOf(totalNum))
                .chainRatio(MathUtils.calculateRingRatioPercentage(totalNum, ringNum).floatValue())
                .build();
    }
}
