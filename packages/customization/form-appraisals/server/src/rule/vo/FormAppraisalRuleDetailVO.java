package com.guhecloud.cube.srv.gen.appraisals.server.rule.vo;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 考核规则详情 VO
 * 用于返回考核规则的完整详情信息
 */
@Data
public class FormAppraisalRuleDetailVO {
    /**
     * 规则 ID
     */
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
     * 考核周期类型
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
     * 责任部门 ID 列表
     */
    private List<Long> responsibleDepartmentIds;
    /**
     * 责任部门名称
     */
    private String responsibleDepartmentNames;
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
    /**
     * 规则内容列表
     */
    private List<FormAppraisalRuleContentVO> contents;
}
