package com.guhecloud.cube.srv.gen.securitymanagement.server.common.rocketmq.consumer;

import com.guhecloud.cube.base.device.center.metadata.event.DeviceChange;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = Const.DEVICE_CHANGE_TOPIC + "_security_device_del",
                         topic = Const.DEVICE_CHANGE_TOPIC, selectorType = SelectorType.TAG
)
public class DelSecurityDeviceInstanceMessageConsumer implements RocketMQListener<DeviceChange> {

    private final SecurityCommonMapper securityCommonMapper;

    @Override
    public void onMessage(DeviceChange message) {
        List<Pair<DeviceInfo, DeviceChangeEnum>> deviceChangePairs = message.getDeviceChangePairs();
        List<Pair<DeviceInfo, DeviceChangeEnum>> pairs = deviceChangePairs.stream()
                .filter(deviceChangePair -> deviceChangePair.getVal() == DeviceChangeEnum.DELETE).collect(Collectors.toList());
        List<Long> deviceIds = new ArrayList<>();
        pairs.forEach(pair -> {
            DeviceInfo key = pair.getKey();
            deviceIds.add(key.getDeviceId());
        });

        if (CollectionUtils.isNotEmpty(deviceIds)) {
            securityCommonMapper.delDeviceInstance(deviceIds, System.currentTimeMillis());
            securityCommonMapper.delCollectionDeviceInstance(deviceIds, System.currentTimeMillis());
        }

    }
}
