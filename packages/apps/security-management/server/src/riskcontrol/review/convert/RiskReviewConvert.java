package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.review.convert;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskReviewRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.review.entity.RiskReviewRecordEntity;

public final class RiskReviewConvert {
    private RiskReviewConvert() {
    }

    public static RiskReviewRecordVO toVO(RiskReviewRecordEntity entity) {
        RiskReviewRecordVO vo = new RiskReviewRecordVO();
        vo.setId(entity.getId());
        vo.setObjectId(entity.getObjectId());
        vo.setIdentifyRoundNo(entity.getIdentifyRoundNo());
        vo.setReviewMode(entity.getReviewMode());
        vo.setReviewBatchNo(entity.getReviewBatchNo());
        vo.setReviewResult(entity.getReviewResult());
        vo.setRejectReason(entity.getRejectReason());
        vo.setReviewComment(entity.getReviewComment());
        vo.setReviewerUserId(entity.getReviewerUserId());
        vo.setReviewTime(entity.getReviewTime());
        return vo;
    }
}
