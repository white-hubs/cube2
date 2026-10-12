package com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.event;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.enumeration.AccountUsageStateEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeAccountUsageStateEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    /**
     * 通讯账号号码
     */
    private final String accountNumber;

    /**
     * 使用状态
     */
    private final AccountUsageStateEnum usageState;

    public ChangeAccountUsageStateEvent(Object source, String accountNumber, AccountUsageStateEnum usageState) {
        super(source);
        this.accountNumber = accountNumber;
        this.usageState = usageState;
    }
}
