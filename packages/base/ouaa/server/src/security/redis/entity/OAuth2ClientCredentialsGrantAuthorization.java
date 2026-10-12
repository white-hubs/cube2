package com.guhecloud.cube.biz.ouaa.server.security.redis.entity;

import lombok.Getter;

import java.util.Set;

@Getter
public class OAuth2ClientCredentialsGrantAuthorization extends OAuth2AuthorizationGrantAuthorization {

    public OAuth2ClientCredentialsGrantAuthorization() {
       
    }

    public OAuth2ClientCredentialsGrantAuthorization(String id, String registeredClientId, String principalName,
                                                     Set<String> authorizedScopes, AccessToken accessToken
                                                    ) {
        super(id, registeredClientId, principalName, authorizedScopes, accessToken, null);
    }
}
