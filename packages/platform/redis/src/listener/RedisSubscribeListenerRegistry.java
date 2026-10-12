package com.guhecloud.platform.redis.listener;

import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.redis.annotation.RedisSubscribe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * redis订阅监听
 *
 * @author lanchun
 * @date 2021/12/27 13:56
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisSubscribeListenerRegistry implements ApplicationRunner {

    private AtomicLong counter = new AtomicLong(0);

    private final ApplicationContext applicationContext;

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RedisMessageListenerContainer container = this.applicationContext.getBean(RedisMessageListenerContainer.class);
        // 加载@SpringBootApplication包扫描路径配置
        List<Class<?>> classes = ApplicationContextHolder.lookupApplicationClasses(this.applicationContext, clazz -> clazz != null
                                                                                                                     &&
                                                                                                                     !Modifier.isInterface(clazz.getModifiers()) &&
                                                                                                                     !Modifier.isAbstract(clazz.getModifiers()));
        for (Class classz : classes) {
            //从方法上获取注解
            ReflectionUtils.doWithMethods(Objects.requireNonNull(classz),
                                          method -> {
                                              String beanName = this.applicationContext.getBeanNamesForType(classz)[0];
                                              ReflectionUtils.makeAccessible(method);
                                              Object target = this.applicationContext.getBean(beanName);
                                              RedisSubscribe subscribe = AnnotationUtils.findAnnotation(method, RedisSubscribe.class);
                                              MessageListenerAdapter adapter = this.registerBean((GenericApplicationContext) applicationContext, target, method);
                                              container.addMessageListener(adapter, new PatternTopic(subscribe.topic()));
                                          },
                                          method ->
                                                  !method.isBridge() && !method.isSynthetic() && method.getParameterTypes().length == 1
                                                  && AnnotationUtils.findAnnotation(method, RedisSubscribe.class) != null);
        }
    }

    private MessageListenerAdapter registerBean(GenericApplicationContext context, Object target, Method method) {
        String containerBeanName = String.format("%s_%s", MessageListenerAdapter.class.getName(), this.counter.incrementAndGet());
        context.registerBean(containerBeanName, MessageListenerAdapter.class, () -> new MessageListenerAdapter(target, method.getName()));
        return context.getBean(containerBeanName, MessageListenerAdapter.class);
    }
}
