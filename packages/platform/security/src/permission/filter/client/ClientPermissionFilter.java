package com.guhecloud.platform.security.permission.filter.client;

import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.platform.security.permission.filter.AbstractPermissionFilter;
import com.guhecloud.platform.security.permission.filter.PermissionFilterChain;
import com.guhecloud.platform.security.permission.model.PrincipalMethodPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Set;

@Slf4j
@Component
public class ClientPermissionFilter extends AbstractPermissionFilter implements PermissionFilterChain {

    @Override
    public Principal.Type getPrincipalType() {
        return Principal.Type.CLIENT;
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void doFilter(Set<String> permissions) {
        // 从配置中获取客户端对应的资源标识列表
        Set<PrincipalMethodPermission> clientPermissions = getPermission();
        if (!CollectionUtils.isEmpty(clientPermissions)) {
            clientPermissions.stream().forEach(rolePermission -> permissions.addAll(rolePermission.getHasAuthorities()));
        }
    }
}
