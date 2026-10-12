package instance.command;

import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/18 19:12
 * @Description:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackTaskCommand implements ApprovalOpinionSupport {
    private String account;
    private String taskId;
    private String comment;
    private Map<String, Object> variables;
    private ApprovalOpinion approvalOpinion = new ApprovalOpinion();
    private boolean innerServer = false;
}
