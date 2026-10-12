package com.guhecloud.cube.biz.ouaa.server.config;

import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.RegisteredClientRepositoryImpl;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.JwtTokenCustomizer;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.TokenPasswordResponseCustomizer;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.TokenResponseCustomizer;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.client.ClientUserTokenConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.exchange.AuthorizationCodeExchangeTokenConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.exchange.KaiHeExchangeTokenConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.PasswordCodeTokenAuthenticationConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.PasswordEncryptAuthenticationConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.PasswordEncryptTokenAuthenticationConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.PasswordTokenAuthenticationConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.sms.SmsTokenConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.wechat.WechatOpenPlatformTokenConverter;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.wechat.WechatTokenConverter;
import com.guhecloud.cube.biz.ouaa.server.security.point.CustomAuthEntryPoint;
import com.guhecloud.cube.biz.ouaa.server.security.redis.service.RedisOAuth2AuthorizationService;
import com.guhecloud.cube.biz.ouaa.server.service.ClientService;
import com.guhecloud.platform.base.util.StringUtils;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AccessTokenResponseAuthenticationSuccessHandler;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Slf4j
@Configuration
@Import(OAuth2AuthorizationServerConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationServerConfigurer {
    private final ApplicationContext applicationContext;
    private final TokenSecurityProperties tokenSecurityProperties;
    private final List<JwtTokenCustomizer> jwtTokenCustomizers;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final ClientService clientService;
    private final PasswordTokenAuthenticationConverter passwordTokenAuthenticationConverter;
    private final PasswordCodeTokenAuthenticationConverter passwordCodeTokenAuthenticationConverter;
    private final PasswordEncryptAuthenticationConverter passwordEncryptAuthenticationConverter;
    private final PasswordEncryptTokenAuthenticationConverter passwordEncryptTokenAuthenticationConverter;
    private final AuthorizationCodeExchangeTokenConverter authorizationCodeExchangeTokenConverter;
    private final KaiHeExchangeTokenConverter kaiheExchangeTokenConverter;
    private final ClientUserTokenConverter clientUserTokenConverter;
    private final WechatTokenConverter wechatTokenConverter;
    private final WechatOpenPlatformTokenConverter wechatOpenPlatformTokenConverter;
    private final SmsTokenConverter smsTokenConverter;

    @Bean
    @Order(Integer.MIN_VALUE)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http, JWKSource<SecurityContext> jwkSource
                                                              ) throws Exception {
        var authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();
        var oauth2AuthorizationService = this.oauth2AuthorizationService(redisTemplate, eventPublisher);
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) -> authorizationServer
                        .tokenGenerator(this.tokenGenerator(jwkSource))
                        .authorizationService(oauth2AuthorizationService)
                        .registeredClientRepository(this.registeredClientRepository())
                        .authorizationServerSettings(this.authorizationServerSettings())
                        .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                                .accessTokenResponseHandler(this.authenticationSuccessHandler(oauth2AuthorizationService))
                                .authenticationProviders(providers ->
                                                                 providers.addAll(this.applicationContext
                                                                                          .getBeansOfType(AuthenticationProvider.class)
                                                                                          .values())
                                                        )
                                .accessTokenRequestConverter(new OAuth2RefreshTokenAuthenticationConverter())
                                .accessTokenRequestConverter(kaiheExchangeTokenConverter)
                                .accessTokenRequestConverter(clientUserTokenConverter)
                                .accessTokenRequestConverter(authorizationCodeExchangeTokenConverter)
                                .accessTokenRequestConverter(passwordTokenAuthenticationConverter)
                                .accessTokenRequestConverter(passwordEncryptAuthenticationConverter)
                                .accessTokenRequestConverter(passwordEncryptTokenAuthenticationConverter)
                                .accessTokenRequestConverter(passwordCodeTokenAuthenticationConverter)
                                .accessTokenRequestConverter(wechatTokenConverter)
                                .accessTokenRequestConverter(smsTokenConverter)
                                .accessTokenRequestConverter(wechatOpenPlatformTokenConverter))
                     )
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new CustomAuthEntryPoint()))
        ;
        //        http.oauth2ResourceServer((s)->s.jwt(Customizer.()));
        return http.build();
    }

    @Bean
    public OAuth2AuthorizationService oauth2AuthorizationService(RedisTemplate<String, Object> redisTemplate,
                                                                 ApplicationEventPublisher eventPublisher
                                                                ) {

        return new RedisOAuth2AuthorizationService(redisTemplate, registeredClientRepository(), eventPublisher);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .authorizationEndpoint("/oauth2/authorize")
                .pushedAuthorizationRequestEndpoint("/oauth2/par")
                .deviceAuthorizationEndpoint("/oauth2/device_authorization")
                .deviceVerificationEndpoint("/oauth2/device_verification")
                .tokenEndpoint("/oauth2/token")
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                .tokenRevocationEndpoint("/oauth2/revoke")
                .jwkSetEndpoint("/oauth2/jwks")
                .build();
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler(OAuth2AuthorizationService oAuth2AuthorizationService) {
        var handler = new OAuth2AccessTokenResponseAuthenticationSuccessHandler();
        handler.setAccessTokenResponseCustomizer(ctx -> {
            tokenResponseCustomizer(oAuth2AuthorizationService).customize(ctx);
            tokenPasswordResponseCustomizer(oAuth2AuthorizationService).customize(ctx);
        });
        return handler;
    }

    @Bean
    OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        var jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(context ->
                                              jwtTokenCustomizers.forEach(consumer -> consumer.customize(context))
                                     );
        var accessTokenGenerator = new OAuth2AccessTokenGenerator();
        var refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public KeyPair keyPair() {
        var keyPair = this.tokenSecurityProperties.getKeyPair();
        Asserts.check(StringUtils.isNotEmpty(keyPair.getAlias()) && StringUtils.isNotEmpty(keyPair.getResource()) &&
                      StringUtils.isNotEmpty(keyPair.getKeyPairPassword()) &&
                      StringUtils.isNotEmpty(keyPair.getKeyStorePassword()), "Invalid key-pair configuration");
        var keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keyPair.getResource()), keyPair
                .getKeyStorePassword().toCharArray());
        return keyStoreKeyFactory.getKeyPair(keyPair.getAlias(), keyPair.getKeyPairPassword().toCharArray());
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        var rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .algorithm(JWSAlgorithm.RS256)
                .build();
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new RegisteredClientRepositoryImpl(clientService);
    }

    @Bean
    public TokenPasswordResponseCustomizer tokenPasswordResponseCustomizer(OAuth2AuthorizationService oAuth2AuthorizationService) {
        return new TokenPasswordResponseCustomizer(oAuth2AuthorizationService);
    }

    @Bean
    public TokenResponseCustomizer tokenResponseCustomizer(OAuth2AuthorizationService oAuth2AuthorizationService) {
        return new TokenResponseCustomizer(oAuth2AuthorizationService);
    }

}
