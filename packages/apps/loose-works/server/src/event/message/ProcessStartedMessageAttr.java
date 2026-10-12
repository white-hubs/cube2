package com.guhecloud.cube.srv.gen.loose.works.event.message;

import instance.event.ProcessStartedMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/18 21:27
 * @Description:
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessStartedMessageAttr extends ProcessStartedMessage {

    /**
     * 流程实例名称
     */
    private String instanceName;
}
