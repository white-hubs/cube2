package com.guhecloud.cube.iot.mgt.north.websocket;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType;
import com.guhecloud.cube.iot.mgt.core.data.north.SubscribeInfo;
import com.guhecloud.cube.iot.mgt.core.data.north.outparam.WebSocketParam;
import com.guhecloud.cube.iot.mgt.core.def.SubscribeInterface;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgOutSubscribePush;
import com.guhecloud.cube.iot.mgt.north.websocket.msg.MsgTypeEnum;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 提供对Socket的服务
 *
 * @author fl
 * @date 2022/6/23
 **/
@Component
@Slf4j
public class NorthSocketAdapter {
    private final AtomicInteger onlineCount = new AtomicInteger(0);
    /**
     * sessionId与客户端的映射
     */
    private final Map<String, ConcurrentWebSocketSessionDecorator> sessionMap = new HashMap<>();
    /**
     * 客户端ID与sessionId的映射
     * <p>
     * key: clientId
     * value: sessionId
     */
    private final Map<String, String> clientAndSessionMap = new HashMap<>();

    /**
     * 订阅ID与客户端ID的映射
     * <p>
     * key: sessionId
     * value: subscribeIds
     */
    private final Map<String, Set<String>> subscribeIdMap = new HashMap<>();

    @Resource
    private SubscribeInterface subscribeInterface;

    /**
     * 发送消息
     *
     * @param clientId      客户端ID
     * @param notifyMsgType 消息类型
     * @param jsonMsg       消息内容
     */
    public void sendMsg(String clientId, NotifyMsgType notifyMsgType, String jsonMsg) {
        try {
            MsgOutSubscribePush msg = new MsgOutSubscribePush();
            msg.setMsgType(MsgTypeEnum.SUBSCRIBE_PUSH);
            msg.setContent(jsonMsg);

            jsonMsg = JSON.toJSONString(msg);

            String sessionId = clientAndSessionMap.get(clientId);
            if (StringUtils.isBlank(sessionId)) {
                return;
            }

            WebSocketSession session = sessionMap.get(sessionId);
            if (Objects.isNull(session)) {
                return;
            }

            session.sendMessage(new TextMessage(jsonMsg));
        } catch (Exception e) {
            log.warn("dispatchMsg websocket error", e);
        }
    }

    public synchronized void unSubscribe(String subscribeId, String sessionId) {
        if (StringUtils.isBlank(subscribeId)) {
            log.warn("subscribeId is blank");
            return;
        }

        SubscribeInfo subscribeInfo = subscribeInterface.unSubscribe(subscribeId);
        subscribeIdMap.remove(sessionId);

        if (Objects.nonNull(subscribeInfo)) {
            clientAndSessionMap.remove(subscribeInfo.getOutParam().identity());
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(WebSocketSession session, String message) throws IOException {
        ConcurrentWebSocketSessionDecorator concurrentWebSocketSessionDecorator = sessionMap.get(session.getId());
        if (Objects.nonNull(concurrentWebSocketSessionDecorator)) {
            concurrentWebSocketSessionDecorator.sendMessage(new TextMessage(message));
        } else {
            log.warn("session not found, sessionId={}", session.getId());
        }
    }

    public synchronized String subscribe(SubscribeInfo subscribeInfo, String sessionId) {
        String subscribeId = subscribeInterface.subscribe(subscribeInfo);

        //identity为clientId
        WebSocketParam webSocketParam = subscribeInfo.getOutParam();
        clientAndSessionMap.put(webSocketParam.getClientId(), sessionId);

        //添加订阅ID
        Set<String> subscribeIds = subscribeIdMap.get(sessionId);
        if (Objects.isNull(subscribeIds)) {
            subscribeIds = new HashSet<>();
            subscribeIdMap.put(sessionId, subscribeIds);
        }

        subscribeIds.add(subscribeId);
        return subscribeId;
    }

    public synchronized void register(ConcurrentWebSocketSessionDecorator session) {
        String sessionId = session.getId();
        sessionMap.put(sessionId, session);

        onlineCount.incrementAndGet();
    }

    public synchronized void release(String sessionId) {
        //释放时主动取消订阅
        Set<String> subscribeIds = subscribeIdMap.get(sessionId);
        if (Objects.nonNull(subscribeIds) && !subscribeIds.isEmpty()) {
            for (String subscribeId : subscribeIds) {
                subscribeInterface.unSubscribe(subscribeId);
            }
        }

        //清空映射缓存
        try (ConcurrentWebSocketSessionDecorator ignored = sessionMap.remove(sessionId)) {
            log.debug("unSubscribe sessionId={}", sessionId);
        } catch (IOException e) {
            log.error("Error while closing WebSocket session: {}", sessionId, e);
        }

        //清空订阅ID映射
        subscribeIdMap.remove(sessionId);

        //清空客户端与sessionId的映射
        removeClientAndSession(sessionId);

        onlineCount.decrementAndGet();
    }

    /**
     * 清理掉客户端Id与sessionId的映射
     *
     * @param sessionId sessionId
     */
    private synchronized void removeClientAndSession(String sessionId) {
        String clientId = null;
        for (Map.Entry<String, String> pair : clientAndSessionMap.entrySet()) {
            if (pair.getValue().equals(sessionId)) {
                clientId = pair.getKey();
                break;
            }
        }

        if (clientId != null) {
            clientAndSessionMap.remove(clientId);
        }
    }
}
