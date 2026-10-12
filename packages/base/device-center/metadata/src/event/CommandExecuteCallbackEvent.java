package com.guhecloud.cube.base.device.center.metadata.event;

import com.guhecloud.cube.base.device.center.metadata.lang.AttrCtlExecuteResultEnum;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/7/7 9:27
 * @description
 */
@Data
public class CommandExecuteCallbackEvent {
    public static final String TOPIC = "COMMAND_EXECUTE_CALLBACK";

    private String commandSeq;
    private AttrCtlExecuteResultEnum executeResult;
    private String message;
    /**
     * 设备id
     */
    private Long deviceInstanceId;
    /**
     * 属性id
     */
    private String attributeCode;

}
