package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量更新二维码类型命令
 */
@Data
public class BatchUpdateQrCodeTypeCommand {

    /**
     * 二维码类型更新命令列表
     */
    @NotEmpty(message = "二维码类型列表不能为空")
    @Valid
    private List<UpdateQrCodeTypeCommand> items;
}
