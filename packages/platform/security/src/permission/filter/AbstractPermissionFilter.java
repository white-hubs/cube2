package com.guhecloud.platform.security.permission.filter;

import com.guhecloud.platform.security.permission.cache.PermissionCache;
import com.guhecloud.platform.security.permission.model.PrincipalMethodPermission;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public abstract class AbstractPermissionFilter implements PermissionFilterChain {

    @Autowired
    private PermissionCache permissionCache;

    public Set<PrincipalMethodPermission> getPermission() {
        return permissionCache.getPrincipalPermissions();
    }
}
