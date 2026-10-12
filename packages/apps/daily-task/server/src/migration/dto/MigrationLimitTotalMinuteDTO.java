package com.guhecloud.cube.biz.daily.task.migration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MigrationLimitTotalMinuteDTO {
    private Integer taskDay;
    private Integer taskHour;
    private Integer taskMinute;
}
