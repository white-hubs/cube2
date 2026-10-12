package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.rocketmq.consumer;

import com.guhecloud.cube.base.message.sdk.MessageReceiverSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message.sdk.api.request.MessageReceiverChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageReceiverRequest;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.DevicePositionMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.rocketmq.message.DeviceLifeEventMessage;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WholesalerDeviceVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = DeviceLifeEventMessage.TOPIC + "_security_coldWater",
                         topic = DeviceLifeEventMessage.TOPIC, selectorType = SelectorType.TAG
)
@Data
public class DeviceLifeEventConsumer implements RocketMQListener<DeviceLifeEventMessage> {

    private final DevicePositionMapper devicePositionMapper;

    private final MessageReceiverSendClient messageReceiverSendClient;

    @Override
    public void onMessage(DeviceLifeEventMessage message) {
        if (message.getApplicationCode().equals("COOLINGWATER")) {
            for (Long deviceId : message.getDeviceIds()) {
                try {
                    SessionContextHolder.requestTenant(message.getTenant());
                    List<WholesalerDeviceVo> deviceList = devicePositionMapper.getDeviceList(deviceId);
                    WholesalerDeviceVo wholesalerDeviceVo = deviceList.get(0);
                    if (StringUtils.isNotEmpty(wholesalerDeviceVo.getShiftMasterPhone())) {
                        MessageReceiverRequest messageReceiverRequest = new MessageReceiverRequest();
                        Set<String> set = new HashSet<>();
                        set.add(wholesalerDeviceVo.getShiftMasterPhone());
                        MessageReceiverChannelRequest build = MessageReceiverChannelRequest.builder()
                                .receiver(set)
                                .channelCode(MessageChannelEnum.CHANNEL_SMS).build();
                        messageReceiverRequest.setReceiverChannels(Collections.singletonList(build));
                        messageReceiverRequest.setMsgSceneCode("COOLINGWATER_ALARM_MERCHANT_NOTIFY");
                        Map<String, String> tmpParams = new HashMap<>();
                        tmpParams.put("gearPositionArea", wholesalerDeviceVo.getGearPositionArea());
                        tmpParams.put("shiftMaster", wholesalerDeviceVo.getShiftMaster());
                        tmpParams.put("gearPositionCode", wholesalerDeviceVo.getGearPositionCode());
                        tmpParams.put("deviceCode", wholesalerDeviceVo.getDeviceCode());
                        tmpParams.put("eventName", message.getEventName());
                        messageReceiverRequest.setTmpParams(tmpParams);
                        messageReceiverRequest.setTenant(message.getTenant());
                        messageReceiverSendClient.receiverSend(messageReceiverRequest);
                        log.info("冷冻水发消息成功===={}", messageReceiverRequest);
                    }
                } catch (Exception e) {
                    log.info("冷冻水发消息失败=============={}", e);
                }

            }
        }
    }
}
