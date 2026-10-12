package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FPGDataVo {

    //区域id
    private String areaId;
    //支路id
    private String routeId;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal fvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal pvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal gvalue;

}
