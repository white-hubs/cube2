package com.guhecloud.cube.onecard.server.consumption.utils;

import com.guhecloud.platform.base.context.SessionContextHolder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 19:08
 * @Description:
 */
public class RedisTimeStampUtils {

    private static final String LAST_SYNC_TIME_STAMP = "LAST_SYNC_TIME_STAMP";

    public static String getRedisKey() {
        return LAST_SYNC_TIME_STAMP + ":" + SessionContextHolder.getRequester().getTenant() + ":" + "CONSUMPTION_RECORD";
    }
}
