package com.guhecloud.cube.base.flow.flowable.task.domain.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.TaskFeedbackLogCommand;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskFeedbackLogPO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskFeedbackLogDTO;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/11 11:33
 * @Description:
 */
public interface TaskFeedbackLogRepository extends IService<TaskFeedbackLogPO> {

    /**
     * 提交任务反馈
     *
     * @param taskFeedbackLogDTO
     *
     * @return
     */
    boolean taskFeedbackSubmit(TaskFeedbackLogCommand taskFeedbackLogDTO);

    /**
     * 根据流程id和任务节点获取反馈记录
     *
     * @param processId
     * @param taskNodeKey
     *
     * @return
     */
    List<TaskFeedbackLogDTO> getTaskFeedbackLog(String processId, String taskNodeKey);
}
