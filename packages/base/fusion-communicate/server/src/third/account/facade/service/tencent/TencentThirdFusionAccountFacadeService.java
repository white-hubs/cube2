package com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.service.tencent;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.ThirdFusionAccountAllocationService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.aggregates.ThirdFusionAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionAccountAllocationPO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.query.ThirdFusionAccountFacadeQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountAllocationVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.service.IThirdFusionAccountService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TencentThirdFusionAccountFacadeService implements IThirdFusionAccountService {

    private final ThirdFusionAccountAllocationService thirdFusionAccountAllocationService;

    @Override
    public void batchSaveThirdFusionAccount(List<ThirdFusionAccount> thirdFusionAccounts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pagination<ThirdFusionAccountVO> getThirdFusionAccountPage(ThirdFusionAccountFacadeQueryParam query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ThirdFusionAccountAllocationVO> getOnlineUser(String providerCode, Set<Long> sysUserIds) {
        List<ThirdFusionAccountAllocationPO> thirdFusionAccountAllocations = thirdFusionAccountAllocationService.getThirdFusionAccountAllocations(providerCode, sysUserIds);
        if (CollectionUtils.isEmpty(thirdFusionAccountAllocations)) {
            return Collections.emptyList();
        }
        return thirdFusionAccountAllocations.stream().map(account -> {
            ThirdFusionAccountVO fusionAccountVO = new ThirdFusionAccountVO();
            fusionAccountVO.setId(account.getId());
            fusionAccountVO.setCode(account.getThirdAccountNumber());
            fusionAccountVO.setNumber(account.getThirdAccountNumber());
            fusionAccountVO.setName(account.getThirdAccountNumber());
            fusionAccountVO.setCreateTime(account.getCreateTime());
            return ThirdFusionAccountAllocationVO.builder()
                    .userId(account.getUserId())
                    .thirdFusionAccount(fusionAccountVO)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public Long getSysUserByFusionCommunicateAccount(String providerCode, String thirdAccountNumber) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ThirdFusionAccountAllocationVO> assignFusionAccounts(String providerCode, Set<Long> userIds) {
        LocalDateTime keepAliveTime = DateUtils.now();
        List<ThirdFusionAccountAllocationPO> thirdFusionAccountAllocationPOS = new ArrayList<>();
        userIds.forEach(userId -> {
            Long sysUserByFusionCommunicateAccount = this.thirdFusionAccountAllocationService
                    .getSysUserByFusionCommunicateAccount(providerCode,
                                                          SessionContextHolder
                                                                  .getRequester()
                                                                  .getTerminal() +
                                                          "::" +
                                                          userId);
            if (Objects.isNull(sysUserByFusionCommunicateAccount)) {
                thirdFusionAccountAllocationPOS.add(ThirdFusionAccountAllocationPO
                                                            .builder()
                                                            .providerCode(providerCode)
                                                            .thirdAccountNumber(
                                                                    SessionContextHolder
                                                                            .getRequester()
                                                                            .getTerminal() +
                                                                    "::" + userId)
                                                            .userId(userId)
                                                            .keepAliveTime(keepAliveTime)
                                                            .build());
            }
        });
        if (!CollectionUtils.isEmpty(thirdFusionAccountAllocationPOS)) {
            this.thirdFusionAccountAllocationService.saveBatch(thirdFusionAccountAllocationPOS);
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableFusionAccount(String providerCode, Set<Long> userIds) {
        List<ThirdFusionAccountAllocationPO> thirdFusionAccountAllocationPOS = this.thirdFusionAccountAllocationService
                .getThirdFusionAccountAllocations(providerCode, userIds);
        if (!CollectionUtils.isEmpty(thirdFusionAccountAllocationPOS)) {
            DatabaseContextHolder.softDelete(ThirdFusionAccountAllocationPO.class, thirdFusionAccountAllocationPOS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableFusionAccount(String providerCode, Integer timeoutKeepAliveMinutes) {
        List<ThirdFusionAccountAllocationPO> thirdFusionAccountAllocationPOS = this.thirdFusionAccountAllocationService
                .getThirdFusionAccountAllocations(providerCode, timeoutKeepAliveMinutes);
        thirdFusionAccountAllocationPOS.forEach(thirdFusionAccountAllocationPO -> {
            SessionContextHolder.requestTenant(thirdFusionAccountAllocationPO.getTenant());
            DatabaseContextHolder.softDelete(thirdFusionAccountAllocationService, thirdFusionAccountAllocationPO);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void keepAliveFusionAccount(String providerCode, Set<Long> userIds) {
        LambdaUpdateWrapper<ThirdFusionAccountAllocationPO> updateWrapper = Wrappers.<ThirdFusionAccountAllocationPO>lambdaUpdate()
                .eq(ThirdFusionAccountAllocationPO::getProviderCode, providerCode)
                .in(ThirdFusionAccountAllocationPO::getUserId, userIds)
                .set(ThirdFusionAccountAllocationPO::getKeepAliveTime, DateUtils.now());
        this.thirdFusionAccountAllocationService.update(updateWrapper);
    }
}
