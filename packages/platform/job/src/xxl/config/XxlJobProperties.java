package com.guhecloud.platform.job.xxl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * XXL-Job 配置属性类
 * <p>
 * 用于绑定 application.yml 中的 xxl.job.* 配置项
 */
@Data
@ConfigurationProperties(prefix = "job.impl.xxl-job")
public class XxlJobProperties {

    /**
     * 调度中心配置
     */
    private JobAdminConfig admin;

    /**
     * 访问令牌（AccessToken）
     * 用于执行器与调度中心之间的安全校验
     * 需与调度中心配置一致
     */
    private String accessToken;

    /**
     * 执行器配置
     */
    private JobExecutorConfig executor;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 调度中心配置
     */
    @Data
    public static class JobAdminConfig {

        /**
         * 调度中心地址，多个用逗号分隔
         */
        private String addresses;
    }

    /**
     * 执行器配置
     */
    @Data
    public static class JobExecutorConfig {

        /**
         * 执行器名称，需与控制台配置一致
         * 每个服务应配置唯一名称，如: attendance-service, maintain-service
         */
        private String appname;

        /**
         * 执行器地址
         */
        private String address;

        /**
         * 执行器 IP 地址
         * 默认为空，表示自动获取
         * 当机器有多个 IP 或需要指定时使用
         */
        private String ip;

        /**
         * 执行器端口号
         * 默认 9999，确保端口不冲突
         * 每个服务启动时会内嵌一个 Jetty 服务用于接收调度请求
         */
        private Integer port;

        /**
         * 任务日志存储路径
         * 必须为绝对路径或相对路径，目录需有读写权限
         */
        private String logpath;

        /**
         * 日志保留天数
         * 默认保留 30 天，0 表示永久保留
         */
        private Integer logretentiondays;
    }
}
