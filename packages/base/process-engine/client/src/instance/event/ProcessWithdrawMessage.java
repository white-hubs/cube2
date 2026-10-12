package instance.event;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/18 16:30
 * @Description: 流程撤回消息
 */

@Data
public class ProcessWithdrawMessage {
    public static final String TOPIC = "FLOW_WITHDRAW";
    /**
     * 流程实例Id
     */
    private String processInstanceId;

    /**
     * 撤回节点在整个流程的第几个节点
     */
    private Integer processNodeNum;

    /**
     * 业务类型
     */
    private String businessType;
}
