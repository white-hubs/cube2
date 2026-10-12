package com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author wql
 * @describe 二维码批量删除
 * @date 2023/11/24 15:32
 */
@Data
public class QrCodeBatchDeleteParam {
    @NotBlank(message = "要删除的记录不能为空")
    private String ids;
}
