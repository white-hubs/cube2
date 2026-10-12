package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.BaseRuleTimeTypeRepeatValidator;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class RuleMonthTimeValidator extends BaseRuleTimeTypeRepeatValidator {

    @Override
    protected String getRuleType() {
        return EmergencyConditionTimeTypeEnum.MONTH.name();
    }

    @Override
    protected boolean valid(List<EmergencyTimeValidatorInfo> emergencyTimeValidatorInfos) {
        List<Integer> months = new ArrayList<>();
        emergencyTimeValidatorInfos.forEach(emergencyTimeValidatorInfo -> {
            Assert.isNotAcceptable(StringUtils.isBlank(emergencyTimeValidatorInfo.getTimeConditions()), "未正确配置时间段");
            List<Integer> selectMonths = JsonUtils.deserialize(emergencyTimeValidatorInfo.getTimeConditions(), new TypeReference<>() {});
            months.addAll(selectMonths);
        });
        Set<Integer> set = new HashSet<>(months);
        return months.size() != set.size();
    }
}
