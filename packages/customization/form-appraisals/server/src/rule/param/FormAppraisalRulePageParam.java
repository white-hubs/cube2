
package com.guhecloud.cube.srv.gen.appraisals.server.rule.param;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 考核规则分页查询参数
 * 用于分页查询考核规则列表
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FormAppraisalRulePageParam extends Paging {
    /**
     * 关键词搜索（支持规则名称模糊匹配）
     */
    private String keyword;
    /**
     * 考核周期类型筛选
     */
    private AppraisalPeriodTypeEnum periodType;
    /**
     * 责任部门 ID 筛选
     */
    private Long responsibleDepartmentId;
    /**
     * 所属流程表单分组 ID 筛选
     */
    private Long formGroupId;
    /**
     * 启用状态筛选
     */
    private Boolean enabled;
}
