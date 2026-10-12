package com.guhecloud.platform.base.config;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;

/**
 * 对象复制处理器配置
 *
 * @author Woody
 * @date 2021/7/12
 */
@Slf4j
@Configuration
public class ObjectCopierConfigurer implements ApplicationContextAware {
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        MapperFactory factory = ObjectUtils.getCopierBuilder();
        Collection<MapperFactoryConfigurer> configurers =
                applicationContext.getBeansOfType(MapperFactoryConfigurer.class).values();
        if (!CollectionUtils.isEmpty(configurers)) {
            log.info("Loading object copier configurers: {}", configurers);
            configurers.forEach(configurer -> configurer.configure(factory));
        }
    }

    /**
     * 对象映射工厂配置接口
     */
    public interface MapperFactoryConfigurer {
        /**
         * 对象映射工厂配置
         *
         * @param factory 对象映射工厂实例
         */
        void configure(MapperFactory factory);
    }
}
