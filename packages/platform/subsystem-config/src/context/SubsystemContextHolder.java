package com.guhecloud.subsystem.config.context;

import com.guhecloud.subsystem.config.listener.web.model.SubsystemRequester;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 子系统上下文工具
 *
 * @author lanchun
 * @date 2022/10/11 11:57
 */
@Component
public class SubsystemContextHolder {

    /**
     * 客户端请求对象上下文
     */
    private static final ThreadLocal<SubsystemRequester> REQUESTER_CONTEXT = new ThreadLocal<>();

    /**
     * 设置客户端请求对象
     *
     * @param requester 客户端请求对象
     */
    public static void setRequester(@NonNull SubsystemRequester requester) {
        REQUESTER_CONTEXT.set(requester);
    }

    /**
     * 清空上下文信息
     */
    public static void clearSessionContext() {
        REQUESTER_CONTEXT.remove();
    }

    /**
     * 获取客户端请求对象
     *
     * @return 客户端请求对象
     */
    public static SubsystemRequester getRequester() {
        return getRequester(false);
    }

    /**
     * 获取客户端请求对象
     *
     * @param nullable 客户端请求对象是否可以为空
     *
     * @return 客户端请求对象
     */
    public static SubsystemRequester getRequester(boolean nullable) {
        SubsystemRequester requester = REQUESTER_CONTEXT.get();
        return nullable ? requester : Objects.requireNonNull(requester, "SubsystemRequester has not been initialized");
    }
}
