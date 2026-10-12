package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppointmentIdleVo {
    //会议室名称
    private String conferenceName;

    //空闲率
    private BigDecimal idleRate;
}
