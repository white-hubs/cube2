package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import lombok.Getter;

/**
 * 报警处理超时状态枚举
 *
 * @author lanchun
 * @date 2022/10/25 14:59
 */
@Getter
public enum AlarmHandlerTimeoutStatusEnum {

    TIMED_OUT("超时"),

    NORMAL("正常");

    /**
     * 描述信息
     */
    private final String description;

    AlarmHandlerTimeoutStatusEnum(String description) {
        this.description = description;
    }
}
