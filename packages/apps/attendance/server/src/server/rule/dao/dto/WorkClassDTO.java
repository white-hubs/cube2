package com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.dto;

import lombok.Data;

@Data
public class WorkClassDTO {
    private Long id;//排班ID
    private Long classId;//具体班次的ID，可用于查询 打卡时间
    private Long staffId;//用户员工ID
    private String staffName;//用户名称
    private String staffMobile;//用户电话
    private String startWorkTime;//班次开始时间
    private String endWorkTime;//班次结束时间
}
