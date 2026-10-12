package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 循环参数
 *
 * @author lanchun
 * @date 2023/3/23 12:00
 */
@Getter
public class Circulate {

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    public Circulate() {
    }

    public Circulate(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
