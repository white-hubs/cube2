package com.guhecloud.cube.biz.ouaa.server.security.authentication.sms;

import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTel;
import com.guhecloud.cube.biz.ouaa.server.enumeration.SmsCodeTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.param.sms.VerifySmsCodeParam;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.AbstractOAuth2UserProvider;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.security.exceptions.LoginException;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTelService;
import com.guhecloud.cube.biz.ouaa.server.service.sms.SmsService;
import com.guhecloud.platform.base.standard.exception.ErrorTypeEnum;
import com.guhecloud.platform.base.standard.exception.OuaaException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.OAuth2TokenParameterNames.PARAM_USERNAME_KEY;

@Slf4j
@Component
public class SmsAuthenticationProvider extends AbstractOAuth2UserProvider<SmsAuthenticationToken> {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final SmsService smsService;
    private final UserTelService userTelService;

    public SmsAuthenticationProvider(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                     OAuth2AuthorizationService authorizationService, UserDetailsService userDetailsService,
                                     UserService userService, SmsService smsService, UserTelService userTelService
                                    ) {
        super(tokenGenerator, authorizationService);
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.smsService = smsService;
        this.userTelService = userTelService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected UserAuthenticationToken doAuthenticate(SmsAuthenticationToken smsAuthenticationToken) {
        try {
            return smsLogin(smsAuthenticationToken);
        } catch (Exception e) {
            log.error("手机登录失败", e);
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
                default -> throw new LoginException("invalid_grant", "手机登录失败", null);
            }
        }

    }

    private UserAuthenticationToken convertToken(AuthorizationGrantType authorizationGrantType,
                                                 OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken, UserDetails userDetails
                                                ) {
        Map<String, Object> additionalParameters = new LinkedHashMap<>();
        additionalParameters.put(PARAM_USERNAME_KEY, userDetails.getUsername());
        return new UserAuthenticationToken(authorizationGrantType, userDetails.getUsername(), oAuth2ClientAuthenticationToken, additionalParameters);
    }

    private UserAuthenticationToken smsLogin(SmsAuthenticationToken smsAuthenticationToken) {
        String mobile = smsAuthenticationToken.getMobile();
        String code = smsAuthenticationToken.getSmsCode();
        String tokenId = smsService.verifySmsCode(new VerifySmsCodeParam(mobile, code, SmsCodeTypeEnum.LOGIN));
        //检查手机号的绑定
        if (!userTelService.checkTelBindExist(mobile)) {
            throw new LoginException(HttpStatus.CONFLICT, ErrorTypeEnum.USER_NOT_BOUND_TO_TEL, tokenId);
        }
        UserTel bindUserTel = userTelService.getBindUserTel(mobile);
        User user = userService.getById(bindUserTel.getUserId());
        // 重新查询已保存的用户和权限，返回认证成功对象
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
        if (!userDetails.isEnabled()) {
            throw new LoginException(ErrorTypeEnum.USER_NOT_ACTIVE);
        }
        return convertToken(smsAuthenticationToken.getGrantType(), (OAuth2ClientAuthenticationToken) smsAuthenticationToken.getPrincipal(), userDetails);

    }

}
