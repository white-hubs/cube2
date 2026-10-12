package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

import java.util.List;

@Data
public class RiskReviewCreateParam {
    /**
     * 单条审核的风险对象 ID；批量审核时使用 objectIds。
     */
    private Long objectId;

    /**
     * 批量审核的风险对象 ID 集合。
     */
    private List<Long> objectIds;

    /**
     * 审核结果，取值：APPROVED=审核通过，REJECTED=审核退回。
     */
    private String reviewResult;

    /**
     * 审核退回原因；reviewResult=REJECTED 时必填。
     */
    private String rejectReason;

    /**
     * 审核意见。
     */
    private String reviewComment;

    /**
     * 审核人用户 ID。
     */
    private String userId;
}
