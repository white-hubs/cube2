package com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.service;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.ThirdFusionAccountAllocationService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.ThirdFusionAccountAllocationCommandService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.ThirdFusionAccountCommandService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command.AssignFusionAccountAllocationCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command.DisableFusionAccountAllocationCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command.KeepAliveThirdFusionAccountCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway.ThirdFusionAccountQueryService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway.query.ThirdFusionAccountQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.aggregates.ThirdFusionAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.query.ThirdFusionAccountFacadeQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountAllocationVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.service.IThirdFusionAccountService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdFusionAccountFacadeService implements IThirdFusionAccountService {

    private final ThirdFusionAccountQueryService thirdFusionAccountQueryService;

    private final ThirdFusionAccountAllocationService thirdFusionAccountAllocationService;

    private final ThirdFusionAccountAllocationCommandService thirdFusionAccountAllocationCommandService;

    private final ThirdFusionAccountCommandService thirdFusionAccountCommandService;

    @Override
    public void batchSaveThirdFusionAccount(List<ThirdFusionAccount> thirdFusionAccounts) {
        this.thirdFusionAccountCommandService.batchIncreaseThirdFusionAccount(thirdFusionAccounts);
    }

    @Override
    public Pagination<ThirdFusionAccountVO> getThirdFusionAccountPage(@Valid ThirdFusionAccountFacadeQueryParam query) {
        ThirdFusionAccountQueryParam thirdFusionAccountQueryParam = ObjectUtils
                .copying(query, ThirdFusionAccountQueryParam.class);
        return this.thirdFusionAccountQueryService.getThirdFusionAccountPage(thirdFusionAccountQueryParam);
    }

    @Override
    public List<ThirdFusionAccountAllocationVO> getOnlineUser(String providerCode, Set<Long> sysUserIds) {
        return this.thirdFusionAccountQueryService.getOnlineUser(providerCode, sysUserIds);
    }

    @Override
    public Long getSysUserByFusionCommunicateAccount(String providerCode, String thirdAccountNumber) {
        return this.thirdFusionAccountAllocationService
                .getSysUserByFusionCommunicateAccount(providerCode, thirdAccountNumber);
    }

    @Override
    public List<ThirdFusionAccountAllocationVO> assignFusionAccounts(String providerCode, Set<Long> userIds) {
        AssignFusionAccountAllocationCommand command = new AssignFusionAccountAllocationCommand();
        command.setProviderCode(providerCode);
        command.setUserIds(userIds);
        return this.thirdFusionAccountAllocationCommandService.assignFusionCommunicateAccount(command);
    }

    @Override
    public void disableFusionAccount(String providerCode, Set<Long> userIds) {
        DisableFusionAccountAllocationCommand command = new DisableFusionAccountAllocationCommand();
        command.setProviderCode(providerCode);
        command.setUserIds(userIds);
        this.thirdFusionAccountAllocationCommandService.disableFusionAccount(command);
    }

    @Override
    public void disableFusionAccount(String providerCode, Integer timeoutKeepAliveMinutes) {
        DisableFusionAccountAllocationCommand command = new DisableFusionAccountAllocationCommand();
        command.setProviderCode(providerCode);
        command.setTimeoutKeepAliveMinutes(timeoutKeepAliveMinutes);
        this.thirdFusionAccountAllocationCommandService.disableKeepAliveTimeoutFusionAccount(command);
    }

    @Override
    public void keepAliveFusionAccount(String providerCode, Set<Long> userIds) {
        KeepAliveThirdFusionAccountCommand command = new KeepAliveThirdFusionAccountCommand();
        command.setProviderCode(providerCode);
        command.setUserIds(userIds);
        this.thirdFusionAccountAllocationCommandService.keepAliveFusionAccount(command);
    }
}
