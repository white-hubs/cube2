package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/11/16
 * @description 值班-排班的工作数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkDateCommand {

    /**
     * 月份
     */
    private String month;

    /**
     * 周几
     */
    private Integer week;

    /**
     * 日期
     */
    private LocalDateTime date;

    /**
     * 是节假日，并且自动排休
     */
    private Boolean isHolidayAndAutoRest;

}