package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RiskObjectVO {
    /**
     * 风险对象 ID。
     */
    private Long id;

    /**
     * 风险对象 ID，兼容按 objectId 命名的业务字段。
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
     * 风险对象编号，等同 objectCode。
     */
    private String code;

    /**
     * 风险对象名称，等同 objectName。
     */
    private String name;

    /**
     * 所属风险单元 ID。
     */
    private Long unitId;

    /**
     * 所属风险单元引用对象，由 unitId 解析得到；code=单元编号，name=单元名称。
     */
    private RiskOptionVO unit;

    /**
     * 所属区域 ID；评估任务按单元维度返回时使用。
     */
    private Long areaId;

    /**
     * 所属区域引用；评估任务按单元维度返回时使用。
     */
    private RiskOptionVO area;

    /**
     * 风险单元类型 ID；评估任务按单元维度返回时使用。
     */
    private Long unitTypeId;

    /**
     * 风险单元类型引用；评估任务按单元维度返回时使用。
     */
    private RiskOptionVO unitType;

    /**
     * 资产管理服务资产 ID；风险对象仅返回外部资产引用。
     */
    private Long assetId;

    /**
     * 所属辨识轮次号。
     */
    private String identifyRoundNo;

    /**
     * 责任人用户 ID。
     */
    private String responsibleUserId;

    /**
     * 责任人引用对象，由 responsibleUserId 解析得到。
     */
    private RiskUserRefVO responsibleUser;

    /**
     * 辨识人用户 ID；审核任务中取本条辨识提交记录的 identifierUserId。
     */
    private String identifierUserId;

    /**
     * 辨识人引用对象，由 identifierUserId 解析得到。
     */
    private RiskUserRefVO identifierUser;

    /**
     * 状态。
     * 风险对象列表取值：DRAFT=辨识中，PENDING_REVIEW=待审核，INHERENT_EVAL=固有评估，
     * CONTROL_EVAL=控制评估，CONTROL_SET=管控配置，EXECUTING=措施执行中，ACTIVE=管控中，RE_EVAL=待复评。
     * 辨识审核列表取值：PENDING_REVIEW=待审核，APPROVED=已通过，REJECTED=已退回。
     */
    private String status;

    /**
     * 风险单元管控状态；评估任务按单元维度返回时使用。
     */
    private String controlStatus;

    /**
     * 最新固有风险等级。
     */
    private String inherentRiskLevel;

    /**
     * 最新控制风险等级。
     */
    private String controlRiskLevel;

    /**
     * 危险源或风险描述。
     */
    private String hazardDesc;

    /**
     * 事故类型。
     */
    private String accidentTypes;

    /**
     * 事故原因。
     */
    private String accidentCause;

    /**
     * 触发因素。
     */
    private String triggerFactor;

    /**
     * 应急处置措施。
     */
    private String emergencyMeasure;

    /**
     * 辨识提交时间；审核列表中取本条辨识提交记录的提交时间。
     */
    private LocalDateTime submitTime;

    /**
     * 审核结果，取值：APPROVED=审核通过，REJECTED=审核退回；待审核对象为空。
     */
    private String reviewResult;

    /**
     * 审核方式，取值：SINGLE=单条审核，BATCH=批量审核。
     */
    private String reviewMode;

    /**
     * 批量审核批次号。
     */
    private String reviewBatchNo;

    /**
     * 审核退回原因；reviewResult=REJECTED 时返回。
     */
    private String rejectReason;

    /**
     * 审核意见。
     */
    private String reviewComment;

    /**
     * 审核人用户 ID。
     */
    private String reviewerUserId;

    /**
     * 审核人引用对象，由 reviewerUserId 解析得到。
     */
    private RiskUserRefVO reviewerUser;

    /**
     * 审核时间。
     */
    private LocalDateTime reviewTime;

    /**
     * 风险对象关联的管控措施列表；清单、告知卡和措施详情场景返回。
     */
    private List<RiskControlMeasureVO> measures;

    /**
     * 风险对象数量；评估任务按单元维度返回时使用。
     */
    private Long objectCount;

    /**
     * 已完成评估对象数量；评估任务按单元维度返回时使用。
     */
    private Long completedCount;
}
