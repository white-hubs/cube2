package com.guhecloud.cube.srv.gen.attendance.management.server.clock.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2023/03/14
 * @description 打卡日志表
 */
@Data
@TableName("classes_clock_logs")
public class ClassesClockLogsPO {

    private Long id;

    private Long staffId;

    private Long workId;//排班ID

    private LocalDateTime clockTime;//打卡时间

    private Boolean isStartClock;//是否是上班卡

    private Integer tenant;

}