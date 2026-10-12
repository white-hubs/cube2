package com.guhecloud.cube.srv.gen.maintain.server.electron.vo;

import lombok.Data;

import java.util.List;

@Data
public class PatrolPlanTaskVO {
    private Long id;
    /**
     * 时间范围
     */
    private String timeRange;
    /**
     * 间隔时间的值
     */
    private Integer intervalNumber;
    /**
     * 间隔时间的单位
     * 使用：ChronoUnit.HOURS
     */
    private Integer intervalUnit;
    /**
     * 拆分的时间段
     */
    private List<String> executePeriod;

}
