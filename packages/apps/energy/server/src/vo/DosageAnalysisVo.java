package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DosageAnalysisVo {
    private String timeFlag;

    private String name;
    //本期值
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value = new BigDecimal("0.00");
    //上期值
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal lastValue = new BigDecimal("0.00");
    //温度
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal temperature = new BigDecimal("0.00");
}
