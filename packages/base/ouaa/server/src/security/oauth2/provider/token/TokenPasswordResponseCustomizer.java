package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token;

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

import static com.guhecloud.cube.biz.ouaa.metadata.constant.PasswordExpiredConstants.PASSWORD_EXPIRATION_DATE;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.PasswordExpiredConstants.PASSWORD_EXPIRATION_REMINDED;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.PasswordExpiredConstants.PASSWORD_EXPIRED;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.PasswordExpiredConstants.PASSWORD_FORCED;

@Slf4j
public class TokenPasswordResponseCustomizer implements OAuth2AccessTokenCustomizer {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    public TokenPasswordResponseCustomizer(OAuth2AuthorizationService oAuth2AuthorizationService
                                          ) {this.oAuth2AuthorizationService = oAuth2AuthorizationService;}

    @Override
    public void customize(OAuth2AccessTokenAuthenticationContext context) {
        OAuth2AccessTokenResponse accessTokenResponse = context.getAccessTokenResponse().build();
        OAuth2AccessToken accessToken = accessTokenResponse.getAccessToken();
        Map<String, Object> additionalParameters = new HashMap<>(accessTokenResponse.getAdditionalParameters());
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(accessToken.getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);
        if (Objects.nonNull(authorization)) {
            Map<String, Object> claims = authorization.getAccessToken().getClaims();
            if (claims == null || claims.isEmpty()) {
                return;
            }
            if (authorization.getAuthorizationGrantType() == AuthorizationGrantType.CLIENT_CREDENTIALS) {
                return;
            } else {
                additionalParameters.put(PASSWORD_EXPIRED, Objects.requireNonNull(claims).get(PASSWORD_EXPIRED));
                additionalParameters.put(PASSWORD_FORCED, claims.get(PASSWORD_FORCED));
                additionalParameters.put(PASSWORD_EXPIRATION_REMINDED, claims.get(PASSWORD_EXPIRATION_REMINDED));
                additionalParameters.put(PASSWORD_EXPIRATION_DATE, claims.get(PASSWORD_EXPIRATION_DATE));
            }
            context.getAccessTokenResponse().additionalParameters(additionalParameters);
        }
    }
}
