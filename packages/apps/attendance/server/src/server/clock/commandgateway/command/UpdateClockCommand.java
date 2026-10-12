package com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.RepairClockTypeEnum;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/12/15
 * @description 修改打卡
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClockCommand {

    /**
     * 员工ID
     */
    private Long staffId;

    /**
     * 打卡日期
     */
    private String clockDate;

    /**
     * 排班班次ID
     */
    private Long arrangeClassesId;

    /**
     * 上班打卡时间
     */
    private LocalDateTime startWorkClockTime;

    /**
     * 下班打卡时间
     */
    private LocalDateTime endWorkClockTime;

    /**
     * 时间段
     */
    private Period period;

    /**
     * 触发动作
     */
    private String trigger;

    /**
     * 打卡类型
     */
    private RepairClockTypeEnum clockType;

}