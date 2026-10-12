package com.guhecloud.cube.srv.gen.attendance.management.server.attend.beforestat.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 补卡简略信息
 */
@Data
public class AttRepairSimpleInfoDTO {
    private Long userId;
    private LocalDateTime repairTime;
}
