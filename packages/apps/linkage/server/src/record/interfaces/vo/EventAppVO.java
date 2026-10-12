package com.guhecloud.cube.biz.linkage.server.record.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件应用
 */
@Getter
@AllArgsConstructor
public class EventAppVO {

    /**
     * 应用编号
     */
    private String code;

    /**
     * 应用名称
     */
    private String name;
}
