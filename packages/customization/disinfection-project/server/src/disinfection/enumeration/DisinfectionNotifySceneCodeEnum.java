package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisinfectionNotifySceneCodeEnum {
    /**
     * 任务创建通知、任务即将到期通知、任务到期未完成通知
     */
    TASK_CREATE("DISINFECTION_TASK_CREATE"),
    TASK_LAPSE("DISINFECTION_TASK_LAPSE"),
    TASK_LAPSE_UNFINISHED("DISINFECTION_TASK_LAPSE_UNFINISHED");
    private final String code;
}
