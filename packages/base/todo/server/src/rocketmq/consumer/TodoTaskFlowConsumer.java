package com.guhecloud.cube.biz.todo.server.rocketmq.consumer;

import com.guhecloud.cube.biz.todo.server.param.TodoTaskFlowCirculationParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskModifyParam;
import com.guhecloud.cube.biz.todo.server.rocketmq.message.TodoTaskFlowMessage;
import com.guhecloud.cube.biz.todo.server.service.TodoTaskService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 待办任务流程流转消息
 *
 * @author lanchun
 * @date 2023/2/27 9:18
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "todo_task_msg", topic = TodoTaskFlowMessage.TOPIC, consumeMode = ConsumeMode.ORDERLY)
public class TodoTaskFlowConsumer implements RocketMQListener<TodoTaskFlowMessage> {

    private final TodoTaskService todoTaskService;

    @Override
    public void onMessage(TodoTaskFlowMessage message) {
        try {
            switch (message.getOperationType()) {
                case INSERT:
                    TodoTaskModifyParam todoTaskModifyParam = ObjectUtils.copying(message, TodoTaskModifyParam.class);
                    todoTaskService.save(todoTaskModifyParam);
                    break;
                case CHANGE:
                    TodoTaskFlowCirculationParam param = ObjectUtils.copying(message, TodoTaskFlowCirculationParam.class);
                    this.todoTaskService.todoTaskDone(param);
                    break;
                case DELETE:
                    this.todoTaskService.todoTaskDel(message.getBusinessKey());
                    break;
                default:
                    log.error("不支持的待办任务操作,业务主键:{}", message.getBusinessKey());
                    break;
            }
        } catch (Exception e) {
            log.error("消费失败，操作类型为:" + message.getOperationType() + "业务主键为:" + message.getBusinessKey() + ",失败原因:" +
                      e.getMessage());
        }
    }
}
