package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.MaintainSafetyAnalysisService;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.WorkSituationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PatrolResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.CommonUtil;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectionAnalysisService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectTargetInfoQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectPlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectionAnalysisDataEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.PlanExecStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.TaskResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.BaseInspectStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionAnalysisParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionItemDetailsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionNumParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskDetailParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.AnalysisOfAuditResultsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CheckResultMatchVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepInspectionTaskInfoVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepTaskExecutionAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.HomepageStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionFrequencyAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionIObjDetailsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionItemDetailsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjAbnormalAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjTaskAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionPlanAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionRecordAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskUserAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ObjInspectionInfoVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ProportionOfPlanTypesVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.SafetyReportResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TargetCategoryDetailsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TargetCategoryVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskExecutionAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskTimeAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TrendOfInspectionTimesVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 巡检分析
 * @date 2023/4/25 19:11
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("OptionalOfRedundantMethod")
public class InspectionAnalysisServiceImpl implements InspectionAnalysisService {
    private final InspectTaskMapper inspectTaskMapper;
    private final InspectRecordMapper inspectRecordMapper;
    private final InspectPlanMapper inspectPlanMapper;
    private final InspectTargetInfoQuery inspectTargetInfoQuery;
    private final StatisticAnalysisServiceImpl statisticAnalysisService;
    private final UserClient userClient;
    private final TaskService taskService;
    private final MaintainSafetyAnalysisService maintainSafetyAnalysisService;
    private final FileClient fileClient;

    /**
     * @Author wql
     * @Description 巡检计划分析
     * @Date 19:13 2023/4/25
     **/
    @Override
    public InspectionPlanAnalysisVO inspectionPlanAnalysis(InspectionAnalysisParam analysisParam) {
        InspectionPlanAnalysisVO analysisVO = new InspectionPlanAnalysisVO();
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        Long count = inspectPlanMapper.selectCount(Wrappers.<InspectPlanPO>lambdaQuery()
                                                           .eq(InspectPlanPO::getStatus, PlanExecStatusEnum.EXECUTING.name())
                                                           .between(ObjectUtils.isNotNull(period), InspectPlanPO::getCreateTime, period.getBegin(), period.getEnd()));
        analysisVO.setPatrolSchemeTotal(count);
        List<ProportionOfPlanTypesVO> proportion = inspectPlanMapper.groupingStatistics(Wrappers.query()
                                                                                                .eq("status", PlanExecStatusEnum.EXECUTING.name())
                                                                                                .between(ObjectUtils.isNotNull(period), "create_time", period.getBegin(), period.getEnd())
                                                                                                .groupBy("type_id")
                                                                                                .orderByDesc("planNum"));
        List<ProportionOfPlanTypesVO> typesVOS = Optional.ofNullable(proportion).orElse(Collections.emptyList());
        List<ProportionOfPlanTypesVO> vos = new ArrayList<>();
        long top10Total = 0;
        for (int i = 0; i < typesVOS.size(); i++) {
            if (i < 10) {
                ProportionOfPlanTypesVO vo = typesVOS.get(i);
                top10Total += vo.getPlanNum();
                vo.setProportion(CommonUtil.calculateProportionPercentageAsStringLegacy(count, vo.getPlanNum()));
                vos.add(vo);
            } else {
                ProportionOfPlanTypesVO vo = new ProportionOfPlanTypesVO();
                vo.setPlanType("其他计划");
                vo.setPlanNum(count - top10Total);
                vo.setProportion(CommonUtil.calculateProportionPercentageAsStringLegacy(count, count - top10Total));
                vos.add(vo);
                break;
            }
        }
        analysisVO.setProportionOfPlanTypesVOS(vos);
        return analysisVO;
    }

    /**
     * @Author wql
     * @Description 巡检任务分析
     * @Date 19:38 2023/4/25
     **/
    @Override
    public InspectionTaskAnalysisVO inspectionTaskAnalysis(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        InspectionAnalysisParam oldParam = getOldInspectionAnalysisParam(analysisParam, period);
        InspectionTaskAnalysisVO vo = new InspectionTaskAnalysisVO();
        TaskDetailParam param1 = TaskDetailParam.builder().parentId(0L).period(period).build();
        TaskDetailParam param2 = TaskDetailParam.builder().parentId(0L).period(oldParam.getPeriod()).build();
        Long totalNum = statisticAnalysisService.countTaskNum(param1);
        vo.setTaskTotalNum(totalNum);
        Long oldTotalNum = statisticAnalysisService.countTaskNum(param2);
        vo.setTaskTotalNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldTotalNum, totalNum - oldTotalNum));

        param1.setState(TaskStatusEnum.OVER.name());
        param2.setState(TaskStatusEnum.OVER.name());
        Long completeNum = statisticAnalysisService.countTaskNum(param1);
        Long oldCompleteNum = statisticAnalysisService.countTaskNum(param2);
        vo.setCompleteTaskNum(completeNum);
        vo.setCompleteTaskNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldCompleteNum, completeNum - oldCompleteNum));
        vo.setCompleteTaskNumProportion(CommonUtil.calculateProportionPercentageAsStringLegacy(totalNum, completeNum));

        param1.setResult(TaskResultEnum.ABNORMAL.name());
        param2.setResult(TaskResultEnum.ABNORMAL.name());
        Long anomalyNum = statisticAnalysisService.countTaskNum(param1);
        Long oldAnomalyNum = statisticAnalysisService.countTaskNum(param2);
        vo.setAbnormalTaskNum(anomalyNum);
        vo.setAbnormalTaskNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldTotalNum, anomalyNum - oldAnomalyNum));
        vo.setAbnormalTaskNumProportion(CommonUtil.calculateProportionPercentageAsStringLegacy(totalNum, anomalyNum));
        param1.setResult(null);
        param2.setResult(null);

        param1.setState(TaskStatusEnum.EXPIRE.name());
        param2.setState(TaskStatusEnum.EXPIRE.name());
        Long expireNum = statisticAnalysisService.countTaskNum(param1);
        Long oldExpireNum = statisticAnalysisService.countTaskNum(param2);
        vo.setExpireTaskNum(expireNum);
        vo.setExpireTaskNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldExpireNum, expireNum - oldExpireNum));
        vo.setExpireTaskNumProportion(CommonUtil.calculateProportionPercentageAsStringLegacy(totalNum, expireNum));

        vo.setPeriodTaskNum(totalNum);
        Long total = statisticAnalysisService.countTaskNum();
        String ratio = CommonUtil.calculateProportionPercentageAsStringLegacy(total, totalNum);
        vo.setTaskNum(total);
        vo.setPeriodTaskNumProportion(ratio);

        return vo;
    }

    /**
     * @Author wql
     * @Description 巡检次数分析
     * @Date 19:45 2023/4/25
     **/
    @Override
    public InspectionFrequencyAnalysisVO inspectionFrequencyAnalysis(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        InspectionAnalysisParam oldParam = getOldInspectionAnalysisParam(analysisParam, period);
        TaskDetailParam param1 = TaskDetailParam.builder().parentId(0L).period(period).pageNumber(1).build();
        InspectionFrequencyAnalysisVO vo = new InspectionFrequencyAnalysisVO();
        vo.setTaskTotalNum(statisticAnalysisService.countTaskNum(param1));
        long objTotalNum = inspectTargetInfoQuery.getAllObjTotalNum();
        Long objNum = countObjNum(period);
        Long oldObjNum = countObjNum(oldParam.getPeriod());
        vo.setObjNum(objNum);
        vo.setObjCoverageRate(CommonUtil.calculateProportionPercentageAsStringLegacy(objTotalNum, objNum));
        vo.setObjCoverageRateRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldObjNum, objNum - oldObjNum));
        InspectionNumParam inspectionParam1 = InspectionNumParam.builder().period(period).build();
        InspectionNumParam inspectionParam2 = InspectionNumParam.builder().period(oldParam.getPeriod()).build();
        Long now = statisticAnalysisService.countInspectionNum(inspectionParam1);
        Long old = statisticAnalysisService.countInspectionNum(inspectionParam2);
        vo.setInspectionTotalNum(now);
        vo.setInspectionTotalNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(old, now - old));
        vo.setTrendOfInspectionTimesVOS(statisticsInspectionTrend(analysisParam, period));
        return vo;
    }

    /**
     * @Author wql
     * @Description 统计一段时间内巡检任务覆盖的对象数
     * @Date 18:24 2023/5/8
     **/
    private Long countObjNum(Period period) {
        List<InspectTaskPO> taskPOList = inspectTaskMapper.selectList(Wrappers.<InspectTaskPO>lambdaQuery()
                                                                              .select(InspectTaskPO::getTargetIds)
                                                                              .eq(InspectTaskPO::getParentId, 0)
                                                                              .between(ObjectUtils.isNotNull(period), InspectTaskPO::getCreateTime, period.getBegin(), period.getEnd())
                                                                     );
        if (CollectionUtils.isEmpty(taskPOList)) {
            return 0L;
        }
        List<String> targetIds = taskPOList.stream()
                .map(InspectTaskPO::getTargetIds)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Set<String> set = new HashSet<>();
        Optional.of(targetIds).orElse(Collections.emptyList()).forEach(ids -> {
            if (StringUtils.isNotBlank(ids)) {
                set.addAll(Arrays.asList(ids.split(",")));
            }
        });
        return (long) set.size();
    }

    /**
     * @Author wql
     * @Description 任务执行分析
     * @Date 19:53 2023/4/25
     **/
    @Override
    public TaskExecutionAnalysisVO taskExecutionAnalysis(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        InspectionAnalysisParam oldParam = getOldInspectionAnalysisParam(analysisParam, period);
        TaskExecutionAnalysisVO analyseVO = new TaskExecutionAnalysisVO();
        TaskDetailParam param = TaskDetailParam.builder().period(analysisParam.getPeriod()).build();
        Long totalNum = statisticAnalysisService.countTaskNum(param);
        analyseVO.setTotalTaskNum(totalNum);
        param.setState(TaskStatusEnum.OVERTIME.name());
        analyseVO.setOverTimeTaskNum(statisticAnalysisService.countTaskNum(param));
        param.setState(TaskStatusEnum.EXPIRE.name());
        analyseVO.setExpiredTaskNum(statisticAnalysisService.countTaskNum(param));
        analyseVO.setAbnormalTaskNum(inspectTaskMapper.countAbnormalTaskNum(Wrappers.query().
                                                                                    eq("b.result", InspectRecordResultEnum.ABNORMAL.name())
                                                                                    .between(ObjectUtils.isNotNull(period), "a.create_time", ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))));
        param.setPeriod(oldParam.getPeriod());
        Long oldTotalNum = statisticAnalysisService.countTaskNum(param);
        analyseVO.setTotalTaskNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldTotalNum, totalNum - oldTotalNum));
        return analyseVO;
    }

    /**
     * @Author wql
     * @Description 部门巡检任务分析
     * @Date 20:00 2023/4/25
     **/
    @Override
    public DepTaskExecutionAnalysisVO depTaskExecutionAnalysis(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        InspectionAnalysisParam oldParam = getOldInspectionAnalysisParam(analysisParam, period);
        DepTaskExecutionAnalysisVO analyseVO = new DepTaskExecutionAnalysisVO();
        List<DepInspectionTaskInfoVO> newDepTask = getDepTask(analysisParam);
        List<DepInspectionTaskInfoVO> oldDepTask = getDepTask(oldParam);
        //任务总耗时转天数显示
        newDepTask.parallelStream().forEach(vo -> {
            vo.setTaskTotalTimeStr(statisticAnalysisService.countTimeStr(vo.getTaskTotalTime()));
        });
        //按任务完成率排序
        newDepTask.sort((o1, o2) -> new BigDecimal(
                StringUtils.isBlank(o2.getTaskCompleteRatio()) ? "0" : o2.getTaskCompleteRatio().replace("%", ""))
                .compareTo(new BigDecimal(
                        StringUtils.isBlank(o1.getTaskCompleteRatio()) ? "" : o1.getTaskCompleteRatio().replace("%", ""))));
        oldDepTask.sort((o1, o2) -> new BigDecimal(
                StringUtils.isBlank(o2.getTaskCompleteRatio()) ? "0" : o2.getTaskCompleteRatio().replace("%", ""))
                .compareTo(new BigDecimal(
                        StringUtils.isBlank(o1.getTaskCompleteRatio()) ? "" : o1.getTaskCompleteRatio().replace("%", ""))));
        if (newDepTask.size() > 0) {
            analyseVO.setMaxTaskRatioDep(newDepTask.get(0).getDepName());
            analyseVO.setMinTaskRatioDep(newDepTask.get(newDepTask.size() - 1).getDepName());
            analyseVO.setMaxTaskRatio(newDepTask.get(0).getTaskCompleteRatio());
            analyseVO.setMinTaskRatio(newDepTask.get(newDepTask.size() - 1).getTaskCompleteRatio());
            if (oldDepTask.size() > 0) {
                analyseVO.setMaxTaskRatioRatio(CommonUtil.calculateRingRatioPercentageAsString(newDepTask.get(0)
                                                                                                       .getTaskCompleteRatio(), oldDepTask
                                                                                                       .get(0).getTaskCompleteRatio()));
                analyseVO.setMinTaskRatioRatio(CommonUtil.calculateRingRatioPercentageAsString(newDepTask.get(newDepTask.size() - 1)
                                                                                                       .getTaskCompleteRatio(), oldDepTask
                                                                                                       .get(oldDepTask.size() - 1)
                                                                                                       .getTaskCompleteRatio()));
            } else {
                analyseVO.setMaxTaskRatioRatio("0.00%");
                analyseVO.setMinTaskRatioRatio("0.00%");
            }
        }
        //按已完成任务数排序
        newDepTask.sort((o1, o2) -> new BigDecimal(o2.getCompleteTaskNum()).compareTo(new BigDecimal(o1.getCompleteTaskNum())));
        oldDepTask.sort((o1, o2) -> new BigDecimal(o2.getCompleteTaskNum()).compareTo(new BigDecimal(o1.getCompleteTaskNum())));
        if (newDepTask.size() > 0) {
            analyseVO.setMaxTaskNumDep(newDepTask.get(0).getDepName());
            analyseVO.setMaxTaskNum(newDepTask.get(0).getCompleteTaskNum());
            analyseVO.setMinTaskNumDep(newDepTask.get(newDepTask.size() - 1).getDepName());
            analyseVO.setMinTaskNum(newDepTask.get(newDepTask.size() - 1).getCompleteTaskNum());
            if (oldDepTask.size() > 0) {
                Long maxOldNum = oldDepTask.get(0).getCompleteTaskNum();
                analyseVO.setMaxTaskNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(maxOldNum,
                                                                                                    analyseVO.getMaxTaskNum() - maxOldNum));
                Long minOldNum = oldDepTask.get(oldDepTask.size() - 1).getCompleteTaskNum();
                analyseVO.setMinTaskNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(minOldNum,
                                                                                                    analyseVO.getMinTaskNum() - minOldNum));
            } else {
                analyseVO.setMaxTaskNumRatio("0.00%");
                analyseVO.setMinTaskNumRatio("0.00%");
            }
        }
        analyseVO.setAddDepTask(newDepTask);
        return analyseVO;
    }

    /**
     * @Author wql
     * @Description 任务耗时分析
     * @Date 20:11 2023/4/25
     **/
    @Override
    public TaskTimeAnalysisVO taskTimeAnalysis(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        InspectionAnalysisParam oldParam = getOldInspectionAnalysisParam(analysisParam, period);
        Long newTotalTime = Optional.ofNullable(inspectRecordMapper.countTotalTime(analysisParam, null)).orElse(0L);
        Long oldTotalTime = Optional.ofNullable(inspectRecordMapper.countTotalTime(oldParam, null)).orElse(0L);
        TaskTimeAnalysisVO analyseVO = new TaskTimeAnalysisVO();
        analyseVO.setTotalElapsedTime(minuteToHour(newTotalTime));
        analyseVO.setTotalElapsedTimeRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldTotalTime,
                                                                                                  newTotalTime - oldTotalTime));
        //按巡检类型分组统计巡检耗时
        List<InspectionTaskTimeVO> timeVOS = inspectRecordMapper.countTotalTimeByType(analysisParam);
        if (ObjectUtils.isNotNull(timeVOS) && timeVOS.size() > 0) {
            InspectionTaskTimeVO maxType = timeVOS.get(0);
            InspectionTaskTimeVO minType = timeVOS.get(timeVOS.size() - 1);
            Long maxOldTime = Optional.ofNullable(inspectRecordMapper.countTotalTime(oldParam, maxType.getTypeId())).orElse(0L);
            Long maxTime = (maxType.getTaskTime() == null) ? 0 : (maxType.getTaskTime() - maxOldTime);
            maxType.setTaskTimeRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(maxOldTime, maxTime));
            Long minOldTime = Optional.ofNullable(inspectRecordMapper.countTotalTime(oldParam, minType.getTypeId())).orElse(0L);
            Long minTime = minType.getTaskTime() == null ? 0 : (minType.getTaskTime() - minOldTime);
            minType.setTaskTimeRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(minOldTime, minTime));
        } else {
            timeVOS = Collections.emptyList();
        }
        timeVOS.parallelStream().forEach(vo -> {
            vo.setTaskTimeHour(minuteToHour(vo.getTaskTime()));
        });
        analyseVO.setTaskTypeTime(timeVOS);
        return analyseVO;
    }

    /**
     * @Author wql
     * @Description 分钟转小时
     * @Date 15:00 2023/5/5
     **/
    private Double minuteToHour(Long newTotalTime) {
        if (newTotalTime == null) {
            return 0D;
        }
        return new BigDecimal(newTotalTime).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * @Author wql
     * @Description 稽查任务分析
     * @Date 20:18 2023/4/25
     **/
    @Override
    public AnalysisOfAuditResultsVO analysisOfAuditResults(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        InspectionAnalysisParam oldParam = getOldInspectionAnalysisParam(analysisParam, period);
        AnalysisOfAuditResultsVO analyseVO = new AnalysisOfAuditResultsVO();
        List<InspectTaskPO> newTask = Optional.ofNullable(inspectTaskMapper.getAllCheckTask(analysisParam)).orElse(Collections.emptyList());
        List<InspectTaskPO> oldTask = Optional.ofNullable(inspectTaskMapper.getAllCheckTask(oldParam)).orElse(Collections.emptyList());
        analyseVO.setTotalCheckTaskNum(newTask.size());
        analyseVO.setTotalCheckTaskNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy((long) oldTask.size(),
                                                                                                   (long) newTask.size() - oldTask.size()));
        List<CheckResultMatchVO> checkMatchRatios = getAllTaskMatchRatio(newTask);
        int size = checkMatchRatios.size();
        List<CheckResultMatchVO> top = new ArrayList<>();
        List<CheckResultMatchVO> last = new ArrayList<>();
        if (size > 0) {
            last = checkMatchRatios.stream().sorted(Comparator.comparing(o -> new BigDecimal(o.getMatchRatio().replace("%", ""))))
                    .collect(Collectors.toList());
            top.addAll(last);
            Collections.reverse(top);
        }
        if (size > 5) {
            analyseVO.setTop5Match(top.subList(0, 5));
            analyseVO.setLast5Match(last.subList(0, 5));
        } else {
            analyseVO.setTop5Match(top);
            analyseVO.setLast5Match(last);
        }
        return analyseVO;
    }

    /**
     * @Author wql
     * @Description 巡检对象分析
     * @Date 20:25 2023/4/25
     **/
    @Override
    public InspectionObjAnalysisVO inspectionObjAnalysis(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        InspectionAnalysisParam oldAnalysisParam = getOldInspectionAnalysisParam(analysisParam, period);
        InspectionObjAnalysisVO analyseVO = new InspectionObjAnalysisVO();
        InspectionNumParam newParam = InspectionNumParam.builder().period(analysisParam.getPeriod()).build();
        InspectionNumParam oldParam = InspectionNumParam.builder().period(oldAnalysisParam.getPeriod()).build();
        Long newTotal = statisticAnalysisService.countInspectionNum(newParam);
        Long oldTotal = statisticAnalysisService.countInspectionNum(oldParam);
        analyseVO.setTotalInspectionNum(newTotal);
        analyseVO.setTotalInspectionNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldTotal, newTotal - oldTotal));
        List<ObjInspectionInfoVO> objInspectionInfo = inspectRecordMapper.countInspectionNumByObj(analysisParam);
        if (ObjectUtils.isNotEmpty(objInspectionInfo)) {
            ObjInspectionInfoVO maxObj = objInspectionInfo.get(0);
            analyseVO.setMaxNumObj(maxObj.getObjName());
            analyseVO.setMaxNum(maxObj.getInspectionNum());
            oldParam.setObjId(maxObj.getObjId());
            Long maxOldNum = statisticAnalysisService.countInspectionNum(oldParam);
            analyseVO.setMaxNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(maxOldNum, analyseVO.getMaxNum() - maxOldNum));

            ObjInspectionInfoVO minObj = objInspectionInfo.get(objInspectionInfo.size() - 1);
            analyseVO.setMinNumObj(minObj.getObjName());
            analyseVO.setMinNum(minObj.getInspectionNum());
            oldParam.setObjId(minObj.getObjId());
            Long minOldNum = statisticAnalysisService.countInspectionNum(oldParam);
            analyseVO.setMinNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(minOldNum, analyseVO.getMinNum() - minOldNum));
        }
        return analyseVO;
    }

    /**
     * @Author wql
     * @Description 巡检对象异常项分析
     * @Date 20:31 2023/4/25
     **/
    @Override
    public InspectionObjAbnormalAnalysisVO inspectionObjAbnormalAnalysis(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        InspectionAnalysisParam oldAnalysisParam = getOldInspectionAnalysisParam(analysisParam, period);
        Period newPeriod = analysisParam.getPeriod();
        Period oldPeriod = oldAnalysisParam.getPeriod();
        Long newTotalNum = inspectRecordMapper.countObjAbnormalNum(newPeriod.getBegin(), newPeriod.getEnd(), null);
        Long oldTotalNum = inspectRecordMapper.countObjAbnormalNum(oldPeriod.getBegin(), oldPeriod.getEnd(), null);
        InspectionObjAbnormalAnalysisVO analyseVO = new InspectionObjAbnormalAnalysisVO();
        analyseVO.setAbnormalObjNum(newTotalNum);
        analyseVO.setAbnormalObjNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldTotalNum, newTotalNum - oldTotalNum));
        List<ObjInspectionInfoVO> infos = inspectRecordMapper.countObjAbnormalNumByDep(analysisParam);
        if (ObjectUtils.isNotEmpty(infos)) {
            ObjInspectionInfoVO maxObj = infos.get(0);
            analyseVO.setMaxAbnormalObj(maxObj.getObjName());
            analyseVO.setMaxAbnormalObjNum(maxObj.getInspectionNum());
            Long maxOldNum = inspectRecordMapper.countObjAbnormalNum(newPeriod.getBegin(), newPeriod.getEnd(), maxObj.getObjId());
            analyseVO.setMaxAbnormalObjNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(maxOldNum,
                                                                                                       analyseVO.getMaxAbnormalObjNum() -
                                                                                                       maxOldNum));

            ObjInspectionInfoVO minObj = infos.get(infos.size() - 1);
            analyseVO.setMinAbnormalObj(minObj.getObjName());
            analyseVO.setMinAbnormalObjNum(minObj.getInspectionNum());
            Long minOldNum = inspectRecordMapper.countObjAbnormalNum(oldPeriod.getBegin(), oldPeriod.getEnd(), minObj.getObjId());
            analyseVO.setMinAbnormalObjNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(minOldNum,
                                                                                                       analyseVO.getMinAbnormalObjNum() -
                                                                                                       minOldNum));
        } else {
            analyseVO.setMaxAbnormalObj("");
            analyseVO.setMaxAbnormalObjNum(0L);
            analyseVO.setMaxAbnormalObjNumRatio("0.00%");

            analyseVO.setMinAbnormalObj("");
            analyseVO.setMinAbnormalObjNum(0L);
            analyseVO.setMinAbnormalObjNumRatio("0.00%");
        }
        return analyseVO;
    }

    /**
     * @Author wql
     * @Description 对象任务统计分析
     * @Date 20:35 2023/4/25
     **/
    @Override
    public InspectionObjTaskAnalysisVO inspectionObjTaskAnalysis(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        InspectionAnalysisParam oldAnalysisParam = getOldInspectionAnalysisParam(analysisParam, period);
        InspectionObjTaskAnalysisVO analyseVO = new InspectionObjTaskAnalysisVO();
        TaskDetailParam newParam = TaskDetailParam.builder().period(analysisParam.getPeriod()).build();
        TaskDetailParam oldParam = TaskDetailParam.builder().period(oldAnalysisParam.getPeriod()).build();
        analyseVO.setInspectionTaskTotalNum(statisticAnalysisService.countTaskNum(newParam));
        List<ObjInspectionInfoVO> infos = inspectRecordMapper.countObjTaskNumByObj(analysisParam);
        if (ObjectUtils.isNotEmpty(infos)) {
            ObjInspectionInfoVO maxInfo = infos.get(0);
            analyseVO.setMaxTaskNumObj(maxInfo.getObjName());
            analyseVO.setMaxTaskNum(maxInfo.getInspectionNum());
            oldParam.setObjId(maxInfo.getObjId());
            Long maxOldNum = statisticAnalysisService.countTaskNum(oldParam);
            analyseVO.setMaxTaskNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(maxOldNum,
                                                                                                maxInfo.getInspectionNum() - maxOldNum));

            ObjInspectionInfoVO minInfo = infos.get(infos.size() - 1);
            analyseVO.setMinTaskNumObj(minInfo.getObjName());
            analyseVO.setMinTaskNum(minInfo.getInspectionNum());
            oldParam.setObjId(minInfo.getObjId());
            Long minOldNum = statisticAnalysisService.countTaskNum(oldParam);
            analyseVO.setMinTaskNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(minOldNum, minInfo.getInspectionNum()));
        }
        return analyseVO;
    }

    /**
     * @Author wql
     * @Description 对象巡检次数top10
     * @Date 20:27 2023/4/18
     **/
    @Override
    public List<ObjInspectionInfoVO> objInspectionNumTop(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        List<ObjInspectionInfoVO> objInspectionInfo = inspectRecordMapper.countInspectionNumByObj(analysisParam);
        if (ObjectUtils.isNotEmpty(objInspectionInfo)) {
            return objInspectionInfo.size() > 10 ? objInspectionInfo.subList(0, 10) : objInspectionInfo;
        }
        return Collections.emptyList();
    }

    /**
     * @Author wql
     * @Description 对象异常项数排名top10
     * @Date 8:48 2023/4/19
     **/
    @Override
    public List<ObjInspectionInfoVO> objAbnormalNumTop(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        List<ObjInspectionInfoVO> infos = inspectRecordMapper.countObjAbnormalNumByDep(analysisParam);
        if (ObjectUtils.isNotEmpty(infos)) {
            return infos.size() > 10 ? infos.subList(0, 10) : infos;
        }
        return Collections.emptyList();
    }

    /**
     * @Author wql
     * @Description 对象巡检任务数top10
     * @Date 8:55 2023/4/19
     **/
    @Override
    public List<ObjInspectionInfoVO> objInspectionTaskTop(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        List<ObjInspectionInfoVO> infos = inspectRecordMapper.countObjTaskNumByObj(analysisParam);
        if (ObjectUtils.isNotEmpty(infos)) {
            return infos.size() > 10 ? infos.subList(0, 10) : infos;
        }
        return Collections.emptyList();
    }

    /**
     * @Author wql
     * @Description 对象巡检任务数last10
     * @Date 8:56 2023/4/19
     **/
    @Override
    public List<ObjInspectionInfoVO> objInspectionTaskLast(InspectionAnalysisParam analysisParam) {
        Period period = statisticAnalysisService.getPeriod(analysisParam.getPeriod(), "");
        analysisParam.setPeriod(period);
        List<ObjInspectionInfoVO> infos = inspectRecordMapper.countObjTaskNumByObj(analysisParam);
        if (ObjectUtils.isNotEmpty(infos) && infos.size() > 10) {
            return infos.subList(infos.size() - 11, infos.size() - 1);
        }
        return infos;
    }

    @Override
    public List<TargetCategoryVO> getTargetCategory(InspectionAnalysisParam analysisParam) {
        List<TargetCategoryVO> data = new ArrayList<>();

        List<TargetCategoryDetailsVO> device = new ArrayList<>();
        List<TargetCategoryDetailsVO> property = new ArrayList<>();
        List<TargetCategoryDetailsVO> stall = new ArrayList<>();
        List<TargetCategoryDetailsVO> sitePoint = new ArrayList<>();
        List<TargetCategoryDetailsVO> riskSource = new ArrayList<>();
        List<TargetCategoryDetailsVO> smallProject = new ArrayList<>();
        // 巡检次数
        List<TargetCategoryDetailsVO> inspectionList = inspectRecordMapper.selectInspection(analysisParam, null);
        // 异常次数
        List<TargetCategoryDetailsVO> abnormalList = inspectRecordMapper.selectInspection(analysisParam, PatrolResultEnum.ABNORMAL.name());
        // 任务数
        List<TargetCategoryDetailsVO> taskList = inspectRecordMapper.selectTask(analysisParam);
        // 巡检总次数
        Integer inspectionNumber = 0;
        // 任务总次数
        Integer taskNumber = 0;
        // 异常任务次数
        Integer abnormalNumber = 0;
        for (TargetCategoryDetailsVO ex : inspectionList) {
            if (InspectTargetCategoryEnum.DEVICE.name().equals(ex.getName())) {
                inspectionNumber += ex.getNum();
                ex.setName("inspection");
                device.add(ex);
            } else if (InspectTargetCategoryEnum.STALL.name().equals(ex.getName())) {
                inspectionNumber += ex.getNum();
                ex.setName("inspection");
                stall.add(ex);
            } else if (InspectTargetCategoryEnum.PROPERTY.name().equals(ex.getName())) {
                inspectionNumber += ex.getNum();
                ex.setName("inspection");
                property.add(ex);
            } else if (InspectTargetCategoryEnum.SITE_POINT.name().equals(ex.getName())) {
                inspectionNumber += ex.getNum();
                ex.setName("inspection");
                sitePoint.add(ex);
            } else if (InspectTargetCategoryEnum.RISK_SOURCE.name().equals(ex.getName())) {
                inspectionNumber += ex.getNum();
                ex.setName("inspection");
                riskSource.add(ex);
            } else if (InspectTargetCategoryEnum.SMALL_ENGINEERING.name().equals(ex.getName())) {
                inspectionNumber += ex.getNum();
                ex.setName("inspection");
                smallProject.add(ex);
            }
        }
        for (TargetCategoryDetailsVO ex : abnormalList) {
            if (InspectTargetCategoryEnum.DEVICE.name().equals(ex.getName())) {
                abnormalNumber += ex.getNum();
                ex.setName("abnormal");
                device.add(ex);
            } else if (InspectTargetCategoryEnum.STALL.name().equals(ex.getName())) {
                abnormalNumber += ex.getNum();
                ex.setName("abnormal");
                stall.add(ex);
            } else if (InspectTargetCategoryEnum.PROPERTY.name().equals(ex.getName())) {
                abnormalNumber += ex.getNum();
                ex.setName("abnormal");
                property.add(ex);
            } else if (InspectTargetCategoryEnum.SITE_POINT.name().equals(ex.getName())) {
                abnormalNumber += ex.getNum();
                ex.setName("abnormal");
                sitePoint.add(ex);
            } else if (InspectTargetCategoryEnum.RISK_SOURCE.name().equals(ex.getName())) {
                abnormalNumber += ex.getNum();
                ex.setName("abnormal");
                riskSource.add(ex);
            } else if (InspectTargetCategoryEnum.SMALL_ENGINEERING.name().equals(ex.getName())) {
                abnormalNumber += ex.getNum();
                ex.setName("abnormal");
                smallProject.add(ex);
            }
        }
        for (TargetCategoryDetailsVO ex : taskList) {
            if (InspectTargetCategoryEnum.DEVICE.name().equals(ex.getName())) {
                taskNumber += ex.getNum();
                ex.setName("task");
                device.add(ex);
            } else if (InspectTargetCategoryEnum.STALL.name().equals(ex.getName())) {
                taskNumber += ex.getNum();
                ex.setName("task");
                stall.add(ex);
            } else if (InspectTargetCategoryEnum.PROPERTY.name().equals(ex.getName())) {
                taskNumber += ex.getNum();
                ex.setName("task");
                property.add(ex);
            } else if (InspectTargetCategoryEnum.SITE_POINT.name().equals(ex.getName())) {
                taskNumber += ex.getNum();
                ex.setName("task");
                sitePoint.add(ex);
            } else if (InspectTargetCategoryEnum.RISK_SOURCE.name().equals(ex.getName())) {
                taskNumber += ex.getNum();
                ex.setName("task");
                riskSource.add(ex);
            } else if (InspectTargetCategoryEnum.SMALL_ENGINEERING.name().equals(ex.getName())) {
                taskNumber += ex.getNum();
                ex.setName("task");
                smallProject.add(ex);
            }
        }
        Map<String, Integer> number = new HashMap<>();
        number.put("inspection", inspectionNumber);
        number.put("task", taskNumber);
        number.put("abnormal", abnormalNumber);
        data.add(TargetCategoryVO.builder().targetCategory(InspectTargetCategoryEnum.DEVICE.name())
                         .details(TargetCategoryDetailsVO.getTargetCategoryDetails(device, number)).build());
        data.add(TargetCategoryVO.builder().targetCategory(InspectTargetCategoryEnum.PROPERTY.name())
                         .details(TargetCategoryDetailsVO.getTargetCategoryDetails(property, number)).build());
        data.add(TargetCategoryVO.builder().targetCategory(InspectTargetCategoryEnum.STALL.name())
                         .details(TargetCategoryDetailsVO.getTargetCategoryDetails(stall, number)).build());
        data.add(TargetCategoryVO.builder().targetCategory(InspectTargetCategoryEnum.SITE_POINT.name())
                         .details(TargetCategoryDetailsVO.getTargetCategoryDetails(sitePoint, number)).build());
        data.add(TargetCategoryVO.builder().targetCategory(InspectTargetCategoryEnum.RISK_SOURCE.name())
                         .details(TargetCategoryDetailsVO.getTargetCategoryDetails(riskSource, number)).build());
        data.add(TargetCategoryVO.builder().targetCategory(InspectTargetCategoryEnum.SMALL_ENGINEERING.name())
                         .details(TargetCategoryDetailsVO.getTargetCategoryDetails(smallProject, number)).build());
        return data;
    }

    @Override
    public SafetyReportResultVO inspectAnalysisSafetyReport(Period period) {
        List<InspectionSafetyReportVO> inspectionSafetyReportVOS = inspectRecordMapper.selectInspectRecordForSafeyReport(period);
        Long totalNum = 0L;
        if (!CollectionUtils.isEmpty(inspectionSafetyReportVOS)) {
            totalNum = inspectionSafetyReportVOS.stream().map(InspectionSafetyReportVO::getNum)
                    .reduce(Long::sum).get();
        } else {
            inspectionSafetyReportVOS = new ArrayList<>();
        }
        //环比
        Period month = DateUnitUtils.getPreviousPeriod("THIS_MONTH", period);
        Long ringNum = inspectRecordMapper.countInspectionNum(Wrappers.query()
                                                                      .between(ObjectUtils.isNotNull(period), "a.time", month.getBegin(), month.getEnd())
                                                             );
        if (Objects.isNull(ringNum)) {
            ringNum = 0L;
        }
        //同比
        Period samePeriod = DateUnitUtils.getSamePeriod(period);
        Long sameNum = inspectRecordMapper.countInspectionNum(Wrappers.query()
                                                                      .between(ObjectUtils.isNotNull(period), "a.time", samePeriod.getBegin(), samePeriod.getEnd())
                                                             );
        if (Objects.isNull(sameNum)) {
            sameNum = 0L;
        }

        return SafetyReportResultVO.builder()
                .num(totalNum)
                .ringNum(Long.valueOf(ringNum))
                .sameNum(Long.valueOf(sameNum))
                .inspectionSafetyReportVOLis(inspectionSafetyReportVOS)
                .build();
    }

    @Override
    public Pagination<InspectionItemDetailsVO> getInspectionStandardTtem(InspectionItemDetailsParam query) {
        Page<InspectTaskPO> page = new Page(query.getPageNumber(), query.getPageSize());
        page = inspectTaskMapper.selectPage(page, new LambdaQueryWrapper<InspectTaskPO>()
                .between(InspectTaskPO::getBeginTime, query.getPeriod().getBegin(), query.getPeriod().getEnd())
                .like(InspectTaskPO::getTargetIds, query.getTargetId()));
        List<InspectionItemDetailsVO> list = new ArrayList<>();
        page.getRecords().forEach(ex -> {
            InspectionItemDetailsVO inspectionItemDetailsVO = new InspectionItemDetailsVO();
            inspectionItemDetailsVO.setTaskName(ex.getName());
            inspectionItemDetailsVO.setResult(InspectRecordResultEnum.NORMAL.name());
            if (ObjectUtils.isNotEmpty(ex.getReceiveType()) && ex.getReceiveType().equals("1")) {
                UserDTO userDTO = userClient.getUser(Long.parseLong(ex.getReceiveIds()));
                inspectionItemDetailsVO.setUserName(Optional.ofNullable(userDTO).orElse(new UserDTO()).getName());
            }
            inspectionItemDetailsVO.setEndTime(ex.getEndTime());
            inspectionItemDetailsVO.setNormalItemCount(0);
            inspectionItemDetailsVO.setAbnormalItemCount(0);
            List<InspectRecordPO> item = inspectRecordMapper.selectList(new LambdaQueryWrapper<InspectRecordPO>()
                                                                                .eq(InspectRecordPO::getTaskId, ex.getId())
                                                                                .eq(InspectRecordPO::getTargetId, query.getTargetId()));
            for (InspectRecordPO record : item) {
                if (ObjectUtils.isNotNull(record) && ObjectUtils.isNotNull(record.getResult())) {
                    if (InspectRecordResultEnum.NORMAL.name().equals(record.getResult().name())) {
                        inspectionItemDetailsVO.setNormalItemCount(inspectionItemDetailsVO.getNormalItemCount() + 1);
                    } else if (InspectRecordResultEnum.ABNORMAL.name().equals(record.getResult().name())) {
                        inspectionItemDetailsVO.setResult(InspectRecordResultEnum.ABNORMAL.name());
                        inspectionItemDetailsVO.setAbnormalItemCount(inspectionItemDetailsVO.getAbnormalItemCount() + 1);
                    }
                }
            }
            inspectionItemDetailsVO.setItemCount(item.size());
            list.add(inspectionItemDetailsVO);
        });
        return new Pagination<>(page.getTotal(), page.getPages(), list);
    }

    @Override
    public HomepageStatisticsVO inspectAnalysisHomepageStatistics(BaseDateStatisticsParam param) {
        Assert.parameterIsNull(param.getPeriod(), "请选则时间范围");
        Long userId = SecurityContextHolder.getPrincipal().getId();
        //巡检次数
        WorkSituationVO workSituationVO = maintainSafetyAnalysisService
                .inspectAnalysisPermission(param.getPeriod(), param.getType(), null);
        //正常数
        WorkSituationVO normalVO = maintainSafetyAnalysisService
                .inspectAnalysisPermission(param.getPeriod(), param.getType(), TaskResultEnum.NORMAL.name());
        //异常数
        WorkSituationVO abnormalVO = maintainSafetyAnalysisService
                .inspectAnalysisPermission(param.getPeriod(), param.getType(), TaskResultEnum.ABNORMAL.name());
        TaskDetailParam param1 = TaskDetailParam.builder().parentId(0L).userId(userId).period(param.getPeriod()).build();
        TaskDetailParam param2 = TaskDetailParam.builder().parentId(0L).userId(userId)
                .period(DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod())).build();

        // 任务数
        Long totalNum = statisticAnalysisService.countTaskNumPermission(param1);
        Long oldTotalNum = statisticAnalysisService.countTaskNumPermission(param2);

        //异常任务数
        Integer countAbnormalTaskNum = statisticAnalysisService.countAbnormalTaskNumPermission(param);
        // 进行中任务数
        param1.setState(TaskStatusEnum.EXECUTING.name());
        param2.setState(TaskStatusEnum.EXECUTING.name());
        Long executingNum = statisticAnalysisService.countTaskNumPermission(param1);
        Long oldExecutingNum = statisticAnalysisService.countTaskNumPermission(param2);
        //过期未完成任务数
        param1.setState(TaskStatusEnum.EXPIRE.name());
        param2.setState(TaskStatusEnum.EXPIRE.name());
        Long expireNum = statisticAnalysisService.countTaskNumPermission(param1);
        Long oldExpireNum = statisticAnalysisService.countTaskNumPermission(param2);
        return HomepageStatisticsVO
                .builder()
                .completeRatio(taskService.inspectionTaskFinishedRatioPermission(param.getPeriod(), InspectTaskStatusEnum.OVER))
                .abnormalRatio(MathUtils.calculateProportion(countAbnormalTaskNum, totalNum).floatValue())
                .thisYearInspectionCount(HomepageStatisticsVO.StatisticInfo.builder()
                                                 .num(workSituationVO.getTotalNum())
                                                 .ratio(workSituationVO.getChainRatio())
                                                 .build())
                .normalCount(HomepageStatisticsVO.StatisticInfo.builder()
                                     .num(normalVO.getTotalNum())
                                     .ratio(normalVO.getChainRatio())
                                     .build())
                .abnormalCount(HomepageStatisticsVO.StatisticInfo.builder()
                                       .num(abnormalVO.getTotalNum())
                                       .ratio(abnormalVO.getChainRatio())
                                       .build())
                .thisYearInspectionTaskCount(HomepageStatisticsVO.StatisticInfo.builder()
                                                     .num(Long.valueOf(totalNum))
                                                     .ratio(MathUtils.calculateRingRatioPercentage(totalNum, oldTotalNum).floatValue())
                                                     .build())
                .ongoingTaskCount(HomepageStatisticsVO.StatisticInfo.builder()
                                          .num(Long.valueOf(executingNum))
                                          .ratio(MathUtils.calculateRingRatioPercentage(executingNum, oldExecutingNum).floatValue())
                                          .build())
                .expiredTaskCount(HomepageStatisticsVO.StatisticInfo.builder()
                                          .num(Long.valueOf(expireNum))
                                          .ratio(MathUtils.calculateRingRatioPercentage(expireNum, oldExpireNum).floatValue())
                                          .build())
                .build();
    }

    @Override
    public InspectionIObjDetailsVO inspectAnalysisObjectDetailsStatistics(InspectionItemDetailsParam param) {
        //巡检次数
        Long totalNum = inspectRecordMapper.countInspectionNum(Wrappers.query()
                                                                       .eq("target_id", param.getTargetId())
                                                                       .between(ObjectUtils.isNotNull(param.getPeriod()), "a.time", ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd)));

        Long abnormalNum = inspectRecordMapper.countInspectionNum(Wrappers.query()
                                                                          .eq("target_id", param.getTargetId())
                                                                          .eq("a.result", TaskResultEnum.ABNORMAL.name())
                                                                          .between(ObjectUtils.isNotNull(param.getPeriod()), "a.time", ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd)));
        return InspectionIObjDetailsVO.builder().totalNum(totalNum).abnormalNum(abnormalNum).build();
    }

    @Override
    public InspectionTaskAnalysisVO inspectAnalysisTaskStatistics(BaseDateStatisticsParam param) {
        InspectionTaskAnalysisVO vo = new InspectionTaskAnalysisVO();
        TaskDetailParam param1 = TaskDetailParam.builder().parentId(0L).period(param.getPeriod()).build();
        TaskDetailParam param2 = TaskDetailParam.builder().parentId(0L)
                .period(DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod())).build();
        Long totalNum = statisticAnalysisService.countTaskNum(param1);
        vo.setTaskTotalNum(totalNum);
        Long oldTotalNum = statisticAnalysisService.countTaskNum(param2);
        vo.setTaskTotalNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldTotalNum, totalNum - oldTotalNum));

        param1.setResult(TaskResultEnum.ABNORMAL.name());
        param2.setResult(TaskResultEnum.ABNORMAL.name());
        Long anomalyNum = statisticAnalysisService.countTaskNum(param1);
        Long oldAnomalyNum = statisticAnalysisService.countTaskNum(param2);
        vo.setAbnormalTaskNum(anomalyNum);
        vo.setAbnormalTaskNumRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(oldTotalNum, anomalyNum - oldAnomalyNum));
        return vo;
    }

    @Override
    public Pagination<InspectionTaskUserAnalysisVO> inspectAnalysisTaskExecuteUserStatistics(BaseInspectStatisticsParam param) {
        Period period = param.getPeriod();
        Page<InspectTaskPO> inspectTaskPOPage = inspectTaskMapper.selectPage(PagingUtils.convert(param), new LambdaQueryWrapper<InspectTaskPO>()
                                                                                     .between(ObjectUtils.isNotNull(period), InspectTaskPO::getCreateTime, period.getBegin(), period.getEnd())
                                                                                     .eq(StringUtils.isNotBlank(param.getResult()), InspectTaskPO::getResult, param.getResult())
                                                                            );
        if (CollectionUtils.isEmpty(inspectTaskPOPage.getRecords())) {
            return new Pagination<>();
        }
        List<InspectTaskPO> inspectTaskPOList = inspectTaskPOPage.getRecords();
        List<Long> userIdList = inspectTaskPOList.stream()
                .filter(t -> StringUtils.isNotBlank(t.getReceiveIds()))
                .map(t -> {
                    return Long.valueOf(t.getReceiveIds());
                })
                .collect(Collectors.toList());

        Map<String, String> userMap = Optional.ofNullable(userClient.listUserByIds(userIdList))
                .orElse(Collections.emptyList())
                .stream().collect(Collectors.toMap(t -> String.valueOf(t.getId()), UserDTO::getName));

        List<InspectionTaskUserAnalysisVO> taskUserAnalysisVOS = inspectTaskPOList.stream()
                .map(t -> InspectionTaskUserAnalysisVO.builder()
                        .taskId(t.getId())
                        .taskName(t.getName())
                        .typeName(t.getTypeName())
                        .publisherName(t.getPublisherName())
                        .level(t.getLevel())
                        .status(t.getStatus())
                        .targetNum(t.getTargetNum())
                        .recordCompletionRate(taskService.getTaskRecordRate(t.getId(), t.getTargetNum(), t.getIsSplit()))
                        .taskExecuteUser(userMap.get(t.getReceiveIds()))
                        .result(StringUtils.isNoneBlank(t.getResult()) ? t.getResult() : t.getStatus().name())
                        .build()).collect(Collectors.toList());
        return PagingUtils.convert(inspectTaskPOPage, taskUserAnalysisVOS);
    }

    @Override
    public Pagination<InspectionRecordAnalysisVO> inspectAnalysisTaskRecordStatistics(BaseInspectStatisticsParam query) {
        Page<InspectionRecordAnalysisVO> inspectRecordPage = inspectRecordMapper.selectInspectRecord(PagingUtils.convert(query), query);

        if (Objects.isNull(inspectRecordPage) || CollectionUtils.isEmpty(inspectRecordPage.getRecords())) {
            return new Pagination<>();
        }
        List<Long> businessKeys = inspectRecordPage.getRecords().stream()
                .filter(t -> Objects.nonNull(t.getErrorItemId())).map(InspectionRecordAnalysisVO::getErrorItemId)
                .collect(Collectors.toList());
        Map<Long, List<FileVO>> businessKeysMap;
        if (!CollectionUtils.isEmpty(businessKeys)) {
            BusinessKeysParam businessKeysParam = new BusinessKeysParam();
            businessKeysParam.setBusinessKeys(businessKeys);
            businessKeysMap
                    = fileClient.getByBusinessKeys(businessKeysParam);
        } else {
            businessKeysMap = new HashMap<>();
        }

        inspectRecordPage.getRecords().forEach(t -> {
            if (!CollectionUtils.isEmpty(businessKeysMap.get(t.getErrorItemId()))) {
                t.setFileVO(businessKeysMap.get(t.getErrorItemId()).get(0));
            }
        });

        return PagingUtils.convert(inspectRecordPage);
    }

    /**
     * @Author wql
     * @Description 统计巡检次数趋势
     * @Date 13:45 2023/4/18
     **/
    private List<TrendOfInspectionTimesVO> statisticsInspectionTrend(InspectionAnalysisParam analysisParam, Period period) {
        var date = LocalDateTime.now();
        List<TrendOfInspectionTimesVO> list = new ArrayList<>();
        if (InspectionAnalysisDataEnum.ToDay.name().equals(analysisParam.getTimeFlag())) {
            int hour = date.getHour();
            var startDate = DateUtils.beginOfDay(date);
            for (int i = 0; i < hour; i++) {
                list.add(new TrendOfInspectionTimesVO(DateUtils.offset(startDate, ChronoUnit.HOURS, i), DateUtils.offset(startDate, ChronoUnit.HOURS,
                                                                                                                         i +
                                                                                                                         1), String.valueOf(
                        i + 1)));
            }
        } else if (InspectionAnalysisDataEnum.ThisWeek.name().equals(analysisParam.getTimeFlag())) {
            var startDate = DateUtils.beginOfWeek(date);
            int week = date.getDayOfWeek().getValue();
            for (int i = 0; i < week; i++) {
                var dateTime = DateUtils.offset(startDate, ChronoUnit.DAYS, i);
                list.add(new TrendOfInspectionTimesVO(dateTime, DateUtils.endOfDay(dateTime), String.valueOf(i + 1)));
            }
        } else if (InspectionAnalysisDataEnum.ThisMonth.name().equals(analysisParam.getTimeFlag())) {
            var startDate = DateUtils.beginOfMonth(date);
            int month = date.getMonthValue();
            for (int i = 0; i < month + 1; i++) {
                var dateTime = DateUtils.offset(startDate, ChronoUnit.DAYS, i);
                list.add(new TrendOfInspectionTimesVO(dateTime, DateUtils.endOfDay(dateTime), String.valueOf(i + 1)));
            }
        } else if (InspectionAnalysisDataEnum.ThisYear.name().equals(analysisParam.getTimeFlag())) {
            var startDate = DateUtils.beginOfYear(date);
            int month = LocalDate.now().getMonthValue();
            for (int i = 0; i < month + 1; i++) {
                var dateTime = DateUtils.offsetMonth(startDate, i);
                list.add(new TrendOfInspectionTimesVO(dateTime, DateUtils.endOfMonth(dateTime), (i + 1) + "月"));
            }
        } else {
            long day = DateUtils.between(period.getBegin(), LocalDateTime.now(), ChronoUnit.DAYS);
            var startTime = DateUtils.beginOfDay(period.getBegin());
            for (int i = 0; i < day + 1; i++) {
                var dateTime = DateUtils.offsetDay(startTime, i);
                list.add(new TrendOfInspectionTimesVO(dateTime, DateUtils.endOfDay(dateTime), DateUtils.formatDate(dateTime)));
            }
        }
        Optional.of(list).orElse(Collections.emptyList()).parallelStream().forEach(param -> {
            Period tempPeriod = new Period(param.getStartTime(), param.getEndTime());
            InspectionNumParam numParam = InspectionNumParam.builder().period(tempPeriod).build();
            param.setNumberOfInspection(statisticAnalysisService.countInspectionNum(numParam));
            TaskDetailParam detailParam = TaskDetailParam.builder().period(tempPeriod).build();
            param.setTaskNum(statisticAnalysisService.countTaskNum(detailParam));
        });
        return list;
    }

    /**
     * @Author wql
     * @Description 获取上个周期的时间参数
     * @Date 16:25 2023/4/18
     **/
    public InspectionAnalysisParam getOldInspectionAnalysisParam(InspectionAnalysisParam analysisParam, Period period) {
        long offset = DateUtils.between(period.getBegin(), period.getEnd(), ChronoUnit.DAYS) + 1;
        InspectionAnalysisParam oldParam = new InspectionAnalysisParam();
        oldParam.setTimeFlag(analysisParam.getTimeFlag());
        oldParam.setPeriod(new Period(DateUtils.offsetDay(period.getBegin(), (int) (-offset)), DateUtils.offsetDay(period.getEnd(), (int) (-offset))));
        return oldParam;
    }

    /**
     * @Author wql
     * @Description 统计部门任务完成情况
     * @Date 17:40 2023/4/18
     **/
    public List<DepInspectionTaskInfoVO> getDepTask(InspectionAnalysisParam analysisParam) {
        Period period = analysisParam.getPeriod();
        List<DepInspectionTaskInfoVO> addDepTask = inspectRecordMapper.countTotalTimeByDep(Wrappers.query()
                                                                                                   .isNotNull("a.time")
                                                                                                   .isNotNull("a.organization_id")
                                                                                                   .between(ObjectUtils.isNotNull(period), "a.create_time", period.getBegin(), period.getEnd())
                                                                                                   .groupBy("a.organization_id"));
        Optional.ofNullable(addDepTask).orElse(Collections.emptyList()).parallelStream().forEach(vo -> {
            TaskDetailParam param = TaskDetailParam.builder().period(period).depId(vo.getDepId()).build();
            vo.setTotalTaskNum(statisticAnalysisService.countTaskNum(param));
            param.setState(TaskStatusEnum.OVER.name());
            vo.setCompleteTaskNum(statisticAnalysisService.countTaskNum(param));
            param.setState(TaskStatusEnum.OVERTIME.name());
            vo.setOverTimeTaskNum(statisticAnalysisService.countTaskNum(param));
            param.setState(TaskStatusEnum.EXPIRE.name());
            vo.setExpireTaskNum(statisticAnalysisService.countTaskNum(param));
            vo.setTaskCompleteRatio(CommonUtil.calculateProportionPercentageAsStringLegacy(vo.getTotalTaskNum(), vo.getCompleteTaskNum()));
        });
        return addDepTask;
    }

    /**
     * @Author wql
     * @Description 统计所有稽查任务的稽查匹配率
     * @Date 19:34 2023/4/19
     **/
    public List<CheckResultMatchVO> getAllTaskMatchRatio(List<InspectTaskPO> newTask) {
        List<CheckResultMatchVO> matchVoList = new ArrayList<>();
        for (InspectTaskPO po : newTask) {
            CheckResultMatchVO matchVO = new CheckResultMatchVO();
            List<InspectionTimeVO> inspectionRecord = statisticAnalysisService.getEachInspectionRecord(po.getCheckTaskId(), inspectTaskMapper.getIsSplitById(po.getCheckTaskId()));
            List<InspectionTimeVO> checkRecord = statisticAnalysisService.getEachInspectionRecord(po.getId(), false);
            if (ObjectUtils.isNotEmpty(inspectionRecord) && ObjectUtils.isNotEmpty(checkRecord)) {
                InspectTaskPO taskPO = inspectTaskMapper.selectOne(Wrappers.<InspectTaskPO>lambdaQuery()
                                                                           .eq(InspectTaskPO::getId, po.getCheckTaskId()));
                matchVO.setTaskName(taskPO.getName());
                matchVO.setReleaseTime(DateUtils.format(taskPO.getCreateTime(), "yyyy-MM-dd HH:mm"));
                matchVO.setDutyOfficer(ObjectUtils.isNotNull(checkRecord.get(0)) ? checkRecord.get(0).getUsername() : "");
                List<String> list = new ArrayList<>();
                String ratio = statisticAnalysisService.countMachRatio(inspectionRecord, checkRecord, list);
                matchVO.setMatchRatio(ratio);
                matchVO.setNotMatchNum(list.size());
                matchVoList.add(matchVO);
            }
        }
        return matchVoList;
    }
}

