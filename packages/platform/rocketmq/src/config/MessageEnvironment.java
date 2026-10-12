package com.guhecloud.platform.rocketmq.config;

import com.guhecloud.platform.base.util.StringUtils;
import org.apache.rocketmq.common.UtilAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author pengjian
 * @date 2022/6/27 17:17
 * @description
 */
@Configuration
@Component
public class MessageEnvironment {
    private static final String DEFAULT_INSTANCE_NAME = "DEFAULT";

    @Value("${rocketmq.message.environment:}")
    private String messageEnvironment;

    /**
     * 进程内共享的 RocketMQ client instanceName。
     * 未配置时默认 {@code CUBE_<pid>}，保证同 JVM 复用一个 MQClientInstance，跨进程 clientId 不冲突。
     * 注意：不要配置为字面量 {@code DEFAULT}，否则客户端仍会改成 pid#nanoTime。
     */
    @Value("${rocketmq.client.instance-name:}")
    private String clientInstanceName;

    private volatile String resolvedClientInstanceName;

    public String getMessageEnvironment() {
        if (StringUtils.isEmpty(messageEnvironment)) {
            return "";
        } else {
            return messageEnvironment + "_";
        }
    }

    public String getClientInstanceName() {
        String resolved = resolvedClientInstanceName;
        if (resolved != null) {
            return resolved;
        }
        synchronized (this) {
            if (resolvedClientInstanceName != null) {
                return resolvedClientInstanceName;
            }
            String configured = clientInstanceName;
            if (StringUtils.isEmpty(configured) || DEFAULT_INSTANCE_NAME.equals(configured)) {
                // 与 ClientConfig.changeInstanceNameToPID 同算法，进程内只生成一次
                resolvedClientInstanceName = "CUBE_" + UtilAll.getPid() + "#" + System.nanoTime();
            } else {
                resolvedClientInstanceName = configured;
            }
            return resolvedClientInstanceName;
        }
    }
}
