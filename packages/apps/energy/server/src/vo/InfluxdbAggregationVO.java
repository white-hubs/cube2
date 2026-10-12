package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class InfluxdbAggregationVO {

    private String deviceId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant start;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;
}
