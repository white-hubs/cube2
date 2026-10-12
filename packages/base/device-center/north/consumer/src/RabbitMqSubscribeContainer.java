package com.guhecloud.cube.iot.north.bridge.consumer;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.iot.north.bridge.consumer.cfg.RabbitSubscribeConfig;
import com.guhecloud.cube.iot.north.bridge.consumer.cfg.SubscribeConfig;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.CommandResponseMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceEventAdditionalMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceEventMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DevicePropMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceStatusHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.MsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.RuleEventMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.util.IotNamedThreadFactory;
import com.guhecloud.cube.iot.north.bridge.consumer.util.SubscribeFailException;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceCommandInfoMsg;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceEventAdditionalMsg;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceEventTriggerMsg;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceStatusUpdateMsg;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.LivePropertyUpdateMsg;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.RuleTriggerMsg;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RabbitMqSubscribeContainer implements SubscribeContainer {

    private static final int DEFAULT_THREAD_SIZE = Runtime.getRuntime().availableProcessors();
    private final Collection<DevicePropMsgHandler> devicePropMsgHandler;
    private final Collection<DeviceEventMsgHandler> deviceEventMsgHandler;
    private final Collection<DeviceEventAdditionalMsgHandler> deviceEventAdditionalMsgHandler;
    private final Collection<DeviceStatusHandler> deviceStatusHandler;
    private final Collection<CommandResponseMsgHandler> commandResponseMsgHandler;
    private final Collection<RuleEventMsgHandler> ruleEventMsgHandler;
    private final ExecutorService executorService;
    private final Map<SubscribeMsg, SimpleMessageListenerContainer> listenerContainerMap;
    private final ConnectionFactory connectionFactory;
    private final RabbitSubscribeConfig subscribeConfig;
    private final ScheduledExecutorService keepAliveExecutorService = Executors.newSingleThreadScheduledExecutor();

    public RabbitMqSubscribeContainer(ConnectionFactory connectionFactory, SubscribeConfig subscribeConfig) {
        if (subscribeConfig instanceof RabbitSubscribeConfig) {
            this.subscribeConfig = (RabbitSubscribeConfig) subscribeConfig;
        } else {
            throw new IllegalArgumentException("subscribeConfig is not RabbitSubscribeConfig");
        }

        this.connectionFactory = connectionFactory;

        executorService = new ThreadPoolExecutor(DEFAULT_THREAD_SIZE,
                                                 DEFAULT_THREAD_SIZE * 2,
                                                 60,
                                                 TimeUnit.SECONDS,
                                                 new LinkedBlockingQueue<>(subscribeConfig.getTaskQueueSize()),
                                                 new IotNamedThreadFactory("iot-subscribe-consumer-pool", true),
                                                 new ThreadPoolExecutor.DiscardOldestPolicy());
        listenerContainerMap = new EnumMap<>(SubscribeMsg.class);

        devicePropMsgHandler = new HashSet<>();
        deviceEventMsgHandler = new HashSet<>();
        deviceEventAdditionalMsgHandler = new HashSet<>();
        deviceStatusHandler = new HashSet<>();
        commandResponseMsgHandler = new HashSet<>();
        ruleEventMsgHandler = new HashSet<>();

        //保活
        keepAliveExecutorService.scheduleAtFixedRate(this::keepAlive, 60, 10, TimeUnit.SECONDS);
    }

    private static String nowForFullDateStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 进行连接保活
     */
    private void keepAlive() {
        if (listenerContainerMap.isEmpty()) {
            return;
        }

        listenerContainerMap.forEach((subscribeMsg, simpleMessageListenerContainer) -> {
            if (!simpleMessageListenerContainer.isRunning()) {
                listenMsg(subscribeMsg, true);
            }
        });
    }

    /**
     * 注册消息处理器
     *
     * @param msgHandlers 消息处理器列表
     */
    public synchronized void registerMsgHandlers(List<MsgHandler> msgHandlers) {
        msgHandlers.forEach(msgHandler -> {
            switch (msgHandler) {
                case DevicePropMsgHandler propMsgHandler -> {
                    devicePropMsgHandler.add(propMsgHandler);

                    listenMsg(SubscribeMsg.DEVICE_PROP, false);
                }
                case DeviceEventMsgHandler eventMsgHandler -> {
                    deviceEventMsgHandler.add(eventMsgHandler);
                    listenMsg(SubscribeMsg.DEVICE_EVENT, false);
                }
                case DeviceEventAdditionalMsgHandler eventAdditionalMsgHandler -> {
                    deviceEventAdditionalMsgHandler.add(eventAdditionalMsgHandler);
                    listenMsg(SubscribeMsg.DEVICE_EVENT_ADDITION, false);
                }
                case DeviceStatusHandler statusHandler -> {
                    deviceStatusHandler.add(statusHandler);
                    listenMsg(SubscribeMsg.DEVICE_STATUS, false);
                }
                case CommandResponseMsgHandler responseMsgHandler -> {
                    commandResponseMsgHandler.add(responseMsgHandler);
                    listenMsg(SubscribeMsg.DEVICE_COMMAND, false);
                }
                case RuleEventMsgHandler eventMsgHandler -> {
                    ruleEventMsgHandler.add(eventMsgHandler);
                    listenMsg(SubscribeMsg.RULE_TRIGGER_EVENT, false);
                }
                case null, default -> throw new IllegalArgumentException("未知的消息处理器类型");
            }
        });
    }

    /**
     * 订阅消息
     *
     * @param subscribeMsg 订阅消息
     */
    private synchronized void listenMsg(SubscribeMsg subscribeMsg, boolean overWrite) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = listenerContainerMap.get(subscribeMsg);
        if (Objects.isNull(simpleMessageListenerContainer) || overWrite) {
            //根据约定规则拼接所需要的exchange
            String exchangeName = "north-notify." + subscribeMsg.name();
            String queueName = subscribeMsg.name() + "." + subscribeConfig.getConsumerGroup();

            //交换机和队列绑定
            dynamicBindQueue(exchangeName, queueName);
            log.info("bind  exchange : {} to queue : {}", exchangeName, queueName);

            //创建监听器
            simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
            simpleMessageListenerContainer.addQueueNames(queueName);
            simpleMessageListenerContainer.setMessageListener(new DispatcheMessageListener(subscribeMsg));
            simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);

            //缓存并启动监听器
            listenerContainerMap.put(subscribeMsg, simpleMessageListenerContainer);
            simpleMessageListenerContainer.start();
        }
    }

    /**
     * 动态绑定队列<br/>
     * 自动创建一个随机队列并绑定到指定的交换机上
     *
     * @param exchangeName 交换机名称
     * @param queueName    队列名称
     */
    private void dynamicBindQueue(String exchangeName, String queueName) {
        try (Connection connection = connectionFactory.createConnection(); Channel channel = connection.createChannel(false)) {

            ensureExchange(channel, exchangeName);

            channel.queueDeclare(queueName, false, false, true, null);//使用完毕自动清理

            AMQP.Queue.BindOk result = channel.queueBind(queueName, exchangeName, "empty");
            log.info("dynamicBindQueue result: {}", result);
        } catch (Exception e) {
            log.error("dynamicBindQueue error", e);
            throw new SubscribeFailException("can't connect to rabbitmq", e);
        }
    }

    /**
     * 确保交换机存在
     *
     * @param channel      channel
     * @param exchangeName 交换机名称
     */
    private void ensureExchange(Channel channel, String exchangeName) {
        try {
            channel.exchangeDeclare(exchangeName, "fanout", true);
        } catch (Exception e) {
            log.error("ensureExchange error", e);
            throw new SubscribeFailException("can't connect to rabbitmq", e);
        }
    }

    /**
     * 取出当前时间的后8位
     */
    private String subStringCurtTime() {
        String curtTime = String.valueOf(System.currentTimeMillis());

        //取出最后10位
        return curtTime.substring(curtTime.length() - 8);
    }

    @PreDestroy
    public void destroy() {
        if (Objects.nonNull(listenerContainerMap)) {
            listenerContainerMap.values().forEach(SimpleMessageListenerContainer::stop);
        }

        executorService.shutdownNow();
    }

    /**
     * 消息分发器
     */
    class DispatcheMessageListener implements MessageListener {

        private final SubscribeMsg subscribeMsg;

        public DispatcheMessageListener(SubscribeMsg subscribeMsg) {
            this.subscribeMsg = subscribeMsg;
        }

        @Override
        public void onMessage(Message message) {
            String msgStr = new String(message.getBody());

            try {
                msgStr = JSON.parseObject(msgStr).getString("msg");
                switch (subscribeMsg) {
                    case DEVICE_STATUS:
                        if (deviceStatusHandler.isEmpty()) {
                            log.warn("North consumer: deviceStatusHandler is empty");
                            return;
                        }

                        DeviceStatusUpdateMsg deviceStatusUpdateMsg = JSON.parseObject(msgStr, DeviceStatusUpdateMsg.class);

                        //提交任务
                        executorService.submit(() -> {
                            for (DeviceStatusHandler statusHandler : deviceStatusHandler) {
                                try {
                                    log.debug("North consumer invoke handler : deviceStatusHandler: {}, msg: {}, localTime: {}",
                                              statusHandler, deviceStatusUpdateMsg, nowForFullDateStr());
                                    statusHandler.handle(deviceStatusUpdateMsg);
                                } catch (Exception e) {
                                    log.error("deviceStatusHandler error", e);
                                }
                            }
                        });
                        break;
                    case DEVICE_EVENT:
                        if (deviceEventMsgHandler.isEmpty()) {
                            log.warn("North consumer: deviceEventMsgHandler is empty");
                            return;
                        }

                        DeviceEventTriggerMsg deviceEventTriggerMsg = JSON.parseObject(msgStr, DeviceEventTriggerMsg.class);

                        executorService.submit(() -> {
                            for (DeviceEventMsgHandler eventMsgHandler : deviceEventMsgHandler) {
                                try {
                                    log.info("North consumer invoke handler : deviceEventMsgHandler: {}, msg: {}, localTime: {}",
                                             eventMsgHandler, deviceEventTriggerMsg, nowForFullDateStr());
                                    eventMsgHandler.handle(deviceEventTriggerMsg);
                                } catch (Exception e) {
                                    log.error("deviceEventMsgHandler error", e);
                                }
                            }
                        });
                        break;
                    case DEVICE_EVENT_ADDITION:
                        if (deviceEventAdditionalMsgHandler.isEmpty()) {
                            log.warn("North consumer: deviceEventAdditionalMsgHandler is empty");
                            return;
                        }

                        DeviceEventAdditionalMsg deviceEventAdditionalMsg = JSON.parseObject(msgStr, DeviceEventAdditionalMsg.class);

                        executorService.submit(() -> {
                            for (DeviceEventAdditionalMsgHandler eventAdditionalMsgHandler : deviceEventAdditionalMsgHandler) {
                                try {
                                    log.info("North consumer invoke handler : deviceEventAdditionalMsgHandler: {}, msg: {}, localTime: {}",
                                             eventAdditionalMsgHandler, deviceEventAdditionalMsg, nowForFullDateStr());
                                    eventAdditionalMsgHandler.handle(deviceEventAdditionalMsg);
                                } catch (Exception e) {
                                    log.error("deviceEventAdditionalMsgHandler error", e);
                                }
                            }
                        });
                        break;
                    case DEVICE_PROP:
                        if (devicePropMsgHandler.isEmpty()) {
                            return;
                        }

                        LivePropertyUpdateMsg livePropertyUpdateMsg = JSON.parseObject(msgStr, LivePropertyUpdateMsg.class);
                        executorService.submit(() -> {
                            for (DevicePropMsgHandler propMsgHandler : devicePropMsgHandler) {
                                try {
                                    propMsgHandler.handle(livePropertyUpdateMsg);
                                } catch (Exception e) {
                                    log.error("devicePropMsgHandler error", e);
                                }
                            }
                        });
                        break;
                    case DEVICE_COMMAND:
                        if (commandResponseMsgHandler.isEmpty()) {
                            log.warn("North consumer: commandResponseMsgHandler is empty");
                            return;
                        }

                        DeviceCommandInfoMsg deviceCommandInfoMsg = JSON.parseObject(msgStr, DeviceCommandInfoMsg.class);
                        executorService.submit(() -> {
                            for (CommandResponseMsgHandler responseMsgHandler : commandResponseMsgHandler) {
                                try {
                                    log.info("North consumer invoke handler : commandResponseMsgHandler: {}, msg: {}, localTime: {}",
                                             responseMsgHandler, deviceCommandInfoMsg, nowForFullDateStr());
                                    responseMsgHandler.handle(deviceCommandInfoMsg);
                                } catch (Exception e) {
                                    log.error("commandResponseMsgHandler error", e);
                                }
                            }
                        });
                        break;
                    case RULE_TRIGGER_EVENT:
                        if (ruleEventMsgHandler.isEmpty()) {
                            log.warn("North consumer: ruleEventMsgHandler is empty");
                            return;
                        }

                        RuleTriggerMsg ruleTriggerMsg = JSON.parseObject(msgStr, RuleTriggerMsg.class);
                        executorService.submit(() -> {
                            for (RuleEventMsgHandler eventMsgHandler : ruleEventMsgHandler) {
                                try {
                                    log.info("North consumer invoke handler : ruleEventMsgHandler: {}, msg: {}, localTime: {}",
                                             eventMsgHandler, ruleTriggerMsg, nowForFullDateStr());
                                    eventMsgHandler.handle(ruleTriggerMsg);
                                } catch (Exception e) {
                                    log.error("ruleEventMsgHandler error", e);
                                }
                            }
                        });
                        break;
                }
            } catch (Exception e) {
                log.error("dispatcher MessageListener error", e);
            }
        }
    }
}
