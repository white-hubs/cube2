package com.guhecloud.platform.job.jobtask.cron.dto;

import com.guhecloud.platform.job.jobtask.enums.JobLoopTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/21 17:45
 * @Description:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CronModelDTO {

    /**
     * 每周的那几天
     */
    List<String> dayOfWeeks;
    /**
     * 每月的那几天
     */
    List<String> dayOfMonths;
    /**
     * 每年的那几个月
     */
    List<String> months;
    /**
     * 小时
     */
    Integer hour;

    /**
     * 分钟
     */
    Integer minute;

    /**
     * 秒
     */
    Integer second = 0;

    /**
     * 类型
     */
    JobLoopTypeEnum jobType;
    /**
     * 间隔
     */
    Integer beApart;
}
