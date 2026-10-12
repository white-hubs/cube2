package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendStatVO {
    private String time;
    private String name;
    private String instrumentTypeName;
    private BigDecimal value;

    private BigDecimal dcValue;
    private BigDecimal zhValue;
}
