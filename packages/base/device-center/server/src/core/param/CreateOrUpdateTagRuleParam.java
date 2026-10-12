package com.guhecloud.cube.base.device.center.core.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CreateOrUpdateTagRuleParam {

    /**
     * 标签编号
     */
    @NotBlank(message = "标签编号不能为空")
    private String code;

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    private String name;

    /**
     * 规则内容
     */
    @NotBlank(message = "规则内容不能为空")
    private String rule;
}
