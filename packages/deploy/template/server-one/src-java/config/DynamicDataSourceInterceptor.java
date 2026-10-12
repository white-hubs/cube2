package com.guhecloud.cube.servone.config;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.override.MybatisMapperProxy;
import com.google.common.collect.Maps;
import com.guhecloud.platform.base.util.RegexUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 注意设置Order，保证自定义切面在事务切面前执行，确保先设置数据源key
 *
 * @author Administrator
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(DynamicDataSourceContextHolder.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DynamicDataSourceInterceptor {

    private final DynamicDataSourceProperties dynamicDataSourceProperties;

    private Map<String, String> dataSourceContextMappings;

    @PostConstruct
    public void initialize() {
        Set<String> keys = this.dynamicDataSourceProperties.getDatasource().keySet();
        this.dataSourceContextMappings = keys.isEmpty() ? Collections.emptyMap() : Maps.newHashMapWithExpectedSize(keys.size());
        keys.forEach(value -> {
            for (String key : RegexUtils.splitActives(value)) {
                this.dataSourceContextMappings.put(key, value);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    private String lookupDataSourceKey(ProceedingJoinPoint point) {

        // 代理对象
        Object target = point.getTarget();

        // 普通代理方式获取代理对象类名
        String targetClassName = AopProxyUtils.ultimateTargetClass(target).getName();
        String key = this.dataSourceContextMappings.entrySet().stream().filter(entry -> targetClassName.startsWith(entry.getKey()))
                .findAny().map(Map.Entry::getValue).orElse(null);
        // 未匹配到数据源，targetClassName 可能是特殊的代理对象
        if (StringUtils.isEmpty(key)) {
            // MybatisMapperProxyFactory 以 Proxy.newProxyInstance() 方式创建的代理
            try {
                if (target instanceof Proxy) {
                    InvocationHandler h = Proxy.getInvocationHandler(target);
                    if (h instanceof MybatisMapperProxy) {
                        Field filed = h.getClass().getDeclaredField("mapperInterface");
                        filed.setAccessible(true);
                        Class targetClass = (Class) filed.get(h);
                        String mapperClassName = targetClass.getName();
                        key = this.dataSourceContextMappings.entrySet().stream().filter(entry -> mapperClassName.startsWith(entry.getKey()))
                                .findAny().map(Map.Entry::getValue).orElse(null);
                    }
                }
            } catch (Exception e) {
                // swallow
            }
        }

        return key;
    }

    @Around("execution(* com.guhecloud.cube..commandgateway..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..dao..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..db.mapper..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..infrastruceture..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..application..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..querygateway..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..service..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..interfaces..*.*(..))||"
            + " execution(* com.guhecloud.cube..business..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..controller..*.*(..))||"
            + " execution(* com.guhecloud.cube..handler..*.*(..))  ||"
            + " execution(* com.guhecloud.cube..mapper..*.*(..))||"
            + " execution(* com.guhecloud.cube..consumer..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..rocketmq..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..migration..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..core..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..job..*.*(..)) ||"
            + " execution(* com.guhecloud.cube..infrastructure..*.*(..))"
    )
    public Object routing(ProceedingJoinPoint point) throws Throwable {
        String key = this.lookupDataSourceKey(point);
        if (!StringUtils.isEmpty(key)) {
            DynamicDataSourceContextHolder.push(key);
        }
        try {
            return point.proceed(point.getArgs());
        } finally {
            if (!StringUtils.isEmpty(key)) {
                DynamicDataSourceContextHolder.poll();
            }
        }
    }

}
