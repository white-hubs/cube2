package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleDTO {

    @NotBlank
    protected String deviceId;

    @NotBlank
    protected String op;

    @NotNull
    protected Integer delayTime;
}
