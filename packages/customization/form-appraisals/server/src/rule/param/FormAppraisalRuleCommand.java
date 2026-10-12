
package com.guhecloud.cube.srv.gen.appraisals.server.rule.param;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dto.ContentCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 考核规则命令对象
 * 用于创建或更新考核规则
 */
@Data
public class FormAppraisalRuleCommand {
    /**
     * 规则 ID（更新时使用）
     */
    private Long id;
    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空")
    private String name;
    /**
     * 检查项目
     */
    @NotBlank(message = "检查项目不能为空")
    private String inspectionItem;
    /**
     * 考核周期类型（如月度、季度、年度等）
     */
    @NotNull(message = "周期类型不能为空")
    private AppraisalPeriodTypeEnum periodType;
    /**
     * 总分值
     */
    @NotNull(message = "总分不能为空")
    private BigDecimal totalScore;
    /**
     * 抽查分值
     */
    @NotNull(message = "抽查分不能为空")
    private BigDecimal spotCheckScore;
    /**
     * 检查分值
     */
    @NotNull(message = "检查分不能为空")
    private BigDecimal inspectionScore;
    /**
     * 责任部门 ID 列表
     */
    @NotNull(message = "责任部门不能为空")
    private List<Long> responsibleDepartmentIds;
    /**
     * 所属流程表单分组 ID
     */
    private Long formGroupId;
    /**
     * 是否启用
     */
    @NotNull(message = "启用状态不能为空")
    private Boolean enabled;
    /**
     * 规则内容列表
     */
    @Valid
    @NotNull(message = "规则内容不能为空")
    private List<ContentCommand> contents;
}
