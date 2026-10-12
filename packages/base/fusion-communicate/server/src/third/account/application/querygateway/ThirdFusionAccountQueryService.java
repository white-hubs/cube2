package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.ThirdFusionAccountAllocationService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.ThirdFusionAccountService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway.query.ThirdFusionAccountQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionAccountAllocationPO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionAccountPO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountAllocationVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdFusionAccountQueryService {

    private final ThirdFusionAccountService thirdFusionAccountService;

    private final ThirdFusionAccountAllocationService thirdFusionAccountAllocationService;

    public Pagination<ThirdFusionAccountVO> getThirdFusionAccountPage(@Valid ThirdFusionAccountQueryParam query) {
        Page<ThirdFusionAccountPO> thirdFusionAccountPOPage = this.thirdFusionAccountService.queryThirdFusionAccountPage(query);
        return PagingUtils.convert(thirdFusionAccountPOPage, ThirdFusionAccountVO.class);
    }

    public List<ThirdFusionAccountAllocationVO> getOnlineUser(String providerCode, Set<Long> sysUserIds) {
        List<ThirdFusionAccountAllocationPO> thirdFusionAccountAllocationPOS = thirdFusionAccountAllocationService
                .getThirdFusionAccountAllocations(providerCode, sysUserIds);
        if (CollectionUtils.isEmpty(thirdFusionAccountAllocationPOS)) {
            return Collections.emptyList();
        }
        Set<String> thirdAccounts = thirdFusionAccountAllocationPOS.stream().map(ThirdFusionAccountAllocationPO::getThirdAccountNumber)
                .collect(Collectors.toSet());
        Map<String, ThirdFusionAccountPO> thirdFusionAccountPOMap = this.thirdFusionAccountService
                .queryThirdFusionAccounts(providerCode, thirdAccounts)
                .stream().collect(Collectors.toMap(ThirdFusionAccountPO::getNumber, s -> s));
        return thirdFusionAccountAllocationPOS.stream().map(fusionAccountAllocation -> {
            ThirdFusionAccountAllocationVO thirdFusionAccountAllocationVO = new ThirdFusionAccountAllocationVO();
            thirdFusionAccountAllocationVO.setUserId(fusionAccountAllocation.getUserId());
            if (thirdFusionAccountPOMap.containsKey(fusionAccountAllocation.getThirdAccountNumber())) {
                ThirdFusionAccountVO thirdFusionAccount = ObjectUtils
                        .copying(thirdFusionAccountPOMap.get(fusionAccountAllocation.getThirdAccountNumber()), ThirdFusionAccountVO.class);
                thirdFusionAccountAllocationVO.setThirdFusionAccount(thirdFusionAccount);
            }
            return thirdFusionAccountAllocationVO;
        }).collect(Collectors.toList());
    }
}
