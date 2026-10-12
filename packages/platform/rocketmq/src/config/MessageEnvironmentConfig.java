package com.guhecloud.platform.rocketmq.config;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;

/**
 * 环境隔离时主题与组名称拼接；并统一注入进程内共享的 instanceName，
 * 避免每个 Listener 因 DEFAULT 触发 changeInstanceNameToPID 而独占 MQClientInstance。
 *
 * @author lanchun
 * @date 2023/4/3 17:14
 */
@Component
@Aspect
@Slf4j
public class MessageEnvironmentConfig implements BeanPostProcessor {

    private static final String DEFAULT_INSTANCE_NAME = "DEFAULT";

    @Autowired
    private MessageEnvironment messageEnvironment;

    /**
     * 实例化、依赖注入、初始化完毕时执行
     * 注意：方法返回值不能为null
     * 如果返回null那么在后续初始化方法将报空指针异常或者通过getBean()方法获取不到bena实例对象
     * 因为后置处理器从Spring IoC容器中取出bean实例对象没有再次放回IoC容器中
     *
     * @param bean
     * @param beanName
     *
     * @return
     *
     * @throws BeansException
     */
    @Override
    @SneakyThrows
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RocketMQListener || bean instanceof RocketMQReplyListener) {
            rewriteListenerAnnotation(bean, beanName);
        }
        if (bean instanceof DefaultMQProducer) {
            applySharedInstanceName((DefaultMQProducer) bean, beanName);
        }
        if (bean instanceof DefaultLitePullConsumer) {
            applySharedInstanceName((DefaultLitePullConsumer) bean, beanName);
        }
        return bean;
    }

    @SneakyThrows
    private void rewriteListenerAnnotation(Object bean, String beanName) {
        log.info("消息消费者beanName:{},beanClass:{}", beanName, bean.getClass().toString());
        RocketMQMessageListener rocketMQMessageListener = null;
        if (AopUtils.isCglibProxy(bean)) {
            var h = bean.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);
            var dynamicAdvisedInterceptor = h.get(bean);
            var advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            var target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
            if (target != null) {
                rocketMQMessageListener = target.getClass().getAnnotation(RocketMQMessageListener.class);
            }
        } else {
            rocketMQMessageListener = bean.getClass().getAnnotation(RocketMQMessageListener.class);
        }
        if (Objects.nonNull(rocketMQMessageListener)) {
            var h = Proxy.getInvocationHandler(rocketMQMessageListener);
            var hField = h.getClass().getDeclaredField("memberValues");
            hField.setAccessible(true);
            @SuppressWarnings("unchecked")
            var memberValues = (Map<String, Object>) hField.get(h);
            log.info("消息TOPIC与组切割,TOPIC信息:{}  ,组信息{}", memberValues.get("topic"), memberValues.get("consumerGroup"));
            // 读取环境
            memberValues.put("topic", messageEnvironment.getMessageEnvironment() + rocketMQMessageListener.topic());
            memberValues.put("consumerGroup",
                             messageEnvironment.getMessageEnvironment() + rocketMQMessageListener.consumerGroup());
            rewriteInstanceName(memberValues);
        }
    }

    private void rewriteInstanceName(Map<String, Object> memberValues) {
        Object current = memberValues.get("instanceName");
        if (current == null || StringUtils.isEmpty(String.valueOf(current))
            || DEFAULT_INSTANCE_NAME.equals(String.valueOf(current))) {
            String shared = messageEnvironment.getClientInstanceName();
            memberValues.put("instanceName", shared);
            log.info("RocketMQ listener instanceName unified to {}", shared);
        }
    }

    private void applySharedInstanceName(DefaultMQProducer producer, String beanName) {
        if (shouldOverrideInstanceName(producer.getInstanceName())) {
            String shared = messageEnvironment.getClientInstanceName();
            producer.setInstanceName(shared);
            log.info("RocketMQ producer[{}] instanceName unified to {}", beanName, shared);
        }
    }

    private void applySharedInstanceName(DefaultLitePullConsumer consumer, String beanName) {
        if (shouldOverrideInstanceName(consumer.getInstanceName())) {
            String shared = messageEnvironment.getClientInstanceName();
            consumer.setInstanceName(shared);
            log.info("RocketMQ pullConsumer[{}] instanceName unified to {}", beanName, shared);
        }
    }

    private boolean shouldOverrideInstanceName(String instanceName) {
        return StringUtils.isEmpty(instanceName) || DEFAULT_INSTANCE_NAME.equals(instanceName);
    }
}
