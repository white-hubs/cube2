package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.application.event.handler;

import com.guhecloud.cube.biz.linkage.server.record.domain.aggregates.EventRecord;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.enumeration.AiReconfirmResultEnum;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.event.AiReConfirmResultEvent;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.valueobject.EventSnapshot;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.repository.EventAiResultService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

@Slf4j
@Component
public class IotReConfirmEventHandler {

    private final EventAiResultService eventAiResultService;

    private final ApplicationEventPublisher eventPublisher;

    private static final String ASSIST_RECONFIRM_RESULT_KEY = "@@checkResult@@";

    public IotReConfirmEventHandler(EventAiResultService eventAiResultService, ApplicationEventPublisher eventPublisher) {
        this.eventAiResultService = eventAiResultService;
        this.eventPublisher = eventPublisher;
    }

    public void iotReConfirmEvent(EventRecord eventRecord, String eventSnapshot) {
        try {
            var eventAiResult = eventAiResultService.getEventAiResult(eventRecord.getBaseInfo().getId());
            if (Objects.nonNull(eventAiResult)) {
                log.info("已存在二次确认结果,eventId:{}", eventRecord.getEventDevice().getId());
                return;
            }
            var checkResult = getEventSnapshotReconfirmResult(eventSnapshot);
            if (StringUtils.isBlank(checkResult)) {
                return;
            }
            var aiReconfirmResult =
                    "1".equals(checkResult) ? AiReconfirmResultEnum.CONFIRM_ALARM : AiReconfirmResultEnum.SUSPECTED_ALARM;
            //新增处理记录日志信息
            this.eventAiResultService.addEventAiResult(eventRecord, checkResult, aiReconfirmResult);
            //发送事件确认结果
            this.eventPublisher.publishEvent(new AiReConfirmResultEvent(this, eventRecord.getBaseInfo()
                    .getId(), eventSnapshot, aiReconfirmResult));
        } catch (Exception e) {
            log.error("解析二次确认信息失败", e);
        }
    }

    public AiReconfirmResultEnum convertIotEventResult(String orgResult) {
        if (StringUtils.isBlank(orgResult)) {
            return null;
        }
        return "1".equals(orgResult) ? AiReconfirmResultEnum.CONFIRM_ALARM
                                     : AiReconfirmResultEnum.SUSPECTED_ALARM;
    }

    public String getEventSnapshotReconfirmResult(String eventSnapshotStr) {
        //从事件快照中解析是否存在二次确认信息
        var eventSnapshot = JsonUtils.deserialize(eventSnapshotStr, EventSnapshot.class);
        if (Objects.isNull(eventSnapshot) || CollectionUtils.isEmpty(eventSnapshot.getRelatedProp())) {
            return null;
        }
        var relatedProp = eventSnapshot.getRelatedProp();
        if (!relatedProp.containsKey(ASSIST_RECONFIRM_RESULT_KEY)) {
            return null;
        }
        return relatedProp.get(ASSIST_RECONFIRM_RESULT_KEY).toString();
    }
}
