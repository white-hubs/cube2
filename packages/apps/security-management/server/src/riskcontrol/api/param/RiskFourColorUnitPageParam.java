package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskFourColorUnitPageParam extends Paging {
    /**
     * 关键词，按风险单元名称模糊匹配。
     */
    private String keyword;

    /**
     * 所属区域 ID 筛选。
     */
    private Long areaId;

    /**
     * 单元类型 ID 筛选。
     */
    private Long unitTypeId;

    /**
     * 现状风险等级筛选，取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String riskLevel;
}
