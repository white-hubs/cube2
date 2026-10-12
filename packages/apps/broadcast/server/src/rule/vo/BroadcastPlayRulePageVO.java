package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo;

import lombok.Data;

@Data
public class BroadcastPlayRulePageVO {

    private Long id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 规则描述
     */
    private String remark;
}
