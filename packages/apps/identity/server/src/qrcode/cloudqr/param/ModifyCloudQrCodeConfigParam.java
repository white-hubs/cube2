package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.param;

import lombok.Data;

/**
 * 云二维码配置
 *
 * @author system
 */
@Data
public class ModifyCloudQrCodeConfigParam {

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 应用ID（client_id）
     */
    private String appId;

    /**
     * 应用密钥（client_secret）
     */
    private String appSecret;

    /**
     * 云二维码服务地址
     */
    private String serverUrl;

    /**
     * 是否启用：1-启用，0-禁用
     */
    private Boolean enabled;

    /**
     * 备注
     */
    private String remark;
}
