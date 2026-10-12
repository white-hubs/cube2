package com.guhecloud.cube.biz.adapter.park.server.application.dahua.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 大华入场事件入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DaHuaParkingInRecordParam extends DaHuaParkingRecordParam {
    /**
     * 入场图片URL地址
     */
    @JsonProperty("carImgUrl")
    private String inUrl;

    /**
     * 入场图片(base64)
     */
    private String inImg;

    /**
     * 入场时间 yyyyMMddHHmmss
     */
    private String arriveTime;
}
