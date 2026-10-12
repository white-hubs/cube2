package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo;

import lombok.Data;

@Data
public class BroadcastPlayRuleTemplateVO {

    /**
     * 模板主键
     */
    private Long templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板内容
     */
    private String templateContent;
}
