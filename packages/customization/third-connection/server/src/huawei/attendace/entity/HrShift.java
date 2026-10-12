package com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * HR排班视图实体类
 * 对应视图：hrshift
 */
@Data
public class HrShift {
    /**
     * 人员工号
     */
    private String a0190;
    
    /**
     * 人员手机号
     */
    private String cellPhone;
    
    /**
     * 排班日期
     */
    private LocalDateTime dutyDate;
    
    /**
     * 班次时段号 按 1,2,3 顺序 基本为 1 段班 或 2 段班 1 为上午段，2 为下午段
     */
    private String shiftno;
    
    /**
     * 规定上班时间
     */
    private LocalDateTime workbegin;
    
    /**
     * 规定下班时间
     */
    private LocalDateTime workend;
    
    /**
     * 班次名称
     */
    private String shiftName;
}
