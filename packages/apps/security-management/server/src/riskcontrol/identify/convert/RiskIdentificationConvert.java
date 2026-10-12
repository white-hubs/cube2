package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.convert;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskIdentificationObjectParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskIdentificationRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskObjectVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskObjectStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskIdentificationRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskObjectEntity;

public final class RiskIdentificationConvert {
    private RiskIdentificationConvert() {
    }

    public static RiskObjectEntity toEntity(Long unitId, String roundNo, RiskIdentificationObjectParam param) {
        RiskObjectEntity entity = new RiskObjectEntity();
        updateEntity(entity, param);
        entity.setUnitId(unitId);
        entity.setIdentifyRoundNo(roundNo);
        entity.setStatus(RiskObjectStatusEnum.PENDING_REVIEW.name());
        return entity;
    }

    public static void updateEntity(RiskObjectEntity entity, RiskIdentificationObjectParam param) {
        entity.setObjectName(param.getName());
        entity.setAssetId(param.getAssetId());
        entity.setResponsibleUserId(param.getResponsibleUserId());
        entity.setHazardDesc(param.getHazardDesc());
        entity.setAccidentTypes(param.getAccidentTypes());
        entity.setAccidentCause(param.getAccidentCause());
        entity.setTriggerFactor(param.getTriggerFactor());
        entity.setEmergencyMeasure(param.getEmergencyMeasure());
    }

    public static RiskObjectVO toVO(RiskObjectEntity entity) {
        RiskObjectVO vo = new RiskObjectVO();
        vo.setId(entity.getId());
        vo.setObjectId(entity.getId());
        vo.setObjectCode(entity.getObjectCode());
        vo.setObjectName(entity.getObjectName());
        vo.setCode(entity.getObjectCode());
        vo.setName(entity.getObjectName());
        vo.setUnitId(entity.getUnitId());
        vo.setAssetId(entity.getAssetId());
        vo.setIdentifyRoundNo(entity.getIdentifyRoundNo());
        vo.setResponsibleUserId(entity.getResponsibleUserId());
        vo.setStatus(entity.getStatus());
        vo.setInherentRiskLevel(entity.getInherentRiskLevel());
        vo.setControlRiskLevel(entity.getControlRiskLevel());
        vo.setHazardDesc(entity.getHazardDesc());
        vo.setAccidentTypes(entity.getAccidentTypes());
        vo.setAccidentCause(entity.getAccidentCause());
        vo.setTriggerFactor(entity.getTriggerFactor());
        vo.setEmergencyMeasure(entity.getEmergencyMeasure());
        return vo;
    }

    public static RiskIdentificationRecordVO toVO(RiskIdentificationRecordEntity entity) {
        RiskIdentificationRecordVO vo = new RiskIdentificationRecordVO();
        vo.setId(entity.getId());
        vo.setObjectId(entity.getObjectId());
        vo.setUnitId(entity.getUnitId());
        vo.setIdentifyRoundNo(entity.getIdentifyRoundNo());
        vo.setOperationType(entity.getOperationType());
        vo.setIdentifierUserId(entity.getIdentifierUserId());
        vo.setSubmitTime(entity.getSubmitTime());
        vo.setSnapshotJson(entity.getSnapshotJson());
        return vo;
    }
}
