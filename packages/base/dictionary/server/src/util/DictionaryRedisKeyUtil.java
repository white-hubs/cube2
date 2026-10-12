package com.guhecloud.cube.base.dictionary.server.util;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-25  10:58
 * @Description:
 * @Version: 1.0
 */
public class DictionaryRedisKeyUtil {
    private DictionaryRedisKeyUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取redis存储key
     */
    public static String getRedisKey(String itemCode, String code) {
        return "DICTIONARY:" + itemCode + ":" + SessionContextHolder.getRequester().getTenant() + ":" + code;
    }

    /**
     * 获取redis存储key 用户级
     */
    public static String getRedisKeyLimitUser(String itemCode, String code) {
        return "DICTIONARY:" + itemCode + ":" + SessionContextHolder.getRequester().getTenant() + ":" +
               SecurityContextHolder.getPrincipal().getId() + ":" + code;
    }
}
