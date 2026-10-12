package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RiskReviewRecordVO {
    /**
     * 审核记录 ID。
     */
    private Long id;

    /**
     * 风险对象 ID。
     */
    private Long objectId;

    /**
     * 辨识轮次号。
     */
    private String identifyRoundNo;

    /**
     * 审核方式，取值：SINGLE=单条审核，BATCH=批量审核。
     */
    private String reviewMode;

    /**
     * 批量审核批次号。
     */
    private String reviewBatchNo;

    /**
     * 审核结果，取值：APPROVED=审核通过，REJECTED=审核退回。
     */
    private String reviewResult;

    /**
     * 审核退回原因。
     */
    private String rejectReason;

    /**
     * 审核意见。
     */
    private String reviewComment;

    /**
     * 审核人用户 ID。
     */
    private String reviewerUserId;

    /**
     * 审核人引用对象，由 reviewerUserId 解析得到。
     */
    private RiskUserRefVO reviewerUser;

    /**
     * 审核时间。
     */
    private LocalDateTime reviewTime;
}
