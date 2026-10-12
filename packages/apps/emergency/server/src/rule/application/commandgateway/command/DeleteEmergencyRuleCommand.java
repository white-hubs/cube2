package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/**
 * 预案规则删除命令
 *
 * @author lanchun
 * @date 2022/9/5 10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteEmergencyRuleCommand {

    /**
     * 预案规则id集合
     */
    @NotEmpty
    private Set<Long> emergencyRuleIds;
}
