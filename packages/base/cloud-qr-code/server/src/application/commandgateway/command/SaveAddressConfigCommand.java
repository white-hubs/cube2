package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 保存云地址配置命令
 */
@Data
public class SaveAddressConfigCommand {

    /**
     * 域名地址
     */
    @NotBlank(message = "域名地址不能为空")
    private String ipAddress;
}
