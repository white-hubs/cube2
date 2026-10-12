package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

@Data
public class RiskUnitTypeParam {
    /**
     * 单元类型编号；新增时未传则后端按 RUT-{id} 生成，修改时未传保持原编号。
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
