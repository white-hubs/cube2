package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.postprocessor;

import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.limiter.PasswordLoginFailNumberLimiter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 用户登录
 *
 * @author lanchun
 * @date 2023/1/31 15:50
 */
@Component
public class LoginUsernameUnLockingProcessor implements LoginSuccessProcessor {

    private final PasswordLoginFailNumberLimiter passwordLoginFailNumberLimiter;

    public LoginUsernameUnLockingProcessor(PasswordLoginFailNumberLimiter passwordLoginFailNumberLimiter) {
        this.passwordLoginFailNumberLimiter = passwordLoginFailNumberLimiter;
    }

    @Override
    public void postAfter(UserDetails userDetails) {
        //登录成功,清除错误次数计数
        this.passwordLoginFailNumberLimiter.passwordLoginSuccessClear(userDetails.getUsername());
    }
}
