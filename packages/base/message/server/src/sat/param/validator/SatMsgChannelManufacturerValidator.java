package com.guhecloud.cube.base.message2.server.sat.param.validator;

import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgManufacturer;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgChannelModifyParam;
import com.guhecloud.cube.base.message2.server.sat.param.validator.manufacturer.AccountValidator;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatMsgChannelManufacturerValidator implements ApplicationRunner {

    private final Map<String, AccountValidator> handlerMap = new HashMap<>();

    public boolean verifyChannelManufacturerUpdate(SatMsgManufacturer satMsgManufacturer,
                                                   SatMsgChannelModifyParam param
                                                  ) {
        //不是同一个厂家,证明需要重写初始化模板信息
        if (satMsgManufacturer.getManufacturerCode() != param.getManufacturerCode()) {
            return true;
        }
        //同一个厂家,校验配置参数是否完全一致,若完全一致则不需要重新初始化模板信息
        AccountValidator accountValidator = this.handlerMap
                .get(satMsgManufacturer.getChannelCode() + "-" + satMsgManufacturer.getManufacturerCode());
        if (Objects.isNull(accountValidator)) {
            return false;
        }
        return !accountValidator.accountIdentityVerify(param.getConfig(), satMsgManufacturer.getConfig());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //通道发送器
        ApplicationContextHolder.getBeansOfType(AccountValidator.class)
                .forEach((k, v) ->
                                 this.handlerMap.put(v.getChannelName() + "-" + v.getManufacturerName(), v)
                        );
    }
}
