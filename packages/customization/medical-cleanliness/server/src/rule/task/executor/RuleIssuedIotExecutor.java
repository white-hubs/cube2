package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.RuleIssuedTask;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.dto.RuleIssuedIotDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.rejected.RuleIssuedAbortPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RuleIssuedIotExecutor {

    private static final ThreadPoolExecutor issuedIotExecutor;

    static {
        issuedIotExecutor = new
                ThreadPoolExecutor(3,
                                   6,
                                   5,
                                   TimeUnit.SECONDS,
                                   new LinkedBlockingQueue<>(5000),
                                   new ThreadFactoryBuilder().setNameFormat("medical-cleanliness-rule-issued-%d").build(),
                                   new RuleIssuedAbortPolicy());
    }

    public static void issuedRule(Set<RuleIssuedIotDTO> ruleIssuedIotDTOS) {
        for (RuleIssuedIotDTO ruleIssuedIotDTO : ruleIssuedIotDTOS) {
            issuedRule(ruleIssuedIotDTO);
        }
    }

    public static void issuedRule(RuleIssuedIotDTO ruleIssuedIotDTO) {
        issuedIotExecutor.submit(new RuleIssuedTask(ruleIssuedIotDTO));
    }
}
