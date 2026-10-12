package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionAccountAllocationPO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.infrastructure.repository.mapper.ThirdFusionAccountAllocationMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ThirdFusionAccountAllocationService extends ServiceImpl<ThirdFusionAccountAllocationMapper, ThirdFusionAccountAllocationPO> {

    public Long getSysUserByFusionCommunicateAccount(String providerCode, String thirdAccountNumber) {
        ThirdFusionAccountAllocationPO thirdFusionAccountAllocationPO = this.baseMapper
                .selectOne(Wrappers.<ThirdFusionAccountAllocationPO>lambdaQuery()
                                   .eq(ThirdFusionAccountAllocationPO::getProviderCode, providerCode)
                                   .eq(ThirdFusionAccountAllocationPO::getThirdAccountNumber, thirdAccountNumber)
                          );
        if (Objects.nonNull(thirdFusionAccountAllocationPO)) {
            return thirdFusionAccountAllocationPO.getUserId();
        }
        return null;
    }

    public Set<Long> getSysUsersByIds(String providerCode, Set<Long> sysUserIds) {
        List<ThirdFusionAccountAllocationPO> thirdFusionAccountAllocationPOS = getThirdFusionAccountAllocations(providerCode, sysUserIds);
        if (CollectionUtils.isEmpty(thirdFusionAccountAllocationPOS)) {
            return Collections.emptySet();
        }
        return thirdFusionAccountAllocationPOS.stream().map(ThirdFusionAccountAllocationPO::getUserId)
                .collect(Collectors.toSet());
    }

    public List<ThirdFusionAccountAllocationPO> getThirdFusionAccountAllocations(String providerCode, Set<Long> sysUserIds) {
        return this.baseMapper.selectList(Wrappers.<ThirdFusionAccountAllocationPO>lambdaQuery()
                                                  .eq(ThirdFusionAccountAllocationPO::getProviderCode, providerCode)
                                                  .in(!CollectionUtils
                                                          .isEmpty(sysUserIds), ThirdFusionAccountAllocationPO::getUserId, sysUserIds)
                                         );
    }

    public List<ThirdFusionAccountAllocationPO> getThirdFusionAccountAllocations(String providerCode,
                                                                                 Integer timeoutKeepAliveMinutes
                                                                                ) {
        LocalDateTime timeoutDate = DateUtils.offset(DateUtils.now(), ChronoUnit.MINUTES, -timeoutKeepAliveMinutes);
        return this.baseMapper.selectList(Wrappers.<ThirdFusionAccountAllocationPO>lambdaQuery()
                                                  .eq(ThirdFusionAccountAllocationPO::getProviderCode, providerCode)
                                                  .le(ThirdFusionAccountAllocationPO::getKeepAliveTime, timeoutDate));
    }
}