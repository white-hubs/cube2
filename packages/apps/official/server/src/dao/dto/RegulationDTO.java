package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import annotation.FormField;
import lombok.Data;

@Data
public class RegulationDTO {
    @FormField(label = "申请人", ignore = true)
    private Long applicantId;
    @FormField(label = "申请人部门")
    private Long applicantDeptId;
    @FormField(label = "文件标题")
    private String title;
    //征求日期
    @FormField(label = "征求日期")
    private String applyDate;
    //反馈时间段
    @FormField(label = "反馈时段")
    private String replyPeriod;
}
