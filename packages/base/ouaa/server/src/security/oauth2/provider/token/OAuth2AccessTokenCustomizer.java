package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token;

import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationContext;

public interface OAuth2AccessTokenCustomizer {

    /**
     * 自定义 access token response 内容
     *
     * @param context authorization
     *
     * @return additional information to include in the access token response
     */
    void customize(OAuth2AccessTokenAuthenticationContext context);
}
