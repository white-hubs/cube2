package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.guhecloud.cube.biz.ouaa.server.dto.AuthInfoCacheDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.wechat.WechatAccessTokenResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.wechat.WechatUserInfoResponseDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTel;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.enumeration.AuthTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.enumeration.CaptchaTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.enumeration.OuaaCode;
import com.guhecloud.cube.biz.ouaa.server.enumeration.SmsCodeTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.enumeration.TerminalEnum;
import com.guhecloud.cube.biz.ouaa.server.param.SystemUserAuthParam;
import com.guhecloud.cube.biz.ouaa.server.param.sms.VerifySmsCodeParam;
import com.guhecloud.cube.biz.ouaa.server.security.authentication.password.limiter.PasswordLoginFailNumberLimiter;
import com.guhecloud.cube.biz.ouaa.server.service.SystemUserAuthService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTelService;
import com.guhecloud.cube.biz.ouaa.server.service.UserWechatService;
import com.guhecloud.cube.biz.ouaa.server.service.WechatService;
import com.guhecloud.cube.biz.ouaa.server.service.sms.SmsService;
import com.guhecloud.cube.biz.ouaa.server.verifier.CaptchaVerifier;
import com.guhecloud.cube.biz.ouaa.server.verifier.dto.CaptchaVerifyDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ErrorTypeEnum;
import com.guhecloud.platform.base.standard.exception.OuaaException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordDTO;
import com.guhecloud.platform.cloud.logger.message.BizLogRecordMessage;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemUserAuthServiceImpl implements SystemUserAuthService {
    private final CaptchaVerifier captchaVerifier;
    private final PasswordEncoder passwordEncoder;
    private final PasswordLoginFailNumberLimiter passwordLoginFailNumberLimiter;
    private final UserService userService;
    private final UserTelService userTelService;
    private final SmsService smsService;
    private final WechatService wechatService;
    private final StringRedisTemplate redisTemplate;
    private final UserWechatService userWechatService;
    private final static Integer TOKEN_MINUTES_EXPIRE_TIME = 15;
    private final static String TEMP_AUTH_TOKEN = "temp:auth:token:";
    private final static String KEY = "temp_auth_encrypt_key";

    @Override
    public String systemUserAuth(SystemUserAuthParam param) {
        AuthInfoCacheDTO authInfoCacheDTO = switch (param.authType()) {
            case WECHAT -> authWechat(param.authParam());
            case SMS -> authSms(param.authParam());
            case PASSWORD -> authPassword(param.authParam());
        };
        String tokenId = UUID.randomUUID().toString();
        String serialize = JsonUtils.serialize(authInfoCacheDTO);
        redisTemplate.opsForValue()
                .set(TEMP_AUTH_TOKEN + tokenId, Objects.requireNonNullElse(serialize, ""), TOKEN_MINUTES_EXPIRE_TIME, TimeUnit.MINUTES);
        return tokenId;
    }

    @Override
    public AuthInfoCacheDTO getAuthInfo(String tokenId) {
        String cacheStr = redisTemplate.opsForValue().get(TEMP_AUTH_TOKEN + tokenId);
        if (StringUtils.isBlank(cacheStr)) {
            throw new ServiceException("临时授权已过期，请重新授权！");
        }
        return JsonUtils.deserialize(cacheStr, AuthInfoCacheDTO.class);
    }

    @Override
    public void removeAuthInfo(String tokenId) {
        redisTemplate.delete(TEMP_AUTH_TOKEN + tokenId);
    }

    private AuthInfoCacheDTO authPassword(Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String captchaKey = params.get("captchaKey");
        //目前只有web端需要验证码
        if (TerminalEnum.isWeb()) {
            //验证码验证
            captchaVerifier.verify(new CaptchaVerifyDTO(null, captchaKey, CaptchaTypeEnum.BLOCK_PUZZLE));
        }
        UserDetailsService userDetailsService = ApplicationContextHolder.getBean(UserDetailsService.class);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        authenticationChecks(password, userDetails);
        User user = userService.getUserByAccount(username);

        return new AuthInfoCacheDTO(user.getId(), AuthTypeEnum.PASSWORD, null);
    }

    private AuthInfoCacheDTO authSms(Map<String, String> params) {
        String mobile = params.get("mobile");
        String code = params.get("smsCode");
        String smsCodeType = params.get("smsCodeType");
        //验证短信验证码
        smsService.verifySmsCode(new VerifySmsCodeParam(mobile, code, SmsCodeTypeEnum.valueOf(smsCodeType)));
        //根据手机号查询用户绑定信息
        UserTel userTel = userTelService.getBindUserTel(mobile);
        Assert.isNotFound(userTel, "未查询到手机绑定的用户信息");
        //获取用户信息,然后用户登录信息
        User user = userService.getById(userTel.getUserId());

        return new AuthInfoCacheDTO(user.getId(), AuthTypeEnum.SMS, null);
    }

    private AuthInfoCacheDTO authWechat(Map<String, String> params) {
        String wechatCode = params.get("wechatCode");
        Assert.isParameterError(StringUtils.isBlank(wechatCode), "微信临时授权code不能为空");
        String tokenId = wechatService.auth(wechatCode);
        WechatAccessTokenResponseDTO wechatAccessTokenResponseDTO = wechatService.getAccessToken(tokenId);
        WechatUserInfoResponseDTO wechatUserInfo = wechatService.getWechatUserInfo(wechatAccessTokenResponseDTO.getAccessToken(), wechatAccessTokenResponseDTO.getOpenId());
        UserWechat userWechat = userWechatService.getUserWechatByUnionid(wechatUserInfo.getUnionid());
        //校验当前微信是否已经有绑定的用户
        if (Objects.isNull(userWechat)) {
            throw new ServiceException("当前微信未绑定其他用户");
        }
        //获取用户信息,然后用户登录信息
        User user = userService.getById(userWechat.getUserId());
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("accessToken", wechatAccessTokenResponseDTO);
        payloadMap.put("wechatUserInfo", wechatUserInfo);
        String payload = JsonUtils.serialize(payloadMap);
        return new AuthInfoCacheDTO(user.getId(), AuthTypeEnum.WECHAT, payload);
    }

    private void authenticationChecks(String password, UserDetails user) {
        if (StringUtils.isBlank(password)) {
            log.warn("密码不能为空");
            throw new OuaaException(ErrorTypeEnum.USER_PASSWORD_EMPTY);
        }
        String decrypt = AESUtils.decrypt("comguheyuncloud1", password);
        if (!passwordEncoder.matches(decrypt, user.getPassword())) {
            //密码错误次数计数
            passwordLoginFailNumberLimiter.passwordLoginFailAccumulation(user.getUsername());

            try {
                BizLogRecordDTO loginFailLog = BizLogRecordDTO.builder()
                        .type(OuaaCode.OUAA.name()).typeName(OuaaCode.OUAA.getLabel())
                        .subType("LOGIN").subTypeName("用户登录").operationType(OperationType.LOGIN)
                        .account(user.getUsername()).action("密码错误，绑定失败").fail(true).build();
                RocketMQContextHolder.send(BizLogRecordMessage.TOPIC + ":" + OuaaCode.OUAA, new BizLogRecordMessage(loginFailLog));
            } catch (Exception e) {
                log.error("记录登录日志失败，异常信息为", e);
            }

            throw new OuaaException(ErrorTypeEnum.USER_PASSWORD_ERROR);
        }
    }
}
