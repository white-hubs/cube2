package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskWarningRulePageParam extends Paging {
    /**
     * 启用状态筛选，true 表示启用，false 表示停用。
     */
    private Boolean enabled;
}
