package com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.consumer;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.AlarmReasonConfigCommandService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.reason.IncreaseAlarmReasonConfigCommand;
import com.guhecloud.cube.biz.ouaa.client.message.TenantInitMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "alarm_reason_init", topic = TenantInitMessage.TOPIC, selectorType = SelectorType.TAG)
public class AlarmReasonInitConsumer implements RocketMQListener<TenantInitMessage> {

    private final AlarmReasonConfigCommandService alarmReasonConfigCommandService;

    private final Map<String, String> ALARM_REASON;

    public AlarmReasonInitConsumer(AlarmReasonConfigCommandService alarmReasonConfigCommandService
                                  ) {
        this.alarmReasonConfigCommandService = alarmReasonConfigCommandService;
        ALARM_REASON = new HashMap<>();
        ALARM_REASON.put("ALARM_ACTUAL", "实报");
        ALARM_REASON.put("ALARM_IMPRACTICAL", "误报");
    }

    @Override
    public void onMessage(TenantInitMessage message) {
        SessionContextHolder.requestTenant(message.getTenant());
        List<IncreaseAlarmReasonConfigCommand> commands = new ArrayList<>();
        ALARM_REASON.forEach((k, v) -> {
            IncreaseAlarmReasonConfigCommand command = new IncreaseAlarmReasonConfigCommand();
            command.setCode(k);
            command.setReason(v);
            commands.add(command);
        });
        alarmReasonConfigCommandService.initAlarmReasonConfig(commands);
    }
}
