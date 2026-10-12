package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventAction;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.EventHandler;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventTriggerService {

    private final EventService eventService;

    private final EventHandler eventHandler;

    private final EventActionService eventActionService;

    //事件触发
    public void eventTrigger(@NonNull List<Rule> rules, @NonNull LocalDateTime triggerTime, @NonNull String eventConfig) {
        for (var rule : rules) {
            this.eventTrigger(rule, null, triggerTime, eventConfig, null, null);
        }
    }

    //事件触发
    public void eventTrigger(@NonNull Rule rule, String triggerTargetId, @NonNull LocalDateTime triggerTime, @NonNull String eventConfig,
                             String iotEventInstanceId, String snapshot
                            ) {
        //事件保存
        var event = this.eventService.save(rule, triggerTargetId, triggerTime, eventConfig, iotEventInstanceId, snapshot);
        eventTriggerAction(event);
    }

    /**
     * 事件触发。只生成事件和录像
     *
     * @param rule
     * @param triggerTargetId
     * @param triggerTime
     * @param eventConfig
     * @param iotEventInstanceId
     * @param snapshot
     */
    public Event addEventAndTriggerVideoActionOnly(@NonNull Rule rule, String triggerTargetId, @NonNull LocalDateTime triggerTime,
                                                   @NonNull String eventConfig,
                                                   String iotEventInstanceId, String snapshot
                                                  ) {
        var event = this.eventService.save(rule, triggerTargetId, triggerTime, eventConfig, iotEventInstanceId, snapshot);
        var videoAction = eventActionService.list(Wrappers.<EventAction>lambdaQuery()
                                                          .eq(EventAction::getEventId, event.getId())
                                                          .eq(EventAction::getActionType, LinkageActionTypeEnum.VIDEO_DEVICE_ACTION));
        eventTriggerAction(event, videoAction);
        return event;
    }

    public void eventTrigger(Event event) {
        var videoAction = eventActionService.list(Wrappers.<EventAction>lambdaQuery()
                                                          .eq(EventAction::getEventId, event.getId())
                                                          .eq(EventAction::getActionType, LinkageActionTypeEnum.VIDEO_DEVICE_ACTION));
        eventTriggerAction(event, videoAction);
    }

    public void eventTriggerAction(Event event) {
        eventHandler.executeEventActions(event);
    }

    public void eventTriggerAction(Event event, List<EventAction> eventActions) {
        if (CollectionUtils.isEmpty(eventActions)) {
            return;
        }
        eventHandler.executeEventActions(event, eventActions);
    }
}
