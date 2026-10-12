package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备条件规则配置
 *
 * @author lanchun
 * @date 2022/6/23 10:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleDeviceConditionDTO {

    /**
     * 设备产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     *
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 设备产品品类名称
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
     * 设备主键
     */
    private Long id;

    /**
     * 设备主键
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 所属区域
     */
    private String areaId;

    /**
     * 所属区域名称
     */
    private String areaName;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 属性操作符
     */
    private String op;

    /**
     * 属性值
     */
    private String oprand;

    /**
     * 状态持续时延
     */
    private Integer delayTime;
}
