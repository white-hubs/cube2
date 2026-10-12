package com.guhecloud.cube.base.flow.flowable.task.application.postprocessor;

import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.flowable.process.domain.ExtProcessInstance;
import com.guhecloud.cube.base.flow.flowable.task.application.postprocessor.log.TaskLog;
import org.activiti.engine.task.TaskInfo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/28 14:16
 * @description
 */
public interface TaskOperatePostProcessor {

    void before(String result);

    void after(TaskInfo taskInfo, String operation, ApprovalOpinionSupport approvalOpinionSupport);

    void after(TaskInfo taskInfo, String operation, LocalDateTime createTime, List<TaskLog.Operator> operators,
               ApprovalOpinionSupport approvalOpinionSupport
              );

    void after(TaskInfo taskInfo, String operation, List<TaskLog.Operator> operators, ApprovalOpinionSupport approvalOpinionSupport);

    void after(TaskInfo taskInfo, String operation, ExtProcessInstance processInstance, ApprovalOpinionSupport approvalOpinionSupport);

}
