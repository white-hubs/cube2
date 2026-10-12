package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator;

import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleConditionGroupCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleTimeCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.RuleTimeRepeatValidator;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.holder.RuleTimeTypeRepeatValidatorHolder;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 预案规则修改校验器
 *
 * @author lanchun
 * @date 2022/9/3 14:41
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeEmergencyRuleValidator {

    private final RuleTimeTypeRepeatValidatorHolder ruleTimeTypeRepeatValidatorHolder;

    public void verify(final ChangeEmergencyRuleCommand changeEmergencyRuleCommand) {
        //时段校验
        Map<EmergencyConditionTimeTypeEnum, List<ChangeEmergencyRuleTimeCommand>> emergencyConditionTypeEnumListMap = changeEmergencyRuleCommand
                .getEmergencyRuleConditionGroups()
                .stream()
                .map(ChangeEmergencyRuleConditionGroupCommand::getEmergencyRuleTime)
                .collect(Collectors.groupingBy(ChangeEmergencyRuleTimeCommand::getTimeType));
        if (emergencyConditionTypeEnumListMap.size() > 1) {
            throw new ServiceException("报警时段配置重复,请检查后重新保存");
        }
        emergencyConditionTypeEnumListMap.forEach((key, value) -> {
            RuleTimeRepeatValidator ruleTimeRepeatValidator = this.ruleTimeTypeRepeatValidatorHolder.getValidator(key.name());
            if (Objects.nonNull(ruleTimeRepeatValidator)) {
                if (ruleTimeRepeatValidator.changeValidator(value)) {
                    throw new ServiceException("报警时段配置重复,请检查后重新保存");
                }
            }
        });
    }
}
