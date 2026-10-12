package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo;

import lombok.Data;

import java.util.List;

@Data
public class BroadcastPlayRuleVO {

    private Long id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 规则描述
     */
    private String remark;

    /**
     * 模板集合
     */
    private List<BroadcastPlayRuleTemplateVO> templates;

    /**
     * 设备集合
     */
    private List<BroadcastPlayRuleDeviceVO> devices;
}
