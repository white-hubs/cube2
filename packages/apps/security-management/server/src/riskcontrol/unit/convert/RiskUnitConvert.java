package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.convert;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.area.entity.RiskAreaEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAreaParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAreaVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUnitVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskUnitIdentifyStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitIdentifierRelEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitSignRelEntity;

public final class RiskUnitConvert {
    private RiskUnitConvert() {
    }

    public static RiskAreaEntity toAreaEntity(RiskAreaParam param) {
        RiskAreaEntity entity = new RiskAreaEntity();
        updateAreaEntity(entity, param);
        return entity;
    }

    public static void updateAreaEntity(RiskAreaEntity entity, RiskAreaParam param) {
        entity.setAreaCode(param.getCode());
        entity.setAreaName(param.getName());
        entity.setDepartmentId(param.getDepartmentId());
        entity.setRangeDesc(param.getRangeDesc());
        entity.setDescription(param.getDescription());
        entity.setOwnerUserId(param.getOwnerUserId());
        entity.setAreaType(param.getAreaType());
        entity.setGisGeometry(param.getGisGeometry());
    }

    public static RiskAreaVO toVO(RiskAreaEntity entity) {
        RiskAreaVO vo = new RiskAreaVO();
        vo.setId(entity.getId());
        vo.setCode(entity.getAreaCode());
        vo.setName(entity.getAreaName());
        vo.setDepartmentId(entity.getDepartmentId());
        vo.setRangeDesc(entity.getRangeDesc());
        vo.setDescription(entity.getDescription());
        vo.setOwnerUserId(entity.getOwnerUserId());
        vo.setAreaType(entity.getAreaType());
        vo.setGisGeometry(entity.getGisGeometry());
        vo.setInherentRiskLevel(entity.getInherentRiskLevel());
        vo.setControlRiskLevel(entity.getControlRiskLevel());
        vo.setRiskLevel(entity.getRiskLevel());
        return vo;
    }

    public static RiskUnitEntity toUnitEntity(RiskUnitParam param) {
        RiskUnitEntity entity = new RiskUnitEntity();
        updateUnitEntity(entity, param);
        entity.setIdentifyStatus(RiskUnitIdentifyStatusEnum.IDENTIFYING.name());
        return entity;
    }

    public static void updateUnitEntity(RiskUnitEntity entity, RiskUnitParam param) {
        entity.setUnitCode(param.getCode());
        entity.setUnitName(param.getName());
        entity.setAreaId(param.getAreaId());
        entity.setUnitTypeId(param.getUnitTypeId());
        entity.setResponsibleUserId(param.getResponsibleUserId());
        entity.setIdentifyCycleDays(param.getIdentifyCycleDays());
        entity.setLocationDesc(param.getLocationDesc());
        entity.setDescription(param.getDescription());
        entity.setGisPoint(param.getGisPoint());
    }

    public static RiskUnitVO toVO(RiskUnitEntity entity) {
        RiskUnitVO vo = new RiskUnitVO();
        vo.setId(entity.getId());
        vo.setCode(entity.getUnitCode());
        vo.setName(entity.getUnitName());
        vo.setAreaId(entity.getAreaId());
        vo.setUnitTypeId(entity.getUnitTypeId());
        vo.setResponsibleUserId(entity.getResponsibleUserId());
        vo.setIdentifyCycleDays(entity.getIdentifyCycleDays());
        vo.setIdentifyStatus(entity.getIdentifyStatus());
        vo.setCurrentIdentifyRoundNo(entity.getCurrentIdentifyRoundNo());
        vo.setLastIdentifySubmitTime(entity.getLastIdentifySubmitTime());
        vo.setLastIdentifyReviewTime(entity.getLastIdentifyReviewTime());
        vo.setNextIdentifyTime(entity.getNextIdentifyTime());
        vo.setLocationDesc(entity.getLocationDesc());
        vo.setDescription(entity.getDescription());
        vo.setInherentRiskLevel(entity.getInherentRiskLevel());
        vo.setControlRiskLevel(entity.getControlRiskLevel());
        vo.setRiskLevel(entity.getRiskLevel());
        vo.setControlStatus(entity.getControlStatus());
        vo.setGisPoint(entity.getGisPoint());
        return vo;
    }

    public static RiskUnitSignRelEntity toSignRel(Long unitId, Long signId) {
        RiskUnitSignRelEntity entity = new RiskUnitSignRelEntity();
        entity.setUnitId(unitId);
        entity.setSignId(signId);
        return entity;
    }

    public static RiskUnitIdentifierRelEntity toIdentifierRel(Long unitId, String userId) {
        RiskUnitIdentifierRelEntity entity = new RiskUnitIdentifierRelEntity();
        entity.setUnitId(unitId);
        entity.setUserId(userId);
        return entity;
    }

    public static RiskOptionVO toOption(RiskAreaEntity entity) {
        return new RiskOptionVO(entity.getId(), entity.getAreaCode(), entity.getAreaName());
    }

    public static RiskOptionVO toOption(RiskUnitEntity entity) {
        return new RiskOptionVO(entity.getId(), entity.getUnitCode(), entity.getUnitName());
    }
}
