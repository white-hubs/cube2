package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify.handler;

import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventMessageDelay;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventMessageDelayService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.dto.LinkageMessageDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagenotify.MessageNotifyAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.util.DateUnitUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class MessageDelayNotifyHandler extends MessageNotifyAbstractHandler {

    @Autowired
    private EventMessageDelayService eventMessageDelayService;

    public MessageDelayNotifyHandler() {
        super(MessageNotifyTypeEnum.DELAY_NOTIFY);
    }

    @Override
    public void messageNotify(Event event, LinkageMessageDTO linkageMessageDTO, Object businessPrincipal) {
        //延迟发送消息入库
        EventMessageDelay eventMessageDelay = EventMessageDelay
                .builder()
                .eventId(event.getId())
                .notifyType(linkageMessageDTO.getNotifyType())
                .notifyConfig(JsonUtils.serialize(linkageMessageDTO))
                .delayTime(linkageMessageDTO.getDuration())
                .delayTimeUnit(linkageMessageDTO.getDelayTimeUnit())
                .notified(false)
                .build();
        eventMessageDelay.setNotifyTime(DateUnitUtils.add(event.getTriggerTime(), linkageMessageDTO.getDelayTimeUnit(), linkageMessageDTO.getDuration()));
        this.eventMessageDelayService.save(eventMessageDelay);
    }

    /**
     * 发送延迟消息
     *
     * @param event
     * @param eventMessageDelay
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendDelayMessage(Event event, EventMessageDelay eventMessageDelay) {
        Boolean notified = true;
        try {
            //获取消息配置
            LinkageMessageDTO linkageMessageDTO = buildMessageNotify(eventMessageDelay.getNotifyConfig());
            this.sendMessage(linkageMessageDTO, event.getBusinessType(), String.valueOf(event.getId()), event.getEventConfig(), event.getApplicationCode(), event.getApplicationName());
        } catch (Exception e) {
            notified = false;
            log.error("延迟消息发送失败", e);
        }
        eventMessageDelay.setNotified(notified);
        this.eventMessageDelayService.updateById(eventMessageDelay);
    }
}
