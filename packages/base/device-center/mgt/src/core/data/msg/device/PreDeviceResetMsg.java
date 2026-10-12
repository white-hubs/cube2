package com.guhecloud.cube.iot.mgt.core.data.msg.device;

import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.DownCmd;
import com.guhecloud.cube.iot.mgt.core.data.lang.AccProtocolEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreDeviceResetMsg {
    private String commandSeq;
    private String instanceId;

    /**
     * 租户ID
     */
    private Long tenantId;
    private String deviceName;
    private String deviceId;
    /**
     * 设备编号
     */
    private String deviceNumber;
    /**
     * 父设备ID
     */
    private String parentDeviceId;

    private String parentDeviceNumber;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 平台接入适配协议{@link AccProtocolEnum}
     */
    private Integer protocolId;

    /**
     * 命令类型
     */
    private CommandResetEnum commandResetEnum;

    /**
     * 重置目标<br/>
     * 根据{@link DownCmd}的不同，可能有不同的值：
     * <ul>
     *     <li>属性Code: 重置属性</li>
     *     <li>事件Code: 重置事件</li>
     *     <li>设备: 重置设备(危险动作，暂不提供)</li>
     * </ul>
     */
    private String resetTarget;

    /**
     * 重置值<br/>
     * 重置属性有默认值
     */
    private Object resetValue;
}
