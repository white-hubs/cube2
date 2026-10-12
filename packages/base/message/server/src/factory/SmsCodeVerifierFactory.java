package com.guhecloud.cube.base.message2.server.factory;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.dictionary.adapter.SysDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerHolder;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.verifier.CodeVerify;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class SmsCodeVerifierFactory extends ThirdCodeVerifierFactory {
    private static final String DICT_ITEM_CODE = "SMS_CONFIG";
    private static final String DICT_SMS_MANUFACTURER_CODE = "sms_manufacturer_config";
    private final SysDictionaryAdapter dictionaryAdapter;
    private final ManufacturerHolder manufacturerHolder;

    public CodeVerify getVerifier() {
        try {
            //根据配置选择厂家
            SysDictionaryDTO manufacturerConfig = dictionaryAdapter.getSysDictionaryByCodeAndItemCode(DICT_ITEM_CODE, DICT_SMS_MANUFACTURER_CODE);
            Assert.isParameterError(Objects.isNull(manufacturerConfig), "没有配置厂家信息，无法发送！");
            Assert.isParameterError(StringUtils.isEmpty(manufacturerConfig.getVariable()), "没有配置厂家信息，无法发送！");
            ManufacturerCodeEnum manufacturerCodeEnum = ManufacturerCodeEnum.valueOf(manufacturerConfig.getVariable());
            ManufacturerSender manufacturerSender = manufacturerHolder.getHandler(
                    ChannelEnum.CHANNEL_SMS.name() + "-" + manufacturerCodeEnum.name());
            return (CodeVerify) manufacturerSender;
        } catch (Exception e) {
            log.error("获取短信发送厂家失败", e);
            throw new UnsupportedOperationException("不支持的短信验证码厂家");
        }

    }

    @Override
    protected ChannelEnum getChannel() {
        return ChannelEnum.CHANNEL_SMS;
    }
}
