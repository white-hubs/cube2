package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskUnitTypePageParam extends Paging {
    /**
     * 关键词，按单元类型名称模糊匹配。
     */
    private String keyword;
}
