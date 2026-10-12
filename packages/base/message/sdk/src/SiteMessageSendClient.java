package com.guhecloud.cube.base.message.sdk;

import com.guhecloud.cube.base.message.sdk.api.request.SiteBroadcastSendRequest;
import com.guhecloud.cube.base.message.sdk.api.request.SiteSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.base.message2.client.SatMsgSiteSendClient;
import com.guhecloud.cube.base.message2.client.param.SatMsgSiteEventDataParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgSiteSendParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgSiteTargetModifyParam;
import com.guhecloud.cube.base.message2.client.param.SatSiteBroadcastSendParam;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SiteMessageSendClient {

    @Autowired
    private SatMsgSiteSendClient satMsgSiteSendClient;

    public MessageSendResponse broadcastSend(SiteBroadcastSendRequest siteBroadcastSendRequest) {
        try {
            SatSiteBroadcastSendParam satSiteBroadcastSendParam = ObjectUtils.copying(siteBroadcastSendRequest, SatSiteBroadcastSendParam.class);
            satSiteBroadcastSendParam.setBusinessPrincipal(siteBroadcastSendRequest.getBusinessPrincipal());
            Boolean sendBroadcastSite = this.satMsgSiteSendClient.sendBroadcastSite(satSiteBroadcastSendParam);
            if (sendBroadcastSite) {
                return MessageSendResponse.success();
            }
        } catch (Exception e) {
            log.error("站内广播消息发送失败,失败原因{}", e.getMessage(), e);
        }
        return MessageSendResponse.fail();
    }

    public MessageSendResponse customerBroadcastSend(SiteBroadcastSendRequest siteBroadcastSendRequest) {
        try {
            SatSiteBroadcastSendParam satSiteBroadcastSendParam = ObjectUtils.copying(siteBroadcastSendRequest, SatSiteBroadcastSendParam.class);
            satSiteBroadcastSendParam.setBusinessPrincipal(siteBroadcastSendRequest.getBusinessPrincipal());
            Boolean sendBroadcastSite = this.satMsgSiteSendClient.sendCustomerBroadcastSite(satSiteBroadcastSendParam);
            if (sendBroadcastSite) {
                return MessageSendResponse.success();
            }
        } catch (Exception e) {
            log.error("站内广播消息发送失败,失败原因{}", e.getMessage(), e);
        }
        return MessageSendResponse.fail();
    }

    public MessageSendResponse send(SiteSendRequest siteSendRequest) {
        try {
            SatMsgSiteSendParam satMsgSiteSendParam = SatMsgSiteSendParam.builder()
                    .msgType(siteSendRequest.getEventType())
                    .eventDataParam(new SatMsgSiteEventDataParam(siteSendRequest.getMsgType(), siteSendRequest.getContent()))
                    .targetModifyParams(this.buildSatMsgSiteTargetModifyParam(siteSendRequest))
                    .build();
            this.satMsgSiteSendClient.sendSite(satMsgSiteSendParam);
        } catch (Exception e) {
            log.error("站内消息发送失败,失败原因{}", e.getMessage(), e);
        }
        return MessageSendResponse.fail();
    }

    public MessageSendResponse terminalUserSend(SiteSendRequest siteSendRequest) {
        try {
            SatMsgSiteSendParam satMsgSiteSendParam = SatMsgSiteSendParam.builder()
                    .msgType(siteSendRequest.getEventType())
                    .eventDataParam(new SatMsgSiteEventDataParam(siteSendRequest.getMsgType(), siteSendRequest.getContent()))
                    .targetModifyParams(this.buildSatMsgSiteTargetModifyParam(siteSendRequest))
                    .build();
            this.satMsgSiteSendClient.sendByTerminal(satMsgSiteSendParam);
        } catch (Exception e) {
            log.error("站内消息发送失败,失败原因{}", e.getMessage(), e);
        }
        return MessageSendResponse.fail();
    }

    private List<SatMsgSiteTargetModifyParam> buildSatMsgSiteTargetModifyParam(SiteSendRequest siteSendRequest) {
        List<SatMsgSiteTargetModifyParam> satMsgSiteTargetModifyParams = new ArrayList<>();
        siteSendRequest.getUserIds().forEach(user -> {
            siteSendRequest.getTerminals().forEach(terminal -> {
                SatMsgSiteTargetModifyParam satMsgSiteTargetModifyParam = new SatMsgSiteTargetModifyParam();
                satMsgSiteTargetModifyParam.setUserId(user);
                satMsgSiteTargetModifyParam.setTerminal(terminal);
                satMsgSiteTargetModifyParams.add(satMsgSiteTargetModifyParam);
            });
        });
        return satMsgSiteTargetModifyParams;
    }
}
