package com.guhecloud.cube.base.flow.flowable.process.interfaces;

import base.constant.VariableConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.context.submit.commandgateway.command.ProcessStartedMessageAttrCommand;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.ProcessInstanceCommandService;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.command.CloseProcessInstanceCommand;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.command.StartProcessInstanceCommand;
import com.guhecloud.cube.base.flow.flowable.process.application.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtHistoricProcessInstance;
import com.guhecloud.cube.base.flow.flowable.process.domain.respository.ExtProcessInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.po.ExtProcessInstancePO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.CloseBatchProcessParam;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.CloseProcessParam;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.ProcessInstanceDetailDTO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskInstanceDetailDTO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/8/22 13:07
 * @description
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessInstanceController {
    private final ProcessInstanceCommandService processInstanceCommandService;
    private final RuntimeService runtimeService;
    private final ExtProcessInstanceMapper extProcessInstanceMapper;
    private final HistoryService historyService;
    private final TaskService taskService;
    private final ExtProcessInstanceRepository extProcessInstanceRepository;

    /**
     * 启动流程实例
     * <p>
     * 拷贝流程定义的相关配置数据
     */
    @PostMapping("/instance/start")
    public ProcessInstanceDTO startProcess(@Valid @RequestBody StartProcessInstanceCommand startProcessInstanceCommand) {
        //todo 调用原API创建，后期改造
        //todo 如果流程定义关联了表单，则需要读取表单数据 启动前进行数据解析

        startProcessInstanceCommand.getVariables().put(VariableConstant.DATA, JsonUtils.serialize(
                Collections.singletonList(startProcessInstanceCommand.getVariables().get(VariableConstant.DATA))
                                                                                                 ));
        return processInstanceCommandService.start(
                startProcessInstanceCommand
                                                  );
    }

    /**
     * @Author wql
     * @Description 获取流程实例表单字段数据
     * @Date 10:09 2023/9/26
     **/
    @GetMapping("/process/start/tableInfo")
    public ProcessStartedMessageAttrCommand getProcessInstanceFormInfo(@RequestParam("processInstanceId") String processInstanceId) {
        Assert.isParameterError(StringUtils.isBlank(processInstanceId), "流程实例id不能为空");
        return processInstanceCommandService.getProcessInstanceFormInfo(processInstanceId);
    }

    /**
     * 查询流程实例详情
     *
     * @param processInstanceId 实例id
     */
    @GetMapping("/process/instance/detail/{id}")
    public ProcessInstanceDetailDTO detail(@PathVariable("id") String processInstanceId) {

        // 兼容方案
        HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(processInstanceId).singleResult();
        if (Objects.nonNull(taskInstance)) {
            processInstanceId = taskInstance.getProcessInstanceId();
        }

        Optional<ExtHistoricProcessInstance> optional = extProcessInstanceRepository.getHistoric(processInstanceId);
        if (!optional.isPresent()) {
            throw new ServiceException("流程实例不存在");
        }

        ExtHistoricProcessInstance extHistoricProcessInstance = optional.get();
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        String name = null;
        String currentTaskId = null;
        String taskDefinitionKey = null;
        if (CollectionUtils.isNotEmpty(list)) {
            name = list.get(0).getName();
            taskDefinitionKey = list.get(0).getTaskDefinitionKey();
            currentTaskId = list.get(0).getId();
        }

        TaskInstanceDetailDTO taskInstanceDetailDTO = TaskInstanceDetailDTO.builder()
                .id(currentTaskId)
                .key(taskDefinitionKey)
                .name(name)
                .build();
        Object formInstanceId = extHistoricProcessInstance.getProcessInstance().getProcessVariables()
                .get(VariableConstant.FORM_INSTANCE_Id);

        return ProcessInstanceDetailDTO.builder()
                .data(extHistoricProcessInstance.getData())
                .id(processInstanceId)
                .flowKey(extHistoricProcessInstance.getProcessDefinitionKey())
                .startTime(DateUtils.parse(extHistoricProcessInstance.getProcessInstance().getStartTime()))
                .startUserId(Long.parseLong(extHistoricProcessInstance.getProcessInstance().getStartUserId()))
                .currentTask(taskInstanceDetailDTO)
                .formInstanceId(Objects.isNull(formInstanceId) ? null : formInstanceId.toString())
                .businessType(extHistoricProcessInstance.getBusinessId().getBusinessType())
                .build();
    }

    /**
     * 关闭流程
     *
     * @param closeProcessParam
     */
    @PostMapping("/process/instance/close")
    public void closeProcess(@RequestBody CloseProcessParam closeProcessParam) {
        CloseProcessInstanceCommand closeProcessInstanceCommand = new CloseProcessInstanceCommand(
                closeProcessParam.getProcessInstanceId(),
                closeProcessParam.getBusinessKey()
        );
        closeProcessInstanceCommand.setComment(closeProcessInstanceCommand.getComment());
        processInstanceCommandService.close(closeProcessInstanceCommand);
    }

    @GetMapping("/process/instance/unfinished/page")
    public List<ProcessInstanceDTO> unfinished() {
        List<ProcessInstance> list =
                runtimeService.createProcessInstanceQuery().active().list();

        List<ProcessInstanceDTO> processInstanceDTOS = ObjectUtils.copying(list, ProcessInstanceDTO.class);

        return processInstanceDTOS;
    }

    /**
     * 关闭流程实例
     * <p>
     * 根据businessKey、processDefinitionKey、processInstanceId、businessType
     */
    @DeleteMapping("/process/instance/close")
    public void close(@RequestBody @Valid CloseBatchProcessParam closeBatchProcessParam) {
        switch (closeBatchProcessParam.getProcessLookUpKeyType()) {
            case PROCESS_INSTANCE_ID:
                closeBatchProcessParam.getKeys().forEach(key -> {
                    try {
                        runtimeService.deleteProcessInstance(key, "");
                    } catch (Exception e) {
                        log.error("删除流程实例{}失败,原因:{}", key, e.getMessage());
                    }
                });
                break;
            case BUSINESS_KEY:
                throw new ServiceException("暂不支持");
            case BUSINESS_TYPE:
                // 查询所有businessType的流程实例
                List<ExtProcessInstancePO> extProcessInstances = extProcessInstanceMapper.selectList(
                        Wrappers.<ExtProcessInstancePO>lambdaQuery()
                                .in(ExtProcessInstancePO::getBusinessType)
                                                                                                    );
                if (CollectionUtils.isNotEmpty(extProcessInstances)) {
                    extProcessInstances.forEach(extProcessInstancePO -> {
                        try {
                            runtimeService.deleteProcessInstance(extProcessInstancePO.getId(), "");
                        } catch (Exception e) {
                            log.error("删除流程实例{}失败,原因:{}", extProcessInstancePO.getId(), e.getMessage());
                        }
                    });
                }
                break;
            case PROCESS_DEFINITION_KEY:
                throw new ServiceException("暂不支持");
        }
    }

    /**
     * 修正processInstanceRunning表中的startUserInfo
     */
    @GetMapping("/process/instance/updateStartUserInfo")
    public void updateStartUserInfo() {
        List<ExtProcessInstancePO> pos = extProcessInstanceMapper.selectList(Wrappers.emptyWrapper());
        if (ObjectUtils.isNotEmpty(pos)) {
            pos.forEach(po -> {
                if (extProcessInstanceRepository.setStartUserName(po)) {
                    extProcessInstanceMapper.updateStartUserInfo(po.getId(), JsonUtils.serialize(po.getStartUserInfo()));
                }
            });
        }
    }
}
