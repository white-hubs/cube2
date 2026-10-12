package com.guhecloud.cube.base.message2.server.sat.param.validator.manufacturer;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.TencentSmsAccountConfig;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TencentSmsAccountValidator implements AccountValidator {

    @Override
    public String getChannelName() {
        return ChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public String getManufacturerName() {
        return ManufacturerCodeEnum.TENCENT_YUN.name();
    }

    @Override
    public boolean accountIdentityVerify(String firstManufacturerConfig, String secondManufacturerConfig) {
        TencentSmsAccountConfig firstTencentSmsAccountConfig = JsonUtils.deserialize(firstManufacturerConfig, TencentSmsAccountConfig.class);
        TencentSmsAccountConfig secondTencentSmsAccountConfig = JsonUtils.deserialize(secondManufacturerConfig, TencentSmsAccountConfig.class);
        return firstTencentSmsAccountConfig.equals(secondTencentSmsAccountConfig);
    }
}
