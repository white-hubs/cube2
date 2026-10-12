package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskUnitPageParam extends Paging {
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
     * 责任人用户 ID 筛选。
     */
    private String responsibleUserId;

    /**
     * 单元辨识状态筛选：IDENTIFYING=辨识中/待辨识，PENDING_REVIEW=待审核，REVIEWED=已审核。
     * 兼容传入 PENDING_IDENTIFY、TO_IDENTIFY、待辨识、辨识中、待审核、审核中、已审核。
     */
    private String status;

    /**
     * 当前风险等级筛选，取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String riskLevel;
}
