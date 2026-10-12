package com.guhecloud.cube.srv.gen.energymanagement.server.common;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarbonCal {

    private String instrumentType;
    //用量
    private BigDecimal ddValue;
    //碳排量
    private BigDecimal dcValue;//碳排折算
    //综合能耗折算
    private BigDecimal zhValue;//综合能耗折算

}
