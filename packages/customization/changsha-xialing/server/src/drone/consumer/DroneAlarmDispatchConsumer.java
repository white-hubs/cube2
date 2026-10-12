package com.guhecloud.cube.customization.xialing.server.drone.consumer;

import com.guhecloud.cube.base.alarm.client.message.AlarmRecordOccurredMessage;
import com.guhecloud.cube.customization.xialing.server.drone.service.DroneAlarmDispatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 报警发生消息消费者，用于触发无人机自动派飞。
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = AlarmRecordOccurredMessage.TOPIC + "_DRONE_DISPATCH", topic = AlarmRecordOccurredMessage.TOPIC)
public class DroneAlarmDispatchConsumer implements RocketMQListener<AlarmRecordOccurredMessage> {
    private final DroneAlarmDispatchService droneAlarmDispatchService;

    /**
     * 消费报警发生消息并委托业务服务执行派飞。
     *
     * @param message 报警发生消息
     */
    @Override
    public void onMessage(AlarmRecordOccurredMessage message) {
        try {
            droneAlarmDispatchService.handleAlarmOccurred(message);
        } catch (Exception e) {
            log.error("无人机报警派飞消费失败 alarmRecordId={}", message == null ? null : message.getAlarmRecordId(), e);
        }
    }
}
