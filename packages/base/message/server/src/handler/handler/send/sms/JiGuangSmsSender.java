package com.guhecloud.cube.base.message2.server.handler.handler.send.sms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.client.enumeration.CodeVerifyResultEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.JgSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.CodeVerifyDTO;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.CodeSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg.JgMsgBatchSendRequestDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg.JgMsgRecipientRequestDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg.JgMsgRecipientResponseDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg.JgSendVerifyCodeRequestDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg.JgVerifyCodeRequestDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg.JiGuangTmpDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.verifier.CodeVerify;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 极光短信发送
 *
 * @author lanchun
 * @date 2023/5/11 10:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JiGuangSmsSender implements ManufacturerSender, CodeSender, CodeVerify {

    private static final String SMS_CODE_MSG_ID_KEY = "sms:code:msgId:";
    private static final Integer SMS_CODE_EXPIRE_TIME = 300;

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public MessageSendResponse sendCode(MessageInfo messageInfo) {
        return sendVerifyCodeMsg(messageInfo);
    }

    @Override
    public CodeResponseDTO verifyCode(CodeVerifyDTO codeVerifyDTO) {
        String msgId = redisTemplate.opsForValue().get(SMS_CODE_MSG_ID_KEY + codeVerifyDTO.receiver());
        if (StringUtils.isBlank(msgId)) {
            return CodeResponseDTO.failed("验证码已过期，请重新发送", CodeVerifyResultEnum.EXPIRED);
        }
        Assert.isParameterError(StringUtils.isEmpty(msgId), "验证码已过期，请重新发送");
        try {

            HttpHeaders httpHeaders = getJgHeaders(codeVerifyDTO.authInfo());
            //解析模板配置获取模板id
            JgVerifyCodeRequestDTO req = new JgVerifyCodeRequestDTO();
            req.setCode(codeVerifyDTO.code());
            ResponseEntity<String> response;
            //构建请求体
            HttpEntity<?> entity = new HttpEntity<>(req, httpHeaders);
            response = this.restTemplate.exchange(String.format("https://api.sms.jpush.cn/v1/codes/%s/valid", msgId), HttpMethod.POST, entity, String.class);
            //解析响应结果
            if (HttpStatus.OK == response.getStatusCode()) {
                //解析返回对象
                Map<String, Object> jgResponse = JsonUtils.deserialize(response.getBody(), new TypeReference<Map<String, Object>>() {});
                if (jgResponse != null) {
                    boolean result = (Boolean) jgResponse.getOrDefault("is_valid", false);
                    return result ? CodeResponseDTO.success() : CodeResponseDTO.failed("验证码错误");
                }
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            String responseBody = httpClientErrorException.getResponseBodyAsString();
            if (StringUtils.isBlank(responseBody)) {
                return CodeResponseDTO.failed(httpClientErrorException.getMessage(), CodeVerifyResultEnum.FAILED);
            }
            Map<String, Object> jgResponse = JsonUtils.deserialize(responseBody, new TypeReference<>() {});
            if (Objects.isNull(jgResponse)) {
                return CodeResponseDTO.failed(httpClientErrorException.getMessage(), CodeVerifyResultEnum.FAILED);
            }

            Map<String, Object> error = (Map<String, Object>) jgResponse.get("error");
            Integer code = (Integer) error.get("code");
            if (Integer.valueOf(50012).equals(code)) {
                return CodeResponseDTO.failed("验证码已校验", CodeVerifyResultEnum.VERIFIED);
            }
            if (Integer.valueOf(50010).equals(code)) {
                return CodeResponseDTO.failed("验证码错误", CodeVerifyResultEnum.INVALID);
            }
            if (Integer.valueOf(50011).equals(code)) {
                return CodeResponseDTO.failed("验证码过期", CodeVerifyResultEnum.EXPIRED);
            }
            return CodeResponseDTO.failed(httpClientErrorException.getMessage(), CodeVerifyResultEnum.FAILED);
        } catch (ServiceException e) {
            log.error("验证码校验内部配置错误", e);
            return CodeResponseDTO.failed(e.getMessage());
        }
        return CodeResponseDTO.failed("验证码校验未知错误");
    }

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.JI_GUANG.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        return sendNotifyMsg(messageInfo);

    }

    private MessageSendResponse sendVerifyCodeMsg(MessageInfo messageInfo) {
        Set<String> successReceiver = new HashSet<>();
        Set<String> msgIds = new HashSet<>();
        List<MessageSendResponse.Receiver> failReceiver = new ArrayList<>();
        Set<String> receiver = messageInfo.getReceiver();
        Assert.isParameterError(ListUtils.isEmpty(receiver), "接收人为空");
        MessageSendResponse messageSendResponse = new MessageSendResponse();
        messageSendResponse.setSucceed(Boolean.TRUE);
        receiver.forEach(phone -> {
            try {
                //解析模板配置获取模板id
                JiGuangTmpDTO jiGuangTmpDTO = JsonUtils.deserialize(messageInfo.getTmpConfig(), JiGuangTmpDTO.class);
                JgSendVerifyCodeRequestDTO req = new JgSendVerifyCodeRequestDTO();
                req.setTemp_id(Integer.parseInt(jiGuangTmpDTO.getTmpId()));
                req.setMobile(phone);
                ResponseEntity<String> response;
                //构建请求体
                HttpHeaders httpHeaders = this.getJgHeaders(messageInfo.getSendAccountInfo());
                HttpEntity<?> entity = new HttpEntity<>(JsonUtils.serialize(req), httpHeaders);
                log.info("极光发送短信验证码请求参数：{}", JsonUtils.serialize(entity));
                response = this.restTemplate.exchange("https://api.sms.jpush.cn/v1/codes", HttpMethod.POST, entity, String.class);
                //解析响应结果
                if (HttpStatus.OK == response.getStatusCode()) {
                    //解析返回对象
                    Map jgResponse = JsonUtils.deserialize(response.getBody(), Map.class);
                    String msgId = jgResponse.getOrDefault("msg_id", "").toString();
                    if (StringUtils.isNotBlank(msgId)) {
                        successReceiver.add(phone);
                        redisTemplate.opsForValue().set(SMS_CODE_MSG_ID_KEY + phone, msgId, SMS_CODE_EXPIRE_TIME, TimeUnit.SECONDS);
                        msgIds.add(msgId);
                        return;
                    }
                    String error = JsonUtils.serialize(jgResponse.get("error"));
                    MessageSendResponse.Receiver failReceiverItem = new MessageSendResponse.Receiver(phone, error);
                    failReceiver.add(failReceiverItem);

                }
            } catch (Exception e) {
                MessageSendResponse.Receiver failReceiverItem = new MessageSendResponse.Receiver(phone, e.getMessage());
                failReceiver.add(failReceiverItem);
            }

        });
        messageSendResponse.setMsgIds(msgIds);
        messageSendResponse.setFailReceivers(failReceiver);
        messageSendResponse.setSucceedReceiver(successReceiver);
        return messageSendResponse;

    }

    private MessageSendResponse sendNotifyMsg(MessageInfo messageInfo) {
        try {
            //解析模板配置获取模板id
            JiGuangTmpDTO jiGuangTmpDTO = JsonUtils.deserialize(messageInfo.getTmpConfig(), JiGuangTmpDTO.class);
            JgMsgBatchSendRequestDTO req = new JgMsgBatchSendRequestDTO();
            req.setTemp_id(Integer.parseInt(jiGuangTmpDTO.getTmpId()));
            List<JgMsgRecipientRequestDTO> jgMsgRecipientRequestDTOS = Collections.synchronizedList(new ArrayList<>());
            Map<String, String> tempPara = new HashMap<>();
            messageInfo.getTemplateContentParams().forEach((key, value) -> {
                if (StringUtils.isNotEmpty(value)) {
                    tempPara.put(key, value);
                }
            });
            messageInfo.getReceiver().parallelStream().forEach(phone -> {
                JgMsgRecipientRequestDTO jgMsgRecipientRequestDTO = JgMsgRecipientRequestDTO.builder().mobile(phone).temp_para(tempPara)
                        .build();
                jgMsgRecipientRequestDTOS.add(jgMsgRecipientRequestDTO);
            });
            req.setRecipients(jgMsgRecipientRequestDTOS);
            ResponseEntity<String> response = null;
            //构建请求体
            HttpHeaders httpHeaders = this.getJgHeaders(messageInfo.getSendAccountInfo());
            HttpEntity<?> entity = new HttpEntity<>(req, httpHeaders);
            response = this.restTemplate.exchange("https://api.sms.jpush.cn/v1/messages/batch", HttpMethod.POST, entity, String.class);
            //解析响应结果
            if (HttpStatus.OK == response.getStatusCode()) {
                MessageSendResponse messageSendResponse = new MessageSendResponse();
                messageSendResponse.setSucceed(Boolean.TRUE);
                //解析返回对象
                Map jgResponse = JsonUtils.deserialize(response.getBody(), Map.class);
                String recipients = JsonUtils.serialize(jgResponse.get("recipients"));
                //可能存在部分用户发送成功,部分用户发送失败的情况
                List<JgMsgRecipientResponseDTO> jgMsgRecipientResponses = JsonUtils.deserialize(recipients, new TypeReference<List<JgMsgRecipientResponseDTO>>() {
                });
                //从返回的响应结果里面筛选出发送失败的手机号
                List<MessageSendResponse.Receiver> failReceivers = jgMsgRecipientResponses.stream()
                        .filter(jgMsgRecipientResponse -> StringUtils.isNotEmpty(jgMsgRecipientResponse.getError_code()))
                        .map(jgMsgRecipientResponse ->
                                     new MessageSendResponse.Receiver(jgMsgRecipientResponse.getMobile(), JsonUtils.serialize(jgMsgRecipientResponse))
                            ).collect(Collectors.toList());
                messageSendResponse.setFailReceivers(failReceivers);
                return messageSendResponse;
            }
            throw new ServiceException(response.getBody());
        } catch (Exception e) {
            log.error("消息发送任务id为{}极光短信发送失败:{}", messageInfo.getId(), e);
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

    private HttpHeaders getJgHeaders(String config) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + JgSmsAccountConfig.authorizationChecker(config));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
