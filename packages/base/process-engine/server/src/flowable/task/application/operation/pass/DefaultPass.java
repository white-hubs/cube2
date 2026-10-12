package com.guhecloud.cube.base.flow.flowable.task.application.operation.pass;

import base.constant.VariableConstant;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessExecuteContext;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessInfo;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.AbstractTaskOperation;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.TaskOperatePostProcessor;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Scope("prototype")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultPass extends AbstractTaskOperation<PassCommand> implements Pass {
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final TaskOperatePostProcessor taskOperatePostProcessor;
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ExtProcessInstanceMapper extProcessInstanceMapper;

    @Override
    public void execute(PassCommand passCommand) {
        TaskInfo task = passCommand.getTaskInfo();
        // 获取扩展流程实例
        ProcessInfo processInfo = ProcessExecuteContext.getProcessInfo(task.getProcessInstanceId());
        if (Objects.nonNull(processInfo.getExtProcessInstancePO())) {
            ExtProcessInstancePO extProcessInstancePO = processInfo.getExtProcessInstancePO();
            Map<String, Object> stringObjectMap = JSON.parseObject(extProcessInstancePO.getData(), new TypeReference<Map<String, Object>>() {});
            stringObjectMap.put(task.getTaskDefinitionKey(), passCommand.getData());
            extProcessInstancePO.setData(JsonUtils.serialize(stringObjectMap));
            Map<String, Object> processVariable = new HashMap<>();
            processVariable.put(VariableConstant.DATA, passCommand.getData());
            processVariable.put(VariableConstant.RESULT, passCommand.getResult());
            runtimeService.setVariables(extProcessInstancePO.getId(), processVariable);
            extProcessInstanceMapper.updateById(extProcessInstancePO);
        }
        redisTemplate.opsForHash().delete(VariableConstant.BACK_NODE, task.getProcessInstanceId());
        taskOperatePostProcessor.after(task, TaskOperationType.PASS.name(), passCommand);
        Object loopCounter = task.getTaskLocalVariables().get("loopCounter");
        try {
            if (ObjectUtils.isNotEmpty(loopCounter)) {
                //todo:处理顺序会签节点重复审批的临时方案(手动重置loopCounter)
                runtimeService.setVariable(task.getExecutionId(), "loopCounter", loopCounter);
            }
            taskService.setVariablesLocal(task.getId(), passCommand.getTaskVariables());
            taskService.complete(task.getId(), passCommand.getVariables());
        } catch (ActivitiException e) {
            if (e.getMessage().contains("No outgoing sequence flow of the exclusive gateway")) {
                throw new ServiceException("请先配置流程分支");
            }
        }
    }
}
