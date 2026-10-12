package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.Data;

@Data
public class DeviceTagRuleVO {

    private Long id;

    /**
     * 标签编号
     */
    private String tagCode;

    /**
     * 规则内容
     */
    private String rule;
}
