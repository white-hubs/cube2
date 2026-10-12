package com.guhecloud.cube.base.message2.server.handler.domain.sms;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Getter;

import java.util.Objects;

/**
 * 广东优弘科技短信发送账号配置
 *
 * @author lanchun
 * @date 2024/12/10 10:34
 */
@SuppressWarnings({ "EqualsHashCode" })
@Getter
public class GdYouHongKeJiSmsAccountConfig {

    /**
     * 请求基地址
     */
    private String requestBaseUrl;

    /**
     * 用户帐号，由系统管理员提供
     */
    private String account;

    /**
     * 用户密码, 由系统管理员提供
     */
    private String password;

    /**
     * 虚拟接入号
     */
    private String extno;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GdYouHongKeJiSmsAccountConfig that = (GdYouHongKeJiSmsAccountConfig) o;
        return Objects.equals(account, that.account) && Objects.equals(password, that.password) &&
               Objects.equals(extno, that.extno);
    }

    public static GdYouHongKeJiSmsAccountConfig getJiNoteConfig(String config) {
        return JsonUtils.deserialize(config, GdYouHongKeJiSmsAccountConfig.class);
    }
}
