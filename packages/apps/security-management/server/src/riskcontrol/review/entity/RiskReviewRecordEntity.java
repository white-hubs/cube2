package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.review.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_review_record")
public class RiskReviewRecordEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long objectId;
    private String identifyRoundNo;
    private String reviewMode;
    private String reviewBatchNo;
    private String reviewResult;
    private String rejectReason;
    private String reviewComment;
    private String reviewerUserId;
    private LocalDateTime reviewTime;
}
