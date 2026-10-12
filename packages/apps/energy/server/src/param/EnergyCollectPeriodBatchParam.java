package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * @author Appet
 * @Description 能耗采集时段定义
 * @date 2024-01-26 10:08:36
 */
@Data
public class EnergyCollectPeriodBatchParam {
    //仪表类型
    @NotEmpty
    private String instrumentType;
    private List<EnergyCollectPeriodParam> dataList;
}