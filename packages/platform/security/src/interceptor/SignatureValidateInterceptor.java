package com.guhecloud.platform.security.interceptor;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.annotation.Signable;
import com.guhecloud.platform.security.signature.Signer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.StringUtils;

import jakarta.annotation.PostConstruct;

import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 方法签名验证拦截器
 *
 * @author Woody
 * @date 2021/8/30
 */
@Slf4j
@Aspect
@Component
@ConditionalOnBean(Signer.class)
@ConditionalOnProperty(prefix = "platform.security.signature", name = "enable", matchIfMissing = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SignatureValidateInterceptor {
    private final Signer signer;

    @PostConstruct
    public void initialize() {
        log.info("Loading signer: {}", this.signer);
    }

    /**
     * 获取签名排除参数名称集合
     *
     * @param signables 签名注解实例数组
     *
     * @return 参数名称集合
     */
    private Set<String> getSignableExcludes(Signable... signables) {
        for (Signable signable : signables) {
            if (signable.exclude().length > 0) {
                return Sets.newHashSet(signable.exclude());
            }
        }
        return Collections.emptySet();
    }

    /**
     * 接口签名验证，默认对所有接口参数签名
     *
     * @param point    接口签名拦截点
     * @param signable 接口签名方法注解
     *
     * @throws Throwable 接口调用异常
     */
    @Before("@annotation(signable)")
    public void validate(JoinPoint point, Signable signable) throws Throwable {
        // 获取接口签名信息
        String signature = SessionContextHolder.getRequester().getSignature();
        if (StringUtils.isEmpty(signature)) {
            throw new AccessDeniedException("Signature unspecified");
        }

        // 初始化签名参数
        Object[] args = point.getArgs();
        Parameter[] parameters = ((MethodSignature) point.getSignature()).getMethod().getParameters();
        Map<String, Object> signatures = args.length == 0 ? Collections.emptyMap() : Maps.newTreeMap(String::compareTo);
        for (int i = 0; i < parameters.length; i++) {
            Object arg = args[i];
            Parameter parameter = parameters[i];
            Signable parameterSignable = parameter.getAnnotation(Signable.class);
            if (arg == null || parameterSignable == null) {
                continue;
            }
            Set<String> excludes = getSignableExcludes(parameterSignable, signable);
            Class<?> clazz = arg.getClass();
            ObjectUtils.iterateClassFields(clazz, (field, index) -> {
                String name = field.getName();
                if (!excludes.contains(name)) {
                    signatures.put(name, ObjectUtils.getObjectProperty(arg, field));
                }
            });
        }

        // 验证签名
        if (!this.signer.validate(signature, signatures)) {
            throw new AccessDeniedException("Signature invalid");
        }
    }
}
