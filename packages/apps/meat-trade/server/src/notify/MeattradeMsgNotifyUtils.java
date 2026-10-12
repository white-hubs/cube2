package com.guhecloud.cube.biz.meattrade.server.notify;

import com.guhecloud.cube.base.message.sdk.MessageSceneClient;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeattradeMsgNotifyUtils {

    private final MessageSceneClient messageSceneClient;

    private final MessageSendClient messageSendClient;

    public MessageSendResponse supplierNotify(String sceneCode, String businessId, SupplierDTO supplier, Map<String, String> map) {
        return this.messageSendClient.sceneSpecifyTmpSend(MessageSpecifyTmpSendRequest.builder()
                                                                  .channel(this.getSupplierNotifyChannel(supplier, sceneCode))
                                                                  .businessId(businessId)
                                                                  .msgSceneCode(sceneCode)
                                                                  .tmpParams(map)
                                                                  .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget
                                                                                                                          .builder()
                                                                                                                          .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                                          .ids(Collections.singleton(supplier.getAdminUserId()))
                                                                                                                          .build()))
                                                                  .build());
    }

    public List<MessageChannelRequest> getSupplierNotifyChannel(SupplierDTO supplier, String sceneCode) {
        List<MessageChannelEnum> msgSceneReceiveChannels = this.messageSceneClient.getMsgSceneReceiveChannels(sceneCode);
        return msgSceneReceiveChannels.stream().map(channel ->
                                                            //商家小程序通知,租户为批发商租户编号
                                                            MessageChannelRequest.builder()
                                                                    .channelCode(channel)
                                                                    .tenant(MessageChannelEnum.CHANNEL_SITE == channel
                                                                            ? supplier.getSupplierTenant() : supplier.getTenant())
                                                                    .build()
                                                   ).collect(Collectors.toList());
    }

    public MessageSendResponse commonNotify(String sceneCode,
                                            String businessId,
                                            Set<Long> userIds,
                                            Integer tenant,
                                            Map<String, String> map
                                           ) {
        return this.messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                        .businessId(businessId)
                                                        .msgSceneCode(sceneCode)
                                                        .tmpParams(map)
                                                        .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                                .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                                .ids(userIds)
                                                                                                                .build()))
                                                        .tenant(tenant)
                                                        .build());
    }
}
