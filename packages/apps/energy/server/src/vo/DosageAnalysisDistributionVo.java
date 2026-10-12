package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DosageAnalysisDistributionVo {
    private String timeFlag;
    //峰值
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal fvalue = new BigDecimal("0.00");
    //平值
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal pvalue = new BigDecimal("0.00");
    //谷值
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal gvalue = new BigDecimal("0.00");
}
