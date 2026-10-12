package com.guhecloud.cube.biz.emergency.dcm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DcmEmergencyRuleCondGroup {

    /**
     * 规则条件
     */
    private List<DcmEmergencyRuleCond> emergencyRuleCondList = Collections.emptyList();

    /**
     * 规则时段
     */
    private DcmEmergencyRuleTime emergencyRuleTime;
}
