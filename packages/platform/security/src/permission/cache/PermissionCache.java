package com.guhecloud.platform.security.permission.cache;

import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.platform.security.permission.model.Permission;
import com.guhecloud.platform.security.permission.model.PrincipalMethodPermission;

import java.util.Set;

public interface PermissionCache {

    void setPrincipalPermission(String principalId, Principal.Type principalType, Set<Permission> permissions);

    Set<PrincipalMethodPermission> getPrincipalPermissions();

    Set<PrincipalMethodPermission> getPrincipalPermissions(String permissionType);

    String getPrincipalKey();
}
