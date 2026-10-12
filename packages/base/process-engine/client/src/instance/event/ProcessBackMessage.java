package instance.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/19 14:15
 * @Description: 流程撤回消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessBackMessage {
    public static final String TOPIC = "FLOW_NODE_BACK";
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
