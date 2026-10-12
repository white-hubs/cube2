package com.guhecloud.cube.iot.mgt.core.data.vo.linkage;

import lombok.Data;

/**
 * 联动条件
 */
@Data
public class SingleLinkConditionMeta {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 规则id(number)
     */
    private String ruleNum;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 联动类别
     */
    private String categoryId;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 属性id
     */
    private String propCode;

    /**
     * 比较类型
     */
    private String compareType;

    /**
     * 比较直
     */
    private Object[] compareValue;

    /**
     * 延时时间,单位分
     */
    private Integer delayTime;

    private String transparentStatement;

    /**
     * 用于决定表达式是否扩展至外围<br/>
     * eg: D1 || D2 || D3 ,当d2匹配成功时，不再向外部求解D1 和 D3
     */
    private boolean extendsOutMeta = true;
}