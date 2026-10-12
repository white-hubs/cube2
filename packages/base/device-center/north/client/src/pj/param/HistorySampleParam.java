package com.guhecloud.cube.iot.north.bridge.client.pj.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 设备属性时序历史对象
 *
 * @author mioopp
 */
@Data
public class HistorySampleParam {

    @NotNull
    private String deviceId;

    @NotNull
    private List<String> properties;

    private String order;

    private Long startTs;

    private Long endTs;

    /**
     * 时间分片粒度,单位分钟
     */
    private Integer step = 1;

    /**
     * 聚合类型<br/>
     * min,max,count,sum,avg,any
     */
    private String aggType = "raw";

    /**
     * 最大结果数
     */
    private Integer maxRecords = 1000;
}
