package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import lombok.Data;

/**
 * 更新云二维码项目命令
 */
@Data
public class UpdateCloudQrCodeProjectCommand {

    private Long id;

    /**
     * 项目名称
     */
    private String name;
}
