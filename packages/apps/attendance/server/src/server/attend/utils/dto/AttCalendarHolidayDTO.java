package com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto;

import lombok.Data;

@Data
public class AttCalendarHolidayDTO {
    /**
     * true 表示法定节假日，false 表示法定工作日
     */
    private Boolean holiday;
    /**
     * 日期名称(周六、周日、节日名称、节日名称前补班、节日名称后补班)
     * 例如：周六、周日、国庆节、国庆节前补班、国庆节后补班
     */
    private String name;
    /**
     * 工资倍数
     */
    private Integer wage;
    /**
     * 日期(yyyy-MM-dd)
     */
    private String date;
    /**
     * 节日名称(无节日为空)
     */
    private String target;
}
