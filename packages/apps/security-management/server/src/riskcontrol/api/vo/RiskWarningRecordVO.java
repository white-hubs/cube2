package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RiskWarningRecordVO {
    /**
     * 预警记录 ID。
     */
    private Long id;

    /**
     * 预警规则 ID。
     */
    private Long ruleId;

    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 风险单元引用对象，由 unitId 解析得到；code=单元编号，name=单元名称。
     */
    private RiskOptionVO unit;

    /**
     * 风险对象 ID。
     */
    private Long objectId;

    /**
     * 风险对象编号。
     */
    private String objectCode;

    /**
     * 风险对象名称。
     */
    private String objectName;

    /**
     * 预警类型，取值：LEVEL_UPGRADE=等级升级预警，ASSESSMENT_OVERDUE=评估超期预警，
     * INDICATOR_EXCEED=指标超标预警。
     */
    private String warningType;

    /**
     * 预警严重程度。
     */
    private String severity;

    /**
     * 预警状态，取值：ACTIVE=待处置或处置中，DISPOSED=已处置。
     */
    private String warningStatus;

    /**
     * 预警触发时间。
     */
    private LocalDateTime triggerTime;

    /**
     * 预警处置时间。
     */
    private LocalDateTime disposeTime;

    /**
     * 预警处置说明。
     */
    private String disposeDesc;

    /**
     * 预警处置结果。
     */
    private String disposeResult;
}
