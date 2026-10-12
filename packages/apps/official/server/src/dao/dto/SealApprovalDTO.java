package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import annotation.FormField;
import lombok.Data;

@Data
public class SealApprovalDTO {
    @FormField(label = "申请人", ignore = true)
    private Long applicantId;
    @FormField(label = "申请人部门", ignore = true)
    private Long applicantDeptId;
    @FormField(label = "文件名称")
    private String title;
    @FormField(label = "文件份数")
    private String fileNum;
    @FormField(label = "选择的印章")
    private Long sealId;
    @FormField(label = "用章时间")
    private String useDate;
    @FormField(label = "用章部门")
    private String useDeptId;
    @FormField(label = "用章事由")
    private String reason;
}
