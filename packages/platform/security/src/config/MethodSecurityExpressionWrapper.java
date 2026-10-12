package com.guhecloud.platform.security.config;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.permission.process.PermissionFilterChainProcess;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;

import java.util.stream.Stream;

/**
 * 方法权限表达式处理包装类，自定义hasAuthority()、hasAnyAuthority()的处理逻辑
 *
 * @author Woody
 * @date 2021/7/6
 * @see MethodSecurityConfigurer
 */
public class MethodSecurityExpressionWrapper implements MethodSecurityExpressionOperations {

    private final SecurityExpressionRoot root;
    private final PermissionFilterChainProcess filterChain;

    private Object filterObject;
    private Object returnObject;
    private Object target;

    public MethodSecurityExpressionWrapper(Authentication authentication, PermissionFilterChainProcess filterChain) {
        this.root = new SecurityExpressionRoot(authentication) {
        };
        this.filterChain = filterChain;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    public void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Authentication getAuthentication() {
        return this.root.getAuthentication();
    }

    /**
     * 判断当前请求是否来自指定主机
     *
     * @param host 客户端主机地址
     *
     * @return true/false
     */
    public boolean isHost(String host) {
        return SessionContextHolder.isHost(host);
    }

    /**
     * 判断当前请求是否来自指定域名
     *
     * @param domain 服务端域名
     *
     * @return true/false
     */
    public boolean isDomain(String domain) {
        return SessionContextHolder.isDomain(domain);
    }

    /**
     * 判断当前认证主体是否为超级管理员
     *
     * @return true/false
     */
    public boolean isAdmin() {
        return SecurityContextHolder.isAdmin();
    }

    /**
     * 判断当前认证主体是否为租户管理员
     *
     * @return true/false
     */
    public boolean isTenantAdmin() {
        return SecurityContextHolder.isTenantAdmin();
    }

    /**
     * 判断当前认证主体是否为用户
     *
     * @return true/false
     */
    public boolean isUser() {
        return SecurityContextHolder.isUser();
    }

    /**
     * 判断当前认证主体是否为指定用户
     *
     * @param username 登录账号
     *
     * @return true/false
     */
    public boolean isUser(String username) {
        return SecurityContextHolder.isUser(username);
    }

    /**
     * 判断当前认证主体是否为客户端
     *
     * @return true/false
     */
    public boolean isClient() {
        return SecurityContextHolder.isClient();
    }

    /**
     * 判断客户端编号是否为指定编号
     *
     * @param code 客户端编号
     *
     * @return true/false
     */
    public boolean isClient(String code) {
        return SecurityContextHolder.isClient(code);
    }

    /**
     * 判断当前登录账号是否为指定登录账号
     *
     * @param account 登录账号
     *
     * @return true/false
     */
    public boolean isAccount(String account) {
        return SecurityContextHolder.isAccount(account);
    }

    /**
     * 判断当前请求是否为指定租户
     *
     * @param tenant 租户编号
     *
     * @return true/false
     */
    public boolean isTenant(Integer tenant) {
        return SessionContextHolder.isTenant(tenant);
    }

    /**
     * 判断当前请求是否为指定访问端
     *
     * @param terminal 访问端编号
     *
     * @return true/false
     */
    public boolean isTerminal(String terminal) {
        return SessionContextHolder.isTerminal(terminal);
    }

    /**
     * 判断当前请求是否为指定版本号
     *
     * @param version 客户端版本号
     *
     * @return true/false
     */
    public boolean isVersion(String version) {
        return SessionContextHolder.isVersion(version);
    }

    public boolean hasAuthority(String authority) {
        return filterChain.doProcess(authority);
    }

    @Override
    public boolean hasAnyAuthority(String... authorities) {
        return authorities != null && authorities.length > 0
               && Stream.of(authorities).anyMatch(filterChain::doProcess);
    }

    public boolean hasScope(String scope) {
        return SecurityContextHolder.hasScope(scope);
    }

    public boolean hasAnyScopes(String... scopes) {
        return scopes != null && scopes.length > 0
               && Stream.of(scopes).anyMatch(SecurityContextHolder::hasScope);
    }

    @Override
    public boolean hasRole(String role) {
        return SecurityContextHolder.isAdmin() || this.root.hasRole(role);
    }

    @Override
    public boolean hasAnyRole(String... roles) {
        return SecurityContextHolder.isAdmin() || this.root.hasAnyRole(roles);
    }

    @Override
    public boolean permitAll() {
        return this.root.permitAll();
    }

    @Override
    public boolean denyAll() {
        return this.root.denyAll();
    }

    @Override
    public boolean isAnonymous() {
        return this.root.isAnonymous();
    }

    @Override
    public boolean isAuthenticated() {
        return this.root.isAuthenticated();
    }

    @Override
    public boolean isRememberMe() {
        return this.root.isRememberMe();
    }

    @Override
    public boolean isFullyAuthenticated() {
        return this.root.isFullyAuthenticated();
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        return SecurityContextHolder.isAdmin() || this.root.hasPermission(target, permission);
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return SecurityContextHolder.isAdmin() || this.root.hasPermission(targetId, targetType, permission);
    }

    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator) {
        this.root.setPermissionEvaluator(permissionEvaluator);
    }

    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        this.root.setTrustResolver(trustResolver);

    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.root.setRoleHierarchy(roleHierarchy);
    }

    public void setDefaultRolePrefix(String defaultRolePrefix) {
        this.root.setDefaultRolePrefix(defaultRolePrefix);
    }
}
