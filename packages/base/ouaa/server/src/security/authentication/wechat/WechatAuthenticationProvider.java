package com.guhecloud.cube.biz.ouaa.server.security.authentication.wechat;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.UserSource;
import com.guhecloud.cube.biz.ouaa.server.dto.PhoneInfo;
import com.guhecloud.cube.biz.ouaa.server.dto.WechatResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.enumeration.UserTenantBeloneEnum;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.AbstractOAuth2UserProvider;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.cube.biz.ouaa.server.service.WechatService;
import com.guhecloud.cube.biz.ouaa.server.utils.PasswordUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.base.util.ExceptionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.database.exception.UniqueViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
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
public class WechatAuthenticationProvider extends AbstractOAuth2UserProvider<WechatAuthenticationToken> {

    private final WechatService wechatService;

    private final UserDetailsService userDetailsService;

    private final PasswordUtils passwordUtils;

    private final UserTenantService userTenantService;
    private final UserService userService;

    public WechatAuthenticationProvider(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                        OAuth2AuthorizationService authorizationService, WechatService wechatService,
                                        UserDetailsService userDetailsService, PasswordUtils passwordUtils
            , UserTenantService userTenantService, UserService userService
                                       ) {
        super(tokenGenerator, authorizationService);
        this.wechatService = wechatService;
        this.userDetailsService = userDetailsService;
        this.passwordUtils = passwordUtils;
        this.userTenantService = userTenantService;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (WechatAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected UserAuthenticationToken doAuthenticate(WechatAuthenticationToken authentication) {
        var mobile = authentication.getMobile();
        var token = authentication.getToken();
        var iv = authentication.getIv();
        // 获取用户会话信息
        var session = this.wechatService.token2session(token);
        if (session == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "用户授权超时", null));
        }
        var phoneInfo = JsonUtils.deserialize(AESUtils.decode(mobile, session.getKey(), iv), PhoneInfo.class);
        mobile = phoneInfo.getPhoneNumber();
        var userDetails = registeredIfNotExist(mobile, session);
        return convertToken(authentication.getGrantType(), (OAuth2ClientAuthenticationToken) authentication.getPrincipal(), userDetails);
    }

    private UserAuthenticationToken convertToken(AuthorizationGrantType authorizationGrantType,
                                                 OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken,
                                                 UserDetails userDetails
                                                ) {
        Map<String, Object> additionalParameters = new LinkedHashMap<>();
        additionalParameters.put(PARAM_USERNAME_KEY, userDetails.getUsername());
        return new UserAuthenticationToken(authorizationGrantType, userDetails.getUsername(), oAuth2ClientAuthenticationToken, additionalParameters);
    }

    private UserDetails registeredIfNotExist(String mobile, WechatResponseDTO.Session session) {
        var register = this.wechatService.isRegistered(mobile);
        if (!register) {
            registered(mobile, session);
        }

        if (register) {
            var user = userService.lookupUser(mobile);
            //查询用户是否关联了该租户
            var userTenant = userTenantService.getUserTenantByUserIdAndTenant(user.getId(), SessionContextHolder.getRequester()
                    .getTenant());
            if (userTenant == null) {
                //保存用户和租户关系
                userTenant = UserTenant.builder().userId(user.getId()).tenant(SessionContextHolder.getRequester().getTenant())
                        .belongTenant(UserTenantBeloneEnum.NOT_BELONG.name()).build();
                userTenantService.save(userTenant);
            } else {
                var belongTenant = userTenant.getBelongTenant();
                if (belongTenant == null) {
                    belongTenant = UserTenantBeloneEnum.NOT_BELONG.name() + "," + UserTenantBeloneEnum.BELONG.name();
                    userTenant.setBelongTenant(belongTenant);
                    userTenantService.updateById(userTenant);
                } else {
                    if (!belongTenant.contains(UserTenantBeloneEnum.NOT_BELONG.name())) {
                        belongTenant = UserTenantBeloneEnum.NOT_BELONG.name() + "," + belongTenant;
                        userTenant.setBelongTenant(belongTenant);
                        userTenantService.updateById(userTenant);
                    }
                }
            }
        }
        //获取用户信息
        return this.userDetailsService.loadUserByUsername(mobile);
    }

    private void registered(String mobile, WechatResponseDTO.Session session) {
        //用户注册
        try {
            var newUser = User.builder()
                    .id(IdWorker.getId())
                    .name(mobile)
                    .mobile(mobile)
                    .gender(Gender.NONE)
                    .username(mobile)
                    .admin(false)
                    .password(passwordUtils.getInitializePassword())
                    .source(UserSource.WECHAT.name())
                    .enabled(true)
                    .createBy(mobile)
                    .updateBy(mobile)
                    .build();
            this.wechatService.register(session, newUser);
        } catch (Exception e) {
            if (!ExceptionUtils.contains(e, UniqueViolationException.class)) {
                throw new InternalAuthenticationServiceException(e.getMessage());
            }
        }
    }

}
