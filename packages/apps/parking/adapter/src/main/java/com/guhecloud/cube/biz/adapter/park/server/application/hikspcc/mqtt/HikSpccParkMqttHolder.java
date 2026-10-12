package com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.mqtt;

import com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.model.HikSpccParkConfig;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Slf4j
public class HikSpccParkMqttHolder implements MqttCallback {

    private final ScheduledExecutorService executorService;
    private final BiConsumer<String, MqttMessage> msgConsumer;
    private final AtomicBoolean subscribeSuccess = new AtomicBoolean(false);
    private MqttClient mqttClient;
    private HikSpccParkConfig config;

    /**
     * 构造函数
     *
     * @param config      配置
     * @param msgConsumer 消息消费者, key为topic, value为消息
     */
    public HikSpccParkMqttHolder(HikSpccParkConfig config, BiConsumer<String, MqttMessage> msgConsumer) {
        this.config = config;
        this.msgConsumer = msgConsumer;
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        executorService.scheduleAtFixedRate(this::reConnectIfLost, 0, 10, TimeUnit.SECONDS);
    }

    public void stop() {
        executorService.shutdownNow();
        try {
            if (Objects.nonNull(mqttClient)) {
                quiteSleepMs(5000);
                mqttClient.close();
            }
        } catch (MqttException e) {
            log.error("关闭停车场mqttClient失败", e);
        }
    }

    /**
     * 重连
     */
    private void reConnectIfLost() {
        try {
            if (mqttClient == null) {
                mqttClient = buildClient();
            }

            if (!mqttClient.isConnected()) {
                mqttClient.reconnect();

                //等待连接成功
                quiteSleepMs(2000);
            }

            //已连接成功，但是订阅失败
            if (mqttClient.isConnected() && (!subscribeSuccess.get())) {
                log.debug("连接到停车场EMQX成功");
                AtomicInteger successCount = new AtomicInteger(0);
                //连接丢失后需要从新订阅
                try {
                    mqttClient.subscribe(config.getCarEventVcheckTopic(), 0);
                    log.debug("订阅租户code:{} 成功", config.getTenant());
                    successCount.incrementAndGet();
                } catch (MqttException e) {
                    log.debug("订阅失败租户code:{} 失败", config.getTenant(), e);
                }
                //订阅数量一致，说明订阅成功
                successCount.compareAndSet(1, successCount.get());
            }
        } catch (Exception e) {
            log.error("连接到停车场EMQX失败", e);
        }
    }

    /**
     * 构建mqtt客户端<br/>
     * 前期先监听所有租户
     *
     * @return mqtt客户端
     */
    private MqttClient buildClient() throws MqttException {
        MqttClient client = new MqttClient(config.getHost(), config.getMqttClientId(), new MemoryPersistence());
        //等待1.5秒中，内部验证逻辑waitTime<=0 时，会一直等待{@link org.eclipse.paho.client.mqttv3.internal.Token.waitForResponse(long)}
        client.setTimeToWait(1500);
        // MQTT 连接选项
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(config.getMqttLoginName());
        connOpts.setPassword(config.getMqttPassword().toCharArray());
        // 保留会话
        connOpts.setCleanSession(true);
        connOpts.setAutomaticReconnect(false);

        // 设置回调
        client.setCallback(this);

        // 建立连接
        client.connect(connOpts);
        log.info("Connecting to broker: {}", config.getHost());
        return client;
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.warn("停车场mqtt连接断开，尝试重连", cause);
        //重连在keepAlive中处理
        subscribeSuccess.set(false);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        this.msgConsumer.accept(topic, message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("消息发送成功");
    }

    private void quiteSleepMs(long sleep) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
