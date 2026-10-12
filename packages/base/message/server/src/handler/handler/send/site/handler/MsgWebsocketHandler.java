package com.guhecloud.cube.base.message2.server.handler.handler.send.site.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto.ConsumerWebsocketDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto.MsgEventDataDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.interceptor.JwtHandshakeInterceptor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MsgWebsocketHandler extends TextWebSocketHandler {

    private static final String EVENT_WELCOME = "welcome";

    private static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_WEB_SOCKET_INFO_KEY = "webSocketSessionInfo";

    @Autowired
    ObjectMapper objectMapper;

    public MsgWebsocketHandler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        if (sessionMap.get(session.getId()) != null) {
            sessionMap.remove(session.getId());
            //将删除redis中的session信息
            this.redisTemplate.delete(REDIS_WEB_SOCKET_INFO_KEY + ":" + session.getId());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (sessionMap.get(session.getId()) == null) {
            sessionMap.put(session.getId(), session);
            Map<String, Object> attributes = session.getAttributes();
            //将session信息存入redis
            ConsumerWebsocketDTO consumerWebsocketDTO = ConsumerWebsocketDTO.builder()
                    .sessionId(session.getId())
                    .webSocketUserId(session.getId())
                    .terminal((String) attributes.get("terminal"))
                    .subscribeType((String) attributes.get("subscribeType"))
                    .build();
            this.redisTemplate.opsForValue().set(REDIS_WEB_SOCKET_INFO_KEY + ":" + session.getId(), consumerWebsocketDTO);
            this.sendToSession(session, new MsgEventDataDTO(EVENT_WELCOME, null));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        this.sendToSession(session, new MsgEventDataDTO("health", null));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        log.error("传输异常", throwable);
    }

    public void broadcastSend(MsgEventDataDTO eventDataDTO, Integer tenant, Set<String> nonMatchTerminals) {
        for (WebSocketSession session : sessionMap.values()) {
            try {
                String loginUserId = (String) session.getAttributes().get(JwtHandshakeInterceptor.ATTR_LOGIN_USER);
                if (!loginUserId.startsWith(tenant.toString() + ":")) {
                    continue;
                }
                Object o = this.redisTemplate.opsForValue().get(REDIS_WEB_SOCKET_INFO_KEY + ":" + session.getId());
                if (Objects.nonNull(o)) {
                    ConsumerWebsocketDTO consumerWebsocketDTO = (ConsumerWebsocketDTO) o;
                    if ("broadcast".equals(consumerWebsocketDTO.getSubscribeType())
                        &&
                        (CollectionUtils.isEmpty(nonMatchTerminals) || !nonMatchTerminals.contains(consumerWebsocketDTO.getTerminal()))) {
                        this.sendToSession(session, eventDataDTO);
                    }
                }
            } catch (Exception e) {
                log.error("推送消息异常", e);
            }
        }
    }

    public void broadcastUserSend(MsgEventDataDTO eventDataDTO, String userId, String terminal) {
        for (WebSocketSession session : sessionMap.values()) {
            try {
                String loginUserId = (String) session.getAttributes().get(JwtHandshakeInterceptor.ATTR_LOGIN_USER);
                if (!loginUserId.equals(userId)) {
                    continue;
                }
                Object o = this.redisTemplate.opsForValue().get(REDIS_WEB_SOCKET_INFO_KEY + ":" + session.getId());
                if (Objects.nonNull(o)) {
                    ConsumerWebsocketDTO consumerWebsocketDTO = (ConsumerWebsocketDTO) o;
                    if ("broadcast".equals(consumerWebsocketDTO.getSubscribeType()) &&
                        terminal.equals(consumerWebsocketDTO.getTerminal())) {
                        this.sendToSession(session, eventDataDTO);
                    }
                }
            } catch (Exception e) {
                log.error("推送消息异常", e);
            }
        }
    }

    public void send(String sessionId, MsgEventDataDTO eventDataDTO) {
        for (WebSocketSession session : sessionMap.values()) {
            try {
                if (StringUtils.isEmpty(sessionId)) {
                    this.sendToSession(session, eventDataDTO);
                } else {
                    if (session.getId().equals(sessionId)) {
                        this.sendToSession(session, eventDataDTO);
                    }
                }
            } catch (Exception e) {
                log.error("推送消息异常", e);
            }
        }
    }

    public List<ConsumerWebsocketDTO> getOnLineUser(String userId) {
        List<ConsumerWebsocketDTO> consumerWebsocketDTOS = new ArrayList<>();
        for (WebSocketSession session : sessionMap.values()) {
            String loginUserId = (String) session.getAttributes().get(JwtHandshakeInterceptor.ATTR_LOGIN_USER);
            if (loginUserId.equals(userId)) {
                Object o = this.redisTemplate.opsForValue().get(REDIS_WEB_SOCKET_INFO_KEY + ":" + session.getId());
                if (Objects.nonNull(o)) {
                    consumerWebsocketDTOS.add((ConsumerWebsocketDTO) o);
                }
            }
        }
        return consumerWebsocketDTOS;
    }

    public List<ConsumerWebsocketDTO> getTerminalTenantOnLineUser(String userId, String terminal) {
        List<ConsumerWebsocketDTO> consumerWebsocketDTOS = new ArrayList<>();
        for (WebSocketSession session : sessionMap.values()) {
            String loginUserId = (String) session.getAttributes().get(JwtHandshakeInterceptor.ATTR_LOGIN_USER);
            if (loginUserId.equals(userId)) {
                Object o = this.redisTemplate.opsForValue().get(REDIS_WEB_SOCKET_INFO_KEY + ":" + session.getId());
                if (Objects.nonNull(o)) {
                    ConsumerWebsocketDTO consumerWebsocketDTO = (ConsumerWebsocketDTO) o;
                    if (terminal.equals(consumerWebsocketDTO.getTerminal())) {
                        consumerWebsocketDTOS.add(consumerWebsocketDTO);
                    }
                }
            }
        }
        return consumerWebsocketDTOS;
    }

    public List<ConsumerWebsocketDTO> getTerminalOnLineUser(String userId) {
        List<ConsumerWebsocketDTO> consumerWebsocketDTOS = new ArrayList<>();
        for (WebSocketSession session : sessionMap.values()) {
            String loginUserId = (String) session.getAttributes().get(JwtHandshakeInterceptor.ATTR_TERMINAL_USER);
            if (loginUserId.equals(userId)) {
                Object o = this.redisTemplate.opsForValue().get(REDIS_WEB_SOCKET_INFO_KEY + ":" + session.getId());
                if (Objects.nonNull(o)) {
                    ConsumerWebsocketDTO consumerWebsocketDTO = (ConsumerWebsocketDTO) o;
                    consumerWebsocketDTOS.add(consumerWebsocketDTO);
                }
            }
        }
        return consumerWebsocketDTOS;
    }

    private void sendToSession(WebSocketSession session, MsgEventDataDTO eventDataDTO) throws IOException {
        synchronized (session) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(eventDataDTO)));
        }
    }
}
