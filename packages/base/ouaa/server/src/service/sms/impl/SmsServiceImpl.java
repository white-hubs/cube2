package com.guhecloud.cube.biz.ouaa.server.service.sms.impl;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.dictionary.adapter.SysDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.base.message2.client.CodeClient;
import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.client.enumeration.CodeVerifyResultEnum;
import com.guhecloud.cube.base.message2.client.param.CodeVerifyParam;
import com.guhecloud.cube.base.message2.client.param.SendVerifyCodeParam;
import com.guhecloud.cube.biz.ouaa.server.captcha.model.common.ResponseModel;
import com.guhecloud.cube.biz.ouaa.server.captcha.model.vo.CaptchaVO;
import com.guhecloud.cube.biz.ouaa.server.captcha.service.CaptchaService;
import com.guhecloud.cube.biz.ouaa.server.dto.sms.SmsCacheDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.sms.SmsLimitDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.enumeration.SmsCodeTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.param.sms.SendSmsCodeParam;
import com.guhecloud.cube.biz.ouaa.server.param.sms.VerifySmsCodeParam;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTelService;
import com.guhecloud.cube.biz.ouaa.server.service.sms.SmsService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ErrorTypeEnum;
import com.guhecloud.platform.base.standard.exception.OuaaException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final CaptchaService captchaService;
    private final CodeClient codeClient;
    private final RedisTemplate<String, String> redisTemplate;
    private final static String SMS_CODE_KEY = "sms:code:";
    private final static String SMS_DICT_SEND_LIMIT_KEY = "sms_limit_config";
    private final static String SMS_DICT_ITEM = "SMS_CONFIG";
    private final static String SYS_ABILITY_DICT_ITEM = "SYS_ABILITY_CONFIG";
    private final static String SMS_ABILITY_DICT_CODE = "sms_code";
    private final static Integer CACHE_TTL_HOUR = 24;
    private final static Integer TEMP_SMS_AUTH_TOKEN_EXPIRE = 10;
    private final static String SMS_TEMP_AUTH_TOKEN_KEY = "temp:sms:auth:token:";
    private final UserService userService;
    private final UserTelService userTelService;

    private final SysDictionaryAdapter dictionaryAdapter;

    @Override
    public Boolean sendSmsCode(SendSmsCodeParam sendSmsCodeParam) {
        Assert.isNotAcceptable(StringUtils.isBlank(sendSmsCodeParam.tel()), "手机号不能为空");
        //校验滑动验证码值
        Assert.isNotAcceptable(!verifyBlockPuzzle(sendSmsCodeParam), "验证码值校验异常");
        checkSmsAbility();
        SmsCodeTypeEnum sendSmsCodeType = sendSmsCodeParam.sendSmsCodeType();
        if (Objects.isNull(sendSmsCodeType)) {
            sendSmsCodeParam = new SendSmsCodeParam(sendSmsCodeParam.captchaVerification(), sendSmsCodeParam.tel(), SmsCodeTypeEnum.LOGIN);
        }
        if (Objects.equals(sendSmsCodeType, SmsCodeTypeEnum.RESET_PASSWORD) ||
            Objects.equals(sendSmsCodeType, SmsCodeTypeEnum.TEL_UNBIND) ||
            Objects.equals(sendSmsCodeType, SmsCodeTypeEnum.LOGIN_BIND)
        ) {
            //验证码类型为重置密码或者解绑手机时，需要验证该手机号是否已在系统中有绑定关系
            boolean telBind = userTelService.checkTelBindExist(sendSmsCodeParam.tel());
            if (!telBind) {
                throw new OuaaException(ErrorTypeEnum.USER_NOT_BOUND_TO_TEL);
            }
        }
        //校验是否可发送验证码值
        validateEnableSend(sendSmsCodeParam);
        //发送验证码
        sendVerifyCode(sendSmsCodeParam);
        //更新缓存中发送记录
        sendRecordUpdate(sendSmsCodeParam);
        return Boolean.TRUE;

    }

    public void checkSmsAbility() {
        SysDictionaryDTO sysDictionaryDTO = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(SYS_ABILITY_DICT_ITEM, SMS_ABILITY_DICT_CODE);
        //未配置，默认关闭
        Assert.isParameterError(
                Objects.isNull(sysDictionaryDTO) || StringUtils.isBlank(sysDictionaryDTO.getVariable()), "短信验证码功能未开启！");
        String variable = sysDictionaryDTO.getVariable();
        boolean enabled = Boolean.parseBoolean(variable);
        Assert.isNotAcceptable(!enabled, "短信验证码功能未开启！");

    }

    @Override
    public String getAuthInfo(String tokenId) {
        String mobile = redisTemplate.opsForValue().get(SMS_TEMP_AUTH_TOKEN_KEY + tokenId);
        if (StringUtils.isBlank(mobile)) {
            throw new ServiceException("手机授权已过期！");
        }
        return mobile;
    }

    @Override
    public void removeAuthInfo(String tokenId) {
        redisTemplate.delete(TEMP_SMS_AUTH_TOKEN_EXPIRE + tokenId);
    }

    @NonNull
    private SmsLimitDTO getSmsLimitDTO() {
        SysDictionaryDTO limitDictDTO = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(SMS_DICT_ITEM, SMS_DICT_SEND_LIMIT_KEY);
        if (Objects.isNull(limitDictDTO)) {
            return SmsLimitDTO.defaultLimit();
        }
        String variable = limitDictDTO.getVariable();
        if (StringUtils.isBlank(variable)) {
            return SmsLimitDTO.defaultLimit();
        }
        SmsLimitDTO sendLimitDTO = JsonUtils.deserialize(variable, SmsLimitDTO.class);
        if (Objects.nonNull(sendLimitDTO)) {
            if (ListUtils.isEmpty(sendLimitDTO.getErrorCountTimeLimits()) ||
                Objects.isNull(sendLimitDTO.getMaxSendCountMinutesScope()) || Objects.isNull(sendLimitDTO.getMaxSendCount())) {
                return SmsLimitDTO.defaultLimit();
            } else {
                return sendLimitDTO;
            }
        }
        return SmsLimitDTO.defaultLimit();
    }

    @Override
    public String verifySmsCode(VerifySmsCodeParam verifySmsCodeParam) {
        checkSmsAbility();
        checkUserErrorCount(verifySmsCodeParam.tel(), verifySmsCodeParam.verifySmsCodeType());
        //验证码验证
        verifySmsCodeValidate(verifySmsCodeParam);
        String tokenId = UUID.randomUUID().toString();
        //缓存短信认证授权时间
        redisTemplate.opsForValue()
                .set(SMS_TEMP_AUTH_TOKEN_KEY + tokenId, verifySmsCodeParam.tel(), TEMP_SMS_AUTH_TOKEN_EXPIRE, TimeUnit.MINUTES);
        return tokenId;
    }

    @Override
    public void checkUserErrorCount(String tel, SmsCodeTypeEnum smsCodeTypeEnum) {
        Assert.isParameterError(Objects.isNull(smsCodeTypeEnum), "验证码类型不能为空");
        Long enableVerifyTime;
        //校验是否用户是否已发送验证码
        String smsCacheStr = redisTemplate.opsForValue().get(SMS_CODE_KEY + smsCodeTypeEnum.getValue() + tel);
        SmsCacheDTO smsCacheDTO = JsonUtils.deserialize(smsCacheStr, SmsCacheDTO.class);
        smsCacheDTO = Objects.requireNonNullElse(smsCacheDTO, SmsCacheDTO.builder().build());
        enableVerifyTime = smsCacheDTO.getEnableVerifyTime();
        if (Objects.nonNull(enableVerifyTime)) {
            long curMilli = DateUtils.currentTimestamp();
            if (curMilli < enableVerifyTime) {
                long duration = enableVerifyTime - curMilli + 10 * 1000;
                int minutes = Math.toIntExact(duration / 1000 / 60);
                String msg = String.format("错误次数过多，请在%s分钟后重试", minutes == 0 ? 1 : minutes);
                throw new OuaaException(msg, ErrorTypeEnum.SMS_CODE_VERIFY_ERROR_TOO_MANY.getCode(), ErrorTypeEnum.SMS_CODE_VERIFY_ERROR_TOO_MANY.getErrType());
            }
        }
    }

    private void validateEnableSend(SendSmsCodeParam sendSmsCodeParam) {
        //校验是否超出了最大发送次数
        SmsCacheDTO smsCacheDTO = getSmsCacheDTO(sendSmsCodeParam);
        if (Objects.nonNull(smsCacheDTO)) {
            SmsLimitDTO smsLimitDTO = getSmsLimitDTO();
            Integer maxSendCount = smsLimitDTO.getMaxSendCount();
            Long enableSendTime = smsCacheDTO.getEnableSendTime();
            long curMilli = DateUtils.currentTimestamp();
            if (smsCacheDTO.getSendCount() > maxSendCount && curMilli < enableSendTime) {
                throw new OuaaException(ErrorTypeEnum.SMS_CODE_VERIFY_ERROR_TOO_MANY);
            }
        }

    }

    private boolean verifySmsCodeValidate(VerifySmsCodeParam verifySmsCodeParam) {
        String tel = verifySmsCodeParam.tel();
        CodeResponseDTO codeResponseDTO = codeClient.verifyCode(CodeVerifyParam.builder().receiver(verifySmsCodeParam.tel())
                                                                        .channel(ChannelEnum.CHANNEL_SMS)
                                                                        .code(verifySmsCodeParam.code()).build());
        String key = SMS_CODE_KEY + verifySmsCodeParam.verifySmsCodeType().getValue() + tel;
        if (CodeVerifyResultEnum.SUCCESS.equals(codeResponseDTO.getCode())) {
            return redisTemplate.delete(key);
        }
        String smcCacheStr = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(smcCacheStr)) {
            throw new OuaaException(ErrorTypeEnum.SMS_CODE_EXPIRE);
        }
        SmsCacheDTO smsCacheDTO = JsonUtils.deserialize(smcCacheStr, SmsCacheDTO.class);
        if (Objects.isNull(smsCacheDTO)) {
            throw new OuaaException(ErrorTypeEnum.SMS_CODE_EXPIRE);
        }
        Long errorCount = smsCacheDTO.getErrorCount();
        if (errorCount == null || errorCount == 0L) {
            smsCacheDTO.setErrorCount(1L);
        } else {
            errorCount += 1;
            smsCacheDTO.setErrorCount(errorCount);
        }
        SmsLimitDTO smsLimitDTO = getSmsLimitDTO();
        List<SmsLimitDTO.ErrorCountTimeLimitDTO> errorCountTimeLimits = smsLimitDTO.getErrorCountTimeLimits();
        //按错误次数，从大到小排序
        errorCountTimeLimits.sort(Comparator.comparingInt(SmsLimitDTO.ErrorCountTimeLimitDTO::getErrorCount).reversed());
        boolean isLimit = false;
        int count = 0;
        for (SmsLimitDTO.ErrorCountTimeLimitDTO errorCountTimeLimitDTO : errorCountTimeLimits) {
            if (smsCacheDTO.getErrorCount() >= errorCountTimeLimitDTO.getErrorCount()) {
                if (count == 0 && smsCacheDTO.getErrorCount() > errorCountTimeLimitDTO.getErrorCount()) {
                    //如果超出了最大的错误次数，禁用用户
                    User user = userService.lambdaQuery().eq(User::getMobile, tel).one();
                    if (Objects.nonNull(user)) {
                        user.setEnabled(false);
                        userService.updateById(user);
                        throw new OuaaException(ErrorTypeEnum.USER_NOT_ACTIVE);
                    }
                }
                if (count != 0) {
                    //判断是否需要遵循当前错误次数限制
                    Long lastErrorLimitCount = smsCacheDTO.getLastErrorLimitCount();
                    if (Objects.nonNull(lastErrorLimitCount) && lastErrorLimitCount >= errorCountTimeLimitDTO.getErrorCount()) {
                        continue;
                    }
                }
                isLimit = true;
                LocalDateTime now = LocalDateTime.now();
                long curMillis = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                smsCacheDTO.setEnableVerifyTime(curMillis + errorCountTimeLimitDTO.getMinutes() * 60 * 1000);
                smsCacheDTO.setLastErrorLimitCount(smsCacheDTO.getErrorCount());
                Long expire = redisTemplate.getExpire(SMS_CODE_KEY + verifySmsCodeParam.verifySmsCodeType().getValue() +
                                                      verifySmsCodeParam.tel(), TimeUnit.SECONDS);
                String serialize = JsonUtils.serialize(smsCacheDTO);
                redisTemplate.opsForValue().set(key, serialize == null ? "" : serialize, expire, TimeUnit.SECONDS);
                break;
            }
            count++;
        }
        if (!isLimit) {
            Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            String serialize = JsonUtils.serialize(smsCacheDTO);
            redisTemplate.opsForValue().set(key, serialize == null ? "" : serialize, expire, TimeUnit.SECONDS);
        }

        throw new OuaaException(codeResponseDTO.getMessage(), String.valueOf(codeResponseDTO.getCode()), ErrorTypeEnum.SMS_CODE_ERROR.getErrType());

    }

    private boolean verifyBlockPuzzle(SendSmsCodeParam sendSmsCodeParam) {
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(sendSmsCodeParam.captchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        return Objects.nonNull(response) && response.isSuccess() && response.getRepCode().equals("0000");
    }

    private void sendRecordUpdate(SendSmsCodeParam sendSmsCodeParam) {
        SmsCacheDTO smsCacheDTO = getSmsCacheDTO(sendSmsCodeParam);
        String key = SMS_CODE_KEY + sendSmsCodeParam.sendSmsCodeType().getValue() + sendSmsCodeParam.tel();
        //记录当前手机号的验证信息
        if (Objects.isNull(smsCacheDTO)) {
            smsCacheDTO = SmsCacheDTO.builder()
                    .tel(sendSmsCodeParam.tel())
                    .firstSendTime(DateUtils.currentTimestamp())
                    .lastSendTime(DateUtils.currentTimestamp())
                    .sendCount(1L)
                    .build();
            String serialize = JsonUtils.serialize(smsCacheDTO);
            if (StringUtils.isNotBlank(serialize)) {
                redisTemplate.opsForValue().set(key, serialize, CACHE_TTL_HOUR, TimeUnit.HOURS);
            }
        } else {
            SmsLimitDTO smsLimitDTO = getSmsLimitDTO();
            smsCacheDTO.setLastSendTime(DateUtils.currentTimestamp());
            smsCacheDTO.setSendCount(smsCacheDTO.getSendCount() + 1);
            if (smsCacheDTO.getSendCount() % smsLimitDTO.getMaxSendCount() == 0) {
                smsCacheDTO.setEnableSendTime(DateUtils.currentTimestamp() + smsLimitDTO.getMaxSendCountMinutesScope() * 60 * 1000);
            }
            String serialize = JsonUtils.serialize(smsCacheDTO);
            if (StringUtils.isNotBlank(serialize)) {
                Long expire = redisTemplate.getExpire(key);
                redisTemplate.opsForValue().set(key, serialize, expire, TimeUnit.SECONDS);
            }
        }
    }

    private SmsCacheDTO getSmsCacheDTO(SendSmsCodeParam sendSmsCodeParam) {
        Assert.isParameterError(StringUtils.isBlank(sendSmsCodeParam.tel()), "手机号不能为空！");
        String cacheDTOStr = redisTemplate.opsForValue()
                .get(SMS_CODE_KEY + sendSmsCodeParam.sendSmsCodeType().getValue() + sendSmsCodeParam.tel());
        if (StringUtils.isBlank(cacheDTOStr)) {
            return null;
        }
        return JsonUtils.deserialize(cacheDTOStr, SmsCacheDTO.class);
    }

    private void sendVerifyCode(SendSmsCodeParam sendSmsCodeParam) {

        SendVerifyCodeParam sendVerifyCodeParam = SendVerifyCodeParam.builder()
                .channel(ChannelEnum.CHANNEL_SMS)
                .receivers(new HashSet<>(Collections.singleton(sendSmsCodeParam.tel()))).build();
        //发送验证码
        CodeResponseDTO codeResponseDTO = codeClient.sendCode(sendVerifyCodeParam);
        if (!CodeVerifyResultEnum.SUCCESS.equals(codeResponseDTO.getCode())) {
            throw new ServiceException("发送验证码失败");
        }
    }
}
