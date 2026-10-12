package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo;

import lombok.Data;

/**
 * 广播模板列表返回参数
 *
 * @author lanchun
 * @date 2022/12/12 15:24
 */
@Data
public class BroadcastTemplateVO {

    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 是否是默认模板
     * true:默认模板,false:自定义模板
     */
    private Boolean defaulted;
}
