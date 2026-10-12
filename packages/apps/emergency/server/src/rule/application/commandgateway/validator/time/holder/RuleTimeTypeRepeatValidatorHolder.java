package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.holder;

import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.RuleTimeRepeatValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 时间类型时段重复校验器
 *
 * @author lanchun
 * @date 2023/12/12 18:11
 */
@Component
public class RuleTimeTypeRepeatValidatorHolder {

    private Map<String, RuleTimeRepeatValidator> ruleTimeValidatorMap = new HashMap<>(10);

    public void putValidator(String ruleTimeType, RuleTimeRepeatValidator ruleTimeRepeatValidator) {
        this.ruleTimeValidatorMap.put(ruleTimeType, ruleTimeRepeatValidator);
    }

    public RuleTimeRepeatValidator getValidator(String ruleTimeType) {
        return this.ruleTimeValidatorMap.get(ruleTimeType);
    }
}
