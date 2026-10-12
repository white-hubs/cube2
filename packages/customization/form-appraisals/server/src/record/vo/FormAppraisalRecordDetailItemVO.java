package com.guhecloud.cube.srv.gen.appraisals.server.record.vo;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalCalcTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalContentTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalSourceTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 考核记录明细项 VO
 * 用于返回考核记录的明细项信息
 */
@Data
public class FormAppraisalRecordDetailItemVO {
    /**
     * 明细 ID
     */
    private Long id;
    /**
     * 内容类型
     */
    private AppraisalContentTypeEnum contentType;
    /**
     * 来源类型
     */
    private AppraisalSourceTypeEnum sourceType;
    /**
     * 来源引用 ID
     */
    private String sourceRefId;
    /**
     * 来源引用名称
     */
    private String sourceRefName;
    /**
     * 内容描述
     */
    private String contentDesc;
    /**
     * 标准描述
     */
    private String standardDesc;
    /**
     * 规则项名称
     */
    private String itemName;
    /**
     * 字段标识
     */
    private String fieldKey;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 计算方式
     */
    private AppraisalCalcTypeEnum calcType;
    /**
     * 原始采集值
     */
    private String rawValue;
    /**
     * 匹配的区间文本描述
     */
    private String matchedRangeText;
    /**
     * 系统生成扣分原因
     */
    private FormAppraisalDeductionReasonVO deductionReason;
    /**
     * 原始扣分值
     */
    private BigDecimal originDeductScore;
    /**
     * 人工追加扣分值
     */
    private BigDecimal manualAdjustDeductScore;
    /**
     * 最终扣分值
     */
    private BigDecimal finalDeductScore;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序号
     */
    private Integer sort;
}
