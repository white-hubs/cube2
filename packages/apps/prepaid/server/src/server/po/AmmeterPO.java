package com.guhecloud.cube.srv.prepaid.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 电表实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("ammeter")
@EqualsAndHashCode(callSuper = true)
public class AmmeterPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 电表编号
     */
    private String meterId;
    /**
     * 商铺编号
     */
    private String roomId;
    /**
     * 剩余电费
     */
    private BigDecimal powerRemain;
    /**
     * 电表型号
     */
    private String model;
    /**
     * 合并计量余额
     */
    private BigDecimal money;
    /**
     * 是否欠费（true表示欠费）
     */
    private Boolean oweMoney;
    /**
     * 是否失连（true为失连）
     */
    private Boolean unConnect;
    /**
     * 分合闸状态（true为拉闸，false为合闸）
     */
    private Boolean openOrClose;
    /**
     * 总用电量
     */
    private String powerTotal;
}
