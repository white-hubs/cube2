package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.rule;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Data
@NoArgsConstructor
public class SimpleRuleDTO extends RuleDTO {

    @NotBlank
    private String propCode;
    @NotEmpty
    private Set<String> oprand;

    @Builder(toBuilder = true)
    public SimpleRuleDTO(String deviceId, String propCode, String op, Set<String> oprand, Integer delayTime) {
        super(deviceId, op, delayTime);
        this.propCode = propCode;
        this.oprand = oprand;
    }

}
