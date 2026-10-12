package com.guhecloud.cube.base.alarm.server.flow.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报警类型
 *
 * @author lanchun
 * @date 2023/2/28 11:28
 */
@Getter
@AllArgsConstructor
public class AlarmType {

    /**
     * 类型主键
     */
    private Long id;

    /**
     * 类型编号
     */
    private String code;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 应用主键
     */
    private Long applicationId;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;
}
