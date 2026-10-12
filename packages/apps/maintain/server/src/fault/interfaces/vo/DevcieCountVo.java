package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DevcieCountVo {
    private BigDecimal num;
    private Long deviceId;
}
