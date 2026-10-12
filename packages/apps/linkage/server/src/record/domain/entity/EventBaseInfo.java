package com.guhecloud.cube.biz.linkage.server.record.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 报警信息
 *
 * @author lanchun
 * @date 2023/2/28 11:24
 */
@Getter
@AllArgsConstructor
public class EventBaseInfo {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 报警事件
     */
    private String name;

    /**
     * 事件id
     */
    private Long eventId;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;
}
