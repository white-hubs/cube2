package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/** 可关联业务设备分页查询。 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplicationRelatedDevicePageParam extends Paging {
    /** 产品 id 集合。 */
    private Set<Long> productIds;
    /** 区域 id 集合，按设备最末级区域匹配。 */
    private Set<Long> areaIds;
    /** 设备名称，模糊匹配。 */
    private String deviceName;
}
