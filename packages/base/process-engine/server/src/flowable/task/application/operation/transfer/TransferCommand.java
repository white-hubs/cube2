package com.guhecloud.cube.base.flow.flowable.task.application.operation.transfer;

import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.TaskOperationCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.activiti.engine.task.TaskInfo;

import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class TransferCommand extends TaskOperationCommand implements ApprovalOpinionSupport {
    @NotBlank
    private TaskInfo taskInfo;
    private String account;
    @NotBlank
    private String targetAccount;
    /**
     * 转交信息
     */
    private ApprovalOpinion approvalOpinion = new ApprovalOpinion();
}
