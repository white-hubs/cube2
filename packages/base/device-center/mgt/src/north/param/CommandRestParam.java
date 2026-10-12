package com.guhecloud.cube.iot.mgt.north.param;

import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.DownCmd;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.CommandResetEnum;
import lombok.Data;

@Data
public class CommandRestParam {

    private String commandSeq;

    private String deviceId;

    /**
     * 命令类型<br/>
     * 目前只支持{@link DownCmd#DEV_PROP_RESET}和{@link DownCmd#DEV_EVENT_RESET}
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
}
