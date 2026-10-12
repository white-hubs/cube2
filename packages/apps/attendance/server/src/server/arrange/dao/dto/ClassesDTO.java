package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassesDTO {
    private Long id;//排班班次ID
    private Long classesId;//班次模板ID
    private String date;//班次日期
    private String startWorkTime;//开始时间
    private String endWorkTime;//结束时间
    private String mark;//标识
    private LocalDateTime startRealTime;//班次开始时间
    private LocalDateTime endRealTime;//班次结束时间
}
