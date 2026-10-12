package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报警对象
 *
 * @author lanchun
 * @date 2023/2/28 11:32
 */
@AllArgsConstructor
@Getter
public class AlarmTarget {

    /**
     * 对象主键
     */
    private Long alarmTargetKey;

    /**
     * 对象编号
     */
    private String alarmTargetCode;

    /**
     * 对象名称
     */
    private String alarmTargetName;

    /**
     * 对象类型主键
     */
    private String alarmTargetTypeId;

    /**
     * 对象类型名称
     */
    private String alarmTargetTypeName;

    /**
     * 报警对象经纬度
     */
    private String alarmTargetLongitude;

    /**
     * 报警对象位置信息
     */
    private String alarmTargetAddress;
}
