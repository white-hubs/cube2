package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token;

import com.guhecloud.platform.security.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class TokenResponseCustomizer implements OAuth2AccessTokenCustomizer {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    public TokenResponseCustomizer(OAuth2AuthorizationService oAuth2AuthorizationService
                                  ) {this.oAuth2AuthorizationService = oAuth2AuthorizationService;}

    @Override
    public void customize(OAuth2AccessTokenAuthenticationContext context) {
        OAuth2AccessTokenResponse accessTokenResponse = context.getAccessTokenResponse().build();
        OAuth2AccessToken accessToken = accessTokenResponse.getAccessToken();
        Map<String, Object> additionalParameters = new HashMap<>(accessTokenResponse.getAdditionalParameters());
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(accessToken.getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);
        if (Objects.isNull(authorization)) {
            return;
        }
        Map<String, Object> claims = authorization.getAccessToken().getClaims();
        if (claims == null || claims.isEmpty()) {
            return;
        }
        if (authorization.getAuthorizationGrantType() == AuthorizationGrantType.CLIENT_CREDENTIALS) {
            additionalParameters.put(TokenUtils.JWT_CLAIM_ID, Objects.requireNonNull(claims).get(TokenUtils.JWT_CLAIM_ID));
            additionalParameters.put(TokenUtils.JWT_CLAIM_ADMIN, claims.get(TokenUtils.JWT_CLAIM_ADMIN));
            additionalParameters.put(TokenUtils.JWT_CLAIM_TENANTS, claims.get(TokenUtils.JWT_CLAIM_TENANTS));
        } else {
            additionalParameters.put(TokenUtils.JWT_CLAIM_ID, Objects.requireNonNull(claims).get(TokenUtils.JWT_CLAIM_ID));
            additionalParameters.put(TokenUtils.JWT_CLAIM_NAME, claims.get(TokenUtils.JWT_CLAIM_NAME));
            additionalParameters.put(TokenUtils.JWT_CLAIM_ADMIN, claims.get(TokenUtils.JWT_CLAIM_ADMIN));
            additionalParameters.put(TokenUtils.JWT_CLAIM_TENANTS, claims.get(TokenUtils.JWT_CLAIM_TENANTS));
            additionalParameters.put(TokenUtils.JWT_CLAIM_ADMINISTRATED_TENANTS, claims.get(TokenUtils.JWT_CLAIM_ADMINISTRATED_TENANTS));
            if (claims.containsKey(TokenUtils.JWT_CLAIM_TERMINAL)) {
                additionalParameters.put(TokenUtils.JWT_CLAIM_TERMINAL, claims.get(TokenUtils.JWT_CLAIM_TERMINAL));
            }
        }
        context.getAccessTokenResponse().additionalParameters(additionalParameters);
    }
}
