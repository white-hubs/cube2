package com.guhecloud.cube.biz.emergency.server.facade.iot.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionDeviceInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionExpInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionInfo;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.IssuanceIotRuleGroupEvent;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceCommandIssuesDTO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 规则事件
 *
 * @author lanchun
 * @date 2022/11/15 16:45
 */
public class EventDefinitionFactory {

    public static Set<DeviceCommandIssuesDTO> buildEventSettingParam(IssuanceIotRuleGroupEvent issuanceIotRuleGroupEvent,
                                                                     List<EmergencyRuleConditionInfo> ruleConditionInfos
                                                                    ) {
        Set<DeviceCommandIssuesDTO> deviceCommandIssuesDTOS = new HashSet<>();
        Map<String, List<EmergencyRuleConditionDeviceInfo>> ruleConditionDeviceMap = issuanceIotRuleGroupEvent
                .getEmergencyRuleConditionDeviceInfos()
                .stream()
                .collect(Collectors.groupingBy(conditionDevice -> conditionDevice.getDeviceProdCategoryCode() + ":" +
                                                                  conditionDevice.getDeviceProdId()));
        ruleConditionInfos.stream().forEach(conditionInfo -> {
            List<EmergencyRuleConditionDeviceInfo> modelConditionDevices = ruleConditionDeviceMap.get(
                    conditionInfo.getDeviceProdCategoryCode() + ":" + conditionInfo.getDeviceProdId());
            if (!CollectionUtils.isEmpty(modelConditionDevices) &&
                !CollectionUtils.isEmpty(conditionInfo.getEmergencyRuleConditionExps())) {
                conditionInfo.getEmergencyRuleConditionExps().stream().forEach(emergencyRuleConditionExpInfo -> {
                    modelConditionDevices.stream().forEach(device -> {
                        DeviceCommandIssuesDTO deviceCommandIssuesDTO = getDeviceCommandIssuesDTO(conditionInfo, emergencyRuleConditionExpInfo, device, emergencyRuleConditionExpInfo.getOprand());
                        deviceCommandIssuesDTOS.add(deviceCommandIssuesDTO);
                    });
                });
            }
        });
        return deviceCommandIssuesDTOS;
    }

    public static Set<DeviceCommandIssuesDTO> buildEventResetSettingParam(IssuanceIotRuleGroupEvent issuanceIotRuleGroupEvent,
                                                                          List<EmergencyRuleConditionInfo> ruleConditionInfos
                                                                         ) {
        Set<DeviceCommandIssuesDTO> deviceCommandIssuesDTOS = new HashSet<>();
        Map<String, List<EmergencyRuleConditionDeviceInfo>> ruleConditionDeviceMap = issuanceIotRuleGroupEvent
                .getEmergencyRuleConditionDeviceInfos()
                .stream()
                .collect(Collectors.groupingBy(conditionDevice -> conditionDevice.getDeviceProdCategoryCode() + ":" +
                                                                  conditionDevice.getDeviceProdId()));
        ruleConditionInfos.stream().forEach(conditionInfo -> {
            List<EmergencyRuleConditionDeviceInfo> modelConditionDevices = ruleConditionDeviceMap.get(
                    conditionInfo.getDeviceProdCategoryCode() + ":" + conditionInfo.getDeviceProdId());
            if (!CollectionUtils.isEmpty(modelConditionDevices) &&
                !CollectionUtils.isEmpty(conditionInfo.getEmergencyRuleConditionExps())) {
                conditionInfo.getEmergencyRuleConditionExps().stream().forEach(emergencyRuleConditionExpInfo -> {
                    modelConditionDevices.stream().forEach(device -> {
                        DeviceCommandIssuesDTO deviceCommandIssuesDTO = getDeviceCommandIssuesDTO(conditionInfo, emergencyRuleConditionExpInfo, device, "reset");
                        deviceCommandIssuesDTOS.add(deviceCommandIssuesDTO);
                    });
                });
            }
        });
        return deviceCommandIssuesDTOS;
    }

    public static DeviceCommandIssuesDTO getDeviceCommandIssuesDTO(EmergencyRuleConditionInfo conditionInfo,
                                                                   EmergencyRuleConditionExpInfo emergencyRuleConditionExpInfo,
                                                                   EmergencyRuleConditionDeviceInfo device, String value
                                                                  ) {
        DeviceCommandIssuesDTO deviceCommandIssuesDTO = new DeviceCommandIssuesDTO();
        deviceCommandIssuesDTO.setDeviceId(device.getDeviceId());
        deviceCommandIssuesDTO.setEventCode(conditionInfo.getDeviceEventCode());
        deviceCommandIssuesDTO.setThresholdCode(emergencyRuleConditionExpInfo.getPropCode());
        deviceCommandIssuesDTO.setValue(value);
        deviceCommandIssuesDTO.setCommandSeq(String.valueOf(IdWorker.getId()));
        return deviceCommandIssuesDTO;
    }
}
