package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateParam {

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal readeValue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal fvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal pvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal gvalue;
    private BigDecimal totalValue;
    private String deviceId;
    private String areaId;
    private String routeId;
    private String time;
    private Integer tenant;
    private String instrumentType;
    private String routeName;
    private String projectName;
    private String projectId;
    private String deviceName;
    private String areaName;
    private String sequence;
}
