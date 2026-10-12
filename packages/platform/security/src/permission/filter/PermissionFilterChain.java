package com.guhecloud.platform.security.permission.filter;

import com.guhecloud.platform.security.model.Principal;

import java.util.Set;

public interface PermissionFilterChain {

    Principal.Type getPrincipalType();

    int order();

    void doFilter(Set<String> permissions);
}
