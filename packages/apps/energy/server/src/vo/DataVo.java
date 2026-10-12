package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class DataVo {
    //设备编号
    private String deviceId;
    //区域id
    private String areaId;
    private String areaName;
    //支路id
    private String routeId;
    private String dateFlag;
    private String tenant;
    //设备名称
    private String deviceName;
    //项名称
    private String projectId;
    private String projectName;
    //设备类型
    private String instrumentType;
    //时间标识
    private Integer timeFlag;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalValue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal fvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal pvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal gvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal jvalue;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal coal;//煤
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal carbon;//碳
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant time;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal readeValue;
    //时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime nowReadeTime;
    //占比
    private BigDecimal proportion;
    //父级序列
    private String sequence;

}
