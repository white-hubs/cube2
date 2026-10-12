package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author wql
 * @describe 实体类型
 * @date 2023/10/23 11:00
 */
@Data
public class QrcodeEntityTypeCommand {
    private Long id;
    /**
     * 类型名称
     */
    @NotBlank(message = "类型名称不能为空")
    private String typeName;
    /**
     * 实体类型
     */
    @NotBlank(message = "实体类型不能为空")
    private String entityType;
}
