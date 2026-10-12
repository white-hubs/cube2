package com.guhecloud.cube.biz.linkage.server.record.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件级别实体
 */
@Getter
@AllArgsConstructor
public class EventLevelVO {

    /**
     * 级别主键
     */
    private Long id;

    /**
     * 级别编号
     */
    private String code;

    /**
     * 级别名称
     */
    private String name;
}
