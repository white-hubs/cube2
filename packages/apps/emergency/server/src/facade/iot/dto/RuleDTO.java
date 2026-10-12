package com.guhecloud.cube.biz.emergency.server.facade.iot.dto;

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
