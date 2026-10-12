package com.guhecloud.cube.base.message2.server.initialization.rocketmq.comsumer;

import com.guhecloud.cube.base.message2.server.initialization.event.IncreaseSatMsgTmpEvent;
import com.guhecloud.cube.base.message2.server.initialization.rocketmq.message.MsgTmpAddMessage;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmp;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpService;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "msg_tmp_add", topic = MsgTmpAddMessage.TOPIC, selectorType = SelectorType.TAG)
public class MsgTmpAddConsumer implements RocketMQListener<MsgTmpAddMessage> {

    private final TenantClient tenantClient;

    private final SatMsgTmpService satMsgTmpService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onMessage(MsgTmpAddMessage message) {
        List<TenantDTO> applicationTenants = this.tenantClient.getApplicationTenant(message.getApplicationCode());
        applicationTenants.parallelStream().forEach(tenantDTO -> {
            SessionContextHolder.requestTenant(tenantDTO.getCode());
            //发送模板创建事件
            SatMsgTmp satMsgTmp = ObjectUtils.copying(message, SatMsgTmp.class);
            satMsgTmp.setId(null);
            satMsgTmp.setMsgTmpId(message.getId());
            this.satMsgTmpService.save(satMsgTmp);
            this.applicationEventPublisher.publishEvent(new IncreaseSatMsgTmpEvent(this, Collections.singletonList(satMsgTmp)));
        });
    }
}
