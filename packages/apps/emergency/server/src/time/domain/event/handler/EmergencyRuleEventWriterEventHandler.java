package com.guhecloud.cube.biz.emergency.server.time.domain.event.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.emergency.server.facade.iot.dto.RuleWriteEventDTO;
import com.guhecloud.cube.biz.emergency.server.facade.iot.event.handler.IncreaseIotRuleEventHandler;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionDeviceInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionExpInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionInfo;
import com.guhecloud.cube.biz.emergency.server.time.core.util.RuleTimeParser;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.IssuanceIotRuleGroupEvent;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleTimeJobPO;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleWriterEventLogPO;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleWriterEventPO;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleWriterEventLogService;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleWriterEventService;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.CommandResponseDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleEventWriterEventHandler {

    private final EmergencyRuleWriterEventService emergencyRuleWriterEventService;

    private final EmergencyRuleWriterEventLogService emergencyRuleWriterEventLogService;

    private final IncreaseIotRuleEventHandler increaseIotRuleEventHandler;

    @EventListener
    public void ruleWriterIotEventHandler(IssuanceIotRuleGroupEvent event) {
        List<EmergencyRuleConditionInfo> conditionInfos = event
                .getEmergencyRuleConditionGroupInfo().getEmergencyRuleConditionInfos();
        List<EmergencyRuleConditionInfo> ruleLinkageCondition = conditionInfos.stream()
                .filter(conditionCommand -> EmergencyConditionTypeEnum.DEVICE_EVENT_CONDITION == conditionCommand.getConditionType()
                                            && "NATURE".equals(conditionCommand.getDeviceEventType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ruleLinkageCondition) &&
            CollectionUtils.isEmpty(event.getEmergencyRuleConditionDeviceInfos())) {
            return;
        }
        EmergencyRuleTimeJobPO emergencyRuleTimeJob = event.getEmergencyRuleTimeJob();
        Set<EmergencyRuleWriterEventPO> emergencyRuleWriterEventPOS = new HashSet<>();
        Set<EmergencyRuleWriterEventLogPO> emergencyRuleWriterEventLogPOS = new HashSet<>();
        Set<RuleWriteEventDTO> ruleWriteEventDTOS = new HashSet<>();
        Map<String, List<EmergencyRuleConditionDeviceInfo>> ruleConditionDeviceMap = event
                .getEmergencyRuleConditionDeviceInfos()
                .stream()
                .collect(Collectors.groupingBy(conditionDevice -> conditionDevice.getDeviceProdCategoryCode() + ":" +
                                                                  conditionDevice.getDeviceProdId()));
        ruleLinkageCondition.forEach(conditionInfo -> {
            List<EmergencyRuleConditionDeviceInfo> modelConditionDevices = ruleConditionDeviceMap.get(
                    conditionInfo.getDeviceProdCategoryCode() + ":" + conditionInfo.getDeviceProdId());
            if (!CollectionUtils.isEmpty(modelConditionDevices) &&
                !CollectionUtils.isEmpty(conditionInfo.getEmergencyRuleConditionExps())) {
                conditionInfo.getEmergencyRuleConditionExps().forEach(emergencyRuleConditionExpInfo -> {
                    modelConditionDevices.forEach(device -> {
                        if (!"close".equals(emergencyRuleTimeJob.getOperateType()) &&
                            StringUtils.isNotBlank(emergencyRuleConditionExpInfo.getOprand())) {
                            EmergencyRuleWriterEventPO emergencyRuleWriterEventPO = getEmergencyRuleWriterEventPO(emergencyRuleTimeJob, conditionInfo, emergencyRuleConditionExpInfo, device);
                            emergencyRuleWriterEventPOS.add(emergencyRuleWriterEventPO);
                            EmergencyRuleWriterEventLogPO emergencyRuleWriterEventLogPO = getEmergencyRuleWriterEventLogPO(emergencyRuleWriterEventPO);
                            emergencyRuleWriterEventLogPOS.add(emergencyRuleWriterEventLogPO);
                            ruleWriteEventDTOS.add(getRuleWriteEventDTO(emergencyRuleWriterEventPO, emergencyRuleWriterEventLogPO));
                        }
                    });
                });
            }
        });
        if (!CollectionUtils.isEmpty(emergencyRuleWriterEventPOS)) {
            this.emergencyRuleWriterEventService.saveBatch(emergencyRuleWriterEventPOS);
            this.emergencyRuleWriterEventLogService.saveBatch(emergencyRuleWriterEventLogPOS);
            boolean issueSuccess = true;
            List<CommandResponseDTO> commandResponseDTOS = null;
            try {
                commandResponseDTOS = writerIotEvent(ruleWriteEventDTOS);
            } catch (Exception e) {
                issueSuccess = false;
                log.error("下发命令失败,失败原因:", e);
            }
            if (issueSuccess) {
                commandResponseDTOS.forEach(commandResponseDTO -> {
                    Optional<EmergencyRuleWriterEventLogPO> first = emergencyRuleWriterEventLogPOS.stream()
                            .filter(emergencyRuleWriterEventLogPO -> commandResponseDTO.getCommandSeq()
                                    .equals(emergencyRuleWriterEventLogPO.getId().toString()))
                            .findFirst();
                    first.ifPresent(emergencyRuleWriterEventLogPO -> {
                        emergencyRuleWriterEventLogPO.setIotWriteStatus(commandResponseDTO.getStatusCode());
                        Optional<EmergencyRuleWriterEventPO> ruleWriterEventPOOptional = emergencyRuleWriterEventPOS.stream()
                                .filter(emergencyRuleWriterEventPO -> emergencyRuleWriterEventLogPO.getEmergencyRuleWriterEventId()
                                        .equals(emergencyRuleWriterEventPO.getId()))
                                .findFirst();
                        ruleWriterEventPOOptional.ifPresent(emergencyRuleWriterEventPO -> {
                            emergencyRuleWriterEventPO.setWriteStatus(commandResponseDTO.getStatusCode());
                        });
                    });
                });
            } else {
                emergencyRuleWriterEventPOS
                        .forEach(emergencyRuleWriterEventPO -> emergencyRuleWriterEventPO.setWriteStatus("FAIL"));
                emergencyRuleWriterEventLogPOS
                        .forEach(emergencyRuleWriterEventLogPO -> emergencyRuleWriterEventLogPO.setIotWriteStatus("FAIL"));
            }
            this.emergencyRuleWriterEventService.updateBatchById(emergencyRuleWriterEventPOS);
            this.emergencyRuleWriterEventLogService.updateBatchById(emergencyRuleWriterEventLogPOS);
        }
    }

    public List<CommandResponseDTO> writerIotEvent(Set<RuleWriteEventDTO> ruleWriteEventDTOS) {
        return this.increaseIotRuleEventHandler.issuedEmergencyRuleEvent(ruleWriteEventDTOS);
    }

    public RuleWriteEventDTO getRuleWriteEventDTO(EmergencyRuleWriterEventPO emergencyRuleWriterEventPO,
                                                  EmergencyRuleWriterEventLogPO emergencyRuleWriterEventLogPO
                                                 ) {
        return RuleWriteEventDTO.builder()
                .id(emergencyRuleWriterEventLogPO.getId())
                .deviceId(emergencyRuleWriterEventPO.getDeviceId())
                .eventCode(emergencyRuleWriterEventPO.getEventCode())
                .thresholdCode(emergencyRuleWriterEventPO.getThresholdCode())
                .thresholdValue(emergencyRuleWriterEventPO.getThresholdValue())
                .build();
    }

    public EmergencyRuleWriterEventLogPO getEmergencyRuleWriterEventLogPO(EmergencyRuleWriterEventPO emergencyRuleWriterEventPO) {
        return EmergencyRuleWriterEventLogPO.builder()
                .id(IdWorker.getId())
                .emergencyRuleWriterEventId(emergencyRuleWriterEventPO.getId())
                .emergencyRuleId(emergencyRuleWriterEventPO.getEmergencyRuleId())
                .emergencyRuleTimeId(emergencyRuleWriterEventPO.getEmergencyRuleTimeId())
                .build();
    }

    public EmergencyRuleWriterEventPO getEmergencyRuleWriterEventPO(EmergencyRuleTimeJobPO emergencyRuleTimeJob,
                                                                    EmergencyRuleConditionInfo conditionInfo,
                                                                    EmergencyRuleConditionExpInfo emergencyRuleConditionExpInfo,
                                                                    EmergencyRuleConditionDeviceInfo device
                                                                   ) {
        return EmergencyRuleWriterEventPO.builder()
                .id(IdWorker.getId())
                .emergencyRuleId(emergencyRuleTimeJob.getEmergencyRuleId())
                .emergencyRuleTimeId(emergencyRuleTimeJob.getEmergencyRuleTimeId())
                .deviceId(device.getDeviceId())
                .eventCode(conditionInfo.getDeviceEventCode())
                .thresholdCode(emergencyRuleConditionExpInfo.getPropCode())
                .thresholdValue("close".equals(emergencyRuleTimeJob.getOperateType()) ? "reset" : emergencyRuleConditionExpInfo.getOprand())
                .startTime(Objects.nonNull(emergencyRuleTimeJob.getTriggerLastTime())
                           ? DateUtils.parse(emergencyRuleTimeJob.getTriggerLastTime()) : null)
                .endTime(RuleTimeParser.getRileTimeJobEndTime(emergencyRuleTimeJob))
                .retryCount(0)
                .build();
    }
}
