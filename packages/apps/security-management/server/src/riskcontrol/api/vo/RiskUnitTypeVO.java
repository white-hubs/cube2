package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

@Data
public class RiskUnitTypeVO {
    /**
     * 单元类型 ID。
     */
    private Long id;

    /**
     * 单元类型编号。
     */
    private String code;

    /**
     * 单元类型名称。
     */
    private String name;

    /**
     * 默认辨识周期，单位为天。
     */
    private Integer identifyCycleDays;

    /**
     * 辨识维度说明。
     */
    private String identifyDimensionDesc;
}
