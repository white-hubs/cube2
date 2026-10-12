package com.guhecloud.cube.biz.todo.server.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/5 14:45
 * @Description:
 */
@Getter
public enum TodoTaskTypeEnum {

    NORMAL("普通任务"),
    ALARM("报警任务"),
    APPROVE("审批任务"),
    ;
    private String desc;

    TodoTaskTypeEnum(String desc) {
        this.desc = desc;
    }

}
