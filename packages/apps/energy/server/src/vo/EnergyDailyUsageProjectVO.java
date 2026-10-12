package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.cube.srv.gen.energymanagement.server.common.CarbonCal;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @Description 每日分项用量
 * @date 2021-12-30 15:12:36
 */
@Data
public class EnergyDailyUsageProjectVO extends CarbonCal {
    //
    private Long id;
    private LocalDateTime statDate;
    private String instrumentTypeName;
    private String projectCategory;
    private String projectId;
    private String projectCode;
    private String projectName;
}