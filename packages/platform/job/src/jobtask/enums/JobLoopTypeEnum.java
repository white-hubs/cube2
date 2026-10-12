package com.guhecloud.platform.job.jobtask.enums;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/21 17:27
 * @Description: 循环周期
 */
@Getter
public enum JobLoopTypeEnum {

    EVERY_DAY("每日"),
    EVERY_MONTH("每月"),
    EVERY_WEEK("每周"),
    EVERY_YEAR("每年");

    JobLoopTypeEnum(String desc) {
        this.desc = desc;
    }

    private final String desc;

}

