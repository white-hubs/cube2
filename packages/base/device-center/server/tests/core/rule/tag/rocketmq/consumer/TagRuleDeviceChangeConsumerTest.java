package com.guhecloud.cube.base.device.center.core.rule.tag.rocketmq.consumer;

import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceMapper;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceChange;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceChangeEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.List;

public class TagRuleDeviceChangeConsumerTest {

    @Test
    public void onMessageSkipsChangeWhenDeviceWasNotVisibleToConsumerYet() {
        TagRuleDeviceChangeConsumer consumer = new TagRuleDeviceChangeConsumer(null, mapperReturningNoDevice(), null);
        DeviceChange deviceChange = new DeviceChange();
        deviceChange.setDeviceChangePairs(List.of(Pair.of(DeviceInfo.of(42L, 100L), DeviceChangeEnum.CREATE)));

        consumer.onMessage(deviceChange);
    }

    @Test
    public void onMessageSkipsEmptyDeviceChangeMessage() {
        TagRuleDeviceChangeConsumer consumer = new TagRuleDeviceChangeConsumer(null, mapperReturningNoDevice(), null);
        DeviceChange deviceChange = new DeviceChange();
        deviceChange.setDeviceChangePairs(List.of());

        consumer.onMessage(deviceChange);
    }

    private static CenterBizDeviceMapper mapperReturningNoDevice() {
        return (CenterBizDeviceMapper) Proxy.newProxyInstance(
                CenterBizDeviceMapper.class.getClassLoader(),
                new Class<?>[] {CenterBizDeviceMapper.class},
                (proxy, method, args) -> null
        );
    }
}
