package com.guhecloud.cube.biz.work.center.server.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/5 14:45
 * @Description:
 */
@Getter
public enum WorkTaskTypeEnum {

    NORMAL("普通任务"),
    ALARM("报警任务"),
    APPROVE("审批任务"),
    ;
    private String desc;

    WorkTaskTypeEnum(String desc) {
        this.desc = desc;
    }

}
