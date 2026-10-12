package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-10-12  15:20
 * @Description:
 * @Version: 1.0
 */
@Data
public class ClockInLogDateParam {
    /**
     * 日期
     */
    private LocalDateTime clockInDate;
    /**
     * 人员姓名
     */
    private Long userId;
    /**
     * 是否只查询异常数据
     */
    private Boolean abnormal = false;
}
