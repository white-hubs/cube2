package com.guhecloud.cube.base.flow.flowable.task.application.postprocessor;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.ApprovalCommentConfig;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskRuNodeMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskRunningLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskLogPO;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskRuNodePO;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskRunningPO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * @author pengjian
 * @date 2022/10/28 14:38
 * @description
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LogPostProcessor implements TaskOperatePostProcessor {

    private final TaskRunningLogMapper taskRunningLogMapper;

    private final TaskLogMapper taskLogMapper;

    private final UserClient userClient;

    private final TaskRuNodeMapper taskRuNodeMapper;

    @Override
    public void before(String result) {

    }

    /**
     * 记录任务操作日志
     *
     * @param taskInfo       任务信息
     * @param operation      操作类型
     * @param createTime     创建时间
     * @param operators      操作人
     * @param opinionSupport 审批意见
     */
    @Override
    public void after(TaskInfo taskInfo, String operation, LocalDateTime createTime, List<TaskLog.Operator> operators,
                      ApprovalOpinionSupport opinionSupport
                     ) {
        TaskRuNodePO taskRuNodePO = taskRuNodeMapper.selectOne(
                Wrappers.<TaskRuNodePO>lambdaQuery()
                        .eq(TaskRuNodePO::getProcessInstanceId, taskInfo.getProcessInstanceId())
                        .eq(TaskRuNodePO::getTaskKey, taskInfo.getTaskDefinitionKey())
                                                              );
        if (ObjectUtils.isNull(taskRuNodePO)) {
            return;
        }

        List<TaskRunningPO> runningPOList = taskRunningLogMapper.selectList(
                Wrappers.<TaskRunningPO>lambdaQuery()
                        .eq(TaskRunningPO::getProcessInstanceId, taskInfo.getProcessInstanceId())
                        .orderByAsc(TaskRunningPO::getCreateTime));
        Stack<TaskRunningPO> runningPoStack = new Stack<>();
        runningPoStack.addAll(runningPOList);
        if (ObjectUtils.isNotEmpty(runningPoStack) && runningPoStack.peek().getTaskKey().equals(taskInfo.getTaskDefinitionKey())
            && !TaskOperationType.REPEAT.name().equals(operation)) {
            TaskRunningPO taskRunningPO = runningPoStack.pop();
            String operationInfo = taskRunningPO.getOperationInfo();
            TaskLog taskLog = JsonUtils.deserialize(operationInfo, TaskLog.class);
            taskLog.getTaskOperations().addAll(getTaskOperations(operators, operation, opinionSupport));
            taskRunningPO.setOperationInfo(JsonUtils.serialize(taskLog));
            runningPoStack.push(taskRunningPO);
            TaskLogPO taskLogPO = ObjectUtils.copying(taskRunningPO, TaskLogPO.class);
            log.info("更新运行时日志: {}", JsonUtils.serialize(taskLogPO));
            taskLogMapper.updateById(taskLogPO);
        } else {
            ApprovalCommentConfig approvalCommentConfig = taskRuNodePO.getTaskConfig().getNewApprovalNode().getApprovalCommentConfig();
            // 如果不存在
            TaskLog taskLog = TaskLog.builder()
                    .nodeName(taskInfo.getName())
                    .nodeKey(taskInfo.getTaskDefinitionKey())
                    .approvalCommentConfig(approvalCommentConfig)
                    .taskOperations(getTaskOperations(operators, operation, opinionSupport))
                    .build();
            TaskRunningPO taskRunningPO = TaskRunningPO.builder()
                    .taskKey(taskInfo.getTaskDefinitionKey())
                    .processInstanceId(taskInfo.getProcessInstanceId())
                    .operationInfo(JsonUtils.serialize(taskLog))
                    .build();
            TaskLogPO taskLogPO = ObjectUtils.copying(taskRunningPO, TaskLogPO.class);
            if (Objects.nonNull(createTime)) {
                taskLogPO.setCreateTime(createTime);
            }
            taskLogMapper.insert(taskLogPO);
            taskRunningPO.setId(taskLogPO.getId());
            runningPoStack.push(taskRunningPO);
        }
        TaskOperationType operationType = TaskOperationType.valueOf(operation);

        switch (operationType) {
            case BACK:
            case WITHDRAW:
                //退回时删除当前节点和上一个节点任务
                checkAndDeleteNode(runningPoStack, taskRuNodePO);
                break;
            //驳回，撤销，转交任务停留在当前节点
            case TRANSFER:
            case REJECT:
            case UNDO:
                runningPoStack.pop();
                break;
            default:
                break;
        }
        resetTaskRunningLog(runningPoStack, taskInfo.getProcessInstanceId());
    }

    private void checkAndDeleteNode(Stack<TaskRunningPO> runningPoStack, TaskRuNodePO taskRuNodePO) {
        //退回时当前节点和上一个节点需要删除
        runningPoStack.pop();
        TaskRunningPO pop = runningPoStack.pop();
        boolean isNeedDelete = true;
        while (isNeedDelete) {
            TaskLog taskLog = JsonUtils.deserialize(pop.getOperationInfo(), TaskLog.class);
            isNeedDelete = "自动通过".equals(taskLog.getTaskOperations().get(0).getOperation().getOperator().getName());
            isNeedDelete = isNeedDelete || (taskRuNodePO.getTaskKey().equals(pop.getTaskKey()));
            if (isNeedDelete) {
                pop = runningPoStack.pop();
            }
        }
    }

    private void resetTaskRunningLog(Stack<TaskRunningPO> runningPoStack, String processInstanceId) {
        taskRunningLogMapper.delete(Wrappers.<TaskRunningPO>lambdaQuery().eq(TaskRunningPO::getProcessInstanceId, processInstanceId));
        runningPoStack.forEach(taskRunningLogMapper::insert);
    }

    private List<TaskLog.TaskOperation> getTaskOperations(List<TaskLog.Operator> operators, String operation,
                                                          ApprovalOpinionSupport opinionSupport
                                                         ) {
        List<TaskLog.TaskOperation> taskOperations = new ArrayList<>();
        operators.forEach(operator -> taskOperations.add(
                TaskLog.TaskOperation.builder()
                        .operation(TaskLog.Operation.builder()
                                           .approvalOpinion(opinionSupport.getApprovalOpinion())
                                           .type(operation)
                                           .createTime(LocalDateTime.now())
                                           .operator(operator)
                                           .build()).build()));
        return taskOperations;
    }

    /**
     * 记录节点任务操作日志：根节点
     *
     * @param taskInfo               任务信息
     * @param operation              操作类型
     * @param extProcessInstance     流程实例
     * @param approvalOpinionSupport 审批意见
     */
    @Override
    public void after(TaskInfo taskInfo, String operation, ExtProcessInstance extProcessInstance,
                      ApprovalOpinionSupport approvalOpinionSupport
                     ) {
        ProcessInstance processInstance = extProcessInstance.getProcessInstance();
        StartUserInfo startUserInfo = extProcessInstance.getStartUserInfo();
        after(taskInfo,
              TaskOperationType.SUBMIT.name(),
              DateUtils.parse(processInstance.getStartTime()),
              Collections.singletonList(new TaskLog.Operator(Long.parseLong(processInstance.getStartUserId()),
                                                             Objects.nonNull(startUserInfo)
                                                             ? startUserInfo.getStartUserName()
                                                             : "")), ApprovalOpinionSupport.EMPTY);

    }

    /**
     * 记录节点任务操作日志：抄送，自动通过
     *
     * @param taskInfo               任务信息
     * @param operation              操作类型
     * @param operators              操作人
     * @param approvalOpinionSupport 审批意见
     */
    @Override
    public void after(TaskInfo taskInfo, String operation, List<TaskLog.Operator> operators,
                      ApprovalOpinionSupport approvalOpinionSupport
                     ) {
        after(taskInfo, operation, null, operators, approvalOpinionSupport);
    }

    /**
     * 记录节点任务操作日志：退回，通过，驳回，转交撤销
     *
     * @param taskInfo               任务信息
     * @param operation              操作类型
     * @param approvalOpinionSupport 审批意见
     */
    @Override
    public void after(TaskInfo taskInfo, String operation, ApprovalOpinionSupport approvalOpinionSupport) {
        List<TaskLog.Operator> operators = new ArrayList<>();
        if ("381496".equals(taskInfo.getAssignee())) {
            operators.add(new TaskLog.Operator(381496L, "系统处理"));
        } else {
            UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
            operators.add(ObjectUtils.isNotEmpty(user) ? new TaskLog.Operator(user.getId(), user.getName()) : new TaskLog.Operator());
        }
        after(taskInfo, operation, operators, approvalOpinionSupport);
    }
}
