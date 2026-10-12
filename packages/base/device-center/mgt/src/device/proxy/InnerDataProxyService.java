package com.guhecloud.cube.iot.mgt.device.proxy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.iot.mgt.comm.util.SnowflakeBase62Util;
import com.guhecloud.cube.iot.mgt.device.param.DeviceCreateParam;
import com.guhecloud.cube.iot.mgt.device.service.DevDeviceService;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceCreateEvent;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.guhecloud.cube.iot.mgt.core.data.lang.RocketConstToApps.IOT_DEVICE_REG_SUCCEED;
import static com.guhecloud.cube.iot.mgt.core.data.lang.RocketConstToApps.IOT_INNER_DATA_CHANGE_PROXY;

/**
 * 处理一些内部数据<br/>
 * 这里仅做一些代理工作，避免直接修改DeviceCenter中的代码
 */
@RocketMQMessageListener(consumerGroup = "DEVICE_CENTER_INNER_DATA_PROXY", topic = IOT_INNER_DATA_CHANGE_PROXY,
                         selectorType = SelectorType.TAG
)
@Slf4j
@Component
public class InnerDataProxyService implements  RocketMQListener<String> {

    private final DevDeviceService devDeviceService;

    public InnerDataProxyService( DevDeviceService devDeviceService) {
        this.devDeviceService = devDeviceService;
    }

    @Override
    public void onMessage(String message) {
        //目前只有三方id绑定事件
        log.debug("inner data change message:{}", message);

        JSONObject jsonObject = JSON.parseObject(message);
        if (jsonObject.containsKey("payload")) {
            jsonObject = jsonObject.getJSONObject("payload");
        }

        String msgType = jsonObject.getString("type");
        if (StringUtils.isBlank(msgType)) {
            log.error("inner data change message type is null");
            return;
        }

        jsonObject = jsonObject.getJSONObject("dataMsg");
        String tenantId = jsonObject.getString("tenantId");

        if (IOT_DEVICE_REG_SUCCEED.equals(msgType)) {
            DeviceCreateEvent deviceCreateEvent = JSON.toJavaObject(jsonObject, DeviceCreateEvent.class);

            //开河需要绑定开河的租户
            SessionContextHolder.requestTenant(Integer.valueOf(tenantId));

            devDeviceService.addDevice(buildCreateParam(deviceCreateEvent));
        } else {
            log.warn("inner data change message type is not handled:{}", msgType);
        }

    }

    private DeviceCreateParam buildCreateParam(DeviceCreateEvent deviceCreateEvent) {
        DeviceCreateParam deviceCreateParam = new DeviceCreateParam();
        deviceCreateParam.setProductId(deviceCreateEvent.getProductId());
        deviceCreateParam.setDeviceName(deviceCreateEvent.getDeviceName());
        deviceCreateParam.setDeviceNumber(deviceCreateEvent.getDeviceCode());
        deviceCreateParam.setDeviceType(1);
        return deviceCreateParam;
    }

}

