package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.vo;

import lombok.Data;

/**
 * 设备报警规则分页返回参数
 *
 * @author lanchun
 * @date 2022/6/29 17:47
 */
@Data
public class RuleDeviceAlarmPageVO {

    /**
     * 规则主键
     */
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 设备类型主键
     */
    private String deviceGenreId;

    /**
     * 设备类型名称
     */
    private String deviceGenreName;

    /**
     * 报警级别主键
     */
    private Long alarmLevelId;

    /**
     * 报警级别名称
     */
    private String alarmLevelName;

    /**
     * 报警类型主键
     */
    private Long alarmTypeId;

    /**
     * 报警类型名称
     */
    private String alarmTypeName;

    /**
     * 启用状态
     */
    private Boolean enabled;
}
