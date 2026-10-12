package com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EventConfigTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.LogicalOpEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 预案规则条件
 *
 * @author lanchun
 * @date 2022/9/3 13:35
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmergencyRuleConditionInfo {

    /**
     * 条件组主键
     */
    private Long groupId;

    /**
     * 产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     * 产品品类编号序列号
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

    /**
     * 条件类型(事件或者自定条件)
     */
    private EmergencyConditionTypeEnum conditionType;

    /**
     * 设备事件主键
     */
    private String deviceEventId;

    /**
     * 设备事件编号
     */
    private String deviceEventCode;

    /**
     * 设备事件名称
     */
    private String deviceEventName;

    /**
     * 设备事件类型
     */
    private String deviceEventType;

    /**
     * 逻辑运算符（or and）
     */
    private LogicalOpEnum logicalOp;

    /**
     * 事件配置类型
     */
    private EventConfigTypeEnum eventConfigType;

    /**
     * 条件表达式列表
     */
    private List<EmergencyRuleConditionExpInfo> emergencyRuleConditionExps;

    public void settingEmergencyRuleConditionExps(List<EmergencyRuleConditionExpInfo> emergencyRuleConditionExpInfos) {
        this.emergencyRuleConditionExps = emergencyRuleConditionExpInfos;
    }
}
