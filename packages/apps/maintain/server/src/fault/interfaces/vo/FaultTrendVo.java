package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FaultTrendVo {
    private Long num;
    private String timeFlag;
}
