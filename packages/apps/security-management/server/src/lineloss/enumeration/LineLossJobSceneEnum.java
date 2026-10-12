package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.enumeration;

import lombok.Getter;

/**
 * 定时任务场景枚举
 */
@Getter
public enum LineLossJobSceneEnum {
    READ_COLLECT("读数采集"),
    LINE_LOSS_STAT("线损统计"),
    ;
    private final String description;

    LineLossJobSceneEnum(String description) {
        this.description = description;
    }
}
