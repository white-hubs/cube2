package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量创建二维码命令
 */
@Data
public class BatchCreateQrCodeCommand {

    /**
     * 二维码创建命令列表
     */
    @NotEmpty(message = "二维码列表不能为空")
    @Valid
    private List<CreateQrCodeCommand> items;
}
