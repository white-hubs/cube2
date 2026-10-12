package instance.event;

import base.BusinessId;
import com.guhecloud.cube.base.flow.client.FlowResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author pengjian
 * @date 2022/10/20 16:49
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessCompletedMessage {
    public static final String TOPIC = "FLOW_COMPLETED";

    private String processInstanceId;

    private FlowResult result;
    private BusinessId businessId;

    private String data;

    private LocalDateTime startTime;

    private LocalDateTime completedTime;

    private Integer tenant;

    private Long startUserId;
    private String mobile;
    private String sourceProcessId;
    private Map<String, Object> properties;
}
