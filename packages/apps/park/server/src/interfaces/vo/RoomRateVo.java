package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomRateVo {
    private Long areaId;
    private String areaName;
    private BigDecimal num;
}
