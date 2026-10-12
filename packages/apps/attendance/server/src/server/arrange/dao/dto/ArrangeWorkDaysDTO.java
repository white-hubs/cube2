package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArrangeWorkDaysDTO {
    private Long userId;
    private int workDays;//应出勤天数
}
