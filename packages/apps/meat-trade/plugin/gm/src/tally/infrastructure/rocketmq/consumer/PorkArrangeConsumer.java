package com.guhecloud.cube.gm.meattrade.plugin.tally.infrastructure.rocketmq.consumer;

import com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.producer.GmMeattradeTallyPushProducer;
import com.guhecloud.cube.gm.meattrade.plugin.tally.application.TallyPushService;
import com.guhecloud.cube.gm.meattrade.plugin.tally.domain.enumeration.TallyPushStatusEnum;
import com.guhecloud.cube.gm.meattrade.plugin.tally.domain.po.TallyPushPO;
import com.guhecloud.cube.gm.meattrade.plugin.tally.infrastructure.rocketmq.message.PorkArrangeMessage;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * 理货数据结束消费
 *
 * @author lanchun
 * @date 2023/4/13 13:46
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "MEATTRADE_ARRANGE_PUSH", topic = PorkArrangeMessage.TOPIC)
@ConditionalOnBean(name = "guangMingRocketMQProperties")
public class PorkArrangeConsumer implements RocketMQListener<PorkArrangeMessage> {

    private final TallyPushService tallyPushService;

    private final GmMeattradeTallyPushProducer meattradePublisher;

    @Override
    public void onMessage(PorkArrangeMessage message) {
        if (3 != message.getTallyMachineEventType()) {
            log.debug("光明忽略理货状态为开始、理货中、理货暂停的数据,报备号为{}", message.getPorkReportCode());
            return;
        }
        Long tallyPushId = this.tallyPushService.save(message.getPorkReportCode(), JsonUtils.serialize(message), TallyPushStatusEnum.NOT_PUSHED);
        this.meattradePublisher.sendMessage(TallyPushPO
                                                    .builder()
                                                    .id(tallyPushId)
                                                    .pushContent(JsonUtils.serialize(message))
                                                    .pushStatus(TallyPushStatusEnum.NOT_PUSHED)
                                                    .build());
    }
}
