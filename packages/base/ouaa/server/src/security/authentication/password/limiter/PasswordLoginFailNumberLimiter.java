package com.guhecloud.cube.biz.ouaa.server.security.authentication.password.limiter;

import com.guhecloud.cube.biz.ouaa.server.entity.PasswordSetting;
import com.guhecloud.cube.biz.ouaa.server.service.PasswordService;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 账号密码登陆失败时计数限制
 *
 * @author lanchun
 * @date 2023/1/31 11:35
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasswordLoginFailNumberLimiter {

    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private final PasswordService passwordService;

    private static final String USER_AUTH_LOGIN_FAIL_KEY = "userPasswordLoginFail";

    private static final String USER_ACCOUNT_LOCKING_KEY = "userAccountLocking";

    /**
     * 账号登录失败次数累加
     *
     * @param username
     */
    public void passwordLoginFailAccumulation(String username) {
        Optional<PasswordSetting> passwordSettingOptional = passwordService.get();
        if (!passwordSettingOptional.isPresent()) {
            return;
        }
        PasswordSetting passwordSetting = passwordSettingOptional.get();
        if (Objects.isNull(passwordSetting.getManyLoginFailuresCalibrated()) ||
            Boolean.FALSE.equals(passwordSetting.getManyLoginFailuresCalibrated())) {
            return;
        }
        int loginFailCount = 1;
        Object loginFailCountObject = this.redisTemplate.opsForValue().get(USER_AUTH_LOGIN_FAIL_KEY + ":" + username);
        RedissonRedLock usernameLock = RedisUtils.getDistributedLock().tryLock(username, Expiration.from(5000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(usernameLock)) {
                if (Objects.nonNull(loginFailCountObject)) {
                    //错误次数
                    loginFailCount += (Integer) loginFailCountObject;
                }
                //累加次数
                this.redisTemplate.opsForValue().set(USER_AUTH_LOGIN_FAIL_KEY + ":" + username, loginFailCount);
                //当错误次数达到了指定次数后在一定时间内锁定账号
                if (passwordSetting.getAccountLoginAllowedFailNum() <= loginFailCount) {
                    this.redisTemplate.delete(USER_AUTH_LOGIN_FAIL_KEY + ":" + username);
                    this.accountLocking(username, passwordSetting.getAccountLoginLockingDuration());
                    throw new BadCredentialsException(
                            "密码错误" + loginFailCount + "次，请" + passwordSetting.getAccountLoginLockingDuration() + "分钟以后再试");
                }
            }
        } catch (Exception e) {
            log.error("user login password fail synchronize failed: {}", e.getMessage());
            throw e;
        } finally {
            if (Objects.nonNull(usernameLock)) {
                usernameLock.unlock();
            }
        }
    }

    /**
     * 获取锁定账号
     *
     * @param username
     *
     * @return
     */
    public boolean getLockAccount(String username) {
        return Objects.nonNull(this.redisTemplate.opsForValue().get(USER_ACCOUNT_LOCKING_KEY + ":" + username));
    }

    /**
     * 获取锁定账号的过期时间
     *
     * @param username
     *
     * @return
     */
    public Long getLockAccountExpireTime(String username) {
        return this.redisTemplate.getExpire(USER_ACCOUNT_LOCKING_KEY + ":" + username);
    }

    /**
     * 锁定登录账号
     *
     * @param username
     * @param timeout
     */
    public void accountLocking(String username, Integer timeout) {
        this.accountLocking(username, timeout, TimeUnit.MINUTES);
    }

    /**
     * 锁定登录账号
     *
     * @param username
     * @param timeout
     * @param timeUnit
     */
    public void accountLocking(String username, Integer timeout, TimeUnit timeUnit) {
        this.redisTemplate.opsForValue().set(USER_ACCOUNT_LOCKING_KEY + ":" + username, username, timeout, timeUnit);
    }

    /**
     * 账号登录成功时清除失败计数
     *
     * @param username
     */
    public void passwordLoginSuccessClear(String username) {
        this.redisTemplate.delete(USER_AUTH_LOGIN_FAIL_KEY + ":" + username);
    }
}
