package com.guhecloud.cube.biz.fusion.communicate.server.third.account.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.aggregates.ThirdFusionAccount;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.entity.AccountInfo;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.enumeration.AccountUsageStateEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionAccountPO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.valueobject.AccountLevel;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.valueobject.AccountUsageState;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.infrastructure.repository.mapper.ThirdFusionAccountMapper;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdFusionAccountRepository {

    private final ThirdFusionAccountMapper thirdFusionAccountMapper;

    @Transactional(rollbackFor = Exception.class)
    public ThirdFusionAccount save(ThirdFusionAccount thirdFusionAccount) {
        boolean isExecuteSave = true;
        ThirdFusionAccountPO existCommunicate = this.thirdFusionAccountMapper
                .selectOne(Wrappers.<ThirdFusionAccountPO>lambdaQuery()
                                   .eq(ThirdFusionAccountPO::getNumber, thirdFusionAccount.getAccountInfo().getNumber())
                          );
        if (Objects.nonNull(existCommunicate)) {
            isExecuteSave = false;
        }
        ThirdFusionAccountPO thirdFusionAccountPO = buildThirdFusionAccountPO(thirdFusionAccount);
        if (isExecuteSave) {
            this.thirdFusionAccountMapper.insert(thirdFusionAccountPO);
        } else {
            thirdFusionAccountPO.setId(existCommunicate.getId());
            EntityAuditor.populateCreateAudit(thirdFusionAccountPO, existCommunicate.getCreateBy(), existCommunicate.getCreatorId(),
                                              existCommunicate.getCreatorType(), existCommunicate.getCreateTime());
            this.thirdFusionAccountMapper.updateById(thirdFusionAccountPO);
        }
        return thirdFusionAccount;
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<ThirdFusionAccount> thirdFusionAccounts) {
        List<ThirdFusionAccountPO> thirdFusionAccountPOS = thirdFusionAccounts
                .stream()
                .map(this::buildThirdFusionAccountPO).collect(Collectors.toList());
        this.thirdFusionAccountMapper.insertOrUpdateBatch(thirdFusionAccountPOS);
    }

    public ThirdFusionAccount detail(String accountNumber) {
        ThirdFusionAccountPO existCommunicate = this.thirdFusionAccountMapper
                .selectOne(Wrappers.<ThirdFusionAccountPO>lambdaQuery()
                                   .eq(ThirdFusionAccountPO::getNumber, accountNumber)
                          );
        if (Objects.isNull(existCommunicate)) {
            return null;
        }
        AccountInfo accountInfo = new AccountInfo(existCommunicate.getId(), existCommunicate.getName(), existCommunicate
                .getCode(), existCommunicate.getNumber(), existCommunicate.getPassword());
        AccountLevel accountLevel = null;
        if (StringUtils.isNotBlank(existCommunicate.getLevel())) {
            accountLevel = new AccountLevel(existCommunicate.getLevel());
        }
        AccountUsageState accountUsageState = null;
        if (StringUtils.isNotBlank(existCommunicate.getUsageState())) {
            accountUsageState = new AccountUsageState(AccountUsageStateEnum.valueOf(existCommunicate.getUsageState()));
        }
        return ThirdFusionAccount.builder()
                .accountInfo(accountInfo)
                .level(accountLevel)
                .usageState(accountUsageState)
                .providerCode(existCommunicate.getProviderCode())
                .build();
    }

    public ThirdFusionAccountPO buildThirdFusionAccountPO(ThirdFusionAccount thirdFusionAccount) {
        //通讯账号信息
        return ThirdFusionAccountPO
                .builder()
                .id(thirdFusionAccount.getAccountInfo().getId())
                .code(thirdFusionAccount.getAccountInfo().getCode())
                .name(thirdFusionAccount.getAccountInfo().getName())
                .number(thirdFusionAccount.getAccountInfo().getNumber())
                .password(thirdFusionAccount.getAccountInfo().getPassword())
                .level(Objects.nonNull(thirdFusionAccount.getLevel())
                       ? thirdFusionAccount.getLevel().getLevel() : null)
                .usageState(Objects.nonNull(thirdFusionAccount
                                                    .getUsageState())
                            ? thirdFusionAccount.getUsageState()
                                    .getState().name() : AccountUsageStateEnum.UNUSED.name())
                .providerCode(thirdFusionAccount.getProviderCode())
                .tenant(SessionContextHolder.getRequester().getTenant())
                .deleted(false)
                .build();
    }
}
