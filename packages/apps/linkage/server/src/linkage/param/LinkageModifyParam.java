package com.guhecloud.cube.biz.linkage.server.linkage.param;

import lombok.Data;

import java.util.List;

/**
 * 联动动作
 *
 * @author lanchun
 * @date 2022/10/20 11:43
 */
@Data
public class LinkageModifyParam {

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 动作前置配置
     */
    private List<ActionBeforeParam> actionBeforeParams;

    /**
     * 联动动作
     */
    private List<LinkageActionModifyParam> linkageActionModifyParams;
}
