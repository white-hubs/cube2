package com.guhecloud.cube.srv.gen.official.management.server.po.model;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlowCommonEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    private Long applicantId;//申请人ID

    private String applicantName;//申请人姓名

    private Long applicantDeptId;//部门ID

    private String applicantDeptName;//部门名称

    private LocalDateTime applyTime;//申请时间

    private LocalDateTime applyDoneTime;//申请处理完成时间
}
