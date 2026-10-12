package com.guhecloud.cube.base.flow.flowable.common.message;

import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/9/2 8:58
 * @description
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageSender {
    private final Map<String, String> messageBody = new HashMap<>();

    public MessageSender appendMessageBody(String key, String value) {
        messageBody.put(key, value);
        return this;
    }

    public MessageSender appendProcessInstanceId(String processInstanceId) {
        messageBody.put("processInstanceId", processInstanceId);
        return this;
    }

    public MessageSender appendApplication(ApplicationId applicationId) {
        messageBody.put("applicationCode", applicationId.getApplicationCode());
        messageBody.put("applicationName", applicationId.getApplicationName());
        return this;
    }

    public static List<NotifyUser> buildNotifyUser(Set<Long> userIds) {
        List<NotifyUser> notifyUsers = new ArrayList<>();
        userIds.forEach(userId -> {
            NotifyUser notifyUser = new NotifyUser(userId);
            notifyUsers.add(notifyUser);
        });
        return notifyUsers;
    }

    /**
     * 消息发送
     *
     * @param msgDefineCode 消息定义code
     * @param notifyUsers   消息接收人员
     */
    public void send(String msgDefineCode, List<NotifyUser> notifyUsers) {
        if (CollectionUtils.isEmpty(notifyUsers)) {
            return;
        }
        MessageSceneSendRequest message = new MessageSceneSendRequest();
        message.setTenant(SessionContextHolder.getRequester().getTenant());
        message.setMsgSceneCode(msgDefineCode);
        message.setApplicationCode(messageBody.get("applicationCode"));
        message.setApplicationName(messageBody.get("applicationName"));
        message.setTmpParams(messageBody);
        message.setBusinessId(messageBody.get("processInstanceId"));
        MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
        notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
        Set<Long> ids = notifyUsers.stream().map(NotifyUser::getUserId).collect(Collectors.toSet());
        notifyTarget.setIds(ids);
        message.setMessageNotifyTargets(Collections.singletonList(notifyTarget));
        MessageSendClient sendClient = ApplicationContextHolder.getBean(MessageSendClient.class);
        try {
            ExecutorContextHolder.run(() -> {
                //传递租户信息
                SessionContextHolder.requestTenant(message.getTenant());
                MessageSendResponse messageSendResponse = sendClient.sceneSend(message);
                log.info("消息:{},发送结果,{}", msgDefineCode, JsonUtils.serialize(messageSendResponse));
            });
        } catch (Exception e) {
            log.error("消息发送失败:{}", e.getMessage());
        }
    }
}
