package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.ThirdFusionAccountAllocationService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.ThirdFusionAccountService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command.AssignFusionAccountAllocationCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command.DisableFusionAccountAllocationCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command.KeepAliveThirdFusionAccountCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway.ThirdFusionAccountQueryService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.enumeration.AccountUsageStateEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionAccountAllocationPO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionAccountPO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountAllocationVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionAccountVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdFusionAccountAllocationCommandService {

    private final ThirdFusionAccountAllocationService thirdFusionAccountAllocationService;

    private final ThirdFusionAccountService thirdFusionAccountService;

    private final ThirdFusionAccountQueryService thirdFusionAccountQueryService;

    private static final String ASSIGN_ACCOUNT_LOCK = "fusionCommunicateAccountLock";

    @Transactional(rollbackFor = Exception.class)
    public List<ThirdFusionAccountAllocationVO> assignFusionCommunicateAccount(AssignFusionAccountAllocationCommand command) {
        //加锁防止并发的情况
        RedissonRedLock assignAccountRedLock = RedisUtils.getDistributedLock().tryLock(ASSIGN_ACCOUNT_LOCK, Expiration
                .from(5000, TimeUnit.MILLISECONDS));
        List<ThirdFusionAccountAllocationVO> thirdFusionAccountAllocations = new ArrayList<>(command.getUserIds().size());
        try {
            if (Objects.nonNull(assignAccountRedLock)) {
                List<ThirdFusionAccountAllocationVO> inAssignUsers = this.thirdFusionAccountQueryService
                        .getOnlineUser(command.getProviderCode(), command.getUserIds());
                Set<Long> inAssignUserIds = inAssignUsers.stream().map(ThirdFusionAccountAllocationVO::getUserId)
                        .collect(Collectors.toSet());
                Set<Long> unAssignUserIds = command.getUserIds().stream().filter(userId -> !inAssignUserIds.contains(userId))
                        .collect(Collectors.toSet());
                List<ThirdFusionAccountAllocationPO> thirdFusionAccountAllocationPOS = new ArrayList<>(unAssignUserIds.size());
                thirdFusionAccountAllocations.addAll(assignAccount(command, unAssignUserIds, thirdFusionAccountAllocationPOS));
                if (!CollectionUtils.isEmpty(inAssignUsers)) {
                    thirdFusionAccountAllocations.addAll(inAssignUsers);
                }
            }
        } catch (Exception e) {
            log.error("assign fusion com.guhecloud.cube.biz.fusioncommunicate account fail synchronize failed: {}", e
                    .getMessage());
            throw new ServiceException("获取融合通讯账号失败");
        } finally {
            if (Objects.nonNull(assignAccountRedLock)) {
                assignAccountRedLock.unlock();
            }
        }
        return thirdFusionAccountAllocations;
    }

    public List<ThirdFusionAccountAllocationVO> assignAccount(AssignFusionAccountAllocationCommand command, Set<Long> unAssignUserIds,
                                                              List<ThirdFusionAccountAllocationPO> thirdFusionAccountAllocationPOS
                                                             ) {
        List<ThirdFusionAccountAllocationVO> thirdFusionAccountAllocations = new ArrayList<>();
        //获取空闲的账号
        List<ThirdFusionAccountPO> thirdFusionAccountPOS = this.thirdFusionAccountService
                .queryThirdFusionAccountByState(command.getProviderCode(), AccountUsageStateEnum.UNUSED.name(), unAssignUserIds.size());
        if (CollectionUtils.isEmpty(unAssignUserIds) || CollectionUtils.isEmpty(thirdFusionAccountPOS)) {
            return Collections.emptyList();
        }
        Iterator<Long> unAssignUserIterator = unAssignUserIds.iterator();
        Iterator<ThirdFusionAccountPO> thirdFusionAccountIterator = thirdFusionAccountPOS.iterator();
        while (unAssignUserIterator.hasNext() && thirdFusionAccountIterator.hasNext()) {
            ThirdFusionAccountPO thirdFusionAccountPO = thirdFusionAccountIterator.next();
            Long unAssignUserId = unAssignUserIterator.next();
            ThirdFusionAccountAllocationPO thirdFusionAccountAllocationPO = buildThirdFusionAccountAllocationPO(command, thirdFusionAccountPO, unAssignUserId);
            ThirdFusionAccountVO thirdFusionAccountVO = ObjectUtils.copying(thirdFusionAccountPO, ThirdFusionAccountVO.class);
            thirdFusionAccountAllocationPOS.add(thirdFusionAccountAllocationPO);
            thirdFusionAccountAllocations.add(buildThirdFusionAccountAllocationVO(unAssignUserId, thirdFusionAccountVO));
        }
        this.thirdFusionAccountAllocationService.saveBatch(thirdFusionAccountAllocationPOS);
        this.thirdFusionAccountService.updateAccountState(AccountUsageStateEnum.IN_USED.name(), command
                .getProviderCode(), thirdFusionAccountPOS);
        return thirdFusionAccountAllocations;
    }

    public ThirdFusionAccountAllocationVO buildThirdFusionAccountAllocationVO(Long unAssignUserId,
                                                                              ThirdFusionAccountVO thirdFusionAccountVO
                                                                             ) {
        return ThirdFusionAccountAllocationVO
                .builder()
                .userId(unAssignUserId)
                .thirdFusionAccount(thirdFusionAccountVO)
                .build();
    }

    public ThirdFusionAccountAllocationPO buildThirdFusionAccountAllocationPO(AssignFusionAccountAllocationCommand command,
                                                                              ThirdFusionAccountPO thirdFusionAccountPO,
                                                                              Long unAssignUserId
                                                                             ) {
        return ThirdFusionAccountAllocationPO
                .builder()
                .providerCode(command.getProviderCode())
                .thirdAccountNumber(thirdFusionAccountPO.getNumber())
                .userId(unAssignUserId)
                .keepAliveTime(DateUtils.now())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void disableFusionAccount(DisableFusionAccountAllocationCommand command) {
        List<ThirdFusionAccountAllocationPO> thirdFusionAccountAllocationPOS = this.thirdFusionAccountAllocationService
                .getThirdFusionAccountAllocations(command.getProviderCode(), command
                        .getUserIds());
        if (!CollectionUtils.isEmpty(thirdFusionAccountAllocationPOS)) {
            DatabaseContextHolder.softDelete(thirdFusionAccountAllocationService, thirdFusionAccountAllocationPOS);
            Set<String> thirdFusionAccounts = thirdFusionAccountAllocationPOS.stream()
                    .map(ThirdFusionAccountAllocationPO::getThirdAccountNumber)
                    .collect(Collectors.toSet());
            this.thirdFusionAccountService
                    .updateAccountState(AccountUsageStateEnum.UNUSED.name(), command.getProviderCode(), thirdFusionAccounts);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void disableKeepAliveTimeoutFusionAccount(DisableFusionAccountAllocationCommand command) {
        List<ThirdFusionAccountAllocationPO> thirdFusionAccountAllocationPOS = this.thirdFusionAccountAllocationService
                .getThirdFusionAccountAllocations(command.getProviderCode(), command.getTimeoutKeepAliveMinutes());
        thirdFusionAccountAllocationPOS.forEach(thirdFusionAccountAllocationPO -> {
            SessionContextHolder.requestTenant(thirdFusionAccountAllocationPO.getTenant());
            DatabaseContextHolder.softDelete(thirdFusionAccountAllocationService, thirdFusionAccountAllocationPO);
            this.thirdFusionAccountService
                    .updateAccountState(AccountUsageStateEnum.UNUSED.name(), command.getProviderCode(), Collections
                            .singleton(thirdFusionAccountAllocationPO.getThirdAccountNumber()));
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void keepAliveFusionAccount(KeepAliveThirdFusionAccountCommand command) {
        LambdaUpdateWrapper<ThirdFusionAccountAllocationPO> updateWrapper = Wrappers.<ThirdFusionAccountAllocationPO>lambdaUpdate()
                .eq(ThirdFusionAccountAllocationPO::getProviderCode, command.getProviderCode())
                .in(ThirdFusionAccountAllocationPO::getUserId, command.getUserIds())
                .set(ThirdFusionAccountAllocationPO::getKeepAliveTime, DateUtils.now());
        this.thirdFusionAccountAllocationService.update(updateWrapper);
    }
}
