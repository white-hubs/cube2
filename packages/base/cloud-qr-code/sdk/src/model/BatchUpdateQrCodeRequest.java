package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

import java.util.List;

/**
 * 批量更新二维码请求
 *
 * @author system
 */
@Data
public class BatchUpdateQrCodeRequest {

    /**
     * 更新项列表
     */
    private List<UpdateQrCodeRequest> items;
}
