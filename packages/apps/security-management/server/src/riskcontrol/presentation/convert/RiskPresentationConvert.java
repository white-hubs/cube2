package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.presentation.convert;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUnitInventoryVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitEntity;

public final class RiskPresentationConvert {
    private RiskPresentationConvert() {
    }

    public static RiskUnitInventoryVO toUnitInventoryVO(RiskUnitEntity entity) {
        RiskUnitInventoryVO vo = new RiskUnitInventoryVO();
        vo.setId(entity.getId());
        vo.setUnitId(entity.getId());
        vo.setCode(entity.getUnitCode());
        vo.setName(entity.getUnitName());
        vo.setAreaId(entity.getAreaId());
        vo.setUnitTypeId(entity.getUnitTypeId());
        vo.setResponsibleUserId(entity.getResponsibleUserId());
        vo.setIdentifyCycleDays(entity.getIdentifyCycleDays());
        vo.setIdentifyStatus(entity.getIdentifyStatus());
        vo.setInherentRiskLevel(entity.getInherentRiskLevel());
        vo.setControlRiskLevel(entity.getControlRiskLevel());
        vo.setControlStatus(entity.getControlStatus());
        vo.setNextIdentifyTime(entity.getNextIdentifyTime());
        vo.setGisPoint(entity.getGisPoint());
        vo.setLocationDesc(entity.getLocationDesc());
        vo.setDescription(entity.getDescription());
        return vo;
    }
}
