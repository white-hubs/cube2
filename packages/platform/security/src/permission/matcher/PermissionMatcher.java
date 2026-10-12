package com.guhecloud.platform.security.permission.matcher;

import java.util.Set;

public interface PermissionMatcher {

    /**
     * Decides whether the rule implemented by the strategy matches the supplied permission.
     *
     * @param permissionSource
     * @param matchPermission
     *
     * @return
     */
    boolean matches(Set<String> permissionSource, String matchPermission);
}
