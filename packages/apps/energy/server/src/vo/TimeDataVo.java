package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeDataVo {
    private String begin;
    private String end;
    private String oldBegin;
    private String oldEnd;
    private Integer timeDifference;
    private String tableFlag;
    private LocalDateTime st;
    private LocalDateTime et;
    private LocalDateTime ost;
    private LocalDateTime oet;
    private String qoqBegin;
    private String qoqEnd;
}
