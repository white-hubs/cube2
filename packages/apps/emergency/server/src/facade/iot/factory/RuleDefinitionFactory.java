package com.guhecloud.cube.biz.emergency.server.facade.iot.factory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.emergency.server.facade.iot.dto.ComboRuleDTO;
import com.guhecloud.cube.biz.emergency.server.facade.iot.dto.RuleConfigDTO;
import com.guhecloud.cube.biz.emergency.server.facade.iot.dto.RuleDTO;
import com.guhecloud.cube.biz.emergency.server.facade.iot.dto.SimpleRuleDTO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.CompareOpEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionDeviceInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionInfo;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.IssuanceIotRuleGroupEvent;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.RuleCreateParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleDefinitionFactory {

    public static RuleCreateParam buildRuleCreateParam(IssuanceIotRuleGroupEvent issuanceIotRuleGroupEvent,
                                                       List<EmergencyRuleConditionInfo> ruleConditionInfos,
                                                       String openOrClose
                                                      ) {
        Map<String, List<EmergencyRuleConditionDeviceInfo>> ruleConditionDeviceMap = issuanceIotRuleGroupEvent
                .getEmergencyRuleConditionDeviceInfos()
                .stream()
                .collect(Collectors.groupingBy(conditionDevice -> conditionDevice.getDeviceProdCategoryCode() + ":" +
                                                                  conditionDevice.getDeviceProdId()));
        RuleConfigDTO ruleConfig = new RuleConfigDTO();
        ComboRuleDTO comboRule = new ComboRuleDTO();
        List<RuleDTO> rules = new ArrayList<>();
        //整体comboRule关系为or,comboRule下的simpleRules关系为and
        ruleConditionInfos.forEach(deviceProEvent -> {
            List<EmergencyRuleConditionDeviceInfo> modelConditionDevices = ruleConditionDeviceMap.get(
                    deviceProEvent.getDeviceProdCategoryCode() + ":" + deviceProEvent.getDeviceProdId());
            if (!CollectionUtils.isEmpty(modelConditionDevices)) {
                modelConditionDevices.forEach(device -> {
                    List<RuleDTO> comboSimpleRules = new ArrayList<>(deviceProEvent.getEmergencyRuleConditionExps().size());
                    //每个设备的属性条件关系为and
                    ComboRuleDTO deviceComboRule = ComboRuleDTO.builder()
                            .op("and")
                            .build();
                    deviceProEvent.getEmergencyRuleConditionExps().forEach(deviceConditionExp -> {
                        SimpleRuleDTO simpleRule = getSimpleRuleDTO(device.getDeviceId(), deviceConditionExp.getPropCode(), deviceConditionExp.getOp(), deviceConditionExp.getOprand(), deviceConditionExp.getDelayTime());
                        comboSimpleRules.add(simpleRule);
                    });
                    deviceComboRule.setRules(comboSimpleRules);
                    rules.add(deviceComboRule);
                });
            }
        });
        if (CollectionUtils.isEmpty(rules)) {
            return null;
        }
        comboRule.setOp(issuanceIotRuleGroupEvent.getOp());
        comboRule.setRules(rules);
        ruleConfig.setRule(comboRule);
        ruleConfig.setAction(Collections.emptyList());
        String ruleId = issuanceIotRuleGroupEvent.getEmergencyRuleConditionGroupInfo().getId().toString();
        return getRuleCreateParam(ruleId,
                                  issuanceIotRuleGroupEvent.getEmergencyRuleName() + ":" + ruleId,
                                  "open".equalsIgnoreCase(openOrClose) ? "true" : "false",
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
