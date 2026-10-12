package com.guhecloud.cube.customization.xialing.server.drone.consumer;

import com.guhecloud.cube.base.alarm.client.message.AlarmRecordAssignedMessage;
import com.guhecloud.cube.customization.xialing.server.drone.service.DroneAlarmDispatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 报警初始化指派完成消息消费者，用于补发无人机派飞结果通知。
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = AlarmRecordAssignedMessage.TOPIC + "_DRONE_DISPATCH", topic = AlarmRecordAssignedMessage.TOPIC)
public class DroneAlarmAssignConsumer implements RocketMQListener<AlarmRecordAssignedMessage> {
    private final DroneAlarmDispatchService droneAlarmDispatchService;

    /**
     * 消费报警初始化指派完成消息并委托业务服务发送派飞结果通知。
     *
     * @param message 报警初始化指派完成消息
     */
    @Override
    public void onMessage(AlarmRecordAssignedMessage message) {
        try {
            droneAlarmDispatchService.handleAlarmAssigned(message);
        } catch (Exception e) {
            log.error("无人机报警派飞结果通知消费失败 alarmRecordId={}", message == null ? null : message.getAlarmRecordId(), e);
        }
    }
}
