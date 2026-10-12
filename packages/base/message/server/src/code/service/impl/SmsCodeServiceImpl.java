package com.guhecloud.cube.base.message2.server.code.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.base.message.metadata.constant.TemplateParamKeyConstants;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.dictionary.adapter.SysDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.client.param.CodeVerifyParam;
import com.guhecloud.cube.base.message2.client.param.SendVerifyCodeParam;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.factory.ThirdCodeVerifierFactoryHolder;
import com.guhecloud.cube.base.message2.server.handler.dto.CodeVerifyDTO;
import com.guhecloud.cube.base.message2.server.sat.enumeration.MsgTemplateTypeEnum;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsCodeServiceImpl extends ChannelCodeServiceImpl {
    private static final String MSG_SEND_TOPIC = "MSG_SEND_TASK";
    private final static String DICT_ITEM_CODE = "SMS_CONFIG";
    private final static String SYS_DICT_ABILITY_ITEM_CODE = "SYS_ABILITY_CONFIG";
    private final static String SMS_DICT_ABILITY_ENABLE_CODE = "sms_code_send";
    private final static String DICT_SMS_ACCOUNT_CODE = "sms_account_config";
    private final static String DICT_SMS_TEMPLATE_CODE = "sms_template_config";
    private static final String DICT_SMS_MANUFACTURER_CODE = "sms_manufacturer_config";
    private final SysDictionaryAdapter dictionaryAdapter;
    private final ThirdCodeVerifierFactoryHolder thirdCodeVerifierFactoryHolder;

    @Override
    public CodeResponseDTO sendCode(SendVerifyCodeParam param) {
        //获取验证码能力是否已经开启
        checkSmsAbility();
        Set<String> receivers = param.getReceivers();
        //获取账户信息配置
        SysDictionaryDTO accountConfig = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DICT_ITEM_CODE, DICT_SMS_ACCOUNT_CODE);
        Assert.isParameterError(Objects.isNull(accountConfig), "没有配置账号信息，无法发送！");
        Assert.isParameterError(StringUtils.isEmpty(accountConfig.getVariable()), "没有配置账号信息，无法发送！");
        //获取模板配置
        SysDictionaryDTO templateConfig = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DICT_ITEM_CODE, DICT_SMS_TEMPLATE_CODE);
        Assert.isParameterError(Objects.isNull(templateConfig), "没有配置模板信息，无法发送！");
        Assert.isParameterError(StringUtils.isEmpty(templateConfig.getVariable()), "没有配置模板信息，无法发送！");
        //获取厂家配置
        SysDictionaryDTO manufacturerConfig = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DICT_ITEM_CODE, DICT_SMS_MANUFACTURER_CODE);
        Assert.isParameterError(Objects.isNull(manufacturerConfig), "没有配置厂家信息，无法发送！");
        Assert.isParameterError(StringUtils.isEmpty(manufacturerConfig.getVariable()), "没有配置厂家信息，无法发送！");
        MessageInfo messageInfo = MessageInfo.builder()
                .id(IdWorker.getId())
                .receiver(new HashSet<>(receivers))
                .channelCode(ChannelEnum.CHANNEL_SMS)
                .manufacturerCode(ManufacturerCodeEnum.valueOf(manufacturerConfig.getVariable()))
                .tempType(MsgTemplateTypeEnum.VERIFY_CODE)
                .tmpConfig(templateConfig.getVariable())
                .sendAccountInfo(accountConfig.getVariable())
                .templateContentParams(generateCode())
                .timestamp(System.currentTimeMillis())
                .build();
        RocketMQContextHolder.send(MSG_SEND_TOPIC, messageInfo);
        return CodeResponseDTO.success();
    }

    private Map<String, String> generateCode() {
        Map<String, String> codeMap = new HashMap<>();
        Random random = new Random();

        codeMap.put(TemplateParamKeyConstants.CODE, String.valueOf(random.nextInt(100000, 1000000)));
        return codeMap;
    }

    @Override
    public CodeResponseDTO verifyCode(CodeVerifyParam param) {
        checkSmsAbility();
        SysDictionaryDTO accountConfig = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DICT_ITEM_CODE, DICT_SMS_ACCOUNT_CODE);
        Assert.isParameterError(Objects.isNull(accountConfig), "没有配置账号信息，无法发送！");
        Assert.isParameterError(StringUtils.isEmpty(accountConfig.getVariable()), "没有配置账号信息，无法发送！");
        return thirdCodeVerifierFactoryHolder.getFactory(ChannelEnum.CHANNEL_SMS)
                .getVerifier().verifyCode(new CodeVerifyDTO(param.getCode(), param.getReceiver(), accountConfig.getVariable()));
    }

    @Override
    public ChannelEnum getChannel() {
        return ChannelEnum.CHANNEL_SMS;
    }

    private void checkSmsAbility() {
        SysDictionaryDTO sysDictionaryDTO = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(SYS_DICT_ABILITY_ITEM_CODE, SMS_DICT_ABILITY_ENABLE_CODE);
        //未配置，默认开启
        if (Objects.isNull(sysDictionaryDTO) || StringUtils.isBlank(sysDictionaryDTO.getVariable())) {
            return;
        }
        String variable = sysDictionaryDTO.getVariable();
        try {
            boolean enabled = Boolean.parseBoolean(variable);
            Assert.isNotAcceptable(!enabled, "短信验证码功能未开启！");
        } catch (Exception e) {
            log.error("解析字典变量失败", e);
        }

    }
}
