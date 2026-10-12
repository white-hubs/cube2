package com.guhecloud.cube.srv.gen.identity.server.qrcode.third.wechat.config;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;

import java.util.Objects;

/**
 * 微信公众服务号配置
 *
 * @author lanchun
 * @date 2025/1/6 16:26
 */
@Data
public class WechatPublicServiceAccountConfig {

    /**
     * 第三方用户唯一凭证
     */
    private String appid;

    /**
     * 第三方用户唯一凭证密钥，即appsecret
     */
    private String secret;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WechatPublicServiceAccountConfig that = (WechatPublicServiceAccountConfig) o;
        return Objects.equals(getAppid(), that.getAppid()) && Objects.equals(getSecret(), that.getSecret());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAppid(), getSecret());
    }

    public static WechatPublicServiceAccountConfig getAccountConfig(String accountConfig) {
        return JsonUtils.deserialize(accountConfig, WechatPublicServiceAccountConfig.class);
    }
}
