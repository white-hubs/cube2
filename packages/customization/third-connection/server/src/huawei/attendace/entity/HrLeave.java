package com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * HR请假视图实体类
 * 对应视图：hrleave
 */
@Data
public class HrLeave {
    /**
     * 人员工号
     */
    private String a0190;
    
    /**
     * 人员手机号
     */
    private String cellPhone;
    
    /**
     * 请假类型
     */
    private String leaveType;
    
    /**
     * 请假开始时间
     */
    private LocalDateTime begintime;
    
    /**
     * 请假结束时间
     */
    private LocalDateTime endtime;
}
