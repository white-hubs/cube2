package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.BaseRuleTimeTypeRepeatValidator;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.dto.EmergencyTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class RuleTimeValidator extends BaseRuleTimeTypeRepeatValidator {

    @Override
    protected String getRuleType() {
        return EmergencyConditionTimeTypeEnum.TIME.name();
    }

    @Override
    protected boolean valid(List<EmergencyTimeValidatorInfo> emergencyTimeValidatorInfos) {
        List<EmergencyTimeInfo> emergencyTimeInfos = new ArrayList<>();
        emergencyTimeValidatorInfos.forEach(emergencyTimeValidatorInfo -> {
            Assert.isNotAcceptable(StringUtils.isBlank(emergencyTimeValidatorInfo.getTimeConditions()), "未正确配置时间段");
            emergencyTimeInfos.addAll(JsonUtils.deserialize(emergencyTimeValidatorInfo.getTimeConditions(), new TypeReference<List<EmergencyTimeInfo>>() {
            }));
        });
        emergencyTimeInfos.sort(Comparator.comparing(EmergencyTimeInfo::getBeginTime));
        boolean hasOverlap = false;
        for (int i = 0; i < emergencyTimeInfos.size() - 1; i++) {
            EmergencyTimeInfo currentRange = emergencyTimeInfos.get(i);
            EmergencyTimeInfo nextRange = emergencyTimeInfos.get(i + 1);
            if (currentRange.getEndTime().isAfter(nextRange.getBeginTime())) {
                hasOverlap = true;
                break;
            }
        }
        return hasOverlap;
    }
}
