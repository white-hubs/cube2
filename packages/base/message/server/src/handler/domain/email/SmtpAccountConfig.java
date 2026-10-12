package com.guhecloud.cube.base.message2.server.handler.domain.email;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Getter;

/**
 * smtp邮件发送配置
 *
 * @author lanchun
 * @date 2025/4/28 18:10
 */
@Getter
public class SmtpAccountConfig {

    /**
     * 发送方邮件
     */
    private String sendEmail;

    /**
     * 发送方邮件密码
     */
    private String sendEmailPassword;

    /**
     * 邮箱的SMTP服务器地址
     */
    private String smtpHost;

    /**
     * 邮箱的SMTP服务器端口号
     */
    private String smtpPort;

    public static SmtpAccountConfig getSmtpAccountConfig(String accountConfig) {
        return JsonUtils.deserialize(accountConfig, SmtpAccountConfig.class);
    }
}
