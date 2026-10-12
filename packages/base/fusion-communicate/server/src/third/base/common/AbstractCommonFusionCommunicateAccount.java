package com.guhecloud.cube.biz.fusion.communicate.server.third.base.common;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.aggregates.ThirdFusionAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.query.ThirdFusionAccountFacadeQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountAllocationVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.IFusionCommunicateAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.service.IThirdFusionAccountService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.param.AccountQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.FusionCommunicateAccountVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractCommonFusionCommunicateAccount implements IFusionCommunicateAccount, CommonAccountSync {

    private final IThirdFusionAccountService thirdFusionAccountService;

    protected AbstractCommonFusionCommunicateAccount(IThirdFusionAccountService thirdFusionAccountService
                                                    ) {
        this.thirdFusionAccountService = thirdFusionAccountService;
    }

    @Override
    public Pagination<FusionCommunicateAccountVO> getThirdFusionAccountPage(@Valid AccountQueryParam query) {
        ThirdFusionAccountFacadeQueryParam thirdFusionAccountQueryParam = new ThirdFusionAccountFacadeQueryParam();
        thirdFusionAccountQueryParam.setProviderCode(getProviderCode());
        thirdFusionAccountQueryParam.setKeyword(query.getKeyword());
        thirdFusionAccountQueryParam.setPageNumber(query.getPageNumber());
        thirdFusionAccountQueryParam.setPageSize(query.getPageSize());
        Pagination<ThirdFusionAccountVO> thirdFusionAccountPage = this.thirdFusionAccountService
                .getThirdFusionAccountPage(thirdFusionAccountQueryParam);
        return PagingUtils.convert(thirdFusionAccountPage, ObjectUtils
                .copying(thirdFusionAccountPage.getRecords(), FusionCommunicateAccountVO.class));
    }

    @Override
    public Long getSysUserByFusionCommunicateAccount(String fusionCommunicateAccount) {
        return this.thirdFusionAccountService
                .getSysUserByFusionCommunicateAccount(getProviderCode(), fusionCommunicateAccount);
    }

    @Override
    public List<AccountAllocationUserVO> getOnlineUserIds(Set<Long> userIds) {
        List<ThirdFusionAccountAllocationVO> thirdFusionAccountAllocationVOS = this.thirdFusionAccountService
                .getOnlineUser(getProviderCode(), userIds);
        return thirdFusionAccountAllocationVOS.stream().map(thirdFusionAccount -> {
            AccountVO accountVO = ObjectUtils.copying(thirdFusionAccount.getThirdFusionAccount(), AccountVO.class);
            return new AccountAllocationUserVO(accountVO, thirdFusionAccount.getUserId());
        }).collect(Collectors.toList());
    }

    @Override
    public List<AccountAllocationUserVO> assignFusionCommunicateAccount(Set<Long> userIds) {
        List<ThirdFusionAccountAllocationVO> thirdFusionAccountAllocationVOS = thirdFusionAccountService
                .assignFusionAccounts(getProviderCode(), userIds);
        return thirdFusionAccountAllocationVOS.stream().map(thirdFusionAccount -> {
            AccountVO accountVO = ObjectUtils.copying(thirdFusionAccount.getThirdFusionAccount(), AccountVO.class);
            return new AccountAllocationUserVO(accountVO, thirdFusionAccount.getUserId());
        }).collect(Collectors.toList());
    }

    @Override
    public void disableFusionAccount(Set<Long> userIds) {
        this.thirdFusionAccountService.disableFusionAccount(getProviderCode(), userIds);
    }

    @Override
    public void keepAliveFusionAccount(Set<Long> userIds) {
        this.thirdFusionAccountService.keepAliveFusionAccount(getProviderCode(), userIds);
    }

    /**
     * 同步第三方通讯账号
     */
    public List<ThirdFusionAccount> syncThirdFusionCommunicateAccounts() {
        List<ThirdFusionAccount> manufacturerAccounts = this.getManufacturerAccounts();
        //存储
        this.thirdFusionAccountService.batchSaveThirdFusionAccount(manufacturerAccounts);
        return manufacturerAccounts;
    }

    /**
     * 获取厂家的第三方通讯账号
     *
     * @return
     */
    public abstract List<ThirdFusionAccount> getManufacturerAccounts();

}
