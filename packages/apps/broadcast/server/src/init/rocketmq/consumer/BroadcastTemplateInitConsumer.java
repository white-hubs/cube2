package com.guhecloud.cube.biz.subsystem.broadcast.server.init.rocketmq.consumer;

import com.guhecloud.cube.biz.ouaa.client.message.TenantInitMessage;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.BroadcastTemplateService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastTemplatePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.init.config.BroadcastTemplateInit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TenantInitMessage.TOPIC + "_" + "BROADCAST", topic = TenantInitMessage.TOPIC)
public class BroadcastTemplateInitConsumer implements RocketMQListener<TenantInitMessage> {

    private final BroadcastTemplateInit broadcastTemplateInit;

    private final BroadcastTemplateService broadcastTemplateService;

    @Override
    public void onMessage(TenantInitMessage message) {
        List<BroadcastTemplatePO> broadcastTemplatePOS = new ArrayList<>(this.broadcastTemplateInit.getTemplates().size());
        this.broadcastTemplateInit.getTemplates().forEach(template -> {
            BroadcastTemplatePO broadcastTemplatePO = BroadcastTemplatePO.builder()
                    .name(template.getName())
                    .content(template.getName())
                    .defaulted(true)
                    .build();
            broadcastTemplatePOS.add(broadcastTemplatePO);
        });
        this.broadcastTemplateService.saveBatch(broadcastTemplatePOS);
    }
}
