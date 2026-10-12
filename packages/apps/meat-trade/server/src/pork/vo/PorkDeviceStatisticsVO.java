package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import lombok.Data;

@Data
public class PorkDeviceStatisticsVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名字
     */
    private String name;

    /**
     * 数量
     */
    private Integer number;
}
