package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.event.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.MessageChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.biz.linkage.client.LinkageMessageClient;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRuleTriggerRecord;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.event.DeviceNotifyRuleTriggerEvent;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.DeviceNotifyRuleTriggerRecordRepository;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class DeviceNotifyRuleTriggerEventHandler {

    private final DeviceNotifyRuleTriggerRecordRepository deviceNotifyRuleTriggerRecordRepository;

    private final LinkageMessageClient linkageMessageClient;

    private final MessageSendClient messageSendClient;

    private static final String MSG_SCENE_CODE = "DEVICE_NOTIFY";

    public DeviceNotifyRuleTriggerEventHandler(DeviceNotifyRuleTriggerRecordRepository deviceNotifyRuleTriggerRecordRepository,
                                               LinkageMessageClient linkageMessageClient, MessageSendClient messageSendClient
                                              ) {
        this.deviceNotifyRuleTriggerRecordRepository = deviceNotifyRuleTriggerRecordRepository;
        this.linkageMessageClient = linkageMessageClient;
        this.messageSendClient = messageSendClient;
    }

    @EventListener
    public void handle(final DeviceNotifyRuleTriggerEvent event) {
        if (CollectionUtils.isEmpty(event.getDeviceNotifyRule().getNotifyConfigs())) {
            return;
        }
        try {
            var deviceNotifyRuleTriggerRecord = buildDeviceNotifyRuleTriggerRecord(event);
            //记录入库
            deviceNotifyRuleTriggerRecordRepository.save(deviceNotifyRuleTriggerRecord);
            event.getDeviceNotifyRule().getNotifyConfigs().forEach(config -> {
                var targetNotifyDTOs = config.getTargets().stream().map(target -> {
                    var targetNotifyDTO = new MessageTargetNotifyDTO();
                    targetNotifyDTO.setMessageTargetNotify(target.getMessageTargetNotify());
                    targetNotifyDTO.setTargetConfig(target.getTargetConfig());
                    return targetNotifyDTO;
                }).toList();
                var channelRequests = config.getChannelTypes().stream().map(channelType -> MessageChannelRequest.builder()
                        .channelCode(channelType)
                        .build()).toList();
                var messageNotifyTargets = linkageMessageClient.buildMessageNotifyTargets(targetNotifyDTOs);
                messageSendClient.sceneSpecifyTmpSend(MessageSpecifyTmpSendRequest.builder()
                                                              .channel(channelRequests)
                                                              .msgSceneCode(MSG_SCENE_CODE)
                                                              .businessId(deviceNotifyRuleTriggerRecord.getId().toString())
                                                              .businessPrincipal(deviceNotifyRuleTriggerRecord)
                                                              .tmpParams(getParamMaps(event))
                                                              .messageNotifyTargets(messageNotifyTargets)
                                                              .tenant(SessionContextHolder
                                                                              .getRequester()
                                                                              .getTenant())
                                                              .build());
            });
        } catch (Exception e) {
            log.error("报警指派，抄送消息发送失败:", e);
        }
    }

    private static DeviceNotifyRuleTriggerRecord buildDeviceNotifyRuleTriggerRecord(DeviceNotifyRuleTriggerEvent event) {
        var record = DeviceNotifyRuleTriggerRecord.builder()
                .id(IdWorker.getId())
                .ruleId(event.getDeviceNotifyRule().getId())
                .deviceId(event.getDeviceDetailDTO().getBaseArchivesInfo().getId())
                .deviceCode(event.getDeviceDetailDTO().getBaseArchivesInfo().getCode())
                .deviceName(event.getDeviceDetailDTO().getBaseArchivesInfo().getName())
                .triggerContent(event.getTriggerContent())
                .triggerTime(LocalDateTime.now())
                .readStatus(false)
                .build();
        if (Objects.nonNull(event.getDeviceDetailDTO().getProductInfo())) {
            record.setProductCategoryName(event.getDeviceDetailDTO().getProductInfo().getProductCategory().getName());
            record.setProdName(event.getDeviceDetailDTO().getProductInfo().getName());
        }
        return record;
    }

    private Map<String, String> getParamMaps(DeviceNotifyRuleTriggerEvent event) {
        Map<String, String> paramMaps = new HashMap<>();
        paramMaps.put("deviceProdName", event.getDeviceDetailDTO().getProductInfo().getName());
        paramMaps.put("deviceName", event.getDeviceDetailDTO().getBaseArchivesInfo().getName());
        return paramMaps;
    }
}
