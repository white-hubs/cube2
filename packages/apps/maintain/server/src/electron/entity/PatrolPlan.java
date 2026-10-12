package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.maintain.server.common.filter.MaintainCurrentUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "visible_user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "visible_org_id",
                             fieldFilter = MaintainCurrentUserDeptFieldFilter.class
        ),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "visible_org_id")
}
)
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("patrol_plan")
public class PatrolPlan extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 计划状态
     */
    private String status;
    /**
     * 计划生效时间
     */
    private LocalDateTime startData;
    /**
     * 计划结束时间
     */
    private LocalDateTime endData;
    /**
     * 巡更周期（单次：one；循环：circulate）
     */
    private String patrolCycle;
    /**
     * 巡更日期类型（周：week；月：month）
     */
    private String patrolDataType;
    /**
     * 巡更日期
     */
    private String patrolData;
    /**
     * 执行时间段
     */
    private String executeData;
    /**
     * 巡更路线id
     */
    private Long routeId;
    /**
     * 是否指派当班
     */
    private Boolean onDuty;
    /**
     * 是否按照顺序执行
     */
    private Boolean inOrder;
    private String userId;
    /**
     * 最近一次生成任务时间
     */
    private LocalDateTime lastDate;
    /**
     * 巡更方式
     */
    private String patrolWay;
    /**
     * 权限人员
     */
    private Long visibleUserId;
    /**
     * 权限人员
     */
    private Long visibleOrgId;
    /**
     * 异常时发放人员
     */
    private String exceptionUserIds;

    /**
     * 巡更条件类型 PatrolParticipateTypeEnum
     */
    private String conditionType;
}
