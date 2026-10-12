package com.guhecloud.cube.biz.ouaa.server.security.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Getter
@RedisHash("oauth2_authorization")
public abstract class OAuth2AuthorizationGrantAuthorization implements Serializable {

    @Id
    private final String id;

    private final String registeredClientId;

    private final String principalName;

    private final Set<String> authorizedScopes;

    private final AccessToken accessToken;

    private final RefreshToken refreshToken;

    protected OAuth2AuthorizationGrantAuthorization() {
        this.id = null;
        this.registeredClientId = null;
        this.principalName = null;
        this.authorizedScopes = null;
        this.accessToken = null;
        this.refreshToken = null;
    }

    // @fold:on
    protected OAuth2AuthorizationGrantAuthorization(String id, String registeredClientId, String principalName,
                                                    Set<String> authorizedScopes, AccessToken accessToken,
                                                    RefreshToken refreshToken
                                                   ) {
        this.id = id;
        this.registeredClientId = registeredClientId;
        this.principalName = principalName;
        this.authorizedScopes = authorizedScopes;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Getter
    public static class ClaimsHolder {

        private final Map<String, Object> claims;

        public ClaimsHolder() {
            this.claims = Collections.emptyMap();
        }

        public ClaimsHolder(Map<String, Object> claims) {
            this.claims = claims;
        }

    }

    @Getter
    public static class AccessToken {

        private final OAuth2AccessToken accessToken;

        private final ClaimsHolder claims;

        public AccessToken() {
            this.accessToken = null;
            this.claims = null;
        }

        public AccessToken(OAuth2AccessToken accessToken, ClaimsHolder claims) {
            this.accessToken = accessToken;
            this.claims = claims;
        }
    }

    @Getter
    public static class RefreshToken {

        private final OAuth2RefreshToken refreshToken;

        public RefreshToken() {
            this.refreshToken = null;
        }

        public RefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
            this.refreshToken = oAuth2RefreshToken;
        }
    }
}
