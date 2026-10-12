package com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.enumeration;

import lombok.Getter;

/**
 * 待办任务流转操作枚举
 *
 * @author lanchun
 * @date 2023/2/27 9:21
 */
@Getter
public enum TodoTaskFlowOperationEnum {

    /**
     * 新增
     */
    INSERT,

    /**
     * 修改
     */
    CHANGE,

    /**
     * 删除
     */
    DELETE
}
