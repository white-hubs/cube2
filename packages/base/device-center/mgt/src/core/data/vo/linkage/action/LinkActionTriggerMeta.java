package com.guhecloud.cube.iot.mgt.core.data.vo.linkage.action;


import lombok.Data;

/**
 * 联动触发
 */
@Data
public class LinkActionTriggerMeta {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 规则id
     */
    private String ruleNum;

    private String ruleName;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 属性Code
     */
    private String propCode;

    /**
     * 需要的值
     */
    private String needValue;

    private String transparentStatement;

}