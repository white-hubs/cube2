package com.guhecloud.cube.base.device.center.core.rule.tag.entity;

import lombok.Getter;

@Getter
public class TagRuleTask {

    /**
     * 标签编号
     */
    private final String code;

    public TagRuleTask(String code) {
        this.code = code;
    }
}
