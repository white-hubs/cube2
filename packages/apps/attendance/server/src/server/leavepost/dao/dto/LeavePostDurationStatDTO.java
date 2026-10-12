package com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.dao.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LeavePostDurationStatDTO {
    private Long userId;
    private BigDecimal leavePostDuration;//离岗时长
}
