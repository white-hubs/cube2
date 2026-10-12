package com.guhecloud.cube.base.flow.flowable.task.application.operation.undo;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.flowable.common.ProcessHelper;
import com.guhecloud.cube.base.flow.flowable.common.ProcessUserDTO;
import com.guhecloud.cube.base.flow.flowable.common.message.MessageSender;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.ProcessInstanceCommandService;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.AbstractTaskOperation;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.TaskOperatePostProcessor;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Scope("prototype")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultUndo extends AbstractTaskOperation<UndoCommand> implements Undo {
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final MessageSender messageSender;
    private final ProcessHelper processHelper;
    private final TaskOperatePostProcessor taskOperatePostProcessor;
    private final ProcessInstanceCommandService processInstanceCommandService;

    @Override
    public void execute(UndoCommand undoCommand) {
        // 设置变量
        final String processInstanceId = undoCommand.getProcessInstanceId();
        Set<Long> taskAssignees = new HashSet<>();
        Task task = setVariables(processInstanceId, taskAssignees);
        taskOperatePostProcessor.after(task, TaskOperationType.UNDO.name(), ApprovalOpinionSupport.EMPTY);
        undoMsgSend(processInstanceId, taskAssignees);
        processInstanceCommandService.endProcessInstance(TaskOperationType.UNDO, processInstanceId, TaskOperationType.UNDO.name());
        runtimeService.deleteProcessInstance(processInstanceId, undoCommand.getComment());
    }

    /**
     * 发送消息通知
     *
     * @param processInstanceId 流程实例id
     * @param taskAssignees
     */
    public void undoMsgSend(String processInstanceId, Set<Long> taskAssignees) {
        final Optional<ExtProcessInstance> extProcessInstance = extProcessInstanceRepository.get(processInstanceId);
        extProcessInstance.ifPresent(instance -> {
            // 过滤审批通过的人员
            ProcessUserDTO processStartUser = processHelper.getProcessStartUser(processInstanceId);
            // 耦合部分
            messageSender.appendMessageBody("startUser", processStartUser.getNickname())
                    .appendApplication(instance.getApplicationId())
                    .appendProcessInstanceId(processInstanceId)
                    .appendMessageBody("formName", instance.getProcessInstance().getProcessDefinitionName())
                    .send("PROCESS_UNDO", MessageSender.buildNotifyUser(taskAssignees));
        });
    }

    private Task setVariables(String processInstanceId, Set<Long> taskAssignees) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        for (Task task : tasks) {
            taskAssignees.add(Long.valueOf(task.getAssignee()));
        }
        tasks.forEach(task -> taskService.setAssignee(task.getId(), "10001"));
        runtimeService.setVariable(processInstanceId, VariableConstant.RESULT, TaskOperationType.UNDO.name());
        return tasks.get(0);
    }

}
