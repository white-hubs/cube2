package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskWarningSignPageParam extends Paging {
    /**
     * 关键词，按警示标志名称模糊匹配。
     */
    private String keyword;

    /**
     * 警示标志类型筛选。
     */
    private String signType;

    /**
     * 启用状态筛选，true 表示启用，false 表示停用。
     */
    private Boolean enabled;
}
