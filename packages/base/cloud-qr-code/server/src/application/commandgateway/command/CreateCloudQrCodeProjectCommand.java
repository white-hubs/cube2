package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import lombok.Data;

/**
 * 创建云二维码项目命令
 */
@Data
public class CreateCloudQrCodeProjectCommand {

    /**
     * 项目名称
     */
    private String name;
}
