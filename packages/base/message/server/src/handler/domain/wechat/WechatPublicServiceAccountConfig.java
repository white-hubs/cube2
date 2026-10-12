package com.guhecloud.cube.base.message2.server.handler.domain.wechat;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;

import java.util.Objects;

/**
 * 微信公众服务号推送配置
 *
 * @author lanchun
 * @date 2024/12/31 14:45
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
