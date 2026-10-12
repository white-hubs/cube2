package com.guhecloud.cube.base.alarm.client.dto;

import lombok.Data;

/**
 * 报警对象类型统计返回参数
 *
 * @author lanchun
 * @date 2023/12/19 17:03
 */
@Data
public class AlarmTargetTypeStatisticsDTO {

    /**
     * 设备类型主键
     */
    private String alarmTargetTypeId;

    /**
     * 报警数量
     */
    private Integer alarmNum;
}
