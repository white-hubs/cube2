package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

/**
 * 二维码类型DTO
 *
 * @author system
 */
@Data
public class QrCodeTypeDTO {

    /**
     * 类型ID
     */
    private Long id;

    /**
     * 类型编码
     */
    private String code;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 授权客户端编码
     */
    private String grantClientCode;

    /**
     * 备注
     */
    private String remark;
}
