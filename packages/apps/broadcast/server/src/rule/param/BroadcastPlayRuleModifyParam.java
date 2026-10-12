package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.param;

import lombok.Data;

import java.util.List;

@Data
public class BroadcastPlayRuleModifyParam {

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
    private List<BroadcastPlayRuleTemplateMofidyParam> templates;

    /**
     * 设备集合
     */
    private List<BroadcastPlayRuleDeviceModifyParam> devices;
}
