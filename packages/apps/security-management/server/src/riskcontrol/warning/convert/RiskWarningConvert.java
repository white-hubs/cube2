package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.convert;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningRuleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskWarningRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskWarningRuleVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskWarningStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.entity.RiskWarningRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.entity.RiskWarningRuleEntity;

public final class RiskWarningConvert {
    private RiskWarningConvert() {
    }

    public static RiskWarningRuleEntity toRuleEntity(RiskWarningRuleParam param) {
        RiskWarningRuleEntity entity = new RiskWarningRuleEntity();
        updateRuleEntity(entity, param);
        return entity;
    }

    public static void updateRuleEntity(RiskWarningRuleEntity entity, RiskWarningRuleParam param) {
        entity.setWarningType(param.getWarningType());
        entity.setConditionJson(param.getConditionJson());
        entity.setSeverity(param.getSeverity());
        entity.setNotifyChannels(param.getNotifyChannels());
        entity.setDisposeDeadlineDays(param.getDisposeDeadlineDays());
        entity.setEnabled(param.getEnabled() == null || param.getEnabled());
    }

    public static RiskWarningRuleVO toVO(RiskWarningRuleEntity entity) {
        RiskWarningRuleVO vo = new RiskWarningRuleVO();
        vo.setId(entity.getId());
        vo.setWarningType(entity.getWarningType());
        vo.setConditionJson(entity.getConditionJson());
        vo.setSeverity(entity.getSeverity());
        vo.setNotifyChannels(entity.getNotifyChannels());
        vo.setDisposeDeadlineDays(entity.getDisposeDeadlineDays());
        vo.setEnabled(entity.getEnabled());
        return vo;
    }

    public static RiskWarningRecordEntity toRecordEntity(RiskWarningRuleEntity rule, Long unitId, Long objectId) {
        RiskWarningRecordEntity entity = new RiskWarningRecordEntity();
        entity.setRuleId(rule.getId());
        entity.setUnitId(unitId);
        entity.setObjectId(objectId);
        entity.setSeverity(rule.getSeverity());
        entity.setWarningStatus(RiskWarningStatusEnum.ACTIVE.name());
        return entity;
    }

    public static RiskWarningRecordVO toRecordVO(RiskWarningRecordEntity entity) {
        RiskWarningRecordVO vo = new RiskWarningRecordVO();
        vo.setId(entity.getId());
        vo.setRuleId(entity.getRuleId());
        vo.setUnitId(entity.getUnitId());
        vo.setObjectId(entity.getObjectId());
        vo.setSeverity(entity.getSeverity());
        vo.setWarningStatus(entity.getWarningStatus());
        vo.setTriggerTime(entity.getTriggerTime());
        vo.setDisposeTime(entity.getDisposeTime());
        vo.setDisposeDesc(entity.getDisposeDesc());
        vo.setDisposeResult(entity.getDisposeResult());
        return vo;
    }
}
