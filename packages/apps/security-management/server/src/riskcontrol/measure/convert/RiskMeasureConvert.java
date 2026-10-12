package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.convert;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskControlMeasureParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskMeasureExecutionRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskControlMeasureVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskMeasureExecutionRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskMeasureExecutionStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskControlMeasureEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskMeasureAttachmentEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskMeasureExecutionRecordEntity;

import java.time.LocalDateTime;

public final class RiskMeasureConvert {
    private RiskMeasureConvert() {
    }

    public static RiskControlMeasureEntity toMeasureEntity(Long objectId, RiskControlMeasureParam param) {
        RiskControlMeasureEntity entity = new RiskControlMeasureEntity();
        entity.setObjectId(objectId);
        entity.setControlType(param.getControlType());
        entity.setControlLevel(param.getControlLevel());
        entity.setExecutorUserId(param.getExecutorUserId());
        entity.setContent(param.getContent());
        entity.setExecutionStatus(RiskMeasureExecutionStatusEnum.PENDING.name());
        return entity;
    }

    public static RiskControlMeasureVO toVO(RiskControlMeasureEntity entity) {
        RiskControlMeasureVO vo = new RiskControlMeasureVO();
        vo.setId(entity.getId());
        vo.setMeasureId(entity.getId());
        vo.setObjectId(entity.getObjectId());
        vo.setControlType(entity.getControlType());
        vo.setControlLevel(entity.getControlLevel());
        vo.setExecutorUserId(entity.getExecutorUserId());
        vo.setContent(entity.getContent());
        vo.setExecutionStatus(entity.getExecutionStatus());
        return vo;
    }

    public static RiskMeasureExecutionRecordEntity toExecutionRecordEntity(Long measureId, RiskMeasureExecutionRecordParam param) {
        RiskMeasureExecutionRecordEntity entity = new RiskMeasureExecutionRecordEntity();
        entity.setMeasureId(measureId);
        entity.setExecutionResult(param.getExecutionResult());
        entity.setExecutionDesc(param.getExecutionDesc());
        entity.setExecutorUserId(param.getExecutorUserId());
        entity.setExecutionTime(LocalDateTime.now());
        entity.setGpsLocation(param.getGpsLocation());
        return entity;
    }

    public static RiskMeasureExecutionRecordVO toVO(RiskMeasureExecutionRecordEntity entity) {
        RiskMeasureExecutionRecordVO vo = new RiskMeasureExecutionRecordVO();
        vo.setId(entity.getId());
        vo.setMeasureId(entity.getMeasureId());
        vo.setExecutionResult(entity.getExecutionResult());
        vo.setExecutionDesc(entity.getExecutionDesc());
        vo.setExecutorUserId(entity.getExecutorUserId());
        vo.setExecutionTime(entity.getExecutionTime());
        vo.setGpsLocation(entity.getGpsLocation());
        return vo;
    }

    public static RiskMeasureAttachmentEntity toAttachmentEntity(Long executionRecordId, String fileId) {
        RiskMeasureAttachmentEntity entity = new RiskMeasureAttachmentEntity();
        entity.setExecutionRecordId(executionRecordId);
        entity.setFileId(fileId);
        return entity;
    }
}
