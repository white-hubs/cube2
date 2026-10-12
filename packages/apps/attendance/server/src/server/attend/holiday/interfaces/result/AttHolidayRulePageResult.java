package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 假期规则分页返回对象
 */
@Data
@Builder
public class AttHolidayRulePageResult {
    private Long id;
    /**
     * 假期名称
     */
    private String name;
    /**
     * 最小请假单位(低于此不能请假
     */
    private int minAskUnit;
    /**
     * 请假时间计算方式（工作日/自然日
     */
    private String calculationWay;
    /**
     * 不限额(true表示不限额
     */
    private Boolean notLimit;
    /**
     * 编辑时间
     */
    private LocalDateTime updateTime;
}
