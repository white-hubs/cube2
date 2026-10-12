package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CommonStatVO {
    private LocalDateTime date;//日期
    private String projectId;//分项ID
    private String areaId;//区域ID
    private String routeId;//支路ID
    private String deviceId;
    private String instrumentType;//仪表类型
    private String instrumentTypeName;//仪表类型名称
    private BigDecimal value;//值
    private BigDecimal fvalue;//值
    private BigDecimal pvalue;//值
    private BigDecimal gvalue;//值
    private BigDecimal jvalue;//值
}
