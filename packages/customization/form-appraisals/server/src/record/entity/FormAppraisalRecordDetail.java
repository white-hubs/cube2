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
 * 考核记录明细实体类
 * 存储考核记录的明细项信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("form_appraisal_record_detail")
public class FormAppraisalRecordDetail extends BasicEnhancedTenantEntity<Long> {
    /**
     * 明细 ID（主键）
     */
    @TableId
    private Long id;
    /**
     * 关联的记录 ID
     */
    private Long recordId;
    /**
     * 关联的规则内容 ID
     */
    private Long ruleContentId;
    /**
     * 关联的规则项 ID
     */
    private Long ruleItemId;
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
     * 系统生成扣分原因 JSON 快照
     */
    private String deductionReasonJson;
    /**
     * 原始扣分值（系统自动计算）
     */
    private BigDecimal originDeductScore;
    /**
     * 人工追加扣分值
     */
    private BigDecimal manualAdjustDeductScore;
    /**
     * 最终扣分值（原始扣分 + 人工追加）
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
