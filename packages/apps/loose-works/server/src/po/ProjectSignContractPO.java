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
 * @describe 工程签约
 * @date 2023/9/14 17:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_sign_contract")
public class ProjectSignContractPO extends BasicEnhancedTenantEntity<Long> {
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
     * 请示id
     */
    private Long applyId;
    /**
     * 申请人
     */
    private Long startUser;
    /**
     * 申请部门
     */
    private Long startDep;
    /**
     * 审批通过时间
     */
    private LocalDateTime passTime;
    /**
     * 审批结果
     */
    private String approveResult;
    /**
     * 工单编号
     */
    private String projectNum;
}
