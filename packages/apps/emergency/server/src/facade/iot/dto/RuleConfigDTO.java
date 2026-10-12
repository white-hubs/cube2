package com.guhecloud.cube.biz.emergency.server.facade.iot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RuleConfigDTO {
    private ComboRuleDTO rule;
    private List<PropertySetAction> action;

    public RuleConfigDTO(ComboRuleDTO rule) {
        this.rule = rule;
    }

    public static class PropertySetAction {
        private String deviceId;
        private String propCode;
        private String value;
    }
}
