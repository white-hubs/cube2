package com.guhecloud.cube.base.message.sdk;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message.sdk.api.request.MessageReceiverRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.base.message.sdk.common.Common;
import com.guhecloud.cube.base.message2.client.SatMsgSceneReceiveClient;
import com.guhecloud.cube.base.message2.client.SatSendClient;
import com.guhecloud.cube.base.message2.client.dto.SatMsgSceneReceiveDTO;
import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgChannelSendParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgSendParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class MessageReceiverSendClient {

    @Autowired
    private SatMsgSceneReceiveClient satMsgSceneReceiveClient;

    @Autowired
    private SatSendClient satSendClient;

    public MessageSendResponse receiverSend(MessageReceiverRequest request) {
        //校验请求参数
        Common.validateModel(request);
        //消息接收信息
        SatMsgSceneReceiveDTO msgSceneReceiveByCode = this.satMsgSceneReceiveClient.getSatMsgSceneReceiveByCode(request.getMsgSceneCode());
        if (Objects.isNull(msgSceneReceiveByCode)) {
            return MessageSendResponse.fail();
        }
        try {
            //构建发送通道信息
            List<SatMsgChannelSendParam> satMsgChannelSendParams = buildSendChannel(request);
            this.satSendClient.sendMsg(SatMsgSendParam.builder()
                                               .businessId(request.getBusinessId())
                                               .businessPrincipal(request.getBusinessPrincipal())
                                               .applicationCode(request.getApplicationCode())
                                               .applicationName(request.getApplicationName())
                                               .msgSceneCode(request.getMsgSceneCode())
                                               .msgParams(request.getTmpParams())
                                               .satMsgChannelSendParams(satMsgChannelSendParams)
                                               .tenant(request.getTenant())
                                               .build());
            return MessageSendResponse.success(Collections.emptyList());
        } catch (Exception e) {
            log.error("消息发送失败,失败原因{}", e.getMessage(), e);
            return MessageSendResponse.fail(e.getMessage());
        }
    }

    private List<SatMsgChannelSendParam> buildSendChannel(MessageReceiverRequest receiverRequest) {
        List<SatMsgChannelSendParam> satMsgChannelSendParams = new ArrayList<>(receiverRequest.getReceiverChannels().size());
        receiverRequest.getReceiverChannels().forEach(channel -> {
            List<MsgSendTargetParam> sendTargetList = new ArrayList<>(channel.getReceiver().size());
            channel.getReceiver().forEach(receiver -> {
                MsgSendTargetParam sendTargetParam = MsgSendTargetParam.builder()
                        .receiver(receiver)
                        .build();
                sendTargetList.add(sendTargetParam);
            });
            satMsgChannelSendParams.add(new SatMsgChannelSendParam(channel.getTmpId(),
                                                                   ChannelEnum.valueOf(channel.getChannelCode().name()),
                                                                   channel.getTenant(),
                                                                   channel.getExtendConfig(),
                                                                   sendTargetList));
        });
        return satMsgChannelSendParams;
    }
}
