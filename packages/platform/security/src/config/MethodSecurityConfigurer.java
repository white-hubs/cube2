package com.guhecloud.platform.security.config;

import com.guhecloud.platform.security.permission.process.PermissionFilterChainProcess;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfigurer {

    private final PermissionFilterChainProcess permissionFilterChainProcess;

    @Bean
    public MethodSecurityExpressionHandler createExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler() {
            @Override
            public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication,
                                                             MethodInvocation invocation
                                                            ) {
                StandardEvaluationContext context = (StandardEvaluationContext) super.createEvaluationContext(authentication, invocation);
                MethodSecurityExpressionOperations root = createSecurityExpressionRoot(authentication.get(), invocation);
                context.setRootObject(root);
                return context;
            }

            @Override
            protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
                                                                                      MethodInvocation invocation
                                                                                     ) {
                MethodSecurityExpressionWrapper root = new MethodSecurityExpressionWrapper(authentication, permissionFilterChainProcess);
                root.setThis(invocation.getThis());
                root.setPermissionEvaluator(getPermissionEvaluator());
                root.setTrustResolver(getTrustResolver());
                root.setRoleHierarchy(getRoleHierarchy());
                root.setDefaultRolePrefix(getDefaultRolePrefix());
                return root;
            }
        };
    }
}
