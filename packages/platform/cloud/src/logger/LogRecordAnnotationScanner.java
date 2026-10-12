package com.guhecloud.platform.cloud.logger;

import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class LogRecordAnnotationScanner implements BeanPostProcessor {
    private RedisTemplate redisTemplate; // 改为非静态字段

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Map<Method, LogRecord> annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                                                                                   (MethodIntrospector.MetadataLookup<LogRecord>) method -> AnnotationUtils.findAnnotation(method, LogRecord.class));

        for (Map.Entry<Method, LogRecord> entry : annotatedMethods.entrySet()) {
            //            allLogRecordAnnos.add(entry.getValue());
            LogRecord logRecord = entry.getValue();
            ListOperations<String, Object> listOps = redisTemplate.opsForList();
            listOps.rightPush("allLogRecordAnnos",
                              logRecord.type() + "-" + logRecord.typeName() + "-" + logRecord.subType() + "-" + logRecord.subTypeName());
        }
        return bean;
    }
}