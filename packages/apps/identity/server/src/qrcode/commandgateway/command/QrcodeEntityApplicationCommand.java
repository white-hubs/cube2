package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author wql
 * @describe 实体与应用绑定
 * @date 2023/10/23 11:16
 */
@Data
public class QrcodeEntityApplicationCommand {
    private Long id;
    /**
     * 实体id
     */
    @NotNull
    private Long entityId;
    /**
     * 应用id
     */
    @NotNull
    private Long applicationId;
    /**
     * 是否选中
     */
    private Boolean selected;
}
