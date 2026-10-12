package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

import java.util.List;

/**
 * 批量创建二维码类型请求
 *
 * @author system
 */
@Data
public class BatchCreateQrCodeTypeRequest {

    /**
     * 二维码类型列表
     */
    private List<CreateQrCodeTypeRequest> items;
}
