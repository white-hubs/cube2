package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 账号保活参数
 *
 * @author lanchun
 * @date 2024/8/16 16:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeepAliveThirdFusionAccountCommand {

    /**
     * 厂家编号
     */
    private String providerCode;

    /**
     * 用户主键
     */
    private Set<Long> userIds;
}
