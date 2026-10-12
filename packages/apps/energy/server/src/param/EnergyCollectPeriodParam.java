package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 能耗采集时段定义
 * @date 2024-01-26 10:08:36
 */
@Data
public class EnergyCollectPeriodParam {

    //仪表类型
    private String instrumentType;
    //时段标识：FV\PV\GV\JV
    private String code;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //排序号
    private Integer sort;

}