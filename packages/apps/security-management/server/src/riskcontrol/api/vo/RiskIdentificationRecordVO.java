package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RiskIdentificationRecordVO {
    /**
     * 辨识记录 ID。
     */
    private Long id;

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
     * 辨识轮次号。
     */
    private String identifyRoundNo;

    /**
     * 操作类型，取值：CREATE=新增风险对象，UPDATE=修改风险对象，DELETE=删除风险对象。
     */
    private String operationType;

    /**
     * 辨识人员用户 ID。
     */
    private String identifierUserId;

    /**
     * 辨识人员引用对象，由 identifierUserId 解析得到。
     */
    private RiskUserRefVO identifierUser;

    /**
     * 辨识提交时间。
     */
    private LocalDateTime submitTime;

    /**
     * 快照 JSON，用于历史追溯。
     */
    private String snapshotJson;

    /**
     * 审核方式，取值：SINGLE=单条审核，BATCH=批量审核。
     */
    private String reviewMode;

    /**
     * 批量审核批次号。
     */
    private String reviewBatchNo;

    /**
     * 审核结果，取值：APPROVED=审核通过，REJECTED=审核退回；未审核为空。
     */
    private String reviewResult;

    /**
     * 审核退回原因。
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
}
