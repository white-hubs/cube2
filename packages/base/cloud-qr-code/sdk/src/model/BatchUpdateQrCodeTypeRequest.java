package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

import java.util.List;

/**
 * 批量更新二维码类型请求
 *
 * @author system
 */
@Data
public class BatchUpdateQrCodeTypeRequest {

    /**
     * 更新项列表
     */
    private List<UpdateQrCodeTypeRequest> items;
}
