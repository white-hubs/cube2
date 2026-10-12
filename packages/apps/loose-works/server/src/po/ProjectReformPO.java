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
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/13 9:33
 * @Description: 工程整改单
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_reform")
public class ProjectReformPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 工程监管id
     */
    private String projectScheduleId;

    /**
     * 工程监管名称
     */
    private String projectScheduleName;

    /**
     * 整改单名称
     */

    private String processInsName;
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
    private String reformNum;
}
