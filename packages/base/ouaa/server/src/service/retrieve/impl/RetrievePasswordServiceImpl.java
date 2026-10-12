package com.guhecloud.cube.biz.ouaa.server.service.retrieve.impl;

import com.guhecloud.cube.biz.ouaa.server.dto.retrieve.RetrieveCacheDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.retrieve.RetrievePasswordResetDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.retrieve.RetrievePasswordTokenDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.enumeration.AuthTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.enumeration.SmsCodeTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.param.SystemUserAuthParam;
import com.guhecloud.cube.biz.ouaa.server.param.retrieve.RetrievePasswordResetParam;
import com.guhecloud.cube.biz.ouaa.server.param.retrieve.RetrievePasswordTokenParam;
import com.guhecloud.cube.biz.ouaa.server.param.validated.PasswordValidated;
import com.guhecloud.cube.biz.ouaa.server.service.SystemUserAuthService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.retrieve.RetrievePasswordService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 密码找回
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RetrievePasswordServiceImpl implements RetrievePasswordService {
    private final StringRedisTemplate redisTemplate;
    private final static Integer RETRIEVE_PASSWORD_TOKEN_EXPIRE_TIME_MINUTES = 5;
    private final static String RETRIEVE_PASSWORD_TOKEN_KEY_PREFIX = "retrieve-password-token:";
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidated passwordValidated;
    private final SystemUserAuthService systemUserAuthService;

    @Override
    public RetrievePasswordTokenDTO getToken(RetrievePasswordTokenParam retrievePasswordTokenParam) {

        long userId = authSystemUser(retrievePasswordTokenParam);
        String resetSessionId = UUID.randomUUID().toString();
        //生成16位的secret,用于前端AES加密
        String secretKey = UUID.randomUUID().toString().substring(0, 16);
        cacheRetrieveInfo(resetSessionId, userId, secretKey);
        return new RetrievePasswordTokenDTO(resetSessionId, secretKey);
    }

    @Override
    public RetrievePasswordResetDTO resetPassword(RetrievePasswordResetParam retrievePasswordResetParam) {
        //校验token
        String cacheStr = redisTemplate.opsForValue()
                .get(RETRIEVE_PASSWORD_TOKEN_KEY_PREFIX + retrievePasswordResetParam.resetSessionId());
        Assert.isNotFound(cacheStr, "会话已过期，请重新获取验证码进行验证！");
        RetrieveCacheDTO retrieveCacheDTO = JsonUtils.deserialize(cacheStr, RetrieveCacheDTO.class);
        retrieveCacheDTO = Objects.requireNonNullElse(retrieveCacheDTO, new RetrieveCacheDTO());
        Assert.isNotFound(retrieveCacheDTO, "请重新获取验证码进行验证！");
        //解析新密码
        String newPassword = retrievePasswordResetParam.newPassword();
        String confirmPassword = retrievePasswordResetParam.confirmPassword();
        //解密
        newPassword = AESUtils.decrypt(retrieveCacheDTO.getSecretKey(), newPassword);
        confirmPassword = AESUtils.decrypt(retrieveCacheDTO.getSecretKey(), confirmPassword);
        Assert.isConflict(!newPassword.equals(confirmPassword), "密码不一致");
        //密码合法性校验
        passwordValidated.validate(newPassword);
        //查询用户
        User user = userService.getById(retrieveCacheDTO.getUserId());
        Assert.isNotFound(user, "用户不存在！");
        //修改密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setValidity("PERMANENT");
        user.setLastUpdatePasswordTime(DateUtils.now());
        boolean update = userService.updateById(user);
        //清除用户的token信息
        redisTemplate.delete(RETRIEVE_PASSWORD_TOKEN_KEY_PREFIX + retrievePasswordResetParam.resetSessionId());
        return new RetrievePasswordResetDTO(update);

    }

    private void cacheRetrieveInfo(String resetSessionId, long userId, String secretKey) {
        //缓存用户的加密信息
        RetrieveCacheDTO retrieveCacheDTO = RetrieveCacheDTO
                .builder()
                .resetSessionId(resetSessionId)
                .userId(userId)
                .secretKey(secretKey)
                .build();
        String cacheStr = JsonUtils.serialize(retrieveCacheDTO);
        redisTemplate.opsForValue().set(RETRIEVE_PASSWORD_TOKEN_KEY_PREFIX +
                                        resetSessionId, Objects.requireNonNullElse(cacheStr, ""), RETRIEVE_PASSWORD_TOKEN_EXPIRE_TIME_MINUTES, TimeUnit.MINUTES);
    }

    private long authSystemUser(RetrievePasswordTokenParam retrievePasswordTokenParam) {
        var authType = Objects.isNull(retrievePasswordTokenParam.type()) ? AuthTypeEnum.SMS : retrievePasswordTokenParam.type();
        var systemUserAuthParam = getSystemUserAuthParam(retrievePasswordTokenParam, authType);
        var tokenId = systemUserAuthService.systemUserAuth(systemUserAuthParam);
        var authInfoCacheDTO = systemUserAuthService.getAuthInfo(tokenId);
        systemUserAuthService.removeAuthInfo(tokenId);
        return authInfoCacheDTO.userId();
    }

    private SystemUserAuthParam getSystemUserAuthParam(RetrievePasswordTokenParam retrievePasswordTokenParam,
                                                       AuthTypeEnum authType
                                                      ) {
        var authParam = new HashMap<String, String>();
        if (AuthTypeEnum.WECHAT.equals(authType)) {
            authParam.put("wechatCode", retrievePasswordTokenParam.code());
        } else if (AuthTypeEnum.SMS.equals(authType)) {
            authParam.put("mobile", retrievePasswordTokenParam.tel());
            authParam.put("smsCode", retrievePasswordTokenParam.code());
            authParam.put("smsCodeType", SmsCodeTypeEnum.RESET_PASSWORD.name());
        }
        return new SystemUserAuthParam(authType, authParam);
    }
}
