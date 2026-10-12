package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskAssessmentMethodPageParam extends Paging {
    /**
     * 关键词，按评估方法编号或名称模糊匹配。
     */
    private String keyword;

    /**
     * 启用状态筛选，true 表示启用，false 表示停用。
     */
    private Boolean enabled;
}
