package com.guhecloud.platform.security.permission.matcher;

import org.springframework.util.AntPathMatcher;

import java.util.Set;

public class DefaultPermissionMatcher implements PermissionMatcher {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean matches(Set<String> permissionSource, String matchPermission) {
        for (String pattern : permissionSource) {
            if (antPathMatcher.match(pattern, matchPermission)) {
                return true;
            }
        }
        return false;
    }
}
