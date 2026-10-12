package com.guhecloud.cube.biz.linkage.server.rule.strategy;

import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessChangeHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessDelHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEnabledHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessIncreaseHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessRequest;

public interface RuleBusinessHandler {

    boolean supports(RuleBusinessRequest request);

    void ruleAnalysis(RuleBusinessIncreaseHandlerRequest request);

    void ruleUpdate(RuleBusinessChangeHandlerRequest request);

    void ruleEnabledUpdate(RuleBusinessEnabledHandlerRequest request);

    void ruleRemove(RuleBusinessDelHandlerRequest request);

    void ruleEvent(RuleBusinessEventHandlerRequest request);

}
