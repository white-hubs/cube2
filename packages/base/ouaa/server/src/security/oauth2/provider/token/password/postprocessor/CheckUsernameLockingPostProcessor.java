package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.postprocessor;

import com.guhecloud.platform.base.standard.exception.ErrorTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.limiter.PasswordLoginFailNumberLimiter;
import com.guhecloud.cube.biz.ouaa.server.security.exceptions.LoginException;
import org.springframework.stereotype.Component;

/**
 * 用户账号锁定校验
 *
 * @author lanchun
 * @date 2023/1/31 14:41
 */
@Component
public class CheckUsernameLockingPostProcessor implements PreCheckPostProcessor {

    private final PasswordLoginFailNumberLimiter passwordLoginFailNumberLimiter;

    public CheckUsernameLockingPostProcessor(PasswordLoginFailNumberLimiter passwordLoginFailNumberLimiter) {
        this.passwordLoginFailNumberLimiter = passwordLoginFailNumberLimiter;
    }

    @Override
    public void postBefore(String username) {
        if (passwordLoginFailNumberLimiter.getLockAccount(username)) {
            throw new LoginException(ErrorTypeEnum.USER_PASSWORD_ERROR_TOO_MANY_TIMES);
        }
    }
}
