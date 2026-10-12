package com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration;

import lombok.Getter;

@Getter
public enum MessageModuleTypeEnum {
    ELECTRON_TASK_RECEIVE("任务接收提醒"),
    ELECTRON_TASK_EXPIRE("任务到期提醒"),
    ELECTRON_TASK_ABNORMAL("任务异常");

    private String remarks;

    MessageModuleTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
