package com.guhecloud.cube.srv.gen.property.management.server.consumable.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 仓库耗材库存
 * @date 2023-03-16 10:08:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConsumableStoreStock extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //仓库ID
    private Long storeId;
    //耗材ID
    private Long consumableId;
    //耗材名称
    private String consumableName;
    //库存量
    private Integer num;
    /**
     * 均价
     */
    private BigDecimal avgPrice;
    //金额
    private BigDecimal amount;
    //存放位置（扩展字段）
    private String position;
    //占用库存
    private Integer occupyNum;

}