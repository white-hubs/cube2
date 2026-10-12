package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EnergyDailyUsageProjectResultVO {
    private Long id;
    private LocalDateTime statDate;
    private String time;
    private String instrumentType;
    private String instrumentTypeName;
    private String projectCategory;
    private String projectId;
    private String projectCode;
    private String projectName;
    private BigDecimal ddValue;

    //碳排
    private BigDecimal dcValue;
    //综合能耗
    private BigDecimal zhValue;
    private LocalDateTime createTime;
}
