package com.guhecloud.cube.base.message2.server.sat.constants;

import com.guhecloud.platform.base.context.SessionContextHolder;

/**
 * 存放消息常量
 *
 * @author lanchun
 * @date 2024/9/2 11:01
 */
public final class SatMsgConstant {

    /**
     * 用户消息忽略配置缓存key
     */
    public static final String USER_MSG_IGNORED_REDIS_KEY = "USER_MSG_IGNORED";

    private SatMsgConstant() {

    }

    public static String getTenantRedisKey(String key) {
        return key + ":" + SessionContextHolder.getRequester().getTenant();
    }

    public static String getTenantRedisKey(String key, Integer tenant) {
        return key + ":" + tenant;
    }
}
