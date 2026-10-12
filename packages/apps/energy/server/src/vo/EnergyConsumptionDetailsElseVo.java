package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EnergyConsumptionDetailsElseVo {
    //名称
    private String name;
    //总量
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;

}
