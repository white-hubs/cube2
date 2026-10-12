package com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CmmFlowStatDetailVO {
    //部门
    private String deptName;
    //申请人
    private String applicantName;
    //状态
    private String status;
    //申请时间
    private LocalDateTime applyTime;
    //审批完成时间
    private LocalDateTime applyDoneTime;
}
