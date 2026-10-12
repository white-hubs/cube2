package com.guhecloud.cube.biz.emergency.client.param;

import lombok.Data;

@Data
public class EmergencyRuleSafetyReportVO {

    private String name;
    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
