package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.dto;

import lombok.Data;

/**
 * 场景规则
 *
 * @author lanchun
 * @date 2022/6/30 9:38
 */
@Data
public class RuleSceneDTO {

    /**
     * 密码
     */
    private String password;

    /**
     * 确认输入的密码
     */
    private String againPassword;
}
