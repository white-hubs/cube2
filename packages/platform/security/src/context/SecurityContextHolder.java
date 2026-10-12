package com.guhecloud.platform.security.context;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.config.SecurityProperties;
import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.platform.security.util.TokenUtils;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityContextHolder implements ApplicationContextAware {
    /**
     * JWT上下文
     */
    private static final ThreadLocal<Jwt> JWT_CONTEXT = new ThreadLocal<>();

    /**
     * 认证用户信息上下文
     */
    private static final ThreadLocal<Principal> PRINCIPAL_CONTEXT = new ThreadLocal<>();

    /**
     * 用户的数据权限
     */
    private static final ThreadLocal<String> DB_DATA_PERMISSION = new ThreadLocal<>();

    /**
     * 数据权限开关管控
     */
    private static final ThreadLocal<Boolean> DB_DATA_PERMISSION_SWITCH = new ThreadLocal<>();

    /**
     * 内部服务调用标识
     */
    private static final ThreadLocal<Boolean> IS_INNER_SERVER = new ThreadLocal<>();

    /**
     * 角色权限
     */
    private static final Map<String, Set<String>> AUTHORITIES = new HashMap<>();

    /**
     * 数字签名工具类实例
     */
    private static HmacUtils HMAC_UTILS = null;

    /**
     * 密码加解密处理器
     */
    private static PasswordEncoder PASSWORD_ENCODER = null;

    /**
     * 安全配置属性
     */
    private final SecurityProperties securityProperties;

    /**
     * 判断当前用户是否为超级管理员
     *
     * @return true/false
     */
    public static boolean isAdmin() {
        return Boolean.TRUE.equals(getPrincipalProperty(Principal::getAdmin));
    }

    /**
     * 判断当前用户是否为租户管理员
     *
     * @return true/false
     */
    public static boolean isTenantAdmin() {
        Requester requester = SessionContextHolder.getRequester();
        Integer requestTenant = requester.getTenant();
        if (Objects.isNull(requestTenant)) {
            return false;
        }
        List<String> administratedTenants = TokenUtils.getAdministratedTenants(getJwt());
        return Boolean.TRUE.equals(administratedTenants.contains(String.valueOf(requestTenant)));
    }

    /**
     * 判断当前认证主体是否为用户
     *
     * @return true/false
     */
    public static boolean isUser() {
        return getPrincipalProperty(Principal::getType) == Principal.Type.USER;
    }

    /**
     * 判断当前认证主体是否为指定用户
     *
     * @param username 登录账号
     *
     * @return true/false
     */
    public static boolean isUser(String username) {
        return isUser() && !StringUtils.isEmpty(username) && username.equals(getPrincipalProperty(Principal::getAccount));
    }

    /**
     * 判断当前认证主体是否为客户端
     *
     * @return true/false
     */
    public static boolean isClient() {
        return getPrincipalProperty(Principal::getType) == Principal.Type.CLIENT;
    }

    /**
     * 判断客户端编号是否为指定编号
     *
     * @param code 客户端编号
     *
     * @return true/false
     */
    public static boolean isClient(String code) {
        return isClient() && !StringUtils.isEmpty(code) && code.equals(getPrincipalProperty(Principal::getAccount));
    }

    /**
     * 判断当前登录账号是否为指定登录账号
     *
     * @param account 登录账号
     *
     * @return true/false
     */
    public static boolean isAccount(String account) {
        return !StringUtils.isEmpty(account) && account.equals(getPrincipalProperty(Principal::getAccount));
    }

    /**
     * 获取JWT信息
     *
     * @return JWT信息
     */
    public static Jwt getJwt() {
        return getJwt(false);
    }

    /**
     * 设置JWT信息
     *
     * @param jwt JWT信息
     */
    public static void setJwt(@NonNull Jwt jwt) {
        JWT_CONTEXT.set(jwt);
    }

    /**
     * 获取JWT信息
     *
     * @param nullable JWT是否可以为空
     *
     * @return JWT信息
     */
    public static Jwt getJwt(boolean nullable) {
        Jwt jwt = JWT_CONTEXT.get();
        return nullable ? jwt : Objects.requireNonNull(jwt, "Jwt has not been initialized");
    }

    /**
     * 获取认证主体属性
     *
     * @param function 认证主体属性方法
     * @param <T>      认证主体属性类型
     *
     * @return 认证主体属性值
     */
    public static <T> T getPrincipalProperty(@NonNull Function<Principal, T> function) {
        return ObjectUtils.ifNull(getPrincipal(true), function);
    }

    /**
     * 获取认证主体信息
     *
     * @return 认证主体信息
     */
    public static Principal getPrincipal() {
        return getPrincipal(false);
    }

    /**
     * 获取认证主体信息
     *
     * @param nullable 认证主体是否可以为空
     *
     * @return 认证主体信息
     */
    public static Principal getPrincipal(boolean nullable) {
        Principal principal = PRINCIPAL_CONTEXT.get();
        return nullable ? principal : Objects.requireNonNull(principal, "Principal has not been initialized");
    }

    /**
     * 设置认证主体信息
     *
     * @param principal 认证主体信息
     */
    public static void setPrincipal(Principal principal) {
        PRINCIPAL_CONTEXT.set(principal);
    }

    /**
     * 设置数据权限
     *
     * @param dataPermission
     */
    public static void setDbDataPermission(@NotNull String dataPermission) {
        DB_DATA_PERMISSION.set(dataPermission);
    }

    /**
     * 获取数据权限
     */
    public static String getDbDataPermission() {
        return DB_DATA_PERMISSION.get();
    }

    /**
     * 设置是否开启数据权限
     *
     * @param permissionSwitch
     */
    public static void setDbDataPermissionSwitch(@NonNull Boolean permissionSwitch) {
        DB_DATA_PERMISSION_SWITCH.set(permissionSwitch);
    }

    /**
     * 获取数据权限开关
     *
     * @return
     */
    public static Boolean getDbDataPermissionSwitch() {
        return DB_DATA_PERMISSION_SWITCH.get();
    }

    /**
     * 清除数据权限
     */
    public static void clearDbDataPermission() {
        DB_DATA_PERMISSION.remove();
    }

    /**
     * 设置调用者是否是内部服务
     *
     * @param isInnerServer
     */
    public static void setIsInnerServer(@NonNull Boolean isInnerServer) {
        IS_INNER_SERVER.set(isInnerServer);
    }

    /**
     * 当前上下文是否是内部服务调用
     *
     * @return
     */
    public static Boolean isInnerServer() {
        return IS_INNER_SERVER.get();
    }

    /**
     * 设置权限信息
     *
     * @param authority
     * @param resources
     */
    public static void setAuthorities(String authority, Set<String> resources) {
        AUTHORITIES.put(authority, resources);
    }

    /**
     * 移除权限信息
     *
     * @param authority
     */
    public static void removeAuthorities(String authority) {
        if (AUTHORITIES.get(authority) != null) {
            AUTHORITIES.remove(authority);
        }
    }

    public static boolean hasScope(String scope) {
        if (SecurityContextHolder.isClient()) {
            Jwt jwt = JWT_CONTEXT.get();
            String scopes = jwt.getClaimAsString("scope");
            if (StringUtils.isEmpty(scopes)) {
                return true;
            }
            List<String> scopeList = (List<String>) JSON.parse(scopes);
            if (!scopeList.contains(scope)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 清空上下文信息
     */
    public static void clearSecurityContext() {
        JWT_CONTEXT.remove();
        PRINCIPAL_CONTEXT.remove();
        DB_DATA_PERMISSION.remove();
        DB_DATA_PERMISSION_SWITCH.remove();
    }

    /**
     * 获取数字签名工具类实例
     *
     * @return 数字签名工具类实例
     */
    public static HmacUtils getHmacUtils() {
        return Objects.requireNonNull(HMAC_UTILS, "Hmac utils has not been initialized");
    }

    /**
     * 获取密码加解密处理器
     *
     * @return 密码加解密处理器
     */
    public static PasswordEncoder getPasswordEncoder() {
        return Objects.requireNonNull(PASSWORD_ENCODER, "Password encoder has not been initialized");
    }

    /**
     * 数字签名
     *
     * @param parameters 签名参数
     *
     * @return 签名信息
     */
    public static String sign(Map<?, ?> parameters) {
        Requester requester = SessionContextHolder.getRequester();
        Principal principal = SecurityContextHolder.getPrincipal();
        StringBuilder plaintext = new StringBuilder().append(requester.getTenant()).append(requester.getTerminal())
                .append(principal.getAccount());
        if (parameters instanceof TreeMap) {
            parameters.forEach((key, value) -> plaintext.append(key).append(value));
        } else if (MapUtils.isNotEmpty(parameters)) {
            List<?> keys = new ArrayList<>(parameters.keySet());
            keys.sort(Comparator.comparing(Object::toString));
            keys.forEach(key -> plaintext.append(key).append(parameters.get(key)));
        }
        return getHmacUtils().hmacHex(plaintext.toString()) + System.currentTimeMillis();
    }

    /**
     * 验证数字签名
     *
     * @param signature  数字签名信息
     * @param parameters 签名参数
     *
     * @return true/false
     */
    public static boolean validateSignature(String signature, Map<?, ?> parameters) {
        return !StringUtils.isEmpty(signature) && Objects.equals(signature.substring(0, 64), sign(parameters).substring(0, 64));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PASSWORD_ENCODER = applicationContext.getBean(PasswordEncoder.class);
        HMAC_UTILS = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, this.securityProperties.getSignature().getSecret());
    }

}
