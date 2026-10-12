package com.guhecloud.cube.biz.todo.client.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 待办状态枚举
 *
 * @author lanchun
 * @date 2022/7/22 10:32
 */
@Getter
public enum TodoTaskStatusEnum {
    TODO("待办"),
    DONE("已办");
    /**
     * 描述信息
     */
    private final String description;

    TodoTaskStatusEnum(@NonNull String description) {
        this.description = description;
    }
}
