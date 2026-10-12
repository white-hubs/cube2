package com.guhecloud.cube.biz.emergency.dcm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.LogicalOpEnum;
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
public class DcmEmergencyRuleCond {

    /**
     * 设备产品名称
     */
    private String deviceProdName;

    /**
     * 产品品牌名称
     */
    private String brandName;

    /**
     * 产品型号名称
     */
    private String modelName;

    /**
     * 条件类型(事件或者自定条件)
     */
    private EmergencyConditionTypeEnum conditionType;

    /**
     * 设备事件编号
     */
    private String deviceEventCode;

    /**
     * 逻辑运算符（or and）
     */
    private LogicalOpEnum logicalOp = LogicalOpEnum.OR;

    /**
     * 预案条件表达式列表
     */
    private List<DcmEmergencyRuleCondExp> emergencyRuleConditionExps = Collections.emptyList();
}
