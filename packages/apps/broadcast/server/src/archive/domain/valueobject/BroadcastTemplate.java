package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BroadcastTemplate {

    /**
     * 模板主键
     */
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板内容
     */
    private String content;

    public BroadcastTemplate() {
    }
}
