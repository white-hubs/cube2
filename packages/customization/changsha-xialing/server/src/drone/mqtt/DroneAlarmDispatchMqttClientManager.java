package com.guhecloud.cube.customization.xialing.server.drone.mqtt;

import com.guhecloud.cube.customization.xialing.server.drone.auth.DroneThirdPartyConfigProvider;
import com.guhecloud.cube.customization.xialing.server.drone.dto.XianingLoginCredentialDTO;
import com.guhecloud.cube.customization.xialing.server.drone.service.DroneAlarmDispatchCaptureService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 霞凝无人机报警派飞 MQTT 飞机状态订阅管理。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DroneAlarmDispatchMqttClientManager {
    private static final String DRONE_STATUS_TOPIC_PREFIX = "gdu/drone_status/";
    private static final String NEST_TASK_KEY_PREFIX = "drone:alarm-dispatch:capture:nest:";
    private static final String CLIENT_ID_DEFAULT_PREFIX = "cube-drone-alarm-dispatch";

    private final DroneThirdPartyConfigProvider configProvider;
    private final ObjectProvider<DroneAlarmDispatchCaptureService> captureServiceProvider;
    private final StringRedisTemplate stringRedisTemplate;
    private final Map<Integer, TenantMqttClient> clients = new ConcurrentHashMap<>();

    /**
     * 服务重启后按 Redis 中尚未过期的待拍摄任务恢复订阅。
     */
    @PostConstruct
    public void restoreSubscriptions() {
        try {
            Set<String> keys = stringRedisTemplate.keys(NEST_TASK_KEY_PREFIX + "*");
            if (keys == null || keys.isEmpty()) {
                log.info("XIANINGDRONE_MQTT_RESTORE_SKIP reason=no_pending_nest_keys");
                return;
            }
            log.info("XIANINGDRONE_MQTT_RESTORE_BEGIN pendingNestKeyCount={}", keys.size());
            for (String key : keys) {
                TenantNest tenantNest = parseTenantNest(key);
                if (tenantNest != null) {
                    try {
                        ensureSubscribed(tenantNest.tenant(), tenantNest.nestSn());
                    } catch (Exception e) {
                        log.warn("XIANINGDRONE_MQTT_RESTORE_SUBSCRIBE_FAIL tenant={} nestSn={}", tenantNest.tenant(), tenantNest.nestSn(), e);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("XIANINGDRONE_MQTT_RESTORE_FAIL", e);
        }
    }

    /**
     * 确保指定租户 MQTT client 已连接并订阅机库状态主题。
     */
    public void ensureSubscribed(Integer tenant, String nestSn) {
        if (tenant == null || StringUtils.isBlank(nestSn)) {
            throw new ServiceException("MQTT订阅租户或机库编号为空");
        }
        clients.computeIfAbsent(tenant, TenantMqttClient::new).ensureSubscribed(nestSn.trim());
    }

    @PreDestroy
    public void destroy() {
        clients.values().forEach(TenantMqttClient::close);
        clients.clear();
    }

    private TenantNest parseTenantNest(String key) {
        if (StringUtils.isBlank(key) || !key.startsWith(NEST_TASK_KEY_PREFIX)) {
            return null;
        }
        String value = key.substring(NEST_TASK_KEY_PREFIX.length());
        int index = value.indexOf(':');
        if (index <= 0 || index >= value.length() - 1) {
            return null;
        }
        try {
            return new TenantNest(Integer.valueOf(value.substring(0, index)), value.substring(index + 1));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private class TenantMqttClient implements MqttCallbackExtended {
        private final Integer tenant;
        private final Set<String> subscribedNestSns = ConcurrentHashMap.newKeySet();
        private MqttClient client;

        private TenantMqttClient(Integer tenant) {
            this.tenant = tenant;
        }

        private synchronized void ensureSubscribed(String nestSn) {
            try {
                connectIfNecessary();
                String topic = DRONE_STATUS_TOPIC_PREFIX + nestSn;
                if (subscribedNestSns.add(nestSn)) {
                    log.info("XIANINGDRONE_MQTT_SUBSCRIBE_BEGIN tenant={} topic={}", tenant, topic);
                    client.subscribe(topic, 0);
                    log.info("XIANINGDRONE_MQTT_SUBSCRIBE_SUCCESS tenant={} topic={}", tenant, topic);
                }
            } catch (MqttException e) {
                subscribedNestSns.remove(nestSn);
                throw new ServiceException("无人机报警派飞MQTT订阅失败: " + e.getMessage());
            }
        }

        private void connectIfNecessary() throws MqttException {
            if (client != null && client.isConnected()) {
                return;
            }
            XianingLoginCredentialDTO credential = credential();
            if (StringUtils.isBlank(credential.getMqttHost())) {
                log.warn("XIANINGDRONE_MQTT_CONNECT_FAIL tenant={} reason=mqtt_host_empty", tenant);
                throw new ServiceException("霞凝无人机MQTT地址为空");
            }
            if (client == null) {
                client = new MqttClient(credential.getMqttHost().trim(), clientId(credential), new MemoryPersistence());
                client.setCallback(this);
            }
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            if (StringUtils.isNotBlank(credential.getMqttUsername())) {
                options.setUserName(credential.getMqttUsername().trim());
            }
            if (credential.getMqttPassword() != null) {
                options.setPassword(credential.getMqttPassword().toCharArray());
            }
            log.info("XIANINGDRONE_MQTT_CONNECT_BEGIN tenant={} host={} usernameConfigured={}", tenant, credential.getMqttHost(),
                     StringUtils.isNotBlank(credential.getMqttUsername()));
            client.connect(options);
            log.info("XIANINGDRONE_MQTT_CONNECT_SUCCESS tenant={} host={}", tenant, credential.getMqttHost());
        }

        private XianingLoginCredentialDTO credential() {
            try {
                SessionContextHolder.requestTenant(tenant);
                return configProvider.currentCredential();
            } finally {
                SessionContextHolder.requestTenant(null);
            }
        }

        private String clientId(XianingLoginCredentialDTO credential) {
            String prefix = StringUtils.isNotBlank(credential.getMqttClientIdPrefix())
                            ? credential.getMqttClientIdPrefix().trim()
                            : CLIENT_ID_DEFAULT_PREFIX;
            return prefix + "-" + tenant + "-" + System.currentTimeMillis();
        }

        private void close() {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (MqttException e) {
                log.warn("无人机报警派飞 MQTT 关闭失败 tenant={}", tenant, e);
            }
        }

        @Override
        public void connectionLost(Throwable cause) {
            log.warn("XIANINGDRONE_MQTT_CONNECTION_LOST tenant={}", tenant, cause);
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            if (!reconnect || subscribedNestSns.isEmpty()) {
                return;
            }
            log.info("XIANINGDRONE_MQTT_RECONNECT_COMPLETE tenant={} serverURI={} subscribedNestCount={}", tenant, serverURI,
                     subscribedNestSns.size());
            for (String nestSn : subscribedNestSns) {
                try {
                    client.subscribe(DRONE_STATUS_TOPIC_PREFIX + nestSn, 0);
                    log.info("XIANINGDRONE_MQTT_RESUBSCRIBE_SUCCESS tenant={} nestSn={}", tenant, nestSn);
                } catch (MqttException e) {
                    log.warn("XIANINGDRONE_MQTT_RESUBSCRIBE_FAIL tenant={} nestSn={}", tenant, nestSn, e);
                }
            }
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            String nestSn = topic == null || !topic.startsWith(DRONE_STATUS_TOPIC_PREFIX)
                            ? null
                            : topic.substring(DRONE_STATUS_TOPIC_PREFIX.length());
            if (StringUtils.isBlank(nestSn) || message == null) {
                log.info("XIANINGDRONE_MQTT_MESSAGE_SKIP reason=invalid_topic_or_message tenant={} topic={}", tenant, topic);
                return;
            }
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            log.info("XIANINGDRONE_MQTT_MESSAGE_RECEIVED tenant={} topic={} nestSn={} payloadLength={}", tenant, topic, nestSn,
                     payload.length());
            captureServiceProvider.getObject().handleStatusMessage(tenant, nestSn, payload);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            // This client only subscribes.
        }
    }

    private record TenantNest(Integer tenant, String nestSn) {
    }
}
