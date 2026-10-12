package com.guhecloud.cube.biz.meattrade.server.base.vo;

import lombok.Data;

/**
 * 重量损耗信息
 */
@Data
public class WeightLossPageVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 起点时间
     */
    private Double startTime;

    /**
     * 终点时间
     */
    private Double endTime;

    /**
     * 损耗重量
     */
    private Double loss;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 备注
     */
    private String remark;

}
