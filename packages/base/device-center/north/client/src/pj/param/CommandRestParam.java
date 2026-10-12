package com.guhecloud.cube.iot.north.bridge.client.pj.param;

import lombok.Data;

@Data
public class CommandRestParam {

    private String commandSeq;

    private String deviceId;

    private CommandResetEnum commandResetEnum;

    /**
     * 重置目标<br/>
     * 根据{@link CommandResetEnum}的不同，可能有不同的值：
     * <ul>
     *     <li>属性Code: 重置属性</li>
     *     <li>事件Code: 重置事件</li>
     *     <li>设备: 重置设备(危险动作，暂不提供)</li>
     * </ul>
     */
    private String resetTarget;
}
