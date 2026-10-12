package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.utils;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

@Slf4j
public class StringSplitIdUtil {
    public static void handlerIdsAddSet(String ids, HashSet<Long> deviceIdSet) {
        if (StringUtils.isBlank(ids)) {
            return;
        }
        StringUtils.split(ids, ",").forEach(value -> {
            if (StringUtils.isNotBlank(value)) {
                try {
                    deviceIdSet.add(Long.parseLong(value));
                } catch (NumberFormatException e) {
                    log.warn("Invalid device id format: {}", value, e);
                }
            }
        });
    }
}
