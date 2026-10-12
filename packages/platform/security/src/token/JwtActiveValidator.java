package com.guhecloud.platform.security.token;

import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * 令牌有效性验证器
 *
 * @author Woody
 * @date 2021/6/17
 * @see com.guhecloud.platform.security.config.WebSecurityConfigurer
 */
public interface JwtActiveValidator extends OAuth2TokenValidator<Jwt> {
}
