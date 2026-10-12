package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.CommonTriggerPart;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.RuleTriggerMsg;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.RuleTriggerTask;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.rejected.RuleIssuedAbortPolicy;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleNormVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RuleTriggerExecutor {

    private static final ThreadPoolExecutor ruleTriggerExecutor;

    static {
        ruleTriggerExecutor = new
                ThreadPoolExecutor(3,
                                   6,
                                   5,
                                   TimeUnit.SECONDS,
                                   new LinkedBlockingQueue<>(5000),
                                   new ThreadFactoryBuilder().setNameFormat("medical-cleanliness-rule-trigger-%d").build(),
                                   new RuleIssuedAbortPolicy());
    }

    public static void triggerRule(RuleVO ruleVO,
                                  RuleNormVO ruleNormVO,
                                  RuleTriggerMsg ruleTriggerMsg,
                                  CommonTriggerPart commonTriggerPart
                                 ) {
        ruleTriggerExecutor.submit(new RuleTriggerTask(ruleVO, ruleNormVO, ruleTriggerMsg, commonTriggerPart));
    }
}
