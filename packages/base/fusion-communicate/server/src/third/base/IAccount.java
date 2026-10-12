package com.guhecloud.cube.biz.fusion.communicate.server.third.base;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.param.AccountQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.FusionCommunicateAccountVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Set;

public interface IAccount {

    Pagination<FusionCommunicateAccountVO> getThirdFusionAccountPage(AccountQueryParam query);

    Long getSysUserByFusionCommunicateAccount(String fusionCommunicateAccount);

    List<AccountAllocationUserVO> getOnlineUserIds(Set<Long> userIds);

    List<AccountAllocationUserVO> assignFusionCommunicateAccount(Set<Long> userIds);

    void disableFusionAccount(Set<Long> userIds);

    void keepAliveFusionAccount(Set<Long> userIds);

}
