package com.guhecloud.cube.iot.mgt.core.data.vo.timeseries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceSamplingHistoryResultVo {

    /**
     * propCode -> DevicePropHistoryResultVo
     */
    private Map<String, DevicePropHistoryResultVo> propValues;

    /**
     * 时间戳
     */
    private long ts;
}
