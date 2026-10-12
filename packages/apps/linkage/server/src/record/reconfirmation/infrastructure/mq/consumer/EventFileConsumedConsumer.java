package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.mq.consumer;

import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.biz.linkage.server.record.infrastructure.repository.EventRecordRepository;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.enumeration.AiReconfirmResultEnum;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.event.message.IotEventFileConsumedMessage;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.valueobject.EventAiConfig;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.dto.ChatResult;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.service.EventImageRecognitionFacadeService;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.repository.EventAiResultService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 事件附件消费
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "EVENT_FILE", topic = IotEventFileConsumedMessage.TOPIC, selectorType = SelectorType.TAG)
public class EventFileConsumedConsumer implements RocketMQListener<IotEventFileConsumedMessage> {

    private final EventRecordRepository eventRecordRepository;

    private final EventImageRecognitionFacadeService eventImageRecognitionFacadeService;

    private final DataDictionaryAdapter dataDictionaryAdapter;

    private final EventAiResultService eventAiResultService;

    private static final String SUB_SYSTEM_CODE = "THIRD_SERVICE";

    private static final String KEY = "AI::ALARM_IMAGE::RECOGNITION";

    @Override
    public void onMessage(IotEventFileConsumedMessage message) {
        //获取事件
        var event = this.eventRecordRepository.detailByEventId(message.getEventId());
        if (Objects.isNull(event)) {
            return;
        }
        try {
            var param = new DataDictionaryaramByItemAndCodeParam();
            param.setItemCode(SUB_SYSTEM_CODE);
            param.setCode(KEY);
            var dictionary = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);
            if (Objects.isNull(dictionary) || StringUtils.isBlank(dictionary.getVariable())) {
                log.warn("获取事件ai识别配置为空");
                return;
            }
            var eventAiConfig = JsonUtils.deserialize(dictionary.getVariable(), EventAiConfig.class);
            if (Objects.isNull(eventAiConfig)) {
                log.warn("获取事件ai识别配置为空");
                return;
            }
            if (Boolean.FALSE.equals(eventAiConfig.getEnabled()) ||
                !eventAiConfig.getAlarmEventTypeCodes().contains(event.getEventType().getCode())) {
                return;
            }
            if (CollectionUtils.isEmpty(eventAiConfig.getAppointDevices()) ||
                !eventAiConfig.getAppointDevices().contains(event.getEventDevice().getId().toString())) {
                return;
            }
            if (StringUtils.isNotBlank(eventAiConfig.getImageHostAddress())) {
                message.setFilePath(eventAiConfig.getImageHostAddress() + message.getFilePath());
            }
            String result = eventImageRecognitionFacadeService.eventImageRecognition(eventAiConfig.getCueWord(), message.getFilePath());
            if (StringUtils.isBlank(result)) {
                log.warn("事件记录为:{},ai图像识别结果为空", event.getBaseInfo().getId());
                return;
            }
            log.info("事件记录为:{},ai图像识别结果为:{}", event.getBaseInfo().getId(), result);
            var chatResult = JsonUtils.deserialize(result, ChatResult.class);
            //ai识别结果
            if (Objects.nonNull(chatResult)) {
                var aiHandlerResult =
                        chatResult.getResult() ? AiReconfirmResultEnum.CONFIRM_ALARM : AiReconfirmResultEnum.SUSPECTED_ALARM;
                this.eventAiResultService.addEventAiResult(event, result, aiHandlerResult);
            }
        } catch (Exception e) {
            log.error("事件ai二次识别失败,记录id:{}", event.getBaseInfo().getId(), e);
        }
    }
}
