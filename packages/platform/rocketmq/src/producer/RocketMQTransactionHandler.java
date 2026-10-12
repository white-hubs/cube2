package com.guhecloud.platform.rocketmq.producer;

import lombok.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConverter;

/**
 * RocketMQ事务处理接口
 *
 * @param <T> 消息内容类型
 *
 * @author Woody
 * @date 2021/9/6
 */
public interface RocketMQTransactionHandler<T> {
    /**
     * 获取消息主题
     *
     * @return 消息主题
     */
    String getTopic();

    /**
     * 获取消息内容对象
     *
     * @return 消息内容对象
     */
    Class<T> getPayloadClass();

    /**
     * 获取消息内容
     *
     * @param message   消息对象实例
     * @param converter 消息转换器实例
     *
     * @return 消息内容对象实例
     */
    @SuppressWarnings("unchecked")
    default T getPayload(@NonNull Message<?> message, @NonNull MessageConverter converter) {
        return (T) converter.fromMessage(message, this.getPayloadClass());
    }
}
