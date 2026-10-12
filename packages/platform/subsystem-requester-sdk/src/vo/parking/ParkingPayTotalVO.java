package com.guhecloud.subsystem.requester.sdk.vo.parking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 停车缴费统计
 *
 * @author lanchun
 * @date 2022/9/28 17:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingPayTotalVO {

    /**
     * 收费金额(元)
     */
    private BigDecimal payAmount;
}
