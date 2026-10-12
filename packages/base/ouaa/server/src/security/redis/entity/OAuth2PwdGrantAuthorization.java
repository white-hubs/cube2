package com.guhecloud.cube.biz.ouaa.server.security.redis.entity;

import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
public class OAuth2PwdGrantAuthorization extends OAuth2AuthorizationGrantAuthorization {

    private final String username;

    private final Map<String, Object> additionalParameters;

    public OAuth2PwdGrantAuthorization() {
        this.username = null;
        this.additionalParameters = null;
    }

    public OAuth2PwdGrantAuthorization(String id, String registeredClientId, String principalName, Set<String> authorizedScopes,
                                       AccessToken accessToken, RefreshToken refreshToken, String username,
                                       Map<String, Object> additionalParameters
                                      ) {
        super(id, registeredClientId, principalName, authorizedScopes, accessToken, refreshToken);
        this.username = username;
        this.additionalParameters = additionalParameters;
    }
}
