package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import annotation.FormField;
import lombok.Data;

@Data
public class DocumentCirculationDTO {
    @FormField(label = "申请人", ignore = true)
    private Long applicantId;
    @FormField(label = "申请人部门", ignore = true)
    private Long applicantDeptId;
    @FormField(label = "来文单位")
    private String formUnit;
    @FormField(label = "文件名称")
    private String title;
    @FormField(label = "发文号")
    private String fileCode;
    @FormField(label = "来文时间")
    private String receivedTime;
}
