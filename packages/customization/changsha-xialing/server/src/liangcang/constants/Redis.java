package com.guhecloud.cube.customization.xialing.server.liangcang.constants;

/** Redis key 前缀。 */
public interface Redis {

    String TOKEN_KEY_PREFIX = "changsha:liangcang:warehouse:x-token:";
    String LOCK_PREFIX = "lock:";
    String TENANT_PLACEHOLDER_NONE = "none";
}
