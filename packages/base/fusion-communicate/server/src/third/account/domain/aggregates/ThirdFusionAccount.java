package com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.aggregates;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.entity.AccountInfo;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.valueobject.AccountLevel;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.valueobject.AccountUsageState;
import lombok.Builder;
import lombok.Getter;

/**
 * 通讯账号信息
 *
 * @author lanchun
 * @date 2024/7/29 15:26
 */
@Getter
@Builder
public class ThirdFusionAccount {

    /**
     * 账号级别
     */
    private AccountInfo accountInfo;

    /**
     * 级别
     */
    private AccountLevel level;

    /**
     * 使用状态
     */
    private AccountUsageState usageState;

    /**
     * 厂家编号
     */
    private String providerCode;

    public ThirdFusionAccount(AccountInfo accountInfo,
                              AccountLevel level,
                              AccountUsageState usageState,
                              String providerCode
                             ) {
        this.accountInfo = accountInfo;
        this.level = level;
        this.usageState = usageState;
        this.providerCode = providerCode;
    }

    public void changeAccountInfo(String name, String password, String code) {
        this.accountInfo = new AccountInfo(this.accountInfo.getId(), name, code, this.accountInfo.getNumber(), password);
    }

    /**
     * 修改账号级别
     *
     * @param accountLevel
     */
    public void changeAccountLevel(AccountLevel accountLevel) {
        this.level = accountLevel;
    }

    /**
     * 修改账号使用状态
     *
     * @param usageState
     */
    public void changeAccountUsageState(AccountUsageState usageState) {
        this.usageState = usageState;
    }
}
