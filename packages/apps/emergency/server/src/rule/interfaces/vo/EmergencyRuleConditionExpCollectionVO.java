package com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.CompareOpEnum;
import lombok.Data;

/**
 * 预案规则条件表达式列表返回参数
 *
 * @author lanchun
 * @date 2022/9/5 15:04
 */
@Data
public class EmergencyRuleConditionExpCollectionVO {

    private Long id;

    /**
     * 预案规则主键
     */
    private Long emergencyRuleId;

    /**
     * 预案条件主键
     */
    private Long emergencyRuleConditionId;

    /**
     * 条件组主键
     */
    private Long groupId;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 属性单位
     */
    private String propUnit;

    /**
     * 比较符（= >=）
     */
    private CompareOpEnum op;

    /**
     * 属性值
     */
    private String oprand;

    /**
     * 延时时间
     */
    private Integer delayTime;

    /**
     * 延迟开始时间
     */
    private Integer startDelayTime;

    /**
     * 延迟结束时间
     */
    private Integer endDelayTime;
}
