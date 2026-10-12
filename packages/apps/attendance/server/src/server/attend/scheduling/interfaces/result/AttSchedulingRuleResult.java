package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 排班规则返回对象
 */
@Data
public class AttSchedulingRuleResult {

    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 排班方式(固定班，三班倒)
     */
    private String ways;
    /**
     * 法定节假日是否休息
     */
    private Boolean legalHolidaysRest;
    /**
     * 一个月允许最大连续排班天数
     */
    private Integer continuousDays;
    /**
     * 一天两个班次最小间隔时间
     */
    private Integer intervalHour;
    /**
     * 一个月相同班次数量的json
     */
    private String sameShift;
    //    /**
    //     * 相同班次数量
    //     */
    //    private Integer sameShiftNumber;
    //    /**
    //     * 相同班次类型
    //     */
    //    private String sameShiftType;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}
