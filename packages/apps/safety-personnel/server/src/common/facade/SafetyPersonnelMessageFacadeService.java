package com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade;

import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.SafetyPersonnelConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 消息服务访问门面。
 */
@Service("safetyPersonnelMessageFacade")
@RequiredArgsConstructor
public class SafetyPersonnelMessageFacadeService {

    private final MessageSendClient messageSendClient;

    /**
     * 按场景发送系统通知。
     *
     * @param request 场景发送请求
     *
     * @return 发送结果
     */
    public MessageSendResponse sceneSend(MessageSceneSendRequest request) {
        return messageSendClient.sceneSend(request);
    }

    /**
     * 证书到期提醒场景码。
     *
     * @return 场景编码
     */
    public String certWarnSceneCode() {
        return SafetyPersonnelConstants.MESSAGE_SCENE_CERT_WARN;
    }
}
