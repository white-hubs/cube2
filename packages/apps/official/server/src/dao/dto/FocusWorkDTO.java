package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import annotation.FormField;
import lombok.Data;

@Data
public class FocusWorkDTO {
    @FormField(label = "申请人", ignore = true)
    private Long applicantId;
    @FormField(label = "申请人部门", ignore = true)
    private Long applicantDeptId;
    @FormField(label = "督办事项")
    private String workDesc;
    @FormField(label = "督办事项类型")
    private Long workCategoryId;
    @FormField(label = "督办依据")
    private String basis;
    @FormField(label = "工作要求")
    private String requirement;
    @FormField(label = "牵头领导")
    private String leadLeaderUserId;
    @FormField(label = "责任部门")
    private String deptId;
    @FormField(label = "第一责任人")
    private String firstUserId;
    @FormField(label = "第二责任人")
    private String secondUserId;
    @FormField(label = "完成时间")
    private String completeTime;
    @FormField(label = "督办下发人")
    private String focusWorkStarter;
}
