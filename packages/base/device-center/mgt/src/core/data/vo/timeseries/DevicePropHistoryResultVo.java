package com.guhecloud.cube.iot.mgt.core.data.vo.timeseries;

import lombok.Data;

@Data
public class DevicePropHistoryResultVo {

    /**
     * 设备ID（多设备查询时使用）
     */
    private String deviceId;

    private String propCode;

    private String date;

    private long ts;

    /**
     * 属性值
     */
    private Object value;

    /**
     * 属性值以String方式给出
     */
    private String valueOfStr;

    /**
     * 翻译后的值
     */
    private String valueOfTranslate;
}
