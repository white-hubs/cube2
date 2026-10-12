
package com.guhecloud.cube.srv.gen.appraisals.server.rule.entity;

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

/**
 * 考核规则实体类
 * 存储考核规则的基本信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("form_appraisal_rule")
public class FormAppraisalRule extends BasicEnhancedTenantEntity<Long> {
    /**
     * 规则 ID（主键）
     */
    @TableId
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 检查项目
     */
    private String inspectionItem;
    /**
     * 考核周期类型（如月度、季度、年度等）
     */
    private AppraisalPeriodTypeEnum periodType;
    /**
     * 总分值
     */
    private BigDecimal totalScore;
    /**
     * 抽查分值
     */
    private BigDecimal spotCheckScore;
    /**
     * 检查分值
     */
    private BigDecimal inspectionScore;
    /**
     * 责任部门 ID 列表，逗号分隔
     */
    private String responsibleDepartmentIds;
    /**
     * 所属流程表单分组 ID
     */
    private Long formGroupId;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 规则版本号
     */
    private Integer ruleVersion;
}
