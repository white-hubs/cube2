package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 场景规则触发参数
 *
 * @author lanchun
 * @date 2022/6/30 11:07
 */
@Data
public class RuleSceneTriggerParam {

    /**
     * 规则主键
     */
    @NotNull(message = "规则主键不能为空")
    private Long id;

    /**
     * 密码
     */
    @Size(max = 20, message = "密码不能大于20")
    private String password;
}
