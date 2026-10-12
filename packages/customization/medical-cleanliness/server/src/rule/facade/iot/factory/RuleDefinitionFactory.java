package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.factory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.RuleCreateParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.rule.ComboRuleDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.rule.RuleConfigDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.rule.RuleDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.rule.SimpleRuleDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;
import com.guhecloud.platform.base.context.SessionContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RuleDefinitionFactory {

    public static RuleCreateParam buildRuleCreateParam(RuleVO ruleVO, Integer delayTime) {
        RuleConfigDTO ruleConfig = new RuleConfigDTO();
        ComboRuleDTO comboRule = new ComboRuleDTO();
        List<RuleDTO> rules = new ArrayList<>();
        //整体comboRule关系为or,comboRule下的simpleRules关系为and
        ruleVO.getDeviceDTO().forEach(device -> {
            List<RuleDTO> comboSimpleRules = new ArrayList<>();
            //每个设备的属性条件关系为or
            ComboRuleDTO deviceComboRule = ComboRuleDTO.builder()
                    .op("or")
                    .build();
            ruleVO.getRuleNormList().forEach(ruleNormVO -> {
                if (Objects.nonNull(ruleNormVO.getLowerLimit())) {
                    SimpleRuleDTO simpleRule = getSimpleRuleDTO(device.getDeviceId(), ruleNormVO.getNormCode(), "LT", ruleNormVO
                            .getLowerLimit().toString(), delayTime);
                    comboSimpleRules.add(simpleRule);
                }
                if (Objects.nonNull(ruleNormVO.getUpperLimit())) {
                    SimpleRuleDTO simpleRule = getSimpleRuleDTO(device.getDeviceId(), ruleNormVO.getNormCode(), "GT", ruleNormVO
                            .getUpperLimit().toString(), delayTime);
                    comboSimpleRules.add(simpleRule);
                }
            });
            deviceComboRule.setRules(comboSimpleRules);
            rules.add(deviceComboRule);
        });
        comboRule.setOp("or");
        comboRule.setRules(rules);
        ruleConfig.setRule(comboRule);
        ruleConfig.setAction(Collections.emptyList());
        String ruleId = ruleVO.getIotRuleId().toString();
        return getRuleCreateParam(ruleId,
                                  ruleId,
                                  ruleConfig);
    }

    private static SimpleRuleDTO getSimpleRuleDTO(Long deviceId, String propCode, String op, String oprand, Integer delayTime) {
        return SimpleRuleDTO.builder()
                .deviceId(deviceId.toString())
                .propCode(propCode)
                .op(op)
                .oprand(Collections.singleton(oprand))
                .delayTime(delayTime)
                .build();
    }

    private static RuleCreateParam getRuleCreateParam(String ruleNum,
                                                      String ruleName,
                                                      RuleConfigDTO ruleConfigDTO
                                                     ) {
        RuleCreateParam ruleCreateParam = new RuleCreateParam();
        ruleCreateParam.setRuleNum(ruleNum);
        ruleCreateParam.setRuleName(ruleName);
        ruleCreateParam.setTenantId(SessionContextHolder.getRequester().getTenant().toString());
        ruleCreateParam.setEnabled("true");
        ruleCreateParam.setRuleConfig((JSONObject) JSON.toJSON(ruleConfigDTO));
        return ruleCreateParam;
    }
}
