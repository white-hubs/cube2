package com.guhecloud.cube.base.flow.flowable.task.application.operation.pass;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.TaskOperationCommand;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.activiti.engine.task.TaskInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
public class PassCommand extends TaskOperationCommand implements ApprovalOpinionSupport {

    @NotNull
    private TaskInfo taskInfo;

    public PassCommand() {
        this.account = SecurityContextHolder.getPrincipal().getId().toString();
    }

    /**
     * 处理结果
     */
    private String result;

    private String data;

    private String account;

    /**
     * 变量
     */
    private Map<String, Object> variables;

    /**
     * 审批意见，上面主要是为了兼容
     */
    private ApprovalOpinion approvalOpinion = EMPTY.getApprovalOpinion();
    public Map<String, Object> getVariables() {
        if (Objects.isNull(variables)) {
            this.variables = new HashMap<>();
        }
        this.variables.put(VariableConstant.RESULT, this.result);
        this.variables.put(VariableConstant.DATA, data);
        return variables;
    }
    /**
     * 当前任务变量
     * @return
     */
    public Map<String, Object> getTaskVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put(VariableConstant.RESULT, this.result);
        variables.put(VariableConstant.APPROVAL_OPINION, this.approvalOpinion);
        return variables;
    }
}
