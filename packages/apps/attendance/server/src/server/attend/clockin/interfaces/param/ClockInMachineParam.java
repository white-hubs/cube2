package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-10-12
 * @Description: 考勤机打卡参数
 * @Version: 1.0
 */
@Data
public class ClockInMachineParam {
    @NotNull(message = "打卡人员不能为空")
    private Long userId;
    /**
     * 打卡照片
     */
    private Long photoFileId;
    /**
     * 打卡时间
     */
    @NotNull(message = "打卡时间不能为空")
    private LocalDateTime clockInTime;
    private String location;

}
