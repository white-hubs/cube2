package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.convert;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.entity.RiskAssessmentRecordEntity;

public final class RiskAssessmentConvert {
    private RiskAssessmentConvert() {
    }

    public static RiskAssessmentRecordVO toVO(RiskAssessmentRecordEntity entity) {
        RiskAssessmentRecordVO vo = new RiskAssessmentRecordVO();
        vo.setId(entity.getId());
        vo.setObjectId(entity.getObjectId());
        vo.setMethodId(entity.getMethodId());
        vo.setAssessmentType(entity.getAssessmentType());
        vo.setTotalScore(entity.getTotalScore());
        vo.setRiskLevel(entity.getRiskLevel());
        vo.setMethodSnapshotJson(entity.getMethodSnapshotJson());
        vo.setScoreDetailJson(entity.getScoreDetailJson());
        vo.setSubmitted(entity.getSubmitted());
        return vo;
    }
}
