package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import annotation.FormField;
import lombok.Data;

@Data
public class KeyWorkDTO {
    @FormField(label = "申请人", ignore = true)
    private Long applicantId;
    @FormField(label = "申请人部门", ignore = true)
    private Long applicantDeptId;
    @FormField(label = "工作事项")
    private String workDesc;
    @FormField(label = "工作目标")
    private String workTarget;
    @FormField(label = "时间安排")
    private String period;
    @FormField(label = "责任人")
    private String leader;
    @FormField(label = "完成情况")
    private String finishDesc;
}
