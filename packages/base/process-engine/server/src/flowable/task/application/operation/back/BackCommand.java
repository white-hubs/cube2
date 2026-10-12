package com.guhecloud.cube.base.flow.flowable.task.application.operation.back;

import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.TaskOperationCommand;
import com.guhecloud.cube.base.flow.flowable.task.domain.enumeration.TaskOperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.activiti.engine.task.TaskInfo;

import java.util.Map;

@Data
@AllArgsConstructor
public class BackCommand extends TaskOperationCommand implements ApprovalOpinionSupport {
    private String account;
    private TaskInfo taskInfo;
    private String comment;
    private Map<String, Object> variables;
    private ApprovalOpinion approvalOpinion = ApprovalOpinionSupport.EMPTY.getApprovalOpinion();
    private TaskOperationType operation;
}
