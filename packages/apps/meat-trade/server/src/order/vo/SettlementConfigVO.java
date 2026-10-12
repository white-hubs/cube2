package com.guhecloud.cube.biz.meattrade.server.order.vo;

import com.guhecloud.cube.biz.meattrade.server.order.enumeration.SettlementConfigEnum;
import lombok.Data;

/**
 * 结算配置信息
 */
@Data
public class SettlementConfigVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 计算名称
     */
    private String name;

    /**
     * 计算方式
     */
    private SettlementConfigEnum way;

    /**
     * 起始重量
     */
    private Double startWeight;

    /**
     * 结束重量
     */
    private Double endWeight;

    /**
     * 数值
     */
    private Double value;

    /**
     * 是否选中
     */
    private Boolean flag;

    /**
     * 标识符
     */
    private Long key;
}
