package com.guhecloud.cube.biz.ouaa.server.security.authentication.password;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.AliasConfigEnum;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.AbstractOAuth2UserProvider;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.limiter.PasswordLoginFailNumberLimiter;
import com.guhecloud.cube.biz.ouaa.server.security.exceptions.LoginException;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.postprocessor.LoginSuccessProcessor;
import com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.postprocessor.PreCheckPostProcessor;
import com.guhecloud.platform.base.standard.exception.ErrorTypeEnum;
import com.guhecloud.platform.base.standard.exception.OuaaException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordDTO;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordMessage;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Setter
@Component
public class UserAuthenticationProvider extends AbstractOAuth2UserProvider<PasswordAuthenticationToken> {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final List<LoginSuccessProcessor> loginSuccessProcessors;

    private final List<PreCheckPostProcessor> preCheckPostProcessors;

    private final PasswordLoginFailNumberLimiter passwordLoginFailNumberLimiter;

    public UserAuthenticationProvider(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                      OAuth2AuthorizationService authorizationService, UserDetailsService userDetailsService,
                                      PasswordEncoder passwordEncoder, List<LoginSuccessProcessor> loginSuccessProcessors,
                                      List<PreCheckPostProcessor> preCheckPostProcessors,
                                      PasswordLoginFailNumberLimiter passwordLoginFailNumberLimiter
                                     ) {
        super(tokenGenerator, authorizationService);
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.loginSuccessProcessors = loginSuccessProcessors;
        this.preCheckPostProcessors = preCheckPostProcessors;
        this.passwordLoginFailNumberLimiter = passwordLoginFailNumberLimiter;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (PasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected UserAuthenticationToken doAuthenticate(PasswordAuthenticationToken authentication) {
        return login(authentication);
    }

    public UserAuthenticationToken login(PasswordAuthenticationToken authentication) {
        try {
            var username = authentication.getUsername();
            var password = authentication.getPassword();
            preLoginCheckProcess(username);
            UserDetails user = retrieveUser(username);
            preAuthenticationChecks(user);
            authenticationChecks(password, user);
            loginSuccessProcess(user);
            authentication.setAuthenticated(true);
            return authentication.toUserAuthenticationToken();
        } catch (Exception e) {
            log.error("账号登录失败", e);
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
                default -> throw new LoginException("invalid_grant", "账号登录失败", null);
            }
        }

    }

    private void loginSuccessProcess(UserDetails user) {
        if (CollectionUtils.isNotEmpty(this.loginSuccessProcessors)) {
            this.loginSuccessProcessors.forEach(loginSuccessProcessor -> {
                loginSuccessProcessor.postAfter(user);
            });
        }
        try {
            var loginSuccessLog = BizLogRecordDTO.builder().account(user.getUsername()).type(AliasConfigEnum.OuaaCode.OUAA.name())
                    .typeName(AliasConfigEnum.OuaaCode.OUAA.getLabel()).subType("LOGIN").subTypeName("用户登录")
                    .operationType(OperationType.LOGIN).action("登录成功").fail(false).build();
            RocketMQContextHolder.send(
                    BizLogRecordMessage.TOPIC + ":" + AliasConfigEnum.OuaaCode.OUAA, new BizLogRecordMessage(loginSuccessLog));
        } catch (Exception e) {
            log.error("记录登录日志失败，异常信息为：", e);
        }
    }

    private void preLoginCheckProcess(String username) {
        if (CollectionUtils.isNotEmpty(this.preCheckPostProcessors)) {
            this.preCheckPostProcessors.forEach(preCheckPostProcessor -> {
                preCheckPostProcessor.postBefore(username);
            });
        }
    }

    private UserDetails retrieveUser(String username) {
        if (StringUtils.isEmpty(username)) {
            log.warn("用户名不能为空：{}", username);
            throw new LoginException(ErrorTypeEnum.LOGIN_INFO_ERROR);
        }
        var userDetails = this.userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            log.warn("账号不存在：{}", username);
            passwordLoginFailNumberLimiter.passwordLoginFailAccumulation(username);
            try {
                var loginFailLog = BizLogRecordDTO.builder().type(AliasConfigEnum.OuaaCode.OUAA.name())
                        .typeName(AliasConfigEnum.OuaaCode.OUAA.getLabel()).subType("LOGIN").subTypeName("用户登录")
                        .operationType(OperationType.LOGIN).account(username).action("账户不存在，登录失败").fail(true).build();
                RocketMQContextHolder.send(
                        BizLogRecordMessage.TOPIC + ":" + AliasConfigEnum.OuaaCode.OUAA, new BizLogRecordMessage(loginFailLog));
            } catch (Exception e) {
                log.error("记录登录日志失败，异常信息为：", e);
            }
            throw new LoginException(ErrorTypeEnum.LOGIN_INFO_ERROR);

        }
        return userDetails;
    }

    private void preAuthenticationChecks(UserDetails user) {
        if (!user.isEnabled()) {
            log.warn("账号未启用：{}", user.getUsername());
            throw new LoginException(ErrorTypeEnum.LOGIN_INFO_ERROR);
        }
    }

    private void authenticationChecks(String password, UserDetails user) {
        if (StringUtils.isEmpty(password)) {
            log.warn("密码不能为空: {}", user.getUsername());
            throw new LoginException(ErrorTypeEnum.LOGIN_INFO_ERROR);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            //密码错误次数计数
            passwordLoginFailNumberLimiter.passwordLoginFailAccumulation(user.getUsername());

            try {
                var loginFailLog = BizLogRecordDTO.builder().type(AliasConfigEnum.OuaaCode.OUAA.name())
                        .typeName(AliasConfigEnum.OuaaCode.OUAA.getLabel()).subType("LOGIN").subTypeName("用户登录")
                        .operationType(OperationType.LOGIN).account(user.getUsername()).action("密码错误，登录失败").fail(true).build();
                RocketMQContextHolder.send(
                        BizLogRecordMessage.TOPIC + ":" + AliasConfigEnum.OuaaCode.OUAA, new BizLogRecordMessage(loginFailLog));
            } catch (Exception e) {
                log.error("记录登录日志失败，异常信息为：", e);
            }

            log.warn("密码错误: {}", user.getUsername());
            throw new LoginException(ErrorTypeEnum.LOGIN_INFO_ERROR);
        }
    }

}
