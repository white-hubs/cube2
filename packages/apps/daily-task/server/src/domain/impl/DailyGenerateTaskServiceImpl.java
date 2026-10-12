package com.guhecloud.cube.biz.daily.task.domain.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyCronService;
import com.guhecloud.cube.biz.daily.task.domain.DailyGenerateTaskService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyCronPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyJobRecordPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanStandardRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanSuperintendentPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetRelationPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetStandardRelationPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyDomainTypeEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyPlanStatusEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskTypeEnum;
import com.guhecloud.cube.biz.daily.task.param.DailyParticipateConditionParam;
import com.guhecloud.cube.biz.daily.task.param.DailyParticipateParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTargetStandardResultParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAddParam;
import com.guhecloud.cube.biz.daily.task.plan.coverts.DailyCronUtils;
import com.guhecloud.cube.biz.daily.task.plan.dto.DailyPlanGenerateTaskAfterUpdateDTO;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyJobRecordService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanStandardRelationService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanSuperintendentService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanTargetRelationService;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanTargetStandardRelationService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskUserRuleService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserMapQuery;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.ConditionTypeEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pair;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 任务生成
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyGenerateTaskServiceImpl implements DailyGenerateTaskService {

    private final DailyPlanService dailyPlanService;
    private final DailyJobRecordService dailyJobRecordService;
    private final DailyPlanSuperintendentService dailyPlanSuperintendentService;
    private final DailyPlanStandardRelationService dailyPlanStandardRelationService;
    private final DailyPlanTargetStandardRelationService dailyPlanTargetStandardRelationService;
    private final DailyPlanTargetRelationService dailyPlanTargetRelationService;
    private final DailyCronService dailyCronService;
    private final DailyTaskService dailyTaskService;

    private final FileClient fileClient;
    private final UserClient userClient;
    private final DailyTaskUserRuleService dailyTaskUserRuleService;

    /**
     * 批量计划生成任务
     *
     * @param now 当前时间
     */
    @Override
    public void batchGenerateTaskByPlan(LocalDateTime now) {
        var planList = dailyPlanService.lambdaQuery()
                .eq(DailyPlanPO::getStatus, DailyPlanStatusEnum.RUNNING.name())
                .le(DailyPlanPO::getStartTime, now)
                .list();
        if (CollectionUtils.isEmpty(planList)) {
            var tenant =
                    SessionContextHolder.getRequester() == null ? "-1" : String.valueOf(SessionContextHolder.getRequester().getTenant());
            log.warn("batchGenerateTaskByPlan: planList is empty: {}", tenant);
            return;
        }

        this.batchGenerateTask(planList, now);

    }

    /**
     * 批量根据计划生成每日任务
     * 根据传入的计划列表、当前时间和日类型，批量创建对应的任务及相关数据（如任务扩展信息、负责人、执行内容等），
     * 并更新相关计划状态及触发业务状态机事件。
     *
     * @param planList 待处理的计划集合，不能为空
     * @param now      当前时间，用于判断计划是否过期以及设置任务截止时间
     */
    public void batchGenerateTask(List<DailyPlanPO> planList, LocalDateTime now) {
        /* 初始化各类ID集合与映射关系，便于后续统一操作 */
        var planIdSet = new HashSet<Long>();
        var planTaskIdMap = new HashMap<Long, Long>();
        var planIdTaskParamMap = new HashMap<Long, DailyTaskAddParam>();
        var planFileKeySet = new HashSet<Long>();
        var planFileRefIdTaskFileRefIdMap = new HashMap<Long, Long>();
        var cronIdSet = new HashSet<Long>();
        var taskIds = new ArrayList<Long>();
        var planIdCreateByMap = new HashMap<Long, String>();

        this.befTaskCreateHandler(planList, now, planIdSet, planIdCreateByMap, planTaskIdMap, planIdTaskParamMap, planFileKeySet, planFileRefIdTaskFileRefIdMap, cronIdSet, taskIds);
        if (CollectionUtils.isEmpty(planIdSet)) {
            return;
        }
        Map<Long, Long> createUserMap;
        if (!planIdCreateByMap.isEmpty()) {
            createUserMap = new HashMap<>();
            var userMapQuery = new UserMapQuery();
            userMapQuery.setUserNames(new HashSet<>(planIdCreateByMap.values()));
            Map<String, UserDTO> tenantUserMap = userClient.getTenantUserMap(userMapQuery);
            for (Map.Entry<Long, String> entry : planIdCreateByMap.entrySet()) {
                if (tenantUserMap.containsKey(entry.getValue())) {
                    createUserMap.put(entry.getKey(), tenantUserMap.get(entry.getValue()).getId());
                }
            }
        } else {
            createUserMap = new HashMap<>();
        }


        /* 执行项 */
        var planItemList = dailyPlanStandardRelationService.lambdaQuery().in(DailyPlanStandardRelationPO::getPlanId, planIdSet).list();
        var planIdItemListMap = Optional.ofNullable(planItemList).orElse(Collections.emptyList()).stream()
                .collect(Collectors.groupingBy(DailyPlanStandardRelationPO::getPlanId, Collectors.mapping(DailyPlanStandardRelationPO::getItemId, Collectors.toList())));
        /* 文件 */
        Map<Long, List<FileVO>> fileMap;
        if (CollectionUtils.isNotEmpty(planFileKeySet)) {
            BusinessKeysParam fileParam = new BusinessKeysParam();
            fileParam.setBusinessKeys(new ArrayList<>(planFileKeySet));
            fileMap = fileClient.getByBusinessKeys(fileParam);
        } else {
            fileMap = Collections.emptyMap();
        }

        /* 执行对象的标准 */
        var planTargetStandardList = Optional.ofNullable(dailyPlanTargetStandardRelationService.lambdaQuery()
                                                                 .in(DailyPlanTargetStandardRelationPO::getPlanId, planIdSet).list())
                .orElse(new ArrayList<>());
        var planIdTargetStandardListMap = planTargetStandardList.stream()
                .collect(Collectors.groupingBy(
                        DailyPlanTargetStandardRelationPO::getPlanId, Collectors.groupingBy(DailyPlanTargetStandardRelationPO::getTargetId)
                                              ));
        var planIdSuperintendentListMap = new HashMap<Long, List<DailyParticipateParam>>();
        var planSuperintendentList = dailyPlanSuperintendentService.lambdaQuery().in(DailyPlanSuperintendentPO::getMasterId, planIdSet)
                .list();
        var planIdSuperintendentMap = Optional.ofNullable(planSuperintendentList).orElse(Collections.emptyList()).stream()
                .collect(Collectors.groupingBy(DailyPlanSuperintendentPO::getMasterId));
        planIdSuperintendentMap.forEach((planId, superintendentList) -> {
            var list = superintendentList.stream().map(po -> DailyParticipateParam.builder()
                            .participateType(ConditionTypeEnum.valueOf(po.getSuperintendentType()))
                            .targetId(po.getTargetId()).build())
                    .collect(Collectors.toList());
            planIdSuperintendentListMap.put(planId, list);
        });
        //规则信息
        Set<Long> ruleRefId = new HashSet<>(planIdSet);
        Map<Pair<Long, Long>, DailyPlanTargetRelationPO> targetRelationMap = dailyPlanTargetRelationService.getTargetRelationMap(planIdSet);
        ruleRefId.addAll(targetRelationMap.values().stream().filter(t -> Objects.nonNull(t.getRuleId()))
                                 .map(DailyPlanTargetRelationPO::getRuleId).collect(Collectors.toSet()));
        Map<Long, List<DailyParticipateConditionParam>> ruleByRefIdMap = dailyTaskUserRuleService.getRulesForTaskParam(ruleRefId, DailyDomainTypeEnum.DAILY_PLAN);

        /* 组装 */
        var taskAddParamList = new ArrayList<DailyTaskAddParam>();
        planIdTaskParamMap.forEach((planId, taskAddParam) -> {
            /* 处理对象和对象标准*/
            var targetMap = planIdTargetStandardListMap.getOrDefault(planId, Collections.emptyMap());
            List<DailyTargetStandardResultParam> targetResultList = new ArrayList<>();
            targetMap.forEach((targetId, standardList) -> {
                var standardIdList = standardList.stream().map(po -> DailyStandardResultDTO.builder().id(po.getStandardId()).build())
                        .collect(Collectors.toList());
                List<DailyParticipateConditionParam> dailyParticipateConditionParams = new ArrayList<>();
                String tagIds = null;
                if (targetRelationMap.containsKey(Pair.of(planId, targetId))) {
                    DailyPlanTargetRelationPO targetRelationPO = targetRelationMap.get(Pair.of(planId, targetId));
                    dailyParticipateConditionParams = ruleByRefIdMap.get(targetRelationPO.getRuleId());
                    tagIds = targetRelationPO.getTagIds();
                }

                //兼容之前的数据
                DailyParticipateConditionParam conditionParam;
                if (CollectionUtils.isEmpty(dailyParticipateConditionParams)) {
                    conditionParam = DailyParticipateConditionParam.builder()
                            .participateParams(planIdSuperintendentListMap.getOrDefault(planId, Collections.emptyList()))
                            .build();
                } else {
                    conditionParam = dailyParticipateConditionParams.getFirst();
                }
                var targetResult = DailyTargetStandardResultParam.builder()
                        .targetCategory(standardList.getFirst().getTargetCategory())
                        .targetId(targetId)
                        .standardList(standardIdList)
                        .participateCondition(conditionParam)
                        .build();
                targetResultList.add(targetResult);
            });
            /* 处理文件 */
            var refKey = planFileRefIdTaskFileRefIdMap.getOrDefault(planId, 0L);
            var fileIds = Optional.ofNullable(fileMap.get(refKey)).orElse(Collections.emptyList()).stream().map(FileVO::getId)
                    .collect(Collectors.toList());
            taskAddParam.setFileIds(fileIds);
            taskAddParam.setParticipateList(ruleByRefIdMap.getOrDefault(planId, Collections.emptyList()));
            taskAddParam.setTaskItemIdList(planIdItemListMap.getOrDefault(planId, Collections.emptyList()));
            taskAddParam.setTargetResultList(targetResultList);
            taskAddParam.setPublisherId(createUserMap.get(planId));
            taskAddParamList.add(taskAddParam);
        });

        /* 批量计划生成任务 */
        if (CollectionUtils.isNotEmpty(taskAddParamList)) {
            dailyTaskService.createDailyTaskBatch(taskAddParamList);
        }

        /* 更新计划的最后执行时间和下次执行时间 */
        this.planGenerateTaskUpdateBatch(planList, cronIdSet, now);
    }

    /**
     * 处理即将创建任务的计划数据，构造任务参数并更新相关状态。
     * <p>
     * 该方法会遍历传入的计划列表，过滤掉已过期的计划（结束时间早于当前时间），为有效的计划构造对应的任务参数，
     * 并将相关信息存入传入的集合中供后续使用。同时会记录本次处理涉及的计划ID，并更新已过期计划的状态。
     *
     * @param planList           待处理的计划列表
     * @param now                当前时间，用于判断计划是否过期及设置更新时间
     * @param planIdSet          存放有效计划ID的集合
     * @param planIdCreateByMap  计划ID与对应创建人账号的映射关系
     * @param planTaskIdMap      计划ID与对应生成任务ID的映射关系
     * @param planIdTaskParamMap 计划ID与对应任务新增参数的映射关系
     * @param planFileKeySet     存放计划关联文件key的集合
     * @param planIdFileRefIdMap 计划ID与文件引用ID的映射关系
     * @param cronIdSet          存放计划关联的定时表达式ID集合
     * @param taskIds            存放本次生成的所有任务ID集合
     */
    private void befTaskCreateHandler(List<DailyPlanPO> planList, LocalDateTime now, HashSet<Long> planIdSet,
                                      HashMap<Long, String> planIdCreateByMap, HashMap<Long, Long> planTaskIdMap,
                                      HashMap<Long, DailyTaskAddParam> planIdTaskParamMap, HashSet<Long> planFileKeySet,
                                      HashMap<Long, Long> planIdFileRefIdMap
            , HashSet<Long> cronIdSet, ArrayList<Long> taskIds
                                     ) {
        if (CollectionUtils.isEmpty(planList)) {
            return;
        }

        // 存储已过期的计划ID
        var planExpiredList = new ArrayList<Long>();

        var iterator = planList.iterator();
        while (iterator.hasNext()) {
            var plan = iterator.next();
            // 过期判断
            if ((Objects.nonNull(plan.getEndTime()) && plan.getEndTime().isBefore(now)) || plan.getNextTime() == null) {
                planExpiredList.add(plan.getId());
                iterator.remove();
                continue;
            }
            if (plan.getNextTime().isAfter(now)) {
                // 未到执行时间
                iterator.remove();
                continue;
            }

            planIdCreateByMap.put(plan.getId(), plan.getCreateBy());

            /* 构造任务实体对象并填充基础字段 */
            var taskId = IdWorker.getId();
            var taskName = this.getTaskNameToPlanName(plan.getName(), plan.getExecutionCount());

            DailyTaskAddParam taskAddParam = this.getDailyTaskAddParam(plan, taskId, taskName);

            taskIds.add(taskId);
            planIdTaskParamMap.put(plan.getId(), taskAddParam);
            cronIdSet.add(plan.getCronId());
            planIdSet.add(plan.getId());
            planFileKeySet.add(plan.getFileRefKey());
            planTaskIdMap.put(plan.getId(), taskId);

            if (plan.getFileRefKey() != null) {
                planIdFileRefIdMap.put(plan.getId(), plan.getFileRefKey());
            }
        }

        // 保存执行记录
        dailyJobRecordService.save(DailyJobRecordPO.builder()
                                           .id(IdWorker.getId())
                                           .planIds(planIdSet.stream().map(String::valueOf).collect(Collectors.joining(",")))
                                           .build());

        // 修改过期计划的状态为已完成，并清空下次执行时间
        if (CollectionUtils.isNotEmpty(planExpiredList)) {
            dailyPlanService.lambdaUpdate()
                    .set(DailyPlanPO::getStatus, DailyPlanStatusEnum.FINISHED.name())
                    .set(DailyPlanPO::getNextTime, null)
                    .set(DailyPlanPO::getUpdateTime, now)
                    .in(DailyPlanPO::getId, planExpiredList)
                    .update();
        }
    }

    /**
     * 批量更新计划生成任务的相关字段，包括执行次数和下次执行时间。
     * <p>
     * 该方法会根据计划列表中的 cron 表达式或指定规则计算出下一次的执行时间，
     * 并将更新数据分批处理后提交更新操作。
     *
     * @param planList  待处理的计划列表，不能为空
     * @param cronIdSet 计划关联的 cron ID 集合，用于查询对应的 cron 表达式
     * @param now       当前时间，作为本次执行的时间基准
     */
    private void planGenerateTaskUpdateBatch(List<DailyPlanPO> planList, Set<Long> cronIdSet, LocalDateTime now) {
        if (CollectionUtils.isEmpty(planList)) {
            return;
        }
        var loopList = new ArrayList<ArrayList<DailyPlanGenerateTaskAfterUpdateDTO>>();
        var planUpdateList = new ArrayList<DailyPlanGenerateTaskAfterUpdateDTO>();

        /* 更新计划执行次数和下次执行时间 */
        // 获取 cron 表达式映射关系，便于后续快速查找
        var cronMap = new HashMap<Long, DailyCronPO>();
        var cronList = dailyCronService.lambdaQuery().in(DailyCronPO::getId, cronIdSet).list();
        Optional.ofNullable(cronList).orElse(new ArrayList<>()).forEach(cron -> cronMap.put(cron.getId(), cron));

        var planListSize = planList.size();
        for (int i = 0; i < planListSize; i++) {
            var po = planList.get(i);
            String cron;

            // 根据 cronId 或者指定类型获取 cron 表达式
            if (po.getCronId() != null && cronMap.containsKey(po.getCronId())) {
                var dailyCron = cronMap.get(po.getCronId());
                cron = dailyCron.getCron();
            } else {
                cron = DailyCronUtils.handlerCorn(po.getSpecifiedType(), po.getSpecifiedDays(), po.getSpecifiedTime());
            }

            // 计算下次执行时间并构建更新 DTO 对象
            LocalDateTime nextTime = null;
            try {
                CronExpression cronExpression = new CronExpression(cron);
                nextTime = cronExpression.getNextValidTimeAfter(now);
            } catch (ParseException e) {
                log.error("日常任务, 计划生成任务，cron表达式解析异常：", e);
            }
            var dto = DailyPlanGenerateTaskAfterUpdateDTO.builder()
                    .id(po.getId())
                    .lastTime(now)
                    .executionCount(po.getExecutionCount() + 1)
                    .nextTime(nextTime)
                    .updateTime(now)
                    .build();
            planUpdateList.add(dto);

            // 每满 100 条记录或遍历到最后一条时进行分批存储
            if (planUpdateList.size() >= 100 || i == (planListSize - 1)) {
                loopList.add(new ArrayList<>(planUpdateList));
                planUpdateList.clear();
            }
        }
        /* 执行批量更新操作 */
        if (CollectionUtils.isNotEmpty(loopList)) {
            loopList.forEach(dailyPlanService::batchUpdateMultiFields);
        }
    }

    /**
     * 根据日常计划信息创建日常任务添加参数对象
     *
     * @param plan     日常计划PO对象，用于获取计划相关属性
     * @param taskId   任务ID
     * @param taskName 任务名称
     *
     * @return DailyTaskAddParam 日常任务添加参数对象
     */
    private DailyTaskAddParam getDailyTaskAddParam(DailyPlanPO plan, long taskId, String taskName) {
        DailyTaskAddParam taskAddParam = new DailyTaskAddParam();
        taskAddParam.setId(taskId);
        taskAddParam.setName(taskName);
        taskAddParam.setDailyType(plan.getDailyType());
        taskAddParam.setDraft(false);
        taskAddParam.setPlanId(plan.getId());
        taskAddParam.setTaskCategoryId(plan.getTaskCategoryId());
        taskAddParam.setUrgencyId(plan.getUrgencyId());
        taskAddParam.setTaskDay(plan.getTaskDay());
        taskAddParam.setTaskHour(plan.getTaskHour());
        taskAddParam.setTaskMinute(plan.getTaskMinute());
        taskAddParam.setDescription(plan.getDescription());
        taskAddParam.setMethod(plan.getMethod());
        taskAddParam.setIsOnDuty(plan.getIsOnDuty());
        taskAddParam.setRequireProgress(plan.getRequireProgress());
        taskAddParam.setTaskType(StringUtils.isNotBlank(plan.getTaskType()) ? DailyTaskTypeEnum.valueOf(plan.getTaskType()) : null);
        return taskAddParam;
    }

    /**
     * 根据计划名称和执行次数生成任务名称
     *
     * @param planName       计划名称，如果为null则使用空字符串
     * @param executionCount 执行次数，如果为null则使用0
     *
     * @return 生成的任务名称，格式为"计划名称_V版本号"，版本号从1开始，不足4位前面补0
     */
    private String getTaskNameToPlanName(String planName, Integer executionCount) {
        if (planName == null) {
            planName = "";
        }
        if (executionCount == null) {
            executionCount = 0;
        }
        // 版本号从1开始计算
        var version = executionCount + 1;
        return planName + "_V" + java.lang.String.format("%04d", version);
    }

}
