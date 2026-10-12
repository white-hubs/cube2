package com.guhecloud.platform.event.src;

import com.guhecloud.platform.event.src.entity.EventIdentity;

public interface EventPublisher {

    /**
     * 注册订阅
     *
     * @param identity
     * @param subscriber 订阅信息
     */
    void register(EventIdentity identity, Object subscriber);

    /**
     * 取消注册订阅者
     *
     * @param identity
     * @param subscriber
     */
    void unregister(EventIdentity identity, Object subscriber);

    /**
     * 发布事件（触发所有订阅该事件类型的处理方法）
     *
     * @param identity
     * @param event
     */
    void publish(EventIdentity identity, Object event);

    /**
     * 事务发布事件（触发所有订阅该事件类型的处理方法）
     *
     * @param identity
     * @param event
     */
    void transactionPublish(EventIdentity identity, Object event);
}
