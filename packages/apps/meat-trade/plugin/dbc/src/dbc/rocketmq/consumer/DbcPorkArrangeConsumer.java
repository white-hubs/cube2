package com.guhecloud.cube.sn.meattrade.plugin.dbc.rocketmq.consumer;

import com.guhecloud.cube.sn.meattrade.plugin.common.entity.PorkArrange;
import com.guhecloud.cube.sn.meattrade.plugin.common.service.PorkArrangePushDataService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.rocketmq.message.PorkArrangeMessage;
import com.guhecloud.cube.sn.meattrade.plugin.utils.PorkArrangeConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "SN_PORK_ARRANGE", topic = PorkArrangeMessage.TOPIC,
                         messageModel = MessageModel.CLUSTERING, consumeMode = ConsumeMode.ORDERLY
)
public class DbcPorkArrangeConsumer implements RocketMQListener<PorkArrangeMessage> {

    private final PorkArrangePushDataService porkArrangePushDataService;

    @Override
    public void onMessage(PorkArrangeMessage message) {
        try {
            if (2 == message.getTallyMachineEventType()) {
                log.debug("大白菜不消费理货状态为暂停的理货数据,报备批次为{},状态为{}", message.getPorkReportCode(), message.getTallyMachineEventType());
                return;
            }
            PorkArrange porkArrange = PorkArrangeConvertUtils.convertMessageTOPorkArrange(message);
            //结束状态
            if (3 == message.getTallyMachineEventType()) {
                //只保留当前理货机的所有理货数据
                porkArrange.setArrangeDetails(porkArrange.getArrangeDetails().stream()
                                                      .filter(detail -> porkArrange.getDeviceId().equals(detail.getPlace()))
                                                      .collect(Collectors.toList()));
            }
            Long id = this.porkArrangePushDataService.save(porkArrange);
            log.info("消费理货开始事件成功,id:{}", id);
        } catch (Exception e) {
            log.error("深农消费滑轨开始理货数据失败:{}", message);
            throw new RuntimeException(e);
        }
    }
}
