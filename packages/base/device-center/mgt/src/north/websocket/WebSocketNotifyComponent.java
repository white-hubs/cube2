package com.guhecloud.cube.iot.mgt.north.websocket;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgVo;
import com.guhecloud.cube.iot.mgt.core.data.north.NotifyOut;
import com.guhecloud.cube.iot.mgt.core.data.north.outparam.WebSocketParam;
import com.guhecloud.cube.iot.mgt.core.def.SubscribeInterface;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.KeyValueWithTs;
import jakarta.annotation.Resource;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.guhecloud.cube.iot.mgt.core.data.north.NotifyOut.WEBSOCKET;

@Component
public class WebSocketNotifyComponent {
    @Resource
    private NorthSocketAdapter northSocketAdapter;
    @Resource
    private SubscribeContainer subscribeContainer;
    @Resource
    private SubscribeInterface subscribeInterface;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent applicationReadyEvent) {
        // 注册消息处理器
        subscribeContainer.registerDevicePropMsgHandler(msg -> {
            //这里msg可能会推送多次存在效率问题，右面再优化吧
            String jsonMsg = JSON.toJSONString(new NotifyMsgVo(NotifyMsgType.DEVICE_PROP, msg));
            for (KeyValueWithTs update : msg.getUpdates()) {
                subscribeInterface.queryDeviceProp(msg.getDeviceId(), update.getKey())
                        .forEach(webSocketParam -> doPushDeviceProp(webSocketParam, jsonMsg));
            }
        });
        subscribeContainer.registerDeviceStatusMsgHandler(msg -> {
            String jsonMsg = JSON.toJSONString(new NotifyMsgVo(NotifyMsgType.DEVICE_STATUS, msg));
            subscribeInterface.queryDeviceStatus(msg.getDeviceId())
                    .forEach(webSocketParam -> doPushDeviceStatus(webSocketParam, jsonMsg));
        });
        subscribeContainer.registerRuleTriggerEventMsgHandler(msg -> {
            //预留坑位
        });
        subscribeContainer.registerDeviceEventMsgHandler(msg -> {
            String jsonMsg = JSON.toJSONString(new NotifyMsgVo(NotifyMsgType.DEVICE_EVENT, msg));
            subscribeInterface.queryDeviceEvent(msg.getDeviceId(), msg.getEventCode())
                    .forEach(webSocketParam -> doPushDeviceEvent(webSocketParam, jsonMsg));
        });
        subscribeContainer.registerDeviceEventAdditionalMsgHandler(msg -> {
            String jsonMsg = JSON.toJSONString(new NotifyMsgVo(NotifyMsgType.DEVICE_EVENT_ADDITION, msg));
            subscribeInterface.queryDeviceEvent(String.valueOf(msg.getDeviceId()), msg.getEventCode())
                    .forEach(webSocketParam -> doPushEventAdditional(webSocketParam, jsonMsg));
        });
        subscribeContainer.registerDeviceCommandMsgHandler(msg -> {
            String jsonMsg = JSON.toJSONString(new NotifyMsgVo(NotifyMsgType.DEVICE_COMMAND, msg));
            subscribeInterface.queryCommand(msg.getInstanceId())
                    .forEach(webSocketParam -> doPushCommandResponse(webSocketParam, jsonMsg));
        });
    }

    protected void doPushDeviceProp(WebSocketParam webSocketParam, String jsonMsg) {
        String clientId = webSocketParam.getClientId();
        northSocketAdapter.sendMsg(clientId, NotifyMsgType.DEVICE_PROP, jsonMsg);
    }

    protected void doPushDeviceStatus(WebSocketParam webSocketParam, String jsonMsg) {
        String clientId = webSocketParam.getClientId();
        northSocketAdapter.sendMsg(clientId, NotifyMsgType.DEVICE_STATUS, jsonMsg);
    }

    protected void doPushRuleEvent(WebSocketParam webSocketParam, String jsonMsg) {
        String clientId = webSocketParam.getClientId();
        northSocketAdapter.sendMsg(clientId, NotifyMsgType.RULE_TRIGGER_EVENT, jsonMsg);
    }

    protected void doPushDeviceEvent(WebSocketParam webSocketParam, String jsonMsg) {
        String clientId = webSocketParam.getClientId();
        northSocketAdapter.sendMsg(clientId, NotifyMsgType.DEVICE_EVENT, jsonMsg);
    }

    protected void doPushEventAdditional(WebSocketParam webSocketParam, String jsonMsg) {
        String clientId = webSocketParam.getClientId();
        northSocketAdapter.sendMsg(clientId, NotifyMsgType.DEVICE_EVENT_ADDITION, jsonMsg);
    }

    protected void doPushCommandResponse(WebSocketParam webSocketParam, String jsonMsg) {
        String clientId = webSocketParam.getClientId();
        northSocketAdapter.sendMsg(clientId, NotifyMsgType.DEVICE_COMMAND, jsonMsg);
    }

    public NotifyOut supportedNotifyOut() {
        return WEBSOCKET;
    }
}
