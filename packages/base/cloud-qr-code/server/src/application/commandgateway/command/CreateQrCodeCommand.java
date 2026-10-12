package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import lombok.Data;

/**
 * 创建二维码命令
 */
@Data
public class CreateQrCodeCommand {

    private String code;
    private String name;

    /**
     * 二维码类型ID
     *
     * @deprecated 推荐使用qrTypeCode
     */
    @Deprecated
    private Long qrTypeId;

    /**
     * 二维码类型code（推荐使用）
     */
    private String qrTypeCode;

    private String qrContentType;
    private String qrContent;
    private String mode;
    private String modeContent;
    private String grantClientId;
    private Boolean enabled;
    private ModifyQrRouteCommand route;
}
