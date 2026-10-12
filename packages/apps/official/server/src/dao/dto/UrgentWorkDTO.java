package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import annotation.FormField;
import lombok.Data;

@Data
public class UrgentWorkDTO {
    @FormField(label = "申请人", ignore = true)
    private Long applicantId;
    @FormField(label = "申请人部门", ignore = true)
    private Long applicantDeptId;
    @FormField(label = "办理时限")
    private String timeLimitPeriod;
    @FormField(label = "催办事项")
    private String urgentWork;
    @FormField(label = "工作要求")
    private String requirement;
}
