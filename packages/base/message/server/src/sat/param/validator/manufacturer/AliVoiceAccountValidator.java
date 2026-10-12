package com.guhecloud.cube.base.message2.server.sat.param.validator.manufacturer;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.voice.AliYunVoiceAccountConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AliVoiceAccountValidator implements AccountValidator {

    @Override
    public String getChannelName() {
        return ChannelEnum.CHANNEL_VOICE.name();
    }

    @Override
    public String getManufacturerName() {
        return ManufacturerCodeEnum.ALI_YUN.name();
    }

    @Override
    public boolean accountIdentityVerify(String firstManufacturerConfig, String secondManufacturerConfig) {
        AliYunVoiceAccountConfig firstJgConfig = AliYunVoiceAccountConfig.getAliConfig(firstManufacturerConfig);
        AliYunVoiceAccountConfig secondJgConfig = AliYunVoiceAccountConfig.getAliConfig(secondManufacturerConfig);
        return firstJgConfig.equals(secondJgConfig);
    }
}
