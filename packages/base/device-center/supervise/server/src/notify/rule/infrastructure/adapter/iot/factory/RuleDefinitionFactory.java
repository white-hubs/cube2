package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.factory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.RuleCreateParam;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRule;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.CompareOpEnum;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration.DeviceRuleConditionTypeEnum;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyRuleCondition;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.dto.ComboRuleDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.dto.RuleConfigDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.dto.RuleDTO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.dto.SimpleRuleDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleDefinitionFactory {

    public static RuleCreateParam buildRuleCreateParam(DeviceNotifyRule rule) {
        RuleConfigDTO ruleConfig = new RuleConfigDTO();
        ComboRuleDTO comboRule = new ComboRuleDTO();
        List<RuleDTO> rules = new ArrayList<>();
        List<DeviceNotifyRuleCondition> notifyRuleConditions = rule.getConditions().stream()
                .filter(conditionCommand -> DeviceRuleConditionTypeEnum.CUSTOM_RULE_CONDITION == conditionCommand.getConditionType())
                .toList();
        if (CollectionUtils.isEmpty(notifyRuleConditions)) {
            return null;
        }
        //整体comboRule关系为or,comboRule下的simpleRules关系为and
        notifyRuleConditions.forEach(deviceProEvent -> {
            List<RuleDTO> comboSimpleRules = new ArrayList<>(deviceProEvent.getConditionExps().size());
            //每个设备的属性条件关系为and
            ComboRuleDTO deviceComboRule = ComboRuleDTO.builder()
                    .op("and")
                    .build();
            deviceProEvent.getConditionExps().forEach(deviceConditionExp -> {
                SimpleRuleDTO simpleRule = getSimpleRuleDTO(rule.getDeviceId(), deviceConditionExp.getPropCode(), deviceConditionExp.getOp(), deviceConditionExp.getOprand(), deviceConditionExp.getDelayTime());
                comboSimpleRules.add(simpleRule);
            });
            deviceComboRule.setRules(comboSimpleRules);
            rules.add(deviceComboRule);
        });
        if (CollectionUtils.isEmpty(rules)) {
            return null;
        }
        comboRule.setOp("or");
        comboRule.setRules(rules);
        ruleConfig.setRule(comboRule);
        ruleConfig.setAction(Collections.emptyList());
        return getRuleCreateParam(rule.getIotRuleId().toString(),
                                  rule.getIotRuleId().toString(),
                                  rule.getEnabled() ? "true" : "false",
                                  ruleConfig);
    }

    private static SimpleRuleDTO getSimpleRuleDTO(Long deviceId, String propCode, CompareOpEnum op, String oprand, Integer delayTime) {
        return SimpleRuleDTO.builder()
                .deviceId(deviceId.toString())
                .propCode(propCode)
                .op(op.name())
                .oprand(Collections.singleton(oprand))
                .delayTime(delayTime)
                .build();
    }

    private static RuleCreateParam getRuleCreateParam(String ruleNum,
                                                      String ruleName,
                                                      String enabled,
                                                      RuleConfigDTO ruleConfigDTO
                                                     ) {
        RuleCreateParam ruleCreateParam = new RuleCreateParam();
        ruleCreateParam.setRuleNum(ruleNum);
        ruleCreateParam.setRuleName(ruleName);
        ruleCreateParam.setTenantId(SessionContextHolder.getRequester().getTenant().toString());
        ruleCreateParam.setEnabled(enabled);
        ruleCreateParam.setRuleConfig((JSONObject) JSON.toJSON(ruleConfigDTO));
        return ruleCreateParam;
    }
}
