package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/** 仓库已绑定业务设备分页查询。 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WarehouseBoundDevicePageParam extends Paging {
    /** 智慧仓库粮仓 id。 */
    private Long warehouseId;
    /** 产品 id 集合。 */
    private Set<Long> productIds;
    /** 区域 id 集合，按设备最末级区域匹配。 */
    private Set<Long> areaIds;
    /** 设备名称，模糊匹配。 */
    private String deviceName;
}
