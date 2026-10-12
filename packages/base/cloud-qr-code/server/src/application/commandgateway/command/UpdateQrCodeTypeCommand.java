package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import lombok.Data;

/**
 * 更新二维码类型命令
 */
@Data
public class UpdateQrCodeTypeCommand {
    private Long id;
    private String code;
    private String name;
    private String remark;
}
