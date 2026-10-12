package com.guhecloud.cube.biz.adapter.park.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 停车缴费统计
 *
 * @author lanchun
 * @date 2022/9/28 17:47
 */
@Data
@AllArgsConstructor
public class ParkingPayTotalVO {

    /**
     * 收费金额(元)
     */
    private BigDecimal payAmount;
}
