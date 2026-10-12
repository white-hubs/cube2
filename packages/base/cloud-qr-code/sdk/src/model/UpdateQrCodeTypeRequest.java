package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

/**
 * 更新二维码类型请求
 *
 * @author system
 */
@Data
public class UpdateQrCodeTypeRequest {

    /**
     * 类型编码
     */
    private String code;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;
}
