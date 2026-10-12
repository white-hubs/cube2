package com.guhecloud.cube.srv.gen.official.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.official.management.server.permission.OfficialCurrentUserDeptFieldFilter;
import com.guhecloud.cube.srv.gen.official.management.server.po.model.FlowCommonEntity;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.enumeration.ConditionEnum;
import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项业务表
 */
@Data
@TableName("focus_work")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "merge_refer_users", condition = ConditionEnum.LIKE),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "dept_id", condition = ConditionEnum.LIKE,
                             fieldFilter = OfficialCurrentUserDeptFieldFilter.class
        ),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "dept_id", condition = ConditionEnum.LIKE)
}
)
public class FocusWorkPO extends FlowCommonEntity {

    private String workDesc;//事项描述

    private Long workCategoryId;//事项类型

    private String workCategoryName;

    private String basis;//督办依据

    private String requirement;//工作要求

    private String deptId;//部门ID

    private String deptName;//部门名称

    private String firstUserId;//第一责任人

    private String firstUserName;//姓名

    private String secondUserId;//第二责任人

    private String secondUserName;//姓名

    private LocalDateTime completeTime;//完成时间

    private String completeTimeStr;//保持表单格式的时间

    private LocalDateTime updateCompleteTime;//更新后完成时间

    private String updateCompleteTimeStr;//保持表单格式的时间

    private Float percentage;//进度百分比

    private String status;//事项当前状态

    private String processInstanceId;//流程实例ID

    private String focusWorkStarterId;//督办任务下发人
    /**
     * 牵头领导
     */
    private String leadLeaderUserId;
    private String leadLeaderUserName;

    private String focusWorkStarter;//督办任务下发人
    //融合第一、第二责任人
    private String mergeReferUsers;
    /**
     * 落实情况
     */
    private String practicableDetails;

    public String getMergeReferUsers() {
        String merge = this.firstUserId;
        if (StringUtils.isNotEmpty(this.secondUserId)) {
            merge = merge + "," + this.secondUserId;
        }
        return merge;
    }
}