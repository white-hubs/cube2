package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 定时规则分页返回参数
 *
 * @author lanchun
 * @date 2022/6/29 18:10
 */
@Data
public class RuleTimingPageVO {

    /**
     * 规则主键
     */
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否循环
     */
    private Boolean looped;

    /**
     * 触发次数
     */
    private Integer triggerNum;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
