package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import lombok.Data;

/**
 * 创建二维码类型命令
 */
@Data
public class CreateQrCodeTypeCommand {

    private String code;
    private String name;
    private String grantClientId;
    private String remark;
}
