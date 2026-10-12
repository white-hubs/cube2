package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.convert;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentPlanParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentPlanVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskPlanStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.entity.RiskAssessmentPlanEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.entity.RiskPlanNotifyUserEntity;

public final class RiskPlanConvert {
    private RiskPlanConvert() {
    }

    public static RiskAssessmentPlanEntity toEntity(RiskAssessmentPlanParam param) {
        RiskAssessmentPlanEntity entity = new RiskAssessmentPlanEntity();
        updateEntity(entity, param);
        entity.setStatus(RiskPlanStatusEnum.ENABLED.name());
        return entity;
    }

    public static void updateEntity(RiskAssessmentPlanEntity entity, RiskAssessmentPlanParam param) {
        entity.setUnitId(param.getUnitId());
        entity.setPlanName(param.getPlanName());
        entity.setCycleType(param.getCycleType());
        entity.setNextExecuteTime(param.getNextExecuteTime());
    }

    public static RiskAssessmentPlanVO toVO(RiskAssessmentPlanEntity entity) {
        RiskAssessmentPlanVO vo = new RiskAssessmentPlanVO();
        vo.setId(entity.getId());
        vo.setUnitId(entity.getUnitId());
        vo.setPlanName(entity.getPlanName());
        vo.setCycleType(entity.getCycleType());
        vo.setNextExecuteTime(entity.getNextExecuteTime());
        vo.setLastExecuteTime(entity.getLastExecuteTime());
        vo.setStatus(entity.getStatus());
        return vo;
    }

    public static RiskPlanNotifyUserEntity toNotifyUser(Long planId, String userId) {
        RiskPlanNotifyUserEntity entity = new RiskPlanNotifyUserEntity();
        entity.setPlanId(planId);
        entity.setUserId(userId);
        return entity;
    }
}
