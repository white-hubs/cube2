package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: lw
 * @date: 2022/8/1
 */
@Data
public class AlarmRulesBasicParam {
    private Long messageId;
    //
    private String alarmName;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    //报警级别
    private String alarmLevel;

    /**
     * 触发级别名称
     */
    private String alarmLevelName;

    /**
     * 触发类型名称
     */
    private String alarmTypeName;

    //报警类型
    private String alarmType;

    //告警规则名称
    private String alarmRule;

    //数值
    private String alarmValue;
    /**
     * 绑定关联设备报警
     */
    private List<String> deviceIds;
}
