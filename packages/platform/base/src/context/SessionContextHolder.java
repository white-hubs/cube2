package com.guhecloud.platform.base.context;

import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class SessionContextHolder {

    /**
     * 客户端请求对象上下文
     */
    private static final ThreadLocal<Requester> REQUESTER_CONTEXT = new ThreadLocal<>();

    /**
     * 对象缓存映射上下文
     */
    private static final ThreadLocal<Map<Object, Object>> OBJECT_CACHE_CONTEXT = ThreadLocal.withInitial(HashMap::new);

    /**
     * 获取请求对象属性
     *
     * @param function 请求对象属性方法
     * @param <T>      请求对象属性类型
     *
     * @return 请求对象属性值
     */
    public static <T> T getRequesterProperty(@NonNull Function<Requester, T> function) {
        return ObjectUtils.ifNull(getRequester(true), function);
    }

    /**
     * 判断当前请求是否来自指定主机
     *
     * @param host 客户端主机地址
     *
     * @return true/false
     */
    public static boolean isHost(String host) {
        return !StringUtils.isEmpty(host) && host.equals(getRequesterProperty(Requester::getHost));
    }

    /**
     * 判断当前请求是否来自指定域名
     *
     * @param domain 服务端域名
     *
     * @return true/false
     */
    public static boolean isDomain(String domain) {
        return !StringUtils.isEmpty(domain) && domain.equals(getRequesterProperty(Requester::getDomain));
    }

    /**
     * 判断当前请求是否为指定租户
     *
     * @param tenant 租户编号
     *
     * @return true/false
     */
    public static boolean isTenant(Integer tenant) {
        return tenant != null && Objects.equals(tenant, getRequesterProperty(Requester::getTenant));
    }

    /**
     * 判断当前请求是否为指定访问端
     *
     * @param terminal 访问端编号
     *
     * @return true/false
     */
    public static boolean isTerminal(String terminal) {
        return !StringUtils.isEmpty(terminal) && terminal.equals(getRequesterProperty(Requester::getTerminal));
    }

    /**
     * 判断当前请求是否为指定版本号
     *
     * @param version 客户端版本号
     *
     * @return true/false
     */
    public static boolean isVersion(String version) {
        return !StringUtils.isEmpty(version) && version.equals(getRequesterProperty(Requester::getVersion));
    }

    /**
     * 获取客户端请求对象
     *
     * @return 客户端请求对象
     */
    public static Requester getRequester() {
        return getRequester(false);
    }

    /**
     * 获取客户端请求对象
     *
     * @param nullable 客户端请求对象是否可以为空
     *
     * @return 客户端请求对象
     */
    public static Requester getRequester(boolean nullable) {
        Requester requester = REQUESTER_CONTEXT.get();
        return nullable ? requester : Objects.requireNonNull(requester, "Requester has not been initialized");
    }

    /**
     * 设置客户端请求对象
     *
     * @param requester 客户端请求对象
     */
    public static void setRequester(@NonNull Requester requester) {
        REQUESTER_CONTEXT.set(requester);
    }

    /**
     * 针对后台发起的调用，例如：XXL-JOB,构建租户信息
     *
     * @param tenant
     */
    public static void requestTenant(@NotNull Integer tenant) {
        Requester requester = Requester.builder().tenant(tenant).build();
        REQUESTER_CONTEXT.set(requester);
    }

    /**
     * 从当前上下文中获取对象实例，方便统一对ThreadLocal缓存对象管理
     *
     * @param key      缓存标识
     * @param provider 对象提供方法
     * @param <K>      缓存键类型泛型
     * @param <V>      缓存值得类型泛型
     *
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <K, V> V getContextObject(K key, Function<K, V> provider) {
        return (V) OBJECT_CACHE_CONTEXT.get().computeIfAbsent(key, k -> provider.apply((K) k));
    }

    /**
     * 清空上下文信息
     */
    public static void clearSessionContext() {
        REQUESTER_CONTEXT.remove();
        OBJECT_CACHE_CONTEXT.remove();
    }

}
