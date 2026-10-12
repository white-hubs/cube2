package com.guhecloud.cube.iot.mgt.north.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.iot.mgt.core.data.base.Pair;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.KeyValueWithTs;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.LivePropertyUpdateEvent;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgVo;
import com.guhecloud.cube.iot.mgt.core.data.north.SubscribeInfo;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.DevicePropCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.SubscribeCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.outparam.WebSocketParam;
import com.guhecloud.cube.iot.mgt.core.def.cache.DeviceCacheSupport;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgIn;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgInHeartBeat;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgInSubscribe;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgInUnSubscribe;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgOutByeBye;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgOutHeartBeat;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgOutSubscribePush;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgOutSubscribeResult;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgOutUnSubscribe;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgOutWelcome;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgTypeEnum;
import com.guhecloud.cube.iot.mgt.north.websocket.util.SubscribeInfoJsonSerializeSupport;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator.OverflowStrategy.DROP;

/**
 * Socket交互
 *
 * @author fl
 **/
@Slf4j
public class NorthSocketServer extends TextWebSocketHandler {

    private static final int SEND_TIME_LIMIT = 5000;//5s
    private static final int BUFFER_SIZE_LIMIT = 1024 * 1024;// 1M
    @Resource
    private NorthSocketAdapter northSocketAdapter;
    @Resource
    private DeviceCacheSupport deviceCacheSupport;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        northSocketAdapter.register(new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, BUFFER_SIZE_LIMIT, DROP));

        try {
            sendWelcomeMsg(session);
        } catch (IOException e) {
            afterConnectionClosed(session, CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String message = textMessage.getPayload();

        log.debug("websocket message--->>{}", message);

        if (StringUtils.isBlank(message)) {
            return;
        }

        try {
            MsgIn msgIn = toMsg(message);
            if (msgIn instanceof MsgInHeartBeat) {
                replyHeartBeat(session, (MsgInHeartBeat) msgIn);
                return;
            }

            if (msgIn instanceof MsgInSubscribe) {
                MsgInSubscribe msgInSubscribe = (MsgInSubscribe) msgIn;
                log.debug("收到订阅消息: {}", msgInSubscribe.getContent());

                subscribe(session, msgInSubscribe);
                return;
            }

            if (msgIn instanceof MsgInUnSubscribe) {
                MsgInUnSubscribe msgInUnSubscribe = (MsgInUnSubscribe) msgIn;
                log.debug("收到取消订阅消息: {}", msgInUnSubscribe.getContent());

                northSocketAdapter.unSubscribe(msgInUnSubscribe.getSubscribeId(), session.getId());

                replyUnsubscribe(session, msgInUnSubscribe, true);
            }
        } catch (Exception e) {
            MsgOutByeBye msg = new MsgOutByeBye();
            msg.setMsgType(MsgTypeEnum.BYE_BYE);
            msg.setContent("异常捕获:" + e.getMessage());
            northSocketAdapter.sendMessage(session, JSON.toJSONString(msg));

            afterConnectionClosed(session, CloseStatus.SERVER_ERROR);
            log.error("订阅失败", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        northSocketAdapter.release(session.getId());
    }

    public void sendWelcomeMsg(WebSocketSession session) throws IOException {
        MsgOutWelcome msg = new MsgOutWelcome();
        msg.setMsgType(MsgTypeEnum.WELCOME);
        msg.setContent("connect success");
        northSocketAdapter.sendMessage(session, JSON.toJSONString(msg));
    }

    /**
     * 转换为MsgIn
     *
     * @param message 消息
     *
     * @return MsgIn
     */
    private MsgIn toMsg(String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        String msgType = jsonObject.getString("msgType");
        String content = jsonObject.getString("content");

        if (StringUtils.isBlank(msgType)) {
            throw new IllegalArgumentException("消息类型不能为空");
        }

        MsgTypeEnum msgTypeEnum = MsgTypeEnum.toMsgTypeEnum(msgType);
        switch (msgTypeEnum) {
            case HEART_BEAT:
                MsgInHeartBeat msg = new MsgInHeartBeat();
                msg.setMsgType(msgTypeEnum);
                msg.setContent(content);
                msg.setTs(Long.parseLong(content));
                return msg;
            case SUBSCRIBE:
                MsgInSubscribe msgInSubscribe = new MsgInSubscribe();
                msgInSubscribe.setMsgType(msgTypeEnum);

                JSONObject jsonObj = JSON.parseObject(content);
                long subscribeTTSWithSecond = jsonObj.getLongValue("subscribeTTSWithSecond");
                String subscribeInfoStr = jsonObj.getString("subscribeMsgTypes");

                Optional<List<Pair<NotifyMsgType, SubscribeCondition>>> condition = SubscribeInfoJsonSerializeSupport.toSubscribeMsgTypes(subscribeInfoStr);
                msgInSubscribe.setSubscribeMsgTypes(condition.orElseThrow(() -> new IllegalArgumentException("订阅失败, 未指定订阅参数")));
                msgInSubscribe.setSubscribeTTSWithSecond(subscribeTTSWithSecond);
                return msgInSubscribe;

            case UNSUBSCRIBE:
                MsgInUnSubscribe msgInUnSubscribe = new MsgInUnSubscribe();
                msgInUnSubscribe.setMsgType(msgTypeEnum);
                msgInUnSubscribe.setContent(content);
                msgInUnSubscribe.setSubscribeId(content);
                return msgInUnSubscribe;
            default:
                throw new IllegalArgumentException("消息类型不支持");
        }
    }

    /**
     * 处理Socket订阅
     *
     * @param msgInSubscribe 消息
     *
     * @throws IOException IOException
     */
    private void subscribe(WebSocketSession session, MsgInSubscribe msgInSubscribe) throws IOException {
        //订阅转换
        SubscribeInfo subscribeInfo = new SubscribeInfo();
        subscribeInfo.setOutParam(WebSocketParam.build(UUID.randomUUID().toString().replace("-", ""),
                                                       session.getAttributes().get("tenant").toString()));
        subscribeInfo.setSubscribeTTS(msgInSubscribe.getSubscribeTTSWithSecond());
        subscribeInfo.setSubscribeMsgTypes(msgInSubscribe.getSubscribeMsgTypes());

        String subscribeId = northSocketAdapter.subscribe(subscribeInfo, session.getId());

        MsgOutSubscribeResult msgOutSubscribeResult = new MsgOutSubscribeResult();
        msgOutSubscribeResult.setMsgType(MsgTypeEnum.SUBSCRIBE);
        msgOutSubscribeResult.setContent(subscribeId);
        northSocketAdapter.sendMessage(session, JSON.toJSONString(msgOutSubscribeResult));

        //如果有属性订阅，推送一次订阅的属性值immediately
        pushDevicePropImmediately(session, msgInSubscribe);
    }

    /**
     * 立即推送订阅的设备属性
     *
     * @param session        WebSocket会话
     * @param msgInSubscribe 订阅消息
     *
     * @throws IOException IOException
     */
    private void pushDevicePropImmediately(WebSocketSession session, MsgInSubscribe msgInSubscribe) throws IOException {

        //需求变更，订阅后立即推送一次订阅的属性值
        List<DevicePropCondition> subscribeConditions = new ArrayList<>();
        List<Pair<NotifyMsgType, SubscribeCondition>> subscribeMsgTypes = msgInSubscribe.getSubscribeMsgTypes();
        for (Pair<NotifyMsgType, SubscribeCondition> subscribeMsgType : subscribeMsgTypes) {
            if (subscribeMsgType.getKey() == NotifyMsgType.DEVICE_PROP && subscribeMsgType.getVal() instanceof DevicePropCondition) {
                subscribeConditions.add((DevicePropCondition) subscribeMsgType.getVal());
            }
        }

        //如果没有订阅属性，则不推送
        if (subscribeConditions.isEmpty()) {
            return;
        }

        for (DevicePropCondition subscribeCondition : subscribeConditions) {
            Map<String, List<String>> deviceIdAndPropCode = subscribeCondition.toDeviceIdAndPropCode();
            deviceIdAndPropCode.forEach((deviceId, propCodes) -> {
                LivePropertyUpdateEvent livePropertyUpdateEvent = new LivePropertyUpdateEvent();
                livePropertyUpdateEvent.setDeviceId(deviceId);
                livePropertyUpdateEvent.setUpdates(new ArrayList<>());

                propCodes.forEach(propCode ->
                                          deviceCacheSupport.queryDevicePropCache(Long.valueOf(deviceId), propCode)//根据设备ID+code查询快照
                                                  .ifPresent(deviceProp ->
                                                                     livePropertyUpdateEvent.getUpdates() //如果存在构建KeyValueWithTs
                                                                             .add(KeyValueWithTs.toKeyValueWithTs(propCode,
                                                                                                                  deviceProp.getValue(),
                                                                                                                  deviceProp.getPreValue(),
                                                                                                                  false,
                                                                                                                  deviceProp.getTs()))
                                                            )
                                 );

                NotifyMsgVo notifyMsgVo = new NotifyMsgVo(NotifyMsgType.DEVICE_PROP, livePropertyUpdateEvent);
                String msg = JSON.toJSONString(notifyMsgVo);

                MsgOutSubscribePush outMsg = new MsgOutSubscribePush();
                outMsg.setMsgType(MsgTypeEnum.SUBSCRIBE_PUSH);
                outMsg.setContent(msg);
                try {
                    northSocketAdapter.sendMessage(session, JSON.toJSONString(outMsg));
                } catch (IOException e) {
                    log.error("订阅后及时推送，推送设备属性失败", e);
                }
            });
        }
    }

    /**
     * 订阅
     *
     * @param msgInHeartBeat 消息
     *
     * @throws IOException IOException
     */
    private void replyHeartBeat(WebSocketSession session, MsgInHeartBeat msgInHeartBeat) throws IOException {
        MsgOutHeartBeat msgOutHeartBeat = new MsgOutHeartBeat();
        msgOutHeartBeat.setMsgType(MsgTypeEnum.HEART_BEAT);
        msgOutHeartBeat.setContent(String.valueOf(System.currentTimeMillis()));

        northSocketAdapter.sendMessage(session, JSON.toJSONString(msgOutHeartBeat));
    }

    /**
     * 取消订阅回复
     *
     * @param msgInUnSubscribe 消息
     * @param result           结果
     *
     * @throws IOException IOException
     */
    private void replyUnsubscribe(WebSocketSession session, MsgInUnSubscribe msgInUnSubscribe, boolean result) throws IOException {
        MsgOutUnSubscribe msgOutUnSubscribeResult = new MsgOutUnSubscribe();
        msgOutUnSubscribeResult.setMsgType(MsgTypeEnum.UNSUBSCRIBE);
        msgOutUnSubscribeResult.setContent(String.valueOf(result));

        northSocketAdapter.sendMessage(session, JSON.toJSONString(msgOutUnSubscribeResult));
    }
}
