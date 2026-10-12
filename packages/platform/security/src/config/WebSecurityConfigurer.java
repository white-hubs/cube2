package com.guhecloud.platform.security.config;

import com.google.common.collect.Lists;
import com.guhecloud.platform.base.model.ErrorResponse;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.HttpUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.RegexUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.license.filter.LicenseValidationFilter;
import com.guhecloud.platform.security.token.JwtActiveValidator;
import com.guhecloud.platform.security.token.JwtPrincipalValidator;
import com.guhecloud.platform.security.token.JwtTenantValidator;
import com.guhecloud.platform.security.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 资源服务器的安全配置。为体现与Oauth2集成的关系，前半部分为WebSecurity的一般配置，后半部分为资源服务器Oauth2（jwt）授权相关配置
 *
 * @author GJW
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfigurer {

    private final ApplicationContext applicationContext;
    private final SecurityProperties securityProperties;
    private final JwtTenantValidatorProperties tenantValidatorProperties;
    private final OAuth2ResourceServerProperties resourceServerProperties;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        String[] ignores = RegexUtils.splitActives(this.securityProperties.getResource().getIgnore());
        return (web) -> {
            if (ListUtils.isEmpty(ignores)) {
                return;
            }
            web.ignoring().requestMatchers(ignores);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        this.handleHttpRequests(http);
        this.handleSessionManagement(http);
        this.handleOAuth2(http);
        this.deniesControl(http);
        this.permitsControl(http);
        this.authenticatesControl(http);
        this.accessesControl(http);
        this.accessFilter(http);
        return http.httpBasic((configurer) -> {})
                .csrf(CsrfConfigurer::disable)
                .build();
    }

    private void handleHttpRequests(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth.requestMatchers(HttpMethod.OPTIONS).permitAll().requestMatchers("/public/**").permitAll()
                .requestMatchers("/oauth/**").permitAll().requestMatchers("/oauth2/**").permitAll().requestMatchers("/actuator/health")
                .permitAll().requestMatchers("/websocket/**").permitAll());
    }

    private void handleSessionManagement(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    private void handleOAuth2(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2.accessDeniedHandler(this.accessDeniedHandler())
                .authenticationEntryPoint(this.authenticationEntryPoint())
                .jwt(jwt -> jwt.decoder(this.jwtDecoder()).jwtAuthenticationConverter(this.jwtAuthenticationConverter())));
    }

    private void authenticatesControl(HttpSecurity http) throws Exception {
        String[] authenticates = RegexUtils.splitActives(this.securityProperties.getResource().getAuthenticated());
        if (ListUtils.isEmpty(authenticates)) {
            http.authorizeHttpRequests((auth) -> auth.anyRequest().authenticated());
        } else {
            http.authorizeHttpRequests((auth) -> auth.requestMatchers(authenticates).authenticated());
        }
    }

    private void permitsControl(HttpSecurity http) throws Exception {
        String[] permits = RegexUtils.splitActives(this.securityProperties.getResource().getPermit());
        if (ListUtils.isNotEmpty(permits)) {
            http.authorizeHttpRequests((auth) -> auth.requestMatchers(permits).permitAll());
        }
    }

    private void deniesControl(HttpSecurity http) throws Exception {
        String[] denies = RegexUtils.splitActives(this.securityProperties.getResource().getDeny());
        if (ListUtils.isEmpty(denies)) {
            return;
        }
        http.authorizeHttpRequests((auth) -> auth.requestMatchers(denies).denyAll());
    }

    private void accessesControl(HttpSecurity http) throws Exception {
        List<SecurityProperties.Access> accesses = this.securityProperties.getResource().getAccesses();
        if (CollectionUtils.isEmpty(accesses)) {
            return;
        }
        for (SecurityProperties.Access access : accesses) {
            String[] uris = RegexUtils.splitActives(access.getUri());
            if (ListUtils.isEmpty(uris)) {
                continue;
            }
            http.authorizeHttpRequests((auth) -> {
                AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl = auth.requestMatchers(uris);
                if (StringUtils.isNotBlank(access.getIp())) {
                    IpAddressMatcher hasIpAddress = new IpAddressMatcher(access.getIp());
                    authorizedUrl.access((authentication, context) -> new AuthorizationDecision(hasIpAddress.matches(context.getRequest())));
                }
                if (StringUtils.isNotBlank(access.getRole())) {
                    authorizedUrl.hasRole(access.getRole());
                }
                if (StringUtils.isNotBlank(access.getAuthority())) {
                    authorizedUrl.hasAuthority(access.getAuthority());
                }
                if (StringUtils.isNotBlank(access.getPermission())) {
                    authorizedUrl.access(new WebExpressionAuthorizationManager(access.getPermission()));
                }
            });
        }
    }

    private void accessFilter(HttpSecurity http) throws Exception {
        http.addFilterBefore(new LicenseValidationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            HttpUtils.writeUnifiedResponse(response, ErrorResponse.ofSimpleMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase()));
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, exception) -> {
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            if (exception instanceof OAuth2AuthenticationException) {
                OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
                if (error instanceof BearerTokenError) {
                    status = ((BearerTokenError) error).getHttpStatus();
                }
            }
            HttpUtils.writeUnifiedResponse(response, ErrorResponse.ofSimpleMessage(status.value(), status.getReasonPhrase()));
        };
    }

    /* 资源服务器Oauth2（jwt）授权相关配置 Begin */

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(this.resourceServerProperties.getJwt().getJwkSetUri())
                .jwsAlgorithm(SignatureAlgorithm.RS256)
                .build();
        // 添加令牌验证器（注意保持顺序）
        Collection<OAuth2TokenValidator<Jwt>> validators = Lists.newLinkedList();
        // Jwt 时间校验
        validators.add(new JwtTimestampValidator());
        // Jwt Principal 提取（注意！后续会用到 Principal，优先执行！）
        validators.add(this.jwtPrincipalValidator());
        // Jwt 租户范围有效性校验（请求的tenant必须在颁发token时指定的多个租户范围内）
        validators.add(this.jwtTenantValidator());
        // Token 有效性校验
        this.applicationContext.getBeansOfType(JwtActiveValidator.class).forEach((k, v) -> validators.add(v));
        // 代理多个校验器
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(validators));
        return decoder;
    }

    @Bean
    public OAuth2TokenValidator<Jwt> jwtPrincipalValidator() {
        return new JwtPrincipalValidator();
    }

    @Bean
    public OAuth2TokenValidator<Jwt> jwtTenantValidator() {
        return new JwtTenantValidator(tenantValidatorProperties);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(StringUtils.EMPTY_STRING);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(TokenUtils.JWT_CLAIM_AUTHORITIES);
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}