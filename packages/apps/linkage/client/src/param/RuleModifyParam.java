package com.guhecloud.cube.biz.linkage.client.param;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 规则配置
 *
 * @author lanchun
 * @date 2022/6/21 17:05
 */
@Data
public class RuleModifyParam {

    /**
     * 基础信息
     */
    @Valid
    private RuleBasicModifyParam basicModify;

    /**
     * 规则配置
     */
    @NotEmpty(message = "规则配置不能为空")
    private String ruleConfig;

    /**
     * 规则联动动作配置
     */
    @Valid
    private List<LinkageActionModifyParam> linkageEmergencyActionModifies;
}
