package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import lombok.Data;

/**
 * 更新二维码状态命令
 */
@Data
public class UpdateQrCodeEnabledCommand {
    private Long id;
    private Boolean enabled;
}
