package com.guhecloud.cube.biz.ouaa.server.enumeration;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

@AllArgsConstructor
@Getter
public enum CaptchaTypeEnum {
    /**
     * 滑块验证码
     */
    BLOCK_PUZZLE,
    /**
     * 普通验证码
     */
    CODE;

    public static CaptchaTypeEnum getCaptchaType(String captchaType) {
        if (StringUtils.isBlank(captchaType)) {
            throw new OAuth2AuthenticationException(new OAuth2Error("unknow_captcha_type", "未知的验证码类型", null));
        }
        try {
            return CaptchaTypeEnum.valueOf(captchaType);
        } catch (Exception e) {
            throw new OAuth2AuthenticationException(new OAuth2Error("unknow_captcha_type", "未知的验证码类型", null));
        }

    }
}
