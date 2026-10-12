package com.guhecloud.cube.biz.fusion.communicate.server.third.tencent;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.aggregates.ThirdFusionAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.service.tencent.TencentThirdFusionAccountFacadeService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountAllocationVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.AbstractCommonFusionCommunicateAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TencentCommunicationAccountProvider extends AbstractCommonFusionCommunicateAccount {

    private final TencentThirdFusionAccountFacadeService tencentThirdFusionAccountFacadeService;

    @Autowired
    public TencentCommunicationAccountProvider(TencentThirdFusionAccountFacadeService tencentThirdFusionAccountFacadeService
                                              ) {
        super(tencentThirdFusionAccountFacadeService);
        this.tencentThirdFusionAccountFacadeService = tencentThirdFusionAccountFacadeService;
    }

    @Override
    public List<ThirdFusionAccount> getManufacturerAccounts() {
        return Collections.emptyList();
    }

    @Override
    public List<AccountAllocationUserVO> getOnlineUserIds(Set<Long> userIds) {
        List<ThirdFusionAccountAllocationVO> onlineUser = tencentThirdFusionAccountFacadeService.getOnlineUser(getProviderCode(), userIds);
        return onlineUser.stream().map(thirdFusionAccount -> {
            AccountVO accountVO = ObjectUtils.copying(thirdFusionAccount.getThirdFusionAccount(), AccountVO.class);
            return new AccountAllocationUserVO(accountVO, thirdFusionAccount.getUserId());
        }).collect(Collectors.toList());
    }

    @Override
    public List<AccountAllocationUserVO> assignFusionCommunicateAccount(Set<Long> userIds) {
        List<ThirdFusionAccountAllocationVO> thirdFusionAccountAllocationVOS = tencentThirdFusionAccountFacadeService.assignFusionAccounts(getProviderCode(), userIds);
        return thirdFusionAccountAllocationVOS.stream().map(thirdFusionAccount -> {
            AccountVO accountVO = ObjectUtils.copying(thirdFusionAccount.getThirdFusionAccount(), AccountVO.class);
            return new AccountAllocationUserVO(accountVO, thirdFusionAccount.getUserId());
        }).collect(Collectors.toList());
    }

    public void disableFusionAccount(Integer timeoutKeepAliveMinutes) {
        this.tencentThirdFusionAccountFacadeService.disableFusionAccount(getProviderCode(), timeoutKeepAliveMinutes);
    }

    @Override
    public String getProviderCode() {
        return "tencent";
    }
}
