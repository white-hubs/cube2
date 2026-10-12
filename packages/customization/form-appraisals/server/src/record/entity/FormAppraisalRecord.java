
package com.guhecloud.cube.srv.gen.appraisals.server.record.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalRecordStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考核记录实体类
 * 存储考核记录的完整信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("form_appraisal_record")
public class FormAppraisalRecord extends BasicEnhancedTenantEntity<Long> {
    /**
     * 记录 ID（主键）
     */
    @TableId
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
     * 考核期数（如月份、季度等）
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
     * 责任部门 ID 列表，逗号分隔
     */
    private String responsibleDepartmentIds;
    /**
     * 责任部门名称列表，逗号分隔
     */
    private String responsibleDepartmentNames;
    /**
     * 一级流程表单分组 ID 快照
     */
    private Long firstLevelFormGroupId;
    /**
     * 一级流程表单分组名称快照
     */
    private String firstLevelFormGroupName;
    /**
     * 二级流程表单分组 ID 快照
     */
    private Long secondLevelFormGroupId;
    /**
     * 二级流程表单分组名称快照
     */
    private String secondLevelFormGroupName;
    /**
     * 原始总分（规则定义的总分）
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
     * 覆盖后的最终总分（手动调整后）
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
     * 最终总分（优先使用覆盖值，否则使用计算值）
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
}
