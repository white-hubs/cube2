package com.guhecloud.cube.base.message2.server.handler.handler.send.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.push.JgPushAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.app.dto.JgPushRequestDTO;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 极光app发送
 *
 * @author lanchun
 * @date 2023/5/15 10:11
 */
@Slf4j
@Component
public class JiGuangAppSender implements ManufacturerSender {

    @Autowired
    private RestTemplate restTemplate;

    private static final String DEEP_LINK_URL = "%s://guheyun/main?linkType=message&businessId=%s";

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_APP.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.JI_GUANG.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        ResponseEntity<String> response;
        try {
            JgPushRequestDTO req = getJgPushRequestDTO(messageInfo);
            //构建请求体
            HttpHeaders httpHeaders = this.getJgHeaders(messageInfo.getSendAccountInfo());
            HttpEntity<?> entity = new HttpEntity<>(req, httpHeaders);
            response = this.restTemplate.exchange("https://api.jpush.cn/v3/push", HttpMethod.POST, entity, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                return MessageSendResponse
                        .builder()
                        .id(messageInfo.getId())
                        .succeedReceiver(messageInfo.getReceiver())
                        .succeed(true)
                        .build();
            }
            throw new ServiceException(response.getBody());
        } catch (Exception e) {

            log.error("消息发送任务id为{}极光App发送失败", messageInfo.getId(), e);
            return MessageSendResponse
                    .builder()
                    .id(messageInfo.getId())
                    .succeed(false)
                    .message(e.getMessage())
                    .failReceivers(messageInfo.getReceiver().stream().map(phone -> new MessageSendResponse.Receiver(phone, e.getMessage()))
                                           .collect(Collectors.toList()))
                    .build();
        }
    }

    private JgPushRequestDTO getJgPushRequestDTO(MessageInfo messageInfo) {
        //获取极光cid(推送唯一标识符)
        String cid = this.getCid(messageInfo.getSendAccountInfo());
        HashMap<String, Object> extras = new HashMap<>(1);
        extras.put("customMsgId", messageInfo.getId());
        //android发送参数
        Map<String, Object> androidParam = new HashMap<>();
        androidParam.put("alert", messageInfo.getContent());
        androidParam.put("display_foreground", "1");
        androidParam.put("extras", extras);
        //跳转地址
        JgPushAccountConfig jgConfig = JgPushAccountConfig.getJgConfig(messageInfo.getSendAccountInfo());
        if (StringUtils.isNotBlank(jgConfig.getDeepLinkSchema())) {
            Map<String, Object> intent = new HashMap<>(1);
            intent.put("url", String.format(DEEP_LINK_URL, jgConfig.getDeepLinkSchema(), messageInfo.getId()));
            androidParam.put("intent", intent);
        }

        //ios发送参数
        Map<String, Object> iosParam = new HashMap<>();
        iosParam.put("alert", messageInfo.getContent());
        iosParam.put("extras", extras);

        //自定义消息，透传消息
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("msg_content", messageInfo.getContent());

        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("android", androidParam);
        notificationMap.put("ios", iosParam);
        //推送别名(目前使用用户id为别名)
        Map<String, Object> audienceMap = new HashMap<>();
        audienceMap.put("alias", messageInfo.getReceiver());

        String[] platform = { "android", "ios" };
        JgPushRequestDTO req = JgPushRequestDTO
                .builder()
                .cid(cid)
                .platform(platform)
                .audience(audienceMap)
                .notification(notificationMap)
                .message(messageMap)
                .build();
        return req;
    }

    /**
     * 获取发送的唯一标识
     *
     * @return
     */
    private String getCid(String config) {
        ResponseEntity<String> response = null;
        try {
            //构建请求体
            HttpHeaders httpHeaders = this.getJgHeaders(config);
            HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
            response = this.restTemplate.exchange("https://api.jpush.cn/v3/push/cid?type=push", HttpMethod.GET, entity, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                //解析返回对象
                Map jgResponse = JsonUtils.deserialize(response.getBody(), Map.class);
                String cidListStr = JsonUtils.serialize(jgResponse.get("cidlist"));
                //获取cid池
                List<String> cidListResponse = JsonUtils.deserialize(cidListStr, new TypeReference<List<String>>() {
                });
                return cidListResponse.get(0);
            }
            throw new ServiceException(response.getBody());
        } catch (Exception e) {
            log.error("获取极光cid失败", e);
            return StringUtils.EMPTY_STRING;
        }
    }

    private HttpHeaders getJgHeaders(String config) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + JgPushAccountConfig.authorizationChecker(config));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
