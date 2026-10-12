package com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.consumer;

import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordOperatorLogService;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.AlarmRecordQueryService;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmOperatorTypeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.message.LinkageMessageNotifyMessage;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.mq.rocketmq.message.LinkageMessageNotifyTargetMessage;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "LINKAGE_MESSAGE", topic = LinkageMessageNotifyMessage.TOPIC,
                         selectorType = SelectorType.TAG
)
public class LinkageMessageNotifyConsumer implements RocketMQListener<LinkageMessageNotifyMessage> {

    private final AlarmRecordQueryService alarmRecordQueryService;

    private final AlarmRecordOperatorLogService alarmRecordOperatorLogService;

    @Override
    public void onMessage(LinkageMessageNotifyMessage message) {
        AlarmRecordPO alarmRecordPO = alarmRecordQueryService.getByBusinessKey(message.getBusinessKey());
        if (Objects.isNull(alarmRecordPO)) {
            return;
        }
        String userNamesStr = message.getMessageNotifyTargetMessages().stream()
                .map(LinkageMessageNotifyTargetMessage::getUserName)
                .collect(Collectors.joining(","));
        String operatorLogContent = "消息通知:" + "将预警记录抄送给了" + userNamesStr;
        Principal principal = SecurityContextHolder.getPrincipal(true);
        this.alarmRecordOperatorLogService.save(alarmRecordPO.getId(), AlarmOperatorTypeEnum.ALARM_MESSAGE_NOTIFY, null,
                                                Objects.isNull(principal) ? "系统"
                                                                          : principal.getName(), DateUtils.now(), operatorLogContent, null);
    }
}
