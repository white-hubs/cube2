package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleTimingJob {

    private final RuleTimingJobHandler ruleTimingJobHandler;

    public void ruleTimingExecute(String param) {
        this.ruleTimingJobHandler.executeRuleTiming(param);
    }
}
