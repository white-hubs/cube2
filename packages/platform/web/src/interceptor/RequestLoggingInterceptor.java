package com.guhecloud.platform.base.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 请求日志拦截器
 *
 * @author Woody
 * @date 2021/8/30
 */
@Slf4j
@Component
public class RequestLoggingInterceptor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (log.isDebugEnabled()) {
            registry.registerBeanDefinition("requestLoggingAspect", new RootBeanDefinition(RequestLoggingAspect.class));
        }
    }

    /**
     * 请求日志拦截切面
     */
    @Aspect
    public static class RequestLoggingAspect {
        /**
         * 判断是否可记录日志
         *
         * @param point 接口方法拦截点
         *
         * @return true/false
         */
        private boolean isRecordable(JoinPoint point) {
            Object target = point.getTarget();
            if (target instanceof ErrorController) {
                return false;
            }
            Signature signature = point.getSignature();
            return signature instanceof MethodSignature
                   && !((MethodSignature) signature).getMethod().isAnnotationPresent(ExceptionHandler.class);
        }

        /**
         * 接口请求日志打印
         *
         * @param point 接口方法拦截点
         *
         * @throws Throwable 接口调用异常
         */
        @Around("@within(org.springframework.stereotype.Controller) " +
                "|| @within(org.springframework.web.bind.annotation.RestController)"
        )
        public Object logging(ProceedingJoinPoint point) throws Throwable {
            Object[] args = point.getArgs();
            boolean recordable = this.isRecordable(point);
            if (recordable) {
                log.debug("Method: {}", point.getSignature().toShortString());
                if (args.length == 0) {
                    log.debug("Request: ");
                } else if (args.length == 1) {
                    log.debug("Request: {}", args[0]);
                } else {
                    log.debug("Request: {}", args);
                }
            }
            Object value = point.proceed(args);
            if (recordable) {
                log.debug("Response: {}", value);
            }
            return value;
        }
    }
}
