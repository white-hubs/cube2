package com.guhecloud.cube.base.message2.server.handler.handler.send.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.JuHeSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.juhe.JuHeSendRequestDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms.dto.JuHeTmpDTO;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 聚合数据短信发送
 *
 * @author lanchun
 * @date 2024/12/18 10:48
 */
@Slf4j
@Component
public class JuHeSmsSender implements ManufacturerSender {

    @Autowired
    private RestTemplate restTemplate;

    private static final String SEND_URL = "http://v.juhe.cn/sms/send";

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.JU_HE_DATA.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        try {
            JuHeSmsAccountConfig config = JuHeSmsAccountConfig.getJuHeConfig(messageInfo.getSendAccountInfo());
            //解析模板配置获取模板id
            JuHeTmpDTO juHeTmpDTO = JsonUtils.deserialize(messageInfo.getTmpConfig(), JuHeTmpDTO.class);
            Map<String, String> templatePara = new HashMap<>();
            messageInfo.getTemplateContentParams().forEach((key, value) -> {
                if (StringUtils.isNotEmpty(value)) {
                    templatePara.put(key, value);
                }
            });
            JuHeSendRequestDTO req = new JuHeSendRequestDTO();
            req.setKey(config.getAppKey());
            req.setTpl_id(Integer.parseInt(juHeTmpDTO.getTmpId()));
            //例如模板中含有#code#和#name两个变量，则vars={"name":"张三","code":1234}
            req.setVars(JsonUtils.serialize(templatePara));//模板变量替换
            List<MessageSendResponse.Receiver> failReceivers = new ArrayList<>();
            for (String phone : messageInfo.getReceiver()) {
                req.setMobile(phone);
                //接口发送
                juHeSendResponse juHeSendResponse = doSend(req);
                if (0 != juHeSendResponse.getErrorCode()) {
                    //失败
                    failReceivers.add(new MessageSendResponse.Receiver(phone, juHeSendResponse.getReason()));
                }
            }
            if (failReceivers.size() < messageInfo.getReceiver().size()) {
                return MessageSendResponse
                        .builder()
                        .id(messageInfo.getId())
                        .succeed(true)
                        .failReceivers(failReceivers)
                        .build();
            }
            throw new ServiceException(failReceivers.get(0).getReceiver());
        } catch (Exception e) {
            log.error("消息发送任务id为{},聚合数据短信发送失败:", messageInfo.getId(), e);
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

    private juHeSendResponse doSend(JuHeSendRequestDTO req) {
        ResponseEntity<String> response = null;
        //构建请求体
        HttpHeaders httpHeaders = this.getHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("key", req.getKey());
        params.add("mobile", req.getMobile());
        params.add("tpl_id", String.valueOf(req.getTpl_id()));
        params.add("vars", req.getVars());
        HttpEntity<?> entity = new HttpEntity<>(params, httpHeaders);
        response = this.restTemplate.exchange(SEND_URL, HttpMethod.POST, entity, String.class);
        //解析响应结果
        if (HttpStatus.OK == response.getStatusCode()) {
            MessageSendResponse messageSendResponse = new MessageSendResponse();
            messageSendResponse.setSucceed(Boolean.TRUE);
            //解析返回对象
            return JsonUtils.deserialize(response.getBody(), juHeSendResponse.class);
        }
        return new juHeSendResponse(500, "请求失败");
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    @Data
    public static class juHeSendResponse {

        public juHeSendResponse() {

        }

        public juHeSendResponse(int errorCode, String reason) {
            this.errorCode = errorCode;
            this.reason = reason;
        }

        /**
         * 返回码
         */
        @JsonProperty(value = "error_code")
        private int errorCode;

        /**
         * 返回说明
         */
        private String reason;
    }
}