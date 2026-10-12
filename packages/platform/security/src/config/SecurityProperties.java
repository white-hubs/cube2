package com.guhecloud.platform.security.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 安全配置属性
 *
 * @author Woody
 * @date 2021/6/16
 * @see WebSecurityConfigurer
 * @see com.guhecloud.platform.security.context.SecurityContextHolder
 */
@Getter
@Configuration
@ConfigurationProperties(prefix = "platform.security")
public class SecurityProperties {
    /**
     * 资源安全配置
     */
    private final Resource resource = new Resource();

    /**
     * 数字签名配置属性
     */
    private final Signature signature = new Signature();

    /**
     * 资源安全配置
     */
    @Data
    public static class Resource {
        /**
         * 拒绝的资源地址，多个地址使用","号隔开
         */
        private String deny;

        /**
         * 允许的资源地址，多个地址使用","号隔开
         */
        private String permit;

        /**
         * 忽略的资源地址，多个地址使用","号隔开
         */
        private String ignore;

        /**
         * 需要认证的资源地址，多个地址使用","号隔开
         */
        private String authenticated;

        /**
         * 资源访问控制列表
         */
        private List<Access> accesses;
    }

    /**
     * 资源访问控制
     */
    @Data
    public static class Access {
        /**
         * 资源地址，多个地址使用","号隔开
         */
        private String uri;

        /**
         * 客户端IP
         */
        private String ip;

        /**
         * 客户端角色
         */
        private String role;

        /**
         * 客户端权限
         */
        private String authority;

        /**
         * 访问许可
         */
        private String permission;
    }

    /**
     * 数字签名配置
     */
    @Data
    public static class Signature {
        /**
         * 是否启用
         */
        private Boolean enable = true;

        /**
         * 签名密钥
         */
        private String secret = "signature@secret";

        /**
         * 签名过期时间（秒）
         */
        private Integer expiration = 3 * 60;
    }
}
