package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import annotation.FormField;
import lombok.Data;

@Data
public class DocumentReceivedDTO {
    @FormField(label = "申请人", ignore = true)
    private Long applicantId;
    @FormField(label = "申请人部门", ignore = true)
    private Long applicantDeptId;
    @FormField(label = "来文单位")
    private String fromUnit;
    @FormField(label = "来文字号")
    private String fileCode;
    @FormField(label = "收文编号")
    private String receivedFileCode;
    @FormField(label = "收文时间")
    private String receivedTime;
    @FormField(label = "处理时限")
    private String timeLimit;
}
