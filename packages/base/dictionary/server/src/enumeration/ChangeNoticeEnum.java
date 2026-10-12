package com.guhecloud.cube.base.dictionary.server.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-25  13:48
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum ChangeNoticeEnum {
    ROCKET_MQ("RocketMQ"),
    REDIS("redis"),
    ;
    private final String remarks;

    ChangeNoticeEnum(String remarks) {
        this.remarks = remarks;
    }
}
