package com.guhecloud.cube.biz.fusion.communicate.server.third.base.common;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.aggregates.ThirdFusionAccount;

import java.util.List;

@FunctionalInterface
public interface CommonAccountSync {

    /**
     * 同步第三方通讯账号
     */
    List<ThirdFusionAccount> syncThirdFusionCommunicateAccounts();
}
