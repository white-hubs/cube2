package com.guhecloud.cube.biz.emergency.server.facade.iot.dto;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.CompareOpEnum;
import lombok.Data;

@Data
public class EventDefinitionPropDTO {

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

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
}
