package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 耗材出库统计
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableReduceStockStatVO {
    //部门
    private Long deptId;

    private String deptName;
    //耗材数量
    private Integer consumableNum;
    //金额
    private BigDecimal amount;
}