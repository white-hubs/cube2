package com.guhecloud.cube.srv.gen.property.management.server.property.util;

import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.property.management.client.property.enumeration.MessageBusinessTypeEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2023-10-27  11:10
 * @Description:
 * @Version: 1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageUtil {
    private final AssemblyNoticeClient assemblyNoticeClient;

    public MessageSendConfig getSendMsgConfig(String sceneCodes) {
        try {
            MessageSendSceneQueryPram queryPram = MessageSendSceneQueryPram.builder()
                    .businessType(MessageBusinessTypeEnum.PROPERTY.name())
                    .sceneCodes(Collections.singletonList(sceneCodes))
                    .build();
            List<MessageSendConfig> conditions = assemblyNoticeClient.getSendMsgConfig(queryPram);
            if (ObjectUtils.isNotEmpty(conditions)) {
                return conditions.getFirst();
            }
        } catch (Exception e) {
            log.error("获取消息配置异常：", e);
        }
        return MessageSendConfig.builder().build();
    }

    public List<MessageSendConfig> getSendMsgConfig(List<String> sceneCodes) {
        try {
            MessageSendSceneQueryPram queryPram = MessageSendSceneQueryPram.builder()
                    .businessType(MessageBusinessTypeEnum.PROPERTY.name())
                    .sceneCodes(sceneCodes)
                    .build();
            List<MessageSendConfig> conditions = assemblyNoticeClient.getSendMsgConfig(queryPram);
            return ObjectUtils.isNotEmpty(conditions) ? conditions : new ArrayList<>();
        } catch (Exception e) {
            log.error("获取消息配置异常：", e);
        }
        return new ArrayList<>();
    }

    public Boolean messageSend(String sceneCodes, Map<String, String> map) {
        return this.messageSend(this.getSendMsgConfig(sceneCodes), map);
    }

    public Boolean messageSend(String sceneCodes, Map<String, String> map, Long usrId) {
        Set<Long> userIds = new HashSet<>();
        userIds.add(usrId);
        return this.messageSend(this.getSendMsgConfig(sceneCodes), map, userIds);
    }

    public Boolean messageSend(MessageSendConfig conditions, Map<String, String> map) {
        return this.messageSend(conditions, map, null);
    }

    public Boolean messageSend(MessageSendConfig conditions, Map<String, String> map, Set<Long> userIds) {
        try {
            if (ObjectUtils.isNotEmpty(conditions)) {
                MessageSendParam sendParam = MessageSendParam.builder()
                        .notifyId(conditions.getNotifyId())
                        .tempParams(map)
                        .notifyUsers(userIds)
                        .build();
                MessageSendResponse sendResponse = assemblyNoticeClient.sendMsg(sendParam);
                log.info("资产消息发送结果：{}", sendResponse.getMessage());
                return true;
            }
        } catch (Exception e) {
            log.error("消息发送失败：", e);
        }
        return false;
    }
}
