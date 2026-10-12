package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message;

import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.FlowResult;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.BusinessId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 19:01
 * @Description:
 */

@Data
public class ProcessCompletedMessage {
    public static final String TOPIC = "FLOW_COMPLETED";

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 审批结果
     */
    private FlowResult result;

    /**
     * 业务类型
     */
    private BusinessId businessId;

    /**
     * 表单数据
     */
    private String data;
    private String sourceData;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    private LocalDateTime completedTime;

    /**
     * 租户
     */
    private Integer tenant;

    /**
     * 发起人id
     */
    private Long startUserId;

    /**
     * 发起人手机号
     */
    private String mobile;

    /**
     * 流程变量
     */
    private Map<String, Object> properties;
}
