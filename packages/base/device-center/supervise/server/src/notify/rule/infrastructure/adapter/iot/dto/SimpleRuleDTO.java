package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
