package com.guhecloud.cube.base.file.server.event;

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
 * @CreateTime: 2024-08-07  14:46
 * @Description: 租户初始化文件配置
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = FileTenantInitMessage.TOPIC + "_FILE", topic = FileTenantInitMessage.TOPIC)
public class FileInitialisationTenantEvent implements RocketMQListener<FileTenantInitMessage> {
    private final FileInitialisationTenantService initialisationTenantService;

    @Override
    public void onMessage(FileTenantInitMessage message) {
        SessionContextHolder.clearSessionContext();
        SessionContextHolder.requestTenant(message.getTenant());
        initialisationTenantService.initialisationTenant(message.getTenant());
        SessionContextHolder.clearSessionContext();
    }
}
