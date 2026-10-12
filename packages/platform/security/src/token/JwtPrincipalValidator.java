package com.guhecloud.platform.security.token;

import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.platform.security.util.TokenUtils;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtPrincipalValidator implements OAuth2TokenValidator<Jwt> {

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        SecurityContextHolder.setJwt(jwt);
        Principal principal = Principal.builder().id(TokenUtils.getId(jwt)).type(TokenUtils.getType(jwt)).admin(TokenUtils.getAdmin(jwt))
                .account(TokenUtils.getAccount(jwt)).name(TokenUtils.getName(jwt)).build();
        SecurityContextHolder.setPrincipal(principal);
        return OAuth2TokenValidatorResult.success();
    }

}
