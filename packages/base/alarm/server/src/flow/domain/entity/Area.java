package com.guhecloud.cube.base.alarm.server.flow.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 区域信息
 *
 * @author lanchun
 * @date 2023/2/28 11:33
 */
@AllArgsConstructor
@Getter
public class Area {

    /**
     * 报警区域
     */
    private String area;

    /**
     * 报警区域名称
     */
    private String areaName;
}
