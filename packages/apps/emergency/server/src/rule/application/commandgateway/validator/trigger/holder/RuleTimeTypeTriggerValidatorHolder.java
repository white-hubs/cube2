package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger.holder;

import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger.RuleTimeTriggerValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 规则触发时段校验器
 *
 * @author lanchun
 * @date 2023/12/12 18:11
 */
@Component
public class RuleTimeTypeTriggerValidatorHolder {

    private Map<String, RuleTimeTriggerValidator> ruleTimeTriggerValidatorMap = new HashMap<>(10);

    public void putValidator(String ruleTimeType, RuleTimeTriggerValidator ruleTimeTriggerValidator) {
        this.ruleTimeTriggerValidatorMap.put(ruleTimeType, ruleTimeTriggerValidator);
    }

    public RuleTimeTriggerValidator getValidator(String ruleTimeType) {
        return this.ruleTimeTriggerValidatorMap.get(ruleTimeType);
    }
}
