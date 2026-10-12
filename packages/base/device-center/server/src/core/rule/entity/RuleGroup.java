package com.guhecloud.cube.base.device.center.core.rule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RuleGroup {

    /**
     * 规则
     */
    private List<Rule> rules;
}
