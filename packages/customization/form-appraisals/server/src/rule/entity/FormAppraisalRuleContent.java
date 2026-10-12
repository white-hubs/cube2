package com.guhecloud.cube.srv.gen.appraisals.server.rule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalContentTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalSourceTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 考核规则内容实体类
 * 存储考核规则的具体内容项
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("form_appraisal_rule_content")
public class FormAppraisalRuleContent extends BasicEnhancedTenantEntity<Long> {
    /**
     * 内容 ID（主键）
     */
    @TableId
    private Long id;
    /**
     * 关联的规则 ID
     */
    private Long ruleId;
    /**
     * 内容类型（如检查项、评分项等）
     */
    private AppraisalContentTypeEnum contentType;
    /**
     * 来源类型（数据来源的类型）
     */
    private AppraisalSourceTypeEnum sourceType;
    /**
     * 来源引用 ID（关联的外部数据 ID）
     */
    private String sourceRefId;
    /**
     * 来源引用 code
     */
    private String sourceRefCode;
    /**
     * 来源引用名称
     */
    private String sourceRefName;
    /**
     * 内容描述
     */
    private String contentDesc;
    /**
     * 标准描述（评分标准说明）
     */
    private String standardDesc;
    /**
     * 排序号
     */
    private Integer sort;
}
