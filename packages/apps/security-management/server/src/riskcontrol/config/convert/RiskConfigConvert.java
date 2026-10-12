package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.convert;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentDimensionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentLevelRuleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentMethodParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentOptionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitTypeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningSignParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentDimensionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentLevelRuleVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentMethodVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUnitTypeVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskWarningSignVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentDimensionEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentLevelRuleEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentMethodEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentOptionEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskUnitTypeEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskWarningSignEntity;

public final class RiskConfigConvert {
    private RiskConfigConvert() {
    }

    public static RiskWarningSignEntity toWarningSignEntity(RiskWarningSignParam param) {
        RiskWarningSignEntity entity = new RiskWarningSignEntity();
        updateWarningSignEntity(entity, param);
        return entity;
    }

    public static void updateWarningSignEntity(RiskWarningSignEntity entity, RiskWarningSignParam param) {
        entity.setSignCode(param.getCode());
        entity.setSignName(param.getName());
        entity.setSignType(param.getSignType());
        entity.setIconFileId(param.getIconFileId());
        entity.setEnabled(param.getEnabled() == null || param.getEnabled());
    }

    public static RiskWarningSignVO toWarningSignVO(RiskWarningSignEntity entity) {
        RiskWarningSignVO vo = new RiskWarningSignVO();
        vo.setId(entity.getId());
        vo.setCode(entity.getSignCode());
        vo.setName(entity.getSignName());
        vo.setSignType(entity.getSignType());
        vo.setIconFileId(entity.getIconFileId());
        vo.setEnabled(entity.getEnabled());
        return vo;
    }

    public static RiskUnitTypeEntity toUnitTypeEntity(RiskUnitTypeParam param) {
        RiskUnitTypeEntity entity = new RiskUnitTypeEntity();
        updateUnitTypeEntity(entity, param);
        return entity;
    }

    public static void updateUnitTypeEntity(RiskUnitTypeEntity entity, RiskUnitTypeParam param) {
        entity.setTypeCode(param.getCode());
        entity.setTypeName(param.getName());
        entity.setDefaultIdentifyCycleDays(param.getIdentifyCycleDays());
        entity.setIdentifyDimensionDesc(param.getIdentifyDimensionDesc());
    }

    public static RiskUnitTypeVO toVO(RiskUnitTypeEntity entity) {
        RiskUnitTypeVO vo = new RiskUnitTypeVO();
        vo.setId(entity.getId());
        vo.setCode(entity.getTypeCode());
        vo.setName(entity.getTypeName());
        vo.setIdentifyCycleDays(entity.getDefaultIdentifyCycleDays());
        vo.setIdentifyDimensionDesc(entity.getIdentifyDimensionDesc());
        return vo;
    }

    public static RiskAssessmentMethodEntity toMethodEntity(RiskAssessmentMethodParam param) {
        RiskAssessmentMethodEntity entity = new RiskAssessmentMethodEntity();
        updateMethodEntity(entity, param);
        entity.setPresetFlag(false);
        return entity;
    }

    public static void updateMethodEntity(RiskAssessmentMethodEntity entity, RiskAssessmentMethodParam param) {
        entity.setMethodCode(param.getCode());
        entity.setMethodName(param.getName());
        entity.setMethodDesc(param.getDescription());
        entity.setFormula(param.getFormula());
        entity.setEnabled(param.getEnabled() == null || param.getEnabled());
    }

    public static RiskAssessmentMethodVO toVO(RiskAssessmentMethodEntity entity) {
        RiskAssessmentMethodVO vo = new RiskAssessmentMethodVO();
        vo.setId(entity.getId());
        vo.setCode(entity.getMethodCode());
        vo.setName(entity.getMethodName());
        vo.setDescription(entity.getMethodDesc());
        vo.setFormula(entity.getFormula());
        vo.setPresetFlag(entity.getPresetFlag());
        vo.setEnabled(entity.getEnabled());
        return vo;
    }

    public static RiskAssessmentDimensionEntity toDimensionEntity(Long methodId, RiskAssessmentDimensionParam param) {
        RiskAssessmentDimensionEntity entity = new RiskAssessmentDimensionEntity();
        entity.setMethodId(methodId);
        entity.setDimensionCode(param.getCode());
        entity.setDimensionName(param.getName());
        entity.setScoreRange(param.getScoreRange());
        entity.setSortNo(param.getSortNo());
        return entity;
    }

    public static RiskAssessmentDimensionVO toVO(RiskAssessmentDimensionEntity entity) {
        RiskAssessmentDimensionVO vo = new RiskAssessmentDimensionVO();
        vo.setId(entity.getId());
        vo.setMethodId(entity.getMethodId());
        vo.setCode(entity.getDimensionCode());
        vo.setName(entity.getDimensionName());
        vo.setScoreRange(entity.getScoreRange());
        vo.setSortNo(entity.getSortNo());
        return vo;
    }

    public static RiskAssessmentOptionEntity toOptionEntity(Long dimensionId, RiskAssessmentOptionParam param) {
        RiskAssessmentOptionEntity entity = new RiskAssessmentOptionEntity();
        entity.setDimensionId(dimensionId);
        entity.setOptionName(param.getName());
        entity.setScore(param.getScore());
        entity.setSortNo(param.getSortNo());
        return entity;
    }

    public static RiskAssessmentOptionVO toVO(RiskAssessmentOptionEntity entity) {
        RiskAssessmentOptionVO vo = new RiskAssessmentOptionVO();
        vo.setId(entity.getId());
        vo.setDimensionId(entity.getDimensionId());
        vo.setName(entity.getOptionName());
        vo.setScore(entity.getScore());
        vo.setSortNo(entity.getSortNo());
        return vo;
    }

    public static RiskAssessmentLevelRuleEntity toLevelRuleEntity(Long methodId, RiskAssessmentLevelRuleParam param) {
        RiskAssessmentLevelRuleEntity entity = new RiskAssessmentLevelRuleEntity();
        entity.setMethodId(methodId);
        entity.setRiskLevel(param.getRiskLevel());
        entity.setMinScore(param.getMinScore());
        entity.setMaxScore(param.getMaxScore());
        entity.setColor(param.getColor());
        entity.setControlRequirement(param.getControlRequirement());
        return entity;
    }

    public static RiskAssessmentLevelRuleVO toVO(RiskAssessmentLevelRuleEntity entity) {
        RiskAssessmentLevelRuleVO vo = new RiskAssessmentLevelRuleVO();
        vo.setId(entity.getId());
        vo.setMethodId(entity.getMethodId());
        vo.setRiskLevel(entity.getRiskLevel());
        vo.setMinScore(entity.getMinScore());
        vo.setMaxScore(entity.getMaxScore());
        vo.setColor(entity.getColor());
        vo.setControlRequirement(entity.getControlRequirement());
        return vo;
    }

    public static RiskOptionVO toOption(RiskWarningSignEntity entity) {
        RiskOptionVO option = new RiskOptionVO(entity.getId(), entity.getSignCode(), entity.getSignName());
        option.setIconFileId(entity.getIconFileId());
        return option;
    }

    public static RiskOptionVO toOption(RiskUnitTypeEntity entity) {
        return new RiskOptionVO(entity.getId(), entity.getTypeCode(), entity.getTypeName());
    }

    public static RiskOptionVO toOption(RiskAssessmentMethodEntity entity) {
        return new RiskOptionVO(entity.getId(), entity.getMethodCode(), entity.getMethodName());
    }
}
