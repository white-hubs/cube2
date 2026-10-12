package com.guhecloud.cube.biz.ouaa.server.controller;

import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginOutController {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    /**
     * 账号退出登录(注销token)
     *
     * @return Boolean
     */
    @PostMapping(value = "/login-out")
    public Boolean loginOut() {
        OAuth2Authorization accessToken = oAuth2AuthorizationService.findByToken(SecurityContextHolder.getJwt()
                                                                                         .getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);
        if (Objects.nonNull(accessToken)) {
            this.oAuth2AuthorizationService.remove(accessToken);
        }
        return true;
    }
}
