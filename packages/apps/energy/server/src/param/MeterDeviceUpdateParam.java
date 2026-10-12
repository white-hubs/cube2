package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MeterDeviceUpdateParam {
    private Long id;
    private BigDecimal value;
}
