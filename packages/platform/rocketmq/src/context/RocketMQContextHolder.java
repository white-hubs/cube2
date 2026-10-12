package com.guhecloud.platform.rocketmq.context;

import com.google.common.collect.Maps;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.rocketmq.config.MessageEnvironment;
import com.guhecloud.platform.rocketmq.producer.RocketMQTransactionChecker;
import com.guhecloud.platform.rocketmq.producer.RocketMQTransactionExecutor;
import com.guhecloud.platform.rocketmq.producer.RocketMQTransactionHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * RocketMQ上下文工具类
 *
 * @author Woody
 * @date 2021/9/6
 */
@Slf4j
@Component
@RocketMQTransactionListener
@ConditionalOnClass(RocketMQTemplate.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RocketMQContextHolder implements ApplicationContextAware, RocketMQLocalTransactionListener {
    /**
     * RocketMQ主题消息头标识
     */
    private static final String ROCKETMQ_TOPIC_HEADER = "rocketmq_TOPIC";
    private static final String ROCKETMQ_TAGS_HEADER = "rocketmq_TAGS";

    /**
     * RocketMQ模版实例
     */
    private static RocketMQTemplate ROCKETMQ_TEMPLATE;

    /**
     * 异常上下文
     */
    private static final ThreadLocal<RuntimeException> EXCEPTION_CONTEXT = new ThreadLocal<>();

    /**
     * Topic/本地事务检测器映射表
     */
    private static Map<String, RocketMQTransactionChecker<?>> TOPIC_CHECKER_MAPPING = Collections.emptyMap();

    /**
     * Topic/本地事务执行器映射表
     */
    private static Map<String, RocketMQTransactionExecutor<?>> TOPIC_EXECUTOR_MAPPING = Collections.emptyMap();

    private final RocketMQMessageConverter rocketmqMessageConverter;
    @Autowired
    private MessageEnvironment messageEnvironment;

    private static String getSendTopic(String originalTopic) {
        originalTopic =
                ApplicationContextHolder.getBean(RocketMQContextHolder.class).messageEnvironment.getMessageEnvironment() + originalTopic;
        return originalTopic;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        // 初始化RocketMQ模版实例
        ROCKETMQ_TEMPLATE = applicationContext.getBean(RocketMQTemplate.class);

        // 初始化topic/本地事务检测实例映射表
        Collection<RocketMQTransactionHandler> handlers =
                applicationContext.getBeansOfType(RocketMQTransactionHandler.class).values();
        List<RocketMQTransactionChecker> checkers = handlers.stream()
                .filter(handler -> handler instanceof RocketMQTransactionChecker)
                .map(handler -> (RocketMQTransactionChecker) handler).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(checkers)) {
            log.info("Loading rocketmq transaction checkers: {}", checkers);
            TOPIC_CHECKER_MAPPING = Maps.newHashMapWithExpectedSize(checkers.size());
            checkers.forEach(checker -> {
                String topic = messageEnvironment.getMessageEnvironment() + checker.getTopic();
                Assert.isTrue(!StringUtils.isEmpty(topic),
                              "Rocketmq transaction checker topic not specified: " + checker);
                Assert.isTrue(!TOPIC_CHECKER_MAPPING.containsKey(topic),
                              "Rocketmq transaction checker topic already exists: " + checker);
                TOPIC_CHECKER_MAPPING.put(topic, checker);
            });
        }

        // 初始化topic/本地事务执行实例映射表
        List<RocketMQTransactionExecutor> executors = handlers.stream()
                .filter(handler -> handler instanceof RocketMQTransactionExecutor)
                .map(handler -> (RocketMQTransactionExecutor) handler).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(executors)) {
            log.info("Loading rocketmq transaction executors: {}", executors);
            TOPIC_EXECUTOR_MAPPING = Maps.newHashMapWithExpectedSize(executors.size());
            executors.forEach(executor -> {
                String topic = executor.getTopic();
                Assert.isTrue(!StringUtils.isEmpty(topic),
                              "Rocketmq transaction executor topic not specified: " + executor);
                Assert.isTrue(!TOPIC_EXECUTOR_MAPPING.containsKey(topic),
                              "Rocketmq transaction executor topic already exists: " + executor);
                TOPIC_EXECUTOR_MAPPING.put(topic, executor);
            });
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(@NonNull Message message) {
        RocketMQTransactionChecker<Object> checker = lookupTransactionChecker(message);
        return checker.check(message, checker.getPayload(message, this.rocketmqMessageConverter.getMessageConverter()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public RocketMQLocalTransactionState executeLocalTransaction(@NonNull Message message, Object arg) {
        try {
            if (arg instanceof Supplier) {
                return ((Supplier<RocketMQLocalTransactionState>) arg).get();
            }
            MessageConverter messageConverter = this.rocketmqMessageConverter.getMessageConverter();
            RocketMQTransactionExecutor<Object> executor = lookupTransactionExecutor(message);
            return executor.execute(message, executor.getPayload(message, messageConverter), arg);
        } catch (RuntimeException e) {
            // 将异常存入上下文
            EXCEPTION_CONTEXT.set(e);
            throw e;
        }
    }

    /**
     * 获取RocketMQ模版实例
     *
     * @return RocketMQ模版实例
     */
    public static RocketMQTemplate getRocketMQTemplate() {
        return Objects.requireNonNull(ROCKETMQ_TEMPLATE, "Rocketmq template has not been initialized");
    }

    /**
     * 将消息内容转换成消息对象
     *
     * @param payload 消息内容
     *
     * @return 消息对象实例
     */
    public static Message<?> payload2message(@NonNull Object payload) {
        String json = Objects.requireNonNull(JsonUtils.serialize(payload));
        return MessageBuilder.withPayload(json).build();
    }

    /**
     * 将消息转换成消息体，带Header
     *
     * @param payload
     * @param headers
     *
     * @return
     */
    public static Message<?> payload2message(@NonNull Object payload, Map<String, ?> headers) {
        String json = Objects.requireNonNull(JsonUtils.serialize(payload));
        return MessageBuilder.withPayload(json).copyHeaders(headers).build();
    }

    /**
     * 获取消息主题
     *
     * @param message 消息对象实例
     *
     * @return 消息主题
     */
    public static String getMessageTopic(@NonNull Message<?> message) {
        String topic = message.getHeaders().get(ROCKETMQ_TOPIC_HEADER, String.class);
        Assert.notNull(topic, "Rocketmq message topic not found: " + message);
        return topic;
    }

    /**
     * 根据消息查找本地事务检测器
     *
     * @param message 事务消息
     * @param <T>     消息内容类型
     *
     * @return 本地事务检测器实例
     */
    @SuppressWarnings("unchecked")
    public static <T> RocketMQTransactionChecker<T> lookupTransactionChecker(@NonNull Message<?> message) {
        // 真正回调checker时已将原始的topic分解为topic和tags，无需再拆分
        String topic = getMessageTopic(message);
        RocketMQTransactionChecker<?> executor = TOPIC_CHECKER_MAPPING.get(topic);
        Assert.notNull(executor, String.format("Rocketmq transaction checker with topic '%s' not found", topic));
        return (RocketMQTransactionChecker<T>) executor;
    }

    /**
     * 根据消息查找本地事务处理器
     *
     * @param message 事务消息
     * @param <T>     消息内容类型
     *
     * @return 本地事务处理器实例
     */
    @SuppressWarnings("unchecked")
    public static <T> RocketMQTransactionExecutor<T> lookupTransactionExecutor(@NonNull Message<?> message) {
        String topic = getMessageTopic(message);
        RocketMQTransactionExecutor<?> executor = TOPIC_EXECUTOR_MAPPING.get(topic);
        Assert.notNull(executor, String.format("Rocketmq transaction executor with topic '%s' not found", topic));
        return (RocketMQTransactionExecutor<T>) executor;
    }

    /**
     * 发送消息
     *
     * @param topic   消息主题
     * @param payload 消息内容
     */
    public static void send(@NonNull String topic, @NonNull Object payload) {
        topic = getSendTopic(topic);
        getRocketMQTemplate().send(topic, payload2message(payload));
    }

    /**
     * 当前事务提交后发送消息；若当前无事务，则立即发送。
     *
     * @param topic   消息主题
     * @param payload 消息内容
     */
    public static void sendAfterCommit(@NonNull String topic, @NonNull Object payload) {
        RocketMQTransactionSynchronizationUtils.executeAfterCommitOrNow(() -> send(topic, payload));
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param payload
     * @param headers
     */
    public static void send(@NonNull String topic, @NonNull Object payload, Map<String, ?> headers) {
        topic = getSendTopic(topic);
        getRocketMQTemplate().send(topic, payload2message(payload, headers));
    }

    /**
     * 当前事务提交后发送消息；若当前无事务，则立即发送。
     *
     * @param topic   消息主题
     * @param payload 消息内容
     * @param headers 消息头
     */
    public static void sendAfterCommit(@NonNull String topic, @NonNull Object payload, Map<String, ?> headers) {
        RocketMQTransactionSynchronizationUtils.executeAfterCommitOrNow(() -> send(topic, payload, headers));
    }

    /**
     * 发送消息
     *
     * @param topic   消息主题
     * @param message 消息内容
     */
    public static void send(@NonNull String topic, @NonNull Message<?> message) {
        topic = getSendTopic(topic);
        getRocketMQTemplate().send(topic, message);
    }

    /**
     * 当前事务提交后发送消息；若当前无事务，则立即发送。
     *
     * @param topic   消息主题
     * @param message 消息内容
     */
    public static void sendAfterCommit(@NonNull String topic, @NonNull Message<?> message) {
        RocketMQTransactionSynchronizationUtils.executeAfterCommitOrNow(() -> send(topic, message));
    }

    /**
     * 延迟消息发送
     *
     * @param topic      消息主题
     * @param payload    消息内容
     * @param delayLevel 延迟等级
     */
    public static void asyncSend(@NonNull String topic, @NonNull Object payload, @NonNull int delayLevel) {
        topic = getSendTopic(topic);
        getRocketMQTemplate().asyncSend(topic, payload2message(payload), null, 3000, delayLevel);
    }

    /**
     * 延迟消息发送，带回调处理
     *
     * @param topic
     * @param payload
     * @param delayLevel
     * @param sendCallback
     */
    public static void asyncSend(@NonNull String topic, @NonNull Object payload, @NonNull int delayLevel, SendCallback sendCallback) {
        topic = getSendTopic(topic);
        getRocketMQTemplate().asyncSend(topic, payload2message(payload), sendCallback, 3000, delayLevel);
    }

    /**
     * 延迟消息发送，带回调错误处理
     *
     * @param topic
     * @param payload
     * @param delayLevel
     * @param errorCallBack
     */
    public static void asyncSend(@NonNull String topic, @NonNull Object payload, @NonNull int delayLevel, Consumer<Object> errorCallBack) {
        topic = getSendTopic(topic);
        getRocketMQTemplate().asyncSend(topic, payload2message(payload), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable throwable) {
                errorCallBack.accept(payload);
            }
        }, 3000, delayLevel);
    }

    /**
     * 顺序消息发送
     *
     * @param topic
     * @param payload
     * @param hashKey
     */
    public static void syncSendOrderly(@NonNull String topic, @NonNull Object payload, String hashKey) {
        topic = getSendTopic(topic);
        getRocketMQTemplate().syncSendOrderly(topic, payload2message(payload), hashKey);
    }

    /**
     * @param topic
     * @param payload
     * @param hashKey
     * @param sendCallback
     */
    public static void asyncSendOrderly(@NonNull String topic, @NonNull Object payload, @NonNull String hashKey,
                                        SendCallback sendCallback
                                       ) {
        topic = getSendTopic(topic);
        getRocketMQTemplate().asyncSendOrderly(topic, payload2message(payload), hashKey, sendCallback);
    }

    /**
     * 异步发送顺序消息,带回调错误处理
     *
     * @param topic
     * @param payload
     * @param hashKey
     * @param errorCallBack
     */
    public static void asyncSendOrderly(@NonNull String topic, @NonNull Object payload, @NonNull String hashKey,
                                        Consumer<Object> errorCallBack
                                       ) {
        topic = getSendTopic(topic);
        getRocketMQTemplate().asyncSendOrderly(topic, payload2message(payload), hashKey, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable throwable) {
                errorCallBack.accept(payload);
            }
        }, 3000);
    }

    /**
     * 发送事务消息
     *
     * @param topic    消息主题
     * @param payload  消息内容
     * @param callback 事务消息 回调方法
     *
     * @return 事务消息发送结果
     */
    public static TransactionSendResult sendTransactionalMessage(@NonNull String topic, @NonNull Object payload,
                                                                 @NonNull Supplier<RocketMQLocalTransactionState> callback
                                                                ) {

        // 发送时原始的topic如果带有tags，要去掉tags进行checker的匹配检查，否则无法通过检查
        // 真正回调checker时已将原始的topic分解为topic和tags，无需再拆分，参考lookupTransactionChecker方法
        String topicWithoutTag = ApplicationContextHolder.getBean(RocketMQContextHolder.class).messageEnvironment.getMessageEnvironment() +
                                 topic.split(":")[0];
        Assert.isTrue(TOPIC_CHECKER_MAPPING.containsKey(topicWithoutTag),
                      String.format("Rocketmq transaction checker with topic '%s' not found", topic));

        RocketMQTemplate template = getRocketMQTemplate();
        Message<?> message = payload2message(payload);
        try {
            // 发送事务消息
            topic = getSendTopic(topic);
            TransactionSendResult result = template.sendMessageInTransaction(topic, message, callback);

            // 判断执行本地事务是否抛出了异常，如果存在异常则直接抛出
            RuntimeException exception = EXCEPTION_CONTEXT.get();
            if (exception != null) {
                throw exception;
            }
            return result;
        } finally {
            // 清空异常上下文
            EXCEPTION_CONTEXT.remove();
        }
    }
}
