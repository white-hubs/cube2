package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 定时规则
 *
 * @author lanchun
 * @date 2022/6/29 16:02
 */
@Data
public class RuleTimingDTO {

    private Long id;

    /**
     * 是否循环
     */
    private Boolean looped;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;
}
