package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

import java.util.List;

/**
 * 批量创建二维码请求
 *
 * @author system
 */
@Data
public class BatchCreateQrCodeRequest {

    /**
     * 二维码列表
     */
    private List<CreateQrCodeRequest> items;
}
