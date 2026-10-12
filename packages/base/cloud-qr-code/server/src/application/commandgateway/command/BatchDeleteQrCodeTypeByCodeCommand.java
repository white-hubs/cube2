package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量通过code删除二维码类型命令
 */
@Data
public class BatchDeleteQrCodeTypeByCodeCommand {

    /**
     * 二维码类型编号列表
     */
    @NotEmpty(message = "二维码类型编号列表不能为空")
    private List<String> codes;
}
