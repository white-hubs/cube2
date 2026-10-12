package com.guhecloud.cube.biz.ouaa.server.security.authentication.wechat;

import com.guhecloud.cube.biz.ouaa.server.dto.wechat.WechatAccessTokenResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.AbstractOAuth2UserProvider;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.security.exceptions.LoginException;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.WechatService;
import com.guhecloud.platform.base.standard.exception.ErrorTypeEnum;
import com.guhecloud.platform.base.standard.exception.OuaaException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;

@Slf4j
@Component
public class WechatOpenPlatformAuthenticationProvider extends AbstractOAuth2UserProvider<WechatOpenPlatformAuthenticationToken> {

    private final WechatService wechatService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public WechatOpenPlatformAuthenticationProvider(
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, OAuth2AuthorizationService authorizationService,
            WechatService wechatService, UserDetailsService userDetailsService, UserService userService
                                                   ) {
        super(tokenGenerator, authorizationService);
        this.wechatService = wechatService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (WechatOpenPlatformAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected UserAuthenticationToken doAuthenticate(WechatOpenPlatformAuthenticationToken wechatOpenPlatformAuthenticationToken) {
        try {
            return wechatCodeLogin(wechatOpenPlatformAuthenticationToken);
        } catch (Exception e) {
            log.error("微信登录失败", e);
            switch (e) {
                case LoginException ignored -> throw e;
                case OuaaException ouaaException ->
                        throw new LoginException(ouaaException.getMessage(), ouaaException.getCode(), ouaaException.getDetails()
                                .toString());
                case OAuth2AuthenticationException oAuth2AuthenticationException ->
                        throw new LoginException(oAuth2AuthenticationException.getError().getDescription(), oAuth2AuthenticationException
                                .getError()
                                .getErrorCode(), null);
                case ServiceException serviceException ->
                        throw new LoginException(serviceException.getCode(), serviceException.getMessage(), null);
                default -> throw new LoginException("invalid_grant", "微信登录失败", null);
            }
        }

    }

    private UserAuthenticationToken wechatCodeLogin(WechatOpenPlatformAuthenticationToken wechatOpenPlatformAuthenticationToken) {
        String code = wechatOpenPlatformAuthenticationToken.getWechatCode();
        // 获取用户微信token信息
        String tokenId = wechatService.auth(code);
        WechatAccessTokenResponseDTO wechatServiceAccessToken = wechatService.getAccessToken(tokenId);
        String unionId = wechatServiceAccessToken.getUnionId();
        //查询用户绑定信息
        UserWechat userWechat = wechatService.getUserWechatByUnionid(unionId);
        if (Objects.isNull(userWechat)) {
            throw new LoginException(HttpStatus.CONFLICT, ErrorTypeEnum.WECHAT_BIND_USER_NOT_FOUND, tokenId);
        }
        //查询用户信息
        User userInfo = userService.getById(userWechat.getUserId());
        //更新用户微信token信息
        wechatService.updateWechatToken(userInfo.getId(), wechatServiceAccessToken);
        //缓存用户access_token信息
        wechatService.cacheUserWechatAccessToken(userWechat.getUserId(), wechatServiceAccessToken.getAccessToken());
        // 重新查询已保存的用户和权限，返回认证成功对象
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userInfo.getUsername());
        if (!userDetails.isEnabled()) {
            throw new OAuth2AuthenticationException(new OAuth2Error(ErrorTypeEnum.USER_NOT_ACTIVE.getCode(), ErrorTypeEnum.USER_NOT_ACTIVE.getMsg(), null));
        }
        return convertToken(wechatOpenPlatformAuthenticationToken.getGrantType(), (OAuth2ClientAuthenticationToken) wechatOpenPlatformAuthenticationToken.getPrincipal(), userDetails);
    }

    private UserAuthenticationToken convertToken(AuthorizationGrantType authorizationGrantType,
                                                 OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken,
                                                 UserDetails userDetails
                                                ) {
        Map<String, Object> additionalParameters = new LinkedHashMap<>();
        additionalParameters.put(PARAM_USERNAME_KEY, userDetails.getUsername());
        return new UserAuthenticationToken(authorizationGrantType, userDetails.getUsername(), oAuth2ClientAuthenticationToken, additionalParameters);
    }
}
