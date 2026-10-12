package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.Data;

@Data
public class CameraPatrolCurrencyStatisticsVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 总数
     */
    private Integer totalNum;
}
