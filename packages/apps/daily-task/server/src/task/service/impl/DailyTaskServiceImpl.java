package com.guhecloud.cube.biz.daily.task.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.daily.task.commons.constants.DailyConstants;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyTargetDTO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyParticipateConditionResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyPlanTargetResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskDetailResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskHomePageResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskPageResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskSummaryResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskTargetResult;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyTaskCategoryService;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyUrgencyService;
import com.guhecloud.cube.biz.daily.task.domain.DailySuperintendentService;
import com.guhecloud.cube.biz.daily.task.domain.adapter.client.DailyAttendanceClient;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.ArrangeWorkParam;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskFeedbackPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserRulePO;
import com.guhecloud.cube.biz.daily.task.enums.DailyDomainTypeEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyExecutionResultEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailySuperintendentTypeEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskBusinessTypeEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskNoticeMessageEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskTypeEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskUserTypeEnum;
import com.guhecloud.cube.biz.daily.task.param.DailyBaseStatisticsParam;
import com.guhecloud.cube.biz.daily.task.param.DailyParticipateConditionParam;
import com.guhecloud.cube.biz.daily.task.param.DailyParticipateParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTargetStandardResultParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAddParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAssignParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAssignUserParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskFeedbackParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskOperationParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskOtherTargetParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskPageParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskSubmitParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskTargetQueryParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordTargetBatchParam;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordTargetService;
import com.guhecloud.cube.biz.daily.task.status.base.BaseState;
import com.guhecloud.cube.biz.daily.task.status.base.TransitionContext;
import com.guhecloud.cube.biz.daily.task.status.bussiness.DailyTaskStateNodes;
import com.guhecloud.cube.biz.daily.task.status.service.BusinessStateMachine;
import com.guhecloud.cube.biz.daily.task.task.convert.DailyTaskAddParamCovert;
import com.guhecloud.cube.biz.daily.task.task.convert.DailyTaskConvert;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskMapper;
import com.guhecloud.cube.biz.daily.task.task.result.GridAbnormalTaskCountResult;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskFeedbackService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskItemService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskTargetService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskUserRuleService;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskUserService;
import com.guhecloud.cube.biz.mdata.client.GridAreaConfigClient;
import com.guhecloud.cube.biz.mdata.client.rocketmq.message.GridAreaMemberSetMessage;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.ConditionTypeEnum;
import com.guhecloud.cube.biz.ouaa.metadata.param.ConditionParam;
import com.guhecloud.cube.biz.ouaa.metadata.param.UserRuleConditionParam;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.DISTRIBUTE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.DRAFT_TASK;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.HANDLE_TARGET;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.ISSUE_TASK;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TASK_COMPLETE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TASK_TERMINATE;
import static com.guhecloud.cube.biz.daily.task.status.base.BaseEvent.TRANSFER;

/**
 * 任务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskServiceImpl extends ServiceImpl<DailyTaskMapper, DailyTaskPO> implements DailyTaskService {
    private final BusinessStateMachine businessStateMachine;
    private final DailyTaskUserService dailyTaskUserService;
    private final DailyTaskTargetService dailyTaskTargetService;
    private final DailyTaskItemService dailyTaskItemService;
    private final DailyTaskMapper dailyTaskMapper;
    private final DailyTaskCategoryService dailyTaskCategoryService;
    private final DailyUrgencyService dailyUrgencyService;
    private final FileRefClient fileRefClient;
    private final FileClient fileClient;
    private final UserClient userClient;
    private final DailyRecordService dailyRecordService;
    private final DailySuperintendentService dailySuperintendentService;
    private final DailyRecordTargetService dailyRecordTargetService;
    private final DailyTaskUserRuleService dailyTaskUserRuleService;
    private final GridAreaConfigClient gridAreaConfigClient;
    private final DailyTaskFeedbackService dailyTaskFeedbackService;
    private final DailyAttendanceClient dailyAttendanceClient;
    private final MessageSendClient messageSendClient;

    /**
     * 分页列表
     * 只查询主任务、状态、对象个数、根据当前用户权限来
     * <p>
     * 创建一条任务，不存在父子任务概念
     *
     * @param param 条件
     *
     * @return 分页列表
     * 列表查询只查看主r
     *
     */
    @Override
    public Pagination<DailyTaskPageResult> pageDailyTask(DailyTaskPageParam param) {
        List<String> dailyTypeList = null;
        if (StringUtils.isNotBlank(param.getDailyTypes())) {
            dailyTypeList = List.of(param.getDailyTypes().split(","));
        }
        //获取对象关联任务ID
        if (Objects.nonNull(param.getObjectId())) {
            List<Long> taskIds = dailyTaskTargetService.getTaskIdByObjectId(param.getObjectId(), param.getDailyType());
            if (CollectionUtils.isNotEmpty(taskIds)) {
                param.setTaskIdList(taskIds);
            }
        }
        Page<DailyTaskPO> page = dailyTaskMapper.selectTaskPage(PagingUtils.convert(param), param, dailyTypeList);
        SecurityContextHolder.clearDbDataPermission();
        return extentInfo(page);
    }

    private Pagination<DailyTaskPageResult> extentInfo(Page<DailyTaskPO> page) {
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        var taskCategoryIds = new HashSet<Long>();
        var urgencyIds = new HashSet<Long>();
        var taskIds = new HashSet<Long>();
        for (DailyTaskPO record : page.getRecords()) {
            taskCategoryIds.add(record.getTaskCategoryId());
            urgencyIds.add(record.getUrgencyId());
            taskIds.add(record.getId());
        }
        var taskSuperintendentList = dailyTaskUserService.list(Wrappers.<DailyTaskUserPO>lambdaQuery()
                                                                       .in(DailyTaskUserPO::getMasterId, taskIds));
        var assigneeList = taskSuperintendentList.stream()
                .filter(t -> DailyTaskUserTypeEnum.ASSIGNEE.name().equals(t.getTaskUserType()))
                .collect(Collectors.toList());
        Map<Long, DailySuperintendentDTO> taskSuperintendentMap = dailySuperintendentService.handlerSuperintendent(assigneeList)
                .stream().collect(Collectors.toMap(DailySuperintendentDTO::getMasterId, Function.identity(), (k1, k2) -> k1));
        Map<Long, Long> taskCreateByMap = taskSuperintendentList.stream()
                .filter(t -> DailyTaskUserTypeEnum.SUPERINTENDENT.name().equals(t.getTaskUserType()))
                .collect(Collectors.toMap(DailyTaskUserPO::getMasterId, DailyTaskUserPO::getTargetId, (k1, k2) -> k1));

        UserQuery userQuery = new UserQuery();
        userQuery.setUserIds(new HashSet<>(taskCreateByMap.values()));
        Map<Long, UserDTO> userNameMap = Optional.ofNullable(userClient.postList(userQuery))
                .orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        Map<Long, UserDTO> taskCreateUserMap = new HashMap<>();
        taskCreateByMap.forEach((k, v) -> {
            if (userNameMap.containsKey(v)) {
                taskCreateUserMap.put(k, userNameMap.get(v));
            }
        });

        var categoryMap = dailyTaskCategoryService.getCategoryByIds(taskCategoryIds);
        var urgencyPOMap = dailyUrgencyService.getUrgencyByIds(urgencyIds);
        Map<Long, List<DailyTaskUserPO>> taskUserMap = taskSuperintendentList.stream()
                .collect(Collectors.groupingBy(DailyTaskUserPO::getMasterId));
        Map<Long, List<DailyTaskTargetPO>> taskTargetMap =
                dailyTaskTargetService.getTargetByTaskIdListGroupByTaskId(taskIds);
        Map<String, List<String>> noCompleteStatusMap = new HashMap<>();
        page.getRecords().stream()
                .map(DailyTaskPO::getDailyType)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .forEach(dailyType -> noCompleteStatusMap.put(
                        dailyType, businessStateMachine.getNoFinalStatus(dailyType)));
        Set<Long> abnormalTaskIds = new HashSet<>(Optional.ofNullable(dailyTaskMapper.selectSnapshotTaskIds(new ArrayList<>(taskIds)))
                                                          .orElse(Collections.emptyList()));
        var resultRecords = DailyTaskConvert.convertResult(
                page.getRecords(), categoryMap, urgencyPOMap, taskCreateUserMap, taskSuperintendentMap,
                taskUserMap, taskTargetMap, noCompleteStatusMap);
        resultRecords.forEach(record -> record.setAbnormal(abnormalTaskIds.contains(record.getId())));
        return PagingUtils.convert(page, resultRecords);
    }

    @Override
    public DailyTaskDetailResult detailDailyTask(Long id) {
        var po = this.getById(id);
        Assert.isNotFound(po);
        var result = DailyTaskConvert.convertResult(po);

        if (result.getFileRefKey() != null) {
            result.setFileList(fileClient.getByBusinessKey(result.getFileRefKey()));
        }
        List<DailyItemResultDTO> itemList = dailyTaskItemService.getTaskItemList(id);
        result.setTaskItemList(itemList);
        result.setNormalTaskRecord(dailyRecordService.getNormalTaskRecord(id));
        // 草稿箱使用
        if (DailyTaskStateNodes.TASK_STATE_NODE_DRAFT.getStateCode().equals(po.getStatus())) {
            List<DailyParticipateConditionResult> ruleByRefId = dailyTaskUserRuleService.getRuleByRefId(id, DailyDomainTypeEnum.DAILY_TASK);
            result.setTargetResultList(ruleByRefId);
        }
        var taskSuperintendentList = dailyTaskUserService.list(Wrappers.<DailyTaskUserPO>lambdaQuery()
                                                                       .eq(DailyTaskUserPO::getMasterId, id));
        List<DailyTaskUserPO> assigneeUserList = taskSuperintendentList.stream()
                .filter(t -> !DailyTaskUserTypeEnum.SUPERINTENDENT.name().equals(t.getTaskUserType()) &&
                             DailySuperintendentTypeEnum.USER.name().equals(t.getSuperintendentType()))
                .collect(Collectors.toList());
        var superintendentTypeMap = assigneeUserList.stream()
                .collect(Collectors.groupingBy(DailyTaskUserPO::getSuperintendentType));
        result.setSuperintendentType(
                superintendentTypeMap.containsKey(DailySuperintendentTypeEnum.USER.name()) ? DailySuperintendentTypeEnum.USER.name()
                                                                                           : DailySuperintendentTypeEnum.DEPT.name());
        result.setParticipateList(dailySuperintendentService.handlerSuperintendent(assigneeUserList));

        var categoryPO = dailyTaskCategoryService.getById(result.getTaskCategoryId());
        var urgencyPO = dailyUrgencyService.getById(result.getUrgencyId());
        result.setTaskCategoryName(Objects.nonNull(categoryPO) ? categoryPO.getName() : null);
        result.setUrgencyName(Objects.nonNull(urgencyPO) ? urgencyPO.getName() : null);

        //是否允许移交，获取当前任务是否关联对象且状态都不为完成
        this.canTransfer(result, id, po, taskSuperintendentList);
        return result;
    }

    /**
     *
     * @param result
     * @param id
     * @param dailyTaskPO
     * @param taskSuperintendentList
     */
    private void canTransfer(DailyTaskDetailResult result, Long id, DailyTaskPO dailyTaskPO, List<DailyTaskUserPO> taskSuperintendentList) {
        List<String> noCompleteStatus = businessStateMachine.getNoFinalStatus(dailyTaskPO.getDailyType());
        Optional<DailyTaskUserPO> taskUserPO = Optional.ofNullable(taskSuperintendentList).orElse(new ArrayList<>())
                .stream().filter(t ->
                                         DailyTaskUserTypeEnum.ASSIGNEE.name().equals(t.getTaskUserType())
                                         && Objects.equals(SecurityContextHolder.getPrincipal().getId(), t.getTargetId()))
                .findFirst();
        //发布人
        Optional<DailyTaskUserPO> createByUser = Optional.ofNullable(taskSuperintendentList).orElse(new ArrayList<>())
                .stream().filter(t ->
                                         DailyTaskUserTypeEnum.SUPERINTENDENT.name().equals(t.getTaskUserType())
                                         && Objects.equals(SecurityContextHolder.getPrincipal().getId(), t.getTargetId()))
                .findFirst();
        // 先判断持续性任务按钮权限
        if (Boolean.TRUE.equals(dailyTaskPO.getContinuity())) {
            result.setAllowSubmit(false);
            if (taskUserPO.isEmpty() && createByUser.isEmpty()) {
                result.setAllowDistribute(false);
                result.setAllowTransfer(false);
                result.setAllowFeedback(false);
                result.setAllowHandle(false);
                return;
            }
            boolean isActive = noCompleteStatus.contains(dailyTaskPO.getStatus());
            boolean progressComplete = Objects.nonNull(dailyTaskPO.getProgress())
                                       && BigDecimal.ONE.compareTo(dailyTaskPO.getProgress()) <= 0;
            result.setAllowFeedback(isActive && !progressComplete);
            result.setAllowTransfer(isActive && !progressComplete);
            //任务活跃且当前用户为发布人才可以处理
            result.setAllowHandle(isActive && createByUser.isPresent());
            return;
        }
        boolean isAssignee = taskUserPO.isPresent();
        //责任人包含当前用户
        if (!isAssignee) {
            result.setAllowDistribute(false);
            result.setAllowTransfer(false);
            result.setAllowSubmit(false);
            result.setAllowFeedback(false);
            result.setAllowHandle(false);
            return;
        }
        if (!DailyTaskTypeEnum.TARGET.name().equals(dailyTaskPO.getTaskType())) {
            result.setAllowDistribute(false);
            result.setAllowSubmit(false);
            result.setAllowHandle(noCompleteStatus.contains(dailyTaskPO.getStatus()));
            result.setAllowTransfer(noCompleteStatus.contains(dailyTaskPO.getStatus()));
            return;
        }
        //当前任务是否存在未处理对象
        List<DailyTaskTargetPO> noCompletedTarget = dailyTaskTargetService.getTargetByTaskId(id);
        //是否存在任务id为空，或者不为空且未分发的对象
        long count = noCompletedTarget.stream().filter(t -> noCompleteStatus.contains(t.getStatus()) && (
                Objects.isNull(t.getTaskId())
                || (Objects.equals(t.getTaskId(), id)))).count();
        result.setAllowHandle(false);
        result.setAllowTransfer(noCompleteStatus.contains(dailyTaskPO.getStatus()) && count > 0);
        result.setAllowDistribute(result.getAllowTransfer());
        //已处理个数
        String completeState = businessStateMachine.getCompleteState(dailyTaskPO.getDailyType(), HANDLE_TARGET);

        long handlerNum = noCompletedTarget.stream()
                .filter(t -> Objects.equals(completeState, t.getStatus()) &&
                             (Objects.isNull(dailyTaskPO.getParentId()) || Objects.equals(t.getTaskId(), id))).count();
        long requireNum =
                Objects.isNull(dailyTaskPO.getRequireProgress()) ? dailyTaskPO.getTargetCount() : dailyTaskPO.getRequireProgress();
        result.setAllowSubmit(noCompleteStatus.contains(dailyTaskPO.getStatus()) &&
                              ((Objects.nonNull(dailyTaskPO.getParentId()) && Objects.isNull(dailyTaskPO.getRequireProgress()))
                               || handlerNum >= requireNum));
    }

    /**
     * 添加任务
     *
     * @param param 入参
     *
     * @return 主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDailyTask(DailyTaskAddParam param) {
        List<Long> dailyTaskBatch = this.createDailyTaskBatch(Collections.singletonList(param));
        return dailyTaskBatch.getFirst();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> createDailyTaskBatch(List<DailyTaskAddParam> paramList) {
        if (CollectionUtils.isEmpty(paramList)) {
            throw new ServiceException("任务列表不能为空");
        }
        //处理手动添加的任务项
        dailyTaskItemService.handlerTaskItem(paramList);
        //判断存草稿以及构建任务信息
        List<DailyTaskAddParam> dailyTaskAddParams = this.checkDraft(paramList);
        if (CollectionUtils.isEmpty(dailyTaskAddParams)) {
            return new ArrayList<>();
        }
        List<DailyTaskAddParam> buildTaskUser = this.getBuildTaskUser(dailyTaskAddParams);
        var taskIds = new ArrayList<Long>();
        Map<Long, List<DailyTargetStandardResultParam>> taskTargetMap = new HashMap<>(paramList.size());
        Map<Long, List<Long>> taskItemMap = new HashMap<>(paramList.size());
        Map<Long, Set<DailyParticipateParam>> taskUserMap = new HashMap<>(paramList.size());
        var taskFileRefIdMap = new HashMap<Long, List<Long>>();
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        Map<Long, Set<Long>> taskUserIdMap = new HashMap<>();
        Set<Long> allGridAreaIds = buildTaskUser.stream()
                .map(DailyTaskAddParam::getGridAreaIds)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Map<Long, Set<Long>> gridAreaUserMap = getGridUserIdMap(allGridAreaIds);
        Set<Long> onDutyLimitGridAreaIds = buildTaskUser.stream()
                .filter(taskParam -> Boolean.TRUE.equals(taskParam.getIsOnDuty()))
                .map(DailyTaskAddParam::getGridAreaIds)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Map<Long, Set<Long>> gridAreaOnDutyUserMap = filterOnDutyGridUserIdMap(gridAreaUserMap, onDutyLimitGridAreaIds, LocalDateTime.now());
        for (DailyTaskAddParam taskParam : buildTaskUser) {
            /* 任务对象 */
            DailyTaskPO task = this.buildTaskInfo(taskParam, taskFileRefIdMap);
            Long taskId = task.getId();
            if (Objects.nonNull(taskParam.getPublisherId())) {
                currentUserId = taskParam.getPublisherId();
                //如果没有配置负责人，则默认创建人作为负责人
            }
            DailyParticipateParam createUser = new DailyParticipateParam();
            createUser.setTargetId(currentUserId);
            createUser.setParticipateType(ConditionTypeEnum.USER);
            createUser.setTaskUserType(DailyTaskUserTypeEnum.SUPERINTENDENT.name());
            taskUserMap.computeIfAbsent(taskId, v -> new HashSet<>())
                    .add(createUser);
            //生成任务关联关系
            if (CollectionUtils.isNotEmpty(taskParam.getTargetResultList())) {
                taskTargetMap.put(taskId, taskParam.getTargetResultList());
            }
            if (CollectionUtils.isNotEmpty(taskParam.getTaskItemIdList())) {
                taskItemMap.put(taskId, taskParam.getTaskItemIdList());
            }
            Set<Long> taskUserIds = taskParam.getTaskUserIds();
            Set<Long> gridAreaIds = Optional.ofNullable(taskParam.getGridAreaIds()).orElse(Collections.emptySet());
            Assert.isNotFound(CollectionUtils.isEmpty(taskUserIds) && CollectionUtils.isEmpty(gridAreaIds), "任务接收人不能为空");
            //给当任务配置的角色或者部门进行创建任务
            for (Long taskUserId : Optional.ofNullable(taskUserIds).orElse(Collections.emptySet())) {
                DailyParticipateParam dailyParticipateParam = new DailyParticipateParam();
                dailyParticipateParam.setTargetId(taskUserId);
                dailyParticipateParam.setParticipateType(ConditionTypeEnum.USER);
                dailyParticipateParam.setTaskUserType(DailyTaskUserTypeEnum.ASSIGNEE.name());
                taskUserMap.computeIfAbsent(taskId, v -> new HashSet<>())
                        .add(dailyParticipateParam);
            }
            if (CollectionUtils.isNotEmpty(gridAreaIds)) {
                for (Long gridAreaId : gridAreaIds) {
                    DailyParticipateParam gridParticipateParam = new DailyParticipateParam();
                    gridParticipateParam.setTargetId(gridAreaId);
                    gridParticipateParam.setParticipateType(ConditionTypeEnum.GRID_AREA);
                    gridParticipateParam.setTaskUserType(DailyTaskUserTypeEnum.ASSIGNEE.name());
                    taskUserMap.computeIfAbsent(taskId, v -> new HashSet<>()).add(gridParticipateParam);
                }
                Map<Long, Set<Long>> taskGridUserMap =
                        Boolean.TRUE.equals(taskParam.getIsOnDuty()) ? gridAreaOnDutyUserMap : gridAreaUserMap;
                Set<Long> userIds = gridAreaIds.stream()
                        .map(taskGridUserMap::get)
                        .filter(CollectionUtils::isNotEmpty)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
                for (Long userId : userIds) {
                    DailyParticipateParam userParticipateParam = new DailyParticipateParam();
                    userParticipateParam.setTargetId(userId);
                    userParticipateParam.setParticipateType(ConditionTypeEnum.USER);
                    userParticipateParam.setTaskUserType(DailyTaskUserTypeEnum.ASSIGNEE.name());
                    taskUserMap.computeIfAbsent(taskId, v -> new HashSet<>()).add(userParticipateParam);
                }
                if (CollectionUtils.isNotEmpty(userIds)) {
                    taskUserIdMap.put(task.getId(), userIds);
                }
            } else {
                taskUserIdMap.put(task.getId(), taskUserIds);
            }
            taskIds.add(task.getId());
            this.save(task);
        }
        DailyTaskAddParam first = paramList.getFirst();
        //保存任务关联关系
        dailyTaskTargetService.saveBatchByTaskId(first.getDailyType(), taskTargetMap);
        dailyTaskItemService.saveBatchByTaskId(taskItemMap);
        dailyTaskUserService.saveBatchByTaskId(taskUserMap);
        this.fileInduce(taskFileRefIdMap);
        //发布事件
        Map<String, Object> variables = new HashMap<>();
        variables.put("userIdMap", taskUserIdMap);
        variables.put("sendTodo", true);
        businessStateMachine.pushEvent(taskIds, first.getDailyType(), ISSUE_TASK, variables);
        return taskIds;
    }

    /**
     * 校验草稿箱相关数据
     *
     * @param paramList
     *
     * @return
     */
    private List<DailyTaskAddParam> checkDraft(List<DailyTaskAddParam> paramList) {
        List<DailyTaskAddParam> result = new ArrayList<>();
        var draftTaskIds = new ArrayList<Long>();
        Map<Long, List<DailyTargetStandardResultParam>> taskTargetMap = new HashMap<>(paramList.size());
        Map<Long, List<Long>> taskItemMap = new HashMap<>(paramList.size());
        var taskFileRefIdMap = new HashMap<Long, List<Long>>();
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        List<DailyTaskUserPO> dailyTaskUserPOList = new ArrayList<>();
        for (DailyTaskAddParam taskParam : paramList) {
            DailyTaskPO task = this.buildTaskInfo(taskParam, taskFileRefIdMap);
            Long taskId = task.getId();
            //生成任务关联关系
            if (CollectionUtils.isNotEmpty(taskParam.getTargetResultList())) {
                taskTargetMap.put(taskId, taskParam.getTargetResultList());
            }
            if (CollectionUtils.isNotEmpty(taskParam.getTaskItemIdList())) {
                taskItemMap.put(taskId, taskParam.getTaskItemIdList());
            }
            //草稿箱
            if (Boolean.TRUE.equals(taskParam.getDraft())) {
                draftTaskIds.add(taskId);
                saveDraft(paramList, taskParam, taskId, dailyTaskUserPOList, task, currentUserId, taskTargetMap, taskItemMap, taskFileRefIdMap, draftTaskIds);
                continue;
            }
            result.add(taskParam);
        }
        return result;
    }

    /**
     * 保存草稿
     *
     * @param paramList
     * @param taskParam
     * @param taskId
     * @param dailyTaskUserPOList
     * @param task
     * @param currentUserId
     * @param taskTargetMap
     * @param taskItemMap
     * @param taskFileRefIdMap
     * @param draftTaskIds
     */
    private void saveDraft(List<DailyTaskAddParam> paramList, DailyTaskAddParam taskParam, Long taskId,
                           List<DailyTaskUserPO> dailyTaskUserPOList, DailyTaskPO task, Long currentUserId,
                           Map<Long, List<DailyTargetStandardResultParam>> taskTargetMap,
                           Map<Long, List<Long>> taskItemMap, HashMap<Long, List<Long>> taskFileRefIdMap, ArrayList<Long> draftTaskIds
                          ) {
        dailyTaskUserRuleService.saveRuleBindUser(taskId, taskParam.getParticipateList(), taskParam.getTargetResultList(), dailyTaskUserPOList, null);
        this.save(task);
        DailyTaskUserPO createUser = new DailyTaskUserPO();
        createUser.setTargetId(currentUserId);
        createUser.setMasterId(taskId);
        createUser.setSuperintendentType(ConditionTypeEnum.USER.name());
        createUser.setTaskUserType(DailyTaskUserTypeEnum.SUPERINTENDENT.name());
        dailyTaskUserPOList.add(createUser);
        DailyTaskAddParam first = paramList.getFirst();
        //保存任务关联关系
        dailyTaskTargetService.saveBatchByTaskId(first.getDailyType(), taskTargetMap);
        dailyTaskItemService.saveBatchByTaskId(taskItemMap);
        dailyTaskUserService.saveBatch(dailyTaskUserPOList);
        this.fileInduce(taskFileRefIdMap);
        //保存草稿事件
        businessStateMachine.pushEvent(draftTaskIds, first.getDailyType(), DRAFT_TASK);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editDailyTask(DailyTaskAddParam param) {
        if (Objects.nonNull(param.getId())) {
            Set<Long> refIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(param.getTargetResultList())) {
                refIds.addAll(param.getTargetResultList().stream()
                                      .filter(t -> Objects.nonNull(t.getParticipateCondition().getRuleId()))
                                      .map(t -> t.getParticipateCondition().getRuleId()).collect(Collectors.toSet()));
            }
            refIds.add(param.getId());
            this.deleteDailyTask(param.getId(), refIds, param.getTaskItemIdList());
        }
        if (CollectionUtils.isNotEmpty(param.getTargetResultList())) {
            Set<Long> targetId = param.getTargetResultList().stream()
                    .map(DailyTargetStandardResultParam::getTargetId)
                    .collect(Collectors.toSet());
            dailyTaskUserRuleService.remove(new LambdaQueryWrapper<DailyTaskUserRulePO>().in(DailyTaskUserRulePO::getRefId, targetId));
        }
        createDailyTaskBatch(Collections.singletonList(param));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDailyTask(Long id, Set<Long> refIds, List<Long> taskItemIdList) {
        dailyTaskTargetService.removeByTaskId(id);
        dailyTaskItemService.removeTaskItem(id, taskItemIdList);
        dailyTaskUserService.remove(new LambdaQueryWrapper<DailyTaskUserPO>().eq(DailyTaskUserPO::getId, id));
        if (CollectionUtils.isNotEmpty(refIds)) {
            dailyTaskUserRuleService.remove(new LambdaQueryWrapper<DailyTaskUserRulePO>().in(DailyTaskUserRulePO::getRefId, refIds));
        } else {
            List<DailyTaskTargetPO> targetByTaskId = dailyTaskTargetService.getTargetByTaskId(id);
            if (CollectionUtils.isNotEmpty(targetByTaskId)) {
                dailyTaskUserRuleService.remove(new LambdaQueryWrapper<DailyTaskUserRulePO>()
                                                        .in(DailyTaskUserRulePO::getRefId, targetByTaskId.stream()
                                                                .map(DailyTaskTargetPO::getRuleId)
                                                                .collect(Collectors.toSet())));
            }
        }

        return this.removeById(id);
    }

    @Override
    public List<DailyStandardResultDTO> getDailyTaskByTarget(Long taskId, Long targetId) {
        DailyTaskPO dailyTaskPO = this.lambdaQuery()
                .select(DailyTaskPO::getSequence)
                .like(DailyTaskPO::getId, taskId)
                .one();
        Assert.isNotFound(Objects.isNull(dailyTaskPO), "任务不存在");
        Set<Long> taskIds = new HashSet<>();
        if (StringUtils.isNotBlank(dailyTaskPO.getSequence())) {
            taskIds.addAll(Arrays.stream(dailyTaskPO.getSequence().split("-")).map(Long::valueOf).collect(Collectors.toSet()));
        }
        taskIds.add(taskId);
        return dailyTaskTargetService.getTargetStandardResult(taskIds, targetId);
    }

    @Override
    public List<Integer> getTaskTenantList() {
        return this.baseMapper.getTaskTenantList();
    }

    @Override
    public List<String> getTaskType() {
        return this.baseMapper.getTaskType();
    }

    @Override
    public List<DailyTaskPO> getOverDueTask(List<MessageSendConfig> messageSendConfigs) {
        var endDate = LocalDateTime.now();
        var startDate = DateUtils.offsetMinute(endDate, -1);
        Set<String> businessTypes = messageSendConfigs.stream().map(MessageSendConfig::getBusinessType).collect(Collectors.toSet());
        List<DailyTaskPO> taskPOList = this.lambdaQuery()
                .between(DailyTaskPO::getDeadline, startDate, endDate)
                .in(CollectionUtils.isNotEmpty(businessTypes), DailyTaskPO::getDailyType, businessTypes)
                .in(DailyTaskPO::getStatus, DailyTaskStateNodes.getNoticeMessageStatus(businessTypes)).list();
        return taskPOList;
    }

    @Override
    public List<DailyTaskPO> getTaskWillOverOrProgress(List<MessageSendConfig> messageSendConfigs) {
        List<DailyTaskPO> noticeTask = new ArrayList<>();
        for (MessageSendConfig messageSendConfig : messageSendConfigs) {
            if (Objects.isNull(messageSendConfig.getValue())) {
                continue;
            }
            var endDate = DateUtils.offsetDay(LocalDateTime.now(), messageSendConfig.getValue());
            var startDate = DateUtils.offsetMinute(endDate, -1);
            BigDecimal checkProcess = null;
            if (StringUtils.isNotBlank(messageSendConfig.getConfig())) {
                checkProcess = new BigDecimal(messageSendConfig.getConfig()).multiply(new BigDecimal("0.01"));
            }

            List<DailyTaskPO> taskPOList = this.lambdaQuery()
                    .between(DailyTaskPO::getDeadline, startDate, endDate)
                    .eq(Objects.nonNull(messageSendConfig.getBusinessType()), DailyTaskPO::getDailyType, messageSendConfig.getBusinessType())
                    .lt(Objects.nonNull(checkProcess), DailyTaskPO::getProgress, checkProcess)
                    .in(DailyTaskPO::getStatus, DailyTaskStateNodes.getNoticeMessageStatus(Collections.singleton(messageSendConfig.getBusinessType())))
                    .list();
            if (CollectionUtils.isEmpty(taskPOList)) {
                continue;
            }
            noticeTask.addAll(taskPOList);
        }
        return noticeTask;
    }

    @Override
    public Map<String, List<DailyPlanTargetResult>> relateDailyTaskApp(DailyTaskTargetQueryParam param) {
        return dailyTaskTargetService.getTargetByTaskIdsForApp(param).stream()
                .collect(Collectors.groupingBy(DailyPlanTargetResult::getTargetCategory));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskProgress(TransitionContext context) {
        List<Long> taskIds = context.getTaskIds();
        if (CollectionUtils.isEmpty(taskIds)) {
            return;
        }
        DailyTaskPO dailyTask = this.lambdaQuery()
                .eq(DailyTaskPO::getId, taskIds.getFirst())
                .one();
        if (Objects.isNull(dailyTask)) {
            return;
        }
        String[] sequenceId = dailyTask.getSequence().split("-");
        Set<DailyTaskPO> dailyTaskPOS = new HashSet<>();
        if (sequenceId.length > 0) {
            //获取父任务及所有子任务
            dailyTaskPOS.addAll(this.lambdaQuery()
                                        .like(DailyTaskPO::getSequence, sequenceId[0])
                                        .list());

        }
        Set<Long> taskIdList = dailyTaskPOS.stream().map(DailyTaskPO::getId).collect(Collectors.toSet());
        Map<Long, List<DailyTaskTargetPO>> groupByTaskIdMap = dailyTaskTargetService.getTargetByTaskIdListGroupByTaskId(taskIdList);
        //标准模式
        if (MapUtils.isEmpty(groupByTaskIdMap)) {
            for (DailyTaskPO dailyTaskPO : dailyTaskPOS) {
                dailyTaskPO.setProgress(BigDecimal.ONE);
            }
            this.updateBatchById(dailyTaskPOS);
            return;
        }
        for (DailyTaskPO taskPO : dailyTaskPOS) {
            //获取所有子任务id
            List<DailyTaskTargetPO> taskTargetPOList = groupByTaskIdMap.getOrDefault(taskPO.getId(), new ArrayList<>());

            Set<DailyTaskTargetPO> taskTargetPOS = taskTargetPOList.stream()
                    .filter(f -> !Boolean.TRUE.equals(f.getSnapshot()))
                    .collect(Collectors.toSet());
            String completeState = businessStateMachine.getCompleteState(taskPO.getDailyType(), HANDLE_TARGET);
            long completeCount = taskTargetPOS.stream().filter(t -> StringUtils.equals(t.getStatus(), completeState))
                    .count();
            BigDecimal process = BigDecimal.valueOf(completeCount)
                    .divide(BigDecimal.valueOf(taskPO.getTargetCount()), 2, RoundingMode.HALF_UP);
            taskPO.setProgress(process);
        }
        this.updateBatchById(dailyTaskPOS);
        context.setTopTaskId(Long.valueOf(sequenceId[0]));
        context.setTaskIds(new ArrayList<>(taskIdList));
        List<BaseState> entity = dailyTaskPOS.stream().map(t -> (BaseState) t).collect(Collectors.toList());
        context.setEntity(entity);
    }

    @Override
    public Boolean terminateDailyTask(Long id) {
        DailyTaskPO dailyTaskPO = this.getById(id);
        Assert.isParameterError(!DailyTaskStateNodes.TASK_STATE_NODE_WAIT.getStateCode().equals(dailyTaskPO.getStatus()),
                                "当前任务状态不允许终止");
        List<DailyTaskUserPO> targetUserIds = this.dailyTaskUserService.lambdaQuery()
                .select(DailyTaskUserPO::getTargetId, DailyTaskUserPO::getMasterId)
                .in(DailyTaskUserPO::getMasterId, dailyTaskPO.getId())
                .eq(DailyTaskUserPO::getTaskUserType, DailyTaskUserTypeEnum.ASSIGNEE.name())
                .list();
        Map<String, Object> variables = new HashMap<>();
        variables.put("targetUserIdMap", targetUserIds.stream()
                .collect(Collectors.groupingBy(DailyTaskUserPO::getMasterId, Collectors.mapping(DailyTaskUserPO::getTargetId, Collectors.toSet()))));
        businessStateMachine.pushEvent(id, dailyTaskPO.getDailyType(), TASK_TERMINATE, variables);
        return true;
    }

    @Override
    public Map<Long, Set<Long>> getTaskTargetUserMap(List<Long> taskIdList) {
        List<DailyTaskUserPO> targetUserIds = this.dailyTaskUserService.lambdaQuery()
                .select(DailyTaskUserPO::getTargetId, DailyTaskUserPO::getMasterId)
                .in(DailyTaskUserPO::getMasterId, taskIdList)
                .eq(DailyTaskUserPO::getTaskUserType, DailyTaskUserTypeEnum.ASSIGNEE.name())
                .list();
        if (CollectionUtils.isEmpty(targetUserIds)) {
            return new HashMap<>();
        }
        return targetUserIds.stream()
                .collect(Collectors.groupingBy(DailyTaskUserPO::getMasterId,
                                               Collectors.mapping(DailyTaskUserPO::getTargetId, Collectors.toSet())));
    }

    @Override
    public Map<Long, Long> getGridAbnormalTaskCount(List<Long> gridAreaIds, String dailyType) {
        Assert.isParameterError(StringUtils.isBlank(dailyType), "日常任务类型不能为空");
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return Map.of();
        }
        List<Long> validGridAreaIds = gridAreaIds.stream().filter(Objects::nonNull).distinct().toList();
        if (CollectionUtils.isEmpty(validGridAreaIds)) {
            return Map.of();
        }
        return dailyTaskMapper.selectGridAbnormalTaskCount(
                        validGridAreaIds,
                        dailyType)
                .stream()
                .filter(item -> item.getGridAreaId() != null)
                .collect(Collectors.toMap(
                        GridAbnormalTaskCountResult::getGridAreaId,
                        item -> Optional.ofNullable(item.getAbnormalTaskCount()).orElse(0L),
                        (v1, v2) -> v1));
    }

    @Override
    public DailyTaskSummaryResult getDailyTaskSummary(long taskId) {
        var resBuilder = DailyTaskSummaryResult.builder();
        var task = getById(taskId);
        Assert.isNotFound(task, "任务不存在！");
        //        Set<Long> allTask = new HashSet<>();
        //        Set<Long> childTask = getChildTask(taskId, task.getLevel());
        //        allTask.addAll(childTask);
        //        allTask.add(taskId);
        var targets = Objects.requireNonNullElse(dailyTaskTargetService.getTargetByTaskId(taskId), List.<DailyTaskTargetPO>of());
        //过滤子任务快照
        //        targets = targets.stream().filter(t -> !childTask.contains(t.getTaskId()) ||
        //                                               (childTask.contains(t.getTaskId()) && !Boolean.TRUE.equals(t.getSnapshot())))
        //                .collect(Collectors.toList());
        resBuilder.taskName(task.getName());
        resBuilder.status(task.getStatus());
        resBuilder.progress(task.getProgress());

        resBuilder.total((long) targets.size());
        var checked = targets.stream()
                .filter(t -> Objects.equals(t.getStatus(), DailyTaskStateNodes.TASK_STATE_NODE_DONE.getStateCode()))
                .count();
        resBuilder.checked(checked);
        if (CollectionUtils.isNotEmpty(targets)) {
            var param = new DailyRecordTargetBatchParam();
            param.setTargetIds(ListUtils.convertItem(targets, DailyTaskTargetPO::getTargetId));
            param.setExecutorResult(DailyExecutionResultEnum.ABNORMAL.name());
            param.setTaskIds(ListUtils.convertItem(targets, DailyTaskTargetPO::getTaskId));
            var records = dailyRecordTargetService.batchRecordTargetErrorList(param);
            resBuilder.abnormal((long) records.size());
        }
        return resBuilder.build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean submitDailyTask(DailyTaskSubmitParam param) {
        DailyTaskPO task = this.getById(param.getTaskId());
        Assert.isNotFound(task, "任务不存在");
        List<DailyTaskTargetPO> targets = dailyTaskTargetService.getTargetByTaskId(task.getId());
        if (CollectionUtils.isEmpty(targets)) {
            throw new ServiceException("任务未关联对象无法提交");
        }
        //获取当前用户执行进度
        if (Objects.nonNull(task.getRequireProgress())) {
            long handleCount;
            BigDecimal targetCount;
            //存在父级按当前任务未分配的对象计算
            if (Objects.nonNull(task.getParentId())) {
                // taskId为空或者等于当前任务
                targetCount = BigDecimal.valueOf(targets.stream().filter(t ->
                                                                                 Objects.isNull(t.getTaskId()) ||
                                                                                 Objects.equals(task.getId(), t.getTaskId()))
                                                         .count());
                var recordParam = new DailyRecordTargetBatchParam();
                recordParam.setTargetIds(ListUtils.convertItem(targets, DailyTaskTargetPO::getTargetId));
                recordParam.setTaskIds(Collections.singletonList(task.getId()));
                recordParam.setExecutor(SecurityContextHolder.getPrincipal().getAccount());
                handleCount = dailyRecordTargetService.batchRecordTargetErrorList(recordParam).size();
            } else {
                //主任务
                targetCount = BigDecimal.valueOf(task.getTargetCount());
                handleCount = targets.stream()
                        .filter(t -> Objects.equals(t.getStatus(), DailyTaskStateNodes.TASK_STATE_NODE_DONE.getStateCode()))
                        .count();
            }
            if (handleCount < task.getRequireProgress()) {
                throw new ServiceException(
                        "未达到任务要求进度" + BigDecimal.valueOf(task.getRequireProgress())
                                .divide(BigDecimal.valueOf(task.getTargetCount()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) +
                        "%" + "不允许提交");
            }
        }
        Integer noCompleteTargetNum = 0;
        if (Objects.nonNull(task.getParentId())) {
            noCompleteTargetNum = dailyTaskTargetService.handleTaskTarget(param.getTaskId(), task.getParentId(), param.getDailyType());
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("noCompleteTargetNum", noCompleteTargetNum);
        businessStateMachine.pushEvent(param.getTaskId(), task.getDailyType(), TASK_COMPLETE, variables);
        return true;
    }

    @Override
    public List<Long> getDailyTaskTargetIdList(Long taskId) {
        return dailyTaskTargetService.getTargetTagIdList(taskId);
    }

    @Override
    public Boolean assignDailyTask(DailyTaskAssignParam assignParam) {
        DailyTaskPO dailyTaskPO = this.lambdaQuery()
                .eq(DailyTaskPO::getId, assignParam.getTaskId())
                .one();
        Assert.isNotFound(dailyTaskPO, "任务不存在无法分发");
        Assert.isParameterError(CollectionUtils.isEmpty(assignParam.getAssignUserParams()), "请选择分发人员");
        //获取任务关联的对象
        var distributeDailyTaskTargetList = dailyTaskTargetService.getNoDistributedTarget(assignParam.getTaskId(), dailyTaskPO.getDailyType());
        if (CollectionUtils.isEmpty(distributeDailyTaskTargetList)) {
            throw new ServiceException("任务未关联对象无法分发");
        }
        //生成用户对应任务
        List<Long> userTaskIds = new ArrayList<>();
        Map<Long, Set<DailyParticipateParam>> taskUserMap = new HashMap<>();
        Map<Long, Set<Long>> taskUserIdMap = new HashMap<>();
        Map<Long, Set<DailyTaskTargetPO>> dailyTaskTargetRefMap = new HashMap<>();
        for (DailyTaskAssignUserParam assignUserParam : assignParam.getAssignUserParams()) {
            DailyTaskPO taskPO = getDailyTaskPO(dailyTaskPO);
            taskPO.setTargetCount(distributeDailyTaskTargetList.size());
            taskPO.setParentId(dailyTaskPO.getId());
            //默认100%
            taskPO.setRequireProgress(BigDecimal.valueOf(taskPO.getTargetCount())
                                              .multiply(assignUserParam.getTaskPercent())
                                              .longValue());
            userTaskIds.add(taskPO.getId());
            this.save(taskPO);
            //任务关联人员
            taskUserMap.computeIfAbsent(taskPO.getId(), v -> new HashSet<>())
                    .addAll(getTaskUser(assignUserParam.getUserId()));
            taskUserIdMap.put(taskPO.getId(), Collections.singleton(assignUserParam.getUserId()));
            dailyTaskTargetRefMap.put(taskPO.getId(), new HashSet<>(distributeDailyTaskTargetList));
        }
        dailyTaskUserService.saveBatchByTaskId(taskUserMap);
        //更新对象关联任务
        dailyTaskTargetService.distributedTaskTarget(dailyTaskTargetRefMap, true);
        dailyTaskTargetService.updateBatchById(distributeDailyTaskTargetList);
        Map<String, Object> params = new HashMap<>();
        params.put("targetNum", 0);
        params.put("userIdMap", taskUserIdMap);
        params.put("sendTodo", true);
        params.put("sourceUsers", getSourceUsers(Collections.singletonList(assignParam.getTaskId())));
        //任务下发动作
        businessStateMachine.pushEvent(userTaskIds, dailyTaskPO.getDailyType(), ISSUE_TASK, params);
        //任务分发动作(直接完成当前待办)
        businessStateMachine.pushEvent(assignParam.getTaskId(), dailyTaskPO.getDailyType(), DISTRIBUTE, params);
        return true;
    }

    @Override
    public BigDecimal getDailyTaskPercent(Long taskId) {
        DailyTaskPO dailyTaskPO = this.lambdaQuery().eq(DailyTaskPO::getId, taskId).one();
        Assert.isNotFound(dailyTaskPO, "任务不存在");
        Long topTaskId = Long.valueOf(dailyTaskPO.getSequence().split("-")[0]);
        if (!Objects.equals(taskId, topTaskId)) {
            dailyTaskPO = this.lambdaQuery().eq(DailyTaskPO::getId, topTaskId).one();
        }
        List<DailyTaskPO> dailyTaskPOS = this.lambdaQuery()
                .eq(DailyTaskPO::getParentId, dailyTaskPO.getId())
                .isNotNull(DailyTaskPO::getRequireProgress)
                .list();
        //已分配个数
        BigDecimal assignProgress = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(dailyTaskPOS)) {
            assignProgress = dailyTaskPOS.stream().map(t -> BigDecimal.valueOf(t.getRequireProgress()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        // 还能分发的百分比 = 总个数/已分发个数 + 已处理个数
        BigDecimal divide = assignProgress.divide(BigDecimal.valueOf(dailyTaskPO.getTargetCount()), 2, RoundingMode.HALF_UP);
        return BigDecimal.ONE.subtract(divide);
    }

    @Override
    public List<DailyPlanTargetResult> relateDailyTaskPc(DailyTaskTargetQueryParam param) {
        return dailyTaskTargetService.getTargetByTaskIdsForApp(param);
    }

    @Override
    public DailyTaskTargetResult getDailyTaskTargetRelate(Long taskId, Long id) {
        return dailyTaskTargetService.getDailyTaskTargetRelate(taskId, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean feedbackDailyTask(DailyTaskFeedbackParam param) {
        DailyTaskPO task = this.getById(param.getTaskId());
        Assert.isNotFound(task, "任务不存在");
        Assert.isParameterError(!Boolean.TRUE.equals(task.getContinuity()), "非持续性任务不支持反馈");

        // 校验当前用户是否为执行人

        // 保存反馈记录
        DailyTaskFeedbackPO feedbackPO = DailyTaskFeedbackPO.builder()
                .taskId(param.getTaskId())
                .schedule(param.getSchedule())
                .description(param.getDescription())
                .files(param.getFiles())
                .build();
        dailyTaskFeedbackService.save(feedbackPO);

        // 更新任务进度
        BigDecimal progress = param.getSchedule()
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        task.setProgress(progress);
        this.updateById(task);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleGridAreaMemberSet(GridAreaMemberSetMessage message) {
        if (message == null || message.getGridAreaId() == null || CollectionUtils.isEmpty(message.getMemberIds())) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        Set<Long> memberIds = message.getMemberIds().stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(memberIds)) {
            return;
        }
        String dailyType = StringUtils.isBlank(message.getDailyType()) ? DailyConstants.GRID_MANAGE : message.getDailyType();
        List<DailyTaskPO> tasks = dailyTaskMapper.selectGridAreaOpenTasks(
                message.getGridAreaId(), dailyType, now, businessStateMachine.getNoFinalStatus(dailyType));
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        Set<Long> onDutyUserIds =
                tasks.stream().anyMatch(task -> Boolean.TRUE.equals(task.getIsOnDuty())) ? getOnDutyUserIds(memberIds, now)
                                                                                         : Collections.emptySet();
        Set<Long> openTaskIds = tasks.stream().map(DailyTaskPO::getId).collect(Collectors.toSet());
        Map<Long, Set<Long>> existingUserMap = Optional.ofNullable(dailyTaskUserService.lambdaQuery()
                                                                           .select(DailyTaskUserPO::getMasterId, DailyTaskUserPO::getTargetId)
                                                                           .in(DailyTaskUserPO::getMasterId, openTaskIds)
                                                                           .in(DailyTaskUserPO::getTargetId, memberIds)
                                                                           .eq(DailyTaskUserPO::getTaskUserType, DailyTaskUserTypeEnum.ASSIGNEE.name())
                                                                           .eq(DailyTaskUserPO::getSuperintendentType, ConditionTypeEnum.USER.name())
                                                                           .list())
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.groupingBy(DailyTaskUserPO::getMasterId,
                                               Collectors.mapping(DailyTaskUserPO::getTargetId, Collectors.toSet())));
        List<DailyTaskUserPO> saveUsers = new ArrayList<>();
        Map<Long, Set<Long>> newTaskUserMap = new HashMap<>();
        for (DailyTaskPO task : tasks) {
            Set<Long> candidateUserIds = Boolean.TRUE.equals(task.getIsOnDuty()) ? onDutyUserIds : memberIds;
            if (CollectionUtils.isEmpty(candidateUserIds)) {
                continue;
            }
            Set<Long> existingUserIds = existingUserMap.getOrDefault(task.getId(), Collections.emptySet());
            for (Long userId : candidateUserIds) {
                if (existingUserIds.contains(userId)) {
                    continue;
                }
                saveUsers.add(DailyTaskUserPO.builder()
                                      .id(IdWorker.getId())
                                      .masterId(task.getId())
                                      .taskUserType(DailyTaskUserTypeEnum.ASSIGNEE.name())
                                      .superintendentType(ConditionTypeEnum.USER.name())
                                      .targetId(userId)
                                      .build());
                newTaskUserMap.computeIfAbsent(task.getId(), k -> new HashSet<>()).add(userId);
            }
        }
        if (CollectionUtils.isEmpty(saveUsers)) {
            return;
        }
        dailyTaskUserService.saveBatch(saveUsers);
        for (DailyTaskPO task : tasks) {
            Set<Long> userIds = newTaskUserMap.get(task.getId());
            if (CollectionUtils.isNotEmpty(userIds)) {
                sendTaskIssueMessageAndTodo(task, userIds);
            }
        }
    }

    private Map<Long, Set<Long>> getGridUserIdMap(Set<Long> gridAreaIds) {
        if (CollectionUtils.isEmpty(gridAreaIds)) {
            return Collections.emptyMap();
        }
        return Optional.ofNullable(gridAreaConfigClient.getGridAreaConfigWithGridLeaderList(new ArrayList<>(gridAreaIds)))
                .orElse(Collections.emptyMap());
    }

    private Map<Long, Set<Long>> filterOnDutyGridUserIdMap(Map<Long, Set<Long>> gridUserIdMap, Set<Long> gridAreaIds,
                                                           LocalDateTime checkTime
                                                          ) {
        if (MapUtils.isEmpty(gridUserIdMap) || CollectionUtils.isEmpty(gridAreaIds)) {
            return Collections.emptyMap();
        }
        Set<Long> userIds = gridAreaIds.stream()
                .map(gridUserIdMap::get)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Set<Long> onDutyUserIds = getOnDutyUserIds(userIds, checkTime);
        if (CollectionUtils.isEmpty(onDutyUserIds)) {
            return Collections.emptyMap();
        }
        return gridUserIdMap.entrySet().stream()
                .filter(entry -> gridAreaIds.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                                          entry -> entry.getValue().stream()
                                                  .filter(onDutyUserIds::contains)
                                                  .collect(Collectors.toSet())));
    }

    private Set<Long> getOnDutyUserIds(Set<Long> userIds, LocalDateTime checkTime) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptySet();
        }
        ArrangeWorkParam arrangeWorkParam = new ArrangeWorkParam();
        arrangeWorkParam.setUserIds(userIds);
        arrangeWorkParam.setStartDate(checkTime);
        try {
            return new HashSet<>(Optional.ofNullable(dailyAttendanceClient.postOnDutyPersonnel(arrangeWorkParam))
                                         .orElse(Collections.emptyList()));
        } catch (Exception e) {
            log.warn("获取在岗人员失败: {}", e.getMessage(), e);
            return Collections.emptySet();
        }
    }

    private void sendTaskIssueMessageAndTodo(DailyTaskPO task, Set<Long> userIds) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("taskName", task.getName());
        MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
        notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
        notifyTarget.setIds(userIds);
        MessageSceneSendRequest message = MessageSceneSendRequest.builder()
                .tenant(task.getTenant())
                .applicationCode(task.getDailyType())
                .msgSceneCode(DailyTaskNoticeMessageEnum.DAILY_TASK_GENERATE.name())
                .businessId(String.valueOf(task.getId()))
                .tmpParams(paramMap)
                .messageNotifyTargets(Collections.singletonList(notifyTarget))
                .build();
        try {
            MessageSendResponse messageSendResponse = messageSendClient.sceneSend(message);
            log.info("网格日常任务补发消息结果,{}", JsonUtils.serialize(messageSendResponse));
        } catch (Exception e) {
            log.error("网格日常任务补发消息失败", e);
        }

        TodoTaskInitMessage todoTaskInitMessage = new TodoTaskInitMessage();
        todoTaskInitMessage.setApplicationCode(task.getDailyType());
        todoTaskInitMessage.setApplicationName(task.getDailyType());
        todoTaskInitMessage.setBusinessKey(String.valueOf(task.getId()));
        todoTaskInitMessage.setBusinessType(task.getDailyType());
        todoTaskInitMessage.setBusinessTypeName(DailyTaskBusinessTypeEnum.valueOf(task.getDailyType()).getApplicationName());
        todoTaskInitMessage.setBusinessStatus(task.getStatus());
        todoTaskInitMessage.setBusinessStatusName(DailyTaskStateNodes.getStateNode(task.getStatus()).getStateName());
        todoTaskInitMessage.setTaskName(task.getName());
        todoTaskInitMessage.setTaskTime(task.getCreateTime());
        todoTaskInitMessage.setOperationType(TodoTaskFlowOperationEnum.INSERT);
        todoTaskInitMessage.setUserIds(new ArrayList<>(userIds));
        RocketMQContextHolder.syncSendOrderly(TodoTaskInitMessage.TOPIC, todoTaskInitMessage, String.valueOf(task.getId()));

        WorkTaskInitMessage workTaskInitMessage = new WorkTaskInitMessage();
        workTaskInitMessage.setApplicationCode(task.getDailyType());
        workTaskInitMessage.setApplicationName(task.getDailyType());
        workTaskInitMessage.setBusinessKey(String.valueOf(task.getId()));
        workTaskInitMessage.setBusinessType(task.getDailyType());
        workTaskInitMessage.setBusinessTypeName(task.getDailyType());
        workTaskInitMessage.setBusinessStatus(task.getStatus());
        todoTaskInitMessage.setBusinessTypeName(DailyTaskBusinessTypeEnum.valueOf(task.getDailyType()).getApplicationName());
        workTaskInitMessage.setTaskName(task.getName());
        workTaskInitMessage.setTaskTime(task.getCreateTime());
        workTaskInitMessage.setOperationType(WorkTaskFlowOperationEnum.INSERT);
        workTaskInitMessage.setBusinessPrincipal(JsonUtils.serialize(task));
        workTaskInitMessage.setUserIds(new ArrayList<>(userIds));
        RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, workTaskInitMessage, String.valueOf(task.getId()));
    }

    /**
     * 单个任务用户
     *
     * @param userId
     *
     * @return
     */
    private List<DailyParticipateParam> getTaskUser(Long userId) {
        List<DailyParticipateParam> taskUserParam = new ArrayList<>();
        DailyParticipateParam dailyParticipateParam = new DailyParticipateParam();
        dailyParticipateParam.setTargetId(userId);
        dailyParticipateParam.setParticipateType(ConditionTypeEnum.USER);
        dailyParticipateParam.setTaskUserType(DailyTaskUserTypeEnum.ASSIGNEE.name());
        taskUserParam.add(dailyParticipateParam);
        DailyParticipateParam createUser = new DailyParticipateParam();
        createUser.setTargetId(SecurityContextHolder.getPrincipal().getId());
        createUser.setParticipateType(ConditionTypeEnum.USER);
        createUser.setTaskUserType(DailyTaskUserTypeEnum.SUPERINTENDENT.name());
        taskUserParam.add(createUser);
        return taskUserParam;
    }

    @Override
    public Map<Long, BigDecimal> getTaskProgress(List<Long> taskIdList) {
        if (CollectionUtils.isEmpty(taskIdList)) {
            return new HashMap<>();
        }
        return this.lambdaQuery()
                .select(DailyTaskPO::getId, DailyTaskPO::getProgress)
                .in(DailyTaskPO::getId, taskIdList)
                .list().stream().collect(Collectors.toMap(DailyTaskPO::getId, DailyTaskPO::getProgress));
    }

    @Override
    public DailyTaskHomePageResult statisticsDailyTask(DailyBaseStatisticsParam param) {
        Long totalNum = this.baseMapper.selectTaskNumGroupByStatus(param, null);

        Long processNum = this.baseMapper.selectTaskNumGroupByStatus(param, Collections.singletonList(DailyTaskStateNodes.TASK_STATE_NODE_PROCESSING.getStateCode()));
        Long waitNum = this.baseMapper.selectTaskNumGroupByStatus(param, Collections.singletonList(DailyTaskStateNodes.TASK_STATE_NODE_WAIT.getStateCode()));
        Period previousPeriod = Period.getPreviousPeriod(param.getType(), param.getPeriod());
        param.setPeriod(previousPeriod);
        Long previousTotalNum = this.baseMapper.selectTaskNumGroupByStatus(param, null);

        Long previousProcessNum = this.baseMapper.selectTaskNumGroupByStatus(param, Collections.singletonList(DailyTaskStateNodes.TASK_STATE_NODE_PROCESSING.getStateCode()));
        Long previousWaitNum = this.baseMapper.selectTaskNumGroupByStatus(param, Collections.singletonList(DailyTaskStateNodes.TASK_STATE_NODE_WAIT.getStateCode()));
        return DailyTaskHomePageResult.builder()
                .dailyTaskNum(totalNum)
                .dailyTaskNumRate(MathUtils.calculateRingRatioPercentage(totalNum, previousTotalNum))
                .processTaskNum(processNum)
                .processTaskNumRate(MathUtils.calculateRingRatioPercentage(processNum, previousProcessNum))
                .uncompletedTaskNum(waitNum + processNum)
                .uncompletedTaskNumRate(MathUtils.calculateRingRatioPercentage(waitNum + processNum, previousWaitNum + previousProcessNum))
                .build();
    }

    /**
     * 获取任务下该档位 档主的其他[执行内容相同的]档位
     * 注意不包含自己
     *
     * @param param 条件
     *
     * @return 对象(档位)列表
     */
    @Override
    public List<DailyTargetDTO> getTaskOtherTarget(DailyTaskOtherTargetParam param) {
        if (param == null || param.getTaskId() == null || param.getTargetId() == null || CollectionUtils.isEmpty(param.getItemIdList())) {
            return CollectionUtils.emptyList();
        }
        return dailyTaskTargetService.getTaskOtherTarget(param.getTaskId(), param.getTargetId(), param.getItemIdList());
    }

    private DailyTaskPO buildTaskInfo(DailyTaskAddParam taskParam, HashMap<Long, List<Long>> taskFileRefIdMap) {
        Long fileRefKey = null;
        if (CollectionUtils.isNotEmpty(taskParam.getFileIds())) {
            fileRefKey = IdWorker.getId();
            taskFileRefIdMap.put(fileRefKey, taskParam.getFileIds());
        }
        var targetResultSize =
                CollectionUtils.isEmpty(taskParam.getTargetResultList()) ? 0 : taskParam.getTargetResultList().size();
        return DailyTaskPO.builder().
                id(Objects.nonNull(taskParam.getId()) ? taskParam.getId() : IdWorker.getId())
                .name(taskParam.getName())
                .planId(taskParam.getPlanId())
                .dailyType(taskParam.getDailyType())
                .taskDay(taskParam.getTaskDay())
                .taskHour(taskParam.getTaskHour())
                .taskMinute(taskParam.getTaskMinute())
                .deadline(getTaskDeadline(taskParam.getTaskDay(), taskParam.getTaskHour(), taskParam.getTaskMinute(), LocalDateTime.now()))
                .targetCount(targetResultSize)
                .urgencyId(taskParam.getUrgencyId())
                .taskCategoryId(taskParam.getTaskCategoryId())
                .fileRefKey(fileRefKey)
                .description(taskParam.getDescription())
                .method(taskParam.getMethod())
                .isOnDuty(taskParam.getIsOnDuty())
                .requireProgress(Objects.nonNull(taskParam.getRequireProgress()) ? BigDecimal.valueOf(targetResultSize)
                        .multiply(taskParam.getRequireProgress()).setScale(0, RoundingMode.UP).longValue() : null)
                .taskType(taskParam.getTaskType().name())
                .continuity(taskParam.getContinuity())
                .build();
    }

    private List<DailyTaskAddParam> getBuildTaskUser(List<DailyTaskAddParam> paramList) {
        List<DailyTaskAddParam> result = new ArrayList<>();
        List<UserRuleConditionParam> userRuleConditionParams = new ArrayList<>();
        for (DailyTaskAddParam dailyTaskAddParam : paramList) {
            if (CollectionUtils.isNotEmpty(dailyTaskAddParam.getTargetResultList())) {
                var conditionParamListMap = dailyTaskAddParam
                        .getTargetResultList().stream()
                        .collect(Collectors.groupingBy(DailyTargetStandardResultParam::getParticipateCondition));
                for (var conditionParam : conditionParamListMap.entrySet()) {
                    ensureRuleId(conditionParam.getKey());
                    if (!hasGridArea(conditionParam.getKey())) {
                        userRuleConditionParams.add(buildRuleConditionParam(dailyTaskAddParam, conditionParam.getKey()));
                    }
                    conditionParam.getValue().forEach(t -> {
                        t.getParticipateCondition()
                                .setRuleId(conditionParam.getKey().getRuleId());
                        t.getParticipateCondition()
                                .setParticipateParams(conditionParam.getKey().getParticipateParams());
                    });
                }
            }
            if (CollectionUtils.isNotEmpty(dailyTaskAddParam.getParticipateList())) {
                for (var conditionParam : dailyTaskAddParam.getParticipateList()) {
                    ensureRuleId(conditionParam);
                    if (!hasGridArea(conditionParam)) {
                        userRuleConditionParams.add(buildRuleConditionParam(dailyTaskAddParam, conditionParam));
                    }
                }
            }
        }
        Map<Long, Set<Long>> userByRuleMap = CollectionUtils.isEmpty(userRuleConditionParams) ? new HashMap<>()
                                                                                              : userClient.findUserByRule(userRuleConditionParams);
        //构造任务生成逻辑
        for (DailyTaskAddParam dailyTaskAddParam : paramList) {
            if (CollectionUtils.isEmpty(dailyTaskAddParam.getTargetResultList()) &&
                CollectionUtils.isEmpty(dailyTaskAddParam.getParticipateList())) {
                throw new ServiceException("任务参与人和对象不能同时为空");
            }
            // 只会存在一个
            if (CollectionUtils.isNotEmpty(dailyTaskAddParam.getTargetResultList())) {
                var conditionParamListMap = dailyTaskAddParam
                        .getTargetResultList().stream()
                        .collect(Collectors.groupingBy(DailyTargetStandardResultParam::getParticipateCondition));
                for (var conditionParam : conditionParamListMap.entrySet()) {
                    if (hasGridArea(conditionParam.getKey())) {
                        DailyTaskAddParam taskParam = DailyTaskAddParamCovert.covertNew(dailyTaskAddParam);
                        taskParam.setId(IdWorker.getId());
                        taskParam.setGridAreaIds(getGridAreaIds(conditionParam.getKey()));
                        taskParam.setTargetResultList(conditionParam.getValue());
                        result.add(taskParam);
                        continue;
                    }
                    Set<Long> taskUserId = userByRuleMap.get(conditionParam.getKey().getRuleId());
                    if (CollectionUtils.isEmpty(taskUserId)) {
                        continue;
                    }
                    DailyTaskAddParam taskParam = DailyTaskAddParamCovert.covertNew(dailyTaskAddParam);
                    taskParam.setId(IdWorker.getId());
                    taskParam.setTaskUserIds(taskUserId);
                    taskParam.setTargetResultList(conditionParam.getValue());
                    result.add(taskParam);
                }
            }
            if (CollectionUtils.isNotEmpty(dailyTaskAddParam.getParticipateList())) {
                for (var conditionParam : dailyTaskAddParam.getParticipateList()) {
                    if (hasGridArea(conditionParam)) {
                        DailyTaskAddParam taskParam = DailyTaskAddParamCovert.covertNew(dailyTaskAddParam);
                        taskParam.setId(IdWorker.getId());
                        taskParam.setGridAreaIds(getGridAreaIds(conditionParam));
                        result.add(taskParam);
                        continue;
                    }
                    Set<Long> taskUserId = userByRuleMap.get(conditionParam.getRuleId());
                    if (CollectionUtils.isEmpty(taskUserId)) {
                        continue;
                    }
                    DailyTaskAddParam taskParam = DailyTaskAddParamCovert.covertNew(dailyTaskAddParam);
                    taskParam.setId(IdWorker.getId());
                    taskParam.setTaskUserIds(taskUserId);
                    result.add(taskParam);
                }
            }
        }
        return result;
    }

    private static void ensureRuleId(DailyParticipateConditionParam conditionParam) {
        Long ruleId = conditionParam.getRuleId();
        if (Objects.isNull(ruleId)) {
            ruleId = IdWorker.getId();
            conditionParam.setRuleId(ruleId);
        }
    }

    private static boolean hasGridArea(DailyParticipateConditionParam conditionParam) {
        return Optional.ofNullable(conditionParam.getParticipateParams()).orElse(Collections.emptyList())
                .stream().anyMatch(t -> ConditionTypeEnum.GRID_AREA.equals(t.getParticipateType()));
    }

    private static Set<Long> getGridAreaIds(DailyParticipateConditionParam conditionParam) {
        return Optional.ofNullable(conditionParam.getParticipateParams()).orElse(Collections.emptyList())
                .stream()
                .filter(t -> ConditionTypeEnum.GRID_AREA.equals(t.getParticipateType()))
                .map(DailyParticipateParam::getTargetId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private static UserRuleConditionParam buildRuleConditionParam(DailyTaskAddParam dailyTaskAddParam,
                                                                  DailyParticipateConditionParam conditionParam
                                                                 ) {
        ensureRuleId(conditionParam);
        UserRuleConditionParam param = new UserRuleConditionParam();
        param.setRuleId(conditionParam.getRuleId());
        param.setOnDuty(dailyTaskAddParam.getIsOnDuty());
        param.setAbnormalUserIdList(conditionParam.getAbnormalUserIdList());
        List<ConditionParam> conditionParams = new ArrayList<>();
        for (var participateParam : Optional.ofNullable(conditionParam.getParticipateParams()).orElse(Collections.emptyList())) {
            conditionParams.add(new ConditionParam(participateParam.getParticipateType(), participateParam.getTargetId()));
        }
        param.setParticipateParams(conditionParams);
        return param;
    }

    /**
     * 文件持久化
     *
     * @param taskFileRefIdMap
     */
    private void fileInduce(Map<Long, List<Long>> taskFileRefIdMap) {
        if (MapUtils.isEmpty(taskFileRefIdMap)) {
            return;
        }
        var fileRefParam = new ArrayList<FileRefCreateParam>();
        taskFileRefIdMap.forEach((refKey, fileList) -> {
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setBusinessKey(refKey);
            fileRefCreateParam.setFileIdList(fileList);
            fileRefCreateParam.setSceneCode(DailyConstants.DAILY_TASK_CREATE_FILE);
            fileRefParam.add(fileRefCreateParam);
        });
        fileRefClient.refInduceBatch(fileRefParam);
    }

    private LocalDateTime getTaskDeadline(Integer taskDay, Integer taskHour, Integer taskMinute, LocalDateTime now) {
        var taskDeadline = DateUtils.newLocalDateTime(now);
        if (taskDay != null) {
            taskDeadline = DateUtils.offset(taskDeadline, ChronoUnit.DAYS, taskDay);
        }
        if (taskHour != null) {
            taskDeadline = DateUtils.offset(taskDeadline, ChronoUnit.HOURS, taskHour);
        }
        if (taskMinute != null) {
            taskDeadline = DateUtils.offset(taskDeadline, ChronoUnit.MINUTES, taskMinute);
        }
        return taskDeadline;
    }

    @Override
    public boolean checkTaskCompleted(TransitionContext context) {
        Map<Long, BigDecimal> taskProgress =
                this.getTaskProgress(context.getTaskIds());
        if (MapUtils.isEmpty(taskProgress)) {
            return true;
        }
        BaseState currentEntity = context.getCurrentEntity();
        return BigDecimal.ONE.compareTo(taskProgress.get(currentEntity.getId())) == 0;
    }

    @Override
    public boolean checkTaskDistribute(TransitionContext context) {
        Map<String, Object> params = context.getParams();
        if (MapUtils.isEmpty(params)) {
            return false;
        }
        //剩余对象数
        long count = Long.parseLong(params.getOrDefault("targetNum", "0").toString());
        List<BaseState> entity = context.getEntity();
        DailyTaskPO po = (DailyTaskPO) entity.getFirst();
        return count > 0 || Objects.isNull(po.getParentId());
    }

    @Override
    public boolean checkTaskDistributeComplete(TransitionContext context) {
        Map<String, Object> params = context.getParams();
        if (MapUtils.isEmpty(params)) {
            return false;
        }
        //剩余对象数
        long count = Long.parseLong(params.getOrDefault("targetNum", "0").toString());
        List<BaseState> entity = context.getEntity();
        DailyTaskPO po = (DailyTaskPO) entity.getFirst();
        return count == 0 && Objects.nonNull(po.getParentId());
    }

    @Override
    public Pagination<DailyTaskTargetResult> relateDailyTask(DailyTaskTargetQueryParam param) {
        DailyTaskPO dailyTaskPO = this.getById(param.getTaskId());
        Assert.isNotFound(Objects.isNull(dailyTaskPO), "任务不存在");
        param.setDailyType(dailyTaskPO.getDailyType());
        Pagination<DailyTaskTargetResult> resultPagination = dailyTaskTargetService.getTargetByTaskId(param);
        if (Objects.isNull(resultPagination) || CollectionUtils.isEmpty(resultPagination.getRecords())) {
            return resultPagination;
        }

        //        Map<Long, List<Long>> executeUserId = Optional.ofNullable(dailyTaskUserService.lambdaQuery()
        //                                                                          .in(DailyTaskUserPO::getMasterId, param.getTaskIds())
        //                                                                          .eq(DailyTaskUserPO::getTaskUserType, DailyTaskUserTypeEnum.ASSIGNEE.name())
        //                                                                          .list()).orElse(new ArrayList<>())
        //                .stream()
        //                .collect(Collectors.groupingBy(DailyTaskUserPO::getMasterId, Collectors.mapping(DailyTaskUserPO::getTargetId, Collectors.toList())));
        //        Map<Long, String> userMap = userClient.listEnableUserByIds(executeUserId.values()
        //                                                                           .stream().flatMap(Collection::stream)
        //                                                                           .collect(Collectors.toSet()))
        //                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        String completeState = businessStateMachine.getCompleteState(param.getDailyType(), HANDLE_TARGET);
        List<String> noFinalStatus = businessStateMachine.getNoFinalStatus(param.getDailyType());

        //对象规则
        Map<Long, Long> targetRuleMap = resultPagination.getRecords().stream()
                .filter(t -> Objects.nonNull(t.getRuleId()))
                .collect(Collectors.toMap(DailyTaskTargetResult::getId, DailyTaskTargetResult::getRuleId));
        Map<Long, List<DailyParticipateConditionResult>> ruleMap = dailyTaskUserRuleService.getRuleByRefIds(new HashSet<>(targetRuleMap.values()), DailyDomainTypeEnum.DAILY_TASK);
        for (DailyTaskTargetResult record : resultPagination.getRecords()) {
            record.setAllowAssign(!StringUtils.equals(completeState, record.getStatus())
                                  && Objects.equals(record.getTaskId(), param.getTaskId()));
            //            List<Long> userId = executeUserId.get(record.getTaskId());
            //            if (CollectionUtils.isNotEmpty(userId) && userId.size() == 1) {
            //                record.setExecutorId(userId.getFirst());
            //                record.setExecutorName(userMap.get(userId.getFirst()));
            //            }
            if (targetRuleMap.containsKey(record.getId())) {
                record.setRuleList(ruleMap.get(targetRuleMap.get(record.getId())));
            }
            //允许处理
            boolean executor = Objects.isNull(record.getTaskId()) || Objects.equals(record.getTaskId(), param.getTaskId());
            record.setAllowedHande(
                    !StringUtils.equals(completeState, record.getStatus()) && executor && noFinalStatus.contains(dailyTaskPO.getStatus()));
        }

        return resultPagination;
    }

    private void setChildTaskId(DailyTaskTargetQueryParam param, Integer level) {
        Set<Long> taskIds = getChildTask(param.getTaskId(), level);
        taskIds.add(param.getTaskId());
        param.setTaskIds(taskIds);
    }

    private Set<Long> getChildTask(Long taskId, Integer level) {
        return Optional.ofNullable(this.lambdaQuery()
                                           .select(DailyTaskPO::getId)
                                           .gt(DailyTaskPO::getLevel, level)
                                           .like(DailyTaskPO::getSequence, taskId).list())
                .orElse(new ArrayList<>())
                .stream().map(DailyTaskPO::getId).collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transferDailyTask(DailyTaskOperationParam param) {
        List<DailyTaskPO> dailyTaskPOS = this.lambdaQuery().in(DailyTaskPO::getId, param.getTaskIdList())
                .list();
        Assert.isNotFound(CollectionUtils.isEmpty(dailyTaskPOS), "任务不存在无法移交");
        dailyTaskPOS.stream().filter(t -> DailyTaskStateNodes.checkTransfer(t.getStatus()))
                .findFirst().ifPresent(t -> Assert.isConflict(true, "任务状态不允许移交"));
        Set<Long> sourceUsers = getSourceUsers(param.getTaskIdList());
        dailyTaskUserService.lambdaUpdate()
                .in(DailyTaskUserPO::getMasterId, param.getTaskIdList())
                .eq(DailyTaskUserPO::getTaskUserType, DailyTaskUserTypeEnum.ASSIGNEE.name())
                .set(DailyTaskUserPO::getTargetId, param.getTransferUserId())
                .remove();
        List<DailyTaskUserPO> taskUserPOS = param.getTaskIdList().stream()
                .map(taskId -> {
                    DailyTaskUserPO taskUserPO = new DailyTaskUserPO();
                    taskUserPO.setId(IdWorker.getId());
                    taskUserPO.setMasterId(taskId);
                    taskUserPO.setTaskUserType(DailyTaskUserTypeEnum.ASSIGNEE.name());
                    taskUserPO.setTargetId(param.getTransferUserId());
                    taskUserPO.setSuperintendentType(DailySuperintendentTypeEnum.USER.name());
                    return taskUserPO;
                })
                .toList();
        dailyTaskUserService.saveBatch(taskUserPOS);
        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", param.getTransferUserId());
        variables.put("sourceUsers", sourceUsers);
        UserDTO user = userClient.getUser(param.getOperatorId());
        variables.put("sourceUserName", Objects.nonNull(user) ? user.getName() : null);
        businessStateMachine.pushEvent(param.getTaskIdList(), DailyConstants.DAILY_TASK, TRANSFER, variables);
        return true;
    }

    private Set<Long> getSourceUsers(List<Long> taskIdList) {
        List<DailyTaskUserPO> dailyTaskUserPOS = dailyTaskUserService.lambdaQuery()
                .select(DailyTaskUserPO::getTargetId)
                .in(DailyTaskUserPO::getMasterId, taskIdList)
                .eq(DailyTaskUserPO::getTaskUserType, DailyTaskUserTypeEnum.ASSIGNEE.name())
                .list();
        return dailyTaskUserPOS.stream().map(DailyTaskUserPO::getTargetId)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean distributeDailyTask(DailyTaskOperationParam param) {
        DailyTaskPO dailyTaskPO = this.lambdaQuery()
                .eq(DailyTaskPO::getId, param.getTaskId())
                .one();
        Assert.isNotFound(dailyTaskPO, "任务不存在无法分发");

        //获取任务关联的对象
        var taskTargetVOList = dailyTaskTargetService.getNoDistributedTarget(param.getTaskId(), dailyTaskPO.getDailyType());
        if (CollectionUtils.isEmpty(taskTargetVOList)) {
            throw new ServiceException("任务未关联对象无法分发");
        }
        var distributeDailyTaskTargetList = taskTargetVOList.stream()
                .filter(t -> param.getTaskTargetIdList().contains(t.getTargetId()))
                .collect(Collectors.toSet());
        Assert.isParameterError(distributeDailyTaskTargetList.size() != param.getTaskTargetIdList().size(),
                                "存在已分发对象请重新选择");
        //判断当前主任务是否已经给该用户分发过子任务
        Set<Long> childTask = getChildTask(param.getTaskId(), dailyTaskPO.getLevel());
        DailyTaskPO taskPO = null;
        boolean sendTodo = false;
        if (CollectionUtils.isNotEmpty(childTask)) {
            DailyTaskUserPO taskUserPO = this.dailyTaskUserService.lambdaQuery()
                    .eq(DailyTaskUserPO::getTargetId, param.getTransferUserId())
                    .in(DailyTaskUserPO::getMasterId, childTask)
                    .last("limit 1")
                    .one();
            if (Objects.nonNull(taskUserPO)) {
                taskPO = this.lambdaQuery()
                        .eq(DailyTaskPO::getId, taskUserPO.getMasterId())
                        .in(DailyTaskPO::getStatus, businessStateMachine.getNoFinalStatus(dailyTaskPO.getDailyType()))
                        .one();
            }
        }
        //不存在子任务
        if (Objects.isNull(taskPO)) {
            sendTodo = true;
            taskPO = getDailyTaskPO(dailyTaskPO);
            taskPO.setTargetCount(param.getTaskTargetIdList().size());
            taskPO.setParentId(dailyTaskPO.getId());
            this.save(taskPO);
            //任务关联人员
            saveTaskRefUser(param, taskPO);
        } else {
            //进度100%发送待办
            if (BigDecimal.ONE.compareTo(taskPO.getProgress()) == 0) {
                sendTodo = true;
            }
            Long completedNum = Optional.ofNullable(dailyTaskTargetService.getCompleteTargetNum(taskPO.getId(), taskPO.getDailyType()))
                    .orElse(0L);
            taskPO.setTargetCount(taskPO.getTargetCount() + param.getTaskTargetIdList().size());
            taskPO.setProgress(BigDecimal.valueOf(completedNum)
                                       .divide(BigDecimal.valueOf(taskPO.getTargetCount()), 2, RoundingMode.HALF_UP));
            this.updateById(taskPO);
        }
        Map<Long, Set<Long>> taskUserIdMap = new HashMap<>();
        taskUserIdMap.put(taskPO.getId(), Collections.singleton(param.getTransferUserId()));
        //分发任务关联对象
        //更新对象关联任务
        Map<Long, Set<DailyTaskTargetPO>> dailyTaskTargetRefMap = new HashMap<>();
        dailyTaskTargetRefMap.put(taskPO.getId(), new HashSet<>(distributeDailyTaskTargetList));
        dailyTaskTargetService.distributedTaskTarget(dailyTaskTargetRefMap, false);
        Map<String, Object> params = new HashMap<>();
        params.put("targetNum", taskTargetVOList.size() - distributeDailyTaskTargetList.size());
        params.put("userIdMap", taskUserIdMap);
        params.put("sendTodo", sendTodo);
        params.put("sourceUsers", getSourceUsers(Collections.singletonList(param.getTaskId())));
        //任务下发动作
        businessStateMachine.pushEvent(taskPO.getId(), dailyTaskPO.getDailyType(), ISSUE_TASK, params);
        //任务分发动作
        businessStateMachine.pushEvent(param.getTaskId(), dailyTaskPO.getDailyType(), DISTRIBUTE, params);
        return true;
    }

    private DailyTaskPO getDailyTaskPO(DailyTaskPO dailyTaskPO) {
        DailyTaskPO taskPO;
        taskPO = DailyTaskPO.builder().
                id(IdWorker.getId())
                .name(dailyTaskPO.getName())
                .planId(dailyTaskPO.getPlanId())
                .dailyType(dailyTaskPO.getDailyType())
                .taskDay(dailyTaskPO.getTaskDay())
                .taskHour(dailyTaskPO.getTaskHour())
                .taskMinute(dailyTaskPO.getTaskMinute())
                .deadline(dailyTaskPO.getDeadline())
                .urgencyId(dailyTaskPO.getUrgencyId())
                .taskCategoryId(dailyTaskPO.getTaskCategoryId())
                .fileRefKey(dailyTaskPO.getFileRefKey())
                .description(dailyTaskPO.getDescription())
                .startTime(LocalDateTime.now())
                .method(dailyTaskPO.getMethod())
                .isOnDuty(dailyTaskPO.getIsOnDuty())
                .taskType(dailyTaskPO.getTaskType())
                .build();
        return taskPO;
    }

    private void saveTaskRefUser(DailyTaskOperationParam param, DailyTaskPO taskPO) {
        DailyParticipateParam dailyParticipateParam = new DailyParticipateParam();
        dailyParticipateParam.setTargetId(param.getTransferUserId());
        dailyParticipateParam.setParticipateType(ConditionTypeEnum.USER);
        dailyParticipateParam.setTaskUserType(DailyTaskUserTypeEnum.ASSIGNEE.name());

        DailyParticipateParam createUser = new DailyParticipateParam();
        createUser.setTargetId(SecurityContextHolder.getPrincipal().getId());
        createUser.setParticipateType(ConditionTypeEnum.USER);
        createUser.setTaskUserType(DailyTaskUserTypeEnum.SUPERINTENDENT.name());
        Map<Long, Set<DailyParticipateParam>> taskUserMap = new HashMap<>();
        taskUserMap.computeIfAbsent(taskPO.getId(), v -> new HashSet<>())
                .add(dailyParticipateParam);
        taskUserMap.computeIfAbsent(taskPO.getId(), v -> new HashSet<>())
                .add(createUser);
        dailyTaskUserService.saveBatchByTaskId(taskUserMap);
    }

}
