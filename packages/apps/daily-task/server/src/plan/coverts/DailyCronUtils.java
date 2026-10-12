package com.guhecloud.cube.biz.daily.task.plan.coverts;

import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import com.guhecloud.platform.job.jobtask.cron.dto.CronModelDTO;
import com.guhecloud.platform.job.jobtask.enums.JobLoopTypeEnum;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DailyCronUtils {
    public static String handlerCorn(String specifiedType, String specifiedDays, String specifiedTime) {
        var splits = specifiedTime.split(":");
        var hour = Integer.parseInt(specifiedTime.split(":")[0]);
        var minute = splits.length > 0 ? Integer.parseInt(specifiedTime.split(":")[1]) : 0;
        var second = splits.length > 1 ? Integer.parseInt(specifiedTime.split(":")[2]) : 0;
        var builder = CronModelDTO.builder().jobType(JobLoopTypeEnum.valueOf(specifiedType)).hour(hour).minute(minute).second(second);
        var days = Arrays.stream(specifiedDays.split(",")).collect(Collectors.toList());
        if (JobLoopTypeEnum.EVERY_WEEK.name().equals(specifiedType)) {
            builder.dayOfWeeks(days);
        } else if (JobLoopTypeEnum.EVERY_MONTH.name().equals(specifiedType)) {
            builder.dayOfMonths(days);
        } else if (JobLoopTypeEnum.EVERY_YEAR.name().equals(specifiedType)) {
            builder.months(days);
        }
        return CronExpression.createCronExpression(builder.build());
    }

    /**
     * 将 Quartz Cron 表达式（6 或 7 字段）转换为 Spring Boot 3.x 兼容的 6 字段表达式。
     * <p>
     * 注意：Spring Boot 3.5.6 支持 L, W, # 等高级特性，但不支持第7位“年”字段。
     */
    public static String convertQuartzToSpring(String quartzCron) {
        if (quartzCron == null || quartzCron.isBlank()) {
            throw new IllegalArgumentException("Cron expression must not be null or blank");
        }

        String[] parts = quartzCron.trim().split("\\s+");
        if (parts.length == 6) {
            return quartzCron.trim(); // 已是 Spring 格式
        } else if (parts.length == 7) {
            // 拼接前6个字段
            return String.join(" ", Arrays.copyOf(parts, 6));
        } else {
            throw new IllegalArgumentException(
                    "Expected 6 or 7 cron fields, got: " + parts.length + " in '" + quartzCron + "'");
        }
    }

}
