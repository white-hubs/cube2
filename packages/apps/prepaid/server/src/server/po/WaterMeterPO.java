package com.guhecloud.cube.srv.prepaid.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 水表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("water_meter")
@EqualsAndHashCode(callSuper = true)
public class WaterMeterPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 水表编号
     */
    private String meterId;
    /**
     * 商铺编号
     */
    private String roomId;
    /**
     * 剩余水量
     */
    private String account;
    /**
     * 总购水金额
     */
    private BigDecimal totalMoney;
    /**
     * 累计购水量
     */
    private String totalPower;
    /**
     * 水价
     */
    private BigDecimal price;
    /**
     * 是否失联
     */
    private Boolean unConnect;
    /**
     * 是否欠费
     */
    private Boolean oweStatus;
}
