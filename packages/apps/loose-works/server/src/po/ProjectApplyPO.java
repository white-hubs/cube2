package com.guhecloud.cube.srv.gen.loose.works.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 工程请示
 * @date 2023/9/14 17:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_apply")
public class ProjectApplyPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 小散工程名称
     */
    private String projectName;
    /**
     * 流程实例id
     */
    private String processInsId;
    /**
     * 申请人
     */
    private Long startUser;
    /**
     * 申请部门
     */
    private Long startDep;
    /**
     * 审批完成时间
     */
    private LocalDateTime passTime;
    /**
     * 审批结果
     */
    private String approveResult;
    /**
     * 工单编号
     */
    private String applyNum;
}