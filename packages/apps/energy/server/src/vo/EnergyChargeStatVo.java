package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnergyChargeStatVo {

    private String tenant;
    //设备名称
    private String deviceName;
    //项名称
    private String projectName;
    //设备类型
    private String instrumentType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant time;
    //时间标志
    private int timeFlag;

    private String areaId;
    //
    private String routeId;
    //用量
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal fvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal pvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal gvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal jvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalValue;  //累计用量

    /**
     * 费用
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amount = BigDecimal.ZERO;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amountQoQ = BigDecimal.ZERO;//费用环比
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amountYoY = BigDecimal.ZERO;//费用同比
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal fvAmount = BigDecimal.ZERO;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal fvAmountQoQ = BigDecimal.ZERO;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal pvAmount = BigDecimal.ZERO;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal pvAmountQoQ = BigDecimal.ZERO;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal gvAmount = BigDecimal.ZERO;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal gvAmountQoQ = BigDecimal.ZERO;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal jvAmount = BigDecimal.ZERO;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal jvAmountQoQ = BigDecimal.ZERO;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalAmount = BigDecimal.ZERO;//累计费用
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalAmountQoQ = BigDecimal.ZERO;
}
