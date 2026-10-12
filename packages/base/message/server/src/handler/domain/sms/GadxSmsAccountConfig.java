package com.guhecloud.cube.base.message2.server.handler.domain.sms;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;

/**
 * 公安大学短信平台接入参数
 *
 * @author lanchun
 * @date 2025/2/19 9:42
 */
@Data
public class GadxSmsAccountConfig {

    /**
     * 应用id
     */
    private String appid;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 接口地址
     */
    private String host;

    /**
     * 签名
     */
    private String sign;

    public static GadxSmsAccountConfig getConfig(String config) {
        return JsonUtils.deserialize(config, GadxSmsAccountConfig.class);
    }
}
