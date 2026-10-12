package com.guhecloud.cube.base.device.center.metadata.std.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class CustomerTagPageParam extends Paging {
    /**
     * 时间范围
     */
    private Period period;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 设备id
     */
    private Long deviceId;
}
