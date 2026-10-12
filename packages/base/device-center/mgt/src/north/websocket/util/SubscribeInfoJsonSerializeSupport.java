package com.guhecloud.cube.iot.mgt.north.websocket.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.iot.mgt.core.data.base.Pair;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType;
import com.guhecloud.cube.iot.mgt.core.data.north.NotifyOut;
import com.guhecloud.cube.iot.mgt.core.data.north.SubscribeInfo;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.DeviceCommandCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.DeviceEventCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.DevicePropCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.DeviceStatusCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.RuleTriggerEventCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.SubscribeCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.outparam.WebSocketParam;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class SubscribeInfoJsonSerializeSupport {

    private SubscribeInfoJsonSerializeSupport() {}

    /**
     * JSON转换为SubscribeInfo
     *
     * @param json JSON
     *
     * @return SubscribeInfo
     *
     * @throws SubscribeInfoParseException 解析错误
     */
    public static SubscribeInfo json2Object(String json) {
        if (StringUtils.isBlank(json)) {
            throw new SubscribeInfoParseException("无法解析 SubscribeInfo json 为空");
        }

        JSONObject jsonObject = JSON.parseObject(json);

        //不再校验输出推送方式,mgt仅考虑websocket推送
        Optional<NotifyOut> notifyOutOpt = toNotifyOut(jsonObject.getString("notifyOut"));

        if (!notifyOutOpt.isPresent()) {
            log.warn("SubscribeInfoJsonSerializeSupport json2Object notifyOut is null, json: {}", json);
            throw new SubscribeInfoParseException("notifyOut 未知");
        }

        NotifyOut notifyOut = notifyOutOpt.get();
        if (notifyOut != NotifyOut.WEBSOCKET) {
            log.warn("iot-mgt 仅支持Websocket方法, json: {}", json);
            throw new SubscribeInfoParseException("notifyOut 仅支持 WEBSOCKET");
        }

        WebSocketParam outParam = JSON.parseObject(jsonObject.getString("outParam"), WebSocketParam.class);

        long subscribeTTS = toSubscribeTTS(json);

        List<Pair<NotifyMsgType, SubscribeCondition>> subscribeMsgTypes = toSubscribeMsgTypes(jsonObject.getString("subscribeMsgTypes"))
                .orElseThrow(() -> new SubscribeInfoParseException("subscribeMsgTypes 未知"));

        //构建SubscribeInfo
        SubscribeInfo subscribeInfo = new SubscribeInfo();
        subscribeInfo.setOutParam(outParam);
        subscribeInfo.setSubscribeTTS(subscribeTTS);
        subscribeInfo.setSubscribeMsgTypes(subscribeMsgTypes);

        return subscribeInfo;
    }

    /**
     * 转换为SubscribeMsgTypes
     *
     * @param subscribeMsgTypeJSON 订阅消息类型JSON
     *
     * @return SubscribeMsgTypes
     */
    public static Optional<List<Pair<NotifyMsgType, SubscribeCondition>>> toSubscribeMsgTypes(String subscribeMsgTypeJSON) {
        JSONArray jsonArray = JSON.parseArray(subscribeMsgTypeJSON);

        if (jsonArray == null || jsonArray.isEmpty()) {
            return Optional.empty();
        }

        List<Pair<NotifyMsgType, SubscribeCondition>> subscribeMsgTypes = new java.util.ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            NotifyMsgType notifyMsgType = NotifyMsgType.valueOf(jsonObject.getString("key"));
            SubscribeCondition subscribeCondition = toSubscribeCondition(notifyMsgType, jsonObject.getString("val"))
                    .orElseThrow(() -> new SubscribeInfoParseException("subscribeCondition 未知"));

            subscribeMsgTypes.add(new Pair<>(notifyMsgType, subscribeCondition));
        }

        return Optional.of(subscribeMsgTypes);
    }

    /**
     * 转换为SubscribeCondition
     *
     * @param notifyMsgType          通知消息类型
     * @param subscribeConditionJSON 订阅条件JSON
     *
     * @return SubscribeCondition
     */
    public static Optional<SubscribeCondition> toSubscribeCondition(NotifyMsgType notifyMsgType, String subscribeConditionJSON) {
        switch (notifyMsgType) {
            case DEVICE_PROP:
                return Optional.ofNullable(JSON.parseObject(subscribeConditionJSON, DevicePropCondition.class));
            case DEVICE_EVENT:
                return Optional.ofNullable(JSON.parseObject(subscribeConditionJSON, DeviceEventCondition.class));
            case DEVICE_STATUS:
                return Optional.ofNullable(JSON.parseObject(subscribeConditionJSON, DeviceStatusCondition.class));
            case RULE_TRIGGER_EVENT:
                return Optional.ofNullable(JSON.parseObject(subscribeConditionJSON, RuleTriggerEventCondition.class));
            case DEVICE_COMMAND:
                return Optional.ofNullable(JSON.parseObject(subscribeConditionJSON, DeviceCommandCondition.class));

            default:
                return Optional.empty();
        }
    }

    /**
     * 转换为NotifyOut
     *
     * @param notifyOut 通知输出方式
     *
     * @return NotifyOut
     */
    public static Optional<NotifyOut> toNotifyOut(String notifyOut) {
        if (StringUtils.isBlank(notifyOut)) {
            return Optional.empty();
        }

        return Optional.of(NotifyOut.valueOf(notifyOut));
    }

    /**
     * 获取订阅时长 单位秒
     *
     * @param json 订阅信息
     *
     * @return 订阅时长 单位秒
     */
    public static long toSubscribeTTS(String json) {
        if (StringUtils.isBlank(json)) {
            throw new SubscribeInfoParseException("无法解析 SubscribeInfo json 为空");
        }

        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getLong("subscribeTTS");
    }
}
