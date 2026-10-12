package com.guhecloud.cube.srv.gen.maintain.server.common.config;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/9/19 13:41
 * @description
 */
public interface InDeDuplicationPeriod {
    boolean isInDeDuplicationPeriod(LocalDateTime startTime, LocalDateTime endTime);

    boolean isInTimeDuration(Integer timeSecond);
}
