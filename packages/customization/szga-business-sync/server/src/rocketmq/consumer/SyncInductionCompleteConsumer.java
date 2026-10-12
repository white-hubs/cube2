package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncUserInfoOperationHandler;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.ProcessCompletedMessage;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.InductionPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 19:08
 * @Description: 入职审批完成监听
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_" + "INFO_SYNC",
                         topic = ProcessCompletedMessage.TOPIC, selectorExpression = "PERSONNEL_INDUCTION"
)
public class SyncInductionCompleteConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final SyncUserInfoOperationHandler userInfoOperationHandler;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("流程启动消息内容: {}", message.getData());
        try {
            if (!"PASS".equals(message.getResult().name())) {
                return;
            }
            SessionContextHolder.requestTenant(message.getTenant());
            InductionPO inductionPO = JSONObject.parseObject(message.getData(), InductionPO.class);
            userInfoOperationHandler.inductionComplete(inductionPO);
        } catch (Exception e) {
            log.error("处理入职审批完成消息失败", e);
            SessionContextHolder.clearSessionContext();
        }
    }
}
