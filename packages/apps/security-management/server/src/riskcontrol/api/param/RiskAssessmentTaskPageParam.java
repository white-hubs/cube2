package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskAssessmentTaskPageParam extends Paging {
    /**
     * 关键词，按风险单元名称、位置或风险对象名称模糊匹配。
     */
    private String keyword;

    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 所属区域 ID。
     */
    private Long areaId;

    /**
     * 风险等级筛选，取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String riskLevel;

    /**
     * 评估状态筛选，取值：PENDING=待评估，ASSESSING=评估中，COMPLETED=已完成。
     */
    private String status;
}
