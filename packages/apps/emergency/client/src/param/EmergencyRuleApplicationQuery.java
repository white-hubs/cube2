package com.guhecloud.cube.biz.emergency.client.param;

import lombok.Data;

import java.util.List;

@Data
public class EmergencyRuleApplicationQuery {
    /**
     * 应用编码集合
     */
    private List<String> applicationList;
    /**
     * 报警事件集合
     */
    private List<String> alarmEventList;
}
