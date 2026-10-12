package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author qiu wei jun
 * @date 2022/9/29 14:22:01
 */
@Data
public class TransferCommand {

    public TransferCommand() {
        this.account = SecurityContextHolder.getPrincipal().getId().toString();
    }

    @NotBlank
    private String taskId;
    private String account;
    @NotBlank
    private String targetAccount;
    /**
     * 转交信息
     */
    private ApprovalOpinion approvalOpinion = new ApprovalOpinion();
}
