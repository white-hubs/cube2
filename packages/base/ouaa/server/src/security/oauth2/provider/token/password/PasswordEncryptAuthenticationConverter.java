package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password;

import com.guhecloud.cube.biz.ouaa.metadata.constant.EncryptConstants;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.util.ConverterUtils;
import com.guhecloud.cube.biz.ouaa.server.utils.HttpServletRequestUtil;
import com.guhecloud.platform.base.util.AESUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptAuthenticationConverter extends PasswordTokenAuthenticationConverter {
    private static final AuthorizationGrantType GRANT_TYPE = new AuthorizationGrantType("pwd_encrypt");

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (ConverterUtils.unsupportedGrantType(GRANT_TYPE, request)) {
            return null;
        }
        var parameters = HttpServletRequestUtil.getParametersAsSingleValueMap(request);
        var password = parameters.get("password");
        var decrypt = AESUtils.decrypt(EncryptConstants.PASSWORD_AES_SECURITY_KEY, password);
        parameters.put("password", decrypt);
        request.setAttribute("password", decrypt);
        return super.authentication(GRANT_TYPE, parameters);
    }
}
