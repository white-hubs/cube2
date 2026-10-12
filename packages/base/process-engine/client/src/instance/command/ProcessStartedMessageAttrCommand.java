package instance.command;

import instance.event.ProcessStartedMessage;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/8/22 9:59
 * @Description: ProcessStartedMessage扩展实体
 */

@Data

public class ProcessStartedMessageAttrCommand extends ProcessStartedMessage {
    /**
     * 外部发起人
     */
    private String outsiderName;

    /**
     * 外部发起人名称
     */
    private String outsiderPhone;

    /**
     * 流程实例名称
     */
    private String instanceName;
}
