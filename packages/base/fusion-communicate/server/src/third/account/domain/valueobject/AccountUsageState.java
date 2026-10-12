package com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.valueobject;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.enumeration.AccountUsageStateEnum;
import lombok.Getter;

/**
 * 使用状态
 *
 * @author lanchun
 * @date 2024/7/29 15:22
 */
@Getter
public class AccountUsageState {

    /**
     * 账号使用状态
     */
    private AccountUsageStateEnum state;

    public AccountUsageState(AccountUsageStateEnum state) {
        this.state = state;
    }
}
