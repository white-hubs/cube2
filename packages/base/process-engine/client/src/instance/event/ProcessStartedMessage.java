package instance.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author wql
 * @describe 流程发起成功消息
 * @date 2023/9/14 17:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessStartedMessage {
    public static final String TOPIC = "FLOW_STARTED";
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

    private String applicationCode;

    private String applicationName;

    private Map<String, Object> processVariables;
}
