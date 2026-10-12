package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class DataTimeFlagVo {
    private String timeFlag;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant nowReadeTime;
    //设备类型
    private String instrumentType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant time;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal proportion;

    private LocalDateTime periodTime;
    private String key;
}
