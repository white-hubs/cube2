package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  11:21
 * @Description:
 * @Version: 1.0
 */
@Data
public class AddClockInParam {
    /**
     * 员工id
     */
    private Long userId;
    /**
     * 打卡标识
     */
    @NotNull(message = "打卡标识不能为空")
    private Long id;
    /**
     * 打卡时间
     */
    @NotNull(message = "打卡时间不能为空")
    private LocalDateTime actualClockInTime;
}
