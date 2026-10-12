package com.guhecloud.cube.iot.mgt.north.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 方法调用历史查询参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MethodInvokeHistoryParam extends Paging {

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 方法ID
     */
    private String methodCode;

    /**
     * 开始时间戳(毫秒)
     */
    private Long startTs;

    /**
     * 结束时间戳(毫秒)
     */
    private Long endTs;

    /**
     * 排序方式(默认降序)
     */
    private String order = "desc";
}
