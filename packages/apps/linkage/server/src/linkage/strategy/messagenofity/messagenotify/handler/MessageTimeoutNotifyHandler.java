package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.cube.base.alarm.client.dto.AlarmRecordDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventMessageDelay;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventMessageDelayService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.dto.LinkageMessageDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify.MessageNotifyAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.biz.linkage.server.util.DateUnitUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class MessageTimeoutNotifyHandler extends MessageNotifyAbstractHandler {

    @Autowired
    private EventMessageDelayService eventMessageDelayService;

    public MessageTimeoutNotifyHandler() {
        super(MessageNotifyTypeEnum.TIMEOUT_NOTIFY);
    }

    private static Map<String, String> businessMsgDefineCode = new HashMap<>();

    static {
        businessMsgDefineCode.put(BusinessTypeEnum.EMERGENCY_CENTER_ALARM.toString(), "EMERGENCY_ALARM_TIMEOUT");
    }

    @Override
    public void messageNotify(Event event, LinkageMessageDTO linkageMessageDTO, Object businessPrincipal) {
        if (Objects.nonNull(businessPrincipal)) {
            AlarmRecordDTO alarmRecordDTO = (AlarmRecordDTO) businessPrincipal;
            //发送超时开启消息
            if (Objects.nonNull(alarmRecordDTO.getTimeoutProcessTime())) {
                //超时发送消息入库
                EventMessageDelay eventMessageDelay = EventMessageDelay
                        .builder()
                        .eventId(event.getId())
                        .notifyType(linkageMessageDTO.getNotifyType())
                        .notifyConfig(JsonUtils.serialize(linkageMessageDTO))
                        .delayTime(linkageMessageDTO.getDuration())
                        .delayTimeUnit(linkageMessageDTO.getDelayTimeUnit())
                        .notifyTime(DateUnitUtils.add(alarmRecordDTO.getTimeoutProcessTime(), linkageMessageDTO.getDelayTimeUnit(), linkageMessageDTO.getDuration()))
                        .notified(false)
                        .build();
                this.eventMessageDelayService.save(eventMessageDelay);
            }
        }
    }

    @Override
    public String getMsgDefineCode(String applicationCode, String businessType) {
        String sceneCode = businessMsgDefineCode.get(businessType);
        if (BusinessTypeEnum.EMERGENCY_CENTER_ALARM.toString().equals(businessType)) {
            return applicationCode + "_" + sceneCode;
        }
        return businessMsgDefineCode.get(businessType);
    }

    @Transactional(rollbackFor = Exception.class)
    public void setNotifyTime(Long eventId, LocalDateTime startDate) {
        List<EventMessageDelay> eventMessageDelays = eventMessageDelayService.list(Wrappers.<EventMessageDelay>lambdaQuery()
                                                                                           .eq(EventMessageDelay::getEventId, eventId));
        eventMessageDelays.stream().forEach(eventMessageDelay -> {
            eventMessageDelay.setNotifyTime(DateUnitUtils.add(startDate, eventMessageDelay.getDelayTimeUnit(), eventMessageDelay.getDelayTime()));
        });
        eventMessageDelayService.updateBatchById(eventMessageDelays);
    }

    /**
     * 发送超时消息
     *
     * @param event
     * @param eventMessageDelay
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendTimeoutMessage(Event event, EventMessageDelay eventMessageDelay, AlarmRecordDTO alarmRecordDTO) {
        Boolean notified = true;
        try {
            //获取消息配置
            LinkageMessageDTO linkageMessageDTO = buildMessageNotify(eventMessageDelay.getNotifyConfig());
            this.sendMessage(linkageMessageDTO, event.getBusinessType(), String.valueOf(event.getId()), event.getEventConfig(), event.getApplicationCode(), event.getApplicationName(), alarmRecordDTO);
        } catch (Exception e) {
            notified = false;
            log.error("超时消息发送失败", e);
        }
        eventMessageDelay.setNotified(notified);
        this.eventMessageDelayService.updateById(eventMessageDelay);
    }
}
