package com.guhecloud.cube.srv.gen.appraisals.server.record.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
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
 * 表单考核预览记录实体。
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("form_appraisal_preview_record")
public class FormAppraisalPreviewRecord extends BasicEnhancedTenantEntity<Long> {
    @TableId
    private Long id;
    private Long ruleId;
    private Integer ruleVersion;
    private String name;
    private String inspectionItem;
    private AppraisalPeriodTypeEnum periodType;
    private Integer assessmentYear;
    private Integer assessmentPeriod;
    private LocalDateTime assessmentBeginTime;
    private LocalDateTime assessmentEndTime;
    private String responsibleDepartmentIds;
    private String responsibleDepartmentNames;
    private Long firstLevelFormGroupId;
    private String firstLevelFormGroupName;
    private Long secondLevelFormGroupId;
    private String secondLevelFormGroupName;
    private BigDecimal originTotalScore;
    private BigDecimal originSpotCheckScore;
    private BigDecimal originInspectionScore;
    private BigDecimal finalTotalScore;
    private BigDecimal finalSpotCheckScore;
    private BigDecimal finalInspectionScore;
    private LocalDateTime previewAsOfTime;
    private LocalDateTime generatedAt;
}
