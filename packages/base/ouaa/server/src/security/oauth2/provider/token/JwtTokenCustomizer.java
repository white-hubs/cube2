package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token;

import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

@FunctionalInterface
public interface JwtTokenCustomizer {

    void customize(JwtEncodingContext context);
}
