package com.guhecloud.platform.rocketmq.producer;

import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

/**
 * RocketMQ本地事务执行接口
 *
 * @param <T> 消息内容类型
 *
 * @author Woody
 * @date 2021/9/6
 */
public interface RocketMQTransactionExecutor<T> extends RocketMQTransactionHandler<T> {
    /**
     * 执行本地事务
     *
     * @param message 事务消息
     * @param payload 消息内容
     * @param context 事务消息上下文
     *
     * @return 本地事务状态
     */
    RocketMQLocalTransactionState execute(Message<?> message, T payload, Object context);
}
