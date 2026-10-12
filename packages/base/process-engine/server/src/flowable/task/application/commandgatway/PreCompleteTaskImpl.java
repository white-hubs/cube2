package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway;

import com.guhecloud.cube.base.flow.flowable.definition.domain.repository.ProcessDefinitionRepository;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.Properties;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessExecuteContext;
import com.guhecloud.cube.base.flow.flowable.task.application.validator.TaskHandleValidator;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import instance.command.CompleteTaskCommand;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PreCompleteTaskImpl implements PreCompleteTask {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final TaskHandleValidator taskHandleValidator;

    private final ProcessDefinitionRepository processDefinitionRepository;

    /**
     * @param completeTaskCommand 完成任务命令
     */
    @Override
    @Transactional
    public Task precompletion(CompleteTaskCommand completeTaskCommand) {
        Task task = taskHandleValidator.attributionVerify(completeTaskCommand.getTaskId(), completeTaskCommand.getAccount());
        // 手写签名开启校验,审批意见开启校验
        Properties nodeProperties = ProcessExecuteContext.getProcessNodeProperties(task.getProcessInstanceId(), task.getTaskDefinitionKey());
        Boolean sign = nodeProperties.getSign();
        Boolean opinion = nodeProperties.getOpinion();

        if ((Objects.nonNull(sign) && !sign && Objects.nonNull(completeTaskCommand.getApprovalOpinion())
             && completeTaskCommand.getApprovalOpinion().isSign())) {
            throw new ServiceException("没有开启电子签名");
        }

        if ((Objects.nonNull(opinion) && !opinion && Objects.nonNull(completeTaskCommand.getApprovalOpinion())
             && completeTaskCommand.getApprovalOpinion().isOpinion())) {
            throw new ServiceException("没有开启审批意见");
        }
        return task;
    }
}
