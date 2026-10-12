package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command;

import lombok.Data;

@Data
public class ChangeDeviceNotifyRuleEnableCommand {

    /**
     * 设备id
     */
    private Long id;

    /**
     * 启用禁用
     */
    private Boolean enabled;
}
