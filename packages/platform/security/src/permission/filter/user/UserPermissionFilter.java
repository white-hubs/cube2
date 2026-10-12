package com.guhecloud.platform.security.permission.filter.user;

import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.platform.security.permission.filter.AbstractPermissionFilter;
import com.guhecloud.platform.security.permission.filter.PermissionFilterChain;
import com.guhecloud.platform.security.permission.model.PrincipalMethodPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Set;

/**
 * 用户角色权限过滤
 *
 * @author lanchun
 * @date 2024/5/28 10:57
 */
@Slf4j
@Component
public class UserPermissionFilter extends AbstractPermissionFilter implements PermissionFilterChain {

    @Override
    public Principal.Type getPrincipalType() {
        return Principal.Type.USER;
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void doFilter(Set<String> permissions) {
        if (SecurityContextHolder.isAdmin() || SecurityContextHolder.isTenantAdmin()) {
            permissions.add("**");
            return;
        }
        // 从配置中获取角色对应的权限标识列表
        Set<PrincipalMethodPermission> rolePermissions = getPermission();
        if (!CollectionUtils.isEmpty(rolePermissions)) {
            rolePermissions.stream().forEach(rolePermission -> permissions.addAll(rolePermission.getHasAuthorities()));
        }
    }
}
