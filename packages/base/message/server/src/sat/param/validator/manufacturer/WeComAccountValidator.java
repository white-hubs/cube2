package com.guhecloud.cube.base.message2.server.sat.param.validator.manufacturer;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.wecom.WeComAccountConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WeComAccountValidator implements AccountValidator {

    @Override
    public String getChannelName() {
        return ChannelEnum.CHANNEL_WE_COM.name();
    }

    @Override
    public String getManufacturerName() {
        return ManufacturerCodeEnum.WE_COM.name();
    }

    @Override
    public boolean accountIdentityVerify(String firstManufacturerConfig, String secondManufacturerConfig) {
        WeComAccountConfig firstConfig = WeComAccountConfig.getWeComConfig(firstManufacturerConfig);
        WeComAccountConfig secondConfig = WeComAccountConfig.getWeComConfig(secondManufacturerConfig);
        return firstConfig.equals(secondConfig);
    }
}
