package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.aggregates.ThirdFusionAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.entity.AccountInfo;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.valueobject.AccountLevel;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.service.ThirdFusionAccountFacadeService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.AbstractCommonFusionCommunicateAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.XunYiApiService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response.XunYiAllDeviceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.constants.XunYiAccountDocConstant.DISPATCH_TERMINAL_TYPE;

@Slf4j
@Component
public class XunYiFusionCommunicateAccountProvider extends AbstractCommonFusionCommunicateAccount {

    private final XunYiApiService xunYiApiService;

    private final ThirdFusionAccountFacadeService thirdFusionAccountService;

    @Autowired
    public XunYiFusionCommunicateAccountProvider(ThirdFusionAccountFacadeService thirdFusionAccountService,
                                                 XunYiApiService xunYiApiService
                                                ) {
        super(thirdFusionAccountService);
        this.xunYiApiService = xunYiApiService;
        this.thirdFusionAccountService = thirdFusionAccountService;
    }

    @Override
    public List<ThirdFusionAccount> getManufacturerAccounts() {
        List<XunYiAllDeviceResponse> devices = this.xunYiApiService.getAllDevices();
        //只获取调度台、智能终端账号
        return devices.stream().filter(device -> DISPATCH_TERMINAL_TYPE.equals(device.getType())).map(device -> {
            AccountInfo accountInfo = new AccountInfo(IdWorker.getId(), device.getName(), device.getEmployeeId(), device
                    .getEmployeeId(), device.getPassword());
            AccountLevel accountLevel = new AccountLevel(String.valueOf(device.getEmployeeLevel()));
            return new ThirdFusionAccount(accountInfo, accountLevel, null, getProviderCode());
        }).collect(Collectors.toList());
    }

    public void disableFusionAccount(Integer timeoutKeepAliveMinutes) {
        this.thirdFusionAccountService.disableFusionAccount(getProviderCode(), timeoutKeepAliveMinutes);
    }

    @Override
    public String getProviderCode() {
        return "xunYi";
    }

}
