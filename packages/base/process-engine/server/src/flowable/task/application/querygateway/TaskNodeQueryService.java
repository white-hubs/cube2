package com.guhecloud.cube.base.flow.flowable.task.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskRunningLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskRunningPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/10/27 15:22
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskNodeQueryService {

    private final TaskRunningLogMapper taskRunningLogMapper;

    /**
     * 判断上一个节点是否是第一个人工用户任务
     *
     * @param task 当前任务
     *
     * @return 前一个审批节点key
     */
    public String getPreNodeTaskDefinitionKey(TaskInfo task) {
        List<TaskRunningPO> runningPOList = taskRunningLogMapper.selectList(
                Wrappers.<TaskRunningPO>lambdaQuery()
                        .eq(TaskRunningPO::getProcessInstanceId, task.getProcessInstanceId())
                        .orderByDesc(TaskRunningPO::getCreateTime));
        List<TaskLog> collect = runningPOList.stream().map(po -> JsonUtils.deserialize(po.getOperationInfo(), TaskLog.class))
                //不能回退到发起人节点，抄送节点以及人员为空的自动审批节点
                .filter(taskLog -> !"root".equals(taskLog.getNodeKey())
                                   && !TaskOperationType.COPY.name().equals(taskLog.getTaskOperations().get(0).getOperation().getType())
                                   && !"自动通过".equals(taskLog.getTaskOperations().get(0).getOperation().getOperator().getName())
                                   && !task.getTaskDefinitionKey().equals(taskLog.getNodeKey()))
                .collect(Collectors.toList());
        Assert.isNotFound(ObjectUtils.isEmpty(collect), "无最新完成任务节点，无法回退！");
        log.info("taskNodeKey：{}", collect.getFirst().getNodeKey());
        // 最新完成任务节点
        return collect.getFirst().getNodeKey();
    }

}
