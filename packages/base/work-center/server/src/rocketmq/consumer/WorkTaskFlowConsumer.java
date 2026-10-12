package com.guhecloud.cube.biz.work.center.server.rocketmq.consumer;

import com.guhecloud.cube.biz.work.center.server.param.WorkTaskFlowCirculationParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskModifyParam;
import com.guhecloud.cube.biz.work.center.server.rocketmq.message.WorkTaskFlowMessage;
import com.guhecloud.cube.biz.work.center.server.service.WorkTaskService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工作任务流程流转消息
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "work_task_msg", topic = WorkTaskFlowMessage.TOPIC, consumeMode = ConsumeMode.ORDERLY)
public class WorkTaskFlowConsumer implements RocketMQListener<WorkTaskFlowMessage> {

    private final WorkTaskService workTaskService;

    @Override
    public void onMessage(WorkTaskFlowMessage message) {
        try {
            switch (message.getOperationType()) {
                case INSERT:
                    WorkTaskModifyParam workTaskModifyParam = ObjectUtils.copying(message, WorkTaskModifyParam.class);
                    workTaskService.save(workTaskModifyParam);
                    break;
                case CHANGE:
                    WorkTaskFlowCirculationParam param = ObjectUtils.copying(message, WorkTaskFlowCirculationParam.class);
                    this.workTaskService.workTaskDone(param);
                    break;
                case DELETE:
                    this.workTaskService.workTaskDel(message.getBusinessKey());
                    break;
                default:
                    log.error("不支持的待办任务操作,业务主键:{}", message.getBusinessKey());
                    break;
            }
        } catch (Exception e) {
            log.error("消费失败，操作类型为:{}业务主键为:{},失败原因:{}", message.getOperationType(), message.getBusinessKey(), e.getMessage());
        }
    }
}
