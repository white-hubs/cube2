package com.guhecloud.platform.base.context;

import com.guhecloud.platform.base.util.SpringObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 应用上下文工具类
 *
 * @author Woody
 * @date 2021/5/27
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    /**
     * 应用名称属性
     */
    public static final String APPLICATION_NAME_PROPERTY = "spring.application.name";

    /**
     * 应用上下文对象
     */
    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 获取应用上下文对象
     *
     * @return 应用上下文对象
     */
    public static ApplicationContext getApplicationContext() {
        return Objects.requireNonNull(APPLICATION_CONTEXT, "Application context has not been initialized");
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 获取对象实例
     *
     * @param type 对象类型
     * @param <T>  对象类型泛型
     *
     * @return 对象实例
     */
    public static <T> T getBean(Class<T> type) {
        return getBean(type, false);
    }

    /**
     * 获取对象实例
     *
     * @param beanName 对象名称
     * @param type     对象类型
     * @param <T>      对象类型泛型
     *
     * @return 对象实例
     */
    public static <T> T getBeanByName(String beanName, Class<T> type) {
        return getApplicationContext().getBean(beanName, type);
    }

    /**
     * 获取可以为空的对象实例
     *
     * @param type     对象类型
     * @param nullable 对象实例是否可以为空
     * @param <T>      对象类型泛型
     *
     * @return 对象实例
     */
    public static <T> T getBean(Class<T> type, boolean nullable) {
        try {
            return getApplicationContext().getBean(type);
        } catch (NoSuchBeanDefinitionException e) {
            if (nullable) {
                return null;
            }
            throw e;
        }
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        try {
            return getApplicationContext().getBeansOfType(type);
        } catch (NoSuchBeanDefinitionException e) {
            throw e;
        }
    }

    /**
     * 获取应用名称
     *
     * @return 应用名称
     */
    public static String getApplicationName() {
        return getApplicationContext().getEnvironment().getProperty(APPLICATION_NAME_PROPERTY);
    }

    /**
     * 查找应用中的类对象
     *
     * @param condition 类对象条件判断
     *
     * @return 类对象列表
     */
    public static List<Class<?>> lookupApplicationClasses(@NonNull Predicate<Class<?>> condition) {
        return lookupApplicationClasses(getApplicationContext(), condition);
    }

    /**
     * 查找应用中的类对象
     *
     * @param applicationContext 应用上下文对象
     * @param condition          类对象条件判断
     *
     * @return 类对象列表
     */
    public static List<Class<?>> lookupApplicationClasses(@NonNull ApplicationContext applicationContext,
                                                          @NonNull Predicate<Class<?>> condition
                                                         ) {
        Set<String> packages = new HashSet<>();
        applicationContext.getBeansWithAnnotation(SpringBootApplication.class).forEach((key, value) -> {
            Class<?> clazz = value.getClass();

            // 加载@ComponentScan包扫描路径配置
            ComponentScan componentScan = AnnotationUtils.findAnnotation(clazz, ComponentScan.class);
            if (componentScan != null) {
                packages.addAll(Arrays.asList(componentScan.basePackages()));
            }

            // 加载@SpringBootApplication包扫描路径配置
            SpringBootApplication springBootApplication =
                    AnnotationUtils.findAnnotation(clazz, SpringBootApplication.class);
            if (springBootApplication != null) {
                packages.addAll(Arrays.asList(springBootApplication.scanBasePackages()));
            }
        });

        // 根据包扫描路径查找类对象
        if (packages.isEmpty()) {
            return SpringObjectUtils.lookupClasses(StringUtils.EMPTY_STRING, condition);
        }
        return packages.stream().flatMap(pack -> SpringObjectUtils.lookupClasses(pack, condition).stream())
                .distinct().collect(Collectors.toList());
    }
}
