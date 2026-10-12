package com.guhecloud.cube.srv.gen.appraisals.server.record.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalCalcTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalContentTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalSourceTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * 表单考核预览记录明细实体。
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("form_appraisal_preview_record_detail")
public class FormAppraisalPreviewRecordDetail extends BasicEnhancedTenantEntity<Long> {
    @TableId
    private Long id;
    private Long recordId;
    private Long ruleContentId;
    private Long ruleItemId;
    private AppraisalContentTypeEnum contentType;
    private AppraisalSourceTypeEnum sourceType;
    private String sourceRefId;
    private String sourceRefName;
    private String contentDesc;
    private String standardDesc;
    private String itemName;
    private String fieldKey;
    private String fieldName;
    private AppraisalCalcTypeEnum calcType;
    private String rawValue;
    private String matchedRangeText;
    private String deductionReasonJson;
    private BigDecimal originDeductScore;
    private BigDecimal manualAdjustDeductScore;
    private BigDecimal finalDeductScore;
    private String remark;
    private Integer sort;
}
