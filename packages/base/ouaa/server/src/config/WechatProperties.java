package com.guhecloud.cube.biz.ouaa.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置
 *
 * @author Woody
 * @date 2021/7/23
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cube.biz.ouaa.wechat")
public class WechatProperties {
    /**
     * AppID
     */
    private String id;

    /**
     * AppSecret
     */
    private String secret;

    /**
     * 会话超时时间（秒）
     */
    private Integer expiration = 24 * 60 * 60;
}
