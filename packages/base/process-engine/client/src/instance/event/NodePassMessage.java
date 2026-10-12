package instance.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 节点审批通过消息
 * @date 2023/11/6 14:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodePassMessage {
    public static final String TOPIC = "FLOW_NODE_PASS";
    /**
     * 流程实例id
     */
    private String processInstanceId;

    private String businessKey;

    private String businessType;

    private String businessTypeName;

    private String data;

    private LocalDateTime startTime;

    private Integer tenant;

    private Long startUserId;

    /**
     * 来源工单
     */
    private String sourceProcessId;
}
