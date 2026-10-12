package com.guhecloud.platform.cloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Http客户端连接吃配置属性
 *
 * @author Woody
 * @date 2021/7/25
 */
@Data
@Configuration
@ConfigurationProperties("platform.httpclient.pool")
public class HttpClientPoolProperties {
    /**
     * 连接池的最大连接数
     */
    private int maxTotal = 20;

    /**
     * 同路由的并发数
     */
    private int defaultMaxPerRoute = 2;

    /**
     * 重试次数
     */
    private int retryCount = 2;

    /**
     * 客户端和服务器建立连接超时时间（毫秒）
     */
    private int connectTimeout = 2000;

    /**
     * 指客户端从服务器读取数据包的间隔超时时间（毫秒
     */
    private int socketTimeout = 30000;

    /**
     * 长连接保持时间（毫秒）
     */
    private int keepAliveTime = 60000;

    /**
     * 从连接池获取连接的超时时间（毫秒）
     */
    private int connectionRequestTimeout = 200;

    /**
     * 连接空间超时时间（毫秒）
     */
    private int maxIdleTime = 30000;
}
