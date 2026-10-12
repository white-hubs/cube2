package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskObjectPageParam extends Paging {
    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 风险对象 ID。
     */
    private Long objectId;

    /**
     * 审核结果筛选；REJECTED 表示查询被退回后待重新提交的对象。
     */
    private String reviewResult;
}
