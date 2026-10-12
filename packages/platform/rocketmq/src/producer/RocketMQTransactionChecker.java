package com.guhecloud.platform.rocketmq.producer;

import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

/**
 * RocketMQ本地事务检测接口
 *
 * @param <T> 消息内容类型
 *
 * @author Woody
 * @date 2021/9/6
 */
public interface RocketMQTransactionChecker<T> extends RocketMQTransactionHandler<T> {
    /**
     * 检测本地事务执行情况
     *
     * @param message 事务消息
     * @param payload 消息内容
     *
     * @return 本地事务状态
     */
    RocketMQLocalTransactionState check(Message<?> message, T payload);
}
