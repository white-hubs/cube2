package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.BigDecimalSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class DataProjectVo {
    private String projectId;

    private String areaId;

    private String projectName;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant time;
    //占比
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal proportion;

    //设备类型
    private String instrumentType;

}
