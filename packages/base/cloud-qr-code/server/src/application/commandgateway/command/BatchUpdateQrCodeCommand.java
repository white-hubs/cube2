package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量更新二维码命令
 */
@Data
public class BatchUpdateQrCodeCommand {

    /**
     * 二维码更新命令列表
     */
    @NotEmpty(message = "二维码列表不能为空")
    @Valid
    private List<UpdateQrCodeCommand> items;
}
