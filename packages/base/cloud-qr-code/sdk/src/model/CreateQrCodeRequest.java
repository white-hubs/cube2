package com.guhecloud.cube.base.cloud.qr.code.sdk.model;

import lombok.Data;

/**
 * 创建二维码请求
 *
 * @author system
 */
@Data
public class CreateQrCodeRequest {

    private String code;

    /**
     * 二维码名称
     */
    private String name;

    /**
     * 二维码类型ID
     *
     * @deprecated 推荐使用qrTypeCode
     */
    @Deprecated
    private Long qrTypeId;

    /**
     * 二维码类型code（推荐使用）
     */
    private String qrTypeCode;

    /**
     * 二维码内容类型
     */
    private String qrContentType;

    /**
     * 二维码内容
     */
    private String qrContent;

    /**
     * 行为方式：ROUTE-路由跳转、TEXT-文本内容
     */
    private String mode;

    /**
     * 行为内容：路由地址或文本内容
     */
    private String modeContent;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 路由信息
     */
    private QrRouteDTO route;
}
