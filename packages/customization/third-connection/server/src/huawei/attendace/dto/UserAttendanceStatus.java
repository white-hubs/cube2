package com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.dto;

import lombok.Data;

/**
 * 用户考勤状态
 */
@Data
public class UserAttendanceStatus {
    /**
     * 人员工号
     */
    private String employeeNo;
    
    /**
     * 人员手机号
     */
    private String cellPhone;
    
    /**
     * 是否在岗
     */
    private Boolean isOnDuty;
    
    /**
     * 状态说明
     */
    private String statusDesc;
}
