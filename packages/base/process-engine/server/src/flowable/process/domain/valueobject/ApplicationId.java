package com.guhecloud.cube.base.flow.flowable.process.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

/**
 * @author pengjian
 * @date 2022/8/31 15:54
 * @description
 */
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Getter
public class ApplicationId {
    @NotBlank
    private String applicationName;
    @NotBlank
    private String applicationCode;
}
