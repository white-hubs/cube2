package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.cube.base.alarm.client.AlarmRecordClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmRecordDTO;
import com.guhecloud.cube.base.alarm.client.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventMessageDelay;
import com.guhecloud.cube.biz.linkage.server.linkage.enumeration.EventTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventMessageDelayService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify.handler.MessageTimeoutNotifyHandler;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageTimeoutHandler {

    private final EventService eventService;

    private final EventMessageDelayService eventMessageDelayService;

    private final MessageTimeoutNotifyHandler messageTimeoutNotifyHandler;

    private final AlarmRecordClient alarmRecordClient;

    public void messageTimeoutHandler() {
        List<EventMessageDelay> eventMessageDelays = this.eventMessageDelayService.list(Wrappers.<EventMessageDelay>lambdaQuery()
                                                                                                .eq(EventMessageDelay::getNotifyType, MessageNotifyTypeEnum.TIMEOUT_NOTIFY)
                                                                                                .eq(EventMessageDelay::getNotified, Boolean.FALSE)
                                                                                                .le(EventMessageDelay::getNotifyTime, DateUtils.now()));
        eventMessageDelays.parallelStream().forEach(eventMessageDelay -> {
            SessionContextHolder.requestTenant(eventMessageDelay.getTenant());
            Event event = this.eventService.getById(eventMessageDelay.getEventId());
            //目前只有报警才会升级
            if (EventTypeEnum.ALARM_RULE == event.getType()) {
                AlarmRecordDTO alarmRecordDTO = this.alarmRecordClient.getAlarmRecordByBusinessKey(String.valueOf(event.getId()));
                if (AlarmStatusEnum.ALARM_PROGRESS_CONFIRMED == alarmRecordDTO.getAlarmStatus() ||
                    AlarmStatusEnum.ALARM_UN_CONFIRMED == alarmRecordDTO.getAlarmStatus()) {
                    //超时消息发送
                    this.messageTimeoutNotifyHandler.sendTimeoutMessage(event, eventMessageDelay, alarmRecordDTO);
                }
            }
            DatabaseContextHolder.softDelete(eventMessageDelayService, eventMessageDelay.getId());
        });
    }
}
