package com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AttributeHistoryVo {
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;
    private String ts;
}
