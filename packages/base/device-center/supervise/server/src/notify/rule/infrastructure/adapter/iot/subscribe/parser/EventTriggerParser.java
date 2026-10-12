package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.subscribe.parser;

import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductEventDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceEventTriggerMsg;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EventTriggerParser {

    private final IotDeviceClient iotDeviceClient;

    public EventTriggerParser(IotDeviceClient iotDeviceClient) {this.iotDeviceClient = iotDeviceClient;}

    public String parseSnapshot(DeviceEventTriggerMsg deviceEventTriggerMsg) {
        if (Objects.isNull(deviceEventTriggerMsg)) {
            return null;
        }
        //获取设备详情
        var deviceDetailParam = new DeviceDetailParam();
        deviceDetailParam.setDeviceId(deviceEventTriggerMsg.getDeviceId());
        deviceDetailParam.setWithEventSpec(true);
        var detail = iotDeviceClient.detail(deviceDetailParam);
        var eventName = detail.getProductEventVoList().stream()
                .filter(event -> event.getEventCode().equals(deviceEventTriggerMsg.getEventCode()))
                .map(ProductEventDTO::getEventName)
                .findFirst()
                .orElse("");
        String template = "%s 触发 %s 事件";
        return String.format(template, detail.getDeviceName(), eventName);
    }
}
