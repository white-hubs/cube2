package com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EventConfigTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.LogicalOpEnum;
import lombok.Data;

import java.util.List;

/**
 * 报警规则条件列表返回参数
 *
 * @author lanchun
 * @date 2022/9/5 15:05
 */
@Data
public class EmergencyRuleConditionCollectionVO {

    private Long id;

    /**
     * 预案条件组主键
     */
    private Long groupId;

    /**
     * 预案规则主键
     */
    private Long emergencyRuleId;

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
     * 预案规则条件表达式列表
     */
    private List<EmergencyRuleConditionExpCollectionVO> emergencyRuleConditionExps;
}
