package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalCalcTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalContentTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalSourceTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 表单考核明细计算结果。
 */
@Data
public class AppraisalRecordDetailCalculateResult {
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
    private BigDecimal finalDeductScore;
    private Integer sort;
}
