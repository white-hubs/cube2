package com.guhecloud.cube.base.message2.server.handler.handler.send.voice;

import com.aliyun.dyvmsapi20170525.Client;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsRequest;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.client.alibaba.AliYunVoiceClient;
import com.guhecloud.cube.base.message2.server.handler.domain.voice.AliYunVoiceAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.voice.dto.AliYunTmpDTO;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class AliYunVoiceSender implements ManufacturerSender {

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_VOICE.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.ALI_YUN.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        //账号信息
        AliYunVoiceAccountConfig aliVoiceConfig = AliYunVoiceAccountConfig.getAliConfig(messageInfo.getSendAccountInfo());
        Client client = AliYunVoiceClient.createClient(aliVoiceConfig.getAccessKeyId(), aliVoiceConfig.getAccessKeySecret());
        //模板信息解析模板配置获取模板id
        AliYunTmpDTO aliYunTmpDTO = JsonUtils.deserialize(messageInfo.getTmpConfig(), AliYunTmpDTO.class);
        List<MessageSendResponse.Receiver> failReceivers = Collections.synchronizedList(new ArrayList<>());
        messageInfo.getReceiver().parallelStream().forEach(phone -> {
            SingleCallByTtsRequest singleCallByTtsRequest = new SingleCallByTtsRequest()
                    .setCalledNumber(phone)
                    .setTtsCode(aliYunTmpDTO.getTmpId())
                    .setTtsParam(JsonUtils.serialize(messageInfo.getTemplateContentParams()));
            RuntimeOptions runtimeOptions = new RuntimeOptions();
            try {
                SingleCallByTtsResponse singleCallByTtsResponse = client.singleCallByTtsWithOptions(singleCallByTtsRequest, runtimeOptions);
                if (!"OK".equals(singleCallByTtsResponse.body.getCode())) {
                    failReceivers.add(new MessageSendResponse.Receiver(phone, JsonUtils.serialize(singleCallByTtsResponse.getBody())));
                }
            } catch (Exception e) {
                e.printStackTrace();
                failReceivers.add(new MessageSendResponse.Receiver(phone, e.getMessage()));
            }
        });
        MessageSendResponse voiceSendResponse = new MessageSendResponse();
        voiceSendResponse.setSucceed(true);
        voiceSendResponse.setFailReceivers(failReceivers);
        return voiceSendResponse;
    }
}
