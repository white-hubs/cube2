package com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.Entity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 流程实例
 * @date 2023/5/23 17:33
 * {"SELF:start_user_id","DEPT:organization_id"}
 */
@Data
@DataPermission({
        @DataPermissionField(permission = "SELF", field = "start_user_id"),
        @DataPermissionField(permission = "DEPT", field = "organization_id"),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "organization_id")
}
)
@TableName("all_process_instance")
public class ProcessInstancePO implements Entity<Long> {
    private Long id;
    /**
     * 处理状态
     */
    private String state;
    /**
     * 租户
     */
    private Integer tenant;
    /**
     * 流程实例id
     */
    private String procInstId;
    /**
     * 处理人
     */
    private String assignee;
    /**
     * 申请名称
     */
    private String applyName;
    /**
     * 发起人信息
     */
    private String startUserInfo;
    /**
     * 业务模块
     */
    private String businessModel;
    /**
     * 业务类型信息
     */
    private String businessType;
    /**
     * 发起时间
     */
    private LocalDateTime startTime;
    /**
     * 任务开始时间
     */
    private LocalDateTime taskCreateTime;
    /**
     * 任务结束时间
     */
    private LocalDateTime taskEndTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 任务类型 approve:审批,copy:抄送
     */
    private String type;
    /**
     * 抄送用户
     */
    private String copyUser;
    /**
     * 发起人id
     */
    private String startUserId;
    /**
     * 发起人姓名
     */
    private String startUserName;
    /**
     * 发起人部门id
     */
    private Long organizationId;
    /**
     * 发起人部门名称
     */
    private String organizationName;

}