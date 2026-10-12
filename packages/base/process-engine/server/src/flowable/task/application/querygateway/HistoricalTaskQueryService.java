package com.guhecloud.cube.base.flow.flowable.task.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.domain.TaskInstance;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import com.guhecloud.cube.base.flow.flowable.task.domain.repository.TaskInstanceRepository;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskLogPO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * @author 剑秋
 * @description
 * @date 2022/9/10
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HistoricalTaskQueryService {
    private final HistoryService historyService;
    private final TaskInstanceRepository taskInstanceRepository;

    /**
     * 获取指定流程的初始节点的任务实例
     *
     * @param processInstanceId
     *
     * @return
     */
    public Optional<TaskInstance> getInitiate(String processInstanceId) {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .asc()
                .list();
        if (CollectionUtils.isEmpty(historicTaskInstances)) {
            return Optional.empty();
        }

        Optional<HistoricTaskInstance> first = historicTaskInstances.stream().findFirst();

        if (first.isPresent()) {
            return taskInstanceRepository.get(first.get().getId());
        }

        return Optional.empty();

    }

    /**
     * 获取流程最新处理完的任务
     *
     * @param processInstanceId
     *
     * @return
     */
    public HistoricTaskInstance getLastedFinishTask(String processInstanceId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeTaskLocalVariables()
                .includeProcessVariables()
                .orderByHistoricTaskInstanceEndTime().desc().list();
        HistoricTaskInstance historicTaskInstance = list.get(0);
        return historicTaskInstance;
    }

    public List<TaskInfo> getLastFinishedNodeByProcessInstanceId(String processInstanceId) {
        //查询流程人工任务历史数据
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().finished().processInstanceId(processInstanceId)
                .list();
        Map<String, HistoricTaskInstance> map = list.stream().collect(
                Collectors.groupingBy(HistoricTaskInstance::getTaskDefinitionKey, Collectors.collectingAndThen(
                        Collectors.reducing((t1, t2) -> t1.getEndTime().after(t2.getEndTime()) ? t1 : t2),
                        Optional::get
                                                                                                              ))
                                                                     );
        return map.values().stream().collect(Collectors.toList());
    }

    /**
     * 根据流程实例ID获取已完成的任务节点
     *
     * @param processInstanceId
     *
     * @return
     */
    public List<TaskLog> getFinishedTaskNodeByProcessInstanceId(String processInstanceId) {

        // 构建历史
        TaskLogMapper taskLogMapper = ApplicationContextHolder.getBean(TaskLogMapper.class);
        List<TaskLogPO> taskLogPOS = taskLogMapper.selectList(
                Wrappers.<TaskLogPO>lambdaQuery()
                        .eq(TaskLogPO::getProcessInstanceId, processInstanceId)
                        .orderByAsc(TaskLogPO::getCreateTime)
                                                             );
        //不能回退到发起人节点，抄送节点以及人员为空的自动审批节点
        return taskLogPOS.stream().map(po -> JsonUtils.deserialize(po.getOperationInfo(), TaskLog.class))
                //不能回退到发起人节点，抄送节点以及人员为空的自动审批节点
                .filter(taskLog -> !TaskOperationType.COPY.name().equals(taskLog.getTaskOperations().get(0).getOperation().getType())
                                   && !"自动通过".equals(taskLog.getTaskOperations().get(0).getOperation().getOperator().getName()))
                .collect(Collectors.toList());
    }
}
