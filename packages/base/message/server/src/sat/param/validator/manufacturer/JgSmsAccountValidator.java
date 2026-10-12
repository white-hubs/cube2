package com.guhecloud.cube.base.message2.server.sat.param.validator.manufacturer;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.JgSmsAccountConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JgSmsAccountValidator implements AccountValidator {

    @Override
    public String getChannelName() {
        return ChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public String getManufacturerName() {
        return ManufacturerCodeEnum.JI_GUANG.name();
    }

    @Override
    public boolean accountIdentityVerify(String firstManufacturerConfig, String secondManufacturerConfig) {
        JgSmsAccountConfig firstJgConfig = JgSmsAccountConfig.getJgConfig(firstManufacturerConfig);
        JgSmsAccountConfig secondJgConfig = JgSmsAccountConfig.getJgConfig(secondManufacturerConfig);
        return firstJgConfig.equals(secondJgConfig);
    }
}
