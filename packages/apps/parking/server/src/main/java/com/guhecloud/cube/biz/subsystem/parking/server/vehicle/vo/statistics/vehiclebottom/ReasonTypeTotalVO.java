package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.statistics.vehiclebottom;

import lombok.Data;

/**
 * 事由分类统计
 *
 * @author lanchun
 * @date 2024/12/12 16:57
 */
@Data
public class ReasonTypeTotalVO {

    /**
     * 数量
     */
    private Long num;

    /**
     * 事由分类名称
     */
    private String reasonName;

    /**
     * 事由分类编号
     */
    private String reasonType;

}
