package com.guhecloud.cube.base.file.server.event;

import com.guhecloud.cube.base.dictionary.event.DictionaryMessage;
import com.guhecloud.cube.base.file.server.service.FileInitialisationTenantService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-18  19:08
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = DictionaryMessage.TOPIC + "_FILE", topic = DictionaryMessage.TOPIC,
                         selectorExpression = "FILE_SCENE_CODE"
)
public class FileDictionaryEvent implements RocketMQListener<DictionaryMessage> {
    private final FileInitialisationTenantService initialisationTenantService;

    @Override
    public void onMessage(DictionaryMessage message) {
        log.info("文件服务收到字典消息:{}", message);
        SessionContextHolder.clearSessionContext();
        if ("ADD".equals(message.getOperationType())) {
            initialisationTenantService.addConfigAllTenant(message.getName(), message.getCode(), message.getVariable());
        } else if ("UPDATE".equals(message.getOperationType())) {
            initialisationTenantService.updateConfigAllTenant(message.getName(), message.getCode(), message.getVariable());
        }
        SessionContextHolder.clearSessionContext();
    }
}
