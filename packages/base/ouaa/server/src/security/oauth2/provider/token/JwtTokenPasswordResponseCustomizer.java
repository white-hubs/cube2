package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.UserValidates;
import com.guhecloud.cube.biz.ouaa.server.entity.PasswordSetting;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.UserAuthenticationToken;
import com.guhecloud.cube.biz.ouaa.server.service.PasswordService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.guhecloud.cube.biz.ouaa.metadata.constant.PasswordExpiredConstants.PASSWORD_EXPIRATION_DATE;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.PasswordExpiredConstants.PASSWORD_EXPIRATION_REMINDED;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.PasswordExpiredConstants.PASSWORD_EXPIRED;
import static com.guhecloud.cube.biz.ouaa.metadata.constant.PasswordExpiredConstants.PASSWORD_FORCED;

@Component
public class JwtTokenPasswordResponseCustomizer implements JwtTokenCustomizer {
    @Resource
    private PasswordService passwordService;

    @Resource
    private UserService userService;

    private void passwordValidEnhance(JwtEncodingContext context) {
        var authentication = (UserAuthenticationToken) context.getPrincipal();
        var username = authentication.getUsername();
        var user = this.userService.lookupUserNoContext(username);
        //初始密码必须修改
        if (UserValidates.FOLLOW.name().equalsIgnoreCase(user.getValidity())) {
            this.userValidityEnhance(context);
        } else {
            this.userPasswordValidEnhance(context, user.getLastUpdatePasswordTime());
        }
    }

    private void userValidityEnhance(JwtEncodingContext context) {
        context.getClaims().claim(PASSWORD_EXPIRED, true);
        context.getClaims().claim(PASSWORD_FORCED, true);
        context.getClaims().claim(PASSWORD_EXPIRATION_REMINDED, true);
    }

    private void userPasswordValidEnhance(JwtEncodingContext context, LocalDateTime lastUpdatePasswordTime) {
        var passwordSettingOptional = passwordService.get();
        if (passwordSettingOptional.isPresent()) {
            PasswordSetting passwordSetting = passwordSettingOptional.get();

            Boolean valid = Objects.nonNull(lastUpdatePasswordTime) && passwordSetting.isValid(lastUpdatePasswordTime);
            context.getClaims().claim(PASSWORD_EXPIRED, valid);
            context.getClaims().claim(PASSWORD_FORCED, passwordSetting.getForced());
            context.getClaims().claim(PASSWORD_EXPIRATION_REMINDED,
                                      Objects.nonNull(lastUpdatePasswordTime) && passwordSetting.isReminded(lastUpdatePasswordTime));
            context.getClaims().claim(PASSWORD_EXPIRATION_DATE, Objects.isNull(lastUpdatePasswordTime) ? DateUtils.now()
                                                                                                       : passwordSetting.getExpireTime(lastUpdatePasswordTime));
        } else {
            context.getClaims().claim(PASSWORD_EXPIRED, false);
            context.getClaims().claim(PASSWORD_FORCED, false);
            context.getClaims().claim(PASSWORD_EXPIRATION_REMINDED, false);
        }
    }

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getAuthorizationGrantType() != AuthorizationGrantType.CLIENT_CREDENTIALS) {
            passwordValidEnhance(context);
        }
    }
}
