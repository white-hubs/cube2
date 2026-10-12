package com.guhecloud.cube.biz.linkage.server.record.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件应用
 */
@Getter
@AllArgsConstructor
public class EventApp {

    /**
     * 应用编号
     */
    private String code;

    /**
     * 应用名称
     */
    private String name;
}
