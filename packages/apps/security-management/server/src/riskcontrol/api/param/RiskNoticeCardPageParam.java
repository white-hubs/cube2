package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskNoticeCardPageParam extends Paging {
    /**
     * 关键词，按风险单元名称模糊匹配。
     */
    private String keyword;

    /**
     * 风险单元 ID 筛选。
     */
    private Long unitId;

    /**
     * 现状风险等级筛选，取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String riskLevel;

    /**
     * 责任部门 ID 筛选；按风险单元所属区域的 departmentId 过滤。
     */
    private String departmentId;

    /**
     * 输出格式；导出接口固定传 xlsx。
     */
    private String format;
}
