package instance.command;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/9/1 17:16
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTaskCommand implements ApprovalOpinionSupport {
    @NotBlank
    private String taskId;

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
    private boolean innerServer = false;
    /**
     * 审批意见，上面主要是为了兼容
     */
    private ApprovalOpinion approvalOpinion = new ApprovalOpinion();

//    public Map<String, Object> getVariables() {
//        if (Objects.isNull(variables)) {
//            this.variables = new HashMap<>();
//        }
//        this.variables.put(VariableConstant.RESULT, this.result);
//        if (StringUtils.isNotBlank(data)) {
//            this.variables.put(VariableConstant.DATA, data);
//        }
//        this.variables.put(VariableConstant.APPROVAL_OPINION, this.approvalOpinion);
//        return variables;
//    }
}
