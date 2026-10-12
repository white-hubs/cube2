package com.guhecloud.platform.event.src.config;

import com.guhecloud.platform.event.src.EventPublisher;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.entity.EventIdentity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Factory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
public class EventBusPostProcessor implements BeanPostProcessor, ApplicationContextAware, SmartInitializingSingleton {

    private EventPublisher eventPublisher;
    private final List<EventBusListenBean> eventBusListenBeans = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        var eventBusListen = findAnnotation(bean.getClass(), EventBusListen.class);
        if (Objects.isNull(eventBusListen)) {
            return bean;
        }
        eventBusListenBeans.add(new EventBusListenBean(bean, beanName, eventBusListen));
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.eventPublisher = applicationContext.getBean(EventPublisher.class);
    }

    @Override
    public void afterSingletonsInstantiated() {
        if (eventBusListenBeans.isEmpty()) {
            log.warn("event bus subscriber is empty");
        }
        // 按 order 升序排序订阅者
        eventBusListenBeans.sort(Comparator.comparingInt(s -> s.eventBusListen.order()));
        for (EventBusListenBean listenBean : eventBusListenBeans) {
            var eventBusListen = listenBean.eventBusListen;
            var identity = new EventIdentity(eventBusListen.key(), eventBusListen.publisherType());
            //获取实际的bean
            Object bean = getBean(listenBean.bean);
            this.eventPublisher.register(new EventIdentity(eventBusListen.key(), eventBusListen.publisherType()), bean);
            log.info("event bus register success,  {},bean:{}", identity, bean);
        }
    }

    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationClass) {
        if (clazz == null || clazz == Object.class) {
            return null;
        }

        var annotation = clazz.getAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }

        var superClassAnnotation = findAnnotation(clazz.getSuperclass(), annotationClass);
        if (superClassAnnotation != null) {
            return superClassAnnotation;
        }

        for (Class<?> ifc : clazz.getInterfaces()) {
            var interfaceAnnotation = findAnnotation(ifc, annotationClass);
            if (interfaceAnnotation != null) {
                return interfaceAnnotation;
            }
        }
        return null;
    }

    private record EventBusListenBean(Object bean, String beanName, EventBusListen eventBusListen) {}

    private Object getBean(Object bean) {
        if (AopUtils.isCglibProxy(bean)) {
            Factory factory = (Factory) bean;
            Object callback = factory.getCallback(0);
            try {
                Field advised = callback.getClass().getDeclaredField("advised");
                advised.setAccessible(true);
                return ((AdvisedSupport) advised.get(callback)).getTargetSource().getTarget();
            } catch (Exception e) {
                log.error("not find target bean:{}", bean, e);
            }
        }
        return bean;
    }
}
