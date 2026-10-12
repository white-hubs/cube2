package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.rule;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ComboRuleDTO extends RuleDTO{

    private List<RuleDTO> rules;

    @Builder(toBuilder = true)
    public ComboRuleDTO(String deviceId, String op, Integer delayTime, List<RuleDTO> rules) {
        super(deviceId, op, delayTime);
        this.rules = rules;
    }
}
