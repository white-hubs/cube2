package com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项反馈
 */
@Data
public class FocusWorkReplyCommand {

    private Long id;

    private Long applicantId;//申请人ID

    private String applicantName;//申请人姓名

    private Long applicantDeptId;//部门ID

    private String applicantDeptName;//部门名称

    private LocalDateTime applyTime;//申请时间

    private LocalDateTime applyDoneTime;//申请处理完成时间

    private Long focusWorkId;//督办事项ID

    private float percentage;//进度百分比

    private String nextWorkDesc;//下一步工作描述

    private String undoneReason;//未完成原因

    private String assistProblem;//需要协同的问题
    /**
     * 落实情况
     */
    private String practicableDetails;

}