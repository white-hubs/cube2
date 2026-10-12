package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.consumer;

import com.guhecloud.cube.base.device.center.metadata.event.DeviceChange;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionDeviceService;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.rocketmq.message.EmergencyChangeMessage;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "emergency_device_del",
                         topic = Const.DEVICE_CHANGE_TOPIC, selectorType = SelectorType.TAG
)
public class DelEmergencyDeviceInstanceMessageConsumer implements RocketMQListener<DeviceChange> {

    private final EmergencyRuleConditionDeviceService emergencyRuleConditionDeviceService;

    @Override
    public void onMessage(DeviceChange message) {
        List<Pair<DeviceInfo, DeviceChangeEnum>> deviceChangePairs = message.getDeviceChangePairs();
        List<Pair<DeviceInfo, DeviceChangeEnum>> pairs = deviceChangePairs.stream()
                .filter(deviceChangePair -> deviceChangePair.getVal() == DeviceChangeEnum.DELETE).toList();
        Set<Long> deviceIds = new HashSet<>();
        pairs.forEach(pair -> {
            DeviceInfo key = pair.getKey();
            deviceIds.add(key.getDeviceId());
        });
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            //发生变更的规则
            Set<Long> roleIdsByDeviceIds = emergencyRuleConditionDeviceService.getRoleIdsByDeviceIds(deviceIds);
            emergencyRuleConditionDeviceService.removeByDeviceIds(deviceIds);
            roleIdsByDeviceIds.forEach(roleId -> {
                EmergencyChangeMessage emergencyChangeMessage = new EmergencyChangeMessage();
                emergencyChangeMessage.setId(roleId);
                RocketMQContextHolder.send(EmergencyChangeMessage.TOPIC, emergencyChangeMessage);
            });
        }
    }
}
