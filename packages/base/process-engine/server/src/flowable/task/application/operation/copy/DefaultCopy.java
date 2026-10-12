package com.guhecloud.cube.base.flow.flowable.task.application.operation.copy;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.flowable.common.ProcessHelper;
import com.guhecloud.cube.base.flow.flowable.common.ProcessUserDTO;
import com.guhecloud.cube.base.flow.flowable.common.message.MessageSender;
import com.guhecloud.cube.base.flow.flowable.common.message.NotifyUser;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.AbstractTaskOperation;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.TaskOperatePostProcessor;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.CopyLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.CopyLogPO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultCopy extends AbstractTaskOperation<CopyCommand> implements Copy {
    private final RuntimeService runtimeService;
    private final CopyLogMapper copyLogMapper;
    private final ExtProcessInstanceRepository extProcessInstanceRepository;
    private final MessageSender messageSender;
    private final ProcessHelper processHelper;
    private final HistoryService historyService;
    private final TaskOperatePostProcessor taskOperatePostProcessor;
    private final UserClient userClient;

    @Override
    public void execute(CopyCommand copyCommand) {
        List<NotifyUser> notifyUsers = ObjectUtils.copying(copyCommand.getCopyUsers(), NotifyUser.class);
        Object formInstanceId = runtimeService.getVariable(copyCommand.getProcessInstanceId(), VariableConstant.FORM_INSTANCE_Id);
        CopyLogPO copyLogPO = CopyLogPO.builder()
                .taskId(copyCommand.getTaskKey())
                .userIds(StringUtils.join(",", notifyUsers.stream().map(NotifyUser::getUserId).collect(Collectors.toSet())))
                .data(formInstanceId.toString())
                .processInstanceId(copyCommand.getProcessInstanceId())
                .build();
        copyLogMapper.insert(copyLogPO);

        // 日志记录
        log(copyCommand.getTaskKey(), copyCommand.getTaskName(), copyCommand.getProcessInstanceId(), notifyUsers);

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(copyCommand.getProcessInstanceId()).singleResult();
        // 发送消息通知
        send(copyCommand.getProcessInstanceId(), notifyUsers, historicProcessInstance.getProcessDefinitionName());
    }

    private void log(String taskKey, String taskName, String processInstanceId, List<NotifyUser> notifyUsers) {
        Map<Long, String> userIdToNameMap = userClient
                .listUserByIds(notifyUsers.stream().map(NotifyUser::getUserId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        List<TaskLog.Operator> operateInfos = new ArrayList<>();
        notifyUsers.forEach(notifyUser -> operateInfos.add(new TaskLog.Operator(notifyUser.getUserId(), userIdToNameMap.get(notifyUser.getUserId()))));
        TaskEntity taskInfo = new TaskEntityImpl();
        taskInfo.setTaskDefinitionKey(taskKey);
        taskInfo.setName(taskName);
        taskInfo.setProcessInstanceId(processInstanceId);
        taskOperatePostProcessor.after(taskInfo, TaskOperationType.COPY.name(), operateInfos, ApprovalOpinionSupport.EMPTY);

    }

    private void send(String processInstanceId, List<NotifyUser> notifyUsers, String processDefinitionName) {
        ProcessUserDTO processStartUser = processHelper.getProcessStartUser(processInstanceId);
        Optional<ExtProcessInstance> extProcessInstance = extProcessInstanceRepository.get(processInstanceId);
        //发送消息抄送
        messageSender.appendMessageBody("startUser", processStartUser.getNickname())
                .appendApplication(extProcessInstance.get().getApplicationId())
                .appendProcessInstanceId(extProcessInstance.get().getProcessInstance().getProcessInstanceId())
                .appendMessageBody("formName", processDefinitionName)
                .send("TASK_COPY", notifyUsers);
    }
}
