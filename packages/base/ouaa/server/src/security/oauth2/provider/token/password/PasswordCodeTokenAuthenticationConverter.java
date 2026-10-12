package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password;

import com.guhecloud.cube.biz.ouaa.metadata.constant.EncryptConstants;
import com.guhecloud.cube.biz.ouaa.server.enumeration.CaptchaTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.security.exceptions.LoginException;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.util.ConverterUtils;
import com.guhecloud.cube.biz.ouaa.server.utils.HttpServletRequestUtil;
import com.guhecloud.cube.biz.ouaa.server.verifier.CaptchaVerifier;
import com.guhecloud.cube.biz.ouaa.server.verifier.dto.CaptchaVerifyDTO;
import com.guhecloud.platform.base.standard.exception.OuaaException;
import com.guhecloud.platform.base.util.AESUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_CODE_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_KEY_KEY;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_PASSWORD_KEY;

@Component
@Slf4j
public class PasswordCodeTokenAuthenticationConverter extends PasswordTokenAuthenticationConverter {
    public static final AuthorizationGrantType GRANT_TYPE = new AuthorizationGrantType("pwd_simple_code");
    private final static String DEFAULT_CAPTCHA_TYPE = "CODE";
    private final CaptchaVerifier captchaVerifier;

    public PasswordCodeTokenAuthenticationConverter(CaptchaVerifier captchaVerifier
                                                   ) {

        this.captchaVerifier = captchaVerifier;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (ConverterUtils.unsupportedGrantType(GRANT_TYPE, request)) {
            return null;
        }
        var parameters = HttpServletRequestUtil.getParametersAsSingleValueMap(request);
        var key = parameters.get(PARAM_KEY_KEY);
        var password = parameters.get(PARAM_PASSWORD_KEY);
        var code = parameters.get(PARAM_CODE_KEY);
        var captchaType = parameters.get("captchaType");
        captchaType = Objects.requireNonNullElse(captchaType, DEFAULT_CAPTCHA_TYPE);
        var captchaTypeEnum = CaptchaTypeEnum.getCaptchaType(captchaType);
        try {
            captchaVerifier.verify(new CaptchaVerifyDTO(code, key, captchaTypeEnum));
        } catch (OuaaException e) {
            log.error("验证码错误信息:{}", e.getMessage());
            throw new LoginException(e.getMessage(), e.getCode(), e.getDetails());
        }
        var decrypt = AESUtils.decrypt(EncryptConstants.PASSWORD_AES_SECURITY_KEY, password);
        parameters.put(PARAM_PASSWORD_KEY, decrypt);
        request.setAttribute(PARAM_PASSWORD_KEY, decrypt);
        return this.authentication(GRANT_TYPE, parameters);

    }

    @Override
    protected Authentication authentication(AuthorizationGrantType grantType, Map<String, String> parameters) {
        return super.authentication(grantType, parameters);
    }
}
