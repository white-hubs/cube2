package com.guhecloud.cube.base.alarm.server.flow.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报警级别实体
 *
 * @author lanchun
 * @date 2023/2/28 11:30
 */
@Getter
@AllArgsConstructor
public class AlarmLevel {

    /**
     * 级别主键
     */
    private Long id;

    /**
     * 级别编号
     */
    private String code;

    /**
     * 级别名称
     */
    private String name;

    /**
     * 级别序号
     */
    private Integer sort;
}
