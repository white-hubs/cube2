package com.guhecloud.cube.base.flow.flowable.task.interfaces;

import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.TaskFeedbackLogCommand;
import com.guhecloud.cube.base.flow.flowable.task.domain.repository.TaskFeedbackLogRepository;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskFeedbackLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/11 11:38
 * @Description: 任务记录反馈-api
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/task/feedback/log")
public class TaskFeedbackLogController {

    private final TaskFeedbackLogRepository taskFeedbackLogRepository;

    /**
     * 提交任务反馈
     *
     * @param taskFeedbackLogCommand
     *
     * @return
     */
    @PostMapping("/submit")
    public Boolean taskFeedbackSubmit(@RequestBody TaskFeedbackLogCommand taskFeedbackLogCommand) {
        return taskFeedbackLogRepository.taskFeedbackSubmit(taskFeedbackLogCommand);
    }

    /**
     * 根据流程id和任务节点获取反馈记录
     *
     * @param processInstanceId
     * @param taskNodeKey
     *
     * @return
     */
    @GetMapping("/get/log")
    public List<TaskFeedbackLogDTO> getTaskFeedbackLog(@RequestParam("processInstanceId") String processInstanceId,
                                                       @RequestParam("taskNodeKey") String taskNodeKey
                                                      ) {
        return taskFeedbackLogRepository.getTaskFeedbackLog(processInstanceId, taskNodeKey);
    }
}
