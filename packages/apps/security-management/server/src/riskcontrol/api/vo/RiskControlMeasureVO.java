package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import lombok.Data;

import java.util.List;

@Data
public class RiskControlMeasureVO {
    /**
     * 管控措施 ID。
     */
    private Long id;

    /**
     * 管控措施 ID，兼容按 measureId 命名的业务字段。
     */
    private Long measureId;

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
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 风险单元引用对象，由 unitId 解析得到；code=单元编号，name=单元名称。
     */
    private RiskOptionVO unit;

    /**
     * 风险对象最新固有风险等级。
     */
    private String inherentRiskLevel;

    /**
     * 风险对象最新控制评估风险等级。
     */
    private String controlRiskLevel;

    /**
     * 管控类型。
     */
    private String controlType;

    /**
     * 管控层级。
     */
    private String controlLevel;

    /**
     * 措施执行人用户 ID。
     */
    private String executorUserId;

    /**
     * 措施执行人引用对象，由 executorUserId 解析得到。
     */
    private RiskUserRefVO executorUser;

    /**
     * 管控措施内容。
     */
    private String content;

    /**
     * 措施执行状态，取值：PENDING=待执行，EXECUTING=执行中，COMPLETED=已完成。
     */
    private String executionStatus;

    /**
     * 措施执行记录列表；措施详情接口返回。
     */
    private List<RiskMeasureExecutionRecordVO> executionRecords;
}
