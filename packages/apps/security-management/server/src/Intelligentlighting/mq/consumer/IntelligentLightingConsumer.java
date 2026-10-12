package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.mq.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.TimeControlDetailsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.rocketmq.message.SecurityDeviceDeleteMessage;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.GroupBaseDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = SecurityDeviceDeleteMessage.TOPIC + "_Lighting_device",
                         topic = SecurityDeviceDeleteMessage.TOPIC, selectorType = SelectorType.TAG
)
public class IntelligentLightingConsumer implements RocketMQListener<SecurityDeviceDeleteMessage> {

    private final TimeControlDetailsService timeControlDetailsService;

    private final GroupBaseDetailsService groupBaseDetailsService;

    @Override
    public void onMessage(SecurityDeviceDeleteMessage message) {
        if (message.getApplicationCode().equals("INTELLIGENT_LIGHTING")) {
            timeControlDetailsService.remove(Wrappers.<TimeControlDetails>lambdaQuery()
                                                     .eq(TimeControlDetails::getDeviceObject, message.getDeviceId()));
            groupBaseDetailsService.remove(Wrappers.<GroupBaseDetails>lambdaQuery()
                                                   .eq(GroupBaseDetails::getDeviceId, message.getDeviceId()));
        }
    }
}
