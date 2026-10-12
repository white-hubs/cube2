package com.guhecloud.cube.base.message2.server.handler.handler.send.sms;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.GdYouHongKeJiSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.gdyh.GdYouHongKeJiBatchSendRequestDTO;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
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
import java.util.stream.Collectors;

/**
 * 广东优弘科技短信发送
 *
 * @author lanchun
 * @date 2023/5/11 10:42
 */
@Slf4j
@Component
public class GdYouHongKeJiSmsSender implements ManufacturerSender {

    @Autowired
    private RestTemplate restTemplate;

    private static final String SEND_URL = "/Ji_NoteManage/note/sendNote";

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.GD_YOU_HONG_KE_JI.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        try {
            //解析模板配置获取模板id
            GdYouHongKeJiSmsAccountConfig gdYouHongKeJiSmsAccountConfig = JsonUtils.deserialize(messageInfo.getSendAccountInfo(), GdYouHongKeJiSmsAccountConfig.class);
            GdYouHongKeJiBatchSendRequestDTO req = new GdYouHongKeJiBatchSendRequestDTO();
            req.setAccount(gdYouHongKeJiSmsAccountConfig.getAccount());
            req.setPassword(gdYouHongKeJiSmsAccountConfig.getPassword());
            req.setExtno(gdYouHongKeJiSmsAccountConfig.getExtno());
            req.setAction("send");
            req.setContent(messageInfo.getContent());
            req.setMobile(String.join(",", messageInfo.getReceiver()));
            ResponseEntity<String> response = null;
            //构建请求体
            HttpHeaders httpHeaders = this.getHeaders();
            HttpEntity<?> entity = new HttpEntity<>(req, httpHeaders);
            response = this.restTemplate.exchange(
                    gdYouHongKeJiSmsAccountConfig.getRequestBaseUrl() + SEND_URL, HttpMethod.POST, entity, String.class);
            //解析响应结果
            if (HttpStatus.OK == response.getStatusCode()) {
                MessageSendResponse messageSendResponse = new MessageSendResponse();
                messageSendResponse.setSucceed(Boolean.TRUE);
                return messageSendResponse;
            }
            throw new ServiceException(response.getBody());
        } catch (Exception e) {
            log.error("消息发送任务id为{}广东优弘科技短信发送失败:", messageInfo.getId(), e);
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

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
