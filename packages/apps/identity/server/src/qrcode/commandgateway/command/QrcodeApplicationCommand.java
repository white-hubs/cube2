package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author wql
 * @describe 应用信息
 * @date 2023/10/23 11:41
 */
@Data
public class QrcodeApplicationCommand {
    private Long id;
    /**
     * 应用编号
     */
    @NotBlank(message = "应用编号不能为空")
    private String applicationCode;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空")
    private String applicationName;
}
