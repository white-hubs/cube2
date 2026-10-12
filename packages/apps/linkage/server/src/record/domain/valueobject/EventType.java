package com.guhecloud.cube.biz.linkage.server.record.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件类型
 */
@Getter
@AllArgsConstructor
public class EventType {

    /**
     * 事件分类主键
     */
    private Long id;

    /**
     * 事件分类编号
     */
    private String code;

    /**
     * 事件分类名称
     */
    private String name;
}
