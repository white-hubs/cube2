package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 分配账号参数
 *
 * @author lanchun
 * @date 2024/7/31 15:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignFusionAccountAllocationCommand {

    /**
     * 厂家编号
     */
    private String providerCode;

    /**
     * 用户主键集合
     */
    private Set<Long> userIds;
}
