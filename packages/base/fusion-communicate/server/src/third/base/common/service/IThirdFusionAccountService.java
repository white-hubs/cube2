package com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.service;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.aggregates.ThirdFusionAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.query.ThirdFusionAccountFacadeQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountAllocationVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Set;

public interface IThirdFusionAccountService {

    void batchSaveThirdFusionAccount(List<ThirdFusionAccount> thirdFusionAccounts);

    Pagination<ThirdFusionAccountVO> getThirdFusionAccountPage(ThirdFusionAccountFacadeQueryParam query);

    List<ThirdFusionAccountAllocationVO> getOnlineUser(String providerCode, Set<Long> sysUserIds);

    Long getSysUserByFusionCommunicateAccount(String providerCode, String thirdAccountNumber);

    List<ThirdFusionAccountAllocationVO> assignFusionAccounts(String providerCode, Set<Long> userIds);

    void disableFusionAccount(String providerCode, Set<Long> userIds);

    void disableFusionAccount(String providerCode, Integer timeoutKeepAliveMinutes);

    void keepAliveFusionAccount(String providerCode, Set<Long> userIds);
}
