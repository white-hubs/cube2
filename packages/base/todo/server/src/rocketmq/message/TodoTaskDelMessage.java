package com.guhecloud.cube.biz.todo.server.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 待办任务删除消息
 *
 * @author lanchun
 * @date 2023/2/27 9:16
 */
@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class TodoTaskDelMessage extends TodoTaskFlowMessage {

}
