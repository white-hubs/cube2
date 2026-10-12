package com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.producer.wapper;

import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.exception.RequestTimeoutException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.RequestCallback;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 光明消息生产者Warp
 *
 * @author lanchun
 * @date 2023/8/17 16:05
 */
public class GmMeattradeProducerWarp extends ClientConfig implements MQProducer {

    private final DefaultMQProducer defaultMQProducer;

    private final String topic;

    private final String appKey;

    public GmMeattradeProducerWarp(DefaultMQProducer defaultMQProducer, String topic, String appKey) {
        this.defaultMQProducer = defaultMQProducer;
        this.topic = topic;
        this.appKey = appKey;
    }

    @Override
    public void start() throws MQClientException {
        this.defaultMQProducer.start();
    }

    @Override
    public void shutdown() {
        this.defaultMQProducer.shutdown();
    }

    @Override
    public List<MessageQueue> fetchPublishMessageQueues(String topic) throws MQClientException {
        return this.defaultMQProducer.fetchPublishMessageQueues(topic);
    }

    @Override
    public SendResult send(Message msg) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.send(msg);
    }

    @Override
    public SendResult send(Message msg, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.send(msg, timeout);
    }

    @Override
    public void send(Message msg, SendCallback sendCallback) throws MQClientException, RemotingException, InterruptedException {
        this.defaultMQProducer.send(msg, sendCallback);
    }

    @Override
    public void send(Message msg, SendCallback sendCallback,
                     long timeout
                    ) throws MQClientException, RemotingException, InterruptedException {
        this.defaultMQProducer.send(msg, sendCallback, timeout);
    }

    @Override
    public void sendOneway(Message msg) throws MQClientException, RemotingException, InterruptedException {
        this.defaultMQProducer.sendOneway(msg);
    }

    @Override
    public SendResult send(Message msg,
                           MessageQueue mq
                          ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.send(msg, mq);
    }

    @Override
    public SendResult send(Message msg, MessageQueue mq,
                           long timeout
                          ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.send(msg, mq, timeout);
    }

    @Override
    public void send(Message msg, MessageQueue mq,
                     SendCallback sendCallback
                    ) throws MQClientException, RemotingException, InterruptedException {
        this.defaultMQProducer.send(msg, mq, sendCallback);
    }

    @Override
    public void send(Message msg, MessageQueue mq, SendCallback sendCallback,
                     long timeout
                    ) throws MQClientException, RemotingException, InterruptedException {
        this.defaultMQProducer.send(msg, mq, sendCallback, timeout);
    }

    @Override
    public void sendOneway(Message msg, MessageQueue mq) throws MQClientException, RemotingException, InterruptedException {
        this.defaultMQProducer.sendOneway(msg, mq);
    }

    @Override
    public SendResult send(Message msg, MessageQueueSelector selector,
                           Object arg
                          ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.send(msg, selector, arg);
    }

    @Override
    public SendResult send(Message msg, MessageQueueSelector selector, Object arg,
                           long timeout
                          ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.send(msg, selector, arg, timeout);
    }

    @Override
    public void send(Message msg, MessageQueueSelector selector, Object arg,
                     SendCallback sendCallback
                    ) throws MQClientException, RemotingException, InterruptedException {
        this.defaultMQProducer.send(msg, selector, arg, sendCallback);
    }

    @Override
    public void send(Message msg, MessageQueueSelector selector, Object arg, SendCallback sendCallback,
                     long timeout
                    ) throws MQClientException, RemotingException, InterruptedException {
        this.defaultMQProducer.send(msg, selector, arg, sendCallback, timeout);
    }

    @Override
    public void sendOneway(Message msg, MessageQueueSelector selector,
                           Object arg
                          ) throws MQClientException, RemotingException, InterruptedException {
        this.defaultMQProducer.sendOneway(msg, selector, arg);
    }

    @Override
    public TransactionSendResult sendMessageInTransaction(Message msg, Object arg) throws MQClientException {
        return this.defaultMQProducer.sendMessageInTransaction(msg, arg);
    }

    @Override
    public SendResult send(Collection<Message> msgs) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.send(msgs);
    }

    @Override
    public SendResult send(Collection<Message> msgs,
                           long timeout
                          ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.send(msgs, timeout);
    }

    @Override
    public SendResult send(Collection<Message> msgs,
                           MessageQueue mq
                          ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.send(msgs, mq);
    }

    @Override
    public SendResult send(Collection<Message> msgs, MessageQueue mq,
                           long timeout
                          ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.send(msgs, mq, timeout);
    }

    @Override
    public void send(Collection<Message> msgs,
                     SendCallback sendCallback
                    ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        this.defaultMQProducer.send(msgs, sendCallback);
    }

    @Override
    public void send(Collection<Message> msgs, SendCallback sendCallback,
                     long timeout
                    ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        this.defaultMQProducer.send(msgs, sendCallback, timeout);
    }

    @Override
    public void send(Collection<Message> msgs, MessageQueue mq,
                     SendCallback sendCallback
                    ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        this.defaultMQProducer.send(msgs, mq, sendCallback);
    }

    @Override
    public void send(Collection<Message> msgs, MessageQueue mq, SendCallback sendCallback,
                     long timeout
                    ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        this.defaultMQProducer.send(msgs, mq, sendCallback, timeout);
    }

    @Override
    public String recallMessage(String topic,
                                String recallHandle
                               ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.recallMessage(topic, recallHandle);
    }

    @Override
    public Message request(Message msg,
                           long timeout
                          ) throws RequestTimeoutException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.request(msg, timeout);
    }

    @Override
    public void request(Message msg, RequestCallback requestCallback,
                        long timeout
                       ) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        this.defaultMQProducer.request(msg, requestCallback, timeout);
    }

    @Override
    public Message request(Message msg, MessageQueueSelector selector, Object arg,
                           long timeout
                          ) throws RequestTimeoutException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.request(msg, selector, arg, timeout);
    }

    @Override
    public void request(Message msg, MessageQueueSelector selector, Object arg, RequestCallback requestCallback,
                        long timeout
                       ) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        this.defaultMQProducer.request(msg, selector, arg, requestCallback, timeout);
    }

    @Override
    public Message request(Message msg, MessageQueue mq,
                           long timeout
                          ) throws RequestTimeoutException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
        return this.defaultMQProducer.request(msg, mq, timeout);
    }

    @Override
    public void request(Message msg, MessageQueue mq, RequestCallback requestCallback,
                        long timeout
                       ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        this.defaultMQProducer.request(msg, mq, requestCallback, timeout);
    }

    @Override
    public void createTopic(String key, String newTopic, int queueNum, Map<String, String> attributes) throws MQClientException {
        this.defaultMQProducer.createTopic(key, newTopic, queueNum, attributes);
    }

    @Override
    public void createTopic(String key, String newTopic, int queueNum, int topicSysFlag,
                            Map<String, String> attributes
                           ) throws MQClientException {
        this.defaultMQProducer.createTopic(key, newTopic, queueNum, topicSysFlag, attributes);
    }

    @Override
    public long searchOffset(MessageQueue mq, long timestamp) throws MQClientException {
        return this.defaultMQProducer.searchOffset(mq, timestamp);
    }

    @Override
    public long maxOffset(MessageQueue mq) throws MQClientException {
        return this.defaultMQProducer.maxOffset(mq);
    }

    @Override
    public long minOffset(MessageQueue mq) throws MQClientException {
        return this.defaultMQProducer.minOffset(mq);
    }

    @Override
    public long earliestMsgStoreTime(MessageQueue mq) throws MQClientException {
        return this.defaultMQProducer.earliestMsgStoreTime(mq);
    }

    @Override
    public QueryResult queryMessage(String topic, String key, int maxNum, long begin,
                                    long end
                                   ) throws MQClientException, InterruptedException {
        return this.defaultMQProducer.queryMessage(topic, key, maxNum, begin, end);
    }

    @Override
    public MessageExt viewMessage(String topic,
                                  String msgId
                                 ) throws RemotingException, MQBrokerException, InterruptedException, MQClientException {
        return this.defaultMQProducer.viewMessage(topic, msgId);
    }

    public DefaultMQProducer getDefaultMQProducer() {
        return defaultMQProducer;
    }

    public String getTopic() {
        return topic;
    }

    public String getAppKey() {
        return appKey;
    }
}
