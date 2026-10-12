package com.guhecloud.cube.base.message.sdk;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message.sdk.api.channel.ChannelReceiverHolder;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.request.MessageChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.base.message.sdk.common.Common;
import com.guhecloud.cube.base.message2.client.SatMsgSceneReceiveClient;
import com.guhecloud.cube.base.message2.client.SatSendClient;
import com.guhecloud.cube.base.message2.client.dto.SatMsgSceneReceiveDTO;
import com.guhecloud.cube.base.message2.client.dto.SatMsgSceneReceiveUserDTO;
import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgChannelSendParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgSendParam;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQueryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageSendClient {

    @Autowired
    private SatSendClient satSendClient;

    @Autowired
    private SatMsgSceneReceiveClient satMsgSceneReceiveClient;

    @Autowired
    private UserQueryService userQueryService;

    public MessageSendResponse sceneSend(MessageSceneSendRequest request) {
        //校验请求参数
        Common.validateModel(request);
        //消息接收信息
        SatMsgSceneReceiveDTO msgSceneReceiveByCode = this.satMsgSceneReceiveClient.getSatMsgSceneReceiveByCode(request.getMsgSceneCode());
        if (Objects.isNull(msgSceneReceiveByCode)) {
            return MessageSendResponse.fail();
        }
        //执行发送
        try {
            //获取接收人信息
            List<UserDTO> userDTOS = this.getMessageNotifyUsers(msgSceneReceiveByCode, request);
            //发送通道构造
            List<SatMsgChannelSendParam> satMsgChannelSendParams = buildSendChannel(msgSceneReceiveByCode.getChannelCodes(), userDTOS);
            this.satSendClient.sendMsg(SatMsgSendParam.builder()
                                               .businessId(request.getBusinessId())
                                               .businessPrincipal(request.getBusinessPrincipal())
                                               .msgSceneCode(request.getMsgSceneCode())
                                               .msgParams(request.getTmpParams())
                                               .satMsgChannelSendParams(satMsgChannelSendParams)
                                               .applicationCode(request.getApplicationCode())
                                               .applicationName(request.getApplicationName())
                                               .tenant(request.getTenant())
                                               .sourceTenant(
                                                       null == request.getSourceTenant() ? request.getTenant() : request.getSourceTenant())
                                               .build());
            return MessageSendResponse.success(userDTOS);
        } catch (Exception e) {
            log.error("消息发送失败,失败原因{}", e.getMessage(), e);
            return MessageSendResponse.fail(e.getMessage());
        }
    }

    public MessageSendResponse sceneSpecifyTmpSend(MessageSpecifyTmpSendRequest request) {
        //校验请求参数
        Common.validateModel(request);
        //消息接收信息
        SatMsgSceneReceiveDTO msgSceneReceiveByCode = this.satMsgSceneReceiveClient.getSatMsgSceneReceiveByCode(request.getMsgSceneCode());
        if (Objects.isNull(msgSceneReceiveByCode)) {
            return MessageSendResponse.fail();
        }
        //执行发送
        try {
            //获取接收人信息
            List<UserDTO> userDTOS = this.getMessageNotifyUsers(msgSceneReceiveByCode, request);
            //发送通道构造
            List<SatMsgChannelSendParam> satMsgChannelSendParams;
            if (!CollectionUtils.isEmpty(request.getChannel())) {
                satMsgChannelSendParams = buildSpecifyTmpSendChannel(request.getChannel(), msgSceneReceiveByCode.getChannelCodes(), userDTOS);
            } else {
                satMsgChannelSendParams = buildSendChannel(msgSceneReceiveByCode.getChannelCodes(), userDTOS);
            }
            this.satSendClient.sendMsg(SatMsgSendParam.builder()
                                               .businessId(request.getBusinessId())
                                               .businessPrincipal(request.getBusinessPrincipal())
                                               .applicationCode(request.getApplicationCode())
                                               .applicationName(request.getApplicationName())
                                               .msgSceneCode(request.getMsgSceneCode())
                                               .msgParams(request.getTmpParams())
                                               .satMsgChannelSendParams(satMsgChannelSendParams)
                                               .tenant(request.getTenant())
                                               .sourceTenant(
                                                       null == request.getSourceTenant() ? request.getTenant() : request.getSourceTenant())
                                               .build());
            return MessageSendResponse.success(userDTOS);
        } catch (Exception e) {
            log.error("消息发送失败,失败原因{}", e.getMessage(), e);
            return MessageSendResponse.fail(e.getMessage());
        }
    }

    /**
     * 获取消息发送用户
     *
     * @param request
     *
     * @return
     */
    private List<UserDTO> getMessageNotifyUsers(SatMsgSceneReceiveDTO msgSceneReceiveByCode, MessageSceneSendRequest request) {
        if (CollectionUtils.isEmpty(request.getMessageNotifyTargets()) &&
            CollectionUtils.isEmpty(msgSceneReceiveByCode.getUsers())) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(request.getMessageNotifyTargets())) {
            Set<Long> userIds = msgSceneReceiveByCode.getUsers().stream().map(SatMsgSceneReceiveUserDTO::getUserId)
                    .collect(Collectors.toSet());
            return this.userQueryService.userIds(userIds).postList();
        }
        List<UserDTO> userDTOS = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<List<UserDTO>>> completableFutures = new ArrayList<>(request.getMessageNotifyTargets().size());
        Integer tenant = Objects.nonNull(request.getTenant()) ? request.getTenant() : SessionContextHolder.getRequester().getTenant();
        for (int i = 0; i < request.getMessageNotifyTargets().size(); i++) {
            int messageNotifyTargetIndex = i;
            completableFutures.add(CompletableFuture.supplyAsync(() -> {
                SessionContextHolder.requestTenant(tenant);
                if (MessageNotifyTargetEnum.DEPARTMENT ==
                    request.getMessageNotifyTargets().get(messageNotifyTargetIndex).getNotifyTargetType()) {
                    this.userQueryService.organizationIds(request.getMessageNotifyTargets().get(messageNotifyTargetIndex).getIds());
                } else if (MessageNotifyTargetEnum.ROLE ==
                           request.getMessageNotifyTargets().get(messageNotifyTargetIndex).getNotifyTargetType()) {
                    this.userQueryService.roleIds(request.getMessageNotifyTargets().get(messageNotifyTargetIndex).getIds());
                } else if (MessageNotifyTargetEnum.USER ==
                           request.getMessageNotifyTargets().get(messageNotifyTargetIndex).getNotifyTargetType()) {
                    this.userQueryService.userIds(request.getMessageNotifyTargets().get(messageNotifyTargetIndex).getIds());
                }
                List<UserDTO> notifyTargetUserDTOList = this.userQueryService.enabled().postList();
                userDTOS.addAll(notifyTargetUserDTOList);
                return notifyTargetUserDTOList;
            }));
        }
        try {
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("Interrupted!", e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error("消息获取用户异常,异常原因{}", e.getMessage(), e);
        } catch (TimeoutException e) {
            log.error("消息获取用户信息超时,异常原因{}", e.getMessage(), e);
        }
        return userDTOS.stream().distinct().collect(Collectors.toList());
    }

    private List<SatMsgChannelSendParam> buildSendChannel(List<ChannelEnum> channels, List<UserDTO> userDTOS) {
        List<SatMsgChannelSendParam> satMsgChannelSendParams = new ArrayList<>(channels.size());
        channels.forEach(channel -> {
            List<MsgSendTargetParam> sendTargetList = ChannelReceiverHolder
                    .getChannel(channel.name())
                    .getReceiver(userDTOS);
            satMsgChannelSendParams.add(new SatMsgChannelSendParam(null, channel, null, null, sendTargetList));
        });
        return satMsgChannelSendParams;
    }

    private List<SatMsgChannelSendParam> buildSpecifyTmpSendChannel(List<MessageChannelRequest> channelRequests,
                                                                    List<ChannelEnum> channels, List<UserDTO> userDTOS
                                                                   ) {
        List<MessageChannelRequest> sceneHaveChannels = channelRequests.stream()
                .filter(channelRequest -> channels.contains(ChannelEnum.valueOf(channelRequest.getChannelCode().name())))
                .collect(Collectors.toList());
        List<SatMsgChannelSendParam> satMsgChannelSendParams = new ArrayList<>(sceneHaveChannels.size());
        sceneHaveChannels.forEach(channel -> {
            List<MsgSendTargetParam> sendTargetList = ChannelReceiverHolder
                    .getChannel(channel.getChannelCode().name())
                    .getReceiver(userDTOS);
            satMsgChannelSendParams.add(new SatMsgChannelSendParam(channel.getTmpId(),
                                                                   ChannelEnum.valueOf(channel.getChannelCode().name()),
                                                                   channel.getTenant(),
                                                                   channel.getExtendConfig(),
                                                                   sendTargetList));
        });
        return satMsgChannelSendParams;
    }
}
