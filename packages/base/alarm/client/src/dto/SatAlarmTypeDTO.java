package com.guhecloud.cube.base.alarm.client.dto;

import lombok.Data;

import java.util.List;

/**
 * 报警类型数据模型
 *
 * @author lanchun
 * @date 2022/6/2 14:29
 */
@Data
public class SatAlarmTypeDTO {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型关联的访问端
     */
    private List<String> terminals;
}
