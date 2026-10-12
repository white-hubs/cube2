package com.guhecloud.cube.biz.daily.task.migration;

import com.guhecloud.cube.biz.daily.task.migration.dto.MigrationLimitTotalMinuteDTO;

public class MigrationUtils {
    public static MigrationLimitTotalMinuteDTO convertMinutesToDHM(Integer totalMinutes) {
        if (totalMinutes == null || totalMinutes == 0) {
            return new MigrationLimitTotalMinuteDTO();
        }

        int days = totalMinutes / (24 * 60);           // 1天 = 1440分钟
        int remainingAfterDays = totalMinutes % (24 * 60);

        int hours = remainingAfterDays / 60;           // 1小时 = 60分钟
        int minutes = remainingAfterDays % 60;

        return MigrationLimitTotalMinuteDTO.builder()
                .taskDay(days)
                .taskHour(hours)
                .taskMinute(minutes)
                .build();
    }
}
