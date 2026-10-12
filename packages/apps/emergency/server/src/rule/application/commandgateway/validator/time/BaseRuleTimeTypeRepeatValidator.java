package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time;

import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleTimeCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.IncreaseEmergencyRuleTimeCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.holder.RuleTimeTypeRepeatValidatorHolder;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;

import jakarta.annotation.PostConstruct;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseRuleTimeTypeRepeatValidator implements RuleTimeRepeatValidator {

    @Autowired
    private RuleTimeTypeRepeatValidatorHolder ruleTimeTypeRepeatValidatorHolder;

    @Override
    public boolean increaseValidator(List<IncreaseEmergencyRuleTimeCommand> increaseEmergencyRuleTimes) {
        List<IncreaseEmergencyRuleTimeCommand> emergencyConditionTypeEnumLists =
                increaseEmergencyRuleTimes.stream()
                        .filter(increaseEmergencyRuleTimeCommand -> this.getRuleType()
                                .equals(increaseEmergencyRuleTimeCommand.getTimeType().name()))
                        .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(emergencyConditionTypeEnumLists)) {
            return this.valid(ObjectUtils.copying(emergencyConditionTypeEnumLists, EmergencyTimeValidatorInfo.class));
        }
        return true;
    }

    @Override
    public boolean changeValidator(List<ChangeEmergencyRuleTimeCommand> changeEmergencyRuleTimes) {
        List<ChangeEmergencyRuleTimeCommand> emergencyConditionTypeEnumLists =
                changeEmergencyRuleTimes.stream()
                        .filter(changeEmergencyRuleTimeCommand -> this.getRuleType()
                                .equals(changeEmergencyRuleTimeCommand.getTimeType().name()))
                        .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(emergencyConditionTypeEnumLists)) {
            return this.valid(ObjectUtils.copying(emergencyConditionTypeEnumLists, EmergencyTimeValidatorInfo.class));
        }
        return true;
    }

    protected abstract String getRuleType();

    protected abstract boolean valid(List<EmergencyTimeValidatorInfo> emergencyTimeValidatorInfos);

    @PostConstruct
    private void initHandler() {
        this.ruleTimeTypeRepeatValidatorHolder.putValidator(this.getRuleType(), this);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmergencyTimeValidatorInfo {

        /**
         * 时段类型
         */
        @NotNull(message = "时段类型不能为空")
        private EmergencyConditionTimeTypeEnum timeType;

        /**
         * 时间条件
         */
        private String timeConditions;
    }
}
