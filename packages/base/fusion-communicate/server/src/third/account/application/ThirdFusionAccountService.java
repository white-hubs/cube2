package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway.query.ThirdFusionAccountQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionAccountAllocationPO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionAccountPO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.infrastructure.repository.mapper.ThirdFusionAccountMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdFusionAccountService extends ServiceImpl<ThirdFusionAccountMapper, ThirdFusionAccountPO> {

    private final ThirdFusionAccountAllocationService thirdFusionAccountAllocationService;

    public Page<ThirdFusionAccountPO> queryThirdFusionAccountPage(@Valid ThirdFusionAccountQueryParam query) {
        return this.baseMapper.selectPage(PagingUtils.convert(query), Wrappers.<ThirdFusionAccountPO>lambdaQuery()
                                                  .eq(ThirdFusionAccountPO::getProviderCode, query.getProviderCode())
                                                  .and(StringUtils.isNotBlank(query.getKeyword()), q -> q
                                                          .like(ThirdFusionAccountPO::getName, query.getKeyword())
                                                          .or()
                                                          .like(ThirdFusionAccountPO::getNumber, query.getKeyword()))
                                                  .orderByDesc(ThirdFusionAccountPO::getCreateTime)
                                         );
    }

    public List<ThirdFusionAccountPO> queryThirdFusionAccountByState(String providerCode, String usageState, Integer accountNumbers) {
        Page<ThirdFusionAccountPO> page = new Page<>(1, accountNumbers);
        return this.baseMapper.selectPage(page, Wrappers.<ThirdFusionAccountPO>lambdaQuery()
                                                  .eq(ThirdFusionAccountPO::getProviderCode, providerCode)
                                                  .eq(ThirdFusionAccountPO::getUsageState, usageState)
                                         ).getRecords();
    }

    public List<ThirdFusionAccountPO> queryThirdFusionAccounts(String providerCode, Set<String> thirdFusionAccounts) {
        return this.baseMapper.selectList(Wrappers.<ThirdFusionAccountPO>lambdaQuery()
                                                  .eq(ThirdFusionAccountPO::getProviderCode, providerCode)
                                                  .in(ThirdFusionAccountPO::getNumber, thirdFusionAccounts));
    }

    public List<ThirdFusionAccountPO> queryThirdFusionAccountsByUserIds(String providerCode, Set<Long> sysUserIds) {
        List<ThirdFusionAccountAllocationPO> fusionAccountAllocations = this.thirdFusionAccountAllocationService
                .getThirdFusionAccountAllocations(providerCode, sysUserIds);
        if (CollectionUtils.isEmpty(fusionAccountAllocations)) {
            return Collections.emptyList();
        }
        Set<String> accounts = fusionAccountAllocations.stream().map(ThirdFusionAccountAllocationPO::getThirdAccountNumber)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(accounts)) {
            return Collections.emptyList();
        }
        return this.baseMapper.selectList(Wrappers.<ThirdFusionAccountPO>lambdaQuery()
                                                  .eq(ThirdFusionAccountPO::getProviderCode, providerCode)
                                                  .in(ThirdFusionAccountPO::getNumber, accounts));
    }

    public void updateAccountState(String usageState, String providerCode, List<ThirdFusionAccountPO> thirdFusionAccounts) {
        this.updateAccountState(usageState, providerCode, thirdFusionAccounts.stream().map(ThirdFusionAccountPO::getNumber)
                .collect(Collectors.toSet()));
    }

    public void updateAccountState(String usageState, String providerCode, Set<String> thirdFusionAccounts) {
        LambdaUpdateWrapper<ThirdFusionAccountPO> accountPOLambdaUpdateWrapper = Wrappers.<ThirdFusionAccountPO>lambdaUpdate()
                .eq(ThirdFusionAccountPO::getProviderCode, providerCode)
                .in(ThirdFusionAccountPO::getNumber, thirdFusionAccounts)
                .set(ThirdFusionAccountPO::getUsageState, usageState);
        this.update(accountPOLambdaUpdateWrapper);
    }
}
