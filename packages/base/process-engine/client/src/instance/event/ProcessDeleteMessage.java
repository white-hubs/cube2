package instance.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/21 15:39
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDeleteMessage {
    public static final String TOPIC = "PROCESS_INSTANCE_DELETE";

    private List<String> processInstanceId;
}
