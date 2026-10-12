package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EnergyProjectAreaStatVO {

    private LocalDateTime statDate;
    // 字符串的统计日期(statDate)
    private String time;
    private String projectCategory;
    private String instrumentType;
    private String instrumentTypeName;
    private String projectId;
    private String projectCode;
    private String projectName;
    private String parentId;
    private String parentName;
    // 用能
    private BigDecimal ddValue;
    //碳排
    private BigDecimal dcValue;
    //综合能耗
    private BigDecimal zhValue;
}
