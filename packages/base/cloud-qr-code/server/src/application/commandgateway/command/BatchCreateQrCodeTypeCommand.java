package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量创建二维码类型命令
 */
@Data
public class BatchCreateQrCodeTypeCommand {

    /**
     * 二维码类型创建命令列表
     */
    @NotEmpty(message = "二维码类型列表不能为空")
    @Valid
    private List<CreateQrCodeTypeCommand> items;
}
