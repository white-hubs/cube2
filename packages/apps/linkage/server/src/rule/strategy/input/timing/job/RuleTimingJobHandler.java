package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.job;

import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.handler.RuleTimingHandler;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleTimingJobHandler {

    private final RuleTimingHandler ruleTimingHandler;

    private final RuleService ruleService;

    public void executeRuleTiming(String ruleId) {
        Rule rule = ruleService.getById(Long.valueOf(ruleId));
        if (Objects.nonNull(rule) && rule.getEnabled()) {
            RuleBusinessEventHandlerRequest request = new RuleBusinessEventHandlerRequest(rule.getBusinessType());
            request.setRule(rule);
            request.setTriggerTime(DateUtils.now());
            this.ruleTimingHandler.ruleEvent(request);
        }
    }
}
