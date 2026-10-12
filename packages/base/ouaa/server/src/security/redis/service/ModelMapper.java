package com.guhecloud.cube.biz.ouaa.server.security.redis.service;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.PasswordTokenAuthenticationConverter;
import com.guhecloud.cube.biz.ouaa.server.security.redis.entity.OAuth2AuthorizationGrantAuthorization;
import com.guhecloud.cube.biz.ouaa.server.security.redis.entity.OAuth2ClientCredentialsGrantAuthorization;
import com.guhecloud.cube.biz.ouaa.server.security.redis.entity.OAuth2PwdGrantAuthorization;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.security.Principal;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.server.security.redis.entity.OAuth2AuthorizationGrantAuthorization.AccessToken;
import static com.guhecloud.cube.biz.ouaa.server.security.redis.entity.OAuth2AuthorizationGrantAuthorization.RefreshToken;

final class ModelMapper {

    static OAuth2AuthorizationGrantAuthorization convertOAuth2AuthorizationGrantAuthorization(
            OAuth2Authorization authorization
                                                                                             ) {
        if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(authorization.getAuthorizationGrantType())) {
            return convertOAuth2ClientCredentialsGrantAuthorization(authorization);
        } else {
            return convertOAuth2PwdGrantAuthorization(authorization);
        }
    }

    static OAuth2PwdGrantAuthorization convertOAuth2PwdGrantAuthorization(
            OAuth2Authorization authorization
                                                                         ) {
        var userAuthenticationToken = (UserAuthenticationToken) authorization.getAttribute(Principal.class.getName());
        return new OAuth2PwdGrantAuthorization(authorization.getId(),
                                               authorization.getRegisteredClientId(), authorization.getPrincipalName(),
                                               authorization.getAuthorizedScopes(), extractAccessToken(authorization),
                                               extractRefreshToken(authorization),
                                               Objects.requireNonNull(userAuthenticationToken).getUsername(),
                                               userAuthenticationToken.getAdditionalParameters());
    }

    static OAuth2ClientCredentialsGrantAuthorization convertOAuth2ClientCredentialsGrantAuthorization(
            OAuth2Authorization authorization
                                                                                                     ) {

        OAuth2AuthorizationGrantAuthorization.AccessToken accessToken = extractAccessToken(authorization);

        return new OAuth2ClientCredentialsGrantAuthorization(authorization.getId(),
                                                             authorization.getRegisteredClientId(), authorization.getPrincipalName(),
                                                             authorization.getAuthorizedScopes(), accessToken);
    }

    static AccessToken extractAccessToken(OAuth2Authorization authorization) {
        if (authorization.getAccessToken() != null) {
            OAuth2Authorization.Token<OAuth2AccessToken> oauth2AccessToken = authorization.getAccessToken();
            return new OAuth2AuthorizationGrantAuthorization.AccessToken(
                    oauth2AccessToken.getToken(),
                    new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(oauth2AccessToken.getClaims()));
        }
        return null;
    }

    static RefreshToken extractRefreshToken(OAuth2Authorization authorization) {
        if (authorization.getRefreshToken() != null) {
            OAuth2Authorization.Token<OAuth2RefreshToken> oAuth2RefreshTokenToken = authorization.getRefreshToken();
            return new OAuth2AuthorizationGrantAuthorization.RefreshToken(
                    oAuth2RefreshTokenToken.getToken());
        }
        return null;
    }

    static void mapOAuth2AuthorizationGrantAuthorization(
            OAuth2AuthorizationGrantAuthorization authorizationGrantAuthorization,
            RegisteredClient registeredClient,
            OAuth2Authorization.Builder builder
                                                        ) {
        if (authorizationGrantAuthorization instanceof OAuth2PwdGrantAuthorization authorization) {
            mapOAuth2PwdGrantAuthorization(authorization, registeredClient, builder);
        } else if (authorizationGrantAuthorization instanceof OAuth2ClientCredentialsGrantAuthorization authorization) {
            mapOAuth2ClientCredentialsGrantAuthorization(authorization, builder);
        }
    }

    static void mapOAuth2PwdGrantAuthorization(OAuth2PwdGrantAuthorization authorizationGrantAuthorization,
                                               RegisteredClient registeredClient,
                                               OAuth2Authorization.Builder builder
                                              ) {
        var oAuth2ClientAuthenticationToken = new OAuth2ClientAuthenticationToken(registeredClient,
                                                                                  ClientAuthenticationMethod.CLIENT_SECRET_POST,
                                                                                  null);
        var userAuthenticationToken = new UserAuthenticationToken(PasswordTokenAuthenticationConverter.GRANT_TYPE,
                                                                  authorizationGrantAuthorization.getUsername(),
                                                                  oAuth2ClientAuthenticationToken,
                                                                  authorizationGrantAuthorization.getAdditionalParameters());
        builder.id(authorizationGrantAuthorization.getId())
                .principalName(authorizationGrantAuthorization.getPrincipalName())
                .attribute(Principal.class.getName(), userAuthenticationToken)
                .authorizationGrantType(PasswordTokenAuthenticationConverter.GRANT_TYPE)
                .authorizedScopes(authorizationGrantAuthorization.getAuthorizedScopes())
                .refreshToken(authorizationGrantAuthorization.getRefreshToken().getRefreshToken());
        mapAccessToken(authorizationGrantAuthorization.getAccessToken(), builder);
    }

    static void mapOAuth2ClientCredentialsGrantAuthorization(
            OAuth2ClientCredentialsGrantAuthorization clientCredentialsGrantAuthorization,
            OAuth2Authorization.Builder builder
                                                            ) {

        builder.id(clientCredentialsGrantAuthorization.getId())
                .principalName(clientCredentialsGrantAuthorization.getPrincipalName())
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizedScopes(clientCredentialsGrantAuthorization.getAuthorizedScopes());
        mapAccessToken(clientCredentialsGrantAuthorization.getAccessToken(), builder);
    }

    static void mapAccessToken(OAuth2AuthorizationGrantAuthorization.AccessToken accessToken,
                               OAuth2Authorization.Builder builder
                              ) {
        if (accessToken == null) {
            return;
        }
        builder.token(accessToken.getAccessToken(), (metadata) -> {
            metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, accessToken
                    .getClaims().getClaims());
            metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, false);
        });
    }
}
