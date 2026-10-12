package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElectricVehicleViolationStatVO {

    /**
     * 违规充电总数
     */
    private Integer chargeNum;

    /**
     * 违规充电环比
     */
    private BigDecimal chargeChainRatio;

    /**
     * 违规停放总数
     */
    private Integer parkingNum;

    /**
     * 违规停放环比
     */
    private BigDecimal parkingChainRatio;

}
