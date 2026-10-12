package com.guhecloud.cube.srv.gen.maintain.server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 率
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalNumberRateDTO {
    private Long number;
    private Long total;
    private BigDecimal rate;
}
