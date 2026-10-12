package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 租售情况统计返回对象
 */
@Data
public class RentedSoldStatResult {
    /**
     * 租
     */
    private BigDecimal rented;
    /**
     * 售
     */
    private BigDecimal sold;
    /**
     * 预定
     */
    private BigDecimal reserve;
    /**
     * 总(非闲置)
     */
    private BigDecimal total;
}
