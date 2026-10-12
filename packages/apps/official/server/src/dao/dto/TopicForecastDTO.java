package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import annotation.FormField;
import lombok.Data;

@Data
public class TopicForecastDTO {
    @FormField(label = "申请人", ignore = true)
    private Long applicantId;
    @FormField(label = "申请人部门", ignore = true)
    private Long applicantDeptId;
    @FormField(label = "提议部门")
    private String applyDeptId;
    @FormField(label = "预报时间")
    private String forecastDate;
    @FormField(label = "提议名称")
    private String title;
}
