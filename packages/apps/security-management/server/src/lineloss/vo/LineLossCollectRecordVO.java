package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LineLossCollectRecordVO {
    private Long deviceId;
    private Integer tenant;
    private LocalDateTime collectTime;
    private BigDecimal value;
}
