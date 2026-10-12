package com.guhecloud.cube.base.message2.server.handler.handler.send.sms;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.GadxSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.gdyh.GadxSendRequestDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.gdyh.GadxSendResponseDTO;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.Base64Utils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GadxSmsSender implements ManufacturerSender {

    private final RestTemplate restTemplate;

    private static final String SEND_URL = "/sms/v1/send";

    public GadxSmsSender(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.GONG_AN_DA_XUE.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        try {
            GadxSmsAccountConfig config = GadxSmsAccountConfig.getConfig(messageInfo.getSendAccountInfo());
            GadxSendRequestDTO request = new GadxSendRequestDTO();
            request.setContents(config.getSign() + " " + messageInfo.getContent());
            request.setMobiles(String.join(",", messageInfo.getReceiver()));
            request.setSign(generateSign(config, request));
            ResponseEntity<GadxSendResponseDTO> response = null;
            //构建请求体
            HttpHeaders httpHeaders = this.getHeaders();
            HttpEntity<?> entity = new HttpEntity<>(request, httpHeaders);
            response = this.restTemplate.exchange(
                    config.getHost() + SEND_URL + "?appId=" + config.getAppid(), HttpMethod.POST, entity, GadxSendResponseDTO.class);
            List<GadxSendResponseDTO.GadxSendUserResponse> data = getGadxSendUserResponses(response);
            //从返回的响应结果里面筛选出发送失败的手机号
            List<MessageSendResponse.Receiver> failReceivers = data.stream()
                    .filter(sendUserResponse -> 0 != sendUserResponse.getCode())
                    .map(sendUserResponse ->
                                 new MessageSendResponse.Receiver(sendUserResponse.getMobile(), JsonUtils.serialize(sendUserResponse))
                        ).collect(Collectors.toList());
            MessageSendResponse messageSendResponse = new MessageSendResponse();
            messageSendResponse.setId(messageInfo.getId());
            messageSendResponse.setSucceed(Boolean.TRUE);
            messageSendResponse.setFailReceivers(failReceivers);
            return messageSendResponse;
        } catch (Exception e) {
            log.error("消息发送任务id为{},公安大学数据短信发送失败:", messageInfo.getId(), e);
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

    private List<GadxSendResponseDTO.GadxSendUserResponse> getGadxSendUserResponses(
            ResponseEntity<GadxSendResponseDTO> response
                                                                                   ) {
        if (HttpStatus.OK != response.getStatusCode() || Objects.isNull(response.getBody())) {
            throw new ServiceException("调用失败,httpStatus:" + response.getStatusCode());
        }
        //解析返回对象
        GadxSendResponseDTO sendResponseDTO = response.getBody();
        if (0 != sendResponseDTO.getCode() || CollectionUtils.isEmpty(sendResponseDTO.getData())) {
            throw new ServiceException("短信发送失败,code:" + sendResponseDTO.getCode());
        }
        return sendResponseDTO.getData();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String generateSign(GadxSmsAccountConfig config, GadxSendRequestDTO requestDTO) {
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, config.getPrivateKey(), null);
        return Base64Utils.encode(sign.sign(JsonUtils.serialize(requestDTO)));
    }
}
