package com.guhecloud.cube.base.flow.flowable.resolver.enumeration;

import lombok.Getter;

/**
 * @Author:LoveMyOrange
 * @Description: 申报人设置
 * @Date:Created in 2022/10/9 18:15
 */
@Getter
public enum AssigneeTypeEnums {
    ASSIGN_USER("指定人员", "getAssignUser"),
    SELF_SELECT("发起人自选", null),
    LEADER_TOP("直属领导", null),
    LEADER("领导", null),
    ROLE("角色", "getRoleUser"),
    SELF("发起人本人", "getSelfUser"),
    FORM_USER("表单内用户", "getFormUser"),
    POST("职位+管理部门", "getPostUser"),
    SINGLE_POST("单职位", "getPostSingleUser"),
    DEP_POST("部门+岗位", "getDepPostUser"),
    DEP_POSITION("部门+职位", "getDepPositionUser"),
    EMPTY("空节点", null),
    APPOINTMENT("岗位+管理部门", "getAppointmentUser"),
    SINGLE_APPOINTMENT("单岗位", "getAppointmentSingletUser"),
    PREV_ASSIGNEE("上一节点审批人", "getPrevAssigneeUser");

    private final String typeName;
    private final String methName;

    AssigneeTypeEnums(String typeName, String methName) {
        this.typeName = typeName;
        this.methName = methName;
    }

}
