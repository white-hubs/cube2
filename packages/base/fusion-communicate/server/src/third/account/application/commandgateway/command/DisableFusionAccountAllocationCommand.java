package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisableFusionAccountAllocationCommand {

    /**
     * 厂家编号
     */
    private String providerCode;

    /**
     * 用户主键集合
     */
    private Set<Long> userIds;

    /**
     * 超时保活分钟数
     */
    private Integer timeoutKeepAliveMinutes;
}
