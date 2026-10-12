package com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.config;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.IProviderConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.model.ProviderConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.constants.FusionCommunicateConfigConstant;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TencentConfig extends ProviderConfig {

    /**
     * 凭证id
     */
    private String securityId;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 地域信息(该参数必传)
     */
    private String region;

    /**
     * TRTC的SDKAppId。
     */
    private Long sdkAppId;

    /**
     * TRTC的SDKAppSecret
     */
    private String sdkAppSecretKey;

    public static TencentConfig getTenantConfig() {
        IProviderConfig providerConfig = ApplicationContextHolder.getBean(IProviderConfig.class);
        ProviderConfig config = providerConfig.getProviderConfig(FusionCommunicateConfigConstant.CONFIG_KEY);
        return JsonUtils.deserialize(config.getConfig(), TencentConfig.class);
    }
}
