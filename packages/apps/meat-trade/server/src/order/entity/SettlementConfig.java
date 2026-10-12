package com.guhecloud.cube.biz.meattrade.server.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.order.enumeration.SettlementConfigEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 结算配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("meattrade_settlement_config")
public class SettlementConfig extends BasicEnhancedEntity<Long> {

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
    private BigDecimal startWeight;

    /**
     * 结束重量
     */
    private BigDecimal endWeight;

    /**
     * 数值
     */
    private BigDecimal value;

    /**
     * 是否选中
     */
    private Boolean flag;

    /**
     * 市场方租户编号
     */
    private Integer tenant;
}
