package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import annotation.FormField;
import lombok.Data;

@Data
public class FocusWorkReplyDTO {
    @FormField(label = "申请人", ignore = true)
    private Long applicantId;
    @FormField(label = "申请人部门", ignore = true)
    private Long applicantDeptId;
    @FormField(label = "督办事项")
    private String focusWorkId;
    @FormField(label = "完成时间")
    private String completeTime;
    @FormField(label = "完成进度")
    private String percentage;
    @FormField(label = "下一步工作描述")
    private String nextWorkDesc;
    @FormField(label = "未完成原因")
    private String undoneReason;
    @FormField(label = "需要协同的问题")
    private String assistProblem;
    @FormField(label = "落实情况")
    private String practicableDetails;
}
