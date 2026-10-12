package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 场景规则分页返回参数
 *
 * @author lanchun
 * @date 2022/6/30 10:34
 */
@Data
public class RuleScenePageVO {

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
     * 触发次数
     */
    private Integer triggerNum;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
