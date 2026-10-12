package com.guhecloud.platform.security.util;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.model.Principal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

/**
 * 令牌工具类
 *
 * @author Woody
 * @date 2021/6/19
 */
public final class TokenUtils {

    /**
     * 主体身份标识
     */
    public static final String JWT_CLAIM_ID = "id";

    /**
     * JWT身份标识
     */
    public static final String JWT_CLAIM_JTI = "jti";

    /**
     * 超级管理员标识
     */
    public static final String JWT_CLAIM_ADMIN = "admin";

    /**
     * 用户名标识
     */
    public static final String JWT_CLAIM_NAME = "name";

    /**
     * 用户账号标识
     */
    public static final String JWT_CLAIM_USER_NAME = "user_name";

    /**
     * 客户端账号标识
     */
    public static final String JWT_CLAIM_CLIENT_ID = "client_id";

    /**
     * 用户权限标识
     */
    public static final String JWT_CLAIM_AUTHORITIES = "authorities";

    /**
     * 租户编号（多个，逗号分割）
     */
    public static final String JWT_CLAIM_TENANTS = "tenants";

    /**
     * 管理员租户编号（多个，逗号分割）
     */
    public static final String JWT_CLAIM_ADMINISTRATED_TENANTS = "administrated_tenants";
    /**
     * 来源端,有些场景无法确定终端，需要后端先存储在返回
     */
    public static final String JWT_CLAIM_TERMINAL = "terminal";

    private TokenUtils() {
    }

    /**
     * 获取主体身份
     *
     * @param jwt JWT对象
     *
     * @return 主体身份标识
     */
    public static Long getId(Jwt jwt) {
        String value = ObjectUtils.ifNull(jwt, t -> t.getClaimAsString(JWT_CLAIM_ID));
        return ObjectUtils.ifNull(value, Long::parseLong);
    }

    /**
     * 获取主体身份
     *
     * @param token OAuth2访问令牌对象
     *
     * @return 主体身份标识
     */
    public static Long getId(OAuth2AccessToken token) {
        Jwt jwt = Jwt.withTokenValue(token.getTokenValue()).build();
        return jwt.getClaim(JWT_CLAIM_ID);
    }

    /**
     * 获取JWT身份标识
     *
     * @param jwt JWT对象
     *
     * @return JWT身份标识
     */
    public static String getJti(Jwt jwt) {
        return jwt == null ? null : jwt.getClaimAsString(JWT_CLAIM_JTI);
    }

    /**
     * 获取JWT身份标识
     *
     * @param token OAuth2访问令牌对象
     *
     * @return JWT身份标识
     */
    public static String getJti(OAuth2AccessToken token) {
        Jwt jwt = Jwt.withTokenValue(token.getTokenValue()).build();
        return jwt.getClaimAsString(JWT_CLAIM_JTI);
    }

    /**
     * 获取主体类型枚举
     *
     * @param jwt JWT对象
     *
     * @return 主体类型枚举
     */
    public static Principal.Type getType(Jwt jwt) {
        return jwt == null ? null : jwt.hasClaim(JWT_CLAIM_USER_NAME) ? Principal.Type.USER :
                                    jwt.hasClaim(JWT_CLAIM_CLIENT_ID) ? Principal.Type.CLIENT : null;
    }

    /**
     * 获取主体类型枚举
     *
     * @param token OAuth2访问令牌对象
     *
     * @return 主体类型枚举
     */
    public static Principal.Type getType(OAuth2AccessToken token) {
        Jwt jwt = Jwt.withTokenValue(token.getTokenValue()).build();
        if (jwt.hasClaim(JWT_CLAIM_USER_NAME)) {
            return Principal.Type.USER;
        }
        if (jwt.hasClaim(JWT_CLAIM_CLIENT_ID)) {
            return Principal.Type.CLIENT;
        }
        return null;

    }

    /**
     * 判断是否是超级管理员
     *
     * @param jwt JWT对象
     *
     * @return true/false
     */
    public static Boolean getAdmin(Jwt jwt) {
        return jwt == null ? null : jwt.getClaimAsBoolean(JWT_CLAIM_ADMIN);
    }

    /**
     * 判断是否是超级管理员
     *
     * @param token OAuth2访问令牌对象
     *
     * @return true/false
     */
    public static Boolean getAdmin(OAuth2AccessToken token) {
        Jwt jwt = Jwt.withTokenValue(token.getTokenValue()).build();
        return jwt.getClaimAsBoolean(JWT_CLAIM_ADMIN);
    }

    /**
     * 获取登录用户名
     *
     * @param jwt
     *
     * @return
     */
    public static String getName(Jwt jwt) {
        return ObjectUtils.ifNull(jwt, t -> jwt.getClaimAsString(JWT_CLAIM_NAME),
                                  () -> jwt.getClaimAsString(JWT_CLAIM_NAME));
    }

    /**
     * 获取登录用户名
     *
     * @param token OAuth2访问令牌对象
     *
     * @return 登录账号
     */
    public static String getName(OAuth2AccessToken token) {
        Jwt jwt = Jwt.withTokenValue(token.getTokenValue()).build();
        String accountWithUsername = jwt.getClaimAsString(JWT_CLAIM_NAME);
        if (StringUtils.isNotEmpty(accountWithUsername)) {
            return accountWithUsername;
        }
        return jwt.getClaimAsString(JWT_CLAIM_CLIENT_ID);
    }

    /**
     * 获取登录账号
     *
     * @param jwt JWT对象
     *
     * @return 登录账号
     */
    public static String getAccount(Jwt jwt) {
        return ObjectUtils.ifNull(jwt, t -> jwt.getClaimAsString(JWT_CLAIM_USER_NAME),
                                  () -> jwt.getClaimAsString(JWT_CLAIM_CLIENT_ID));
    }

    /**
     * 获取登录账号
     *
     * @param token OAuth2访问令牌对象
     *
     * @return 登录账号
     */
    public static String getAccount(OAuth2AccessToken token) {
        Jwt jwt = Jwt.withTokenValue(token.getTokenValue()).build();
        String accountWithUsername = jwt.getClaimAsString(JWT_CLAIM_USER_NAME);
        if (StringUtils.isNotEmpty(accountWithUsername)) {
            return accountWithUsername;
        }
        return jwt.getClaimAsString(JWT_CLAIM_CLIENT_ID);
    }

    /**
     * 获取登录账号
     *
     * @param authentication 认证信息
     *
     * @return 登录账号
     */
    public static String getAccount(JwtAuthenticationToken authentication) {
        return getAccount(authentication.getToken());
    }

    /**
     * 获取用户权限列表
     *
     * @param jwt JWT对象
     *
     * @return 权限标识列表
     */
    public static List<String> getAuthorities(Jwt jwt) {
        return jwt == null ? null : jwt.getClaimAsStringList(JWT_CLAIM_AUTHORITIES);
    }

    /**
     * 获取租户编号列表
     *
     * @param jwt JWT对象
     *
     * @return 租户编号列表
     */
    public static List<String> getTenants(Jwt jwt) {
        return jwt == null ? null : jwt.getClaimAsStringList(JWT_CLAIM_TENANTS);
    }

    /**
     * 获取管理员租户编号列表
     *
     * @param jwt JWT对象
     *
     * @return 租户编号列表
     */
    public static List<String> getAdministratedTenants(Jwt jwt) {
        return jwt == null ? null : jwt.getClaimAsStringList(JWT_CLAIM_ADMINISTRATED_TENANTS);
    }

    /**
     * 构建令牌缓存标识（登录账号）
     *
     * @param account 登录账号
     *
     * @return 标识
     */
    public static String key(String account) {
        return String.format("oauth2:user_jti:%s", account);
    }
}
