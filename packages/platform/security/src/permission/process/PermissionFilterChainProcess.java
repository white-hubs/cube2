package com.guhecloud.platform.security.permission.process;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.platform.security.permission.filter.PermissionFilterChain;
import com.guhecloud.platform.security.permission.matcher.DefaultPermissionMatcher;
import com.guhecloud.platform.security.permission.matcher.PermissionMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class PermissionFilterChainProcess extends AbstractPermissionFilterChainProcess implements ApplicationContextAware {

    private PermissionMatcher permissionMatcher = new DefaultPermissionMatcher();

    // 使用Map存储不同类型用户的权限过滤器链
    private final EnumMap<Principal.Type, List<PermissionFilterChain>> principalPermissionFilters = new EnumMap<>(Principal.Type.class);

    @Override
    protected boolean preCheck() {
        return SecurityContextHolder.isInnerServer();
    }

    @Override
    public boolean postProcess(String permission) {
        List<PermissionFilterChain> permissionFilterChains = principalPermissionFilters.get(SecurityContextHolder.getPrincipal().getType());
        if (CollectionUtils.isEmpty(permissionFilterChains)) {
            return true;
        }
        Set<String> permissions = new HashSet<>();
        for (PermissionFilterChain permissionFilterChain : permissionFilterChains) {
            permissionFilterChain.doFilter(permissions);
        }
        if (CollectionUtils.isEmpty(permissions)) {
            return false;
        }
        return permissionMatcher.matches(permissions, permission);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (this.principalPermissionFilters.isEmpty()) {
            Map<String, PermissionFilterChain> permissionFilterChains = applicationContext.getBeansOfType(PermissionFilterChain.class);
            permissionFilterChains.values().forEach(permissionFilterChain -> {
                Principal.Type principalType = permissionFilterChain.getPrincipalType();
                this.principalPermissionFilters.computeIfAbsent(principalType, k -> new ArrayList<>()).add(permissionFilterChain);
            });
        }
    }
}
