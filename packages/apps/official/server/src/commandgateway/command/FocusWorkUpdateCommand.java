package com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项更新
 */
@Data
public class FocusWorkUpdateCommand {

    private Long id;

    private Long applicantId;//申请人ID

    private String applicantName;//申请人姓名

    private Long applicantDeptId;//部门ID

    private String applicantDeptName;//部门名称

    private LocalDateTime applyTime;//申请时间

    private LocalDateTime applyDoneTime;//申请处理完成时间

    private Long focusWorkId;//督办事项ID

    private LocalDateTime updateOverTime;//更新完成时间

    private String updateOverTimeStr;

    private String status;//状态

    private String updateReason;//更新理由

    private String newTarget;//调整后目标

    private String processInstanceId;//流程实例ID

}