package com.guhecloud.cube.biz.daily.task.record.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.daily.task.commons.constants.DailyConstants;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordExceptionPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordRepairPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyExecutionResultEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyRecordExceptionStatusEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyRecordStatusEnum;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskBusinessTypeEnum;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordExceptionMapper;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordMapper;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordRepairSubmitParam;
import com.guhecloud.cube.biz.daily.task.record.param.DailyRecordRepairTransferParam;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordExceptionResult;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordRepairResult;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordExceptionService;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordRepairService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskFlowOperationEnum;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.todo.client.message.TodoTaskInitMessage;
import com.guhecloud.cube.biz.work.center.client.enumeration.WorkTaskFlowOperationEnum;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowCirculationMessage;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskInitMessage;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyRecordExceptionServiceImpl extends ServiceImpl<DailyRecordExceptionMapper, DailyRecordExceptionPO>
        implements DailyRecordExceptionService {
    private final DailyRecordRepairService dailyRecordRepairService;
    private final FileRefClient fileRefClient;
    private final FileClient fileClient;
    private final UserClient userClient;
    private final DailyRecordMapper dailyRecordMapper;

    @Override
    public List<DailyRecordExceptionPO> createExceptions(Long recordId, Long taskId, String dailyType,
                                                         Map<Long, String> abnormalTargets
                                                        ) {
        var principal = SecurityContextHolder.getPrincipal();
        List<DailyRecordExceptionPO> exceptions = new ArrayList<>();
        if (abnormalTargets.isEmpty()) {
            DailyRecordExceptionPO exception = buildException(recordId, taskId, null, null, principal.getId(), principal.getName());
            exceptions.add(exception);
        } else {
            abnormalTargets.forEach((targetId, targetName) ->
                                            exceptions.add(buildException(recordId, taskId, targetId, targetName, principal.getId(), principal.getName())));
        }
        this.saveBatch(exceptions);
        exceptions.forEach(exception -> createRepairTodo(exception, dailyType));
        return exceptions;
    }

    private DailyRecordExceptionPO buildException(Long recordId, Long taskId, Long targetId, String targetName,
                                                  Long repairUserId, String repairUserName
                                                 ) {
        DailyRecordExceptionPO exception = new DailyRecordExceptionPO();
        exception.setId(IdWorker.getId());
        exception.setRecordId(recordId);
        exception.setTaskId(taskId);
        exception.setTargetId(targetId);
        exception.setTargetName(targetName);
        exception.setStatus(DailyRecordExceptionStatusEnum.WAIT_REPAIR.name());
        exception.setRepairUserId(repairUserId);
        exception.setRepairUserName(repairUserName);
        return exception;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean submitRepair(DailyRecordRepairSubmitParam param) {
        Assert.isParameterError(param == null || param.getExceptionId() == null, "异常记录不能为空");
        Assert.isParameterError(!DailyExecutionResultEnum.NORMAL.name().equals(param.getExecutionResult())
                                && !DailyExecutionResultEnum.ABNORMAL.name().equals(param.getExecutionResult()), "处理结果不正确");
        DailyRecordExceptionPO exception = this.getById(param.getExceptionId());
        Assert.isNotFound(exception, "异常记录不存在");
        var principal = SecurityContextHolder.getPrincipal();
        Assert.isConflict(!DailyRecordExceptionStatusEnum.WAIT_REPAIR.name().equals(exception.getStatus()), "异常已修复");
        Assert.isConflict(!Objects.equals(exception.getRepairUserId(), principal.getId()), "当前用户不是待修复人");

        DailyRecordRepairPO repair = new DailyRecordRepairPO();
        repair.setId(IdWorker.getId());
        repair.setExceptionId(exception.getId());
        repair.setExecutionResult(param.getExecutionResult());
        repair.setHandlerId(principal.getId());
        repair.setHandlerName(principal.getName());
        repair.setRemark(param.getRemark());
        if (CollectionUtils.isNotEmpty(param.getFileIds())) {
            repair.setFileRefKey(repair.getId());
            FileRefCreateParam fileParam = new FileRefCreateParam();
            fileParam.setBusinessKey(repair.getId());
            fileParam.setFileIdList(param.getFileIds());
            fileParam.setSceneCode(DailyConstants.DAILY_TASK_CREATE_FILE);
            fileParam.setTag(DailyConstants.DAILY_TASK);
            fileRefClient.refInduceBatch(Collections.singletonList(fileParam));
        }
        dailyRecordRepairService.save(repair);
        if (DailyExecutionResultEnum.ABNORMAL.name().equals(param.getExecutionResult())) {
            return true;
        }

        exception.setStatus(DailyRecordExceptionStatusEnum.REPAIRED.name());
        exception.setRepairedTime(LocalDateTime.now());
        this.updateById(exception);
        refreshRecordStatus(exception.getRecordId());
        closeRepairTodo(exception, principal.getId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transferRepair(DailyRecordRepairTransferParam param) {
        Assert.isParameterError(param == null || param.getExceptionId() == null || param.getRepairUserId() == null, "移交参数不能为空");
        DailyRecordExceptionPO exception = this.getById(param.getExceptionId());
        Assert.isNotFound(exception, "异常记录不存在");
        var principal = SecurityContextHolder.getPrincipal();
        Assert.isConflict(!DailyRecordExceptionStatusEnum.WAIT_REPAIR.name().equals(exception.getStatus()), "异常已修复");
        Assert.isConflict(!Objects.equals(exception.getRepairUserId(), principal.getId()), "当前用户不是待修复人");
        var user = userClient.getUser(param.getRepairUserId());
        Assert.isNotFound(user, "移交用户不存在");
        Long sourceUserId = exception.getRepairUserId();
        exception.setRepairUserId(user.getId());
        exception.setRepairUserName(user.getName());
        this.updateById(exception);
        transferRepairTodo(exception, sourceUserId);
        return true;
    }

    @Override
    public DailyRecordExceptionResult getExceptionDetail(Long exceptionId) {
        DailyRecordExceptionPO exception = this.getById(exceptionId);
        Assert.isNotFound(exception, "异常记录不存在");
        return getResultByRecordIds(Collections.singletonList(exception.getRecordId()))
                .getOrDefault(exception.getRecordId(), Collections.emptyList()).stream()
                .filter(result -> Objects.equals(result.getId(), exceptionId))
                .findFirst().orElse(null);
    }

    @Override
    public Map<Long, List<DailyRecordExceptionResult>> getResultByRecordIds(List<Long> recordIds) {
        if (CollectionUtils.isEmpty(recordIds)) {
            return Collections.emptyMap();
        }
        List<DailyRecordExceptionPO> exceptions = this.lambdaQuery().in(DailyRecordExceptionPO::getRecordId, recordIds).list();
        if (CollectionUtils.isEmpty(exceptions)) {
            return Collections.emptyMap();
        }
        Set<Long> exceptionIds = exceptions.stream().map(DailyRecordExceptionPO::getId).collect(Collectors.toSet());
        List<DailyRecordRepairPO> repairs = dailyRecordRepairService.lambdaQuery()
                .in(DailyRecordRepairPO::getExceptionId, exceptionIds).orderByAsc(DailyRecordRepairPO::getCreateTime).list();
        Set<Long> fileKeys = repairs.stream().map(DailyRecordRepairPO::getFileRefKey).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, List<com.guhecloud.cube.base.file.client.vo.FileVO>> fileMap = Collections.emptyMap();
        if (CollectionUtils.isNotEmpty(fileKeys)) {
            BusinessKeysParam fileParam = new BusinessKeysParam();
            fileParam.setBusinessKeys(new ArrayList<>(fileKeys));
            fileMap = fileClient.getByBusinessKeys(fileParam);
        }
        Map<Long, List<com.guhecloud.cube.base.file.client.vo.FileVO>> finalFileMap = fileMap;
        Map<Long, List<DailyRecordRepairResult>> repairMap = repairs.stream().map(repair -> {
            DailyRecordRepairResult result = new DailyRecordRepairResult();
            result.setId(repair.getId());
            result.setExecutionResult(repair.getExecutionResult());
            result.setHandlerId(repair.getHandlerId());
            result.setHandlerName(repair.getHandlerName());
            result.setRemark(repair.getRemark());
            result.setCreateTime(repair.getCreateTime());
            result.setFileList(finalFileMap.get(repair.getFileRefKey()));
            return Map.entry(repair.getExceptionId(), result);
        }).collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        return exceptions.stream().map(exception -> {
            DailyRecordExceptionResult result = new DailyRecordExceptionResult();
            result.setId(exception.getId());
            result.setRecordId(exception.getRecordId());
            result.setTaskId(exception.getTaskId());
            result.setTargetId(exception.getTargetId());
            result.setTargetName(exception.getTargetName());
            result.setStatus(exception.getStatus());
            result.setRepairUserId(exception.getRepairUserId());
            result.setRepairUserName(exception.getRepairUserName());
            result.setRepairedTime(exception.getRepairedTime());
            result.setAllowRepair(DailyRecordExceptionStatusEnum.WAIT_REPAIR.name().equals(exception.getStatus())
                                  && Objects.equals(exception.getRepairUserId(), currentUserId));
            result.setRepairList(repairMap.getOrDefault(exception.getId(), Collections.emptyList()));
            return result;
        }).collect(Collectors.groupingBy(DailyRecordExceptionResult::getRecordId));
    }

    private void refreshRecordStatus(Long recordId) {
        List<DailyRecordExceptionPO> exceptions = this.lambdaQuery()
                .eq(DailyRecordExceptionPO::getRecordId, recordId)
                .list();
        String recordStatus;
        if (CollectionUtils.isEmpty(exceptions)) {
            recordStatus = DailyRecordStatusEnum.NORMAL.name();
        } else {
            long waitRepairCount = exceptions.stream()
                    .filter(exception -> DailyRecordExceptionStatusEnum.WAIT_REPAIR.name().equals(exception.getStatus()))
                    .count();
            if (waitRepairCount == 0) {
                recordStatus = DailyRecordStatusEnum.ABNORMAL_REPAIRED.name();
            } else if (waitRepairCount == exceptions.size()) {
                recordStatus = DailyRecordStatusEnum.ABNORMAL_WAIT_REPAIR.name();
            } else {
                recordStatus = DailyRecordStatusEnum.ABNORMAL_REPAIRING.name();
            }
        }
        dailyRecordMapper.update(null, Wrappers.<DailyRecordPO>lambdaUpdate()
                .eq(DailyRecordPO::getId, recordId)
                .set(DailyRecordPO::getRecordStatus, recordStatus));
    }

    private void createRepairTodo(DailyRecordExceptionPO exception, String dailyType) {
        String businessKey = getRepairTodoBusinessKey(exception);
        String taskName = exception.getTargetName() == null ? "异常记录待修复" : exception.getTargetName() + "待修复";
        String repairBusinessType = getRepairBusinessType(dailyType);
        String applicationName = getApplicationName(dailyType);
        String repairBusinessTypeName = getRepairBusinessTypeName(repairBusinessType);
        TodoTaskInitMessage todo = new TodoTaskInitMessage();
        todo.setApplicationCode(dailyType);
        todo.setApplicationName(applicationName);
        todo.setBusinessKey(businessKey);
        todo.setBusinessType(repairBusinessType);
        todo.setBusinessTypeName(repairBusinessTypeName);
        todo.setBusinessStatus(DailyRecordExceptionStatusEnum.WAIT_REPAIR.name());
        todo.setBusinessStatusName(DailyRecordExceptionStatusEnum.WAIT_REPAIR.getValue());
        todo.setTaskName(taskName);
        todo.setTaskTime(LocalDateTime.now());
        todo.setOperationType(TodoTaskFlowOperationEnum.INSERT);
        todo.setUserIds(Collections.singletonList(exception.getRepairUserId()));
        RocketMQContextHolder.syncSendOrderly(TodoTaskInitMessage.TOPIC, todo, businessKey);

        WorkTaskInitMessage work = new WorkTaskInitMessage();
        work.setApplicationCode(dailyType);
        work.setApplicationName(applicationName);
        work.setBusinessKey(businessKey);
        work.setBusinessType(repairBusinessType);
        work.setBusinessTypeName(repairBusinessTypeName);
        work.setBusinessStatus(DailyRecordExceptionStatusEnum.WAIT_REPAIR.name());
        work.setBusinessStatusName(DailyRecordExceptionStatusEnum.WAIT_REPAIR.getValue());
        work.setTaskName(taskName);
        work.setTaskTime(LocalDateTime.now());
        work.setOperationType(WorkTaskFlowOperationEnum.INSERT);
        work.setUserIds(Collections.singletonList(exception.getRepairUserId()));
        RocketMQContextHolder.syncSendOrderly(WorkTaskInitMessage.TOPIC, work, businessKey);
    }

    private String getRepairBusinessType(String dailyType) {
        return switch (dailyType) {
            case DailyConstants.DAILY_TASK -> DailyConstants.DAILY_TASK_REPAIR;
            case DailyConstants.DAILY_INSPECTION -> DailyConstants.DAILY_INSPECTION_REPAIR;
            case DailyConstants.DAILY_SMALL_INSPECTION -> DailyConstants.DAILY_SMALL_INSPECTION_REPAIR;
            case DailyConstants.GRID_MANAGE -> DailyConstants.GRID_MANAGE_REPAIR;
            default -> throw new IllegalArgumentException("不支持的日常任务类型: " + dailyType);
        };
    }

    private String getApplicationName(String dailyType) {
        return DailyTaskBusinessTypeEnum.valueOf(dailyType).getApplicationName();
    }

    private String getRepairBusinessTypeName(String repairBusinessType) {
        return switch (repairBusinessType) {
            case DailyConstants.DAILY_TASK_REPAIR -> "日常任务修复";
            case DailyConstants.DAILY_INSPECTION_REPAIR -> "日常巡检修复";
            case DailyConstants.DAILY_SMALL_INSPECTION_REPAIR -> "小散工程修复";
            case DailyConstants.GRID_MANAGE_REPAIR -> "网格管理修复";
            default -> throw new IllegalArgumentException("不支持的日常任务修复类型: " + repairBusinessType);
        };
    }

    private void closeRepairTodo(DailyRecordExceptionPO exception, Long userId) {
        sendTodoChange(getRepairTodoBusinessKey(exception), DailyRecordExceptionStatusEnum.WAIT_REPAIR.name(),
                       DailyRecordExceptionStatusEnum.REPAIRED.name(), DailyRecordExceptionStatusEnum.REPAIRED.getValue(),
                       Collections.singleton(userId), false, null);
    }

    private void transferRepairTodo(DailyRecordExceptionPO exception, Long sourceUserId) {
        sendTodoChange(getRepairTodoBusinessKey(exception), DailyRecordExceptionStatusEnum.WAIT_REPAIR.name(),
                       DailyRecordExceptionStatusEnum.WAIT_REPAIR.name(), DailyRecordExceptionStatusEnum.WAIT_REPAIR.getValue(),
                       Collections.singleton(sourceUserId), true, Collections.singletonList(exception.getRepairUserId()));
    }

    private String getRepairTodoBusinessKey(DailyRecordExceptionPO exception) {
        return String.valueOf(exception.getRecordId());
    }

    private void sendTodoChange(String businessKey, String beforeStatus, String nextStatus, String nextStatusName,
                                Set<Long> sourceUsers, boolean generatedNext, List<Long> nextUsers
                               ) {
        TodoTaskFlowCirculationMessage todo = new TodoTaskFlowCirculationMessage();
        todo.setBusinessKey(businessKey);
        todo.setBusinessStatus(beforeStatus);
        todo.setUserIds(sourceUsers);
        todo.setHandlerTime(LocalDateTime.now());
        todo.setTaskTime(LocalDateTime.now());
        todo.setOperationType(TodoTaskFlowOperationEnum.CHANGE);
        todo.setGeneratedNextStatusTask(generatedNext);
        todo.setNextBusinessStatus(nextStatus);
        todo.setNextBusinessStatusName(nextStatusName);
        todo.setNextUserIds(nextUsers);
        RocketMQContextHolder.syncSendOrderly(TodoTaskFlowCirculationMessage.TOPIC, todo, businessKey);

        WorkTaskFlowCirculationMessage work = new WorkTaskFlowCirculationMessage();
        work.setBusinessKey(businessKey);
        work.setBusinessStatus(beforeStatus);
        work.setUserIds(sourceUsers);
        work.setHandlerTime(LocalDateTime.now());
        work.setTaskTime(LocalDateTime.now());
        work.setOperationType(WorkTaskFlowOperationEnum.CHANGE);
        work.setGeneratedNextStatusTask(generatedNext);
        work.setNextBusinessStatus(nextStatus);
        work.setNextBusinessStatusName(nextStatusName);
        work.setNextUserIds(nextUsers);
        RocketMQContextHolder.syncSendOrderly(WorkTaskFlowCirculationMessage.TOPIC, work, businessKey);
    }
}
