package com.guhecloud.cube.srv.gen.appraisals.server.rule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalCalcTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 考核规则项实体类
 * 存储考核规则的评分项明细
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("form_appraisal_rule_item")
public class FormAppraisalRuleItem extends BasicEnhancedTenantEntity<Long> {
    /**
     * 规则项 ID（主键）
     */
    @TableId
    private Long id;
    /**
     * 关联的规则 ID
     */
    private Long ruleId;
    /**
     * 关联的内容 ID
     */
    private Long contentId;
    /**
     * 规则项名称
     */
    private String itemName;
    /**
     * 字段标识（对应的数据字段 key）
     */
    private String fieldKey;
    /**
     * 字段标识（对应的数据字段 名称）
     */
    private String fieldName;
    /**
     * 计算方式（如扣分、加分等）
     */
    private AppraisalCalcTypeEnum calcType;
    /**
     * 区间配置 JSON 字符串
     */
    private String rangeConfigJson;
    /**
     * 排序号
     */
    private Integer sort;
}
