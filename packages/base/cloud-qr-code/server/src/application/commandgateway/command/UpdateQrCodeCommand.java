package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import lombok.Data;

/**
 * 更新二维码命令
 */
@Data
public class UpdateQrCodeCommand {
    private Long id;
    private String code;
    private String name;
    private String qrContentType;
    private String qrContent;
    private String mode;
    private String modeContent;
    private Boolean enabled;
    private ModifyQrRouteCommand route;
}
