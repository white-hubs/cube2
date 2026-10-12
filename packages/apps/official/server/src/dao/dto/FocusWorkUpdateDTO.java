package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import annotation.FormField;
import lombok.Data;

@Data
public class FocusWorkUpdateDTO {
    @FormField(label = "申请人", ignore = true)
    private Long applicantId;
    @FormField(label = "申请人部门", ignore = true)
    private Long applicantDeptId;
    @FormField(label = "变更结果")
    private String status;
    @FormField(label = "变更完成时间")
    private String updateOverTime;
    @FormField(label = "督办事项")
    private String focusWorkId;
    @FormField(label = "原工作要求")
    private String requirement;
    @FormField(label = "调整原因")
    private String updateReason;
    @FormField(label = "调整后目标")
    private String newTarget;
}
