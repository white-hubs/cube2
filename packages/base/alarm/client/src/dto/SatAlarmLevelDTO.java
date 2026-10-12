package com.guhecloud.cube.base.alarm.client.dto;

import lombok.Data;

/**
 * 报警等级返回数据
 *
 * @author lanchun
 * @date 2022/6/2 14:12
 */
@Data
public class SatAlarmLevelDTO {

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
     * 序号
     */
    private Integer sort;
}
