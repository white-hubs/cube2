package com.guhecloud.cube.srv.gen.maintain.server.electron.vo;

import lombok.Data;

import java.util.List;

/**
 * 计算任务时间段返回对象
 */
@Data
public class CalcTaskPeriodVO {
    private String tip;
    private List<String> periodList;
}
