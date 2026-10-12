package com.guhecloud.cube.base.flow.flowable.task.interfaces;

import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import com.guhecloud.cube.base.flow.flowable.task.application.querygateway.HistoricalTaskQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/21 16:29
 * @description
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/process/task/historical")
public class HistoricalTaskController {

    private final HistoricalTaskQueryService historicalTaskQueryService;

    /**
     * 根据流程实例ID获取历史任务
     *
     * @param processInstanceId
     *
     * @return
     */
    @GetMapping("/finished/{processInstanceId}")
    public List<TaskInfo> getFinishedTasksByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId) {
        return historicalTaskQueryService.getLastFinishedNodeByProcessInstanceId(processInstanceId);
    }

    /**
     * 根据流程实例ID获取已完成的任务节点
     *
     * @param processInstanceId
     *
     * @return
     */
    @GetMapping("/finished/task-node/{processInstanceId}")
    public List<TaskLog> getFinishedTaskNodeByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId) {
        return historicalTaskQueryService.getFinishedTaskNodeByProcessInstanceId(processInstanceId);
    }

}
