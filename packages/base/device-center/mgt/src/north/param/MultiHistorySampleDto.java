package com.guhecloud.cube.iot.mgt.north.param;

import lombok.Data;

/**
 * 多设备-属性时序历史采样查询参数
 * <p>
 * devIdAndPropCodes 格式：deviceId.propCode,deviceId.propCode...
 */
@Data
public class MultiHistorySampleDto {

    /**
     * deviceId.propCode,deviceId.propCode...
     */
    private String devIdAndPropCodes;

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

