package com.guhecloud.cube.biz.vmonitor.server.auth.rocketmq.consumer;

import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.vmonitor.server.auth.constants.Const;
import com.guhecloud.cube.biz.vmonitor.server.auth.rocketmq.message.AreaChangeMessage;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthScopeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = Const.CONSUMER_GROUP + "_area", topic = AreaChangeMessage.TOPIC)
public class AuthScopeAreaChangeConsumer implements RocketMQListener<AreaChangeMessage> {

    private final AuthScopeService authScopeService;

    private final AreaClient areaClient;

    @Override
    public void onMessage(AreaChangeMessage message) {
        AreaDTO area = areaClient.getAreaById(message.getId());
        this.authScopeService.batchUpdateAuthScopeNameByScopeId(area.getId(), area.getName());
    }
}
