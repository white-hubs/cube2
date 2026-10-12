package com.guhecloud.cube.srv.gen.appraisals.server.record.vo;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalRecordStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 考核记录详情 VO
 * 用于返回考核记录的完整详情信息（包含明细项）
 */
@Data
public class FormAppraisalRecordDetailVO {
    /**
     * 记录 ID
     */
    private Long id;
    /**
     * 关联的规则 ID
     */
    private Long ruleId;
    /**
     * 规则版本号
     */
    private Integer ruleVersion;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 检查项目
     */
    private String inspectionItem;
    /**
     * 考核周期类型
     */
    private AppraisalPeriodTypeEnum periodType;
    /**
     * 考核年度
     */
    private Integer assessmentYear;
    /**
     * 考核期数
     */
    private Integer assessmentPeriod;
    /**
     * 考核开始时间
     */
    private LocalDateTime assessmentBeginTime;
    /**
     * 考核结束时间
     */
    private LocalDateTime assessmentEndTime;
    /**
     * 责任部门 ID 列表
     */
    private List<Long> responsibleDepartmentIds;
    /**
     * 责任部门名称
     */
    private String responsibleDepartmentNames;
    /**
     * 一级流程表单分组 ID
     */
    private Long firstLevelFormGroupId;
    /**
     * 一级流程表单分组名称
     */
    private String firstLevelFormGroupName;
    /**
     * 二级流程表单分组 ID
     */
    private Long secondLevelFormGroupId;
    /**
     * 二级流程表单分组名称
     */
    private String secondLevelFormGroupName;
    /**
     * 原始总分
     */
    private BigDecimal originTotalScore;
    /**
     * 原始抽查分
     */
    private BigDecimal originSpotCheckScore;
    /**
     * 原始检查分
     */
    private BigDecimal originInspectionScore;
    /**
     * 覆盖后的最终总分
     */
    private BigDecimal overrideFinalTotalScore;
    /**
     * 覆盖后的最终抽查分
     */
    private BigDecimal overrideFinalSpotCheckScore;
    /**
     * 覆盖后的最终检查分
     */
    private BigDecimal overrideFinalInspectionScore;
    /**
     * 最终总分
     */
    private BigDecimal finalTotalScore;
    /**
     * 最终抽查分
     */
    private BigDecimal finalSpotCheckScore;
    /**
     * 最终检查分
     */
    private BigDecimal finalInspectionScore;
    /**
     * 记录状态
     */
    private AppraisalRecordStatusEnum status;
    /**
     * 生成时间
     */
    private LocalDateTime generatedAt;
    /**
     * 调整备注
     */
    private String adjustRemark;
    /**
     * 明细项列表
     */
    private List<FormAppraisalRecordDetailItemVO> details;
}
