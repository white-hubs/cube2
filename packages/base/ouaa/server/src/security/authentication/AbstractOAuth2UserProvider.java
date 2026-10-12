package com.guhecloud.cube.biz.ouaa.server.security.authentication;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;
import java.util.Objects;

public abstract class AbstractOAuth2UserProvider<T extends OAuth2AuthorizationGrantAuthenticationToken> implements AuthenticationProvider {

    protected final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    protected final OAuth2AuthorizationService authorizationService;

    protected AbstractOAuth2UserProvider(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                         OAuth2AuthorizationService authorizationService
                                        ) {
        this.tokenGenerator = tokenGenerator;
        this.authorizationService = authorizationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var authToken = (T) authentication;
        var userAuthenticate = doAuthenticate(authToken);
        var clientAuth = getAuthenticatedClientElseThrowInvalidClient(userAuthenticate);
        var registeredClient = clientAuth.getRegisteredClient();
        if (Objects.isNull(registeredClient)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
        }
        if (!registeredClient.getAuthorizationGrantTypes().contains(userAuthenticate.getGrantType())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
        var generatedAccessToken = generateOAuth2Token(OAuth2TokenType.ACCESS_TOKEN, registeredClient, userAuthenticate);
        var accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                                                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                                                generatedAccessToken.getExpiresAt(), registeredClient.getScopes());
        var generatedRefreshToken = generateOAuth2Token(OAuth2TokenType.REFRESH_TOKEN, registeredClient, userAuthenticate);
        var refreshToken = new OAuth2RefreshToken(generatedRefreshToken.getTokenValue(), generatedRefreshToken.getIssuedAt(), generatedRefreshToken.getExpiresAt());
        var authorization = generateAuthorization(registeredClient, clientAuth, userAuthenticate, accessToken, refreshToken, generatedAccessToken);
        this.authorizationService.save(authorization);
        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient, clientAuth, accessToken, refreshToken);
    }

    protected OAuth2Authorization generateAuthorization(RegisteredClient registeredClient, OAuth2ClientAuthenticationToken clientPrincipal,
                                                        UserAuthenticationToken authenticatedToken, OAuth2AccessToken accessToken,
                                                        OAuth2RefreshToken refreshToken, OAuth2Token generatedAccessToken
                                                       ) {
        var claimAccessor = (ClaimAccessor) generatedAccessToken;
        return OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(clientPrincipal.getName())
                .attribute(Principal.class.getName(), authenticatedToken)
                .authorizationGrantType(authenticatedToken.getGrantType())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .token(accessToken, (metadata) -> {
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims());
                    metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, false);
                })
                .token(refreshToken, (metadata) ->
                        metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims())
                      )
                .build();
    }

    protected OAuth2Token generateOAuth2Token(OAuth2TokenType tokenType, RegisteredClient registeredClient,
                                              UserAuthenticationToken authenticatedToken
                                             ) {
        var tokenContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authenticatedToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .tokenType(tokenType)
                .authorizedScopes(registeredClient.getScopes())
                .authorizationGrantType(authenticatedToken.getGrantType())
                .authorizationGrant(authenticatedToken)
                .build();
        var generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the token.", null);
            throw new OAuth2AuthenticationException(error);
        }
        return generatedAccessToken;
    }

    protected OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(UserAuthenticationToken authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    protected abstract UserAuthenticationToken doAuthenticate(T authentication);
}
