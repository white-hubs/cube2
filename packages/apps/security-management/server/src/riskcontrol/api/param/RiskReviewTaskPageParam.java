package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskReviewTaskPageParam extends Paging {
    /**
     * 关键词，按风险对象名称、危险源或事故类型模糊匹配。
     */
    private String keyword;

    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 审核状态筛选。
     * 取值：PENDING_REVIEW=待审核，APPROVED=已通过，REJECTED=已退回；不传或 ALL 表示全部状态。
     */
    private String status;

    /**
     * 辨识人用户 ID。
     */
    private String identifierUserId;

    /**
     * 辨识提交时间开始。
     */
    private LocalDateTime startTime;

    /**
     * 辨识提交时间结束。
     */
    private LocalDateTime endTime;
}
